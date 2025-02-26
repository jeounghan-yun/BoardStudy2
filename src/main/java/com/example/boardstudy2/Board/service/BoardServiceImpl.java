package com.example.boardstudy2.Board.service;

import com.example.boardstudy2.Utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.boardstudy2.Board.dao.BoardDAO;
import com.example.boardstudy2.common.Common;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service("boardService")
public class BoardServiceImpl implements BoardService{
    @Autowired
    private BoardDAO boardDAO;

    @Autowired
    private FileUtil fileUtil;

    @Value("${part4.upload.path}")
    private String uploadDir;

    @Value("${spring.servlet.multipart.location}")
    private String tempDir;

    /**
     * 게시물 리스트
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> SelectBoardList(Map<String, Object> map) throws Exception {

        String startDate = (String) map.get("startDate");
        String endDate   = (String) map.get("endDate");

        // 시작일과 종료일이 비어있지않다면(존재한다면) "-"를 없애주고 넣는다. (시작일과 종료일은 같이 다녀야 됨.)
        map.put("startDate", startDate.replace("-", ""));
        map.put("endDate"  , endDate.replace("-"  , ""));


        return boardDAO.selectBoardList(map);
    }

    /**
     * 게시판 등록
     * @param map
     * @throws Exception
     */
    public String InsertBoardData(Map<String, Object> map) throws Exception {
        String result = "SUCCESS";
        List<String> originalFileNames = new ArrayList<>(); // 오리지날 파일명 리스트
        List<String> uniqueFileNames   = new ArrayList<>(); // 유니크 파일명 리스트

        try {
            // 파일 존재 확인
            if(Common.fileCheck(tempDir)){
                map.put("fileYn", "Y");
                boardDAO.InsertBoardData(map);

                fileUtil.UploadFile(map);
                if("SUCCESS".equals(map.get("result"))){
                    originalFileNames = (List<String>) map.get("originalFileNames");
                    uniqueFileNames   = (List<String>) map.get("uniqueFileNames");

                    int mseq = (Integer) map.get("rseq");

                    if(originalFileNames.size() > 0){
                        map.put("mseq", mseq);                  // 게시물 seq를 file의 상위 번호로 넣어줌.
                        map.put("flph", mseq + "/");

                        for(int i = 0 ; i < originalFileNames.size() ; i++) {
                            map.put("originalFileNames", originalFileNames.get(i));
                            map.put("uniqueFileNames"  , uniqueFileNames.get(i));

                            boardDAO.InsertFileData(map);              // 파일 DB에 저장
                        }
                    } else {
                        result = "ERROR";
                    }
                } else {
                    result = "ERROR";
                }
            } else {
                map.put("fileYn", "N");
                boardDAO.InsertBoardData(map);
            }
        } catch (Exception e) {
            result = "ERROR";
        } finally{
            return result;
        }
    }

    /**
     * 게시물 상세 데이터
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> BoardDetailData(Map<String, Object> map) throws Exception {
        boardDAO.BoardReadCnt(map);

        List<Map<String, Object>> resultMap = new ArrayList<>();

        resultMap = boardDAO.BoardDetailData(map);

        Map<String, Object> firstData = resultMap.get(0);
        String fileYn = (String) firstData.get("fileYn");

        if("E".equals(map.get("boardMode")) && "Y".equals(fileYn)){
            fileUtil.GetUploadFile(resultMap);
        }
        return resultMap;
    }

    /**
     * 게시물 삭제
     * @param map
     * @return
     * @throws Exception
     */
    public String BoardDelData(Map<String, Object> map) throws Exception {
        String result = "SUCCESS";

        try {
            int resultInt = boardDAO.BoardDelData(map);

            if(Common.isEmpty(map.get("SEQ")) || resultInt < 1){ // SEQ를 먼저 체크하고 삭제 로직으로 들어간다.
                result = "ERROR";
            }
        } catch (Exception e) {
            result = "ERROR";
        } finally {
            return result;
        }
    }

    /**
     * 게시물 수정
     * @param map
     * @return
     * @throws Exception
     */
    public String BoardEditData(Map<String, Object> map) throws Exception {
        String result                  = "SUCCESS";
        String seq                     = (String) map.get("SEQ");   // 시퀀스
        String delFileNames            = (String) map.get("delFileNames");
        String addFileNames            = (String) map.get("addFileNames");
        List<String> delFileNameList   = Arrays.asList(delFileNames.split(","));
        List<String> addFileNameList   = Arrays.asList(addFileNames.split(","));
        List<String> originalFileNames = new ArrayList<>(); // 오리지날 파일명 리스트
        List<String> uniqueFileNames   = new ArrayList<>(); // 유니크 파일명 리스트

        try{
            int resultInt = boardDAO.BoardEditData(map);

            map.put("delFileNames", delFileNameList);
            map.put("addFileNames", addFileNameList);

            if(!Common.isEmpty(delFileNames)){
                boardDAO.DeleteFileData(map);
                fileUtil.DelFile(map);
            }

            fileUtil.addMoveFile(map);
            originalFileNames = (List<String>) map.get("originalFileNames");
            uniqueFileNames   = (List<String>) map.get("uniqueFileNames");
            map.put("mseq"  , map.get("SEQ"));
            map.put("fileYn", map.get("Y"));
            map.put("flph"  , seq + "/");

            if(!Common.isEmpty(addFileNames)){
                for(int i = 0 ; i < addFileNameList.size(); i++) {
                    map.put("originalFileNames", originalFileNames.get(i));
                    map.put("uniqueFileNames"  , uniqueFileNames.get(i));

                    boardDAO.InsertFileData(map);              // 파일 DB에 저장
                }
            }

            if(Common.isEmpty(map.get("SEQ")) || resultInt != 1){
                result = "ERROR";
            }
        } catch (Exception e) {
            result = "ERROR";
        } finally {
            return result;
        }
    }
}

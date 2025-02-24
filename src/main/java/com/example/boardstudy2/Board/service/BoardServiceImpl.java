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
            if(Common.FileCheck(tempDir)){
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
            fileUtil.getUploadFile(map);
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
        String result = "SUCCESS";
        
        try{
            int resultInt = boardDAO.BoardEditData(map);

            // 파일 수정
            fileUtil.editUplodFile(map);

            String fileNames = (String) map.get("fileNames");
            List<String> fileNameList = Arrays.asList(fileNames.split(","));
            map.put("fileNames", fileNameList);

            boardDAO.boardFileEdit(map);

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

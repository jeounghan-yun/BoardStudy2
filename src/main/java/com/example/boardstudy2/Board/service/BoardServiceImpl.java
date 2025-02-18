package com.example.boardstudy2.Board.service;

import com.example.boardstudy2.Utils.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.boardstudy2.Board.dao.BoardDAO;
import com.example.boardstudy2.common.Common;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        String userId = (String) map.get("userId");
        List<String> originalFileNames = new ArrayList<>(); // 오리지날 파일명 리스트
        List<String> uniqueFileNames   = new ArrayList<>(); // 유니크 파일명 리스트
//        int resultBoardInt; // 게시판 글 성공 여부
//        int resultFileInt;  // 게시물 파일 등록 성공 여부

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
                        map.put("flph", userId + "/" + mseq + "/");

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
        return boardDAO.BoardDetailData(map);
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
            } else {
                fileUtil.FileDelete(map);
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

            if(Common.isEmpty(map.get("SEQ")) || resultInt != 1){ // SEQ를 먼저 체크하고 삭제 로직으로 들어간다.
                result = "ERROR";
            }
        } catch (Exception e) {
            result = "ERROR";
        } finally {
            return result;
        }
    }
}

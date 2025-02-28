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

        // 시작일과 종료일이 비어있지않다면(존재한다면) "-"를 없애주고 넣음.
        map.put("startDate", startDate.replace("-", ""));
        map.put("endDate"  , endDate.replace("-"  , ""));

        return boardDAO.SelectBoardList(map);
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
            Common.XSSReplace(map); // XSSReplace 메서드 호출

            // 파일 존재 시
            if(Common.fileCheck(tempDir)){
                map.put("fileYn", "Y");
                boardDAO.InsertBoardData(map); // 게시물 등록, mseq 리턴 받음.
                fileUtil.UploadFile(map);      // 파일 처리 및 이동

                //파일 업로드 성공 시
                if("SUCCESS".equals(map.get("result"))){
                    originalFileNames = (List<String>) map.get("originalFileNames"); // 원본파일명 리스트
                    uniqueFileNames   = (List<String>) map.get("uniqueFileNames");   // 유일파일명 리스트

                    int mseq = (Integer) map.get("rseq");

                    // 등록할 원본 파일명 리스트가 존재한다면 파일 DB등록
                    if(originalFileNames.size() > 0){
                        map.put("mseq", mseq);        // 게시물 seq를 file의 상위 번호로 넣어줌.
                        map.put("flph", mseq + "/");

                        // 등록할 파일명 만큼 반복
                        for(int i = 0 ; i < originalFileNames.size() ; i++) {
                            map.put("originalFileNames", originalFileNames.get(i));
                            map.put("uniqueFileNames"  , uniqueFileNames.get(i));

                            boardDAO.InsertFileData(map); // 파일 DB에 등록
                        }
                    } else {
                        result = "ATTA"; // 첨부파일 실패
                    }
                } else {
                    result = "ERROR";
                }

                // ERROR 발생 시 게시물 및 파일 삭제
                if ("ERROR".equals(result)) {
                    boardDAO.regDelData(map);
                }
            // 파일 존재하지 않을 시
            } else {
                map.put("fileYn", "N");
                boardDAO.InsertBoardData(map);
            }
        } catch (Exception e) {
            result = "ERROR";
            boardDAO.regDelData(map);
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
        List<Map<String, Object>> resultMap = new ArrayList<>();

        // 게시물 클릭 시 조회수부터 +1 증가
        boardDAO.BoardReadCnt(map);

        // 상세 데이터 가져오기
        resultMap = boardDAO.BoardDetailData(map);

        Map<String, Object> firstData = resultMap.get(0); // resultMap의 첫 데이터의 파일 여부를 가져옴.
        String fileYn = (String) firstData.get("fileYn");

        // 수정 페이지이면서 파일이 존재할 시 로직
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
        map.put("result", result);

        try {
            int resultInt = boardDAO.BoardDelData(map);

            if(Common.isEmpty(map.get("SEQ")) || resultInt < 1){
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
     * 1. SEQ가 존재하는지 확인
     * 2. 삭제 파일명 리스트 존재 시 파일 실제 파일 삭제 진행
     * 3. 파일 삭제 처리 성공 시 파일 DB 삭제
     *
     * 4. 삭제 or 추가 파일명 리스트 존재 시.
     *    파일 처리/추가 및 파일 이동
     *
     * 5. 파일 추가 및 이동 처리 성공 및 추가 파일이 존재할 시.
     *    파일 DB 추가
     * @param map
     * @return
     * @throws Exception
     */
    public String BoardEditData(Map<String, Object> map) throws Exception {
        String result                  = "SUCCESS";
        String seq                     = (String) map.get("SEQ");
        String delFileNames            = (String) map.get("delFileNames");
        String addFileNames            = (String) map.get("addFileNames");
        List<String> delFileNameList   = Arrays.asList(delFileNames.split(",")); // 삭제 파일명 리스트
        List<String> addFileNameList   = Arrays.asList(addFileNames.split(",")); // 추가 파일명 리스트
        List<String> originalFileNames = new ArrayList<>();                            // 오리지날 파일명 리스트
        List<String> uniqueFileNames   = new ArrayList<>();                            // 유니크 파일명 리스트

        try{
            // 1. SEQ가 존재하는지 확인
            if(!Common.isEmpty(map.get("SEQ"))) {
                map.put("delFileNames", delFileNameList);
                map.put("addFileNames", addFileNameList);

                // 2. 삭제 파일명 리스트 존재 시 파일 실제 파일 삭제 진행
                if (!Common.isEmpty(delFileNames)) {
                    result = fileUtil.DelFile(map);   // 디렉터리 파일 삭제 처리
                    // 3. 파일 삭제 처리 성공 시 파일 DB 삭제
                    if ("SUCCESS".equals(result)) {
                        boardDAO.DeleteFileData(map);
                    }
                }

                // 4. 삭제 or 추가 파일명 리스트 존재 시.
                // 파일 처리/추가 및 파일 이동, DB 수정 처리
                if (!Common.isEmpty(delFileNameList) || !Common.isEmpty(addFileNameList)) {
                    fileUtil.AddMoveFile(map); // 디렉터리 파일 추가 및 이동 처리

                    // 5. 디렉터리 파일 추가 및 이동 처리 성공 및 추가 파일이 존재할 시.
                    if("SUCCESS".equals(map.get("result")) && !Common.isEmpty(addFileNames)) {
                        originalFileNames = (List<String>) map.get("originalFileNames");
                        uniqueFileNames   = (List<String>) map.get("uniqueFileNames");
                        map.put("mseq", map.get("SEQ"));
                        map.put("flph", seq + "/");

                        for (int i = 0; i < originalFileNames.size(); i++) {
                            map.put("originalFileNames", originalFileNames.get(i));
                            map.put("uniqueFileNames"  , uniqueFileNames.get(i));
                            boardDAO.InsertFileData(map); // 파일 DB 저장
                        }
                    }
                }
                // 6. 게시물 수정
                Common.XSSReplace(map);
                boardDAO.BoardEditData(map);
            } else {
                // SEQ 존재하지 않을 시 ERROR
                result = "ERROR";
            }

        } catch (Exception e) {
            result = "ERROR";
        } finally {
            return result;
        }
    }
}

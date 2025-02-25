package com.example.boardstudy2.Board.service;

import java.util.List;
import java.util.Map;

public interface BoardService {
    /**
     * 게시물 리스트
     *
     * @param map
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> SelectBoardList (Map<String, Object> map) throws Exception;

    /**
     * 게시물 등록
     * @param map
     * @throws Exception
     */
    String InsertBoardData(Map<String, Object> map) throws Exception;

    /**
     * 게시물 상세 데이터
     * @param map
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> BoardDetailData(Map<String, Object> map) throws Exception;

    /**
     * 게시물 삭제
     * @param map
     * @return
     * @throws Exception
     */
    String BoardDelData(Map<String, Object> map) throws Exception;

    /**
     * 게시물 수정
     * @param map
     * @return
     * @throws Exception
     */
    String BoardEditData(Map<String, Object> map) throws Exception;
}

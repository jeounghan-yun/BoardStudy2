package com.example.boardstudy2.Board.dao;

import org.springframework.stereotype.Repository;
import com.example.boardstudy2.Utils.CommonMapper;

import java.util.List;
import java.util.Map;

@Repository("boardDAO")
public class BoardDAO  extends CommonMapper {
    /**
     * 게시물 리스트
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectBoardList(Map<String, Object> map) throws Exception {
        return (List<Map<String, Object>>) selectPagingList("mapper.board.selectBoardList", map);
    }

    /**
     * 게시물 등록
     * @param map
     * @throws Exception
     */
    public int InsertBoardData(Map<String, Object> map) throws Exception{
        return (int) insert("mapper.board.insertBoardData", map);
    }

    /**
     * 게시물 파일 등록
     * @param map
     * @return
     * @throws Exception
     */
    public int InsertFileData(Map<String, Object> map) throws Exception{
        return (int) insert("mapper.board.InsertFileData", map);
    }

    /**
     * 게시물 상세 데이터
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> BoardDetailData(Map<String, Object> map) throws Exception {
        return (List<Map<String, Object>>) selectList("mapper.board.boardDetailData", map);
    }

    /**
     * 조회수 증가
     * @param map
     * @throws Exception
     */
    public void BoardReadCnt(Map<String, Object> map) throws Exception {
        update("mapper.board.boardReadCnt", map);
    }

    /**
     * 게시물 삭제
     * @param map
     * @throws Exception
     */
    public int BoardDelData(Map<String, Object> map) throws Exception {
        return (int) delete("mapper.board.boardDelData", map);
    }

    /**
     * 게시물 수정
     * @param map
     * @throws Exception
     */
    public int BoardEditData(Map<String, Object> map) throws Exception {
        return (int) update("mapper.board.boardEditData", map);
    }

    /**
     * 게시물 파일 삭제
     * @param map
     * @throws Exception
     */
    public void DeleteFileData(Map<String, Object> map) throws Exception {
        update("mapper.board.DeleteFileData", map);
    }
}

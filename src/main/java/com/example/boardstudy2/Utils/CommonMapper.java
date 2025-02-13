package com.example.boardstudy2.Utils;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.example.boardstudy2.common.Common;

import java.util.Map;

@Component
public class CommonMapper {
    @Autowired //의존성 주입
    @Qualifier("sqlSessionTemplate") // 지정한 한정자 값이 been객체가 존재해야한다.(been 확인 완료)
    private SqlSessionTemplate sqlSessionTemplate; //SqlSessionFactory를 객체로 가져온 함수 sqlSeesionTemplate클래스를 가져옴

    //추가
    public Object insert (String queryId, Object params){
        return sqlSessionTemplate.insert(queryId, params);
    }

    /**
     * 수정 조건 존재
     * @param queryId
     * @param params
     * @return
     */
    public Object update(String queryId, Object params){
        return sqlSessionTemplate.update(queryId, params);
    }

    /**
     * 삭제 조건 존재
     * @param queryId
     * @param params
     * @return
     */
    public Object delete(String queryId, Object params){
        return sqlSessionTemplate.delete(queryId, params);
    }

    /**
     * 검색
     * @param queryId
     * @param params
     * @return
     */
//    public Object select(String queryId, Object params){ return sqlSessionTemplate.selectOne(queryId, params); }

    /**
     * 1개 데이터 검색 조건 X
     * @param queryId
     * @return
     */
    public Object selectOne(String queryId){
        return sqlSessionTemplate.selectOne(queryId);
    }

    /**
     * 1개 데이터 검색 조건 존재
     * @param queryId
     * @param params
     * @return
     */
    public Object selectOne(String queryId, Object params){
        return sqlSessionTemplate.selectOne(queryId, params);
    }

    /**
     * 검색 리스트 조건 존재
     * @param queryId
     * @param params
     * @return
     */
    public Object list(String queryId, Object params){
        return sqlSessionTemplate.selectList(queryId, params);
    }

    /**
     * 검색 리스트 조건 존재 X
     * @param queryId
     * @return
     */
    public Object list(String queryId){
        return sqlSessionTemplate.selectList(queryId);
    }

    /**
     * 리스트 검색
     * @param queryId
     * @param params
     * @return
     */
    public Object selectList(String queryId, Object params){ return sqlSessionTemplate.selectList(queryId, params); }

    /**
     * 페이징 리스트 검색
     * @param queryId
     * @param params
     * @return
     */
    public Object selectPagingList(String queryId, Object params){
        // map 만들기
        Map<String, Object> map = (Map<String, Object>) params;

        String listNum = (String) map.get("pagingListCount");
        // 현재 페이지 번호(map으로 가져오기)
        String pageNum = (String) map.get("page");         // 현재 페이지 번호

        // 현재 페이지 기본값 DB에서 0인덱스 부터 불러오기 때문
        int currentPage = 0;
        // 한 페이지에 보여줄 행의 수 기본값
        int listCnt = 5;
        
        // 한 페이지에 보여주는 리스트의 개수가 존재하면 업데이트
        if (Common.isEmpty(listNum) == false) {
            listCnt = Integer.parseInt(listNum);
        }

        // 현재 기억될 페이지가 존재하면 업데이트
        if (Common.isEmpty(pageNum) == false) {
            currentPage = Integer.parseInt(pageNum) - 1;
        }

        map.put("START", (currentPage * listCnt));
        map.put("END"  , listCnt);

        // 마지막 페이지 구하는 공식
        return sqlSessionTemplate.selectList(queryId, map);
    }
}

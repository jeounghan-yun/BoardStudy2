package com.example.boardstudy2.Login.dao;

import com.example.boardstudy2.Utils.CommonMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("LoginDAO")
public class LoginDAO extends CommonMapper {

    /**
     * ID 중복 체크
     * @param map
     * @return
     * @throws Exception
     */
    public int IDCheck(Map<String, Object> map) throws Exception {
        return (int) selectOne("mapper.login.IDCheck", map);
    }

    /**
     * 회원가입
     * @param map
     * @return
     * @throws Exception
     */
    public int InsertSignUp(Map<String, Object> map) throws Exception {
        return (int) insert("mapper.login.InsertSignUp", map);
    }
}

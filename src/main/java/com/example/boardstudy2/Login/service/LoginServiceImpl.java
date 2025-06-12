package com.example.boardstudy2.Login.service;

import com.example.boardstudy2.Login.dao.LoginDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("LoginService")
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDAO loginDAO;

    /**
     * ID 중복 체크
     * @param map
     * @return
     * @throws Exception
     */
    public String IDCheck(Map<String, Object> map) throws Exception{
        String result = "SUCCESS";
        try {
            loginDAO.IDCheck(map);
        } catch (Exception e) {
            result = "ERROR";
        }

        return result;
    }
    
    /**
     * 회원가입
     * @param map
     * @return
     * @throws Exception
     */
    public String InsertSignUp(Map<String, Object> map) throws Exception{

        String result = "SUCCESS";
        try {
            loginDAO.InsertSignUp(map);
        } catch (Exception e) {
            result = "ERROR";
        }

        return result;
    }

}

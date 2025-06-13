package com.example.boardstudy2.Login.service;

import com.example.boardstudy2.Login.dao.LoginDAO;
import com.example.boardstudy2.common.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            if(loginDAO.IDCheck(map) > 0) {
                result = "ERROR";
            }
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
        String result   = "SUCCESS";
        String passWord = map.get("userPw2").toString(); // 비밀번호

        try {
            // 암호화
            if(!Common.isEmpty(map.get("userPw2"))) {
                map.put("userPw2", (String) Common.encrypt(passWord));
            }
            loginDAO.InsertSignUp(map);
        } catch (Exception e) {
            result = "ERROR";
        }

        return result;
    }

    /**
     * 로그인
     * @param map
     * @return
     * @throws Exception
     */
    public String SelectSignIn(Map<String, Object> map) throws Exception{
        String result = "ERROR";
        String passWord = "";

        passWord = loginDAO.SelectSignIn(map);
        // 복호화
        if(!Common.isEmpty(passWord)) {
            String passWordChack = Common.decrypt(passWord);

            //비밀번호 맞는지 확인
            if(passWordChack.equals((String) map.get("userPw1"))) {
                result = "SUCCESS";
            }
        }
        return result;
    }
}

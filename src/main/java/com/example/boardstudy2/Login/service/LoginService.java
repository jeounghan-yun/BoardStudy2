package com.example.boardstudy2.Login.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface LoginService {
    /**
     * ID 중복 체크
     * @param map
     * @return
     * @throws Exception
     */
    String IDCheck (Map<String, Object> map) throws Exception;

    /**
     * 회원가입
     * @param map
     * @return
     * @throws Exception
     */
    String InsertSignUp (Map<String, Object> map) throws Exception;
}

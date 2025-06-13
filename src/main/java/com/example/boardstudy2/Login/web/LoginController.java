package com.example.boardstudy2.Login.web;

import com.example.boardstudy2.Login.service.LoginService;
import com.example.boardstudy2.Utils.CommandMap;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/Login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 로그인창 기본
     * @param commandMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/Login")
    public ModelAndView Login(CommandMap commandMap, HttpServletRequest request) throws Exception {
        ModelAndView mv  = new ModelAndView("jsonView");
        mv.setViewName("/Login/Login");

        return mv;
    }

    /**
     * ID 중복 체크
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/idCheck")
    public ModelAndView IDCheck(CommandMap commandMap) throws Exception {
        ModelAndView mv  = new ModelAndView("jsonView");

        mv.addObject("errCode", loginService.IDCheck(commandMap.getMap()));

        return mv;
    }

    /**
     * 회원가입
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/SignUp")
    public ModelAndView InsertSignUp(CommandMap commandMap) throws Exception {
        ModelAndView mv  = new ModelAndView("jsonView");

        mv.addObject("errCode", loginService.InsertSignUp(commandMap.getMap()));

        return mv;
    }

    /**
     * 로그인
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/SignIn")
    public ModelAndView SelectSignIn(CommandMap commandMap) throws Exception {
        ModelAndView mv  = new ModelAndView("jsonView");

        mv.addObject("errCode", loginService.SelectSignIn(commandMap.getMap()));

        return mv;
    }
}

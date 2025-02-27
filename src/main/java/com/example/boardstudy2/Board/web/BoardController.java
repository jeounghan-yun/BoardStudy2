package com.example.boardstudy2.Board.web;

import com.example.boardstudy2.Utils.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.example.boardstudy2.Board.service.BoardService;
import com.example.boardstudy2.Utils.CommandMap;
import com.example.boardstudy2.common.Common;

import java.io.File;
import java.util.List;
import java.util.Map;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private FileUtil fileUtil;

    /**
     * 게시판 기본
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping (value="/")
    public ModelAndView Board(CommandMap commandMap, HttpServletRequest request) throws Exception{
        ModelAndView mv  = new ModelAndView("jsonView");

        String boardMode = request.getParameter("boardMode");
        String errCode   = "SUCCESS";

        if (errCode.equals("SUCCESS")){
            if(Common.isEmpty(boardMode)) { // boardMode 데이터가 없다면
                boardMode = "L";            // 기본 "L"을 넣는다.
            }
            mv.setViewName("Board");
        } else {
            errCode = "ERROR";
        }

        if (!"L".equals(boardMode)) {
            errCode = "ERROR";
        }

        mv.addObject("boardMode"      , boardMode);                         // boardMode를 mv에 담아서 보내줌.
        mv.addObject("SEQ"            , commandMap.get("SEQ"));             // 시퀀스
        mv.addObject("regId"          , commandMap.get("regId"));           // 등록자
        mv.addObject("srchTitle"      , commandMap.get("srchTitle"));       // 검색 제목
        mv.addObject("srchReg"        , commandMap.get("srchReg"));         // 검색 등록자
        mv.addObject("startDate"      , commandMap.get("startDate"));       // 검색 시작일
        mv.addObject("endDate"        , commandMap.get("endDate"));         // 검색 종료일
        mv.addObject("pagingListCount", commandMap.get("pagingListCount")); // 한 페이지 당 출력되는 리스트 개수
        mv.addObject("numCount"       , commandMap.get("numCount"));        // 리스트 목록 넘버

        if("N".equals(commandMap.get("useYn"))){
            mv.addObject("useYn", "N");
        } else {
            mv.addObject("useYn", "Y");
        }

        // 페이지 번호
        if(Common.isEmpty(commandMap.get("page"))) {
            mv.addObject("page", 1);
        } else {
            mv.addObject("page", commandMap.get("page"));
        }

        mv.addObject("errCode", errCode);

        return mv;
    }

    /**
     * 게시물 리스트
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/Board/BoardList")
    public ModelAndView BoardList(CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");

        List<Map<String, Object>> boardList = boardService.SelectBoardList(commandMap.getMap());  // 목록 리스트

        mv.addObject("list", boardList);
        return mv;
    }

    /**
     * 게시물 등록
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/Board/BoardReg")
    public ModelAndView InsertBoardData (CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");

        String errCode = boardService.InsertBoardData(commandMap.getMap());

        mv.addObject("errCode", errCode);

        return mv;
    }

    /**
     * 게시물 상세 데이터
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/Board/BoardDetail")
    public ModelAndView BoardDetail (CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");

        mv.addObject("DetailData", boardService.BoardDetailData(commandMap.getMap()));

        return mv;
    }

    /**
     * 게시물 삭제
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/Board/BoardDelData")
    public ModelAndView BoardDelData (CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");

        String errCode = boardService.BoardDelData(commandMap.getMap());

        mv.addObject("errCode", errCode);

        return mv;
    }

    /**
     * 게시물 수정
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/Board/BoardEdit")
    public ModelAndView BoardEditData (CommandMap commandMap) throws Exception{
        ModelAndView mv = new ModelAndView("jsonView");

        String errCode = boardService.BoardEditData(commandMap.getMap());

        mv.addObject("errCode", errCode);

        return mv;
    }

}




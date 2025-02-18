<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js" type="text/javascript"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"                          type="text/javascript"></script>
<script src="assets/js/common.js"                                              type="text/javascript"></script>

<script>
    var boardMode       = "${boardMode}";
    var SEQ             = "${SEQ}";
    var srchTitle       = "${srchTitle}";
    var srchReg         = "${srchReg}";
    var regId           = "${regId}";
    var startDate       = "${startDate}";
    var endDate         = "${endDate}";
    var pagingListCount = "${pagingListCount}";
    var page            = "${page}";
    var numCount        = "${numCount}"
    var useYn           = "${useYn}";
    var listCnt;

    $(document).ready(function () {
    })

    insertSelectBox = function () {
        if(pagingListCount == ""){
            pagingListCounts();
        } else {
            $("#pagingListCount").val(pagingListCount).prop("selected", true); //분기 처리
        }

        $("#srchTitle").val(srchTitle);
        $("#srchReg").val(srchReg);
        $("#startDate").val(startDate);
        $("#endDate").val(endDate);
    }

    /**
     * 검색시 검색어 기억
     */
    onSearch = function () {
        var searchYn = "Y"

        // 검색 창 제한 두기 (수정)
        if($("#startDate").val() > $("#endDate").val() && !isNull($("#endDate").val())){
            alert("시작일이 종료일보다 큽니다.");
            searchYn = "N";
        }

        if("Y".equals(searchYn)) {
            srchTitle       = $("#srchTitle").val();
            srchReg         = $("#srchReg").val();
            startDate       = $("#startDate").val();
            endDate         = $("#endDate").val();
            pagingListCount = $("#pagingListCount option:selected").val();

            page = 1;
            getListData();
        }
    }

    /**
     * 검색 시 검색어 가지고 페이징
     * @param pageNum
     */
    getListPage = function (pageNum) {
        page = pageNum;  // 현재 페이지 업데이트
        getListData();
    }

    /**
     * 상세 페이지 이동
     * @param seq
     * @constructor
     */
    BoardViewPage = function (seq, regId, page, numCount) {
        var params = {
            boardMode : "V",
            SEQ       : seq,
            regId     : regId,
            page      : page,
            numCount  : numCount
        }
        gotoPage(params);
    }

    /**
     * 등록 페이지 이동
     * @constructor
     */
    BoardRegPage = function (page) {
        var params = {
            boardMode : "W",
            page      : page,
        }
        gotoPage(params);
    }

    /**
     * 리스트 페이지 이동
     * @constructor
     */
    BoardListPage = function (page, useYn) {
        var params = {
              boardMode : "L",
              page      : page,
              useYn     : useYn
        }
        gotoPage(params);
    }

    /**
     * 수정 페이지 이동
     * @constructor
     */
    BoardEditPage = function (page) {
        var params = {
            boardMode : "E",
            SEQ       : SEQ,
            page      : page
        }
        gotoPage(params);
    }

    /**
     * 값 담아서 Submit
     * @param params
     */
    gotoPage = function (params) {
        var comSubmit = new ComSubmit();

        comSubmit.setUrl('/');

        comSubmit.addParam("boardMode"      , params.boardMode);
        comSubmit.addParam("SEQ"            , params.SEQ);
        comSubmit.addParam("regId"          , params.regId);

        comSubmit.addParam("page"           , params.page);

        comSubmit.addParam("srchTitle"      , srchTitle);
        comSubmit.addParam("srchReg"        , srchReg);
        comSubmit.addParam("startDate"      , startDate);
        comSubmit.addParam("endDate"        , endDate);
        comSubmit.addParam("pagingListCount", pagingListCount);
        comSubmit.addParam("useYn"          , params.useYn);
        comSubmit.addParam("numCount"       , params.numCount);

        comSubmit.submit();
    }
</script>
<body>
    <%--  헤더  --%>
    <%@include file="/WEB-INF/include/include-header.jspf"%>

    <%--등록/상세/리스트--%>
    <c:choose>
        <c:when test="${boardMode eq 'W' or boardMode eq 'E'}">
            <%@include file="/WEB-INF/jsp/Board/BoardReg.jsp"%>
        </c:when>
        <c:when test="${boardMode eq 'V'}">
            <%@include file="/WEB-INF/jsp/Board/BoardView.jsp"%>
        </c:when>
        <c:otherwise>
            <%@include file="/WEB-INF/jsp/Board/BoardList.jsp"%>
        </c:otherwise>
    </c:choose>

    <%-- comsubmit을 위한 form --%>
    <%@include file="/WEB-INF/include/include-body.jspf"%>
</body>

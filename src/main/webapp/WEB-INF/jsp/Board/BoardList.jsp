<%@page pageEncoding="utf-8"%>

<script type="text/javascript">
    $(document).ready(function () {
        // 한 페이지당 리스트 개수 옵션
        pagingListCounts();

        // 등록 페이지 등록 / 상세 페이지 게시물 삭제 시 검색어 초기화
        if("N".equals(useYn)) {
            initialize('frmSrchBoard');
            onSearch();
        }

        // 검색어 기억
        if("Y".equals(useYn)){
            insertSelectBox();
        }

        // 리스트 목록 출력
        // setInterval(function () {
            getListData();
        // }, 2000);

        // 한 페이지 당 출력되는 리스트 개수 변경 시 마다 기억
        $('#pagingListCount').on('change', function() {
            onSearch();
        });

        // datePicker 달력
        initializeDatePicker("#startDate, #endDate");
    })

    /**
     * 리스트 목록 출력
     */
    getListData = function () {
        $.ajax({
              type    : "POST"
            , url     : "/Board/BoardList"
            , data    : $("#frmSrchBoard").serialize() + "&page=" + page
            , success : function (data) {
                var data = data.list;
                console.log(data);
                listCnt = $("#pagingListCount").val();
                var params = {
                      currentPage : page
                    , totalCount  : data[0].totalCount
                    , pageCnt     : 10
                    , listCnt     : listCnt
                }
                paginNation(params);

                var str = "";
                numCount = page > 1 ? data[0].totalCount - (listCnt * (page - 1)) : data[0].totalCount;

                $("#totalCount").html("총 합계 : " + data[0].totalCount);
                $.each(data, function(k, v){
                    let fileYn = "";
                    // 파일 여부
                    fileYn = 'Y'.equals(v.fileYn) ? "<div class='fileYn'><img src='assets/images/file_icon.png' class='file-icon'></div>" : "<div class='fileYn'></div>";

                    str += "<div class='num'>"    + numCount  +"</div>";
                    str += "<div class='title'><a onClick='BoardViewPage("+ v.seq +", \"" + v.regId + "\", "+ page + ", "+ numCount + ");'>" + v.ttl     +"</a></div>";
                    str += fileYn;
                    str += "<div class='writer'>" + v.regId   +"</div>";
                    str += "<div class='date'>"   + v.regDate +"</div>";
                    str += "<div class='count'>"  + v.readCnt +"</div>";

                    numCount--;
                });

                $("#resultTable").html(str);
            }
        })
    }

    /**
     * 한 페이지당 리스트 개수 옵션
     */
    pagingListCounts = function () {
        var str = ""

        for (var i = 5; i <= 20; i += 5) {
            str += "<option>" + i + "</option>"
        }
        $("#pagingListCount").html(str);

    }

    /**
     * 페이지네이션
     * @param params
     */
    paginNation = function (params) {
        var currentPage = (parseInt(params.currentPage));    // 현재 페이지
        var totalCount  = (parseInt(params.totalCount));     // 전체 레코드 수
        var listCnt     = (parseInt(params.listCnt));        // 페이지당 레코드 수
        var pageCnt     = params.pageCnt;                    // 페이지 수

        var totalPage   = Math.ceil(totalCount / listCnt)    // 총 페이지 수
        var startPage   = Math.floor((currentPage - 1) / pageCnt) * pageCnt + 1; //시작 페이지
        var endPage     = startPage + pageCnt - 1;

        if(endPage > totalPage){
            endPage = totalPage;
        }

        var str = ""

        // 처음 페이지 이동
        if(1 != currentPage){
            str += "<a onClick='getListPage(1)' class='bt first'>" + '<<' + "</a>";
        } else {
            str += "<a href='javascript:;' class='bt first'>" + '<<' + "</a>";
        }
        // 이전 페이지 이동
        if(1 < currentPage){
            str += "<a onClick='getListPage("+ (currentPage - 1) +")' class='bt prev'>" + '<' + "</a>";
        } else {
            str += "<a href='javascript:;' class='bt prev'>" + '<' + "</a>";
        }

        // 페이지
        for(var i = startPage; i <= endPage; i++){
            if(i != currentPage) {
                str += "<a onClick='getListPage("+ i +")' class='num'>" + i + "</a>";
            } else {
                str += "<a href='javascript:;' class='num on'>" + i + "</a>";
            }
        }

        // 다음 페이지 이동
        if(totalPage != currentPage){
            str += "<a onClick='getListPage("+ (currentPage + 1) +")' class='bt next'>" + '>' + "</a>";
        } else {
            str += "<a href='javascript:;' class='bt next'>" + '>' + "</a>";
        }
        // 마지막 페이지 이동
        if(totalPage != currentPage){
            str += "<a onClick='getListPage("+ totalPage +")' class='bt last'>" + '>>' + "</a>";
        } else {
            str += "<a href='javascript:;' class='bt last'>" + '>>' + "</a>";
        }

        $("#paging").html(str);
    }

</script>
<body>
<div class="board_wrap">
    <div  class="right-align">
        <a href="/Login/Login">로그인</a>
    </div>
    <div>
        <hr style="border: solid 3px">
    </div>
    <div class="board_title">
        <strong>styler</strong>
    </div>
    <form id="frmSrchBoard" name="frmSrchBoard" onSubmit="return false">
        <div>
            <div>
                <%--제목--%>
                <span style="font-size: 13px; padding-left: 13.5px">제목</span><input style="width: 140px; height:20px" id="srchTitle" name="srchTitle" maxlength="30">
            </div>
            <%--등록자--%>
            <span style="font-size: 13px">등록자</span><input  style="width: 140px; height:20px" id="srchReg" name="srchReg" maxlength="12">
            <%--한 페이지 당 리스트 개수--%>
            <select style="float: right; width: 68px" id="pagingListCount" name="pagingListCount"></select>

            <div>
                <%--기간--%>
                <span style="font-size: 13px; padding-left: 10px">기간</span>
                <input style="width: 140px; height:20px" type="date" id="startDate" name="startDate" placeholder="시작일" maxlength="10"/>
                ~
                <input style="width: 140px; height:20px" type="date" id="endDate" name="endDate" placeholder="종료일" maxlength="10"/>
                <button style="width: 50px" class="bt_search" onClick="onSearch()">검색</button>
                <button style="width: 50px" class="bt_search" onClick="initialize('frmSrchBoard')">초기화</button>
                <span style="float: right; font-size: 13px" id="totalCount"></span>
            </div>
        </div>
    </form>

    <div class="board_list_wrap">
        <div class="board_list">
            <div class="top">
                <div class="num">    번호</div>
                <div class="title">  제목</div>
                <div class="fileYn"><img src='assets/images/file_icon.png' class='file-icon'></div>
                <div class="writer"> 등록자</div>
                <div class="date">   등록일</div>
                <div class="count">  조회수</div>
            </div>
            <div id="resultTable"></div>
        </div>
        <div class="board_page" id="paging"></div>
        <div class="bt_wrap">
            <a onClick="BoardRegPage(page)" class="on">등록</a>
        </div>
    </div>
</div>
</body>
</html>
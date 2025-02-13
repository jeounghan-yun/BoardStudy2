
/**
 * null 체크함수
 * @param str
 */
function isNull(str){
    if(str == null || str == "" || str == undefined) return true;
    return false;
}

/**
 * comsubmit
 * @param opt_formId
 */
function ComSubmit(opt_formId) {
    this.formId = isNull(opt_formId) == true ? "commonForm" : opt_formId;
    this.url = "";

    if(this.formId == "commonForm") {
        $("#commonForm")[0].reset();
    }

    this.setUrl = function setUrl(url){
        this.url = url;
    }

    this.addParam = function addParam(key, value){
        $("#"+this.formId).append($("<input type='hidden' name='"+key+"' id='"+key+"' value='"+value+"'>"));
    }

    this.submit = function submit(){
        var frm = $("#"+this.formId)[0];
        frm.action = this.url;
        frm.method = "post";
        frm.submit();
    }
}

/**
 * 
 * @param str
 * @returns {boolean}
 */
String.prototype.equals = function (str) {
    return typeof str === "string" && this.toString() === str;
};

/**
 * 페이지네이션
 * @param params
 */
function paginNation(params) {
    var currentPage = (parseInt(params.currentPage));    // 현재 페이지
    var totalCount  = (parseInt(params.totalCount));     // 전체 레코드 수
    var listCnt     = (parseInt(params.listCnt));        // 페이지당 레코드 수
    var pageCnt     = params.pageCnt;                    // 페이지 수

    var totalPage   = Math.ceil(totalCount / listCnt) // 총 페이지 수

    var startPage   = Math.floor((currentPage - 1) / pageCnt) * pageCnt + 1; //시작 페이지
    // var endPage     = Math.min(startPage + pageCnt - 1, totalPage);
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

/**
 * 검색창 초기화
 */
function initialize() {
    $('#frmSrchBoard').each(function (){
        this.reset();
    })
}

function initializeDatePicker(selector){
    $(selector).datepicker({
        dateFormat: "yy-mm-dd", // 날짜 형식
        changeMonth: true,      // 월 선택 가능
        changeYear: true,       // 연도 선택 가능
        showButtonPanel: true   // 오늘 날짜 선택 버튼 추가
    });
}


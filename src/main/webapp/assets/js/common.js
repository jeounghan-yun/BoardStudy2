
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

    // url 설정
    this.setUrl = function setUrl(url){
        this.url = url;
    }

    // 파라미터 추가
    this.addParam = function addParam(key, value){
        $("#"+this.formId).append($("<input type='hidden' name='"+key+"' id='"+key+"' value='"+value+"'>"));
    }

    // 서버로 넘겨줌
    this.submit = function submit(){
        var frm = $("#"+this.formId)[0];
        frm.action = this.url;
        frm.method = "post";
        frm.submit();
    }
}

/**
 * 비교 함수
 * @param str
 * @returns {boolean}
 */
String.prototype.equals = function (str) {
    return typeof str === "string" && this.toString() === str;
};

/**
 * 검색창 초기화
 */
function initialize() {
    $('#frmSrchBoard').each(function (){
        this.reset();
    })
}

/**
 * 달력
 * @param selector
 */
function initializeDatePicker(selector){
    $(selector).datepicker({
        dateFormat: "yy-mm-dd", // 날짜 형식
        changeMonth: true,      // 월 선택 가능
        changeYear: true,       // 연도 선택 가능
        showButtonPanel: true   // 오늘 날짜 선택 버튼 추가
    });
}

/**
 * XSS 크로스 사이트 스크립팅 체크
 * @param str
 * @param level
 * @returns {*}
 * @constructor
 */
function XSSCheck(str) {
    if (!str) return false;

    const regex = /[<>&/]/g;
    return regex.test(str);
}


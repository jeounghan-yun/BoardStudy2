
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
function initialize(formId) {
    $('#' + formId).each(function () {
        this.reset();
    });
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

/**
 * css 반응형 웹
 * @type {number}
 */
const time_to_show_login = 400;
const time_to_hidden_login = 200;

function change_to_login() {
    document.querySelector('.cont_forms').className = "cont_forms cont_forms_active_login";
    document.querySelector('.cont_form_login').style.display = "block";
    document.querySelector('.cont_form_sign_up').style.opacity = "0";
    setTimeout(function(){  document.querySelector('.cont_form_login').style.opacity = "1"; },time_to_show_login);

    setTimeout(function(){
        document.querySelector('.cont_form_sign_up').style.display = "none";
    },time_to_hidden_login);
}

const time_to_show_sign_up = 100;
const time_to_hidden_sign_up = 400;

function change_to_sign_up(at) {
    document.querySelector('.cont_forms').className = "cont_forms cont_forms_active_sign_up";
    document.querySelector('.cont_form_sign_up').style.display = "block";
    document.querySelector('.cont_form_login').style.opacity = "0";

    setTimeout(function(){  document.querySelector('.cont_form_sign_up').style.opacity = "1";
    },time_to_show_sign_up);

    setTimeout(function(){   document.querySelector('.cont_form_login').style.display = "none";
    },time_to_hidden_sign_up);


}

const time_to_hidden_all = 500;

function hidden_login_and_sign_up() {

    document.querySelector('.cont_forms').className = "cont_forms";
    document.querySelector('.cont_form_sign_up').style.opacity = "0";
    document.querySelector('.cont_form_login').style.opacity = "0";

    setTimeout(function(){
        document.querySelector('.cont_form_sign_up').style.display = "none";
        document.querySelector('.cont_form_login').style.display = "none";
    },time_to_hidden_all);

}

/**
 * 이메일 유효성 검사
 * @param email
 * @returns {boolean}
 */
function emailCheck(email) {
    var reg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    return reg.test(email);
}

/**
 * ID 중복 체크
 * @param userId
 * @returns {boolean}
 */
function idCheck(userId, callback) {
    var isDuplicate = false;
    console.log('sdfas')
    $.ajax({
        type      : "POST"
        , url     : "/Login/idCheck"
        , data    : {userId:userId}
        , success : function (data) {
            console.log(data);
            if('SUCCESS'.equals(data.errCode)){
                isDuplicate = true;
                callback(isDuplicate);
            } else {
                callback(isDuplicate);
            }
        }, error: function () {
            callback(false);
        }
    })
}
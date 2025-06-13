<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/css.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"    type="text/javascript"></script>
    <script src="../assets/js/common.js"                                                 type="text/javascript"></script>
</head>

<script>
    $(document).ready(function () {
        $('#signUp').on('click', function (){
            SaveSignUp();
        })

        $("#clph").on("keyup", function() {
            var input = $(this).val().replace(/[^0-9]/g, ''); // 숫자만 남김

            var result = "";
            if (input.length < 4) {
                result = input;
            } else if (input.length < 7) {
                result = input.substr(0, 3) + "-" + input.substr(3);
            } else if (input.length < 11) {
                result = input.substr(0, 3) + "-" + input.substr(3, 3) + "-" + input.substr(6);
            } else {
                result = input.substr(0, 3) + "-" + input.substr(3, 4) + "-" + input.substr(7, 4);
            }

            $(this).val(result);
        })

        $("#login").on('click', function () {
            SaveLogin();
        })
    })


    /**
     * 회원가입
     * email   : email@gmail.com 형식 체크 O
     * ID      : 중복 체크 O
     * passWd  : 비밀번호와 비밀번호 확인 체크 O
     * clph    : 번호 양식에 맞게 코드 O
     * @constructor
     */
    function SaveSignUp() {
        var signUpSave = true; // 회원가입 참/거짓 체크
        var message = "";      // 오류 메시지

        // 빈 칸 유효성 검사
        if(isNull($("#email2").val()) || isNull($("#userId2").val()) || isNull($("#userPw2").val()) ||
            isNull($("#userPwCheck2").val()) || isNull($("#clph").val())) {
            message = "빈 칸이 존재합니다.<br>";
            $("#message2").html(message);
            signUpSave = false;
        }

        // 이메일 형식 유효성 검사
        if(!isNull($("#email2").val())){
            if(!emailCheck($("#email2").val())) {
                message += "이메일 형식이 맞지 않습니다.<br>";
                $("#message2").html(message);
                signUpSave = false;
            }
        }

        // ID 중복 체크
        if(!isNull($("#userId2").val())) {
            console.log('id 빈칸');
            $.ajax({
                type      : "POST"
                , url     : "/Login/idCheck"
                , data    : {userId:$("#userId2").val()}
                , async   : false
                , success : function (data) {
                    console.log(data);
                    if('ERROR'.equals(data.errCode)){
                        message += "이미 사용 중인 아이디입니다.<br>";
                        $("#message2").html(message);
                        signUpSave = false;
                    }
                }
            })
        }

        // 비밀번호 확인 유효성 검사
        if($("#userPw2").val() != $("#userPwCheck2").val()) {
            message += "비밀번호와 비밀번호 확인이 일치하지 않습니다. <br>";
            $("#message2").html(message);
            signUpSave = false;
        }

        // 회원가입
        if(signUpSave){
            $.ajax({
                type      : "POST"
                , url     : "/Login/SignUp"
                , data    : $("#frmSignUp").serialize()
                , success : function (data) {
                    console.log(data.errCode);
                    if('SUCCESS'.equals(data.errCode)){
                        alert("회원가입 완료되었습니다. 로그인하십시오.");
                        initialize("frmSignUp");
                        change_to_login(); // 로그인 창으로 전환
                    } else {
                        alert("회원가입이 실패했습니다.");
                    }
                }
            })
        }
    }

    /**
     * 로그인
     * @constructor
     */
    function SaveLogin() {
        var loginSave = true;
        var message = "";      // 오류 메시지

        // 빈 칸 유효성 검사
        if(isNull($("#userId1").val()) && isNull($("#userPw1").val())) {
            message = "아이디와 비밀번호를 입력해주세요.<br>";
            $("#message1").html(message);
            loginSave = false;
        }

        // 아이디 입력란이 비었을 때
        if(isNull($("#userId1").val() && !isNull($("#userPw1").val()))) {
            message = "아이디를 입력해주세요.<br>";
            $("#message1").html(message);
            loginSave = false;
        }

        if(!isNull($("#userId1").val() && isNull($("#userPw1").val()))) {
            message = "비밀번호를 입력해주세요.<br>";
            $("#message1").html(message);
            loginSave = false;
        }

        if(loginSave) {
            $.ajax({
                type: "POST"
                , url: "/Login/SignIn"
                , data: $("#frmSignIn").serialize()
                , success: function (data) {
                    console.log(data);
                    if("SUCCESS".equals(data.errCode)) {
                        alert("로그인이 완료되었습니다.");
                        BoardListPage();
                    } else {
                        alert("로그인이 실패했습니다..");
                    }
                }
            })
        }
    }


</script>
<body class="all_box">
    <div>
        <div class="cotn_principal">
            <div class="cont_centrar">

                <div class="cont_login">
                    <div class="cont_info_log_sign_up">
                        <div class="col_md_login">
                            <div class="cont_ba_opcitiy">

                                <h2>LOGIN</h2>
                                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                                <button class="btn_login" onclick="change_to_login()">LOGIN</button>
                            </div>
                        </div>
                        <div class="col_md_sign_up">
                            <div class="cont_ba_opcitiy">
                                <h2>SIGN UP</h2>
                                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                                <button class="btn_sign_up" onclick="change_to_sign_up()">SIGN UP</button>
                            </div>
                        </div>
                    </div>


                    <div class="cont_back_info">
                        <div class="cont_img_back_grey">
                            <img src="https://images.unsplash.com/42/U7Fc1sy5SCUDIu4tlJY3_NY_by_PhilippHenzler_philmotion.de.jpg?ixlib=rb-0.3.5&q=50&fm=jpg&crop=entropy&s=7686972873678f32efaf2cd79671673d" alt="" />
                        </div>

                    </div>
                    <div class="cont_forms" >
                        <div class="cont_img_back_">
                            <img src="https://images.unsplash.com/42/U7Fc1sy5SCUDIu4tlJY3_NY_by_PhilippHenzler_philmotion.de.jpg?ixlib=rb-0.3.5&q=50&fm=jpg&crop=entropy&s=7686972873678f32efaf2cd79671673d" alt="" />
                        </div>
                        <div>
                            <form class="cont_form_login" id="frmSignIn" name="frmSignIn" onsubmit="return false">
                                <a href="#" onclick="hidden_login_and_sign_up()" ><i class="material-icons">&#xE5C4;</i></a>
                                <h2>LOGIN</h2>
                                <input type="text" id="userId1" name="userId1" placeholder="ID" />
                                <input type="password" id="userPw1" name="userPw1" placeholder="Password" />
                                <button class="btn_login" id="login">LOGIN</button>
                                <div id="message1" style="font-size: 12px; left: 0; bottom: 0; margin-top: 15px;">
                            </form>
                        </div>
                        <div>
                            <form class="cont_form_sign_up" id="frmSignUp" name="frmSignUp" onsubmit="return false">
                                <a href="#" onclick="hidden_login_and_sign_up()"><i class="material-icons">&#xE5C4;</i></a>
                                <h2>SIGN UP</h2>
                                <input type="text" id="email2" name="email2" placeholder="Email" />
                                <input type="text" id="userId2" name="userId2" placeholder="ID" />
                                <input type="password" id="userPw2" name="userPw2" placeholder="Password" />
                                <input type="password" id="userPwCheck2" placeholder="Confirm Password" />
                                <input type="text" id="clph" name="clph" placeholder="phone number" maxlength="13"/>
                                <button class="btn_sign_up" id="signUp">SIGN UP</button>
                                <div id="message2" style="font-size: 12px; left: 0; bottom: 0; margin-top: 15px;">
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>


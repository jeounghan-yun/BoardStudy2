<%@page pageEncoding="utf-8"%>

<script>
    var isSave;
    var url    = "";
    // var files;

    $(document).ready(function () {
        $("#file").on('change', function () {
            // files = $('#file')[0].files;
            fileTemp();
        })

        if(boardMode.equals("E")){
            getViewData()
        }

        url  = boardMode.equals("W") ? "/Board/BoardReg" : "/Board/BoardEdit";

        $("#isSave").on('click', function() {

            if(isNull($("#ttl").val()) || isNull($("#userId").val()) || isNull($("#cnts").val())){
                alert("빈 칸이 존재합니다.");
                isSave = false;
            } else {
                isSave = true;
            }

            if(isSave === true){
                $.ajax({
                      type : "POST"
                    , url  : url
                    , data : $("#frmRegBoard").serialize() + "&SEQ=" + SEQ + "&file=" + "&files=" + files
                    , success : function (data) {
                        if("SUCCESS".equals(data.errCode)){
                            alert("저장 되었습니다.");
                            BoardListPage(1, "N");
                        } else {
                            alert("오류가 발생하였습니다.");
                        }
                    }
                })
            }
        })
    })

    getViewData = function () {
        $.ajax({
              type : "POST"
            , url  : "/Board/BoardDetail"
            , data : {SEQ : SEQ}
            , success : function (data) {
                var data = data.DetailData

                $("#ttl").attr('value', data.ttl);
                $("#userId").attr('value', data.regId);
                document.getElementById("userId").disabled = 'true';
                $("#cnts").html(data.cnts);
            }
        })
    }



</script>

<body>
<div class="board_wrap">
    <div class="board_title">
        <strong>공지사항</strong>
        <p>공지사항을 빠르고 정확하게 안내해드립니다.</p>
    </div>
    <div class="board_write_wrap">
        <form id="frmRegBoard" name="frmRegBoard" onSubmit="return false">
            <div class="board_write">
                <div class="title">
                    <dl>
                        <dt>제목</dt>
                        <dd><input type="text" id="ttl" name="ttl" placeholder="제목 입력" maxlength="30"></dd>
                    </dl>
                </div>
                <div class="info">
                    <dl>
                        <dt>등록자</dt>
                        <dd><input type="text" id="userId" name="userId" placeholder="등록자 입력" maxlength="12"></dd>
                    </dl>
                </div>
                <div class="cont">
                    <textarea type="text" id="cnts" name="cnts" placeholder="내용 입력" maxlength="500"></textarea>
                </div>
                <div class="file">
                    <input type="file" id="file" name="file" class="file-input" multiple="multiple"/>
                    <ul style="font-size: 15px" id="fileList"></ul>
                </div>
            </div>
        </form>
        <div class="bt_wrap">
            <a id="isSave" class="on">저장</a>
            <a onClick="BoardListPage(page)">취소</a>
        </div>
    </div>
</div>
</body>
</html>

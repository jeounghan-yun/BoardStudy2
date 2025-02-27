<%@page pageEncoding="utf-8"%>

<script>
    $(document).ready(function () {
        // 수정 페이지 시 상세 데이터 가져오기
        if(boardMode.equals("E")){
            getViewData()
        }

        // 등록 페이지 시 파일 첨부 실행
        $("#file").on('change', function () {
            files = $('#file')[0].files;
            fileTemp(); //임시 파일 이동
        })

        // 등록 페이지 등록 버튼을 클릭 실행
        $("#isSave").on('click', function() {
            var url    = boardMode.equals("W") ? "/Board/BoardReg" : "/Board/BoardEdit";
            var isSave = true;

            if(isNull($("#ttl").val()) || isNull($("#userId").val()) || isNull($("#cnts").val())){
                alert("빈 칸이 존재합니다.");
                isSave = false;
            }

            var comSubmit = new ComSubmit();

            comSubmit.addParam("ttl"   , $("#ttl").val());
            comSubmit.addParam("userId", $("#userId").val());
            comSubmit.addParam("cnts"  , $("#cnts").val());

            if ("E".equals(boardMode)) {
                comSubmit.addParam("SEQ"         , SEQ);
                comSubmit.addParam("delFileNames", delFileNames);
                comSubmit.addParam("addFileNames", addFileNames);
                comSubmit.addParam("userId"      , $("#userId").val());
            }

            if (isSave){
                $.ajax({
                      type : "POST"
                    , url  : url
                    , data : $("#commonForm").serialize()
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

    /**
     * 수정 시 상세 데이터
     */
    getViewData = function () {
        $.ajax({
              type : "POST"
            , url  : "/Board/BoardDetail"
            , data : {
                          SEQ       : SEQ
                        , boardMode : boardMode
                     }
            , success : function (data) {
                var data = data.DetailData;
                var fileOriginNmList = [];
                var fileUniNmList = [];

                $("#ttl").attr('value', data[0].ttl);
                $("#userId").attr('value', data[0].regId);
                document.getElementById("userId").disabled = 'true';
                $("#cnts").html(data[0].cnts);

                $.each(data, function (k, v) {
                    fileOriginNmList.push(v.originFileNm);
                    fileUniNmList.push(v.uniFileNm);
                })

                if(!isNull(data[0].originFileNm)){
                    EditFile(fileOriginNmList, fileUniNmList);
                }
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
                    <div class="fileBox" id="fileList"></div>
                </div>
            </div>
        </form>
        <div class="bt_wrap">
            <a id="isSave" class="on">저장</a>
            <a id="cancelBtn" onClick="BoardListPage(page); navigator.sendBeacon('/cancelUpload');">취소</a>
        </div>
    </div>
</div>
</body>
</html>

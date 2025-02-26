<%@page pageEncoding="utf-8"%>

<script>
    $(document).ready(function () {
        getViewData();

        $("#isDel").on('click', function (){
            DelData();
        })
    })

    getViewData = function () {
        $.ajax({
              type : "POST"
            , url  : "/Board/BoardDetail"
            , data : {SEQ : SEQ}
            , success : function (data) {
                var data = data.DetailData;

                $("#seq").html(numCount);
                $("#ttl").html(data[0].ttl);
                $("#regId").html(data[0].regId);
                $("#regDate").html(data[0].regDate);
                $("#readCnt").html(data[0].readCnt);
                $("#cnts").html(data[0].cnts);

                var str = "";

                $.each(data, function (k, v){
                    if("Y".equals(v.fileYn)) {
                        // str += "<div><a href='http://localhost:8080/download?filePath=" + v.fileFlph + "&uuid=" + v.uniFileNm + "&originFileName=" + encodeURIComponent(v.originFileNm) + "' download='" + v.originFileNm + "'>" + v.originFileNm + "</a></div>";
                        str += "<div class='file-item'>" + "<a href='http://localhost:8080/download?filePath=" + v.fileFlph + "&uuid=" + v.uniFileNm + "&originFileName=" + encodeURIComponent(v.originFileNm) + "' download='" + v.originFileNm + "' class='file-name'>" + v.originFileNm + "</a>" + "</div>";
                    } else {
                        str += "<div>파일 없음.</div>"
                    }
                })

                $("#fileList").html(str);
            }
        })
    }

    DelData = function () {
        $.ajax({
              type : "POST"
            , url  : "Board/BoardDelData"
            , data : { SEQ   : SEQ
                     , regId : regId }
            , success : function (data) {
                if("SUCCESS".equals(data.errCode)){
                    alert("삭제 되었습니다.")
                    BoardListPage(1, "N");
                } else {
                    alert("오류가 발생하였습니다.");
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
    <div class="board_view_wrap">
        <div class="board_view">
            <div class="title" id="ttl"></div>
            <div class="info">
                <dl>
                    <dt>번호</dt>
                    <dd id="seq"></dd>
                </dl>
                <dl>
                    <dt>등록자</dt>
                    <dd id="regId"></dd>
                </dl>
                <dl>
                    <dt>작성일</dt>
                    <dd id="regDate"></dd>
                </dl>
                <dl>
                    <dt>조회</dt>
                    <dd id="readCnt"></dd>
                </dl>
            </div>
            <div class="cont" id="cnts"></div>
            <div class="file">
                <div class="fileBox" id="fileList"></div>
            </div>

        </div>
        <div class="bt_wrap">
            <a onClick="BoardListPage(page)" class="on">목록</a>
            <a onClick="BoardEditPage(page)">수정</a>
            <a id="isDel" class="del">삭제</a>
        </div>
    </div>
</div>
</body>
</html>

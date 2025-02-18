var files;
var fileList;
/**
 * 파일 임시 저장
 */
fileTemp = function () {
    files = $('#file')[0].files;
    fileList = $('#fileList');
    var formData = new FormData;

    fileList.empty(); // 기존 목록 초기화

    for (var i = 0; i < files.length; i++) {
        formData.append("files", files[i]);
    }

    $.ajax({
        type : "POST"
        , url         : "/Board/AjaxTempFile"
        , data        : formData
        , processData : false
        , contentType : false
        , success     : function (data) {
            for (var i = 0; i < data.length; i++) {
                fileList.append('<li>' + data[i] + '</li>');
            }

        },
        error: function (err) {
            console.error("파일 업로드 실패", err);
        }
    })
}

// window.addEventListener("beforeunload", function (event) {
//     $.ajax({
//           type    : "POST"
//         , url     : "/Board/Cancel"
//         , success : function (data) {
//         }
//     })
//
// })

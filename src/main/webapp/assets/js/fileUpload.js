
/**
 * 파일 임시 저장
 */
// fileTemp = function () {
//     var files = $('#file')[0].files;
//     var fileList = $('#fileList');
//     var formData = new FormData;
//
//     fileList.empty(); // 기존 목록 초기화
//
//     for (var i = 0; i < files.length; i++) {
//         formData.append("files", files[i]);
//     }
//
//     $.ajax({
//         type : "POST"
//         , url         : "/Board/AjaxTempFile"
//         , data        : formData
//         , processData : false
//         , contentType : false
//         , success     : function (data) {
//             for (var i = 0; i < data.length; i++) {
//                 (function (fileName) {
//                     var listItem = $('<li>').text(fileName);
//
//                     var deleteBtn = $('<button class="delBtn">')
//                         .text('삭제')
//                         .attr('data-file', fileName)
//                         .on('click', function (){
//                             deleteFile(fileName, $(this).parent());
//                         });
//
//                     listItem.append(deleteBtn);
//                     fileList.append(listItem);
//                 })(files[i].name)
//                 // (data[i]);
//             }
//         },
//         error: function (err) {
//             console.error("파일 업로드 실패", err);
//         }
//     })
// }
//
//

/**
 * 임시파일 저장
 */
fileTemp = function () {
    var fileInput = $('#file')[0];
    var files = fileInput.files;
    // var fileList = $('#fileList');
    var formData = new FormData();
    var dt = new DataTransfer();

    // 기존 파일 유지 (이전 파일이 있으면 추가)
    if (fileInput.files.length > 0) {
        for (var i = 0; i < fileInput.files.length; i++) {
            dt.items.add(fileInput.files[i]);
        }
    }

    // 새로 선택한 파일 추가
    for (var i = 0; i < files.length; i++) {
        dt.items.add(files[i]);
        formData.append("files", files[i]); // 서버 전송용 FormData 추가
    }

    $.ajax({
        type: "POST",
        url: "/Board/AjaxTempFile",
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            renderFileList(data); // 화면에 파일 목록 업데이트
        },
        error: function (err) {
            console.error("파일 업로드 실패", err);
        }
    });
};

/**
 * 파일 목록을 화면에 출력하는 함수
 * @param fileListData
 */
function renderFileList(fileListData) {
    var fileList = $('#fileList');

    for (var i = 0; i < fileListData.length; i++) {
        (function (fileName, index) {
            var listItem = $('<li>').text(fileName);

            var deleteBtn = $('<button class="delBtn">')
                .text('삭제')
                .attr('data-file', fileName)
                .on('click', function () {
                    deleteFile(index, fileName, $(this).parent());
                });

            listItem.append(deleteBtn);
            fileList.append(listItem);
        })(fileListData[i], i);
    }
}

/**
 * 특정 파일 삭제
 * @param fileIndex
 * @param fileName
 * @param listItem
 */
function deleteFile(fileIndex, fileName, listItem) {
    var fileInput = $('#file')[0];
    var dt = new DataTransfer();

    $.ajax({
        type: "POST",
        url: "/Board/DeleteTempFile",
        data: { fileName: fileName },
        success: function (data) {
            if ("SUCCESS".equals(data)) {
                for (var i = 0; i < fileInput.files.length; i++) {
                    if (fileInput.files[i].name !== fileName) { // 삭제할 파일 제외
                        dt.items.add(fileInput.files[i]);
                    }
                }

                fileInput.files = dt.files; // 값 업데이트
                listItem.remove(); // 화면에서도 삭제
            }
        }
    });

}

// document.getElementById('cancelBtn').addEventListener('click', function(event) {
//     // 서버에 임시 파일 삭제 요청
//     navigator.sendBeacon('/cancelUpload');
// });

// $('#cancelBtn').addEventListener('click', function(event) {
//     // 서버에 임시 파일 삭제 요청
//     navigator.sendBeacon('/cancelUpload');
// });


// document.getElementById('cancelBtn').addEventListener('beforeunload', function (event) {
//     // 서버에 임시 파일 삭제 요청
//     navigator.sendBeacon('/cancelUpload');
// });

// window.addEventListener('beforeunload', function (event) {
//     // 서버에 임시 파일 삭제 요청
//     navigator.sendBeacon('/cancelUpload');
// });
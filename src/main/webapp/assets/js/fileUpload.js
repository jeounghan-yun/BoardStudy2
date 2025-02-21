/**
 * 임시파일 저장
 */
fileTemp = function () {
    var fileInput = $('#file')[0];
    var files = fileInput.files;
    // var fileList = $('#fileList');
    var formData = new FormData();
    var dt = new DataTransfer();    //데이터 관리 및 저장역할

    // 기존 파일 유지 (이전 파일이 있으면 추가)
    if (fileInput.files.length > 0) {
        for (var i = 0; i < fileInput.files.length; i++) {
            dt.items.add(fileInput.files[i]);
        }
    }
    for (var i = 0; i < files.length; i++) {
        dt.items.add(files[i]);
        formData.append("files", files[i]); // 서버 전송용 FormData 추가
    }

    tempAjax(formData);
};

function tempAjax(formData) {
    $.ajax({
          type: "POST"
        , url: "/Board/AjaxTempFile"
        , data: formData
        , processData: false
        , contentType: false
        , success: function (data) {
            console.log(data);
            renderFileList(data); // 화면에 파일 목록 업데이트
        },
        error: function (err) {
            console.error("파일 업로드 실패", err);
        }
    });
}

/**
 * 수정 페이지 리스트
 * @param fileNmList
 * @constructor
 */
function EditFile(fileNmList) {
    var saveList = [];

    // 기존 파일 유지 (이전 파일이 있으면 추가)
    if (fileNmList.length > 0) {
        for (var i = 0; i < fileNmList.length; i++) {
            saveList.push(fileNmList[i]);
        }
    }
    renderFileList(fileNmList);
}

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
                    deleteFile(index, $(this).parent(), fileName);
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
function deleteFile(fileIndex, listItem, fileName) {
    var fileInput = $('#file')[0];
    var dt = new DataTransfer();

    $.ajax({
        type: "POST",
        url: "/Board/DeleteTempFile",
        data: {fileName  : fileName},
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
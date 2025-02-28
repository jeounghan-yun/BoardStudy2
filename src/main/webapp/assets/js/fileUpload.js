/**
 * 임시 파일 저장
 */
fileTemp = function () {
    var fileInput = $('#file')[0];
    var files = fileInput.files;
    var formData = new FormData();
    var dt = new DataTransfer();    //데이터 관리 및 저장역할

    // 기존 파일 유지 (이전 파일이 있으면 추가)
    if (fileInput.files.length > 0) {
        for (var i = 0; i < fileInput.files.length; i++) {
            dt.items.add(fileInput.files[i]);
        }
    }
    // 서버 전송용 FormData 추가
    for (var i = 0; i < files.length; i++) {
        dt.items.add(files[i]);
        formData.append("files", files[i]);
    }

    tempAjax(formData);
};

/**
 * 임시 파일 저장
 * @param formData
 */
function tempAjax(formData) {
    $.ajax({
          type: "POST"
        , url: "/Board/AjaxTempFile"
        , data: formData
        , processData: false
        , contentType: false
        , success: function (data) {
            renderFileList1(data); // 화면에 파일 목록 업데이트
        }
    });
}

/**
 * 수정 페이지 리스트
 * @param fileNmList
 * @constructor
 */
function EditFile(fileNmList, uniNmList) {
    var saveList = [];

    // 기존 파일 유지 (이전 파일이 있으면 추가)
    if (fileNmList.length > 0) {
        for (var i = 0; i < fileNmList.length; i++) {
            saveList.push(fileNmList[i]);
        }
    }
    renderFileList2(fileNmList, uniNmList);
}

/**
 * 파일 목록을 화면에 출력 함수1
 * @param fileListData
 */
function renderFileList1(fileListData) {
    var fileList = $('#fileList');

    for (var i = 0; i < fileListData.length; i++) {
        (function (fileName, index, uniNmList) {
            var listItem1 = $('<li class="file-item">');                     // 파일 아이템 컨테이너
            var fileNameSpan = $('<span class="file-name">').text(fileName); // 파일명

            // 파일 삭제 버튼
            var deleteBtn = $('<button class="delBtn">')
                .text('삭제')
                .attr('data-file', fileName)
                .on('click', function () {
                    deleteFile(index, $(this).parent(), fileName, uniNmList); // 임시 폴더 특정 파일 삭제
                });

            listItem1.append(fileNameSpan);  // 파일 이름 추가
            listItem1.append(deleteBtn);     // 삭제 버튼 추가 (오른쪽으로 정렬됨)
            fileList.append(listItem1);
            addFileNames.push(fileName);
        })(fileListData[i], i);
    }
}

/**
 * 파일 목록을 화면에 출력 함수2
 * @param fileListData
 */
function renderFileList2(fileListData, uniNmList) {
    var fileList = $('#fileList');

    for (var i = 0; i < fileListData.length; i++) {
        (function (fileName, index, uniNmList) {
            var listItem2 = $('<li class="file-item">'); // 파일 아이템 컨테이너

            var fileNameSpan = $('<span class="file-name">').text(fileName);

            var deleteBtn = $('<button class="delBtn">')
                .text('삭제')
                .attr('data-file', fileName)
                .on('click', function () {
                    deleteFile(index, $(this).parent(), fileName, uniNmList);
                });

            listItem2.append(fileNameSpan);  // 파일 이름 추가
            listItem2.append(deleteBtn);     // 삭제 버튼 추가 (오른쪽으로 정렬됨)
            fileList.append(listItem2);
        })(fileListData[i], i, uniNmList[i]);
    }
}

/**
 * 특정 파일 삭제
 * @param fileIndex
 * @param fileName
 * @param listItem
 */
function deleteFile(fileIndex, listItem, fileName, uniNmList) {
    var fileInput = $('#file')[0];
    var dt = new DataTransfer();

    $.ajax({
        type: "POST",
        url: "/Board/DeleteTempFile",
        data: {
              fileName    : fileName
            , unifileName : uniNmList
            , boardMode   : boardMode
        },
        success: function (data) {
            if ("SUCCESS".equals(data)) {
                for (var i = 0; i < fileInput.files.length; i++) {
                    if (fileInput.files[i].name !== fileName) { // 삭제할 파일 제외
                        dt.items.add(fileInput.files[i]);
                    }
                }
                delFileNames.push(uniNmList);

                fileInput.files = dt.files; // 값 업데이트
                listItem.remove();          // 화면에서도 삭제
            }
        }
    });
}
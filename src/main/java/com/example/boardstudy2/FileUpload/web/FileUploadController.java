package com.example.boardstudy2.FileUpload.web;

import com.example.boardstudy2.Utils.CommandMap;
import com.example.boardstudy2.common.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.boardstudy2.Utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.MalformedURLException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FileUploadController {

    @Autowired
    private FileUtil flieUtil;

    @Value("${spring.servlet.multipart.location}")
    private String tempDir;

    @Value("${part4.upload.path}")
    private String uploadDir;

    /**
     * 임시 파일 저장 RequestParam
     * @param files
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/Board/AjaxTempFile", method= RequestMethod.POST)
    @ResponseBody
    public List<String> fileUpload(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<String> list = new ArrayList<>();

        list = flieUtil.TempFile(files);
        return list; // 업로드된 파일 이름 목록 반환
    }

    /**
     * 파일 다운로드
     * @param filePath
     * @param uuid
     * @param originFileName
     * @return
     * @throws MalformedURLException
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath, @RequestParam String uuid, @RequestParam String originFileName) throws MalformedURLException {
        String fileExtension = originFileName.substring(originFileName.lastIndexOf('.')); // 확장자 추출
        String filePaths     = filePath + uuid + fileExtension;                               // 경로 합치기
        Path path            = Paths.get(uploadDir, filePaths);                               // 실제 경로 Path타입으로 변경
        Resource resource    = new UrlResource(path.toUri());

        try{
            originFileName = URLEncoder.encode(originFileName, "UTF-8").replace("+", " ");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originFileName + "\"")
                .body(resource);
    }

    /**
     * 임시폴더 특정 파일 삭제
     * @RequestParam String fileName
     * @param
     * @return
     */
    @PostMapping("/Board/DeleteTempFile")
    @ResponseBody
    public String deleteTempFile(CommandMap commandMap) {
        String errCode     = "SUCCESS";
        String fileName    = (String) commandMap.get("fileName"); // 파일 명
        String unifileName = (String) commandMap.get("unifileName"); // 파일 명
        String tempDirs    = "";

        // 확장자 추출
        String fileExtension = "";
        if (fileName != null && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        if("E".equals(commandMap.get("boardMode"))){
            tempDirs = tempDir + unifileName + "." + fileExtension; // 수정 시 uuid로 찾기
        } else {
            tempDirs = tempDir + fileName;    // 등록 시 파일명으로 찾기
        }


        File file = new File(tempDirs);   //등록


        if (file.exists()) {
            file.delete();
        } else {
            errCode = "ERROR";
        }

        return errCode;
    }

    /**
     * 등록 취소 및 브라우저 닫기 클릭 시 임시 파일 캔슬
     */
    @PostMapping("/cancelUpload")
    public void cancelUpload() {
        File tmpDir = new File(tempDir);  // 전달된 임시 폴더 경로를 사용해 폴더 객체 생성

        if (tmpDir.exists() && tmpDir.isDirectory()) {
            File[] files = tmpDir.listFiles();  // 폴더 내 모든 파일 목록을 가져옴
            Common.fileDel(files);
        }
    }
}

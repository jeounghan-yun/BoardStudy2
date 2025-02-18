package com.example.boardstudy2.FileUpload.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.boardstudy2.Utils.FileUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    @Value("${part4.upload.path}")
    private String uploadDir;

    /**
     * 임시 파일 저장
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
     * 파일 다운로드 기능
     * @param filePath
     * @return
     * @throws MalformedURLException
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) throws MalformedURLException {
        Path path = Paths.get(uploadDir, filePath);
        Resource resource = new UrlResource(path.toUri());

        //파일 존재 확인
        if (resource.exists()) {
            try {
                // 파일명을 URL 인코딩 처리 한글 및 특수 문자가 깨지지 않도록
                String encodedFileName = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8.toString());

                // URL 인코딩할 때 공백이 "+"로 변환되므로 이를 " "으로 변경
                encodedFileName = encodedFileName.replaceAll("\\+", " ");

                // 파일 다운로드 응답 헤더 설정
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                        .body(resource);
            } catch (UnsupportedEncodingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 등록 취소 및 브라우저 닫기 클릭 시 임시 파일 캔슬
     */
//    @RequestMapping(value="/Board/Cancel", method= RequestMethod.POST)
//    public void cancelUpload() {
//        flieUtil.fileDel();
//    }
}

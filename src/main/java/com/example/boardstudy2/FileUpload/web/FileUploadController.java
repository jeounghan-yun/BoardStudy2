package com.example.boardstudy2.FileUpload.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.boardstudy2.Utils.FileUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath, @RequestParam String uuid, @RequestParam String originFileName) throws MalformedURLException {
        String fileExtension = originFileName.substring(originFileName.lastIndexOf('.'));

        String filePaths = filePath + uuid + fileExtension;
        Path path = Paths.get(uploadDir, filePaths);
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originFileName + "\"")
                .body(resource);
    }




    /**
     * 등록 취소 및 브라우저 닫기 클릭 시 임시 파일 캔슬
     */
//    @RequestMapping(value="/Board/Cancel", method= RequestMethod.POST)
//    public void cancelUpload() {
//        flieUtil.fileDel();
//    }
}

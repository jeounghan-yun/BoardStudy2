package com.example.boardstudy2.FileUpload.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.boardstudy2.Utils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileUploadController {

    @Autowired
    private FileUtil flieUtil;

    @RequestMapping(value="/Board/AjaxTempFile", method= RequestMethod.POST)
    @ResponseBody
    public List<String> fileUpload(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<String> list = new ArrayList<>();

        list = flieUtil.tempFile(files);
        return list; // 업로드된 파일 이름 목록 반환
    }
}

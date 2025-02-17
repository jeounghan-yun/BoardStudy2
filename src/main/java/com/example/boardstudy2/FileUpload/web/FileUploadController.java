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

    /**
     * 파일 임시 저장
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
}

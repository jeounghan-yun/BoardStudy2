package com.example.boardstudy2.Utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtil {

    public List<String> tempFile(List<MultipartFile> files) throws IOException {
        List<String> fileNmList = new ArrayList<>();
        String tempDir = "D:/Project/BoardStudy2/temp/";

        File dir = new File(tempDir);

        // 폴더가 존재하면 파일 삭제 후 폴더도 삭제
        File[] filesInDir = dir.listFiles();
        if (filesInDir != null) {
            for (File f : filesInDir) { // 폴더 내 파일 삭제
                if (f.exists()) { // 파일 존재 여부 확인
                    f.delete();
                }
            }
        }

        // 폴더가 존재하지 않으면 새로 생성
        if(!dir.exists()){
            dir.mkdirs();
        }

        // 파일 저장 및 파일 이름 추가
        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isBlank()) {
                throw new IOException("파일 이름이 올바르지 않습니다.");
            }

            // 임시 파일명을 방지하기 위해 UUID로 새로운 파일명 생성
//            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
//            String newFileName = UUID.randomUUID().toString() + fileExtension;

            Path tempPath = Paths.get(tempDir, originalFileName);
            byte[] bytes = file.getBytes();
            Files.write(tempPath, bytes);
//            Files.copy(file.getInputStream(), tempPath, StandardCopyOption.REPLACE_EXISTING);

            fileNmList.add(originalFileName);
        }

        return fileNmList;
    }
}

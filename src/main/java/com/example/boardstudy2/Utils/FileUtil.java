package com.example.boardstudy2.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

@Component
@PropertySource("classpath:application.properties")
public class FileUtil {

    // application.properties 임시 파일 경로 가져옴.
    @Value("${spring.servlet.multipart.location}")
    private String tempDir;

    /**
     * 파일 임시 저장
     * @param files
     * @return
     * @throws IOException
     */
    public List<String> tempFile(List<MultipartFile> files) throws IOException {
        List<String> fileNmList = new ArrayList<>();

        File dir = new File(tempDir);

        // 폴더가 존재하지 않으면 새로 생성
        if(!dir.exists()){
            dir.mkdirs();
        }

        // 폴더가 존재하면 파일 삭제 후 폴더도 삭제
        File[] filesInDir = dir.listFiles();
        if (filesInDir != null) {
            for (File f : filesInDir) { // 폴더 내 파일 삭제
                if (f.exists() && f.isFile()) { // 파일 존재 여부 확인
                    f.delete();  // 파일 삭제
                }
            }
        }

        // 파일 저장 및 파일 상태 검증
        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();

            Path tempPath = Paths.get(tempDir, originalFileName);
            Files.copy(file.getInputStream(), tempPath, StandardCopyOption.REPLACE_EXISTING);
            fileNmList.add(originalFileName);
        }

        return fileNmList;
    }
}

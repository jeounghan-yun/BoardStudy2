package com.example.boardstudy2.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@PropertySource("classpath:application.properties")
public class FileUtil {

    // application.properties 임시 파일 경로 가져옴.
    @Value("${spring.servlet.multipart.location}")
    private String tempDir;

    @Value("${part4.upload.path}")
    private String uploadDir;

    /**
     * 파일 임시 저장
     * @param files
     * @return
     * @throws IOException
     */
    public List<String> tempFile(List<MultipartFile> files) throws IOException {
        List<String> fileNmList = new ArrayList<>();

        File tmpDir = new File(tempDir);    // 파일타입
//        Path tmpDir = Paths.get(tempDir);  // 파일경로타입

        // 폴더가 존재하지 않으면 새로 생성
        if(!tmpDir.exists()){
            tmpDir.mkdirs();
        }
//        if (!Files.exists(tmpDir)) {
//            Files.createDirectories(tmpDir);
//        }

        // 폴더가 존재하면 파일 삭제 후 폴더도 삭제
        File[] filesInDir = tmpDir.listFiles();
        if (filesInDir != null) {
            for (File f : filesInDir) {         // 폴더 내 파일 삭제
                if (f.exists() && f.isFile()) { // 파일 존재 여부 확인
                    f.delete();                 // 파일 삭제
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

    /**
     * 업로드 파일 저장
     * 임시폴더에서 -> 최종 업로드 폴더로 파일 이동
     * @param map
     * @return
     * @throws IOException
     */
    public Map<String, Object> uploadFile(Map<String, Object> map) throws IOException {
        Path tmpDir   = Paths.get(tempDir);     // 임시 파일 폴더 경로
        Path finalDir = Paths.get(uploadDir); // 최종 파일 폴더 경로

        // 임시 폴더에 파일이 존재하는지 확인
        if (Files.exists(tmpDir) && Files.isDirectory(tmpDir)) {
            // 최종 폴더가 존재하지 않으면 생성
            if (!Files.exists(finalDir)) {
                Files.createDirectories(finalDir);
            }

            // 임시 폴더의 모든 파일 이동
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(tmpDir);
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath)) { // 파일인지 확인
                    Path targetPath = finalDir.resolve(filePath.getFileName()); // 최종 경로 설정
                    Files.move(filePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            // 임시 폴더가 비어 있으면 삭제
            if (Files.list(tmpDir).findAny().isEmpty()) {
                Files.delete(tmpDir);
            }
        } else {
            throw new IOException("임시 폴더가 존재하지 않거나 올바른 디렉토리가 아닙니다: " + tempDir);
        }

        return map;
    }
}

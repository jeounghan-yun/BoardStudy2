package com.example.boardstudy2.Utils;

import com.example.boardstudy2.common.Common;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

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
    public List<String> TempFile(List<MultipartFile> files) throws IOException {
        List<String> fileNmList = new ArrayList<>();

        File tmpDir = new File(tempDir);     // 파일타입

        // 폴더가 존재하지 않으면 새로 생성
        if(!tmpDir.exists()){
            tmpDir.mkdirs();
        }

        // 폴더가 존재하면 파일 삭제
//        File[] filesInDir = tmpDir.listFiles();
//        Common.fileDel(filesInDir);

        // 파일 저장 및 파일 상태 검증
        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();           // 원본 파일명
            Path tempPath           = Paths.get(tempDir, originalFileName); // 임시파일 경로 + 원본 파일명 합치기

            Files.copy(file.getInputStream(), tempPath, StandardCopyOption.REPLACE_EXISTING); // 임시저장

            fileNmList.add(originalFileName); // 원본 파일명 추출
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
    public Map<String, Object> UploadFile(Map<String, Object> map) throws IOException {
        String result                  = "SUCCESS";                           // 성공 여부
        int seq                        = (Integer) map.get("rseq");           // 시퀀스
        Path tmpDir                    = Paths.get(tempDir);                  // 임시 파일 폴더 경로
        Path finalDir                  = Paths.get(uploadDir + seq);      // 최종 파일 폴더 경로
        List<String> originalFileNames = new ArrayList<>();                   // 원본파일명 리스트
        List<String> uniqueFileNames   = new ArrayList<>();                   // 유일파일명 리스트

        // 임시 폴더에 파일이 존재하는지 확인
        if (Files.exists(tmpDir) && Files.isDirectory(tmpDir)) {
            // 최종 폴더가 존재하지 않으면 생성
            if (!Files.exists(finalDir)) {
                Files.createDirectories(finalDir);
            }

            // 임시 폴더의 모든 파일 이동
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(tmpDir);
            for (Path filePath : directoryStream) {
                // 파일명 가져오기 (파일명만 추가)
                if (Files.isRegularFile(filePath)) {                                                             // 파일인지 확인
                    String originalFileName = filePath.getFileName().toString();                                 // 원본 파일명
                    String fileExtension    = originalFileName.substring(originalFileName.lastIndexOf('.')); // 확장자와 파일명 분리
                    String uuid             = UUID.randomUUID().toString();                                      // uuid 랜덤키 생성
                    String uniqueFileName   = uuid + fileExtension;                                              // 확장자를 uuid에 추가
                    Path targetPath         = finalDir.resolve(uniqueFileName);                                  // 실제 경로에 해당파일을 uuid이름으로 이동
                    Files.move(filePath, targetPath, StandardCopyOption.REPLACE_EXISTING);                       // 임시파일 -> 실제파일 이동

                    uniqueFileNames.add(uuid);
                    originalFileNames.add(filePath.getFileName().toString());
                }
            }
            // 임시 폴더가 비어 있으면 삭제
//            if (Files.list(tmpDir).findAny().isEmpty() || Files.list(tmpDir)) {
//                Files.delete(tmpDir);
//            }
        } else {
            result = "ERROR";
        }
        map.put("result"           , result);
        map.put("originalFileNames", originalFileNames);
        map.put("uniqueFileNames"  , uniqueFileNames);
        return map;
    }

    /**
     * 실제 파일 -> 임시 파일
     * @param map
     * @throws Exception
     */
    public void getUploadFile(Map<String, Object> map) throws Exception {
        String seq                      = (String) map.get("SEQ");            // 시퀀스
        Path tmpPath                    = Paths.get(tempDir);                 // 임시 파일 폴더 경로
        Path finalPath                  = Paths.get(uploadDir + seq);    // 최종 파일 폴더 경로

        // 디렉터리 내부의 파일 리스트 가져오기
        File[] files = finalPath.toFile().listFiles();

        for (File file : files) {
            if (file.isFile()) { // 파일인 경우만 복사
                Path sourceFile = file.toPath();
                Path targetFile = tmpPath.resolve(file.getName()); // 대상 파일 경로 설정

                Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /**
     * 파일 수정
     * @param map
     * @return
     * @throws Exception
     */
    public String editUplodFile(Map<String, Object> map) throws Exception {
        String result     = "SUCCESS";
        String seq        = (String) map.get("SEQ");   // 시퀀스
        String uploadDirs = uploadDir + seq;
        Path tmpDir       = Paths.get(tempDir);        // 임시 파일 폴더 경로
        Path finalDir     = Paths.get(uploadDirs);     // 최종 파일 폴더 경로

        File[] filesInDir = finalDir.toFile().listFiles();
        Common.fileDel(filesInDir);

        if (Files.exists(tmpDir) && Files.isDirectory(tmpDir)) {
            // 임시 폴더의 모든 파일 이동
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(tmpDir);
            for (Path filePath : directoryStream) {
                // 파일명 가져오기 (파일명만 추가)
                if (Files.isRegularFile(filePath)) {
                    Path targetPath = finalDir.resolve(filePath.getFileName());             // 최종 경로 (파일명 유지)
                    Files.move(filePath, targetPath, StandardCopyOption.REPLACE_EXISTING);  // 임시파일 -> 실제파일 이동
                }
            }
        }
        return result;
    }
}

package com.example.boardstudy2.Utils;

import com.example.boardstudy2.common.Common;
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
import java.util.UUID;

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
     *
     * 추가해야됌 취소 및 브라우저 창 껐을 시 임시폴더 파일 삭제
     * @param map
     * @return
     * @throws IOException
     */
    public Map<String, Object> UploadFile(Map<String, Object> map) throws IOException {
        String result                  = "SUCCESS";                           // 성공 여부
        String userId                  = (String) map.get("userId");          // 등록자ID
        int seq                        = (Integer) map.get("rseq");           // 시퀀스
        String finalFolderNm           = userId + "/" + seq;                  // 폴더 경로
        Path tmpDir                    = Paths.get(tempDir);                  // 임시 파일 폴더 경로
        Path finalDir                  = Paths.get(uploadDir, finalFolderNm); // 최종 파일 폴더 경로
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
                    String uuid             = UUID.randomUUID().toString().replaceAll("-", "");  // uuid 랜덤키 생성
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
     * 첨부파일 삭제
     * @param map
     * @return
     * @throws IOException
     */
    public void FileDelete(Map<String, Object> map) throws IOException {
        String userId   = (String) map.get("regId");
        String seq      = (String) map.get("SEQ");
        String filePath = userId + "/" + seq;
        File finalDir   = new File(uploadDir + filePath);
        File targetDir = new File(uploadDir + userId);

        // 파일 삭제
        if(finalDir.exists()){
            File[] files = finalDir.listFiles();

            // 폴더 삭제
            if (files != null) {
                Common.fileDel(files);
                Common.fileDelFolder(finalDir, targetDir);
            }
        }
    }
}

package com.example.boardstudy2.Utils;

import com.example.boardstudy2.common.Common;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private HttpSession httpSession;

    /**
     * 파일 임시 저장
     * @param files
     * @return
     * @throws IOException
     */
    public List<String> TempFile(List<MultipartFile> files) throws IOException {
        List<String> fileNmList = new ArrayList<>();
        String session          = httpSession.getId(); // 세션 ID 가져오기
        String strTmpDir        = tempDir + session;
        File tmpDir             = new File(strTmpDir); // 파일타입

        // 폴더가 존재하지 않으면 새로 생성
        if(!tmpDir.exists()){
            tmpDir.mkdirs();
        }

        // 파일 저장 및 파일 상태 검증
        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();                             // 원본 파일명
            Path tempPath           = Paths.get(tempDir + session, originalFileName);    // 임시파일 경로 + 원본 파일명 합치기

            Files.copy(file.getInputStream(), tempPath, StandardCopyOption.REPLACE_EXISTING); // 임시저장

            fileNmList.add(originalFileName);                                                 // 원본 파일명 추출
        }
        httpSession.setAttribute("sessionId", session); // 세션 ID 저장
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
        String session                 = (String) httpSession.getAttribute("sessionId"); // 세션 ID 가져오기
        int seq                        = (Integer) map.get("rseq");
        String strTmpDir               = tempDir + session;
        Path tmpDir                    = Paths.get(tempDir + session);   // 임시 파일 폴더 경로
        Path finalDir                  = Paths.get(uploadDir + seq);     // 최종 파일 폴더 경로
        List<String> originalFileNames = new ArrayList<>();                   // 원본파일명 리스트
        List<String> uniqueFileNames   = new ArrayList<>();                   // 유일파일명 리스트
        File sessionFile               = new File(strTmpDir);                 // 임시 세션 폴더

        // 임시 폴더에 파일이 존재하는지 확인
        if (Common.fileCheck(strTmpDir)) {
            // 최종 폴더가 존재하지 않으면 생성
            if (!Files.exists(finalDir)) {
                Files.createDirectories(finalDir);
            }

            // 임시 폴더의 모든 파일 이동
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(tmpDir);
            for (Path filePath : directoryStream) {
                // 파일명 가져오기 (파일명만 추가)
                if (Files.isRegularFile(filePath)) {                                                             // 파일인지 확인
                    String originalFileName = Common.getFile(filePath);                                          // 원본 파일명
                    String fileExtension    = originalFileName.substring(originalFileName.lastIndexOf('.')); // 확장자와 파일명 분리
                    String uuid             = Common.UUIDCreate();                                               // uuid 랜덤키 생성
                    String uniqueFileName   = uuid + fileExtension;                                              // 확장자를 uuid에 추가
                    Path targetPath         = finalDir.resolve(uniqueFileName);                                  // 실제 경로에 해당파일을 uuid이름으로 이동
                    Files.move(filePath, targetPath, StandardCopyOption.REPLACE_EXISTING);                       // 임시파일 -> 실제파일 이동

                    uniqueFileNames.add(uuid);
                    originalFileNames.add(originalFileName);
                }
            }

            Common.fileDelFolder(sessionFile);              // 세션 ID 폴더 삭제
            httpSession.removeAttribute("sessionId"); // 세션 ID 세션에서 삭제
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
    public void GetUploadFile(List<Map<String, Object>> map) throws Exception {
        String session   = httpSession.getId();             // 세션 ID 가져오기
        int seq          = (Integer) map.get(0).get("seq");
        String strTmpDir = tempDir + session;
        Path tmpPath     = Paths.get(strTmpDir);            // 임시 파일 폴더 경로
        Path finalPath   = Paths.get(uploadDir + seq); // 최종 파일 폴더 경로
        File sessionFile = new File(strTmpDir);             // 임시 세션 폴더

        Common.fileaddFolder(sessionFile); // 폴더 생성

        // 디렉터리 내부의 파일 리스트 가져오기
        File[] files = finalPath.toFile().listFiles();

        for (File file : files) {
            if (file.isFile()) { // 파일인 경우만 복사
                Path sourceFile = file.toPath();
                Path targetFile = tmpPath.resolve(file.getName()); // 대상 파일 경로 설정

                Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        httpSession.setAttribute("sessionId", session); // 세션 ID 저장
    }

    /**
     * 파일 삭제
     * @param map
     * @throws IOException
     */
    public String DelFile(Map<String, Object> map) throws IOException {
        String result             = "SUCCESS";
        String seq                = (String) map.get("SEQ");                // 시퀀스
        Path finalPath            = Paths.get(uploadDir + seq);        // 최종 파일 폴더 경로
        List<String> delFileNames = (List<String>) map.get("delFileNames"); // 삭제할 파일 리스트

        if (delFileNames != null) {
            for (String delFile : delFileNames) {
                String fileToDelete = null;

                // 최종 폴더에서 삭제 대상 파일 찾기
                if (Files.exists(finalPath) && Files.isDirectory(finalPath)) {
                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(finalPath)) {
                        for (Path filePath : stream) {
                            String fileName = filePath.getFileName().toString();
                            if (fileName.startsWith(delFile + ".")) { // 파일명이 삭제 대상과 일치하는지 확인
                                fileToDelete = fileName;              // 일치하면 변수에 담아줌.
                                break;
                            }
                        }
                    } catch (Exception e) {
                        result = "ERROR";
                    }
                }
                Common.specifcDel(finalPath, fileToDelete); // 특정 파일 삭제
            }
        }
        return result;
    }

    /**
     * 파일 추가
     * @param map
     * @return
     * @throws IOException
     */
    public Map<String, Object> AddMoveFile(Map<String, Object> map) throws IOException {
        String result                  = "SUCCESS";
        String session                 = httpSession.getId();                     // 세션 ID 가져오기
        String strTmpDir               = tempDir + session;
        String seq                     = (String) map.get("SEQ");                 // 시퀀스
        Path tmpDir                    = Paths.get(strTmpDir + "/");         // 임시 파일 폴더 경로
        Path finalDir                  = Paths.get(uploadDir + seq);         // 최종 파일 폴더 경로
        List<String> addFileNames      = (List<String>) map.get("addFileNames");  // 추가된 파일 리스트
        List<String> originalFileNames = new ArrayList<>();                       // 원본 파일명 리스트
        List<String> uniqueFileNames   = new ArrayList<>();                       // UUID 파일명 리스트
        File sessionFile = new File(strTmpDir);                                   // 임시 세션 폴더

        // 파일이 존재하는지 확인
        if (Common.fileCheck(strTmpDir)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(tmpDir)) {
                for (Path filePath : directoryStream) {
                    if (Files.isRegularFile(filePath)) {
                        String originalFileName = filePath.getFileName().toString();

                        // 기존 UUID 파일이라면 그대로 유지
                        if (Common.UUIDYn(originalFileName)) { // UUID 형식인지 확인
                            Common.moveFile(filePath, finalDir, originalFileName); // 임시 폴더 -> 실제 폴더
                        }
                        // 새로운 파일이면 UUID로 변환 후 이동
                        else if (addFileNames.contains(originalFileName)) {
                            String fileExtension  = originalFileName.substring(originalFileName.lastIndexOf('.')); // 확장자
                            String uuid           = Common.UUIDCreate();                                              // UUID 생성
                            String uniqueFileName = uuid + fileExtension;                                             // UUID 파일명 생성

                            Common.moveFile(filePath, finalDir, uniqueFileName); // 임시 폴더 -> 실제 폴더
                            uniqueFileNames.add(uuid);
                            originalFileNames.add(originalFileName);
                        }
                    }
                }
                Common.fileDelFolder(sessionFile);              // 세션 ID 폴더 삭제
                httpSession.removeAttribute("sessionId"); // 세션 ID 세션에서 삭제
            }
        } else {
            result = "ERROR";
        }

        map.put("result"           , result);
        map.put("originalFileNames", originalFileNames);
        map.put("uniqueFileNames"  , uniqueFileNames);
        return map;
    }
}
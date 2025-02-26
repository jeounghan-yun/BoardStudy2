package com.example.boardstudy2.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class Common {

    /**
     * 데이터 존재하는지 여부 체크
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str){
        if(str == null || str == "undefined" || str == ""){
            return true;
        }
        return false;
    }

    /**
     * 파일이 존재하는지 여부 체크
     * @param tempDir
     * @return
     */
    public static boolean fileCheck (String tempDir) {
        Path tmpDir   = Paths.get(tempDir);           // 임시 파일 폴더 경로


        if (Files.exists(tmpDir) && Files.isDirectory(tmpDir)) {
            try {
                boolean hasFiles = Files.list(tmpDir).findAny().isPresent(); // 파일이 하나라도 있으면 true 반환
                if(hasFiles) {
                    return true;
                }

            } catch (IOException e) {
                e.printStackTrace(); // 예외 발생 시 오류 출력
            }
        }
        return false;
    }

    /**
     * 파일 삭제 전부 삭제
     * @param filesInDir
     */
    public static void fileDel (File[] filesInDir) {
        if (filesInDir != null && filesInDir.length > 0) {
            for (File f : filesInDir) {         // 폴더 내 파일 삭제
                if (f.exists() && f.isFile()) { // 파일 존재 여부 확인
                    f.delete();                 // 파일 삭제
                }
            }
        }
    }

    /**
     * 폴더 삭제
     * @param finalDir
     */
    public static void fileDelFolder (File finalDir) {
        if (finalDir.exists() && finalDir.isDirectory() && finalDir.list().length == 0) {
            finalDir.delete(); //폴더 삭제
        }
    }

    /**
     * UUID 형식 확인
     * @param orginalFileName
     * @return
     */
    public static boolean UUIDYn (String orginalFileName) {
        if(orginalFileName.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\..+$")) {
            return true;
        }
        return false;
    }

    /**
     * UUID 생성
     * @return
     */
    public static String UUIDCreate() {
        String uuid;
        uuid = UUID.randomUUID().toString();
        return uuid;
    }

    /**
     * 파일 이동
     * @param beforeFile
     * @param afterFile
     * @param fileName
     */
    public static void moveFile(Path beforeFile, Path afterFile, String fileName) {
        try {
            Files.move(beforeFile, afterFile.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace(); // 오류 로그 출력
        }
    }

    /**
     * 파일 이름 가져오기
     * @param filePath
     * @return
     */
    public static String getFile(Path filePath) {
        String fileName = filePath.getFileName().toString();
        return fileName;
    }
}



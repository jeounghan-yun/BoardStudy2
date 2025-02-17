package com.example.boardstudy2.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static boolean FileCheck (String tempDir) {
        Path tmpDir   = Paths.get(tempDir);           // 임시 파일 폴더 경로

        if(Files.exists(tmpDir) && Files.isDirectory(tmpDir)) {
            return true;
        }
        return false;
    }

    /**
     * 파일 삭제
     * @param filesInDir
     */
    public static void fileDel (File[] filesInDir) {
        if (filesInDir != null) {
            for (File f : filesInDir) {         // 폴더 내 파일 삭제
                if (f.exists() && f.isFile()) { // 파일 존재 여부 확인
                    f.delete();                 // 파일 삭제
                }
            }
        }
    }

}



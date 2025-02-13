package com.example.boardstudy2.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    /**
     * 존재하는지
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str){
        if(str == null || str == "undefined" || str == ""){
            return true;
        }
        return false;
    }
}



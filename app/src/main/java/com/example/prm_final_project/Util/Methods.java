package com.example.prm_final_project.Util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Methods {

    public static String generateFlashCardId(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String id = timestamp.getTime()+"";
        return id;
    };

    public static String getTime(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return sdf.format(timestamp)+"";
    };

}

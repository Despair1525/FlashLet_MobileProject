package com.example.prm_final_project.Util;

import android.util.Log;

import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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
    public static String getDate(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        return sdf.format(timestamp)+"";
    };
    public static String convertMonthYear(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM - yyyy");
        return sdf.format(date);
    };
    public static Long getTimeLong(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    };



    public static int compareStringDate(String o1 , String o2)  {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            Date date1 = sdf.parse(o1);
            Date date2 = sdf.parse(o2);
            return date2.compareTo(date1);
        }
        catch (Exception e){
        };
       return 0;
    };

    public static int compareStringDateDay(String o1 , String o2)  {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
            Date date1 = sdf.parse(o1);
            Date date2 = sdf.parse(o2);
            return date2.compareTo(date1);
        }
        catch (Exception e){
        };
        return 0;
    };
    public static int minusStringDate(String o1 , String o2)  {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
            Date date1 = sdf.parse(o1);
            Date date2 = sdf.parse(o2);

            long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
            int diff = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            return diff;
        }
        catch (Exception e){
            Log.i("main-date","errors");
        };
        return 1;
    };



    public static Long convertDateString(String o1 ) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        Date date1 = sdf.parse(o1);
        return date1.getTime();
    };

    public static int indexDeck (ArrayList<Deck> deckList , Deck findDeck) {
        for(int i = 0 ;i<deckList.size() ;i++) {
            if(deckList.get(i).getDeckId().equalsIgnoreCase(findDeck.getDeckId()))
                return i;
        };
        return -1;
    };
    public static  LocalDateTime caculateCalender(int x){
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault()).plusDays(x);
//        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        return ldt;
    };

}

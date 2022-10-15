package com.example.prm_final_project.Util;

import com.example.prm_final_project.Model.Deck;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public static int compareStringDate(String o1 , String o2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date date1 = sdf.parse(o1);
        Date date2 = sdf.parse(o2);
        return date2.compareTo(date1);
    };

    public static int indexDeck (ArrayList<Deck> deckList , Deck findDeck) {
        for(int i = 0 ;i<deckList.size() ;i++) {
            if(deckList.get(i).getDeckId().equalsIgnoreCase(findDeck.getDeckId()))
                return i;
        };
        return -1;
    };

}

package com.example.prm_final_project.Util;

import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class Methods {
    public final static String usernameRegex = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
//    public final static String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    public final static String passwordRegex = "[\\s\\S]*";
    public final static String emailRegex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    public final static String phoneRegex = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$";

    /**
     * Email: https://www.w3resource.com/javascript/form/email-validation.php
     * Username: https://mkyong.com/regular-expressions/how-to-validate-username-with-regular-expression/
     * Password: https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a
     * Phone: https://stackoverflow.com/questions/16699007/regular-expression-to-match-standard-10-digit-phone-number
     */

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
    public static Long getTimeLong(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
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

    public static String getUsername(String username){
        String mUsername="";
        if(Pattern.matches(usernameRegex, username))
            mUsername = username;
        return mUsername;
    };

    public static String getEmail(String email){
        String mEmail="";
        if(Pattern.matches(emailRegex, email))
            mEmail = email.toLowerCase();
        return mEmail;
    };

    public static String getPhone(String phone){
        String mPhone="";
        if(Pattern.matches(phoneRegex, phone))
            mPhone = phone;
        return mPhone;
    };

    public static String getPassword(String password){
        String mPassword="";
        if(Pattern.matches(passwordRegex, password))
            mPassword = password;
        return mPassword;
    };

//    public static boolean[] isDuplicate(String email, String phone){
//        boolean[] check = {false, false};
//        for (User user : UserDao.allUsers) {
//            if(user.getEmail().equals(email))
//                check[0] = true;
//            if(user.getPhone().equals(phone))
//                check[1] = true;
//        }
//        return check;
//    }
}

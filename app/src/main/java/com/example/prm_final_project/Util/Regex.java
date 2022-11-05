package com.example.prm_final_project.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Regex {
    /**
     * Email: https://www.w3resource.com/javascript/form/email-validation.php
     * Username: https://mkyong.com/regular-expressions/how-to-validate-username-with-regular-expression/
     * Password: https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a
     * Phone: https://stackoverflow.com/questions/16699007/regular-expression-to-match-standard-10-digit-phone-number
     */

    public final static String usernameRegex = "^[a-zA-Z0-9]([\\s._-](?![\\s._-])|[a-zA-Z0-9]){3,28}[a-zA-Z0-9]$";
    public final static String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
//    public final static String passwordRegex = "[\\s\\S]*";
    public final static String emailRegex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    public final static String phoneRegex = "^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";

    public static String textNormalization(String s) {
        return s.trim().replaceAll(" +", " ");
    }


    public static String getUsername(String username) {
        String mUsername = "";
        if (Pattern.matches(usernameRegex, username))
            mUsername = username;
        return mUsername;
    }

    ;

    public static String getEmail(String email) {
        String mEmail = "";
        if (Pattern.matches(emailRegex, email))
            mEmail = email.toLowerCase();
        return mEmail;
    }

    ;

    public static String getPhone(String phone) {
        String mPhone = "";
        if (Pattern.matches(phoneRegex, phone))
            mPhone = phone;
        return mPhone;
    }

    ;

    public static String getPassword(String password) {
        String mPassword = "";
        if (Pattern.matches(passwordRegex, password))
            mPassword = password;
        return mPassword;
    }

    ;

    public static List<List<String>> transformStringToDeck(String dataRaw, String regex1, String regex2) {
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);

        List<List<String>> deck = new ArrayList<>();
        List<String> deckRaw = Arrays.asList(pattern1.split(dataRaw));
        for (String card : deckRaw) {
            List<String> newCard = new LinkedList<String> (Arrays.asList(pattern2.split(card,2)));
            if (newCard.size() < 2) {
                newCard.add("");
            }
            deck.add(newCard);
        }
        return deck;
    }
        ;
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

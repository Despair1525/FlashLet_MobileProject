package com.example.prm_final_project.Util;

public class Regex {
    public static String textNormalization(String s){
        return s.trim().replaceAll(" +", " ");
    }
}

package com.example.prm_final_project.Dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDao {

    public static FirebaseUser getUser(){
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user==null){
            return null;
        }

        return user;
    }
}

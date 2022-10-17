package com.example.prm_final_project.Dao;

import com.example.prm_final_project.Model.FavoriteDeck;
import com.example.prm_final_project.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

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

    public static void addUser(User user) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).setValue(user);

    };
}

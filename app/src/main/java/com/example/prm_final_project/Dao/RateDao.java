package com.example.prm_final_project.Dao;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prm_final_project.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RateDao {


    public static FirebaseDatabase data = FirebaseDatabase.getInstance();
    public static DatabaseReference ref;
    public static ArrayList<User> allUsers = new ArrayList<>();

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
    public static void readAllUsers(ArrayList<User> allUsers){
        ref = data.getReference("Users");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u = snapshot.getValue(User.class);
                allUsers.add(u);
                Log.d("USERDAO_childAdded", "id " + u.getUsername());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u = snapshot.getValue(User.class);
                int index = getIndexByKey(allUsers, snapshot);
                if(index != -1) {
                    allUsers.set(index, u);
                }

                Log.d("USERDAO_childChanged", "username " + u.getUsername());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = getIndexByKey(allUsers, snapshot);
                if(index != -1) {
                    allUsers.remove(index);
                }

                Log.d("USERDAO_childRemoved", "username " + snapshot.getValue(User.class).getUsername());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("USERDAO_childMoved", "username " + snapshot.getValue(User.class).getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("USERDAO_cancelled", "error " + error.getMessage());
            }
        });
    }
    public static void logout(){
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }
    public static int getIndexByKey(ArrayList<User> allUsers, DataSnapshot ds){
        if(ds == null){
            return -1;
        }
        User u = ds.getValue(User.class);
        for (int i = 0; i < allUsers.size(); i++) {
            if(u.getUserId().equals(allUsers.get(i).getUserId())){
                return i;
            }
        }
        return -1;
    }
}

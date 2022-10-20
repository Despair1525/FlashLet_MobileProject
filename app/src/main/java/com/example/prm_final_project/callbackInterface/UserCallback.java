package com.example.prm_final_project.callbackInterface;

import com.example.prm_final_project.Model.User;

import java.util.ArrayList;

public interface UserCallback {
    void onResponse(ArrayList<User> allUsers, User changeUser, int type);
}

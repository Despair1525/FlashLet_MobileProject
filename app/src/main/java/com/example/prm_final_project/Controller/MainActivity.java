package com.example.prm_final_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.R;

// Khởi động ban đầu sẽ vào loading screen để load Database

public class MainActivity extends AppCompatActivity {
    private ProgressBar pbMain;
    private boolean isGuest = true; // Hiện tại đại để mặc định là Guest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        pbMain = findViewById(R.id.pbLoadDecks);
        pbMain.setVisibility(View.VISIBLE);
        Intent i ;

        i = new Intent(MainActivity.this, LoginActivity.class);
//        else {
//            /// Chuyển đến Public Desk
//        };

//        i.putExtra("allDecks", allDecks); // Chuyển dữ liệu sang
        startActivity(i);

    }
    //////  Hàm Load Database

//    public FirebaseAuth getmAuth(){
//        mAuth = FirebaseAuth.getInstance();
//        return mAuth;
//    }
//    public FirebaseUser getCurrUser(){
//        mAuth = getmAuth();
//        FirebaseUser user = mAuth.getCurrentUser();
//        return user;
//    }

//// Sau khi load xong thì chuyển trang




}
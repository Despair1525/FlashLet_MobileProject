package com.example.prm_final_project.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kotlin.time.Duration;

// Khởi động ban đầu sẽ vào loading screen để load Database

public class MainActivity extends AppCompatActivity {
    private boolean isGuest = false;
    private ArrayList<Deck> allDecks = new ArrayList<>();
    private FirebaseAuth mAuth;
    FirebaseDatabase rootRef;// Hiện tại đại để mặc định là Guest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Check Connection
        if (isConnectedToInternet()) {
            //

            rootRef = FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();
            isGuest = checkGuest();
            if(isGuest) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);

            }else{
                Intent i = new Intent(MainActivity.this,HomePageActivity.class);
                startActivity(i);
            };

            finish();

//            readAllDecks(rootRef.getReference("Decks"), new OnGetDataListener() {
//                @Override
//                public void onSuccess(DataSnapshot dataSnapshot) {
//                    Toast.makeText(MainActivity.this, "Load Database Success", Toast.LENGTH_SHORT).show();
//                }
//                @Override
//                public void onFailure() {
//                    Toast.makeText(MainActivity.this, "Load Database Fail", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
        else {
            Toast.makeText(MainActivity.this,"Not connect to internet",Toast.LENGTH_SHORT).show();
            // Gửi sang trang Lỗi kết nối
        };

    }
    public FirebaseAuth getmAuth(){
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    public boolean checkGuest(){
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        if (user==null){
            return true;
        }
        return false;
    }
    public FirebaseUser getCurrUser(){
        mAuth = getmAuth();
        FirebaseUser user = mAuth.getCurrentUser();
        return user;
    }

    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshot);
        void onFailure();
    }


    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }







}
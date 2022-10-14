package com.example.prm_final_project.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;
import com.example.prm_final_project.fragment.CreateDeckFragment;
import com.example.prm_final_project.fragment.HomeFragment;
import com.example.prm_final_project.fragment.ProfileFragment;
import com.example.prm_final_project.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

// Khởi động ban đầu sẽ vào loading screen để load Database

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    private boolean isGuest = false;
    private ArrayList<Deck> allDecks = new ArrayList<>();
    private FirebaseAuth mAuth;
    private final static int HOME_FRAGMENT = 0;
    private final static int SEARCH_FRAGMENT = 1;
    private final static int CREATE_FRAGMENT = 2;
    private final static int PROFILE_FRAGMENT = 3;
    private int CURRENT_FRAGMENT;

    FirebaseDatabase rootRef;// Hiện tại đại để mặc định là Guest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isConnectedToInternet()) {
            rootRef = FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();
            isGuest = checkGuest();
            if (isGuest) {
                Log.e("check guest", "true");
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);

            } else {
                Log.e("check guest", "false");
                // set home fragment by default
                loadFragment(new HomeFragment());
                CURRENT_FRAGMENT = HOME_FRAGMENT;
                bottomNavigationView = findViewById(R.id.bottom_navigation);
                bottomNavigationView.setOnNavigationItemSelectedListener(this);
//                Intent i = new Intent(MainActivity.this, HomePageActivity.class);
//                startActivity(i);
            }

        } else {
            Toast.makeText(MainActivity.this, "Not connect to internet", Toast.LENGTH_SHORT).show();
            // Gửi sang trang Lỗi kết nối
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                if(CURRENT_FRAGMENT != HOME_FRAGMENT){
                    loadFragment(fragment);
                    CURRENT_FRAGMENT = HOME_FRAGMENT;
                }
                return true;
            case R.id.nav_search:
                fragment = new SearchFragment();
                if(CURRENT_FRAGMENT != SEARCH_FRAGMENT){
                    loadFragment(fragment);
                    CURRENT_FRAGMENT = SEARCH_FRAGMENT;
                }
                return true;
            case R.id.nav_create:
                fragment = new CreateDeckFragment();
                if(CURRENT_FRAGMENT != CREATE_FRAGMENT){
                    loadFragment(fragment);
                    CURRENT_FRAGMENT = CREATE_FRAGMENT;
                }
                return true;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                if(CURRENT_FRAGMENT != PROFILE_FRAGMENT){
                    loadFragment(fragment);
                    CURRENT_FRAGMENT = PROFILE_FRAGMENT;
                }
                return true;
        }
        return true;
    }

    void loadFragment(Fragment fragment) {
        //to attach fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public FirebaseAuth getmAuth(){
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }
//
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
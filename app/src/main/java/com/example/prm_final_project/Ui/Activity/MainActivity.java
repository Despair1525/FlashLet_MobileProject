package com.example.prm_final_project.Ui.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Fragment.HomeFragment;
import com.example.prm_final_project.Ui.Fragment.ProfileFragment;
import com.example.prm_final_project.Ui.Fragment.SearchFragment;
import com.example.prm_final_project.Services.InternetConnection;
import com.example.prm_final_project.Ui.Fragment.SearchResultViewModel;
import com.example.prm_final_project.callbackInterface.AllDataCallback;
import com.example.prm_final_project.callbackInterface.UserCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Hashtable;

// Khởi động ban đầu sẽ vào loading screen để load Database

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private String m_Text;
    BottomNavigationView bottomNavigationView;
    private boolean isGuest = false;
    private boolean firstTime = true;
    private FirebaseAuth mAuth;
    private final static int HOME_FRAGMENT = 0;
    private final static int SEARCH_FRAGMENT = 1;
    private final static int CREATE_FRAGMENT = 2;
    private final static int PROFILE_FRAGMENT = 3;
    private int CURRENT_FRAGMENT;

    private Hashtable<String,Deck> allDecks  = new Hashtable<>();
    Fragment fragmentHome = new HomeFragment();;
    Fragment fragmentProfile = new ProfileFragment() ;
    FirebaseDatabase rootRef;// Hiện tại đại để mặc định là Guest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (InternetConnection.isConnectedToInternet(getApplicationContext())) {

            rootRef = FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();
            isGuest = checkGuest();

            UserDao.readAllUserFirst(new UserCallback() {
                @Override
                public void onResponse(ArrayList<User> allUsers, User changeUser, int type) {
                    if(type ==0){
                    DeckDao.readAllDecksOnce(new AllDataCallback() {
                        @Override
                        public void onResponse(Hashtable<String, Deck> allDecks) {
                            if (isGuest) {
                                Log.e("check guest", "true");
                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(i);

                            } else {
                                Log.e("check guest", "false");
                                // set home fragment by default
                                loadFragment(fragmentHome);
                                CURRENT_FRAGMENT = HOME_FRAGMENT;
                                bottomNavigationView = findViewById(R.id.bottom_navigation);
                                bottomNavigationView.setOnNavigationItemSelectedListener(MainActivity.this);
                            }
                        };

                    });}
                    else{
                        Log.i("here","Yolo");
                        Intent i = new Intent(MainActivity.this,NoInternetActivity.class);
                        startActivity(i);
                        finish();
                    };
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Not connect to internet", Toast.LENGTH_SHORT).show();
            // Gửi sang trang Lỗi kết nối
            Intent i = new Intent(this,NoInternetActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                if(CURRENT_FRAGMENT != HOME_FRAGMENT){
                    loadFragment(fragmentHome);
                    CURRENT_FRAGMENT = HOME_FRAGMENT;
                }
                return true;
            case R.id.nav_search:
                if(CURRENT_FRAGMENT != SEARCH_FRAGMENT){
                    loadFragment(new SearchFragment());
                    CURRENT_FRAGMENT = SEARCH_FRAGMENT;
                }
                return true;
            case R.id.nav_create:
                creatDeck();
                // do not set this fragment selected
                return false;
            case R.id.nav_profile:
                if(CURRENT_FRAGMENT != PROFILE_FRAGMENT){
                    loadFragment(fragmentProfile);
                    CURRENT_FRAGMENT = PROFILE_FRAGMENT;
                }
                return true;
        }
        return false;
    }

    // when press back button, return home fragment
    @Override
    public void onBackPressed() {
        if (bottomNavigationView.getSelectedItemId() == R.id.nav_home) {
            super.onBackPressed();
        } else {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    void loadFragment(Fragment fragment) {
        //to attach fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    public FirebaseAuth getmAuth(){
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

// create Deck
    public void creatDeck(){
        if(checkGuest()) {
            Toast.makeText(MainActivity.this,"You have to login !",Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Title");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT );
            builder.setView(input);
            builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                    Intent i =  new Intent(MainActivity.this,EditDeckActivity.class);
                    i.putExtra("editTitle",m_Text);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        };

    };
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




}
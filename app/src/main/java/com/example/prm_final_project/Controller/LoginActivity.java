package com.example.prm_final_project.Controller;

//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.R;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText edtEmail,edtPassword;
    private Button btnLogin;
    private TextView btnGuest, btnSignup;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (TextInputEditText) findViewById(R.id.edtLoginEmail);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPass);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGuest = (TextView) findViewById(R.id.tvGuest);
        btnSignup = (TextView)findViewById(R.id.tvSignup);

        progressBar = (ProgressBar)findViewById(R.id.loginPgBr);
        progressBar.setVisibility(View.INVISIBLE);

//        mAuth = FirebaseAuth.getInstance();
//
//        allDecks = (ArrayList<Deck>) getIntent().getSerializableExtra("allDecks")

        // Set Event cho Guest và signUp

        btnGuest.setClickable(true);
        btnSignup.setClickable(true);
//        btnLogin  Truy cap vao database check authentication

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PublishDecksActivity.class);
//                i.putExtra("allDecks", allDecks) // Send cac Decks Public sang
                startActivity(i);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
//                i.putExtra("allDecks",allDecks); Gửi Data sang bên Register
                startActivity(i);
            }
        });
        }
}
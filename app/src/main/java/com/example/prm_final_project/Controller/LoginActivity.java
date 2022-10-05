package com.example.prm_final_project.Controller;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText edtEmail,edtPassword;
    private Button btnLogin;
    private TextView btnGuest, btnSignup;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
//        mAuth = FirebaseAuth.getInstance();
//        allDecks = (ArrayList<Deck>) getIntent().getSerializableExtra("allDecks")



//      Truy cap vao database check authentication
        btnLogin.setOnClickListener(view -> onLogin());
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
                onSignUp();
            }
        });
        }

    private void initUi(){
        edtEmail = (TextInputEditText) findViewById(R.id.edtLoginEmail);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPass);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGuest = (TextView) findViewById(R.id.tvGuest);
        btnSignup = (TextView)findViewById(R.id.tvSignup);

        progressDialog = new ProgressDialog(this);
    };
    private void onLogin(){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, ViewCardActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    };
    private void onSignUp(){
    Intent i = new Intent(this,RegisterActivity.class);
    startActivity(i);

    };
}
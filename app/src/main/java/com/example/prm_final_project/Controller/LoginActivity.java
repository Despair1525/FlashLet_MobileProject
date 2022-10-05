package com.example.prm_final_project.Controller;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText edtEmail,edtPassword;
    private Button btnLogin;
    private TextView btnGuest, btnSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private ArrayList<Deck> allDecks = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
        // Access Firebase

        connectDatabase();
        // Read all decks
        allDecks = (ArrayList<Deck>) getIntent().getSerializableExtra("allDecks");
//        mAuth.signOut();
//      Set Su kien cho nut
        btnLogin.setOnClickListener(view -> onLogin());
        btnGuest.setOnClickListener(view -> onGuest());
        btnSignup.setOnClickListener(view -> onSignUp());


    };


    private void connectDatabase() {
        mAuth = FirebaseAuth.getInstance();

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
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Please enter valid email !");
            edtEmail.requestFocus();
            return;
        }
        else if(password.length() < 6 || password.isEmpty()) { // Add 1 ham regex
            edtPassword.setError("Please enter valid password !");
            edtPassword.requestFocus();
            return;
        };

        // Authentication on firebase
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
                            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                            intent.putExtra("allDecks", allDecks);
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
    private void onGuest(){
        Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
        i.putExtra("allDecks", allDecks); // Send cac Decks Public sang
        startActivity(i);

    };

    private void onSignUp(){
    Intent i = new Intent(this,RegisterActivity.class);
    startActivity(i);
    };
}
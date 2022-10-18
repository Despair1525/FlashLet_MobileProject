package com.example.prm_final_project.Ui.Activity;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail,edtPassword;
    private Button btnLogin;
    private TextView btnGuest, btnSignup, tvForgotPassword;


    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private ArrayList<Deck> allDecks = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flashlet-25aye-default-rtdb.firebaseio.com/");




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
        btnSignup.setOnClickListener(view -> onSignUp());
        tvForgotPassword.setOnClickListener(view -> onForgotPassword());

    };

    private void connectDatabase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void initUi(){
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword =  findViewById(R.id.edtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (TextView)findViewById(R.id.tvSignup);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);

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
//                        if (task.isSuccessful()) {
                        progressDialog.dismiss();
//                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.putExtra("allDecks", allDecks);
//                            startActivity(intent);
//                            finishAffinity();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("allDecks", allDecks);
                                startActivity(intent);
                                finishAffinity();
                            }
                            else{
//                                Toast.makeText(LoginActivity.this, "Please verify your email address!",
//                                        Toast.LENGTH_SHORT).show();
                                mAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(LoginActivity.this, "Please verify your email address!",
                                                            Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                                mAuth.signOut();
                                            }
                                        });
                                mAuth.signOut();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                });
    };
//<<<<<<< Updated upstream
//    private void onGuest(){
////        Intent i = new Intent(getApplicationContext(), HomeFragment.class);
//=======
////    private void onGuest(){
////        Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
//>>>>>>> Stashed changes
////        i.putExtra("allDecks", allDecks); // Send cac Decks Public sang
////        startActivity(i);

//        Fragment fragment = new HomeFragment();
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//
//    };

    private void onSignUp(){
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
    };

    private void onForgotPassword(){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    };
}
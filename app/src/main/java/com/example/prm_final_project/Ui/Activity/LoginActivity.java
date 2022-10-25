package com.example.prm_final_project.Ui.Activity;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmail,edPassword;
    private Button btnLogin, btnGoogle, btnFacebook;
    private TextView btnGuest, btnSignup, tvForgotPassword;
    private String email, password;


    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private ArrayList<Deck> allDecks = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flashlet-25aye-default-rtdb.firebaseio.com/");
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
        // Access Firebase
        connectDatabase();
        // Read all decks
        allDecks = (ArrayList<Deck>) getIntent().getSerializableExtra("allDecks");
        btnLogin.setOnClickListener(view -> onLogin());
        btnSignup.setOnClickListener(view -> onSignUp());
        btnFacebook.setOnClickListener(view -> onLoginFacebook());
        btnGoogle.setOnClickListener(view -> onLoginGoogle());
        tvForgotPassword.setOnClickListener(view -> onForgotPassword());

    };


    private void connectDatabase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void initUi(){
        edEmail = findViewById(R.id.edtEmail);
        edPassword =  findViewById(R.id.edtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnGoogle = (Button) findViewById(R.id.btnGoogle);
        btnSignup = (TextView)findViewById(R.id.tvSignup);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        progressDialog = new ProgressDialog(this);
    };

    private boolean validate(){
        email = edEmail.getText().toString().trim();
        password = edPassword.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty()){
            if(email.isEmpty())
                edEmail.setError("Please enter your email!");
            if(password.isEmpty())
                edPassword.setError("Please enter your password!");
            return false;
        }
        email = Methods.getEmail(email);
        password = Methods.getPassword(password);
        if(email.isEmpty() || password.isEmpty()){
            if(email.isEmpty())
                edEmail.setError("Please enter valid email!");
            if(password.isEmpty())
                edPassword.setError("Please enter valid password!");
            return false;
        }
        return true;
    }

    private void onLogin(){
        if(validate()){
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("allDecks", allDecks);
                                startActivity(intent);
                                finish();
                            }
                            else{
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
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                });}
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

    private void onLoginFacebook(){

    };

    private void onLoginGoogle(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 100);
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                task.getResult(ApiException.class);
                finish();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }catch (ApiException e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
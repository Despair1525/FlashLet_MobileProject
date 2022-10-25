package com.example.prm_final_project.Ui.Activity;

//import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.Util.Regex;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmail,edPassword;
    private Button btnLogin, btnGoogle, btnFacebook;
    private TextView btnGuest, btnSignup, tvForgotPassword;
    private String email, password;
    private CallbackManager callbackManager;


    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private ArrayList<Deck> allDecks = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flashlet-25aye-default-rtdb.firebaseio.com/");
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private AccessToken token;

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
        btnGoogle.setOnClickListener(view -> onLoginGoogle());
        tvForgotPassword.setOnClickListener(view -> onForgotPassword());
        btnFacebook.setOnClickListener(view -> onLoginFacebook());
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
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
        FacebookSdk.sdkInitialize(LoginActivity.this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        token = loginResult.getAccessToken();
                        onLoginFacebook_();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(@NonNull FacebookException e) {
                    }
                });
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
        email = Regex.getEmail(email);
        password = Regex.getPassword(password);
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
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));

    };
    private void onLoginFacebook_(){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getAdditionalUserInfo().isNewUser()){
                        User newUser = new User(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getPhotoUrl().toString(),
                                mAuth.getCurrentUser().getPhoneNumber(), mAuth.getCurrentUser().getEmail());
                        UserDao.addUser(newUser);
                    }
                    LoginManager.getInstance().logOut();
                    finish();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("allDecks", allDecks);

                    startActivity(intent);

                }else{
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void onLoginGoogle(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 6969);
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);

        if (requestCode==6969){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                        User newUser = new User(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getDisplayName(), null,
                                                mAuth.getCurrentUser().getPhoneNumber(), mAuth.getCurrentUser().getEmail());
                                        UserDao.addUser(newUser);
                                    }
                                    gsc.signOut();
                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("allDecks", allDecks);

                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT);
                                }
                            }
                        });
            }catch (ApiException e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
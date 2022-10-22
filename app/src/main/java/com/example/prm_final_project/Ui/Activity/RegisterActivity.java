package com.example.prm_final_project.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edUserName;
    private EditText edEmail;
    private EditText edPass1;
    private EditText edPass2;
    private EditText edPhone;
    private Button btRegister;
    private TextView tvLogin;
    private ImageView backImg;
    private ProgressDialog progressDialog;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flashlet-25aye-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUi();
        btRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        backImg.setOnClickListener(this);
    }
private void initUi(){
    edUserName = findViewById(R.id.edtUsername);
    edEmail = findViewById(R.id.edtEmail);
    edPass1 = findViewById(R.id.edtPassword1);
    edPass2 = findViewById(R.id.edtPassword2);
    edPhone = findViewById(R.id.edtPhone);
    btRegister = findViewById(R.id.btn_signup);
    tvLogin = findViewById(R.id.tvLogin);
    backImg = findViewById(R.id.imageView2);
    progressDialog = new ProgressDialog(this);

};
    private void onRegister(){
        String email = edEmail.getText().toString();
        String password = edPass1.getText().toString();
        String phone = edPhone.getText().toString();
        String username = edUserName.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getInstance().getCurrentUser();
//                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                    .setDisplayName(edUserName.getText().toString()).build();;
//
//                            user.updateProfile(profileUpdates)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                Toast.makeText(RegisterActivity.this, "Register Succsess", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//                            User newUser = new User(user.getUid(),password,user.getDisplayName(),user.getPhotoUrl()+"",phone,user.getEmail(),
//                                    new ArrayList<String>(), true);
//
//                            UserDao.addUser(newUser);
//                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
//                            startActivity(intent);
//                            finishAffinity();
                            mAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                User newUser = new User(mAuth.getCurrentUser().getUid(),username,null,email,phone);
                                                UserDao.addUser(newUser);

                                                //                                                databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("avatar").setValue("null");
//                                                databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("email").setValue(email);
//                                                databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("phone").setValue(phone);
//                                                databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("userId").setValue(mAuth.getCurrentUser().getUid());
//                                                databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("username").setValue(username);

                                                Toast.makeText(RegisterActivity.this, "Register successfully. Pleas check your email for verification",
                                                        Toast.LENGTH_SHORT).show();

                                            }else{
                                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            mAuth.signOut();
                                        }
                                    });
                        } else {
                            Log.i("Faild",task.getException().getLocalizedMessage());
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    };
    @Override
    public void onClick(View view) {
       if(view == btRegister) {
            onRegister();
        };
       if(view == backImg || view == tvLogin) {
           Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
           startActivity(i);
           finish();
       }

    }

}
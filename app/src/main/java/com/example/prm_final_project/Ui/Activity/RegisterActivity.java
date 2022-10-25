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
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.Util.Regex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edUserName, edEmail, edPass1, edPass2, edPhone;
    private Button btRegister;
    private TextView tvLogin;
    private ImageView backImg;
    private ProgressDialog progressDialog;

    private String username, email, password1, password2, phone;
//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flashlet-25aye-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUi();
        btRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        backImg.setOnClickListener(this);
    }

    private void initUi() {
        edUserName = findViewById(R.id.edtUsername);
        edEmail = findViewById(R.id.edtEmail);
        edPass1 = findViewById(R.id.edtPassword1);
        edPass2 = findViewById(R.id.edtPassword2);
        edPhone = findViewById(R.id.edtPhone);
        btRegister = findViewById(R.id.btn_signup);
        tvLogin = findViewById(R.id.tvLogin);
        backImg = findViewById(R.id.imageView2);
        progressDialog = new ProgressDialog(this);
    }

    private boolean validate(){
        email = edEmail.getText().toString().trim();
        password1 = edPass1.getText().toString().trim();
        password2 = edPass2.getText().toString().trim();
        phone = edPhone.getText().toString().trim();
        username = edUserName.getText().toString().trim();
        if(email.isEmpty() || password1.isEmpty() || password2.isEmpty() || phone.isEmpty() || username.isEmpty()){
            if(email.isEmpty())
                edEmail.setError("Please fill out this field!");
            if(password1.isEmpty())
                edPass1.setError("Please fill out this field!");
            if(password2.isEmpty())
                edPass2.setError("Please fill out this field!");
            if(phone.isEmpty())
                edPhone.setError("Please fill out this field!");
            if(username.isEmpty())
                edUserName.setError("Please fill out this field!");
            return false;
        }
        email = Regex.getEmail(email);
        password1 = Regex.getPassword(password1);
        password2 = Regex.getPassword(password2);
        phone = Regex.getPhone(phone);
        username = Regex.getUsername(username);
        if(email.isEmpty() || password1.isEmpty() || password2.isEmpty() || phone.isEmpty() || username.isEmpty()){
            if(email.isEmpty())
                edEmail.setError("Please enter valid email!");
            if(password1.isEmpty())
                edPass1.setError("Please enter valid password!");
            if(password2.isEmpty())
                edPass2.setError("Please enter valid confirm password!");
            if(phone.isEmpty())
                edPhone.setError("Please enter valid phone number!");
            if(username.isEmpty())
                edUserName.setError("Please enter valid user name!");
            return false;
        }
//        boolean[] isDuplicate = Methods.isDuplicate(email, phone);
//        if(isDuplicate[0] || isDuplicate[1]){
//            if(isDuplicate[0])
//                edEmail.setError("Email already exists!");
//            if(isDuplicate[1])
//                edPhone.setError("Phone number already exists!");
//            return false;
//        }
        if(!password1.equals(password2)){
            edPass2.setError("The password confirmation does not match!");
            return false;
        }
        return true;
    }

    private void onRegister() {
        if(validate()){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getInstance().getCurrentUser();
                                mAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    User newUser = new User(mAuth.getCurrentUser().getUid(), username, null, email, phone);
                                                    UserDao.addUser(newUser);
                                                    Toast.makeText(RegisterActivity.this, "Register successfully. Pleas check your email for verification",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                                mAuth.signOut();
                                            }
                                        });
                            } else {
                                try{
                                    throw task.getException();
                                }catch(FirebaseAuthUserCollisionException e){
                                    edEmail.setError(e.getMessage());
                                    edEmail.requestFocus();
                                }catch(Exception e){
                                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        }
                    });
        }
    }

    ;

    @Override
    public void onClick(View view) {
        if (view == btRegister) {
            onRegister();
        }
        ;
        if (view == backImg || view == tvLogin) {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

    }

}
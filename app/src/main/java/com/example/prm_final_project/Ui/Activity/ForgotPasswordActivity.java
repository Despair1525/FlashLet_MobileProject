package com.example.prm_final_project.Ui.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.Util.Regex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnSubmit;
    private ProgressDialog progressDialog;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initUi();
        btnSubmit.setOnClickListener(view -> onSendVerificationEmail());
    }

    private void initUi(){
        edtEmail = findViewById(R.id.edtEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressDialog = new ProgressDialog(this);
    }

    private boolean validate(){
        email = edtEmail.getText().toString().trim();
        if(email.isEmpty()){
            edtEmail.setError("Please enter your email!");
            return false;
        }
        email = Regex.getEmail(email);
        if(email.isEmpty()){
            edtEmail.setError("Please enter valid email!");
            return false;
        }
        return true;
    }

    private void onSendVerificationEmail(){
        if(validate()){
        progressDialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = edtEmail.getText().toString().trim();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email sent.", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }}
}
package com.example.prm_final_project.Ui.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Regex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText editTextOldPassword, editTextNewPassword, editTextRePassword;
    TextView textViewForgotPassword;
    Button buttonSaveChangePassword;
    String oldPassword, newPassword, rePassword;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    FirebaseUser user;
    boolean[] isValid = {false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initUi();

        oldPassword = editTextOldPassword.getText().toString();
        newPassword = editTextNewPassword.getText().toString();
        rePassword = editTextRePassword.getText().toString();

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePasswordActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        buttonSaveChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangePassword();
            }
        });
    }

    private void onClickChangePassword() {
        if (validate()) {
            progressDialog.show();
//            user = UserDao.getUser();
//            newPassword = editTextNewPassword.getText().toString().trim();
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePasswordActivity.this, "Change password successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Change password failed!", Toast.LENGTH_SHORT).show();
                                //                                progressDialog.dismiss();
//                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
//                                builder.setTitle("Re-authenticate needed");
//                                builder.setMessage("You need to login again to change password!");
//                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                });
//                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        Toast.makeText(getBaseContext(), "Change password failed", Toast.LENGTH_SHORT).show();
//                                        onBackPressed();
//                                    }
//                                });
//                                dialog = builder.create();
                            }
                            progressDialog.dismiss();
                            onBackPressed();
                        }
                    });
        }

    }

    private boolean validate(){
        oldPassword = editTextOldPassword.getText().toString();
        newPassword = editTextNewPassword.getText().toString();
        rePassword = editTextRePassword.getText().toString();
        if(oldPassword.length()<6 || newPassword.length()<6 || rePassword.length()<6){
            if(oldPassword.length()<6)
                editTextOldPassword.setError("Password must have at least 6 characters!");
            if(newPassword.length()<6)
                editTextNewPassword.setError("Password must have at least 6 characters!");
            if(rePassword.length()<6)
                editTextRePassword.setError("Password must have at least 6 characters!");
            return false;
        }
        oldPassword = Regex.getPassword(oldPassword);
        newPassword = Regex.getPassword(newPassword);
        rePassword = Regex.getPassword(rePassword);
        if(oldPassword.isEmpty() || newPassword.isEmpty() || rePassword.isEmpty()){
            if(oldPassword.isEmpty())
                editTextOldPassword.setError("Please enter valid password!");
            if(newPassword.isEmpty())
                editTextNewPassword.setError("Please enter valid password!");
            if(rePassword.isEmpty())
                editTextRePassword.setError("Please enter valid confirm password!");
            return false;
        }


        user = UserDao.getUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
//                            Toast.makeText(ChangePasswordActivity.this, "Password correct!", Toast.LENGTH_SHORT).show();
//                            editTextOldPassword.setError("Password incorrect!");
                            isValid[0] = true;
                        } else {
                            editTextOldPassword.setError("Password incorrect!");
                            isValid[0] = false;
                        }
                    }
                });
        if(!isValid[0]) return false;

        if(!newPassword.equals(rePassword)){
            editTextRePassword.setError("The password confirmation does not match!");
            return false;
        }
        return true;
    }

    private void initUi() {
        progressDialog = new ProgressDialog(this);
        editTextOldPassword = findViewById(R.id.editTextOldPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextRePassword = findViewById(R.id.editTextRePassword);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonSaveChangePassword = findViewById(R.id.buttonSaveChangePassword);
    }
}
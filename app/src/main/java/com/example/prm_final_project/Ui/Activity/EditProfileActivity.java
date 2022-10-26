package com.example.prm_final_project.Ui.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Fragment.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    public static final int REQUEST_CODE_SELECT_IMAGE = 2;

    ImageView imageAvatar;
    TextView textViewUserName, textViewEmail, textViewPhone;
    EditText editTextUserName, editTextEmail, editTextPhone;
    Button btnSelectImage, btnEditProfile;
    Uri selectedImageUri;

    FirebaseUser firebaseUser;
    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initUi();

        firebaseUser = UserDao.getUser();
        user = UserDao.getCurrentUser();

        Intent intent = getIntent();
        String field = intent.getStringExtra("EditField");
        switch (field) {
            case "username":
                textViewUserName.setVisibility(View.VISIBLE);
                editTextUserName.setVisibility(View.VISIBLE);
                editTextUserName.setText(user.getUsername());
                btnEditProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userName = editTextUserName.getText().toString().trim();
                        if (userName.length()>0) {
                            user.setUsername(userName);
                            UserDao.addUser(user);
                            Toast.makeText(EditProfileActivity.this, "Edit display name successfully", Toast.LENGTH_SHORT).show();
                        }
                        onBackPressed();
                    }
                });
                break;
            case "email":
                textViewEmail.setVisibility(View.VISIBLE);
                editTextEmail.setVisibility(View.VISIBLE);
                editTextEmail.setText(user.getEmail());
                btnEditProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = editTextEmail.getText().toString().trim();
                        if (email.length()>0) {
                            user.setEmail(email);
                            UserDao.addUser(user);
                            Toast.makeText(EditProfileActivity.this, "Edit email successfully", Toast.LENGTH_SHORT).show();
                        }
                        onBackPressed();
                    }
                });
                break;
            case "phone":
                textViewPhone.setVisibility(View.VISIBLE);
                editTextPhone.setVisibility(View.VISIBLE);
                editTextPhone.setText(user.getPhone());
                btnEditProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phone = editTextPhone.getText().toString().trim();
                        if (phone.length()>0) {
                            user.setPhone(phone);
                            UserDao.addUser(user);
                            Toast.makeText(EditProfileActivity.this, "Edit phone number successfully", Toast.LENGTH_SHORT).show();
                        }
                        onBackPressed();
                    }
                });
                break;
        }

//        btnSelectImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(ContextCompat.checkSelfPermission(
//                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(
//                            EditProfileActivity.this,
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                            REQUEST_CODE_STORAGE_PERMISSION
//                    );
//                } else {
//                    selectImage();
//                }
//            }
//        });


//        String email = editTextEmail.getText().toString().trim();
//        String phone = editTextPhone.getText().toString().trim();



//        btnEditProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseUser user = UserDao.getUser();
//                User u = UserDao.getCurrentUser();
//                if (!userName.isEmpty()) {
//                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                            .setDisplayName(userName)
//                            .build();
//
//                    user.updateProfile(profileUpdates)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        u.setUsername(userName);
//                                    }
//                                }
//                            });
//                }
//                if (selectedImageUri != null){
//                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                            .setPhotoUri(selectedImageUri)
//                            .build();
//
//                    user.updateProfile(profileUpdates)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        u.setAvatar(selectedImageUri.toString());
//                                    }
//                                }
//                            });
//                }
//                if (!email.isEmpty()) {
//                    user.updateEmail(email)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        u.setEmail(email);
//                                    }
//                                }
//                            });
//                }
//                if (!phone.isEmpty()) {
//                    u.setPhone(phone);
//                }
//
//                u.setPhone("thanh26042001@gmail.com");
//                UserDao.addUser(u);
//                Toast.makeText(EditProfileActivity.this, "Edit profile successfully", Toast.LENGTH_SHORT).show();
//
//                onBackPressed();
//
////                Intent intent = new Intent(EditProfileActivity.this, ProfileFragment.class);
////                startActivity(intent);
////                finish();
//            }
//        });
    }

    private void initUi(){
//        imageAvatar = findViewById(R.id.imageAvatarEditProfile);
//        btnSelectImage = findViewById(R.id.btnSelectImage);

        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserName.setVisibility(View.GONE);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewEmail.setVisibility(View.GONE);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewPhone.setVisibility(View.GONE);

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextUserName.setVisibility(View.GONE);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextEmail.setVisibility(View.GONE);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPhone.setVisibility(View.GONE);
        btnEditProfile = findViewById(R.id.buttonSaveEditProfile);
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if(data != null) {
                selectedImageUri = data.getData();
                if(selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageAvatar.setImageBitmap(bitmap);

                        //Following is selected image file
                        File selectedImageFile = new File(getPathFromUri(selectedImageUri));

                    } catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri, null, null, null, null);
        if(cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences prefs= getSharedPreferences("prefs", MODE_PRIVATE);
        prefs.edit().putBoolean("isShouldReload", true).apply();
    }
}
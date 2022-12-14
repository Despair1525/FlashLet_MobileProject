package com.example.prm_final_project.Ui.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Activity.ChangePasswordActivity;
import com.example.prm_final_project.Ui.Activity.EditProfileActivity;
import com.example.prm_final_project.Ui.Activity.LoginActivity;
import com.example.prm_final_project.Ui.Activity.MyFlashcardsActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.example.prm_final_project.Ui.Activity.NotificationSettingsActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    public static final int REQUEST_CODE_SELECT_IMAGE = 2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imageAvatar;
    private TextView tvUsernameTitle, tvUsername, tvEmail, tvPhone;
//    private Button btnEditProfile, btnMyFlashCards, btnChangePassword, btnNotification, btnLogout;
    private CardView cvEditUsername, cvEditEmail, cvEditPhone, cvMyFlashCards, cvChangePassword, cvNotification, cvLogout;

    boolean isLoadImage = true;
    ProgressDialog progressDialog;

    Context thisContext;

    Uri selectedImageUri;
    FirebaseUser firebaseUser;
    User user;
    FirebaseStorage storage;



    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisContext = container.getContext();
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        initUi(view);

        progressDialog = new ProgressDialog(thisContext);

        firebaseUser = UserDao.getUser();

        user = UserDao.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        if (selectedImageUri==null) {
            if(user.getAvatar() != null && !user.getAvatar().equalsIgnoreCase("null")) {
                Uri imgUri = Uri.parse(user.getAvatar());
                try {
                    Glide.with(thisContext)
                            .load(imgUri)
                            .into(imageAvatar);
                } catch (Exception e){
                    imageAvatar.setImageResource(R.drawable.default_avatar);
                }
            } else {
                imageAvatar.setImageResource(R.drawable.default_avatar);
            }

        } else {
            imageAvatar.setImageURI(selectedImageUri);
        }

        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(
                        thisContext.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    if (isLoadImage == true) {
                        selectImage();
//                        openGallery();
                    } else {
                        Toast.makeText(getActivity(), "Need to wait for previous process finished",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tvUsernameTitle.setText(user.getUsername());
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(user.getPhone());
        cvEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("EditField","username");
                startActivity(intent);
            }
        });
//        cvEditEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
//                intent.putExtra("EditField","email");
//                startActivity(intent);
//            }
//        });
        cvEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("EditField","phone");
                startActivity(intent);
            }
        });
        cvMyFlashCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyFlashcardsActivity.class);
                startActivity(intent);
            }
        });
        cvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        cvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationSettingsActivity.class);
                startActivity(intent);
            }
        });
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDao.logout();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);
        if (prefs.getBoolean("isShouldReload", false)) {
            User user = UserDao.getCurrentUser();
            tvUsernameTitle.setText(user.getUsername());
            tvUsername.setText(user.getUsername());
            tvEmail.setText(user.getEmail());
            tvPhone.setText(user.getPhone());
        }
    }

    private void initUi(View view) {
        imageAvatar = view.findViewById(R.id.imageAvatar);
        tvUsernameTitle = view.findViewById(R.id.tvUsername);
        tvUsername = view.findViewById(R.id.tvUsernameProfile);
        tvEmail = view.findViewById(R.id.tvEmailProfile);
        tvPhone = view.findViewById(R.id.tvPhoneProfile);
        cvEditUsername = view.findViewById(R.id.CardViewEditUsername);
        cvEditEmail = view.findViewById(R.id.CardViewEditEmail);
        cvEditPhone = view.findViewById(R.id.CardViewEditPhone);
        cvChangePassword = view.findViewById(R.id.CardViewChangePassword);
        cvMyFlashCards = view.findViewById(R.id.CardViewMyFlashcards);
        cvNotification = view.findViewById(R.id.CardViewNotification);
        cvLogout = view.findViewById(R.id.CardViewLogout);
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(thisContext.getPackageManager()) != null) {
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
                Toast.makeText(getActivity(), "Permission denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if(data != null) {
                selectedImageUri = data.getData();
                if(selectedImageUri != null) {
                    try {
                        progressDialog.show();
                        progressDialog.setMessage("Loading image");
                        progressDialog.setCancelable(false);
                        InputStream inputStream = thisContext.getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageAvatar.setImageBitmap(bitmap);

                        isLoadImage = false;

                        //Following is selected image file
                        File selectedImageFile = new File(getPathFromUri(selectedImageUri));
//                        selectedImageFile.renameTo(new File(user.getUserId() + ".jpg"));
//
                        Uri file = Uri.fromFile(selectedImageFile);
                        storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference().child("images");
                        StorageReference riversRef = storageRef.child(user.getUserId() + ".jpg");
//                        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
//                        riversRef.delete();
                        if (user.getAvatar() != null){
                            riversRef.delete();
                        }
                        UploadTask uploadTask = riversRef.putFile(file);
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return riversRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    user.setAvatar(downloadUri.toString());
                                    UserDao.addUser(user);
                                    try {
                                        Toast.makeText(getContext(), "Edit avatar successfully", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Log.d("Profile Fragment toast","error");
                                    }
                                    isLoadImage = true;
                                } else {
                                    try {
                                        Toast.makeText(getContext(), "Fail to get image URL", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Log.d("Profile Fragment toast","error");
                                    }
                                }
                                progressDialog.dismiss();
                            }
                        });

                    } catch (Exception exception) {
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = thisContext.getContentResolver()
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
}
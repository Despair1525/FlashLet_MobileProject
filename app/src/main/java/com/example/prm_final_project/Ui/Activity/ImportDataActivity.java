package com.example.prm_final_project.Ui.Activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Adapter.EditCardAdapt;
import com.example.prm_final_project.Adapter.ViewCardImportAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.Util.Regex;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImportDataActivity extends AppCompatActivity implements View.OnClickListener {
    private static int PERMISSION_REQUEST_CODE= 25;
    private static int RESULT_REQUEST_CODE= 15;
    private String regex="[\\n]+";

    String[] permission={READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};
    ActivityResultLauncher<Intent> activityResultLauncher;

    Button btImportFile;
    ImageView IvSetting;
    TextView tvFileView,tvRegexCard,tvRegexCards;
    RecyclerView rvList;

    ViewCardImportAdapter adapter;
    private ProgressDialog dialog;

    private String regex1_default="[\\n]+";
    private String regex2_default="[|]+";

    private String rawString="";
    private List<List<String>> rawDeck = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_data);



        btImportFile = findViewById(R.id.btImport);
        tvFileView = findViewById(R.id.tvFileView);
        tvRegexCard = findViewById(R.id.tvRegexCard);
        tvRegexCards = findViewById(R.id.tvRegexCards);

        IvSetting = findViewById(R.id.IvSetting);
        rvList = findViewById(R.id.rcListImport);
        tvFileView.setMovementMethod(new ScrollingMovementMethod());
        btImportFile.setOnClickListener(this);
        IvSetting.setOnClickListener(this);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if(Environment.isExternalStorageManager()) {
                            Toast.makeText(getApplicationContext(),"PerMission Granted",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"PerMission Denied",Toast.LENGTH_SHORT).show();
                        };
                    };
                };
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

    }


    boolean checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        else{
            int readCheck = ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE);
            int writeCheck = ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
            return readCheck == PackageManager.PERMISSION_GRANTED  && writeCheck== PackageManager.PERMISSION_GRANTED;
        }
    }

    void requestPermisson(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                activityResultLauncher.launch(intent);
            }catch (Exception e) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activityResultLauncher.launch(intent);
            };
        } else {
            ActivityCompat.requestPermissions(ImportDataActivity.this,permission,PERMISSION_REQUEST_CODE);
        };
    };

    private void performFileSearh() {
        Intent intent  = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent,RESULT_REQUEST_CODE);
    }

    private String readText(String path){
        File file= new File(Environment.getExternalStorageDirectory(),path);
        StringBuilder text = new StringBuilder();
        try {
            InputStreamReader input = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(input);
            String line = br.readLine();
            while( line != null) {

                text.append(line);
                text.append("\n");
                line = br.readLine();
            };
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("String-file",e.getMessage());
        } catch (IOException e) {
            Log.i("String-file",e.getMessage());
            e.printStackTrace();
        }
        return text.toString();
    };
    private void setRycleView(List<List<String>> list){
        dialog = new ProgressDialog(ImportDataActivity.this);
        dialog.setMessage("Loading");
        dialog.show();
        adapter = new ViewCardImportAdapter(this,list);
        rvList.setAdapter( adapter);
        rvList.setLayoutManager(new LinearLayoutManager((this)));
        dialog.dismiss();
    };
    private void onSettingRegex(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your Regex");
        // Set up Layout for dialog

        LayoutInflater inflater = ImportDataActivity.this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.item_edit_card,null);
        builder.setView(layout);

        TextView tv1 = layout.findViewById(R.id.tv1);
        TextView tv2 = layout.findViewById(R.id.tv2);
        EditText inputFront =layout.findViewById(R.id.Editfront);
        EditText inputBack =layout.findViewById(R.id.Editback);

        tv1.setText("Enter betwwen front and back card");
        tv2.setText("Enter betwwen cards");

        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String regex1;
                String regex2;
                regex1 = inputFront.getText().toString().trim();
                regex2 = inputBack.getText().toString().trim();
                if(regex1.isEmpty() ) regex1 = regex1_default;
                if(regex2.isEmpty() ) regex2 = regex2_default;

                if(rawString != null) {
                    tvRegexCard.setText(regex1);
                    tvRegexCards.setText(regex2);
            Log.i("Regex","["+regex1+"]+");
                   List<List<String>> newList = Regex.transformStringToDeck(rawString,"["+regex1+"]+", "["+regex2+"]+");
                   rawDeck = newList;
                   setRycleView(newList);
                };
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    @Override
    public void onClick(View v) {
        if(v == btImportFile){
            if(checkPermission()){
                performFileSearh();}
            else {
                requestPermisson();
            };
        }
        if(v == IvSetting){
            onSettingRegex();
        };
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if(data != null){
                Uri uri = data.getData();
                String path = uri.getPath();
                path =path.substring(path.indexOf(":")+1);
                if(path.contains("emulated")) {
                    path=path.substring(path.indexOf("0")+1);
                };
                Toast.makeText(this, ""+path, Toast.LENGTH_SHORT).show();


                String dataRaw = readText(path);
                tvFileView.setText(dataRaw);
                List<List<String>> arrList = Regex.transformStringToDeck(dataRaw,regex1_default,regex2_default);
                rawDeck = arrList;
                rawString = dataRaw;
                setRycleView(arrList);
            };
        };
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE) {
            boolean readper = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writer = grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (grantResults.length > 0) {
                if (readper && writer) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Deny", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "You Deny", Toast.LENGTH_SHORT).show();
            };
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    };

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_data_menu,menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.SaveEditAction:
                onSave();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void onSave() {
        dialog = new ProgressDialog(ImportDataActivity.this);
        dialog.setMessage("Loading");
        dialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you want to Import Data to Deck ?");
        builder.setPositiveButton("Import", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent();
                Bundle args = new Bundle();
                args.putSerializable("importCard",(Serializable) rawDeck);
                i.putExtra("BUNDLE",args);
                setResult(15, i);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
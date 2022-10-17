package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.prm_final_project.R;

public class NoInternetActivity extends AppCompatActivity {
    Button btRetry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        btRetry = findViewById(R.id.btRety);
        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoInternetActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
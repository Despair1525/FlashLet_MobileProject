package com.example.prm_final_project.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.prm_final_project.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
private TextView btnLogout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnLogout = findViewById(R.id.tvLogout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnLogout) {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);

        };

    }
}
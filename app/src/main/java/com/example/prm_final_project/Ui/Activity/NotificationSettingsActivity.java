package com.example.prm_final_project.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Fragment.NotificationSettingFragment;

public class NotificationSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new NotificationSettingFragment())
                .commit();

        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}
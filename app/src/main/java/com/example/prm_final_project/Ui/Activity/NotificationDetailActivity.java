package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prm_final_project.R;

import org.w3c.dom.Text;

public class NotificationDetailActivity extends AppCompatActivity {

    private TextView message_detail;
    private Button bnt_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        init();
        Intent intent = getIntent();
        Log.i("message_quote", intent.getStringExtra("message_quote"));
        message_detail.setText(intent.getStringExtra("message_quote"));

        bnt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnHome();
            }
        });
        getSupportActionBar().setTitle("Notification Detail");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        returnHome();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnHome();
    }

    public void returnHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void init(){
        bnt_home = findViewById(R.id.btn_back_to_study);
        message_detail = findViewById(R.id.message_detail);
    }
}
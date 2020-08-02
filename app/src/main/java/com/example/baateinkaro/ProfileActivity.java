package com.example.baateinkaro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    private String receiveruserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        receiveruserid=getIntent().getStringExtra("visit_userid");
        Toast.makeText(this, "User Id"+receiveruserid, Toast.LENGTH_SHORT).show();
    }
}

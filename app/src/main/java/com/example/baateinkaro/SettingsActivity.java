package com.example.baateinkaro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private Button update;
    private EditText username,userstatus;
    private CircleImageView userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InitializeFields();
    }

    private void InitializeFields() {
        update=findViewById(R.id.update_settings);
        username=findViewById(R.id.set_username);
        userstatus=findViewById(R.id.set_status);
        userimage=findViewById(R.id.profile_image);
    }
}

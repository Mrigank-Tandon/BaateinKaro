package com.example.baateinkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private Button update;
    private EditText username,userstatus;
    private CircleImageView userimage;
    private String Cuurentuserid;
    private FirebaseAuth mauth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mauth=FirebaseAuth.getInstance();
        RootRef=FirebaseDatabase.getInstance().getReference();
        Cuurentuserid=mauth.getCurrentUser().getUid();
        InitializeFields();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });
    }

    private void updateSettings() {
        String setusername=username.getText().toString();
        String setStatus=userstatus.getText().toString();
        if(TextUtils.isEmpty(setusername)){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(setStatus)){
            Toast.makeText(this, "Please enter status", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, String>profilemap=new HashMap<>();
            profilemap.put("uid",Cuurentuserid);
            profilemap.put("name",setusername);
            profilemap.put("status",setStatus);
            RootRef.child("Users").child(Cuurentuserid).setValue(profilemap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                SendUserToMainActivity();
                            }
                            else
                            {
                                String message=task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void InitializeFields() {
        update=findViewById(R.id.update_settings);
        username=findViewById(R.id.set_username);
        userstatus=findViewById(R.id.set_status);
        userimage=findViewById(R.id.profile_image);
    }
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}

package com.example.baateinkaro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;


import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private Button update;
    private EditText username,userstatus;
    private CircleImageView userimage;
    private String Cuurentuserid;
    private FirebaseAuth mauth;
    private DatabaseReference RootRef;
    private static final int gallerypick=1;
    private StorageReference userprofileumageref;
    private ProgressDialog pictureupload;
    private Toolbar settingstoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mauth=FirebaseAuth.getInstance();
        RootRef=FirebaseDatabase.getInstance().getReference();
        userprofileumageref=FirebaseStorage.getInstance().getReference().child("Profile Images");
        Cuurentuserid=mauth.getCurrentUser().getUid();
        InitializeFields();
        username.setVisibility(View.INVISIBLE);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });
        retriveuserinfo();
        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,gallerypick);

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
            HashMap<String,Object>profilemap=new HashMap<>();
            profilemap.put("uid",Cuurentuserid);
            profilemap.put("name",setusername);
            profilemap.put("status",setStatus);
            RootRef.child("Users").child(Cuurentuserid).updateChildren(profilemap)
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

    private void retriveuserinfo() {
        RootRef.child("Users").child(Cuurentuserid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists())&&(dataSnapshot.hasChild("name")&&(dataSnapshot.hasChild("image")))){
                            String retrieveusername=dataSnapshot.child("name").getValue().toString();
                            String retrieveuserstatus=dataSnapshot.child("status").getValue().toString();
                            String retrieveuserimage=dataSnapshot.child("image").getValue().toString();
                            username.setText(retrieveusername);
                            userstatus.setText(retrieveuserstatus);
                            Glide.with(SettingsActivity.this).load(retrieveuserimage).into(userimage);

                        }
                        else if((dataSnapshot.exists())&&(dataSnapshot.hasChild("name"))){
                            String retrieveusername=dataSnapshot.child("name").getValue().toString();
                            String retrieveuserstatus=dataSnapshot.child("status").getValue().toString();
                            username.setText(retrieveusername);
                            userstatus.setText(retrieveuserstatus);

                        }
                        else{
                            username.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingsActivity.this, "Please Set Update your profile..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==gallerypick && resultCode==RESULT_OK && data!=null){
            Uri imageuri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                pictureupload.setTitle("Set Profile Image");
                pictureupload.setMessage("Please Wait, Your Profile Image is Updating...");
                pictureupload.setCanceledOnTouchOutside(false);
                pictureupload.show();
                Uri resultUri = result.getUri();
                final StorageReference filepath=userprofileumageref.child(Cuurentuserid+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this, "Profile Picture Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    RootRef.child("Users").child(Cuurentuserid).child("image")
                                            .setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SettingsActivity.this, "Imaged Saved in Database", Toast.LENGTH_SHORT).show();
                                                        pictureupload.dismiss();
                                                    }
                                                    else{
                                                        String message=task.getException().toString();
                                                        Toast.makeText(SettingsActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                        pictureupload.dismiss();
                                                    }

                                                }
                                            });
                                }
                            });


                        }
                        else{
                            String message=task.getException().toString();
                            pictureupload.dismiss();
                            Toast.makeText(SettingsActivity.this, "Error:"+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    private void InitializeFields() {
        update=findViewById(R.id.update_settings);
        username=findViewById(R.id.set_username);
        userstatus=findViewById(R.id.set_status);
        userimage=findViewById(R.id.profile_image);

        pictureupload=new ProgressDialog(this);
        settingstoolbar=findViewById(R.id.settings_toolbar);
        setSupportActionBar(settingstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Account Settings");
    }
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}

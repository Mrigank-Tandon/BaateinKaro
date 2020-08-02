package com.example.baateinkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String receiveruserid;
    private CircleImageView userprofileimage;
    private TextView userprofilename,userprofilestaus;
    private Button SendmessageButton;
    private DatabaseReference Userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Userref= FirebaseDatabase.getInstance().getReference().child("Users");
        receiveruserid=getIntent().getStringExtra("visit_userid");
        userprofileimage=findViewById(R.id.visit_profile);
        userprofilename=findViewById(R.id.visit_username);
        userprofilestaus=findViewById(R.id.visit_status);
        SendmessageButton=findViewById(R.id.message_button);
        Retrieveuserinfo();

    }

    private void Retrieveuserinfo() {
        Userref.child(receiveruserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&& (snapshot.hasChild("image"))){
                    String userimage=snapshot.child("image").getValue().toString();
                    String username=snapshot.child("name").getValue().toString();
                    String userstatus=snapshot.child("status").getValue().toString();
                    Glide.with(ProfileActivity.this).load(userimage).placeholder(R.drawable.profile_image).into(userprofileimage);
                    userprofilename.setText(username);
                    userprofilestaus.setText(userstatus);

                }
                else{
                    String username=snapshot.child("name").getValue().toString();
                    String userstatus=snapshot.child("status").getValue().toString();

                    userprofilename.setText(username);
                    userprofilestaus.setText(userstatus);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

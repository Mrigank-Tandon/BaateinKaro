package com.example.baateinkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String receiveruserid,senderuserid,currentstate;
    private CircleImageView userprofileimage;
    private TextView userprofilename,userprofilestaus;
    private Button SendmessageButton,DeclinerequestButton;
    private DatabaseReference Userref,chatreqref;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mauth=FirebaseAuth.getInstance();
        Userref= FirebaseDatabase.getInstance().getReference().child("Users");
        chatreqref= FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        senderuserid=mauth.getCurrentUser().getUid();
        receiveruserid=getIntent().getStringExtra("visit_userid");
        userprofileimage=findViewById(R.id.visit_profile);
        userprofilename=findViewById(R.id.visit_username);
        userprofilestaus=findViewById(R.id.visit_status);
        SendmessageButton=findViewById(R.id.message_button);
        DeclinerequestButton=findViewById(R.id.decline_button);
        currentstate="new";
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
                    ManageChatrequests();

                }
                else{
                    String username=snapshot.child("name").getValue().toString();
                    String userstatus=snapshot.child("status").getValue().toString();

                    userprofilename.setText(username);
                    userprofilestaus.setText(userstatus);
                    ManageChatrequests();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ManageChatrequests()
    {
        chatreqref.child(senderuserid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(receiveruserid)){
                            String request_type=snapshot.child(receiveruserid).child("request_type").getValue().toString();
                            if(request_type.equals("sent")){
                                currentstate="request_sent";
                                SendmessageButton.setText("Cancel Request");
                            }
                            else if(request_type.equals("received")){
                                currentstate="request_received";
                                SendmessageButton.setText("Accept Request");
                                DeclinerequestButton.setVisibility(View.VISIBLE);
                                DeclinerequestButton.setEnabled(true);
                                DeclinerequestButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cancelchatrequest();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
      if(!senderuserid.equals(receiveruserid)){
          SendmessageButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  SendmessageButton.setEnabled(false);
                  if(currentstate.equals("new")){
                      Sendchatrequest();
                  }
                  if(currentstate.equals("request_sent")){
                      cancelchatrequest();
                  }
              }
          });

      }
      else{
          SendmessageButton.setVisibility(View.INVISIBLE);
      }
    }

    private void cancelchatrequest() {
        chatreqref.child(senderuserid).child(receiveruserid)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            chatreqref.child(receiveruserid).child(senderuserid)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                SendmessageButton.setEnabled(true);
                                                currentstate="new";
                                                SendmessageButton.setText("Send Message");
                                                DeclinerequestButton.setVisibility(View.INVISIBLE);
                                                DeclinerequestButton.setEnabled(false);

                                            }
                                        }
                                    });

                        }
                    }
                });

    }

    private void Sendchatrequest() {
        chatreqref.child(senderuserid).child(receiveruserid).child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            chatreqref.child(receiveruserid).child(senderuserid).child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                SendmessageButton.setEnabled(true);
                                                currentstate="request_sent";
                                                SendmessageButton.setText("Cancel Request");
                                            }

                                        }
                                    });
                        }
                    }
                });


    }
}

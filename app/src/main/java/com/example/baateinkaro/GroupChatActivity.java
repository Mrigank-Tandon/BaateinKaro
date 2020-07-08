package com.example.baateinkaro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.DragStartHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {
    private Toolbar mtoolbar;

    private ImageButton sendmessagebutton;
    private EditText usermessageinput;
    private ScrollView mscrollview;
    private TextView displayTextmessage;
    private FirebaseAuth mauth;
    private DatabaseReference userref,grpnameref,grpmessagekeyref;
    private String currentgrpname,currentuserid,currentusername,currentdates,currrenttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        currentgrpname=getIntent().getExtras().get("groupname").toString();
        Toast.makeText(this, currentgrpname, Toast.LENGTH_SHORT).show();
        mauth=FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();
        userref= FirebaseDatabase.getInstance().getReference().child("Users");
        grpnameref=FirebaseDatabase.getInstance().getReference().child("Groups").child(currentgrpname);



        InitializeFields();
        getuserinfo();
        sendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savemessagetodatabase();
                usermessageinput.setText("");
                mscrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        grpnameref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Displaymessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Displaymessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void savemessagetodatabase() {
        String message=usermessageinput.getText().toString();
        String messagekey=grpnameref.push().getKey();
        if(TextUtils.isEmpty(message)){
            Toast.makeText(this, "please write message", Toast.LENGTH_SHORT).show();
        }
        else{
            Calendar fordate= Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("YYYY-MM-dd");
            currentdates =currentdate.format(fordate.getTime());
            Calendar fortime= Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("h:mm a");
            currrenttime =currenttime.format(fortime.getTime());
            HashMap<String, Object> gr = new HashMap<>();
            grpnameref.updateChildren(gr);
            grpmessagekeyref=grpnameref.child(messagekey);
            HashMap<String, Object> messinfo = new HashMap<>();
            messinfo.put("name",currentusername);
            messinfo.put("message",message);
            messinfo.put("date",currentdates);
            messinfo.put("time",currrenttime);
            grpmessagekeyref.updateChildren(messinfo);
        }
    }

    private void getuserinfo() {
        userref.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if(dataSnapshot.exists()){
                      currentusername=dataSnapshot.child("name").getValue().toString();
                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void InitializeFields() {
        mtoolbar=(Toolbar) findViewById(R.id.grp_app_bar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(currentgrpname);
        sendmessagebutton=(ImageButton) findViewById(R.id.send_message);
        usermessageinput=findViewById(R.id.group_message);
        displayTextmessage=findViewById(R.id.text_grp);
        mscrollview=findViewById(R.id.scroll_view);

    }
    private void Displaymessages(DataSnapshot dataSnapshot) {
          Iterator iterator=dataSnapshot.getChildren().iterator();
          while(iterator.hasNext()){
              String chatdate=(String)((DataSnapshot)iterator.next()).getValue();
              String chatmessage=(String)((DataSnapshot)iterator.next()).getValue();
              String chatname=(String)((DataSnapshot)iterator.next()).getValue();
              String chattime=(String)((DataSnapshot)iterator.next()).getValue();
              displayTextmessage.append(chatname+" \n"+chatmessage+" \n"+chatdate+"      "+chattime+"\n\n\n");
              mscrollview.fullScroll(ScrollView.FOCUS_DOWN);
          }
    }
}

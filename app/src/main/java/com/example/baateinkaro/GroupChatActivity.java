package com.example.baateinkaro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class GroupChatActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ImageButton sendmessagebutton;
    private EditText usermessageinput;
    private ScrollView mscrollview;
    private TextView displayTextmessage;
    private String currentgrpname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        currentgrpname=getIntent().getExtras().get("groupname").toString();
        Toast.makeText(this, currentgrpname, Toast.LENGTH_SHORT).show();
        InitializeFields();
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
}

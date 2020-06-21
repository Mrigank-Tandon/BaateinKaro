package com.example.baateinkaro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class GroupChatActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ImageButton sendmessagebutton;
    private EditText usermessageinput;
    private ScrollView mscrollview;
    private TextView displayTextmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        InitializeFields();
    }

    private void InitializeFields() {
        mtoolbar=(Toolbar) findViewById(R.id.grp_app_bar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Group Name");
        sendmessagebutton=(ImageButton) findViewById(R.id.send_message);
        usermessageinput=findViewById(R.id.group_message);
        displayTextmessage=findViewById(R.id.text_grp);
        mscrollview=findViewById(R.id.scroll_view);

    }
}

package com.example.baateinkaro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class FindFriendsActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private RecyclerView findfriendsrecylelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        findfriendsrecylelist=findViewById(R.id.find_friends_recycleview);
        findfriendsrecylelist.setLayoutManager(new LinearLayoutManager(this));
        mtoolbar=findViewById(R.id.findfriendstoolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");
    }
}

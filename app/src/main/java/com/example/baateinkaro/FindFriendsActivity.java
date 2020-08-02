package com.example.baateinkaro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private RecyclerView findfriendsrecylelist;
    private DatabaseReference userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        userref= FirebaseDatabase.getInstance().getReference().child("Users");
        findfriendsrecylelist=findViewById(R.id.find_friends_recycleview);
        findfriendsrecylelist.setLayoutManager(new LinearLayoutManager(this));
        mtoolbar=findViewById(R.id.findfriendstoolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options=new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(userref,Contacts.class)
                .build();
        FirebaseRecyclerAdapter<Contacts,findfriendviewholder> adapter=new FirebaseRecyclerAdapter<Contacts, findfriendviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull findfriendviewholder holder, final int position, @NonNull Contacts model) {
                  holder.userName.setText(model.getName());
                  holder.userstatus.setText(model.getStatus());

                  Glide.with(FindFriendsActivity.this).load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profileimage);

                  holder.itemView.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          String visit_userid=getRef(position).getKey();
                          Intent profileintent = new Intent(FindFriendsActivity.this,ProfileActivity.class);
                          profileintent.putExtra("visit_userid",visit_userid);
                          startActivity(profileintent);
                      }
                  });
            }

            @NonNull
            @Override
            public findfriendviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.userdisplayllayout, parent, false);
                findfriendviewholder viewholder=new findfriendviewholder(view);
                return viewholder;
            }
        };
        findfriendsrecylelist.setAdapter(adapter);
        adapter.startListening();

    }
    public static class findfriendviewholder extends RecyclerView.ViewHolder{

        TextView userName,userstatus;
        CircleImageView profileimage;
        public findfriendviewholder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.username);
            userstatus=itemView.findViewById(R.id.userstatus);
            profileimage=itemView.findViewById(R.id.user_profile_image);
        }
    }
}

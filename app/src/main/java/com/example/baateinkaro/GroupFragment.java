package com.example.baateinkaro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {
    private View groupfragment;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arrayList=new ArrayList<>();
    private DatabaseReference grpref;

    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupfragment=inflater.inflate(R.layout.fragment_group, container, false);
        grpref= FirebaseDatabase.getInstance().getReference().child("Groups");
        Initializefields();
        retrieveanddisplay();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentgroup=parent.getItemAtPosition(position).toString();
                Intent grpchat=new Intent(getContext(),GroupChatActivity.class);
                grpchat.putExtra("groupname",currentgroup);
                startActivity(grpchat);
            }
        });
        return  groupfragment;
    }

    private void retrieveanddisplay() {
       grpref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Set<String> set=new HashSet<>();
               Iterator iterator=dataSnapshot.getChildren().iterator();
               while(iterator.hasNext()){
                   set.add(((DataSnapshot)iterator.next()).getKey());

               }
               arrayList.clear();
               arrayList.addAll(set);
               arrayAdapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

    }

    private void Initializefields() {
        listView= groupfragment.findViewById(R.id.list_view);
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }
}

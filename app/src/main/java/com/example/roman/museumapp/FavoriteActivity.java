package com.example.roman.museumapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class FavoriteActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ArrayList<String> IDlist;
    private ArrayList<String> titlelist;
    private ArrayAdapter adapter;
    private ListView favoList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        IDlist = new ArrayList<String>();
        titlelist = new ArrayList<String>();
        getFavos();
        adapter = new ArrayAdapter<> (FavoriteActivity.this, android.R.layout.simple_list_item_1, titlelist);
        favoList = findViewById(R.id.favoList);
        favoList.setAdapter(adapter);
        favoList.setOnItemClickListener(new OnListItemClickListener());
    }

    private void getFavos() {
        databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                IDlist.clear();
                titlelist.clear();
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    String title, ID;
                    ID = item.getKey();
                    title = item.child("title").getValue().toString();
                    IDlist.add(ID);
                    titlelist.add(title);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class OnListItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(FavoriteActivity.this, ArtshowActivity.class);
            intent.putExtra("id", IDlist.get(i));
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

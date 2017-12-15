package com.example.roman.museumapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.util.Iterator;

public class ArtshowActivity extends AppCompatActivity {
    private RequestQueue queue;
    private String imageurl;
    private ImageView imageView;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String objectIDText;
    private String titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artshow);

        queue = Volley.newRequestQueue(this);

        mAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.imageView);

        final TextView title = findViewById(R.id.titelView);
        final TextView objectId = findViewById(R.id.idView);
        final TextView artist = findViewById(R.id.artistView);
        final TextView techniek = findViewById(R.id.techniekView);
        final TextView desc = findViewById(R.id.descView);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        final String url = "https://www.rijksmuseum.nl/api/nl/collection/" + id + "?key=n0IObiog&format=json";
        Log.d("ARTSHOW", "onCreate: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("ARTSHOW", "onResponse: " + response);
                            JSONObject jsonObject = new  JSONObject(response);
                            JSONObject artObject = jsonObject.getJSONObject("artObject");

                            title.setText(artObject.getString("title"));
                            objectId.setText(artObject.getString("objectNumber"));
                            artist.setText(artObject.getString("principalMaker"));
                            techniek.setText(artObject.getString("physicalMedium"));
                            desc.setText(artObject.getString("description"));

                            objectIDText = artObject.getString("objectNumber");
                            titleText = artObject.getString("title");

                            imageurl = artObject.getJSONObject("webImage").getString("url");
                            Log.d("ARTSHOW", "onResponse: " + imageurl);
                            Picasso.with(ArtshowActivity.this).load(imageurl).into(imageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ArtshowActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        MenuItem item = menu.findItem(R.id.favorite);
        if (mAuth.getCurrentUser() == null) {
            item.setVisible(false);
        }
        else {
            item.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:
                final String email = mAuth.getCurrentUser().getUid();
                Log.d("ARTSHOW", "onOptionsItemSelected: " + email);
                databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(objectIDText)) {
                            Log.d("ARTSHOW", "onDataChange: already favo'd");
                            DatabaseReference favos = databaseReference.child("users").child(email);
                            favos.child(objectIDText).removeValue();
                            Toast.makeText(ArtshowActivity.this, "Removed from favorites",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else {
                            DatabaseReference favos = databaseReference.child("users").child(email);
                            favos.child(objectIDText).child("title").setValue(titleText);
                            Toast.makeText(ArtshowActivity.this, "Added to favorites",
                            Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        }
        return true;
    }
}

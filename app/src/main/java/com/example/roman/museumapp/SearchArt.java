package com.example.roman.museumapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchArt extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText searchText;
    private ListView searchList;
    private JSONArray jsonArray;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_art);
        mAuth = FirebaseAuth.getInstance();

        searchText = findViewById(R.id.searchText);
        searchList = findViewById(R.id.artList);
        Button search = findViewById(R.id.search);
        queue = Volley.newRequestQueue(this);

        search.setOnClickListener(new OnSearchButtonClicked());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(SearchArt.this, "You need to log in to add favorites.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class OnSearchButtonClicked implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String url = "https://www.rijksmuseum.nl/api/nl/collection?key=n0IObiog&format=json&q=" + searchText.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("SEARCH", "onResponse: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                jsonArray = jsonObject.getJSONArray("artObjects");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            CustomAdapter customAdapter = new CustomAdapter();
                            searchList.setAdapter(customAdapter);
                            searchList.setOnItemClickListener(new OnListItemClickListener());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(stringRequest);
        }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Log.d("SEATCH2", "getView: called");
            view = getLayoutInflater().inflate(R.layout.listrow, null);
            TextView objectView = (TextView) view.findViewById(R.id.objectId);
            TextView nameView   = (TextView) view.findViewById(R.id.objectName);

            try {
                objectView.setText(jsonArray.getJSONObject(i).getString("objectNumber"));
                Log.d("SEARCH", "getView: "+ jsonArray.getJSONObject(i).getString("objectNumber"));
                nameView.setText(jsonArray.getJSONObject(i).getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return view;
        }
    }

    private class OnListItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(SearchArt.this, ArtshowActivity.class);
            try {
                intent.putExtra("id", jsonArray.getJSONObject(i).getString("objectNumber"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(intent);
            finish();
        }
    }







}

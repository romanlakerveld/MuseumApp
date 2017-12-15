package com.example.roman.museumapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private Button create;
    private Button exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        create = findViewById(R.id.create);
        create.setOnClickListener(new onCreateButtonListener());

        Button search = findViewById(R.id.search);
        search.setOnClickListener(new onSearchButtonListener());

        exist = findViewById(R.id.exist);
        exist.setOnClickListener(new OnExistClickListener());

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new OnLogoutClickListener());

        Button favorite = findViewById(R.id.favorites);
        favorite.setOnClickListener(new OnFaveClickListener());

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d("LOGIN", "onStart: no user logged in");
        }
        Log.d("LOGIN", "onStart: ");

    }

    public void createUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // TODO: wsl een update
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // TODO : ook hier een update
                        }

                        // ...
                    }
                });
    }

    public void logIn() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // TODO : ook hier een update
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // TODO : ook hier een update
                        }

                        // ...
                    }
                });
    }

    private class onCreateButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (mAuth.getCurrentUser() == null) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(MainActivity.this, "Please logout first",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class onSearchButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, SearchArt.class);
            startActivity(intent);
            finish();
        }
    }

    private class OnExistClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (mAuth.getCurrentUser() != null) {
                Log.d("MAIN", "onClick: ");
                Toast.makeText(MainActivity.this, "Already logged in.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(MainActivity.this, ExistActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private class OnLogoutClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (mAuth.getCurrentUser() == null) {
                Toast.makeText(MainActivity.this, "Not logged in.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d("MAIN", "onClick: signout");
                mAuth.signOut();
            }
        }
    }

    private class OnFaveClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(MainActivity.this, "Please log in to see your favorites",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


}

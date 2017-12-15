package com.example.roman.museumapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        Button button = findViewById(R.id.logIn);
        button.setOnClickListener(new onButtonClickListener());

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d("LOGIN", "onStart: no user logged in");
        }
        else {
            Log.d("LOGIN", "onStart: user is logged in");
        }
    }

    class onButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String emailtext = email.getText().toString();
            String passtext = password.getText().toString();
            Log.d("LOGIN", "onClick: " + emailtext);
            mAuth.createUserWithEmailAndPassword(emailtext, passtext)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LOGIN", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("LOGIN", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

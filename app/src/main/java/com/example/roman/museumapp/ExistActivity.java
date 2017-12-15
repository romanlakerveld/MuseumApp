package com.example.roman.museumapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ExistActivity extends AppCompatActivity {
    private TextView emailView;
    private TextView passView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist);

        mAuth = FirebaseAuth.getInstance();

        emailView = findViewById(R.id.email);
        passView = findViewById(R.id.password);

        Button button = findViewById(R.id.login);
        button.setOnClickListener(new OnLoginClickListener());

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(ExistActivity.this, "Already logged in",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ExistActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    private class OnLoginClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            mAuth.signInWithEmailAndPassword(emailView.getText().toString(), passView.getText().toString())
                    .addOnCompleteListener(ExistActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("EXIST", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(ExistActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("EXIST", "signInWithEmail:failure", task.getException());
                                Toast.makeText(ExistActivity.this, "Authentication failed.",
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
        Intent intent = new Intent(ExistActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

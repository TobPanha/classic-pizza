package com.example.somethingfortesting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText name, email, password;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.buttonSignUp);
        signIn = findViewById(R.id.textviewSignIn);

        signIn.setOnClickListener(v ->
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class))
        );

        btnSignUp.setOnClickListener(v -> signUp());
    }

    private void signUp() {
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Enter Name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistrationActivity.this,
                                "Registration Successful!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}

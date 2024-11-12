package com.example.implementimi2fa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        final EditText email = findViewById(R.id.emailET);
        final EditText mobile = findViewById(R.id.mobileET);
        final EditText password = findViewById(R.id.passwordET);
        final EditText conPassword = findViewById(R.id.conPasswordET);
        final Button signUpBtn = findViewById(R.id.signUpBtn);
        final TextView signInBtn = findViewById(R.id.signInBtn);

//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String getEmailTxt = email.getText().toString();
//
//                //Hapet OTP verifikimi bashke me numrin dhe emailin
//                Intent intent = new Intent(SignupActivity.this, VerifyActivity.class);
//
//                intent.putExtra("email", getEmailTxt);
//
//                startActivity(intent);
//            }
//        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

    }
}

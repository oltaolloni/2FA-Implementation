package com.example.implementimi2fa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.AuthProvider;
import java.util.Random;

import javax.mail.MessagingException;

public class LoginActivity extends AppCompatActivity {

    private MailSender mailSender = new MailSender();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        final EditText emailET = findViewById(R.id.emailET);
        final EditText passwordET = findViewById(R.id.passwordET);
        final TextView signUpBtn = findViewById(R.id.signUpBtn);
        final Button signInBtn = findViewById(R.id.signInBtn);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                signInBtn.setVisibility(View.INVISIBLE);

                // Call a backend function to generate and send the OTP email
                String otp = generateOtp();
                sendOtpEmail(email, otp);

                Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("otp", otp);
                startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    private void sendOtpEmail(String email, String otp) {
        new Thread(() -> {
            try {
                mailSender.sendOtpEmail(email, otp);
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "OTP sent to " + email, Toast.LENGTH_SHORT).show());
            } catch (MessagingException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }
}

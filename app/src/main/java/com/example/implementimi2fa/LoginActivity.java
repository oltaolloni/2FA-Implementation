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
    private String userEmail = "receiverEmail@gmail.com";
    private String userPass = "receiverPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailET = findViewById(R.id.emailET);
        final EditText passwordET = findViewById(R.id.passwordET);
        final TextView signUpBtn = findViewById(R.id.signUpBtn);
        final Button signInBtn = findViewById(R.id.signInBtn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                String pass = passwordET.getText().toString().trim();
                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                } else if(email.equals(userEmail) && pass.equals(userPass)){
                    String otp = generateOtp();
                    sendOtpEmail(email, otp);

                    Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("otp", otp);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }


            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

    }

    //Funksioni qe gjeneron nje random OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    //Funksioni qe dergon OTP ne emailin e caktuar
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

package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.view.Home;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Pega os dados de destino caso venha de Login ou Cadastro
        String targetActivity = getIntent().getStringExtra("TARGET_ACTIVITY");
        int userId = getIntent().getIntExtra("USER_ID", -1);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if ("HOME".equals(targetActivity)) {
                // Se o destino for a Home (pós login/cadastro)
                intent = new Intent(SplashActivity.this, Home.class);
                intent.putExtra("USER_ID", userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            } else {
                // Fluxo normal de abertura do app
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}
package com.example.myapplication.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.novo_agendamento;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    TextView lblUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        lblUser = findViewById(R.id.lblUser);
        Button btoAgendar = findViewById(R.id.btoAgendar);

        String userName = getIntent().getStringExtra("USER_NAME");

        btoAgendar.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, agendamento.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        });
        if (userName != null && !userName.trim().isEmpty()) {
            String firstName = userName.trim().split("\\s+")[0];
            String capitalized = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
            lblUser.setText(capitalized);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
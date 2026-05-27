package com.example.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.dbhelper;
import com.example.myapplication.perfil;

public class Home extends AppCompatActivity {

    private TextView lblUser;
    private dbhelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        db = new dbhelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        lblUser = findViewById(R.id.lblUser);
        Button btoAgendar = findViewById(R.id.btoAgendar);
        ImageView ftPerfil = findViewById(R.id.ftPerfil);

        btoAgendar.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, agendamento.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        ftPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, perfil.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarNome();
    }

    private void atualizarNome() {
        String nomeAtual = db.getNomeUsuario(userId);
        if (nomeAtual != null && !nomeAtual.trim().isEmpty()) {
            String firstName = nomeAtual.trim().split("\\s+")[0];
            String capitalized = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
            lblUser.setText(capitalized);
        }
    }
}
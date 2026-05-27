package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class cadastro extends AppCompatActivity {
    TextView tvVoltarLogin;
    TextInputEditText editNome, editSenha, editConfirmarSenha;
    Button btoCadastrar;
    View layoutRaiz;
    dbhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutRaizCadastro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new dbhelper(this);

        editNome            = findViewById(R.id.editCadNome);
        editSenha           = findViewById(R.id.editCadSenha);
        editConfirmarSenha  = findViewById(R.id.editCadConfirmarSenha);
        btoCadastrar        = findViewById(R.id.btoCadastrar);
        tvVoltarLogin       = findViewById(R.id.tvVoltarLogin);
        layoutRaiz          = findViewById(R.id.layoutRaizCadastro);

        btoCadastrar.setOnClickListener(v -> tentarCadastrar());

        tvVoltarLogin.setOnClickListener(v -> finish());
    }

    private void tentarCadastrar() {
        String nome             = editNome.getText().toString().trim();
        String senha            = editSenha.getText().toString().trim();
        String confirmarSenha   = editConfirmarSenha.getText().toString().trim();

        if (nome.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            MainActivity.exibirSnackbar(this, layoutRaiz, "Preencha todos os campos", "erro");
            return;
        }

        if (nome.length() < 3) {
            MainActivity.exibirSnackbar(this, layoutRaiz, "Nome deve ter ao menos 3 caracteres", "warning");
            return;
        }

        if (senha.length() < 6) {
            MainActivity.exibirSnackbar(this, layoutRaiz, "Senha deve ter ao menos 6 caracteres", "warning");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            MainActivity.exibirSnackbar(this, layoutRaiz, "As senhas não coincidem", "erro");
            return;
        }

        long id = db.cadUser(nome, senha);

        if (id != -1) {
            MainActivity.exibirSnackbar(this, layoutRaiz, "Cadastro realizado com sucesso!", "sucesso");

            layoutRaiz.postDelayed(() -> {
                Intent intent = new Intent(cadastro.this, SplashActivity.class);
                intent.putExtra("TARGET_ACTIVITY", "HOME");
                intent.putExtra("USER_ID", (int) id);
                startActivity(intent);
                finish();
            }, 1500);

        } else {
            MainActivity.exibirSnackbar(this, layoutRaiz, "Erro ao cadastrar, tente novamente", "erro");
        }
    }
}
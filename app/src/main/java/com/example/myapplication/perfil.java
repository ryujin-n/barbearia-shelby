package com.example.myapplication;

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

import com.google.android.material.textfield.TextInputEditText;

public class perfil extends AppCompatActivity {

    private TextInputEditText editNovoNome, editSenhaAtual, editNovaSenha, editConfirmarNovaSenha;
    private TextView tvNomePerfil;
    private dbhelper db;
    private int userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        db = new dbhelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);
        userName = db.getNomeUsuario(userId);

        initViews();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutRaizPerfil), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        findViewById(R.id.btnSalvarNome).setOnClickListener(v -> salvarNome());
        findViewById(R.id.btnSalvarSenha).setOnClickListener(v -> salvarSenha());
        findViewById(R.id.btnSairConta).setOnClickListener(v -> sairConta());
        findViewById(R.id.btnExcluirConta).setOnClickListener(v -> confirmarExclusaoConta());
    }

    private void initViews() {
        editNovoNome = findViewById(R.id.editNovoNome);
        editSenhaAtual = findViewById(R.id.editSenhaAtual);
        editNovaSenha = findViewById(R.id.editNovaSenha);
        editConfirmarNovaSenha = findViewById(R.id.editConfirmarNovaSenha);
        tvNomePerfil = findViewById(R.id.tvNomePerfil);

        tvNomePerfil.setText(capitalizeWords(userName));
        editNovoNome.setText("");
    }

    private String capitalizeWords(String str) {
        if (str == null || str.isEmpty()) return "";
        String[] words = str.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1).toLowerCase())
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }

    private void salvarNome() {
        String novoNome = editNovoNome.getText().toString().trim();
        if (novoNome.isEmpty() || novoNome.length() < 3) {
            MainActivity.exibirSnackbar(this, findViewById(R.id.layoutRaizPerfil), "Nome inválido", "erro");
            return;
        }

        if (db.atualizarNome(userId, novoNome)) {
            userName = novoNome;
            tvNomePerfil.setText(capitalizeWords(userName));
            editNovoNome.setText("");
            MainActivity.exibirSnackbar(this, findViewById(R.id.layoutRaizPerfil), "Nome atualizado!", "sucesso");
        } else {
            MainActivity.exibirSnackbar(this, findViewById(R.id.layoutRaizPerfil), "Erro ao atualizar nome", "erro");
        }
    }

    private void salvarSenha() {
        String atual = editSenhaAtual.getText().toString().trim();
        String nova = editNovaSenha.getText().toString().trim();
        String confirma = editConfirmarNovaSenha.getText().toString().trim();

        if (atual.isEmpty() || nova.isEmpty() || confirma.isEmpty()) {
            MainActivity.exibirSnackbar(this, findViewById(R.id.layoutRaizPerfil), "Preencha todos os campos", "erro");
            return;
        }

        if (!nova.equals(confirma)) {
            MainActivity.exibirSnackbar(this, findViewById(R.id.layoutRaizPerfil), "As senhas não coincidem", "erro");
            return;
        }

        if (nova.length() < 6) {
            MainActivity.exibirSnackbar(this, findViewById(R.id.layoutRaizPerfil), "Senha muito curta", "warning");
            return;
        }

        if (db.verificarSenha(userId, atual)) {
            if (db.atualizarSenha(userId, nova)) {
                MainActivity.exibirSnackbar(this, findViewById(R.id.layoutRaizPerfil), "Senha alterada!", "sucesso");
                editSenhaAtual.setText("");
                editNovaSenha.setText("");
                editConfirmarNovaSenha.setText("");
            } else {
                MainActivity.exibirSnackbar(this, findViewById(R.id.layoutRaizPerfil), "Erro ao atualizar senha", "erro");
            }
        } else {
            MainActivity.exibirSnackbar(this, findViewById(R.id.layoutRaizPerfil), "Senha atual incorreta", "erro");
        }
    }

    private void sairConta() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void confirmarExclusaoConta() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Excluir Conta")
                .setMessage("Tem certeza que deseja excluir sua conta? Todos os seus dados e agendamentos serão removidos permanentemente.")
                .setPositiveButton("Excluir", (dialog, which) -> {
                    db.delUser(userId);
                    sairConta();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
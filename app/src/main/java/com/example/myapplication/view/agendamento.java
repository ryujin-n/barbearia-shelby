package com.example.myapplication.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.AgendamentoAdapter;
import com.example.myapplication.dbhelper;
import com.example.myapplication.model.Agendamento;
import com.example.myapplication.novo_agendamento;

import java.util.ArrayList;
import java.util.List;

public class agendamento extends AppCompatActivity {

    private RecyclerView recyclerAgendamentos;
    private View layoutVazio;
    private dbhelper db;
    private AgendamentoAdapter adapter;
    private String userName;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agendamento);

        db = new dbhelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);
        userName = db.getNomeUsuario(userId);

        initViews();
        setupRecyclerView();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutRaizAgend), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnCriarAgendamento).setOnClickListener(v -> {
            Intent intent = new Intent(agendamento.this, novo_agendamento.class);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        });

        findViewById(R.id.btnCancelarAgendamento).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAgendamentos();
    }

    private void initViews() {
        recyclerAgendamentos = findViewById(R.id.recyclerAgendamentos);
        layoutVazio = findViewById(R.id.layoutVazio);
    }

    private void setupRecyclerView() {
        recyclerAgendamentos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AgendamentoAdapter(new ArrayList<>(), this::confirmarExclusao);
        recyclerAgendamentos.setAdapter(adapter);
    }

    private void carregarAgendamentos() {
        List<Agendamento> lista = new ArrayList<>();
        Cursor cursor = db.listAgendamento(userId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(dbhelper.COL_AG_ID));
                int uId = cursor.getInt(cursor.getColumnIndexOrThrow(dbhelper.COL_AG_FK_USER));
                String servico = cursor.getString(cursor.getColumnIndexOrThrow(dbhelper.COL_AG_SERVICO));
                String barbeiro = cursor.getString(cursor.getColumnIndexOrThrow(dbhelper.COL_AG_BARBEIRO));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(dbhelper.COL_AG_DATETIME));

                lista.add(new Agendamento(id, uId, servico, barbeiro, data));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter.updateList(lista);

        if (lista.isEmpty()) {
            layoutVazio.setVisibility(View.VISIBLE);
            recyclerAgendamentos.setVisibility(View.GONE);
        } else {
            layoutVazio.setVisibility(View.GONE);
            recyclerAgendamentos.setVisibility(View.VISIBLE);
        }
    }

    private void confirmarExclusao(Agendamento agendamento) {
        new AlertDialog.Builder(this)
                .setTitle("Cancelar Agendamento")
                .setMessage("Deseja realmente cancelar este agendamento?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    db.delAgendamento(agendamento.getId());
                    carregarAgendamentos();
                    MainActivity.exibirSnackbar(this, findViewById(R.id.layoutRaizAgend), "Agendamento cancelado", "sucesso");
                })
                .setNegativeButton("Não", null)
                .show();
    }
}
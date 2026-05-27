package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.databinding.ActivityNovoAgendamentoBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Locale;

public class novo_agendamento extends AppCompatActivity {

    private ActivityNovoAgendamentoBinding binding;
    private dbhelper db;

    private int usuarioId;
    private String dataSelecionada    = "";
    private String horarioSelecionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNovoAgendamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutRaizNovoAgend, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new dbhelper(this);

        String nomeUsuario = getIntent().getStringExtra("USER_NAME");
        usuarioId = db.buscarUser(nomeUsuario);

        setupBarbeiros();
        setupPickers();
        setupChips();

        binding.btnVoltar.setOnClickListener(v -> finish());
        binding.btnConfirmarAgendamento.setOnClickListener(v -> confirmarAgendamento());
    }

    private void setupBarbeiros() {
        String[] barbeiros = {
                "Ricardo Silva Costa",
                "Marcos Oliveira Santos",
                "Lucas Pereira Lima",
                "André Souza Mendes",
                "Gabriel Fernandes Rocha"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                barbeiros
        );

        binding.dropdownBarbeiro.setAdapter(adapter);
    }

    private void setupPickers() {

        binding.editData.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        dataSelecionada = String.format(
                                Locale.getDefault(), "%02d/%02d/%d",
                                dayOfMonth, month + 1, year
                        );
                        binding.editData.setText(dataSelecionada);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            dialog.show();
        });

        binding.editHorario.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            TimePickerDialog dialog = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minuteOfHour) -> {
                        // arredonda para :00 ou :30
                        int minAjustado  = (minuteOfHour < 15) ? 0 : (minuteOfHour < 45) ? 30 : 0;
                        int horaAjustada = (minuteOfHour >= 45) ? (hourOfDay + 1) % 24 : hourOfDay;

                        horarioSelecionado = String.format(
                                Locale.getDefault(), "%02d:%02d",
                                horaAjustada, minAjustado
                        );
                        binding.editHorario.setText(horarioSelecionado);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            );

            dialog.show();
        });
    }

    private void setupChips() {
        binding.chipGroupServico.setOnCheckedStateChangeListener((group, checkedIds) -> {
        });
    }

    private String getServicoSelecionado() {
        int id = binding.chipGroupServico.getCheckedChipId();
        if (id == View.NO_ID) return "";
        Chip chip = findViewById(id);
        return chip != null ? chip.getText().toString() : "";
    }

    private void confirmarAgendamento() {
        String servico  = getServicoSelecionado();
        String barbeiro = binding.dropdownBarbeiro.getText().toString().trim();

        if (servico.isEmpty()) {
            showSnackbar("Selecione um serviço", "warning");
            return;
        }

        if (barbeiro.isEmpty()) {
            showSnackbar("Selecione um barbeiro", "warning");
            return;
        }

        if (dataSelecionada.isEmpty()) {
            showSnackbar("Selecione uma data", "warning");
            return;
        }

        if (horarioSelecionado.isEmpty()) {
            showSnackbar("Selecione um horário", "warning");
            return;
        }

        if (usuarioId == -1) {
            showSnackbar("Erro: Usuário não identificado. Faça login novamente.", "erro");
            return;
        }

        String dataHorario = dataSelecionada + " às " + horarioSelecionado;
        long id = db.criarAgendamento(usuarioId, servico, barbeiro, dataHorario);

        if (id != -1) {
            showSnackbar("Agendamento confirmado!", "sucesso");
            binding.getRoot().postDelayed(this::finish, 1500);
        } else {
            showSnackbar("Erro ao agendar, tente novamente", "erro");
        }
    }

    private void showSnackbar(String mensagem, String tipo) {
        MainActivity.exibirSnackbar(this, binding.getRoot(), mensagem, tipo);
    }
}
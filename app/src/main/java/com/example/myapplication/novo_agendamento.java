package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.databinding.ActivityNovoAgendamentoBinding;

import java.util.Calendar;
import java.util.Locale;

public class novo_agendamento extends AppCompatActivity {

    private ActivityNovoAgendamentoBinding binding;

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

        setupPickers();
        setupBarbeiros();

        binding.btnVoltar.setOnClickListener(v -> finish());
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
        // Date Picker
        binding.editData.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                binding.editData.setText(selectedDate);
            }, year, month, day);

            datePickerDialog.show();
        });

        // Time Picker (30 minute interval)
        binding.editHorario.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
                // Adjust to nearest 30 mins
                int adjustedMinute = (minuteOfHour < 15) ? 0 : (minuteOfHour < 45) ? 30 : 0;
                int adjustedHour = (minuteOfHour >= 45) ? (hourOfDay + 1) % 24 : hourOfDay;

                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", adjustedHour, adjustedMinute);
                binding.editHorario.setText(selectedTime);
            }, hour, minute, true);

            timePickerDialog.show();
        });
    }
}
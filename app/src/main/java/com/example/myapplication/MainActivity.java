package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    EditText nome, senha;
    Button btoLogin;
    TextView btoCadastrar;
    View vLogin;
    dbhelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutRaizLogin), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nome = findViewById(R.id.txtNome);
        senha = findViewById(R.id.txtSenha);
        btoLogin = findViewById(R.id.btoLogin);
        btoCadastrar = findViewById(R.id.btoCadastrar);
        vLogin = findViewById(R.id.layoutRaizLogin);
        db = new dbhelper(this);

        btoCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, cadastro.class);
            startActivity(intent);
        });

        btoLogin.setOnClickListener(v ->{
            String txtnome = nome.getText().toString().trim();
            String txtsenha = senha.getText().toString().trim();

            if (txtnome.isEmpty() || txtsenha.isEmpty()) {
                showSnackbar("Preencha todos os campos", "erro");
                return;
            }

            if (db.verLogin(txtnome, txtsenha)) {
                int userId = db.buscarUser(txtnome);
                showSnackbar("Login realizado!", "sucesso");

                vLogin.postDelayed(() -> {
                    Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                    intent.putExtra("TARGET_ACTIVITY", "HOME");
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();
                }, 1000);

            } else {
                showSnackbar("Usuário ou senha incorretos", "erro");
            }
        });
    }

    public void showSnackbar(String mensagem, String tipo) {
        exibirSnackbar(this, vLogin, mensagem, tipo);
    }

    public static void exibirSnackbar(Context context, View view, String mensagem, String tipo) {
        Snackbar snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT);

        switch (tipo) {
            case "erro":
                applyCustomSnackbarStyle(context, snackbar, R.drawable.bg_snackbar_error, R.color.md_error);
                break;
            case "warning":
                applyCustomSnackbarStyle(context, snackbar, R.drawable.bg_snackbar_warning, R.color.md_warning);
                break;
            case "sucesso":
                applyDefaultSnackbarStyle(context, snackbar, R.color.md_success, R.color.md_on_success);
                break;
            default:
                applyDefaultSnackbarStyle(context, snackbar, R.color.md_surface_variant, R.color.md_on_surface);
                break;
        }
        snackbar.show();
    }

    private static void applyCustomSnackbarStyle(Context context, Snackbar snackbar, int backgroundRes, int colorRes) {
        View snackView = snackbar.getView();
        snackView.setBackgroundResource(backgroundRes);
        snackView.setBackgroundTintList(null);

        if (snackView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackView.getLayoutParams();
            params.setMargins(48, 0, 48, 64);
            snackView.setLayoutParams(params);
        }

        TextView tv = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, colorRes));
        
        Drawable infoIcon = ContextCompat.getDrawable(context, R.drawable.ic_info);
        if (infoIcon != null) {
            Drawable wrappedIcon = DrawableCompat.wrap(infoIcon).mutate();
            DrawableCompat.setTint(wrappedIcon, ContextCompat.getColor(context, colorRes));
            tv.setCompoundDrawablesWithIntrinsicBounds(wrappedIcon, null, null, null);
            tv.setCompoundDrawablePadding(24);
        }

        snackbar.setAction("", v1 -> snackbar.dismiss());
        Button actionButton = snackView.findViewById(com.google.android.material.R.id.snackbar_action);
        Drawable closeIcon = ContextCompat.getDrawable(context, R.drawable.ic_close);
        if (closeIcon != null) {
            Drawable wrappedClose = DrawableCompat.wrap(closeIcon).mutate();
            DrawableCompat.setTint(wrappedClose, ContextCompat.getColor(context, colorRes));
            actionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, wrappedClose, null);
        }
    }

    private static void applyDefaultSnackbarStyle(Context context, Snackbar snackbar, int bgColorRes, int textColorRes) {
        View snackView = snackbar.getView();
        snackView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, bgColorRes)));
        TextView tv = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context, textColorRes));
    }
}
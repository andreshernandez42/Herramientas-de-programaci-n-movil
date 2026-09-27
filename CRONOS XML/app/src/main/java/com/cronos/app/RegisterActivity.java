package com.cronos.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText etFullName;
    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private Button btnRegister;
    private Button btnBackLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        vincularVistas();
        configurarEventos();
    }

    private void vincularVistas() {
        etFullName = findViewById(R.id.etFullName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackLogin = findViewById(R.id.btnBackLogin);
    }

    private void configurarEventos() {
        btnRegister.setOnClickListener(view -> registrarUsuario());
        btnBackLogin.setOnClickListener(view -> volverAlLogin());
    }

    private void registrarUsuario() {
        String nombre = etFullName.getText().toString().trim();
        String correo = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etRegisterEmail.setError("Correo no válido");
            return;
        }
        if (password.length() < 6) {
            etRegisterPassword.setError("Mínimo 6 caracteres");
            return;
        }
        Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void volverAlLogin() {
        finish();
    }
}

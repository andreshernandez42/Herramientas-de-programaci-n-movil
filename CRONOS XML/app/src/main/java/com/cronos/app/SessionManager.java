package com.cronos.app;

import android.content.Context;
import android.content.SharedPreferences;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// maneja los usuarios y la sesion
public class SessionManager {

    private static final String PREFS = "cronos_sesion";
    private static final String KEY_CORREO_ACTIVO = "correo_activo";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public boolean existeUsuario(String correo) {
        return prefs.contains("pass_" + normalizar(correo));
    }

    public void registrarUsuario(String nombre, String correo, String password) {
        String c = normalizar(correo);
        prefs.edit()
                .putString("nombre_" + c, nombre)
                .putString("pass_" + c, hash(password))
                .apply();
    }

    public boolean validarCredenciales(String correo, String password) {
        String guardada = prefs.getString("pass_" + normalizar(correo), null);
        return guardada != null && guardada.equals(hash(password));
    }

    public void iniciarSesion(String correo) {
        prefs.edit().putString(KEY_CORREO_ACTIVO, normalizar(correo)).apply();
    }

    public boolean haySesionActiva() {
        return prefs.getString(KEY_CORREO_ACTIVO, null) != null;
    }

    public String getCorreoActivo() {
        return prefs.getString(KEY_CORREO_ACTIVO, "invitado");
    }

    public String getNombreActivo() {
        return prefs.getString("nombre_" + getCorreoActivo(), "Usuario Cronos");
    }

    public void cerrarSesion() {
        prefs.edit().remove(KEY_CORREO_ACTIVO).apply();
    }

    private String normalizar(String correo) {
        return correo.trim().toLowerCase();
    }

    private String hash(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return texto;
        }
    }
}

package com.cronos.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

// pantalla principal con el menu y el contenido
public class MainActivity extends AppCompatActivity
        implements MenuFragment.OnOpcionSeleccionadaListener {

    private static final String TAG_MENU = "menu_fragment";
    private static final String KEY_TITULO = "titulo_actual";

    private Toolbar toolbar;
    private ImageButton btnMenuToggle;
    private TextView tvToolbarTitle;
    private Button btnToolbarSafeExit;
    private View menuContainer;
    private View paneDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vincularVistas();
        configurarToolbar();

        if (savedInstanceState == null) {
            // se cargan los dos fragmentos
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.menuContainer, new MenuFragment(), TAG_MENU)
                    .commit();
            cargarFragment(new HomeFragment(), MenuFragment.TITULOS[MenuFragment.OPCION_INICIO]);
        } else {
            tvToolbarTitle.setText(savedInstanceState.getString(KEY_TITULO, "⏳ Cronos Tareas"));
        }
    }

    private void vincularVistas() {
        toolbar = findViewById(R.id.toolbar);
        btnMenuToggle = findViewById(R.id.btnMenuToggle);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        btnToolbarSafeExit = findViewById(R.id.btnToolbarSafeExit);
        menuContainer = findViewById(R.id.menuContainer);
        paneDivider = findViewById(R.id.paneDivider);
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        btnMenuToggle.setOnClickListener(view -> alternarMenu());
        btnToolbarSafeExit.setOnClickListener(view -> confirmarSalidaSegura());
    }

    // cuando tocan una opcion cambia el fragmento de la derecha
    @Override
    public void onOpcionSeleccionada(int posicion) {
        Fragment fragment;
        if (posicion == MenuFragment.OPCION_PERFIL) fragment = new ProfileFragment();
        else if (posicion == MenuFragment.OPCION_FOTOS) fragment = new GalleryFragment();
        else if (posicion == MenuFragment.OPCION_VIDEO) fragment = new VideoFragment();
        else if (posicion == MenuFragment.OPCION_WEB) fragment = new WebFragment();
        else if (posicion == MenuFragment.OPCION_BOTONES) fragment = new ControlsFragment();
        else if (posicion == MenuFragment.OPCION_TIPS) fragment = new TipsFragment();
        else fragment = new HomeFragment();

        cargarFragment(fragment, MenuFragment.TITULOS[posicion]);
    }

    // mostrar u ocultar el menu
    public void alternarMenu() {
        int visibilidad = menuContainer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        menuContainer.setVisibility(visibilidad);
        paneDivider.setVisibility(visibilidad);
    }

    private void cargarFragment(Fragment fragment, String titulo) {
        tvToolbarTitle.setText(titulo);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITULO, tvToolbarTitle.getText().toString());
    }

    public void confirmarSalidaSegura() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("🔒 Salida segura")
                .setMessage("¿Desea cerrar la sesión de forma segura y proteger sus datos?")
                .setPositiveButton("Salir", (dialog, which) -> cerrarSesion())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cerrarSesion() {
        new SessionManager(this).cerrarSesion();
        Toast.makeText(this, "Sesión cerrada de forma segura", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

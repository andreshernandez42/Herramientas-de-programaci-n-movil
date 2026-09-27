package com.cronos.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GalleryFragment extends Fragment {

    private Button btnGalleryTime;
    private Button btnGalleryImage;
    private Button btnGalleryCamera;
    private Button btnGalleryFolder;

    public GalleryFragment() {
        super(R.layout.fragment_gallery);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vincularVistas(view);
        configurarEventos();
    }

    private void vincularVistas(View view) {
        btnGalleryTime = view.findViewById(R.id.btnGalleryTime);
        btnGalleryImage = view.findViewById(R.id.btnGalleryImage);
        btnGalleryCamera = view.findViewById(R.id.btnGalleryCamera);
        btnGalleryFolder = view.findViewById(R.id.btnGalleryFolder);
    }

    private void configurarEventos() {
        btnGalleryTime.setOnClickListener(v -> abrirRecurso("Tiempo"));
        btnGalleryImage.setOnClickListener(v -> abrirRecurso("Imágenes"));
        btnGalleryCamera.setOnClickListener(v -> abrirRecurso("Cámara"));
        btnGalleryFolder.setOnClickListener(v -> abrirRecurso("Carpeta"));
    }

    private void abrirRecurso(String nombre) {
        Toast.makeText(requireContext(),
                "Recurso seleccionado: " + nombre,
                Toast.LENGTH_SHORT).show();
    }
}

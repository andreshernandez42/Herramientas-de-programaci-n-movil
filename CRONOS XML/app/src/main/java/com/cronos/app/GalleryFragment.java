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

        String detalle;
        if ("Tiempo".equals(nombre)) {
            detalle = "Módulo de gestión temporal: Cronología de actividades, horas dedicadas y trazabilidad de tareas de Cronos.";
        } else if ("Imágenes".equals(nombre)) {
            detalle = "Galería visual: Fotografías, esquemas y capturas asociadas a tus pendientes y asignaturas.";
        } else if ("Cámara".equals(nombre)) {
            detalle = "Captura rápida: Toma fotos de apuntes, tableros o documentos para vincularlos directamente a tus tareas.";
        } else {
            detalle = "Archivos y Carpetas: Explorador de documentos organizados por categorías (Universidad, Trabajo, Proyectos).";
        }

        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Galería - " + nombre)
                .setMessage(detalle)
                .setPositiveButton("Aceptar", null)
                .show();
    }
}

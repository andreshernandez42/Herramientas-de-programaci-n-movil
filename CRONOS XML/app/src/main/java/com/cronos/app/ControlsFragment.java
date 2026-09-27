package com.cronos.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ControlsFragment extends Fragment {

    private EditText etQuickTitle;
    private EditText etQuickDescription;
    private Spinner spQuickCategory;
    private Spinner spQuickPriority;
    private Button btnSaveQuickTask;
    private Button btnReminder;
    private Button btnTags;
    private Button btnClearCompleted;

    public ControlsFragment() {
        super(R.layout.fragment_controls);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vincularVistas(view);
        configurarSpinners();
        configurarEventos();
    }

    private void vincularVistas(View view) {
        etQuickTitle = view.findViewById(R.id.etQuickTitle);
        etQuickDescription = view.findViewById(R.id.etQuickDescription);
        spQuickCategory = view.findViewById(R.id.spQuickCategory);
        spQuickPriority = view.findViewById(R.id.spQuickPriority);
        btnSaveQuickTask = view.findViewById(R.id.btnSaveQuickTask);
        btnReminder = view.findViewById(R.id.btnReminder);
        btnTags = view.findViewById(R.id.btnTags);
        btnClearCompleted = view.findViewById(R.id.btnClearCompleted);
    }

    private void configurarSpinners() {
        List<String> categories = Arrays.asList(
                "Universidad", "Trabajo", "Casa", "Salud", "Proyectos", "Personal"
        );
        List<String> priorities = Arrays.asList("Alta", "Media", "Baja");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, categories
        );
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, priorities
        );

        spQuickCategory.setAdapter(categoryAdapter);
        spQuickPriority.setAdapter(priorityAdapter);
    }

    private void configurarEventos() {
        btnSaveQuickTask.setOnClickListener(v -> guardarTareaRapida());
        btnReminder.setOnClickListener(v -> programarRecordatorio());
        btnTags.setOnClickListener(v -> administrarEtiquetas());
        btnClearCompleted.setOnClickListener(v -> limpiarCompletadas());
    }

    private void guardarTareaRapida() {
        String title = etQuickTitle.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etQuickTitle.setError("Ingrese un título");
            return;
        }

        String category = spQuickCategory.getSelectedItem().toString();
        String priority = spQuickPriority.getSelectedItem().toString();

        Toast.makeText(requireContext(),
                "Tarea guardada: " + title + " • " + category + " • " + priority,
                Toast.LENGTH_LONG).show();

        etQuickTitle.setText("");
        etQuickDescription.setText("");
    }

    private void programarRecordatorio() {
        Calendar now = Calendar.getInstance();

        DatePickerDialog dateDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> abrirSelectorHora(year, month, dayOfMonth),
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dateDialog.show();
    }

    private void abrirSelectorHora(int year, int month, int dayOfMonth) {
        Calendar now = Calendar.getInstance();

        TimePickerDialog timeDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> Toast.makeText(
                        requireContext(),
                        "Recordatorio: " + dayOfMonth + "/" + (month + 1) + "/" + year
                                + " " + String.format("%02d:%02d", hourOfDay, minute),
                        Toast.LENGTH_LONG
                ).show(),
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );

        timeDialog.show();
    }

    private void administrarEtiquetas() {
        final String[] tags = {"Urgente", "Universidad", "Trabajo", "Personal"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Organizar por etiquetas")
                .setSingleChoiceItems(tags, -1, null)
                .setPositiveButton("Asignar", (dialog, which) ->
                        Toast.makeText(requireContext(),
                                "Etiqueta asignada", Toast.LENGTH_SHORT).show())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void limpiarCompletadas() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Limpiar completadas")
                .setMessage("¿Desea eliminar las tareas completadas?")
                .setPositiveButton("Limpiar", (dialog, which) ->
                        Toast.makeText(requireContext(),
                                "Tareas completadas eliminadas", Toast.LENGTH_SHORT).show())
                .setNegativeButton("Cancelar", null)
                .show();
    }
}

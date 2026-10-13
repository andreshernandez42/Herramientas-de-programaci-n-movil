package com.cronos.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
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
    private SwitchCompat swFocusMode;
    private TextView tvFocusMinutes;
    private SeekBar sbFocusMinutes;
    private RadioGroup rgBreakType;
    private CheckBox cbFocusSound;
    private Button btnStartFocus;

    private TaskRepository repository;
    private String fechaRecordatorio = "";
    private long recordatorioMillis = 0;

    // pide el permiso de notificaciones
    private final ActivityResultLauncher<String> permisoNotificaciones =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), concedido -> {
                if (!concedido) {
                    Toast.makeText(requireContext(),
                            "Sin permiso de notificaciones el recordatorio no podrá avisarle",
                            Toast.LENGTH_LONG).show();
                }
            });

    public ControlsFragment() {
        super(R.layout.fragment_controls);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new TaskRepository(requireContext());
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
        swFocusMode = view.findViewById(R.id.swFocusMode);
        tvFocusMinutes = view.findViewById(R.id.tvFocusMinutes);
        sbFocusMinutes = view.findViewById(R.id.sbFocusMinutes);
        rgBreakType = view.findViewById(R.id.rgBreakType);
        cbFocusSound = view.findViewById(R.id.cbFocusSound);
        btnStartFocus = view.findViewById(R.id.btnStartFocus);
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

        // si el switch esta activo se puede iniciar
        swFocusMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnStartFocus.setEnabled(isChecked);
            Toast.makeText(requireContext(),
                    isChecked ? "Modo concentración activado" : "Modo concentración desactivado",
                    Toast.LENGTH_SHORT).show();
        });

        // cambia los minutos con la barra
        sbFocusMinutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvFocusMinutes.setText("Duración: " + Math.max(5, progress) + " minutos");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        btnStartFocus.setOnClickListener(v -> iniciarSesionConcentracion());
    }

    // muestra lo que se eligio
    private void iniciarSesionConcentracion() {
        int minutos = Math.max(5, sbFocusMinutes.getProgress());
        String descanso = rgBreakType.getCheckedRadioButtonId() == R.id.rbBreakLong
                ? "largo (15 min)" : "corto (5 min)";
        String aviso = cbFocusSound.isChecked() ? "con aviso al terminar" : "sin aviso";

        new AlertDialog.Builder(requireContext())
                .setTitle("Sesión de concentración")
                .setMessage("Duración: " + minutos + " minutos\nDescanso: " + descanso + "\nNotificación: " + aviso)
                .setPositiveButton("Comenzar", (dialog, which) ->
                        Toast.makeText(requireContext(), "¡Sesión iniciada! Enfócate en tu tarea.", Toast.LENGTH_SHORT).show())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // guarda la tarea rapida y sale en el home
    private void guardarTareaRapida() {
        String title = etQuickTitle.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etQuickTitle.setError("Ingrese un título");
            return;
        }

        String description = etQuickDescription.getText().toString().trim();
        String category = spQuickCategory.getSelectedItem().toString();
        String priority = spQuickPriority.getSelectedItem().toString();

        repository.agregarTarea(new TaskItem(
                title,
                description.isEmpty() ? "Tarea rápida" : description,
                category,
                priority,
                fechaRecordatorio.isEmpty() ? "Sin fecha" : fechaRecordatorio,
                false
        ));

        String mensaje = "Tarea guardada: " + title + " • " + category + " • " + priority;
        if (recordatorioMillis > 0) {
            // programa la notificacion
            ReminderReceiver.programar(requireContext(), title, recordatorioMillis);
            mensaje += "\nRecordatorio: " + fechaRecordatorio;
        }
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show();

        etQuickTitle.setText("");
        etQuickDescription.setText("");
        fechaRecordatorio = "";
        recordatorioMillis = 0;
        btnReminder.setText("⏰ Programar Recordatorio");
    }

    private void programarRecordatorio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            permisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS);
        }

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
                (view, hourOfDay, minute) -> {
                    Calendar elegido = Calendar.getInstance();
                    elegido.set(year, month, dayOfMonth, hourOfDay, minute, 0);
                    if (elegido.getTimeInMillis() <= System.currentTimeMillis()) {
                        Toast.makeText(requireContext(),
                                "Elija una fecha y hora futuras para el recordatorio",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    recordatorioMillis = elegido.getTimeInMillis();
                    fechaRecordatorio = String.format(java.util.Locale.getDefault(),
                            "%02d/%02d/%d %02d:%02d", dayOfMonth, month + 1, year, hourOfDay, minute);
                    btnReminder.setText("⏰ Recordatorio: " + fechaRecordatorio);
                    Toast.makeText(requireContext(),
                            "Recordatorio listo. Se activará al guardar la tarea rápida.",
                            Toast.LENGTH_LONG).show();
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );

        timeDialog.show();
    }

    // ponerle etiqueta a una tarea
    private void administrarEtiquetas() {
        final List<TaskItem> tareas = repository.obtenerTareas();
        final List<TaskItem> pendientes = new java.util.ArrayList<>();
        for (TaskItem tarea : tareas) {
            if (!tarea.isCompleted()) pendientes.add(tarea);
        }
        if (pendientes.isEmpty()) {
            Toast.makeText(requireContext(), "No hay tareas pendientes para etiquetar", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] titulos = new String[pendientes.size()];
        for (int i = 0; i < pendientes.size(); i++) {
            titulos[i] = pendientes.get(i).getTitle();
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Seleccione la tarea")
                .setItems(titulos, (dialog, index) -> elegirEtiqueta(tareas, pendientes.get(index)))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void elegirEtiqueta(List<TaskItem> tareas, TaskItem tarea) {
        final String[] tags = repository.obtenerEtiquetas().toArray(new String[0]);
        final int[] seleccion = {-1};

        new AlertDialog.Builder(requireContext())
                .setTitle("Organizar por etiquetas")
                .setSingleChoiceItems(tags, -1, (dialog, which) -> seleccion[0] = which)
                .setPositiveButton("Asignar", (dialog, which) -> {
                    if (seleccion[0] < 0) {
                        Toast.makeText(requireContext(), "Seleccione una etiqueta", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    asignarEtiqueta(tareas, tarea, tags[seleccion[0]]);
                })
                .setNeutralButton("➕ Nueva etiqueta", (dialog, which) -> crearEtiqueta(tareas, tarea))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // crear una etiqueta nueva
    private void crearEtiqueta(List<TaskItem> tareas, TaskItem tarea) {
        final EditText etNuevaEtiqueta = new EditText(requireContext());
        etNuevaEtiqueta.setHint("Nombre de la etiqueta (ej. Examen)");
        etNuevaEtiqueta.setSingleLine(true);

        new AlertDialog.Builder(requireContext())
                .setTitle("Nueva etiqueta")
                .setView(etNuevaEtiqueta)
                .setPositiveButton("Crear y asignar", (dialog, which) -> {
                    String nombre = etNuevaEtiqueta.getText().toString().trim().replace("#", "");
                    if (nombre.isEmpty()) {
                        Toast.makeText(requireContext(), "Escriba un nombre para la etiqueta", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!repository.agregarEtiqueta(nombre)) {
                        Toast.makeText(requireContext(), "La etiqueta ya existe; se asignará la existente", Toast.LENGTH_SHORT).show();
                    }
                    asignarEtiqueta(tareas, tarea, nombre);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void asignarEtiqueta(List<TaskItem> tareas, TaskItem tarea, String etiqueta) {
        tarea.setTag(etiqueta);
        repository.guardarTareas(tareas);
        Toast.makeText(requireContext(),
                "Etiqueta #" + etiqueta + " asignada a \"" + tarea.getTitle() + "\"",
                Toast.LENGTH_SHORT).show();
    }

    private void limpiarCompletadas() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Limpiar completadas")
                .setMessage("¿Desea eliminar las tareas completadas?")
                .setPositiveButton("Limpiar", (dialog, which) -> {
                    int eliminadas = repository.limpiarCompletadas();
                    Toast.makeText(requireContext(),
                            eliminadas == 0 ? "No había tareas completadas"
                                    : eliminadas + " tarea(s) completada(s) eliminada(s)",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}

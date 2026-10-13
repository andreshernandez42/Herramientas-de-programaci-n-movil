package com.cronos.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.res.ColorStateList;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvTasks;
    private ImageButton fabAddTask;
    private Button btnFilterAll;
    private Button btnFilterPending;
    private Button btnFilterCompleted;
    private ChipGroup chipGroupCategory;
    private View panelNuevaTarea;
    private View hsvEstados;
    private View hsvCategorias;
    private EditText etNewTitle;
    private EditText etNewDescription;
    private Spinner spNewCategory;
    private Spinner spNewPriority;
    private android.widget.TextView tvNewDate;
    private android.widget.TextView tvNewReminder;
    private final long[] recordatorioNuevo = {0};

    private final List<TaskItem> allTasks = new ArrayList<>();
    private TaskAdapter adapter;
    private String currentStatus = "TODAS";
    private String categoriaElegida = "Todas";
    private TaskRepository repository;

    // permiso para las notificaciones
    private final androidx.activity.result.ActivityResultLauncher<String> permisoNotificaciones =
            registerForActivityResult(new androidx.activity.result.contract.ActivityResultContracts.RequestPermission(),
                    concedido -> { });

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new TaskRepository(requireContext());
        vincularVistas(view);
        configurarCategorias();
        cargarDatosIniciales();
        configurarLista();
        configurarEventos();
        aplicarFiltro();
    }

    private void vincularVistas(View view) {
        rvTasks = view.findViewById(R.id.rvTasks);
        fabAddTask = view.findViewById(R.id.fabAddTask);
        btnFilterAll = view.findViewById(R.id.btnFilterAll);
        btnFilterPending = view.findViewById(R.id.btnFilterPending);
        btnFilterCompleted = view.findViewById(R.id.btnFilterCompleted);
        chipGroupCategory = view.findViewById(R.id.chipGroupCategory);
        panelNuevaTarea = view.findViewById(R.id.panelNuevaTarea);
        hsvEstados = view.findViewById(R.id.hsvEstados);
        hsvCategorias = view.findViewById(R.id.hsvCategorias);
        etNewTitle = view.findViewById(R.id.etNewTitle);
        etNewDescription = view.findViewById(R.id.etNewDescription);
        spNewCategory = view.findViewById(R.id.spNewCategory);
        spNewPriority = view.findViewById(R.id.spNewPriority);
        tvNewDate = view.findViewById(R.id.tvNewDate);
        tvNewReminder = view.findViewById(R.id.tvNewReminder);
    }

    // chips de categorias
    private void configurarCategorias() {
        List<String> categorias = Arrays.asList("Todas", "Universidad", "Trabajo", "Casa", "Salud", "Proyectos", "Personal");
        for (String categoria : categorias) {
            chipGroupCategory.addView(crearChip(categoria, categoria, 12));
        }
        ((Chip) chipGroupCategory.getChildAt(0)).setChecked(true);
        chipGroupCategory.setOnCheckedStateChangeListener((group, ids) -> {
            if (ids.isEmpty()) return;
            categoriaElegida = (String) group.findViewById(ids.get(0)).getTag();
            aplicarFiltro();
        });
    }

    private Chip crearChip(String texto, String valor, int tamano) {
        int cian = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.cronos_cyan);
        int azul = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.cronos_navy);
        int blanco = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.white);
        int[][] estados = {{android.R.attr.state_checked}, {}};

        Chip chip = new Chip(requireContext());
        chip.setId(View.generateViewId());
        chip.setText(texto);
        chip.setTag(valor);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(false);
        chip.setTextSize(tamano);
        float dp = getResources().getDisplayMetrics().density;
        chip.setChipMinHeight(32 * dp);
        chip.setEnsureMinTouchTargetSize(false);
        chip.setChipBackgroundColor(new ColorStateList(estados, new int[]{cian, blanco}));
        chip.setTextColor(new ColorStateList(estados, new int[]{blanco, azul}));
        chip.setChipStrokeColor(ColorStateList.valueOf(0xFFD9E2E7));
        chip.setChipStrokeWidth(1 * dp);
        return chip;
    }

    private void cargarDatosIniciales() {
        allTasks.clear();
        allTasks.addAll(repository.obtenerTareas());
    }

    private void configurarLista() {
        rvTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TaskAdapter(new ArrayList<>(), new TaskAdapter.OnTaskActionListener() {
            @Override
            public void onCheckedChanged(TaskItem task, boolean completed) {
                task.setCompleted(completed);
                repository.guardarTareas(allTasks);
                Toast.makeText(requireContext(),
                        completed ? "Tarea completada" : "Tarea pendiente",
                        Toast.LENGTH_SHORT).show();
                aplicarFiltro();
            }

            @Override
            public void onDelete(TaskItem task) {
                confirmarEliminar(task);
            }
        });
        rvTasks.setAdapter(adapter);
    }

    private void configurarEventos() {
        btnFilterAll.setOnClickListener(view -> cambiarFiltroEstado("TODAS"));
        btnFilterPending.setOnClickListener(view -> cambiarFiltroEstado("PENDIENTE"));
        btnFilterCompleted.setOnClickListener(view -> cambiarFiltroEstado("COMPLETADA"));

        fabAddTask.setOnClickListener(view -> abrirPanelNuevaTarea());
        requireView().findViewById(R.id.btnCancelNewTask).setOnClickListener(v -> cerrarPanelNuevaTarea());
        requireView().findViewById(R.id.btnSaveNewTask).setOnClickListener(v -> guardarNuevaTarea());
        tvNewDate.setOnClickListener(v -> seleccionarFecha(tvNewDate));
        tvNewReminder.setOnClickListener(v -> seleccionarRecordatorio(tvNewReminder, recordatorioNuevo));
        configurarSpinnersNuevaTarea();
        actualizarEstiloBotonesFiltro();
    }

    private void cambiarFiltroEstado(String status) {
        currentStatus = status;
        actualizarEstiloBotonesFiltro();
        aplicarFiltro();
    }

    private void actualizarEstiloBotonesFiltro() {
        int colorCyan = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.cronos_cyan);
        int colorNavy = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.cronos_navy);
        int colorWhite = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.white);

        btnFilterAll.setBackgroundTintList(android.content.res.ColorStateList.valueOf("TODAS".equals(currentStatus) ? colorCyan : colorWhite));
        btnFilterAll.setTextColor("TODAS".equals(currentStatus) ? colorWhite : colorNavy);

        btnFilterPending.setBackgroundTintList(android.content.res.ColorStateList.valueOf("PENDIENTE".equals(currentStatus) ? colorCyan : colorWhite));
        btnFilterPending.setTextColor("PENDIENTE".equals(currentStatus) ? colorWhite : colorNavy);

        btnFilterCompleted.setBackgroundTintList(android.content.res.ColorStateList.valueOf("COMPLETADA".equals(currentStatus) ? colorCyan : colorWhite));
        btnFilterCompleted.setTextColor("COMPLETADA".equals(currentStatus) ? colorWhite : colorNavy);
    }

    private void aplicarFiltro() {
        List<TaskItem> filtered = new ArrayList<>();
        for (TaskItem task : allTasks) {
            boolean statusMatches = currentStatus.equals("TODAS")
                    || (currentStatus.equals("PENDIENTE") && !task.isCompleted())
                    || (currentStatus.equals("COMPLETADA") && task.isCompleted());

            boolean categoryMatches = categoriaElegida.equals("Todas")
                    || task.getCategory().equals(categoriaElegida);

            if (statusMatches && categoryMatches) {
                filtered.add(task);
            }
        }
        adapter.updateTasks(filtered);
    }

    private void configurarSpinnersNuevaTarea() {
        List<String> categories = Arrays.asList("Universidad", "Trabajo", "Casa", "Salud", "Proyectos", "Personal");
        spNewCategory.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories));
        List<String> priorities = Arrays.asList("Alta", "Media", "Baja");
        spNewPriority.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, priorities));
    }

    // muestra el formulario
    private void abrirPanelNuevaTarea() {
        if (panelNuevaTarea.getVisibility() == View.VISIBLE) {
            cerrarPanelNuevaTarea();
            return;
        }
        etNewTitle.setText("");
        etNewTitle.setError(null);
        etNewDescription.setText("");
        spNewCategory.setSelection(0);
        spNewPriority.setSelection(0);
        tvNewDate.setText("");
        tvNewReminder.setText("");
        recordatorioNuevo[0] = 0;

        hsvEstados.setVisibility(View.GONE);
        hsvCategorias.setVisibility(View.GONE);
        rvTasks.setVisibility(View.GONE);
        panelNuevaTarea.setVisibility(View.VISIBLE);
        fabAddTask.setRotation(45f);
        etNewTitle.requestFocus();
    }

    private void cerrarPanelNuevaTarea() {
        ocultarTeclado(etNewTitle);
        panelNuevaTarea.setVisibility(View.GONE);
        hsvEstados.setVisibility(View.VISIBLE);
        hsvCategorias.setVisibility(View.VISIBLE);
        rvTasks.setVisibility(View.VISIBLE);
        fabAddTask.setRotation(0f);
    }

    private void guardarNuevaTarea() {
        String title = etNewTitle.getText().toString().trim();
        if (title.isEmpty()) {
            etNewTitle.setError("Escriba un título");
            etNewTitle.requestFocus();
            return;
        }
        String desc = etNewDescription.getText().toString().trim();
        String date = tvNewDate.getText().toString().trim();

        allTasks.add(new TaskItem(
                title,
                desc.isEmpty() ? "Tarea creada desde Home" : desc,
                spNewCategory.getSelectedItem().toString(),
                spNewPriority.getSelectedItem().toString(),
                date.isEmpty() ? "Sin fecha" : date,
                false
        ));
        repository.guardarTareas(allTasks);
        if (recordatorioNuevo[0] > 0) {
            ReminderReceiver.programar(requireContext(), title, recordatorioNuevo[0]);
        }
        cerrarPanelNuevaTarea();
        aplicarFiltro();
        if (adapter.getItemCount() > 0) rvTasks.scrollToPosition(adapter.getItemCount() - 1);
        Toast.makeText(requireContext(), "Tarea guardada", Toast.LENGTH_SHORT).show();
    }

    private void ocultarTeclado(View vista) {
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager)
                requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
    }

    private void confirmarEliminar(TaskItem task) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar tarea")
                .setMessage("¿Desea eliminar \"" + task.getTitle() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    allTasks.remove(task);
                    repository.guardarTareas(allTasks);
                    aplicarFiltro();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // escoger fecha y hora del recordatorio
    private void seleccionarRecordatorio(android.widget.TextView destino, long[] recordatorioMillis) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU
                && androidx.core.content.ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            permisoNotificaciones.launch(android.Manifest.permission.POST_NOTIFICATIONS);
        }
        java.util.Calendar hoy = java.util.Calendar.getInstance();
        new android.app.DatePickerDialog(requireContext(), (picker, year, month, day) ->
                new android.app.TimePickerDialog(requireContext(), (timePicker, hour, minute) -> {
                    java.util.Calendar elegido = java.util.Calendar.getInstance();
                    elegido.set(year, month, day, hour, minute, 0);
                    if (elegido.getTimeInMillis() <= System.currentTimeMillis()) {
                        Toast.makeText(requireContext(), "Elija una fecha y hora futuras", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    recordatorioMillis[0] = elegido.getTimeInMillis();
                    destino.setText(String.format(java.util.Locale.getDefault(),
                            "⏰ %02d/%02d/%d %02d:%02d", day, month + 1, year, hour, minute));
                }, hoy.get(java.util.Calendar.HOUR_OF_DAY), hoy.get(java.util.Calendar.MINUTE), true).show(),
                hoy.get(java.util.Calendar.YEAR),
                hoy.get(java.util.Calendar.MONTH),
                hoy.get(java.util.Calendar.DAY_OF_MONTH)).show();
    }

    private void seleccionarFecha(android.widget.TextView destino) {
        java.util.Calendar hoy = java.util.Calendar.getInstance();
        new android.app.DatePickerDialog(requireContext(),
                (picker, year, month, day) -> destino.setText(
                        String.format(java.util.Locale.getDefault(), "%02d/%02d/%d", day, month + 1, year)),
                hoy.get(java.util.Calendar.YEAR),
                hoy.get(java.util.Calendar.MONTH),
                hoy.get(java.util.Calendar.DAY_OF_MONTH)).show();
    }
}

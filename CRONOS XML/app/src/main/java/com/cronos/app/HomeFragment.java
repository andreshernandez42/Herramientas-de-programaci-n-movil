package com.cronos.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvTasks;
    private FloatingActionButton fabAddTask;
    private Button btnFilterAll;
    private Button btnFilterPending;
    private Button btnFilterCompleted;
    private Spinner spCategoryFilter;

    private final List<TaskItem> allTasks = new ArrayList<>();
    private TaskAdapter adapter;
    private String currentStatus = "TODAS";

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        spCategoryFilter = view.findViewById(R.id.spCategoryFilter);
    }

    private void configurarCategorias() {
        List<String> categories = Arrays.asList(
                "Todas las categorías", "Universidad", "Trabajo",
                "Casa", "Salud", "Proyectos", "Personal"
        );
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );
        spCategoryFilter.setAdapter(categoryAdapter);
    }

    private void cargarDatosIniciales() {
        allTasks.clear();
        allTasks.add(new TaskItem(
                "Entregar informe final",
                "Revisar documentación y adjuntar evidencias",
                "Universidad", "Alta", "29/09/2026", false
        ));
        allTasks.add(new TaskItem(
                "Reunión de equipo",
                "Revisar avance de la entrega",
                "Trabajo", "Media", "28/09/2026", false
        ));
        allTasks.add(new TaskItem(
                "Limpiar sala y cocina",
                "Actividad del hogar",
                "Casa", "Baja", "30/09/2026", true
        ));
    }

    private void configurarLista() {
        rvTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TaskAdapter(new ArrayList<>(), new TaskAdapter.OnTaskActionListener() {
            @Override
            public void onCheckedChanged(TaskItem task, boolean completed) {
                task.setCompleted(completed);
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

        spCategoryFilter.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onSelected(int position) {
                aplicarFiltro();
            }
        });

        fabAddTask.setOnClickListener(view -> mostrarFormularioNuevaTarea());
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
        String selectedCategory = spCategoryFilter.getSelectedItem() == null
                ? "Todas las categorías"
                : spCategoryFilter.getSelectedItem().toString();

        List<TaskItem> filtered = new ArrayList<>();
        for (TaskItem task : allTasks) {
            boolean statusMatches = currentStatus.equals("TODAS")
                    || (currentStatus.equals("PENDIENTE") && !task.isCompleted())
                    || (currentStatus.equals("COMPLETADA") && task.isCompleted());

            boolean categoryMatches = selectedCategory.equals("Todas las categorías")
                    || task.getCategory().equals(selectedCategory);

            if (statusMatches && categoryMatches) {
                filtered.add(task);
            }
        }
        adapter.updateTasks(filtered);
    }

    private void mostrarFormularioNuevaTarea() {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(48, 24, 48, 12);

        final EditText etTitle = new EditText(requireContext());
        etTitle.setHint("Título de la tarea *");
        layout.addView(etTitle);

        final EditText etDesc = new EditText(requireContext());
        etDesc.setHint("Descripción (opcional)");
        layout.addView(etDesc);

        final Spinner spCat = new Spinner(requireContext());
        List<String> categories = Arrays.asList("Universidad", "Trabajo", "Casa", "Salud", "Proyectos", "Personal");
        spCat.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories));
        layout.addView(spCat);

        final Spinner spPri = new Spinner(requireContext());
        List<String> priorities = Arrays.asList("Alta", "Media", "Baja");
        spPri.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, priorities));
        layout.addView(spPri);

        final EditText etDate = new EditText(requireContext());
        etDate.setHint("Fecha límite (ej. 30/09/2026)");
        layout.addView(etDate);

        new AlertDialog.Builder(requireContext())
                .setTitle("Nueva tarea")
                .setView(layout)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    if (!title.isEmpty()) {
                        String desc = etDesc.getText().toString().trim();
                        String cat = spCat.getSelectedItem().toString();
                        String pri = spPri.getSelectedItem().toString();
                        String date = etDate.getText().toString().trim();

                        allTasks.add(new TaskItem(
                                title,
                                desc.isEmpty() ? "Tarea creada desde Home" : desc,
                                cat,
                                pri,
                                date.isEmpty() ? "Sin fecha" : date,
                                false
                        ));
                        aplicarFiltro();
                    } else {
                        Toast.makeText(requireContext(), "El título es obligatorio", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarEliminar(TaskItem task) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar tarea")
                .setMessage("¿Desea eliminar \"" + task.getTitle() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    allTasks.remove(task);
                    aplicarFiltro();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}

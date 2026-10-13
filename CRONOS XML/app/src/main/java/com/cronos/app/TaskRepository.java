package com.cronos.app;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

// guarda las tareas de cada usuario
public class TaskRepository {

    private static final String PREFS = "cronos_tareas";

    private final Context context;
    private final SharedPreferences prefs;
    private final String key;

    public TaskRepository(Context context) {
        this.context = context.getApplicationContext();
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        key = "tareas_" + new SessionManager(context).getCorreoActivo();
    }

    public List<TaskItem> obtenerTareas() {
        List<TaskItem> tareas = new ArrayList<>();
        String guardado = prefs.getString(key, null);
        if (guardado == null) {
            tareas.addAll(tareasIniciales());
            guardarTareas(tareas);
            return tareas;
        }
        try {
            JSONArray array = new JSONArray(guardado);
            for (int i = 0; i < array.length(); i++) {
                tareas.add(TaskItem.fromJson(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            tareas.addAll(tareasIniciales());
        }
        return tareas;
    }

    public void guardarTareas(List<TaskItem> tareas) {
        JSONArray array = new JSONArray();
        try {
            for (TaskItem tarea : tareas) {
                array.put(tarea.toJson());
            }
        } catch (JSONException e) {
            return;
        }
        prefs.edit().putString(key, array.toString()).apply();
        actualizarWidgets();
    }

    // actualiza el widget
    private void actualizarWidgets() {
        android.appwidget.AppWidgetManager manager = android.appwidget.AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new android.content.ComponentName(context, CronosWidget.class));
        for (int id : ids) {
            CronosWidget.actualizarWidget(context, manager, id);
        }
    }

    // etiquetas normales y las que crea el usuario
    public List<String> obtenerEtiquetas() {
        List<String> etiquetas = new ArrayList<>(java.util.Arrays.asList("Urgente", "Universidad", "Trabajo", "Personal"));
        String guardadas = prefs.getString("etiquetas_" + key, null);
        if (guardadas != null) {
            try {
                JSONArray array = new JSONArray(guardadas);
                for (int i = 0; i < array.length(); i++) {
                    String etiqueta = array.getString(i);
                    if (!etiquetas.contains(etiqueta)) etiquetas.add(etiqueta);
                }
            } catch (JSONException ignored) {
                // si falla quedan las de siempre
            }
        }
        return etiquetas;
    }

    // guarda una etiqueta nueva
    public boolean agregarEtiqueta(String etiqueta) {
        List<String> etiquetas = obtenerEtiquetas();
        for (String existente : etiquetas) {
            if (existente.equalsIgnoreCase(etiqueta)) return false;
        }
        JSONArray personalizadas = new JSONArray();
        for (String e : etiquetas.subList(4, etiquetas.size())) personalizadas.put(e);
        personalizadas.put(etiqueta);
        prefs.edit().putString("etiquetas_" + key, personalizadas.toString()).apply();
        return true;
    }

    public void agregarTarea(TaskItem tarea) {
        List<TaskItem> tareas = obtenerTareas();
        tareas.add(tarea);
        guardarTareas(tareas);
    }

    public int limpiarCompletadas() {
        List<TaskItem> tareas = obtenerTareas();
        int antes = tareas.size();
        tareas.removeIf(TaskItem::isCompleted);
        guardarTareas(tareas);
        return antes - tareas.size();
    }

    public static int contarCompletadas(List<TaskItem> tareas) {
        int total = 0;
        for (TaskItem tarea : tareas) {
            if (tarea.isCompleted()) total++;
        }
        return total;
    }

    private List<TaskItem> tareasIniciales() {
        List<TaskItem> iniciales = new ArrayList<>();
        iniciales.add(new TaskItem("Entregar informe final",
                "Revisar documentación y adjuntar evidencias",
                "Universidad", "Alta", "13/10/2026", false));
        iniciales.add(new TaskItem("Reunión de equipo",
                "Revisar avance de la entrega",
                "Trabajo", "Media", "14/10/2026", false));
        iniciales.add(new TaskItem("Limpiar sala y cocina",
                "Actividad del hogar",
                "Casa", "Baja", "15/10/2026", true));
        return iniciales;
    }
}

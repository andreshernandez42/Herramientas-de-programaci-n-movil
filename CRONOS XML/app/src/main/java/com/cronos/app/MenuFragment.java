package com.cronos.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// menu de la izquierda con los iconos
public class MenuFragment extends Fragment {

    // para avisarle a la actividad
    public interface OnOpcionSeleccionadaListener {
        void onOpcionSeleccionada(int posicion);
    }

    public static final int OPCION_INICIO = 0;
    public static final int OPCION_PERFIL = 1;
    public static final int OPCION_FOTOS = 2;
    public static final int OPCION_VIDEO = 3;
    public static final int OPCION_WEB = 4;
    public static final int OPCION_BOTONES = 5;
    public static final int OPCION_TIPS = 6;

    // iconos del menu
    public static final String[] ICONOS = { "📋", "👤", "📷", "🎬", "🌐", "🔘", "💡" };

    // nombre de cada opcion
    public static final String[] TITULOS = {
            "Mis tareas", "Perfil", "Fotos", "Video", "Web", "Botones", "Tips"
    };

    private static final String KEY_SELECCION = "opcion_seleccionada";

    private ListView lvMenuOptions;
    private OnOpcionSeleccionadaListener listener;
    private int opcionSeleccionada = OPCION_INICIO;

    public MenuFragment() {
        super(R.layout.fragment_menu);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnOpcionSeleccionadaListener) {
            listener = (OnOpcionSeleccionadaListener) context;
        } else {
            throw new IllegalStateException("La actividad debe implementar OnOpcionSeleccionadaListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            opcionSeleccionada = savedInstanceState.getInt(KEY_SELECCION, OPCION_INICIO);
        }
        lvMenuOptions = view.findViewById(R.id.lvMenuOptions);
        configurarLista();
    }

    private void configurarLista() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                requireContext(), R.layout.item_menu_option, R.id.tvMenuOption, ICONOS) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View fila = super.getView(position, convertView, parent);
                // el nombre para accesibilidad
                fila.setContentDescription(TITULOS[position]);
                return fila;
            }
        };
        lvMenuOptions.setAdapter(adapter);
        lvMenuOptions.setItemChecked(opcionSeleccionada, true);

        lvMenuOptions.setOnItemClickListener((parent, view, position, id) -> seleccionarOpcion(position));
    }

    // marca el icono y avisa
    public void seleccionarOpcion(int posicion) {
        opcionSeleccionada = posicion;
        lvMenuOptions.setItemChecked(posicion, true);
        if (listener != null) {
            listener.onOpcionSeleccionada(posicion);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECCION, opcionSeleccionada);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

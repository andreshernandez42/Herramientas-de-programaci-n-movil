package com.cronos.app;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GalleryFragment extends Fragment {

    private Button btnGalleryTime;
    private Button btnGalleryImage;
    private Button btnGalleryCamera;
    private Button btnGalleryFolder;
    private RecyclerView rvFotos;
    private ImageView ivFotoSeleccionada;
    private TextView tvTituloFoto;
    private TextView tvDescripcionFoto;

    private final List<Foto> fotos = new ArrayList<>();
    private File archivoCamara;

    // camara
    private final ActivityResultLauncher<Uri> tomarFoto =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), guardada -> {
                if (guardada && archivoCamara != null && archivoCamara.length() > 0) {
                    pedirDescripcion(archivoCamara, "📷 Foto de cámara");
                } else if (archivoCamara != null) {
                    archivoCamara.delete();
                }
            });

    // galeria del telefono
    private final ActivityResultLauncher<PickVisualMediaRequest> elegirImagen =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) copiarYAgregar(uri, "🖼 Imagen de la galería");
            });

    // archivos del telefono
    private final ActivityResultLauncher<String[]> abrirCarpeta =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri != null) copiarYAgregar(uri, "📁 Imagen de carpeta");
            });

    public GalleryFragment() {
        super(R.layout.fragment_gallery);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // recupera la foto si se cerro la app
        if (savedInstanceState != null && savedInstanceState.getString("archivo_camara") != null) {
            archivoCamara = new File(savedInstanceState.getString("archivo_camara"));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (archivoCamara != null) outState.putString("archivo_camara", archivoCamara.getAbsolutePath());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vincularVistas(view);
        configurarEventos();
        configurarListaFotos();
    }

    private void vincularVistas(View view) {
        btnGalleryTime = view.findViewById(R.id.btnGalleryTime);
        btnGalleryImage = view.findViewById(R.id.btnGalleryImage);
        btnGalleryCamera = view.findViewById(R.id.btnGalleryCamera);
        btnGalleryFolder = view.findViewById(R.id.btnGalleryFolder);
        rvFotos = view.findViewById(R.id.rvFotos);
        ivFotoSeleccionada = view.findViewById(R.id.ivFotoSeleccionada);
        tvTituloFoto = view.findViewById(R.id.tvTituloFoto);
        tvDescripcionFoto = view.findViewById(R.id.tvDescripcionFoto);
    }

    // carga la lista de fotos
    private void configurarListaFotos() {
        fotos.clear();
        fotos.addAll(cargarFotosUsuario());
        fotos.add(new Foto(R.drawable.img_campus, "🎓 Sede Universidad Poligran",
                "Evidencias académicas y apuntes de clases presenciales. Aquí se asocian las fotos de tableros y materiales de las asignaturas a las tareas de la categoría Universidad."));
        fotos.add(new Foto(R.drawable.img_office, "💼 Oficina y entregables",
                "Fotografías de tableros Kanban y minutas de reuniones semanales que respaldan las tareas de la categoría Trabajo."));
        fotos.add(new Foto(R.drawable.img_home, "🏠 Hogar y bienestar",
                "Recordatorios fotográficos de actividades personales y del hogar, como listas de compras o turnos de limpieza."));
        fotos.add(new Foto(R.drawable.img_library, "📚 Biblioteca y estudio",
                "Registro de libros y fuentes consultadas para trabajos e informes, útil para planear las sesiones de estudio."));
        fotos.add(new Foto(R.drawable.img_gym, "💪 Salud y ejercicio",
                "Rutinas de actividad física y pausas activas vinculadas a las tareas de la categoría Salud."));
        fotos.add(new Foto(R.drawable.img_project, "🗂 Tablero de proyectos",
                "Estado de los proyectos por columnas (pendiente, en proceso y terminado) para hacer seguimiento visual del avance."));

        rvFotos.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFotos.setAdapter(new FotoAdapter(fotos, new FotoAdapter.OnFotoClickListener() {
            @Override
            public void onFotoSeleccionada(Foto foto) {
                mostrarFoto(foto);
            }

            @Override
            public void onFotoMantenida(Foto foto) {
                confirmarEliminarFoto(foto);
            }
        }));
        mostrarFoto(fotos.get(0));
    }

    private void mostrarFoto(Foto foto) {
        foto.cargarEn(ivFotoSeleccionada);
        ivFotoSeleccionada.setContentDescription(foto.getTitulo());
        tvTituloFoto.setText(foto.getTitulo());
        tvDescripcionFoto.setText(foto.getDescripcion());
    }

    private void configurarEventos() {
        btnGalleryTime.setOnClickListener(v -> mostrarInfoTiempo());
        btnGalleryImage.setOnClickListener(v -> elegirImagen.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));
        btnGalleryCamera.setOnClickListener(v -> abrirCamara());
        btnGalleryFolder.setOnClickListener(v -> abrirCarpeta.launch(new String[]{"image/*"}));
    }

    private void mostrarInfoTiempo() {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Galería - Tiempo")
                .setMessage("Aquí ves las fotos que has agregado: " + contarFotosUsuario()
                        + ".\n\nUsa Cámara, Imágenes o Carpetas para agregar fotos de tus apuntes, tableros o documentos. "
                        + "Mantén presionada una foto tuya para borrarla.")
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void abrirCamara() {
        try {
            archivoCamara = nuevoArchivo();
            Uri uri = FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName() + ".fileprovider", archivoCamara);
            tomarFoto.launch(uri);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "No hay una app de cámara disponible", Toast.LENGTH_SHORT).show();
        }
    }

    // guarda una copia de la imagen
    private void copiarYAgregar(Uri uri, String titulo) {
        File destino = nuevoArchivo();
        try (InputStream in = requireContext().getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(destino)) {
            if (in == null) throw new java.io.IOException("sin datos");
            byte[] buffer = new byte[8192];
            int leidos;
            while ((leidos = in.read(buffer)) != -1) out.write(buffer, 0, leidos);
        } catch (java.io.IOException e) {
            destino.delete();
            Toast.makeText(requireContext(), "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        pedirDescripcion(destino, titulo);
    }

    private void pedirDescripcion(File archivo, String titulo) {
        final EditText etDescripcion = new EditText(requireContext());
        etDescripcion.setHint("Descripción de la foto");

        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Nueva foto")
                .setView(etDescripcion)
                .setCancelable(false)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    ocultarTeclado(etDescripcion);
                    String descripcion = etDescripcion.getText().toString().trim();
                    agregarFoto(new Foto(archivo.getAbsolutePath(), titulo,
                            descripcion.isEmpty() ? "Foto agregada por el usuario." : descripcion));
                })
                .setNegativeButton("Descartar", (dialog, which) -> {
                    ocultarTeclado(etDescripcion);
                    archivo.delete();
                })
                .show();
    }

    private void ocultarTeclado(View vista) {
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
    }

    private void agregarFoto(Foto foto) {
        List<Foto> propias = cargarFotosUsuario();
        propias.add(0, foto);
        guardarFotosUsuario(propias);
        configurarListaFotos();
        mostrarFoto(foto);
    }

    private void confirmarEliminarFoto(Foto foto) {
        if (!foto.esDelUsuario()) {
            Toast.makeText(requireContext(), "Solo puedes borrar las fotos que agregaste", Toast.LENGTH_SHORT).show();
            return;
        }
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Eliminar foto")
                .setMessage("¿Desea eliminar \"" + foto.getTitulo() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    List<Foto> propias = cargarFotosUsuario();
                    for (int i = 0; i < propias.size(); i++) {
                        if (propias.get(i).getRutaArchivo().equals(foto.getRutaArchivo())) {
                            propias.remove(i);
                            break;
                        }
                    }
                    guardarFotosUsuario(propias);
                    new File(foto.getRutaArchivo()).delete();
                    configurarListaFotos();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private File nuevoArchivo() {
        File carpeta = new File(requireContext().getFilesDir(), "fotos");
        if (!carpeta.exists()) carpeta.mkdirs();
        return new File(carpeta, "foto_" + System.currentTimeMillis() + ".jpg");
    }

    // fotos de cada usuario
    private SharedPreferences prefs() {
        return requireContext().getSharedPreferences("cronos_fotos", Context.MODE_PRIVATE);
    }

    private String clave() {
        return "fotos_" + new SessionManager(requireContext()).getCorreoActivo();
    }

    private int contarFotosUsuario() {
        return cargarFotosUsuario().size();
    }

    private List<Foto> cargarFotosUsuario() {
        List<Foto> lista = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(prefs().getString(clave(), "[]"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                String ruta = json.getString("ruta");
                if (new File(ruta).exists()) {
                    lista.add(new Foto(ruta, json.optString("titulo"), json.optString("descripcion")));
                }
            }
        } catch (JSONException e) {
            // si falla quedan las de la app
        }
        return lista;
    }

    private void guardarFotosUsuario(List<Foto> lista) {
        JSONArray array = new JSONArray();
        try {
            for (Foto foto : lista) {
                JSONObject json = new JSONObject();
                json.put("ruta", foto.getRutaArchivo());
                json.put("titulo", foto.getTitulo());
                json.put("descripcion", foto.getDescripcion());
                array.put(json);
            }
        } catch (JSONException e) {
            return;
        }
        prefs().edit().putString(clave(), array.toString()).apply();
    }
}

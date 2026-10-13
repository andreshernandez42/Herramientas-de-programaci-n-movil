# CRONOS - Entrega 2 Android (Java + XML)

Proyecto base preparado para cubrir los requisitos técnicos de la Entrega 2:

1. Actividades y/o fragmentos.
2. Interfaz XML para cada componente.
3. Identificadores (`android:id`) y propiedades.
4. Variables Java y vínculo con `findViewById()`.
5. Eventos (`setOnClickListener`, filtros, checkbox, menú) y métodos asociados.

## Arquitectura incluida

### Activities
- `SplashActivity` -> `activity_splash.xml`
- `LoginActivity` -> `activity_login.xml`
- `RegisterActivity` -> `activity_register.xml`
- `MainActivity` -> `activity_main.xml`

### Fragments
- `HomeFragment` -> `fragment_home.xml`
- `ProfileFragment` -> `fragment_profile.xml`
- `GalleryFragment` -> `fragment_gallery.xml`
- `VideoFragment` -> `fragment_video.xml`
- `WebFragment` -> `fragment_web.xml`
- `ControlsFragment` -> `fragment_controls.xml`
- `TipsFragment` -> `fragment_tips.xml`


## Entrega 3

### Cambios principales
- Dos fragmentos en la misma pantalla: `MenuFragment` (barra de iconos a la izquierda) y el contenido de cada opcion a la derecha. El boton de menu muestra u oculta la barra.
- Registro e inicio de sesion reales con `SessionManager` (la contraseña se guarda como hash SHA-256).
- Las tareas se guardan por usuario con `TaskRepository` (SharedPreferences en JSON).
- Home con filtros por estado y categoria, formulario de nueva tarea dentro de la pantalla y tarjetas con categoria, fecha y prioridad.
- Perfil con nombre, correo y el total de tareas, completadas y pendientes.
- Fotos: lista de imagenes con descripcion y botones para agregar fotos desde la camara, la galeria o una carpeta.
- Video: reproduce la demo guardada en `res/raw/cronos_demo.mp4` con `MediaController`.
- Web: carga la URL escrita y el mapa de la sede.
- Botones: tarea rapida, recordatorio con notificacion (`ReminderReceiver`), etiquetas personalizables, limpiar completadas y controles Switch, SeekBar, RadioGroup y CheckBox.
- Widget de escritorio `CronosWidget` con las tareas pendientes.

### Como ejecutar
1. Abrir la carpeta `CRONOS XML` en Android Studio (File > Open).
2. Si pide la version de Java para Gradle elegir JVM 21.
3. Esperar la sincronizacion de Gradle y ejecutar con Run en un emulador o celular.
4. Para generar el APK: Build > Build App Bundle(s) / APK(s) > Build APK(s). Queda en `app/build/outputs/apk/debug/app-debug.apk`.

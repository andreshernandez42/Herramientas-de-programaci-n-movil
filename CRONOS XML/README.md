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

### Soporte
- `TaskItem.java`: modelo de una tarea.
- `TaskAdapter.java`: adaptador del RecyclerView.
- `SimpleItemSelectedListener.java`: ayuda para eventos del Spinner.
- `item_task.xml`: diseño de cada tarjeta de tarea.

## Cómo abrirlo
1. Descomprime esta carpeta.
2. Android Studio > File > Open.
3. Selecciona `CRONOS_Entrega2_Android_Java_XML`.
4. Espera la sincronización de Gradle.
5. Ejecuta en un emulador o dispositivo Android.

## Pantallazos que debes tomar
- Splash
- Login
- Registro
- Home
- Perfil
- Fotos
- Video
- Web
- Botones / Controles
- Tips

Los pantallazos deben ser reales desde el emulador/dispositivo. Los mockups sirven como diseño, pero no sustituyen la evidencia de ejecución.

## Qué insertar en el documento para cada componente
- Pantallazo real.
- XML completo o el fragmento XML exigido por el docente.
- Tabla de IDs y propiedades.
- Variables Java y `findViewById()`.
- Eventos y métodos.

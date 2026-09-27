# Matriz técnica para copiar al Documento Maestro

| Componente | XML | IDs principales | Evento / método |
|---|---|---|---|
| SplashActivity | activity_splash.xml | tvSplashLogo, pbSplashLoading, tvSplashLoading | Temporizador -> iniciarAplicacion() |
| LoginActivity | activity_login.xml | etLoginEmail, etLoginPassword, btnLogin, btnGoRegister | Click -> validarInicioSesion(), abrirRegistro() |
| RegisterActivity | activity_register.xml | etFullName, etRegisterEmail, etRegisterPassword, btnRegister, btnBackLogin | Click -> registrarUsuario(), volverAlLogin() |
| MainActivity | activity_main.xml | drawerLayout, toolbar, navigationView, fragmentContainer | Menú -> cargarFragment(), cerrarSesion() |
| HomeFragment | fragment_home.xml | rvTasks, fabAddTask, btnFilterAll, btnFilterPending, btnFilterCompleted, spCategoryFilter | Click/selección -> aplicarFiltro(), mostrarFormularioNuevaTarea(), confirmarEliminar() |
| ProfileFragment | fragment_profile.xml | tvProfileName, tvProfileEmail, tvProfileStats, btnEditProfile | Click -> editarPerfil() |
| GalleryFragment | fragment_gallery.xml | btnGalleryTime, btnGalleryImage, btnGalleryCamera, btnGalleryFolder | Click -> abrirRecurso() |
| VideoFragment | fragment_video.xml | btnVideoPlay | Click -> reproducirVideo() |
| WebFragment | fragment_web.xml | etWebUrl, btnWebGo, webView | Click -> cargarPagina() |
| ControlsFragment | fragment_controls.xml | etQuickTitle, etQuickDescription, spQuickCategory, spQuickPriority, btnSaveQuickTask, btnReminder, btnTags, btnClearCompleted | Click -> guardarTareaRapida(), programarRecordatorio(), administrarEtiquetas(), limpiarCompletadas() |
| TipsFragment | fragment_tips.xml | cardTip1, cardTip2, cardTip3 | Click -> mostrarDetalleTip() |

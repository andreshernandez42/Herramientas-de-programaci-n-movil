package com.cronos.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import java.util.List;

// widget del escritorio con las tareas pendientes
public class CronosWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            actualizarWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void actualizarWidget(Context context, AppWidgetManager manager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cronos_widget);

        SessionManager session = new SessionManager(context);
        if (session.haySesionActiva()) {
            List<TaskItem> tareas = new TaskRepository(context).obtenerTareas();
            int pendientes = tareas.size() - TaskRepository.contarCompletadas(tareas);
            views.setTextViewText(R.id.tvWidgetPending, pendientes + " tarea(s) pendiente(s)");
        } else {
            views.setTextViewText(R.id.tvWidgetPending, "Inicie sesión para ver sus tareas");
        }

        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btnWidgetOpen, pendingIntent);
        views.setOnClickPendingIntent(R.id.widgetRoot, pendingIntent);

        manager.updateAppWidget(appWidgetId, views);
    }
}

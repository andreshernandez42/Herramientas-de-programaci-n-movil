package com.cronos.app;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

// recordatorios con notificacion
public class ReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "cronos_recordatorios";
    private static final String EXTRA_TITULO = "titulo_tarea";
    private static final String EXTRA_ID = "id_recordatorio";

    // programa la alarma
    public static void programar(Context context, String tituloTarea, long cuandoMillis) {
        int id = (int) (System.currentTimeMillis() & 0x7FFFFFFF);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_TITULO, tituloTarea);
        intent.putExtra(EXTRA_ID, id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, id, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            // si no deja alarma exacta usa una normal
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cuandoMillis, pendingIntent);
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cuandoMillis, pendingIntent);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String titulo = intent.getStringExtra(EXTRA_TITULO);
        int id = intent.getIntExtra(EXTRA_ID, 0);
        crearCanal(context);

        PendingIntent abrirApp = PendingIntent.getActivity(
                context, id, new Intent(context, SplashActivity.class),
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cronos_clock)
                .setContentTitle("⏰ Recordatorio de CRONOS")
                .setContentText(titulo == null ? "Tienes una tarea pendiente" : titulo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(abrirApp)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                || ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context).notify(id, builder.build());
        }
    }

    private static void crearCanal(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    CHANNEL_ID, "Recordatorios de tareas", NotificationManager.IMPORTANCE_HIGH);
            canal.setDescription("Avisos de los recordatorios programados en CRONOS");
            context.getSystemService(NotificationManager.class).createNotificationChannel(canal);
        }
    }
}

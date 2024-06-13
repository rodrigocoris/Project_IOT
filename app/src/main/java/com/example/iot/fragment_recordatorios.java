package com.example.iot;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import java.util.Calendar;
import java.util.HashSet;

public class fragment_recordatorios extends Fragment {

    private TextView textViewReminders;
    private HashSet<String> remindersSet;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public fragment_recordatorios() {
        // Constructor público vacío requerido
    }

    public static fragment_recordatorios newInstance(String param1, String param2) {
        fragment_recordatorios fragment = new fragment_recordatorios();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        remindersSet = new HashSet<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recordatorios, container, false);

        textViewReminders = view.findViewById(R.id.textView_reminders);

        Button btnRec1 = view.findViewById(R.id.rec1);
        Button btnRec2 = view.findViewById(R.id.rec2);
        Button btnRec3 = view.findViewById(R.id.rec3);

        btnRec1.setOnClickListener(v -> handleButtonClick("Recordatorio de agua\n"));
        btnRec2.setOnClickListener(v -> handleButtonClick("Recordatorio de trasplantar\n"));
        btnRec3.setOnClickListener(v -> handleButtonClick("Recordatorio de fertilizar\n"));

        return view;
    }

    private void handleButtonClick(String message) {
        if (remindersSet.contains(message)) {
            Toast.makeText(getContext(), "Este recordatorio ya ha sido agregado", Toast.LENGTH_SHORT).show();
        } else {
            showDateTimePicker(message);
        }
    }

    private void showDateTimePicker(String message) {
        Calendar calendar = Calendar.getInstance();
        Context context = getContext();
        if (context == null) {
            Toast.makeText(getContext(), "Error: Contexto no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        new DatePickerDialog(context, (dateView, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(context, (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                addReminder(message);
                setAlarm(calendar, message);
                sendNotification(context, "Alarma configurada", "La alarma para " + message + " ha sido configurada.");
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void addReminder(String message) {
        remindersSet.add(message);
        String currentText = textViewReminders.getText().toString();
        textViewReminders.setText(currentText + "\n" + message);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarm(Calendar calendar, String message) {
        Context context = getContext();
        if (context == null) return;

        try {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("message", message);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    (int) System.currentTimeMillis(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(context, "Alarma configurada para " + message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error: No se pudo acceder al AlarmManager", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("AlarmSetup", "Error al configurar la alarma", e);
            Toast.makeText(context, "Error al configurar la alarma: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void sendNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("alarm_channel", "Alarma", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Canal para alarmas");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm_channel")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}


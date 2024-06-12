package com.example.iot;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.Calendar;
import java.util.HashSet;

public class fragment_recordatorios extends Fragment {

    private TextView textViewReminders;
    private HashSet<String> remindersSet; // Para rastrear los recordatorios agregados

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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

        remindersSet = new HashSet<>(); // Inicializar el conjunto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño para este fragmento
        View view = inflater.inflate(R.layout.fragment_recordatorios, container, false);

        // Encontrar el TextView
        textViewReminders = view.findViewById(R.id.textView_reminders);

        // Encontrar botones por sus IDs
        Button btnRec1 = view.findViewById(R.id.rec1);
        Button btnRec2 = view.findViewById(R.id.rec2);
        Button btnRec3 = view.findViewById(R.id.rec3);

        // Establecer listeners de click para los botones
        btnRec1.setOnClickListener(v -> handleButtonClick("\nRecordatorio de agua\n"));
        btnRec2.setOnClickListener(v -> handleButtonClick("Recordatorio de trasplantar\n"));
        btnRec3.setOnClickListener(v -> handleButtonClick("Recordatorio de fertilizar\n"));

        return view;
    }

    private void handleButtonClick(String message) {
        if (remindersSet.contains(message)) {
            // Mostrar toast si el recordatorio ya se ha agregado
            Toast.makeText(getContext(), "Este recordatorio ya ha sido agregado", Toast.LENGTH_SHORT).show();
        } else {
            // Solicitar fecha y hora para la alarma
            showDateTimePicker(message);
        }
    }

    private void showDateTimePicker(String message) {
        Calendar calendar = Calendar.getInstance();

        // Asegurarse de que el contexto no sea nulo
        Context context = getContext();
        if (context == null) {
            Toast.makeText(getContext(), "Error: Contexto no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar el DatePickerDialog
        new DatePickerDialog(context, (dateView, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Mostrar el TimePickerDialog
            new TimePickerDialog(context, (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                // Añadir el recordatorio y configurar la alarma
                addReminder(message);
                setAlarm(calendar, message);
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


}

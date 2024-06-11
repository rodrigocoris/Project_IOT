package com.example.iot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
        btnRec1.setOnClickListener(v -> handleButtonClick("\n Recordatorio de agua "));
        btnRec2.setOnClickListener(v -> handleButtonClick("\n Recordatorio de trasplantar "));
        btnRec3.setOnClickListener(v -> handleButtonClick("\n Recordatorio de fertilizar "));

        return view;
    }

    private void handleButtonClick(String message) {
        if (remindersSet.contains(message)) {
            // Mostrar toast si el recordatorio ya se ha agregado
            Toast.makeText(getContext(), "Este recordatorio ya ha sido agregado", Toast.LENGTH_SHORT).show();
        } else {
            // Agregar recordatorio si no está ya agregado
            remindersSet.add(message);
            String currentText = textViewReminders.getText().toString();
            textViewReminders.setText(currentText + "\n" + message);
            // Mostrar toast para indicar que el recordatorio se ha agregado
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}

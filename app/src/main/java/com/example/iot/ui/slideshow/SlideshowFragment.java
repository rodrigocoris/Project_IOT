package com.example.iot.ui.slideshow;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.iot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class SlideshowFragment extends Fragment {

    private DatabaseReference mDatabase;
    private static final double TEMPERATURE_THRESHOLD_HIGH = 36.0;
    private static final double TEMPERATURE_THRESHOLD_LOW = 35.0;
    private static final String CHANNEL_ID = "temperature_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 100;

    private Button btnNotification;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        // Obtener una referencia a la base de datos de Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnNotification = root.findViewById(R.id.btn_notification);

        // Crear el canal de notificación
        createNotificationChannel();

        // Configurar el botón para mostrar la notificación
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTemperatureAndSendNotification();
            }
        });

        return root;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Temperature Channel";
            String description = "Channel for temperature notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Registrar el canal con el sistema
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.jardineria)
                .setContentTitle("iPlanta")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Aquí puedes solicitar los permisos necesarios si no están concedidos.
            // Sin embargo, es mejor manejar la solicitud de permisos fuera de este método.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void checkTemperatureAndSendNotification() {
        mDatabase.child("Lecturas").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            JSONObject data = new JSONObject(snapshot.getValue(String.class));
                            double temperature = data.optDouble("Temperatura", 0.0);

                            if (temperature >= TEMPERATURE_THRESHOLD_HIGH) {
                                showNotification("La temperatura es mayor o igual a 36°, ¡mueve tu cultivo!");
                            } else if (temperature < TEMPERATURE_THRESHOLD_LOW) {
                                showNotification("La temperatura es menor a 35°, no es necesario mover tu cultivo.");
                            } else {
                                showNotification("La temperatura está entre 35° y 36°, no es necesario mover tu cultivo.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showNotification("Error al procesar los datos de temperatura desde Firebase.");
                        }
                    }
                } else {
                    showNotification("No se encontraron datos en la base de datos.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de Firebase
                showNotification("Error al leer la temperatura desde Firebase.");
            }
        });
    }




    private double extractTemperature(String value) {
        try {
            JSONObject jsonObject = new JSONObject(value);
            return jsonObject.getDouble("Temperatura");
        } catch (JSONException e) {
            e.printStackTrace();
            return 0.0; // Si no se puede extraer la temperatura, devolver 0
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkTemperatureAndSendNotification();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        btnNotification = null;
    }
}


package com.example.iot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class temp_fragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> dataList;
    private String lastFecha = "";
    private String lastHora = "";
    private String lastTemperature = "";
    private String lastHumidity = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temp_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataList = new ArrayList<>();
        adapter = new MyAdapter(dataList);
        recyclerView.setAdapter(adapter);

        // Obtener referencia a la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Lecturas"); // nombre de referencia de Firebase

        // Leer datos de Firebase en tiempo real
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dataList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String json = dataSnapshot.getValue(String.class);

                        // Convertir el JSON a un objeto con Gson
                        try {
                            JSONObject dataObject = new JSONObject(json);
                            String fecha = dataObject.optString("Fecha", "");
                            String hora = dataObject.optString("Hora", "");
                            String temperatura = dataObject.optString("Temperatura", "");
                            String humedad = dataObject.optString("Humedad", "");

                            // Comprobar si alguno de los datos ha cambiado
                            boolean isFechaChanged = !fecha.equals(lastFecha);
                            boolean isHoraChanged = !hora.equals(lastHora);
                            boolean isTemperatureChanged = !temperatura.equals(lastTemperature);
                            boolean isHumidityChanged = !humedad.equals(lastHumidity);

                            // Actualizar último valor de los datos
                            if (isFechaChanged || isHoraChanged || isTemperatureChanged || isHumidityChanged) {
                                lastFecha = fecha;
                                lastHora = hora;
                                lastTemperature = temperatura;
                                lastHumidity = humedad;

                                // Formatear la cadena de datos a agregar
                                String data = String.format("Fecha: %s\nHora: %s\nTemperatura: %s°C\nHumedad: %s%%\n",
                                        fecha, hora, temperatura, humedad);


                                dataList.add(data);
                            }
                        } catch (JSONException e) {
                            Log.e("TempFragment", "Error al convertir JSON:", e);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    // Desplazarse al último elemento
                    recyclerView.scrollToPosition(dataList.size() - 1);
                } else {
                    dataList.clear();
                    dataList.add("No hay datos disponibles");
                    adapter.notifyDataSetChanged();
                    // Desplazarse al último elemento
                    recyclerView.scrollToPosition(dataList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TempFragment", "Error al leer datos de Firebase:", error.toException());
            }
        };

        reference.addValueEventListener(valueEventListener);

        return view;
    }
}

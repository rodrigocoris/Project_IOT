package com.example.iot;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new MyAdapter(dataList);
        recyclerView.setAdapter(adapter);

        // Obtener referencia a la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Dato"); //  nombre de  referencia de Firebase

        // Leer datos de Firebase en tiempo real
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dataList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String data = dataSnapshot.getValue(String.class);
                        dataList.add(data);
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
            public void onCancelled(DatabaseError error) {
                Log.w("MainActivity", "Error al leer datos de Firebase:", error.toException());
            }
        };

        reference.addValueEventListener(valueEventListener);
    }
}

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
import java.util.ArrayList;
import java.util.List;

public class temp_fragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> dataList;

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
        DatabaseReference reference = database.getReference("Dato"); // nombre de referencia de Firebase

        // Leer datos de Firebase en tiempo real
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TempFragment", "Error al leer datos de Firebase:", error.toException());
            }
        };

        reference.addValueEventListener(valueEventListener);

        return view;
    }
}

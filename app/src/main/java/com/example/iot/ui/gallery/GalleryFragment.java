package com.example.iot.ui.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot.ImageData;
import com.example.iot.MyAdapter;
import com.example.iot.MyAdapter2;
import com.example.iot.R;
import com.example.iot.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyAdapter2(getImageData()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<ImageData> getImageData() {
        List<ImageData> imageDataList = new ArrayList<>();
        imageDataList.add(new ImageData(R.drawable.temperatura, "Temperatura"));
        imageDataList.add(new ImageData(R.drawable.humedad, "Humedad"));
        imageDataList.add(new ImageData(R.drawable.temperatura_humedad, "Temperatura y Humedad"));
        imageDataList.add(new ImageData(R.drawable.machinelearning, "Prediccion con machine learning"));
        imageDataList.add(new ImageData(R.drawable.series_temperatura, "Series de tiempo temperatura"));
        imageDataList.add(new ImageData(R.drawable.series_humedad, "Series de tiempo humedad"));
        imageDataList.add(new ImageData(R.drawable.series_combinado, "Series de tiempo temperatura y humedad"));
        imageDataList.add(new ImageData(R.drawable.distribucion_temperatura, "Distribuciones temperatura"));
        imageDataList.add(new ImageData(R.drawable.distribucion_humedad, "Distribuciones humedad"));
        imageDataList.add(new ImageData(R.drawable.correlacion_temperatura_humedad, "Correlacion de temperatura y humedad"));
        imageDataList.add(new ImageData(R.drawable.temperatura_fecha_hora, "Fecha y hora de temperatura"));
        imageDataList.add(new ImageData(R.drawable.humedad_fecha_hora, "Fecha y hora de humedad"));
        imageDataList.add(new ImageData(R.drawable.temperatura_humedad_fecha_hora, "Fecha y hora de de temperatura y humedad"));
        imageDataList.add(new ImageData(R.drawable.boxplot_predicciones_humedad, "Predicciones Humedad"));
        imageDataList.add(new ImageData(R.drawable.boxplot_predicciones_temperatura, "Predicciones Temperatura"));
        imageDataList.add(new ImageData(R.drawable.boxplot_temperatura_humedad, "Predicciones Temperatura y Humedad"));
        return imageDataList;
    }
}

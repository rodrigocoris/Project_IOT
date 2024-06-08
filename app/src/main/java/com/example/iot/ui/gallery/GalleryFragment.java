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
        imageDataList.add(new ImageData(R.drawable.boxplot_predicciones_humedad, "Predicciones Humedad"));
        imageDataList.add(new ImageData(R.drawable.boxplot_predicciones_temperatura, "Predicciones Temperatura"));
        imageDataList.add(new ImageData(R.drawable.boxplot_temperatura_humedad, "Predicciones Temperatura y Humedad"));
        return imageDataList;
    }
}

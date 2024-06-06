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

import com.example.iot.R;
import com.example.iot.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView imageViewTemperatura = binding.imageViewTemperatura;
        ImageView imageViewHumedad = binding.imageViewHumedad;
        ImageView imageViewTemperaturaHumedad = binding.imageViewTemperaturaHumedad;

        // Cargar y mostrar las imágenes desde los recursos
        imageViewTemperatura.setImageBitmap(loadImageFromResources(R.drawable.temperatura));
        imageViewHumedad.setImageBitmap(loadImageFromResources(R.drawable.humedad));
        imageViewTemperaturaHumedad.setImageBitmap(loadImageFromResources(R.drawable.temperatura_humedad));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private Bitmap loadImageFromResources(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }
}

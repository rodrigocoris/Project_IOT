package com.example.iot;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_consejos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_consejos extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private VideoView videoView1; // Declarar VideoView

    public fragment_consejos() {
        // Required empty public constructor
    }

    public static fragment_consejos newInstance(String param1, String param2) {
        fragment_consejos fragment = new fragment_consejos();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consejos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener referencia del VideoView
        videoView1 = view.findViewById(R.id.videoView1);

        // Configurar la URI del video
        String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.vid;
        Uri uri = Uri.parse(videoPath);
        videoView1.setVideoURI(uri);

        // Configurar un Listener para manejar el estado de preparación
        videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Iniciar la reproducción cuando el video esté listo
                videoView1.start();
            }
        });
    }
}
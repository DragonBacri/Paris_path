package com.example.parcours_paris_java;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.parcours_paris_java.databinding.FragmentSecondBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class SecondFragment extends Fragment {


    String text2 = "";

    TextView tv ;
    Parcours parcours;

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        tv = binding.textViewId;

        Bundle bundle = getArguments();
        parcours = (Parcours) bundle.getSerializable("Parcours");

        return binding.getRoot();

    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Map<String, String> map = new HashMap<String, String>();
        try {
            File_extracter file_extracter = new File_extracter(requireActivity().getAssets(),"test.txt");
            map = file_extracter.extract_name();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.d("e", map.get("No"));


        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText(parcours.getName());

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
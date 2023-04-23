package com.example.parcours_paris_java;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.parcours_paris_java.databinding.FragmentSecondBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class SecondFragment extends Fragment {

    EditText editText;



    TextView tv_question ;

    TextView tv_result;
    Parcours parcours;

    public Button button;



    private FragmentSecondBinding binding;


    private List<Questions> questionsList;

    int n ;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        tv_question = binding.textViewId;
        tv_result = binding.textViewId2;
        editText = binding.editans;
        button = binding.buttonSecond;
        button.setText("Entrer");
        Bundle bundle = getArguments();
        parcours = (Parcours) bundle.getSerializable("Parcours");
        questionsList = parcours.getQuestionsList();
        n = 0;
        tv_question.setText(questionsList.get(n).getQuestion());

        return binding.getRoot();

    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button.getText().equals("Question suivante")) {
                    if (n < questionsList.size()) {
                        newQuestion();
                    }
                    else {
                        tv_result.setText("Vous avez fini ce parcours bravo ! J'espère qu'il vous aura plu. A bientôt l'ami !");
                    }
                }
                else {

                    if (editText.getText().toString().equals(questionsList.get(n).getAnswer())) {
                        tv_result.setText("BRAVOO tu as trouvé");
                        button.setText("Question suivante");
                        n = n + 1;

                    }

                    else {

                        Log.d("ici",editText.getText().toString());
                        Log.d("ici",questionsList.get(n).getAnswer());

                        tv_result.setText("FAUX essaie encore !");
                    }
                }
            }
        });

        binding.buttonHelp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tv_result.setText("La bonne réponse était : "+questionsList.get(n).getAnswer());
                button.setText("Question suivante");
                n = n + 1;

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void newQuestion(){
        tv_question.setText(questionsList.get(n).getQuestion());
        tv_result.setText("");
        button.setText("Entrer");
    }



}
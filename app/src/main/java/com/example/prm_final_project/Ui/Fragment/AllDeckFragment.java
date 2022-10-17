package com.example.prm_final_project.Ui.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prm_final_project.R;

public class AllDeckFragment extends Fragment {
    Context thiscontext;
    public AllDeckFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_deck, container, false);
        return view;
    }
    @Override
    public void onStart() {
        Log.d("fragment all deck", "fragmentB: onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("fragment all deck", "fragmentB: onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("fragment all deck", "fragmentB: onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("fragment all deck", "fragmentB: onStop");
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        Log.d("fragment all deck", "fragmentB: onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("fragment all deck", "fragmentB: onDestroy");
        super.onDestroy();
    }
}
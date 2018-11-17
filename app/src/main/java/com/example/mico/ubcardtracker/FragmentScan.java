package com.example.mico.ubcardtracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentScan extends Fragment {
    public static String ScanCardNumber;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_scan, container, false);
        initViews(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Scan Card");
        Toast.makeText(getActivity(),ScanCardNumber,Toast.LENGTH_SHORT).show();
        return view;
    }
    private void initViews(View view) {
    }
}

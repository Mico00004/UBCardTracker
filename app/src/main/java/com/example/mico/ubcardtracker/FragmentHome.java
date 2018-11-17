package com.example.mico.ubcardtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentHome extends Fragment {

    private ImageView ScanImage,MyAccountImage,MessageImage,SettingsImage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");

        ScanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),ScanCardActivity.class));
            }
        });
        MyAccountImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        MessageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        SettingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
    private void initViews(View view) {
        ScanImage = view.findViewById(R.id.scan_image);
        MyAccountImage = view.findViewById(R.id.myaccount_image);
        MessageImage = view.findViewById(R.id.message_image);
        SettingsImage = view.findViewById(R.id.settings_image);


    }
}

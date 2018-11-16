package com.example.mico.ubcardtracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mico.ubcardtracker.m_MySQL.CardDownloader;
import com.example.mico.ubcardtracker.m_MySQL.ScannedCardDownloader;

public class FragmentScanned extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanned, container, false);
        initViews(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Scanned Cards");

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.sw1);
        final String urlAddress = Config.SELECT_SCANNED_CARDS_URL;
        final ListView scanned_card_Lv = view.findViewById(R.id.scanned_cards_lv);
        new ScannedCardDownloader(getActivity(), urlAddress, scanned_card_Lv,swipeRefreshLayout).execute();
        Log.wtf("Download","Data");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ScannedCardDownloader(getActivity(), urlAddress, scanned_card_Lv,swipeRefreshLayout).execute();

            }
        });
        return view;
    }
    private void initViews(View view) {

    }
}

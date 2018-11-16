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

public class FragmentAllCards extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_cards, container, false);
        initViews(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("All Cards");

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.sw);
        final String urlAddress = Config.SELECT_ALL_CARDS_URL;
        final ListView all_card_Lv = view.findViewById(R.id.all_cards_lv);
        new CardDownloader(getActivity(), urlAddress, all_card_Lv,swipeRefreshLayout).execute();
        Log.wtf("Download","Data");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new CardDownloader(getActivity(), urlAddress, all_card_Lv,swipeRefreshLayout).execute();

            }
        });

        return view;
    }
    private void initViews(View view) {

    }
}

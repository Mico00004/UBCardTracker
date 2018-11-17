package com.example.mico.ubcardtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mico.ubcardtracker.m_DataObject.ScannedCards;
import com.google.gson.Gson;

public class AllCardInfo extends AppCompatActivity {

    private TextView CardNumber,CardNumberDetail,Type,CurrentLocation,Destination,Area;
    private ImageView CardBgInfo, Status;
    private LinearLayout CurrentLocationLayout, DestinationLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_card_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Credit Card Detail");

        CardNumber = findViewById(R.id.card_number_info);;
        CardNumberDetail = findViewById(R.id.card_number_info_details);
        Type = findViewById(R.id.type_info);
        CurrentLocation = findViewById(R.id.current_location_info);
        Destination = findViewById(R.id.destination_info);
        CardBgInfo = findViewById(R.id.card_bg_info);
        Area = findViewById(R.id.area_info);
        Status = findViewById(R.id.status_img);
        CurrentLocationLayout = findViewById(R.id.current_location_layout);
        DestinationLayout = findViewById(R.id.destination_layout);


        Intent i = this.getIntent();
        String card_number = i.getExtras().getString("CARD_NUMBER_KEY");
        String type = i.getExtras().getString("TYPE_KEY");
        String status = i.getExtras().getString("STATUS_KEY");
        String current_location = i.getExtras().getString("CURRENT_LOCATION_KEY");
        String destination = i.getExtras().getString("DESTINATION_KEY");
        String area = i.getExtras().getString("AREA_KEY");

        CardNumber.setText(card_number);
        CardNumberDetail.setText(card_number);
        Type.setText(type);
        CurrentLocation.setText(current_location);
        Destination.setText(destination);
        Area.setText(area);

        if(type.contentEquals("Platinum"))
        {
            CardBgInfo.setImageResource(R.drawable.platinum);
        }
        else if(type.contentEquals("Gold"))
        {
            CardBgInfo.setImageResource(R.drawable.gold);
        }
        else if(type.contentEquals("Classic"))
        {
            CardBgInfo.setImageResource(R.drawable.classic);
        }

        if (status.contentEquals("Scanned"))
        {
            Status.setImageResource(R.drawable.scanned);
        }
        else if (status.contentEquals("On-Delivery") || status.contentEquals("Pending for Delivery"))
        {
            Status.setImageResource(R.drawable.ondelivery);
        }
        else if (status.contentEquals("Delivered"))
        {
            Status.setImageResource(R.drawable.delivered);
        }

        if(!status.contentEquals("On-Delivery")){
            CurrentLocationLayout.setVisibility(View.GONE);
            DestinationLayout.setVisibility(View.GONE);
        }



        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

package com.example.mico.ubcardtracker.m_MySQL;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mico.ubcardtracker.m_DataObject.ScannedCards;
import com.example.mico.ubcardtracker.m_UI.ScannedCardCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScannedCardDataParser extends AsyncTask<Void,Void,Boolean> {
    Context c;
    String jsonData;
    ListView lv;
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<ScannedCards> scanned_cards = new ArrayList<>();

    public ScannedCardDataParser(Context c, String jsonData, ListView lv, SwipeRefreshLayout swipeRefreshLayout) {
        this.c = c;
        this.jsonData = jsonData;
        this.lv = lv;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (swipeRefreshLayout.isRefreshing())
        {
            swipeRefreshLayout.setRefreshing(true);
        }
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Boolean parsed) {
        super.onPostExecute(parsed);
        swipeRefreshLayout.setRefreshing(false);
        if (parsed) {
            ScannedCardCustomAdapter scannedCardCustomAdapter = new ScannedCardCustomAdapter(c,scanned_cards);
            lv.setAdapter(scannedCardCustomAdapter);
        } else {
            Toast.makeText(c, "No Card Found", Toast.LENGTH_SHORT).show();
        }
    }
    private Boolean parseData()
    {
        try
        {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo;

            scanned_cards.clear();

            ScannedCards scanned_card;
            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);

                int id = jo.getInt("id");
                String card_number = jo.getString("CardNumber");
                String type = jo.getString("Type");
                String status = jo.getString("Status");
                String current_location = jo.getString("CurrentLocation");
                String client_address = jo.getString("ClientAddress");
                String area = jo.getString("Area");
                String scanned_date = jo.getString("ScannedDate");


                scanned_card = new ScannedCards();

                scanned_card.setId(id);
                scanned_card.setCard_number(card_number);
                scanned_card.setType(type);
                scanned_card.setStatus(status);
                scanned_card.setCurrent_location(current_location);
                scanned_card.setDestination(client_address);
                scanned_card.setArea(area);
                scanned_card.setScanned_date(scanned_date);

                scanned_cards.add(scanned_card);
            }


            return true;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

}

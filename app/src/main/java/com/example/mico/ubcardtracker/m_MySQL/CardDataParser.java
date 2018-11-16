package com.example.mico.ubcardtracker.m_MySQL;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mico.ubcardtracker.m_DataObject.Cards;
import com.example.mico.ubcardtracker.m_UI.CardCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CardDataParser extends AsyncTask<Void,Void,Boolean> {
    Context c;
    String jsonData;
    ListView lv;
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<Cards> cards = new ArrayList<>();

    public CardDataParser(Context c, String jsonData, ListView lv, SwipeRefreshLayout swipeRefreshLayout) {
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
            CardCustomAdapter cardCustomAdapter = new CardCustomAdapter(c,cards);
            lv.setAdapter(cardCustomAdapter);
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

            cards.clear();

            Cards card;
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


                card = new Cards();

                card.setId(id);
                card.setCard_number(card_number);
                card.setType(type);
                card.setStatus(status);
                card.setCurrent_location(current_location);
                card.setDestination(client_address);
                card.setArea(area);
                card.setScanned_date(scanned_date);

                cards.add(card);
            }

            return true;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
}

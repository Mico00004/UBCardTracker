package com.example.mico.ubcardtracker.m_MySQL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class CardDownloader extends AsyncTask<Void,Void,String> {
    Context c;
    String urlAddress;
    ListView lv;
    SwipeRefreshLayout swipeRefreshLayout;

    public CardDownloader(Context c, String urlAddress, ListView lv, SwipeRefreshLayout swipeRefreshLayout) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.lv = lv;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        if (!swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(true);
        }

    }
    @Override
    protected String doInBackground(Void... params) {
        return this.downloadData();
    }

    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);

        swipeRefreshLayout.setRefreshing(false);
        if(jsonData == null)
        {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(c,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }
        else
        {
            CardDataParser cardDataParser = new CardDataParser(c,jsonData,lv,swipeRefreshLayout);
            cardDataParser.execute();
        }
    }
    private String downloadData()
    {
        HttpURLConnection con = Connector.connect(urlAddress);
        if(con == null)
        {
            return null;

        }
        try
        {
            InputStream is = new BufferedInputStream(con.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuffer jsonData = new StringBuffer();

            while((line = br.readLine()) !=null)
            {
                jsonData.append(line+"\n");
            }
            br.close();
            is.close();

            return jsonData.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

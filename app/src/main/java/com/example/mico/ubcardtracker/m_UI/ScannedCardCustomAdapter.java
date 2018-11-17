package com.example.mico.ubcardtracker.m_UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mico.ubcardtracker.AllCardInfo;
import com.example.mico.ubcardtracker.Config;
import com.example.mico.ubcardtracker.FragmentScanned;
import com.example.mico.ubcardtracker.INodeJS;
import com.example.mico.ubcardtracker.R;
import com.example.mico.ubcardtracker.RetrofitClient;
import com.example.mico.ubcardtracker.m_DataObject.ScannedCards;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ScannedCardCustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<ScannedCards> scanned_cards;
    INodeJS myAPI;
    public static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private AlertDialog dialog;
    private ProgressBar progress;
    private Spinner CourierSpinner;
    private String cardNumber,area,destination,type;
    public ScannedCardCustomAdapter(Context c, ArrayList<ScannedCards> scanned_cards) {
        this.c = c;
        this.scanned_cards = scanned_cards;
    }

    @Override
    public int getCount() {
        return scanned_cards.size();
    }

    @Override
    public Object getItem(int position) {
        return scanned_cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.model_scanned_cards, parent, false);
        }

        ImageView TypeImage = convertView.findViewById(R.id.scanned_card_type_img);
        TextView CardNumber = convertView.findViewById(R.id.tv_scanned_card);
        Button Deliver = convertView.findViewById(R.id.deliver_btn);

        final ScannedCards scanned_card = (ScannedCards) this.getItem(position);

        cardNumber = scanned_card.getCard_number();
        type = scanned_card.getType();
        area = scanned_card.getArea();
        destination = scanned_card.getArea();

        CardNumber.setText(scanned_card.getCard_number());

        if (type.contentEquals("Platinum")){
            TypeImage.setImageResource(R.drawable.platinum);
        }
        else if(type.contentEquals("Gold")){
            TypeImage.setImageResource(R.drawable.gold);
        }else if(type.contentEquals("Classic")){
            TypeImage.setImageResource(R.drawable.classic);
        }

        Deliver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDeliver();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailActivity(scanned_card.getId(),scanned_card.getCard_number(),scanned_card.getType(),scanned_card.getStatus(),scanned_card.getCurrent_location(),scanned_card.getDestination(),scanned_card.getArea());
            }
        });
        return convertView;
    }
    private void openDetailActivity(int id,String card_number,String type,String status,String current_location,String destination,String area)
    {
        Intent i = new Intent(c, AllCardInfo.class);
        i.putExtra("ID_KEY",id);
        i.putExtra("CARD_NUMBER_KEY",card_number);
        i.putExtra("TYPE_KEY",type);
        i.putExtra("STATUS_KEY",status);
        i.putExtra("CURRENT_LOCATION_KEY",current_location);
        i.putExtra("DESTINATION_KEY",destination);
        i.putExtra("AREA_KEY",area);
        c.startActivity(i);
    }
    private void showDeliver(){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.deliver_dialog, null);

        CourierSpinner = view.findViewById(R.id.courier_spin);
        String[] arraySpinner = new String[] {
                "PRC", "Intervolt", "Safe Frates", "LBC"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CourierSpinner.setAdapter(adapter);

        progress = view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setPositiveButton("Deliver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                deliverCard();
            }
        });
    }
    private void deliverCard(){
        final String company = CourierSpinner.getSelectedItem().toString();
        final String url = Config.SELECT_DELIVER_CARD_URL;
        final RequestQueue requestQueue = Volley.newRequestQueue(c);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Successfully Deliver Card")) {
                            FragmentScanned.swipeRefreshLayout.setRefreshing(true);
                            Toast.makeText(c,"Deliver Successfully",Toast.LENGTH_SHORT);
                        }
                        else
                        {
                            Toast.makeText(c,"Failed to Deliver",Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c,"No Internet Connection",Toast.LENGTH_SHORT);
                    }

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("company",company);
                params.put("card_number",cardNumber);
                params.put("area",area);
                params.put("destination",destination);
                return params;
            }};
        requestQueue.add(stringRequest);
    }
    private void sendMessageonNem(String address,String amount,String message){
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        compositeDisposable.add(myAPI.sendMessage(address,amount,message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains(""))
                            Toast.makeText(c,"Successfully Send in Nem",Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

}

package com.example.mico.ubcardtracker.m_UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mico.ubcardtracker.AllCardInfo;
import com.example.mico.ubcardtracker.R;
import com.example.mico.ubcardtracker.m_DataObject.Cards;

import java.util.ArrayList;

public class CardCustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<Cards> cards;

    public CardCustomAdapter(Context c, ArrayList<Cards> cards) {
        this.c = c;
        this.cards = cards;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.model_all_cards, parent, false);
        }
        ImageView CardBG  = convertView.findViewById(R.id.card_bg);
        TextView CardNumber = convertView.findViewById(R.id.all_card_number);


        final Cards card = (Cards) this.getItem(position);

        String type = card.getType();
        CardNumber.setText(card.getCard_number());

        if(type.contentEquals("Platinum"))
        {
            CardBG.setImageResource(R.drawable.platinum);
        }
        if(type.contentEquals("Gold"))
        {
            CardBG.setImageResource(R.drawable.gold);
            CardNumber.setTextColor(Color.WHITE);
        }
        if(type.contentEquals("Classic"))
        {
            CardBG.setImageResource(R.drawable.classic);
            CardNumber.setTextColor(Color.WHITE);

        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailActivity(card.getId(),card.getCard_number(),card.getType(),card.getStatus(),card.getCurrent_location(),card.getDestination(),card.getArea());
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
}

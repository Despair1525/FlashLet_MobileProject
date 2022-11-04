package com.example.prm_final_project.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;

import java.util.ArrayList;
import java.util.List;

public class cardViewAdapter1 extends RecyclerView.Adapter<cardViewAdapter1.AddressViewHolder> {
    private Context context;
    private Deck cards;

    public Deck getCards() {
        return cards;
    }

    public void setCards(Deck cards) {
        this.cards = cards;
    }

    public cardViewAdapter1(Context context, Deck cards) {
        this.context = context;
        this.cards = cards;
    };
    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_cardview2,parent,false);
        return  new AddressViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
    List<String> card = cards.getCards().get(position);
    holder.front.setText(card.get(0));
    holder.back.setText(card.get(1));


    }

    @Override
    public int getItemCount() {
        return cards==null?0:cards.getCards().size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView front;
        TextView back;
        cardViewAdapter1 cardViewAdapter;
        public AddressViewHolder(View view, cardViewAdapter1 cardViewAdapter1) {
            super(view);
            this.cardViewAdapter = cardViewAdapter1;
            this.front = view.findViewById(R.id.front);
            this.back = view.findViewById(R.id.back);
        }
    }
}

package com.example.prm_final_project.Adapter;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm_final_project.R;

import java.util.ArrayList;
import java.util.List;

public class ViewCardImportAdapter  extends RecyclerView.Adapter< ViewCardImportAdapter.AddressViewHolder> {
    private Context context;
    private List<List<String>> cards;



    public  ViewCardImportAdapter(Context context, List<List<String>> cards) {
        this.context = context;
        this.cards = cards;
    };
    @NonNull
    @Override
    public ViewCardImportAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_cardview2,parent,false);
        return  new ViewCardImportAdapter.AddressViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        List<String> card = cards.get(position);
        holder.front.setText(card.get(0));
        holder.front.setMovementMethod(new ScrollingMovementMethod());
        holder.back.setText(card.get(1));
        holder.back.setMovementMethod(new ScrollingMovementMethod());

    }


    @Override
    public int getItemCount() {
        return cards==null?0:cards.size();
    }
    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView front;
        TextView back;
        ViewCardImportAdapter cardViewAdapter;
        public AddressViewHolder(View view,  ViewCardImportAdapter cardViewAdapter1) {
            super(view);
            this.cardViewAdapter = cardViewAdapter1;
            this.front = view.findViewById(R.id.front);
            this.back = view.findViewById(R.id.back);

        }
    }
}

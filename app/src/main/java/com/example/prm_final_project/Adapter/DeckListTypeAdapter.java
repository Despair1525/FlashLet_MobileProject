package com.example.prm_final_project.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Model.DeckListType;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.callbackInterface.AdapterCallback;
import com.example.prm_final_project.callbackInterface.RecentDeckCallback;
import com.google.firebase.database.core.Context;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

public class DeckListTypeAdapter extends RecyclerView.Adapter<DeckListTypeAdapter.AddressViewHolder> {

    private ArrayList<DeckListType> items;
    private ArrayList<DeckListType> OriginItems;
    int row_index = 0;
    AdapterCallback callback;
    RecyclerView rc;

    public DeckListTypeAdapter(ArrayList<DeckListType> items,AdapterCallback callback,RecyclerView rc) {
        this.items = items;
        this.OriginItems = items;
        this.callback = callback;
        this.rc = rc;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_type,parent,false);
        AddressViewHolder Address = new AddressViewHolder(view);
        return Address;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, @SuppressLint("RecyclerView") int i) {
        DeckListType currentItem = items.get(i);
        holder.imgView.setImageResource(currentItem.getImage());
        holder.title.setText(currentItem.getText());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rc.scrollToPosition(i);
                row_index = i;
                notifyDataSetChanged();
            }
        });
        if (row_index == i){
            holder.item.setBackgroundResource(R.drawable.deck_shape_clicked);
        }
        else {
            holder.item.setBackgroundResource(R.drawable.deck_shape);
        };
        if(row_index == 1){
        };
        callback.onResponse(row_index);
    }



    @Override
    public int getItemCount() {
        return items==null?0:items.size();
    }
    public class AddressViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView title;
        LinearLayout item;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.ivListTypeIcon);
            title = itemView.findViewById(R.id.tvListTypeTitle);
            item = itemView.findViewById(R.id.itemListType);

        }
    }
}

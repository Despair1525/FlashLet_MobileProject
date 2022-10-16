package com.example.prm_final_project.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_final_project.Model.DeckListType;
import com.example.prm_final_project.R;

import java.util.ArrayList;

public class DeckListTypeAdapter extends RecyclerView.Adapter<DeckListTypeAdapter.AddressViewHolder> {

    private ArrayList<DeckListType> items;
    int row_index = 0;

    public DeckListTypeAdapter(ArrayList<DeckListType> items) {
        this.items = items;
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
                row_index =i;
                notifyDataSetChanged();
            }
        });
        if (row_index == i){
            holder.item.setBackgroundResource(R.drawable.deck_shape_clicked);
        }
        else {
            holder.item.setBackgroundResource(R.drawable.deck_shape);

        };
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

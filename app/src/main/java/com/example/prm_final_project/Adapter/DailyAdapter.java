package com.example.prm_final_project.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.callbackInterface.AdapterCallback;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.AddressViewHolder> {

    int row_index = 0;
    RecyclerView rc;
    LocalDateTime currentTime;
    int dateBefore = -3;

    public DailyAdapter( RecyclerView rc ) {
        this.rc = rc;
        this.currentTime = Methods.caculateCalender(0);
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_time,parent,false);
        AddressViewHolder Address = new AddressViewHolder(view);
        return Address;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, @SuppressLint("RecyclerView") int i) {
        int current = i + dateBefore;
        LocalDateTime itemTime = currentTime.plusDays(current);
        if( current ==0 ){
            holder.item.setBackgroundResource(R.drawable.deck_shape_clicked);
            holder.day.setTextColor(Color.parseColor("#FFFFFF"));
            holder.date.setTextColor(Color.parseColor("#FFFFFF"));

        }else{
            holder.item.setBackgroundResource(R.drawable.deck_shape);
            holder.day.setTextColor(Color.parseColor("#64A36B"));
            holder.date.setTextColor(Color.parseColor("#64A36B"));
        };
        holder.day.setText(itemTime.getDayOfWeek().toString().substring(0,3));
        holder.date.setText(itemTime.getDayOfMonth()+"");
    }



    @Override
    public int getItemCount() {
        return 7;
    }
    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        TextView date;
        LinearLayout item;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.itemListType);
            day = itemView.findViewById(R.id.tv_time_text);
            date = itemView.findViewById(R.id.tv_time_date_num);
        }
    }
}

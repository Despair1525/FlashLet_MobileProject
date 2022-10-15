package com.example.prm_final_project.Adapter;

import android.app.slice.SliceItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;

import java.util.List;

public class SliderFlashcardAdapter extends RecyclerView.Adapter<SliderFlashcardAdapter.SliderViewHolder> {
    private Context context;
    private Deck cards;
    private ViewPager2 viewPager2;

    public SliderFlashcardAdapter(Context context,Deck cards,ViewPager2 viewPager2) {
        this.context = context;
        this.cards = cards;
        this.viewPager2 = viewPager2;
    };
    @NonNull
    @Override
    public SliderFlashcardAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.slide_card_container,parent,false);
        return  new SliderFlashcardAdapter.SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        List<String> card = cards.getCards().get(position);
        holder.front.setText(card.get(0));
        holder.back.setText(card.get(1));
//        holder.back.setText(card.get(1));
    }


    @Override
    public int getItemCount() {
        return cards==null?0:cards.getCards().size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {

        TextView front;
        TextView back;


        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            front = itemView.findViewById(R.id.SliderCardFront);
            back = itemView.findViewById(R.id.SliderCardBack);

        }

        void setCardView(SliceItem sliceItem){

        };
    }
}

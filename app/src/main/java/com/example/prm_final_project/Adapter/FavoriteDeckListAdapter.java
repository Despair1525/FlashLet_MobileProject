package com.example.prm_final_project.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Activity.EditProfileActivity;
import com.example.prm_final_project.Ui.Activity.LoginActivity;
import com.example.prm_final_project.Ui.Activity.ViewCardActivity;

import java.util.ArrayList;

public class FavoriteDeckListAdapter extends RecyclerView.Adapter<FavoriteDeckListAdapter.DeckViewHolder>{

    Context context;
    boolean isGuest;
    ArrayList<Deck> decks;
    Dialog dialog;

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public FavoriteDeckListAdapter(Context context, ArrayList<Deck> deck){
        this.context = context;
        this.decks = deck ;

    };

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_favorite_deck_view,parent,false);
        return new DeckViewHolder(view, FavoriteDeckListAdapter.this);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        Deck curentDeck = decks.get(position);
        holder.title.setText(curentDeck.getTitle());
        holder.totalNum.setText(curentDeck.getCards().size()+" cards");
        User u = UserDao.allUserHT.get(curentDeck.getUid());
        if(u == null) {
            holder.author.setText("");
        }else{
            holder.author.setText(u.getUsername());
            Uri imgUri ;
            if(u.getAvatar() != null && !u.getAvatar().equalsIgnoreCase("null")) {
                imgUri = Uri.parse(u.getAvatar());
                try {
                    Glide.with(context)
                            .load(imgUri)
                            .into(holder.imgAvt);
                } catch (Exception e){
                    holder.imgAvt.setImageResource(R.drawable.default_avatar);
                }
            }
             else {
                 holder.imgAvt.setImageResource(R.drawable.default_avatar);
            }
        };

        holder.layoutItem.setOnClickListener(view -> onItem(curentDeck));
        holder.deleteFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete from favorite");
                builder.setMessage("Are you sure to delete this deck from favorite list");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            decks.remove(position);
                            notifyItemRemoved(position);
                            //this line below gives you the animation and also updates the
                            //list items after the deleted item
                            notifyItemRangeChanged(position, getItemCount());
                            UserDao.deleteFavoriteDeck(curentDeck.getDeckId());
                            Toast.makeText(context, "Delete from favorite successfully", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("No", null);
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void onItem(Deck currentDeck) {
        Intent i = new Intent(context, ViewCardActivity.class);
        i.putExtra("viewDeck",currentDeck);
        // add view each time click
        DeckDao.addView(currentDeck);

        // Add Save Recent Deck to database
        User currentUser = UserDao.allUserHT.get(UserDao.getUser().getUid());
        if(currentUser != null) {
            String deckId = currentDeck.getDeckId();
            ArrayList<RecentDeck> recentDecks = DeckDao.createRecentDeck(deckId,currentUser.getRecentDecks());
            currentUser.setRecentDecks(recentDecks);
       UserDao.addUser(currentUser);
        };
        //
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return decks==null?0:decks.size();
    }


    public class DeckViewHolder extends RecyclerView.ViewHolder {
        TextView author ;
        TextView title ;
        TextView totalNum;
        FavoriteDeckListAdapter favoriteDeckListAdapter;
        ImageView imgAvt;
       ImageView deleteFav;
        private LinearLayout layoutItem;
        public DeckViewHolder(@NonNull View itemView, FavoriteDeckListAdapter favoriteDeckListAdapter) {
            super(itemView);
            this.favoriteDeckListAdapter = favoriteDeckListAdapter;
            layoutItem = itemView.findViewById(R.id.favDeckViewItem);
            author = itemView.findViewById(R.id.tvFavDeckAuthor);
            totalNum = (TextView) itemView.findViewById(R.id.tvFavDeckTotal);
            title = itemView.findViewById(R.id.tvFavDeckName);
            imgAvt = itemView.findViewById(R.id.fav_card_avt);
            deleteFav = itemView.findViewById(R.id.deleteFavDeck);
        }
    }
}

package com.example.prm_final_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.FavoriteDeck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.Ui.Activity.EditDeckActivity;
import com.example.prm_final_project.Ui.Activity.ViewCardActivity;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.callbackInterface.RecentDeckCallback;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeDeckListAdapter extends RecyclerView.Adapter<HomeDeckListAdapter.DeckViewHolder>{

    Context context;
    boolean isGuest;
    ArrayList<Deck> decks;

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public HomeDeckListAdapter(Context context, ArrayList<Deck> deck){
        this.context = context;
        this.decks = deck ;

    };

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_deck_view,parent,false);
        return new DeckViewHolder(view,HomeDeckListAdapter.this);
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
            RecentDeck newRecent = new RecentDeck(deckId, Methods.getTimeLong());
            ArrayList<RecentDeck> userRecent = currentUser.getRecentDecks();
                int lastRecent = 0;
                for(int position = 0; position < userRecent.size() ; position++) {
                    if(userRecent.get(position).getTimeStamp()  < userRecent.get(lastRecent).getTimeStamp() ) {
                        lastRecent = position;
                    };
                    if(newRecent.getDeckId().equals(userRecent.get(position).getDeckId())) {
                        userRecent.set(position,newRecent );
                    };
                };
                userRecent.add(newRecent);
                if(userRecent.size() > 10) userRecent.remove(lastRecent);
                currentUser.setRecentDecks(userRecent);
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
        HomeDeckListAdapter homeDeckListAdapter;
        ImageView imgAvt;
       TextView actionMenuView;
        private LinearLayout layoutItem;
        public DeckViewHolder(@NonNull View itemView, HomeDeckListAdapter homeDeckListAdapter) {
            super(itemView);
            this.homeDeckListAdapter = homeDeckListAdapter;
            layoutItem = itemView.findViewById(R.id.deckViewItem);
            author = itemView.findViewById(R.id.tvDeckAuthor);
            totalNum = (TextView) itemView.findViewById(R.id.tvDeckTotal);
            title = itemView.findViewById(R.id.tvDeckName);
            imgAvt = itemView.findViewById(R.id.card_avt);
//            actionMenuView = itemView.findViewById(R.id.tvDeckOptions);
        }
    }
}

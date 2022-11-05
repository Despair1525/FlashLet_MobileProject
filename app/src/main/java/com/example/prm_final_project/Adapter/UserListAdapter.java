package com.example.prm_final_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Activity.MyFlashcardsActivity;
import com.example.prm_final_project.Ui.Activity.ViewOthersProfileActivity;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private Context context;
    private ArrayList<User> users = new ArrayList<>();

    public UserListAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_user_view, parent, false);

        return new UserViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User u = users.get(position);
        int deckSize = 0;
        Uri imgUri;
        if(getCardByUser(u.getUserId()) == null){
            deckSize = 0;
        } else{
            deckSize = getCardByUser(u.getUserId()).size();
        }
        if(u.getAvatar() != null && !u.getAvatar().equalsIgnoreCase("null")) {
            imgUri = Uri.parse(u.getAvatar());
            try {
                Glide.with(context)
                        .load(imgUri)
                        .into(holder.imageView);
            } catch (Exception e){
                holder.imageView.setImageResource(R.drawable.default_avatar);
            }
        }
        holder.userNameTextView.setText(u.getUsername());
        holder.totalDeckTextView.setText(deckSize + " decks");
        holder.userItemLayout.setOnClickListener(view -> onClick(u));
    }

    private void onClick(User u) {
        FirebaseUser user = UserDao.getUser();
        Intent intent;
        if (user.getUid().equals(u.getUserId())){
            intent = new Intent(context, MyFlashcardsActivity.class);
        }
        else {
            intent = new Intent(context, ViewOthersProfileActivity.class);
        }

        intent.putExtra("currentUser", u.getUserId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(users == null) {
            return 0;
        } else{
            return users.size();
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private UserListAdapter userListAdapter;
        private TextView userNameTextView, totalDeckTextView;
        private ImageView imageView;
        private LinearLayout userItemLayout;
        public UserViewHolder(@NonNull View itemView, UserListAdapter userListAdapter) {
            super(itemView);
            this.userListAdapter = userListAdapter;
            userNameTextView = itemView.findViewById(R.id.tv_user_name);
            totalDeckTextView = itemView.findViewById(R.id.tv_user_no_deck);
            userItemLayout = itemView.findViewById(R.id.layout_user_item);
            imageView = itemView.findViewById(R.id.iv_avt_img);
        }
    }

    private List<Deck> getCardByUser(String uid){
        return DeckDao.HmAllDeck.values().stream().filter(deck -> deck.getUid().equals(uid)).collect(Collectors.toList());
    };
}

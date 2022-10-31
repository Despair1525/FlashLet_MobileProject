package com.example.prm_final_project.Ui.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class FavouriteFragment extends Fragment {

    private Context thiscontext;
    private TextView textView;
    private LinearLayout recyclerLayout;
    private RecyclerView recycler_favourite;
    private SearchResultViewModel viewModel;
    private ArrayList<RecentDeck> decks = new ArrayList<>();
    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thiscontext = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        initUi(view);

        User user = UserDao.getCurrentUser();

        decks = user.getFavoriteDeck();

        Collections.sort(decks, (o1, o2) -> {
            return (int) (o2.getTimeStamp() - o1.getTimeStamp());
        });
        ArrayList<Deck> favoriteDeck = matchHashMapFavoriteDeck();

        HomeDeckListAdapter homeDeckListAdapter = new HomeDeckListAdapter(thiscontext, favoriteDeck);
        RecyclerView recyclerView = recycler_favourite;
        recyclerView.setAdapter(homeDeckListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(thiscontext));

        return view;
    }

    public void initUi(View view){
        recyclerLayout = view.findViewById(R.id.layout_favourite);
        recycler_favourite = view.findViewById(R.id.recycler_favourite);
    }

    public  ArrayList<Deck> matchHashMapFavoriteDeck(){
        ArrayList<Deck> favoriteDeck = new ArrayList<>();
        for(RecentDeck key: decks) {
            Deck temp = DeckDao.HmAllDeck.get(key.getDeckId());
            if(temp != null) {
                favoriteDeck.add(temp);
            }
        };
        return favoriteDeck;
    }
}
package com.example.prm_final_project.Ui.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Adapter.UserListAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class MyFlashcardsFragment extends Fragment {

    private Context thiscontext;
    private TextView textView;
    private LinearLayout recyclerLayout;
    private RecyclerView recycler_my_flashcard;
    private SearchResultViewModel viewModel;
    private ArrayList<Deck> decks = new ArrayList<>();
    private ArrayList<Deck> allDecks = new ArrayList<>();

    public MyFlashcardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_flashcards, container, false);
        initUi(view);


//        Set<String> keySet = DeckDao.originDeck.keySet();
//        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);


        Collection<Deck> values = DeckDao.HmAllDeck.values();
        allDecks = new ArrayList<Deck>(values);

        User user = UserDao.getCurrentUser();
        decks = UserDao.getDeckByUser(allDecks, user.getUserId());

//        allDecks.add(DeckDao.getDeckById("1665657495678"));
        HomeDeckListAdapter homeDeckListAdapter = new HomeDeckListAdapter(thiscontext, decks);
        RecyclerView recyclerView = recycler_my_flashcard;
        recyclerView.setAdapter(homeDeckListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(thiscontext));

        return view;
    }

    public void initUi(View view){
        recyclerLayout = view.findViewById(R.id.layout_my_flashcards);
        recycler_my_flashcard = view.findViewById(R.id.recycler_my_flashcards);
    }
}
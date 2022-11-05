package com.example.prm_final_project.Ui.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;

import java.util.ArrayList;

public class AllDeckFragment extends Fragment {
    private Context thiscontext;
    private TextView textView;
    private LinearLayout allDeckLayout, recyclerLayout;
    private SearchResultViewModel viewModel;
    private ArrayList<Deck> allDecks = new ArrayList<>();
    private Boolean isSearch;

    public AllDeckFragment() {
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
        View view = inflater.inflate(R.layout.fragment_all_deck, container, false);
        init(view);

        // get data from parent fragment
        viewModel = new ViewModelProvider(getParentFragment())
                .get(SearchResultViewModel.class);
        allDecks = viewModel.getAllDecks().getValue();
        isSearch = viewModel.getIsSearch().getValue();

        if(allDecks.size() > 0 && isSearch){
            textView.setVisibility(View.GONE);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.TOP;

            allDeckLayout.setLayoutParams(params);
            recyclerLayout.setVisibility(View.VISIBLE);

            HomeDeckListAdapter homeDeckListAdapter = new HomeDeckListAdapter(thiscontext, allDecks);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_all_decks);
            recyclerView.setAdapter(homeDeckListAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(thiscontext));
        }
        else if(isSearch){
            textView.setText("No result found!");
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view){
        textView = view.findViewById(R.id.greeting_all_deck);
        allDeckLayout = view.findViewById(R.id.all_decks);
        recyclerLayout = view.findViewById(R.id.layout_all_decks);
    }

}
package com.example.prm_final_project.Ui.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Adapter.UserListAdapter;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;

import java.util.ArrayList;

public class AllResultFragment extends Fragment {

    private Context thiscontext;
    private TextView textView;
    private LinearLayout allDeckLayout;
    private LinearLayout allResultLayout;
    private LinearLayout allUserLayout;
    private Button allDeckButton;
    private Button allUserButton;
    private SearchResultViewModel viewModel;
    private ArrayList<Deck> allDecks = new ArrayList<>();
    private ArrayList<User> allUsers = new ArrayList<>();
    private Boolean isSearch;

    public AllResultFragment() {
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
        View view = inflater.inflate(R.layout.fragment_all_result, container, false);
        init(view);

        // get data from parent fragment
        viewModel = new ViewModelProvider(getParentFragment())
                .get(SearchResultViewModel.class);
        allDecks = viewModel.getAllDecks().getValue();
        allUsers = viewModel.getAllUsers().getValue();
        isSearch = viewModel.getIsSearch().getValue();
        Log.i("search all decks", allDecks.toString());

        if((allDecks.size() > 0 | allUsers.size() > 0) & isSearch){
            Log.d("result ", "found");
            textView.setVisibility(View.GONE);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.TOP;

            allResultLayout.setLayoutParams(params);

            if(allDecks.size() > 0){
                allDeckLayout.setVisibility(View.VISIBLE);
                HomeDeckListAdapter homeDeckListAdapter = new HomeDeckListAdapter(thiscontext, getLimitDeckResult(allDecks, 3));
                RecyclerView recyclerView = view.findViewById(R.id.recycler_search_decks);
                recyclerView.setAdapter(homeDeckListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(thiscontext));
            }
            if(allUsers.size() > 0){
                allUserLayout.setVisibility(View.VISIBLE);
                UserListAdapter userListAdapter = new UserListAdapter(thiscontext, getLimitUserResult(allUsers, 3));
                RecyclerView recyclerView = view.findViewById(R.id.recycler_search_users);
                recyclerView.setAdapter(userListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(thiscontext));
            }
        }
        else if(isSearch) {
            textView.setText("No result found!");
        }


        allDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment parent = (SearchFragment) getParentFragment();
                parent.setPagerFragment(1);
            }
        });


        allUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment parent = (SearchFragment) getParentFragment();
                parent.setPagerFragment(2);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view){
        textView = view.findViewById(R.id.greeting_search_all);
        allDeckLayout = view.findViewById(R.id.search_decks);
        allResultLayout = view.findViewById(R.id.all_result_layout);
        allUserLayout = view.findViewById(R.id.search_users);
        allDeckButton = view.findViewById(R.id.btn_view_all_decks);
        allUserButton = view.findViewById(R.id.btn_view_all_users);
    }

    private ArrayList<Deck> getLimitDeckResult(ArrayList<Deck> originList, int noResult){
        ArrayList<Deck> results = new ArrayList<>();
        if(originList.size() <= noResult){
            return originList;
        }
        else {
            for (int i = 0; i < noResult; i++) {
                results.add(originList.get(i));
            }
        }
        return results;
    }

    private ArrayList<User> getLimitUserResult(ArrayList<User> originList, int noResult){
        ArrayList<User> results = new ArrayList<>();
        if(originList.size() <= noResult){
            return originList;
        }
        else {
            for (int i = 0; i < noResult; i++) {
                results.add(originList.get(i));
            }
        }
        return results;
    }


}
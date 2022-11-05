package com.example.prm_final_project.Ui.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.prm_final_project.Adapter.SearchPageAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Regex;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private Context thiscontext;
    private SearchView searchView;
    private ArrayList<Deck> decks;
    private ArrayList<Deck> allDecks;
    private ArrayList<User> users;
    private ArrayList<User> allUsers;
    private ViewPager viewPager;
    private SearchPageAdapter searchPageAdapter;
    private TabLayout tabLayout;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        init(view);

        // set default value for shared data between parent and child fragments
        SearchResultViewModel viewModel = new ViewModelProvider(this)
                .get(SearchResultViewModel.class);
        viewModel.setAllDecks(allDecks);
        viewModel.setAllUsers(allUsers);
        viewModel.setIsSearch(false);

        // add nested fragments into search fragment
        tabLayout = addChildFragment();
        addViewPager(tabLayout);

        decks.addAll(DeckDao.HmAllDeck.values());
        users.addAll(UserDao.allUserHT.values());

        Log.i("check size allUsers", "size " + users.toString());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String query = Regex.textNormalization(s);
                searchView.clearFocus();
                if(query != null) {
                    allDecks = DeckDao.searchDeckByTitle(decks, query);
                    allUsers = UserDao.searchByUserName(users, query);

                    // set results found into shared view model
                    viewModel.setIsSearch(true);
                    viewModel.setAllDecks(allDecks);
                    viewModel.setAllUsers(allUsers);

                    // restart child fragments
                    addViewPager(tabLayout);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // check when click on Close button
                String s2 = Regex.textNormalization(s);
                if(TextUtils.isEmpty(s2)){
                    viewModel.setAllDecks(new ArrayList<>());
                    viewModel.setAllUsers(new ArrayList<>());
                    viewModel.setIsSearch(false);
                    addViewPager(tabLayout);
                }
                return true;
            }
        });
        return view;
    }

    private TabLayout addChildFragment(){
        // add tab for tab layout
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        return tabLayout;
    }

    private void addViewPager(TabLayout tabLayout){
        // use getChildFragmentManager when having nested fragment
        searchPageAdapter = new SearchPageAdapter(
                getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(searchPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void init(View view){
        tabLayout = view.findViewById(R.id.search_result_tab);
        viewPager = view.findViewById(R.id.search_result_page);
        searchView = view.findViewById(R.id.search_bar);

        decks = new ArrayList<>();
        allDecks = new ArrayList<>();
        users = new ArrayList<>();
        allUsers = new ArrayList<>();
    }

    public void setPagerFragment(int a)
    {
        viewPager.setCurrentItem(a);
    }

}
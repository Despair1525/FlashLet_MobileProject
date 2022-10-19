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
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private Context thiscontext;
    private SearchView searchView;
    private ArrayList<Deck> decks = new ArrayList<>();
    private ArrayList<Deck> allDecks = new ArrayList<>();
    private ArrayList<User> allUsers = new ArrayList<>();
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
        viewModel.setIsSearch(false);

        // add nested fragments into search fragment
        tabLayout = addChildFragment(view);
        addViewPager(tabLayout);

        readDecks();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                if(s.trim() != null) {
                    allDecks = DeckDao.searchDeckByTitle(decks, s);
                    Log.i("search size result", allDecks.toString());

                    // set results found into shared view model
                    viewModel.setAllDecks(allDecks);
                    viewModel.setIsSearch(true);

                    // restart child fragments
                    addViewPager(tabLayout);
                    setPagerFragment(0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // check when click on Close button
                if(TextUtils.isEmpty(s)){
                    viewModel.setAllDecks(new ArrayList<>());
                    viewModel.setIsSearch(false);
                    addViewPager(tabLayout);
                }
                return true;
            }
        });
        return view;
    }

    private TabLayout addChildFragment(View view){
        // add tab for tab layout
        TabLayout tabLayout = view.findViewById(R.id.search_result_tab);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.all_result_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.all_deck_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.all_user_tab));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        return tabLayout;
    }

    private void addViewPager(TabLayout tabLayout){
        // manage screen view pager
        // use getChildFragmentManager when having nested fragment
        searchPageAdapter = new SearchPageAdapter(
                getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(searchPageAdapter);

        // set on click listener
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }
        });
    }

    public void init(View view){
        viewPager = view.findViewById(R.id.search_result_page);
        searchView = view.findViewById(R.id.search_bar);
    }

    private void readDecks(){
        DeckDao.readAllDecks(new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Deck> allDecks, Deck changeDeck, int type) {
            }
        }, decks);
    }

    public void setPagerFragment(int a)
    {
        searchPageAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(a);
    }

}
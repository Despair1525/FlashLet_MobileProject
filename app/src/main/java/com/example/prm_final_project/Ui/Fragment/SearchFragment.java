package com.example.prm_final_project.Ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.prm_final_project.Adapter.SearchPageAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Activity.MainActivity;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    Context thiscontext;
    SearchView searchView;
    ArrayList<Deck> decks = new ArrayList<>();
    ArrayList<Deck> allDecks;
    public SearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // add nested fragments into search fragment
        addChildFragment(view);

        searchView = view.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                Log.d("query text submitted", s);
//                DeckDao.readAllDecks(new FirebaseCallback() {
//                    @Override
//                    public void onResponse(ArrayList<Deck> allDecks, Deck changeDeck, int type) {
//
//                    }
//                }, decks);
//
//                allDecks = DeckDao.searchDeckByTitle(decks, s);
//                Log.d("search size result", allDecks.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private void addChildFragment(View view){
        // add tab for tab layout
        TabLayout tabLayout = view.findViewById(R.id.search_result_tab);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.all_result_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.all_deck_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.all_user_tab));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // manage screen view pager
        // use getChildFragmentManager when having nested fragment
        final ViewPager viewPager = view.findViewById(R.id.search_result_page);
        final SearchPageAdapter searchPageAdapter = new SearchPageAdapter(
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

            }
        });
    }
}
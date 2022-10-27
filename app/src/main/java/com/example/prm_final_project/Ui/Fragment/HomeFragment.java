package com.example.prm_final_project.Ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm_final_project.Adapter.CustomViewPager;
import com.example.prm_final_project.Adapter.DeckListTypeAdapter;
import com.example.prm_final_project.Adapter.HomePageAdapter;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.DeckListType;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.Services.InternetConnection;
import com.example.prm_final_project.Ui.Activity.LoginActivity;
import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Activity.MainActivity;
import com.example.prm_final_project.Ui.Activity.NoInternetActivity;
import com.example.prm_final_project.Ui.Activity.ViewAllActivity;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.Util.recomendSystem;
import com.example.prm_final_project.callbackInterface.AdapterCallback;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.example.prm_final_project.callbackInterface.RecentDeckCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class HomeFragment extends Fragment {

    private ArrayList<Deck> allDecks = new ArrayList<>();

    private ArrayList<Deck> originDecks = new ArrayList<>();
    private ArrayList<Deck> newestDecks = new ArrayList<>();
    private ArrayList<Deck> recentDecks = new ArrayList<>();
    private ArrayList<Deck> slopeOneDeck = new ArrayList<>();
    public static Hashtable<String,Deck> originDeckHt = new Hashtable<>();

    ArrayList<String> myDeckKeys = new ArrayList<>();
    Map<String,Deck> keyedDecks = new HashMap<>();
    ArrayList<Deck> personalDecks = new ArrayList<>();
    int typeDeck =0;
    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;
    FirebaseUser user ;
    User currentUser;

    private ListView lvDecks;
    private boolean inPublic = true;
    HomeDeckListAdapter homeDeckAdap,homeDeckAdapNew ;
    DeckListTypeAdapter deckListTypeAdaper;
    private RecyclerView RvPublicDeck;
    private TextView tvUserName;
    private TextView myDecks, publicDecks;
    private String m_Text = "";
    private ProgressBar PbLoading;
    private ViewPager2 viewPager;
    HomePageAdapter adapter;

    Context thiscontext;
    private boolean firstTime = true;
    TabLayout tabDeckType;

    public HomeFragment(){
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        thiscontext = container.getContext();
        View view =  inflater.inflate(R.layout.activity_homepage, container, false);
        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Data acccess
        user  = UserDao.getUser();
        Log.i("currentUser",   currentUser+"");
        // init Ui
        viewPager = view.findViewById(R.id.viewPageHomePage);
        tvUserName = view.findViewById(R.id.tvHelloUserName);
        tabDeckType =view.findViewById(R.id.tabDeckType);

        tabDeckType.addTab(tabDeckType.newTab());
        tabDeckType.addTab(tabDeckType.newTab());
        tabDeckType.addTab(tabDeckType.newTab());


        String userName="Guest";
        if(user != null){
            userName =  user.getDisplayName();
        };
        tvUserName.setText(userName);
        // Set ViewPager
        setupViewPager(viewPager);

        new TabLayoutMediator( tabDeckType, viewPager,
                (tab, position)-> {tab.setText(adapter.mFragmentTitleList.get(position));
//                tab.setCustomView(R.layout.custom_tab);
                }).attach();

        for (int i = 0; i < tabDeckType.getTabCount(); i++){

            if(i == 1){
                TextView tv = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.custom_tab2, null);
                tabDeckType.getTabAt(i).setCustomView(tv);
            }
            else if(i== 2) {
                TextView tv = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.custom_tab3, null);
                tabDeckType.getTabAt(i).setCustomView(tv);
            }
            else {
                TextView tv = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.custom_tab, null);
                tabDeckType.getTabAt(i).setCustomView(tv);
            }
        }
        return view;
    }

    private void setupViewPager(ViewPager2 viewPager) {

        adapter =new HomePageAdapter(getActivity().getSupportFragmentManager(),
                getActivity().getLifecycle()    );
        adapter.addFragment(new  PublicDeckFragment(), "Public");
        adapter.addFragment(new RecentDeckFragment(), "Recent");
        adapter.addFragment(new RecomenDeckFragment(), "Popular");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

    }



}

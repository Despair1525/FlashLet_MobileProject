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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm_final_project.Adapter.DeckListTypeAdapter;
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
import com.example.prm_final_project.Ui.Activity.NoInternetActivity;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.callbackInterface.AdapterCallback;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.example.prm_final_project.callbackInterface.RecentDeckCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private ArrayList<Deck> originDecks = new ArrayList<>();
    private ArrayList<Deck> allDecks = new ArrayList<>();
    private ArrayList<Deck> newestDecks = new ArrayList<>();
    private ArrayList<Deck> recentDecks = new ArrayList<>();
    private ArrayList<RecentDeck> recentDeckKeys = new ArrayList<>();
    public static HashMap<String,Deck> originDeckHm = new HashMap<>();


    ArrayList<String> myDeckKeys = new ArrayList<>();
    Map<String,Deck> keyedDecks = new HashMap<>();
    ArrayList<Deck> personalDecks = new ArrayList<>();
    int typeDeck =0;
    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;
    FirebaseUser user ;

    private ListView lvDecks;
    private boolean inPublic = true;
    HomeDeckListAdapter homeDeckAdap,homeDeckAdapNew ;
    DeckListTypeAdapter deckListTypeAdaper;
    private RecyclerView RvPublicDeck,RvListDeckType;
    private TextView tvUserName;
    private TextView myDecks, publicDecks;
    private String m_Text = "";
    private ProgressBar PbLoading;
    Context thiscontext;
    private boolean firstTime = true;

    public HomeFragment(){
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        // Setting List

        // check Internet conditions
//        if(!InternetConnection.isConnectedToInternet(getContext())) {
//            Intent i = new Intent(getActivity(),NoInternetActivity.class);
//            getActivity().finish();
//        };

        thiscontext = container.getContext();
        View view =  inflater.inflate(R.layout.activity_homepage, container, false);
        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user  = UserDao.getUser();

        // init Ui
        tvUserName = view.findViewById(R.id.tvHelloUserName);
        PbLoading =  view.findViewById(R.id.pbLoadingData);
        RvPublicDeck = view.findViewById(R.id.RvDecksPublic);
        RvListDeckType = view.findViewById(R.id.RvListDeckType);


//      Get data
        String userName="Guest";
        if(user != null){
            userName =  user.getDisplayName();
        };
        tvUserName.setText(userName);

        // Authentication
        if(firstTime) {
            PbLoading.setVisibility(ProgressBar.VISIBLE);
            ArrayList<DeckListType> listIem = initDeckList();
            DeckDao.readAllDecks(new FirebaseCallback() {
                @Override
                public void onResponse(ArrayList<Deck> Decks, Deck changeDeck, int type) {
                    if (typeDeck == 0) {
                        if (type == 0) {
                            if (changeDeck != null) {
//                            newestDecks.add(changeDeck);
                                allDecks.add(changeDeck);
                            }
                        }
                        ;
                        if (type == 1) {
                            int changeDeckIndex = Methods.indexDeck(allDecks, changeDeck);
                            if (changeDeckIndex != -1) {
//                            newestDecks.set(changeDeckIndex, changeDeck);
                                allDecks.set(changeDeckIndex, changeDeck);
                            }
                            ;
                        }
                        if (type == 2) {
//                        newestDecks.remove(changeDeck);
                            allDecks.remove(changeDeck);
                        }
                        ;
                        PbLoading.setVisibility(ProgressBar.GONE);
                        homeDeckAdap.notifyDataSetChanged();
                    }
                };
            }, originDecks);

            DeckDao.readRecentDeckByUser(recentDeckKeys,new RecentDeckCallback() {
                @Override
                public void onResponse(ArrayList<RecentDeck> allDecks, RecentDeck changeDeck, int type) {
                    Log.i("HomeFragment - RecentDeck",changeDeck.getId()+"");
                }
            });

            deckListTypeAdaper = new DeckListTypeAdapter(listIem, new AdapterCallback() {
                @Override
                public void onResponse( int type) {
                    new CountDownTimer(1000, 1000) {
                        public void onFinish() {
                            RvPublicDeck.setVisibility(RecyclerView.VISIBLE);
                            PbLoading.setVisibility(ProgressBar.GONE);
                        }

                        public void onTick(long millisUntilFinished) {
                            RvPublicDeck.setVisibility(RecyclerView.GONE);
                            PbLoading.setVisibility(ProgressBar.VISIBLE);
                        }
                    }.start();
                    typeDeck = type;
                    if(type == 0){
                        changeRecle(allDecks,originDecks);
                    };
                    if(type == 1){
                        Collections.sort(recentDeckKeys, (o1, o2) -> {
                            return (int) (o2.getTimeStamp() - o1.getTimeStamp());
                        });
                        changeRecle(allDecks,recentDecks);
                        matchHashMapRecentDeck(recentDeckKeys,recentDecks,DeckDao.originDeck);
                    };
                    if(type == 2){
                        Toast.makeText(getActivity(),"You enter Popular",Toast.LENGTH_SHORT).show();
                    };
                    homeDeckAdap.notifyDataSetChanged();
                }
            }, RvListDeckType);

        firstTime = false;
        };


        homeDeckAdap = new HomeDeckListAdapter(thiscontext,allDecks);
        RvListDeckType.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        RvListDeckType.setAdapter((deckListTypeAdaper));
        RvPublicDeck.setAdapter( homeDeckAdap);
        RvPublicDeck.setLayoutManager(new LinearLayoutManager((getActivity())));
        return view;
    }

    private ArrayList<DeckListType> initDeckList() {
        ArrayList<DeckListType> listIem = new ArrayList<>();
        listIem.add(new DeckListType(R.drawable.public_icon,"Public Deck"));
        listIem.add(new DeckListType(R.drawable.rectnly_icon,"Recently Deck"));
        listIem.add(new DeckListType(R.drawable.popular_deck,"Popular Deck"));
return listIem;
    }
public void changeRecle(ArrayList<Deck> a , ArrayList<Deck> b){

    a.clear();
    for(int i = 0; i<b.size();i++) {
       a.add(b.get(i));
    };

};
    public FirebaseUser checkGuest(){
        FirebaseUser user = mAuth.getCurrentUser();
       return user;
    }


    public void onClick(View view) {

    }


    public void matchHashMapRecentDeck(ArrayList<RecentDeck> aKey ,ArrayList<Deck> a ,HashMap<String,Deck> b){
        a.clear();
        for(RecentDeck key: aKey) {
            Deck temp = b.get(key.getDeckId());
            if(temp != null) {
                a.add(temp);
            };
        };

    };
}

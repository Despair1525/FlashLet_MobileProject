package com.example.prm_final_project.Ui.Fragment;

import android.app.ProgressDialog;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm_final_project.Adapter.DeckListTypeAdapter;
import com.example.prm_final_project.Model.DeckListType;
import com.example.prm_final_project.Ui.Activity.LoginActivity;
import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.callbackInterface.AdapterCallback;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private ArrayList<Deck> originDecks = new ArrayList<>();
    private ArrayList<Deck> allDecks = new ArrayList<>();
    private ArrayList<Deck> newestDecks = new ArrayList<>();

    ArrayList<String> myDeckKeys = new ArrayList<>();
    Map<String,Deck> keyedDecks = new HashMap<>();
    ArrayList<Deck> personalDecks = new ArrayList<>();
    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;
    FirebaseUser user ;

    private ListView lvDecks;
    private boolean isGuest =  true;
    private boolean inPublic = true;
    HomeDeckListAdapter homeDeckAdap,homeDeckAdapNew ;
    DeckListTypeAdapter deckListTypeAdaper;
    private RecyclerView RvPublicDeck,RvListDeckType;
    private TextView tvUserName;
    private TextView myDecks, publicDecks, logout;
    private String m_Text = "";
    private ProgressBar PbLoading;

    Context thiscontext;
    private boolean firstTime = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        // Setting List


        thiscontext = container.getContext();
        View view =  inflater.inflate(R.layout.activity_homepage, container, false);
        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user  = checkGuest();

        tvUserName = view.findViewById(R.id.tvHelloUserName);
        PbLoading =  view.findViewById(R.id.pbLoadingData);


//      Get data
        String userName="Guest";
        if(user != null){
            userName =  user.getDisplayName();
        };
        tvUserName.setText(userName);

        // Authentication
        if(firstTime) {
            PbLoading.setVisibility(ProgressBar.VISIBLE);
            DeckDao.readAllDecks(new FirebaseCallback() {
                @Override
                public void onResponse(ArrayList<Deck> Decks, Deck changeDeck, int type) {
                    if (type == 0) {
                        if(changeDeck != null) {
                            newestDecks.add(changeDeck);
                            allDecks.add(changeDeck);
                        }
                        ;
                    }
                    ;
                    if (type == 1) {
                        int changeDeckIndex = Methods.indexDeck(newestDecks, changeDeck);
                        if (changeDeckIndex != -1) {
                            newestDecks.set(changeDeckIndex, changeDeck);
                            allDecks.set(changeDeckIndex,changeDeck);
                        }
                        ;
                    }
                    ;
                    if (type == 2) {
                        newestDecks.remove(changeDeck);
                        allDecks.remove(changeDeck);
                    }
                    ;
                    Collections.sort(newestDecks, (o1, o2) -> {
                        try {
                            return (Methods.compareStringDate(o1.getDate(), o2.getDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    });
                    PbLoading.setVisibility(ProgressBar.GONE);
                    homeDeckAdap.notifyDataSetChanged();
                }
            }, originDecks);
        firstTime = false;
        };

        logout =  view.findViewById(R.id.tvLogout);
        logout.setOnClickListener(this::onClick);

        homeDeckAdap = new HomeDeckListAdapter(thiscontext,allDecks);

        RvPublicDeck = view.findViewById(R.id.RvDecksPublic);
        RvListDeckType = view.findViewById(R.id.RvListDeckType);
        ArrayList<DeckListType> listIem = initDeckList();

        deckListTypeAdaper = new DeckListTypeAdapter(listIem, new AdapterCallback() {
            @Override
            public void onResponse( int type) {

                if(type == 0){
                    changeRecle(allDecks,originDecks);
//                    Toast.makeText(getActivity(),"You enter public",Toast.LENGTH_SHORT).show();
                };
                if(type == 1){
//(String deckId, String Uid, String title, String descriptions ,String author,String date ,boolean isPublic ,int view,List<List<String>> cards)
                    changeRecle(allDecks,newestDecks);
                };
                if(type == 2){
                    Toast.makeText(getActivity(),"You enter Popular",Toast.LENGTH_SHORT).show();
                };

                homeDeckAdap.notifyDataSetChanged();
            }
        });
        RvListDeckType.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        RvListDeckType.setAdapter((deckListTypeAdaper));

// Set style For ListView (Adjust )
//        homeDeckAdapNew = new HomeDeckListAdapter(thiscontext,newestDecks);


// Set For List Newest

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

    for(int i = 0; i<a.size();i++) {
       a.set(i,b.get(i));
    };

};
    public FirebaseUser checkGuest(){
        FirebaseUser user = mAuth.getCurrentUser();
       return user;
    }
    public void logout(){
        mAuth.signOut();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.putExtra("allDecks", allDecks);
        startActivity(i);
    }

    public void onClick(View view) {
        if(view == logout) {
            logout();
        }
    }
}

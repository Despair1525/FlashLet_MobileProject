package com.example.prm_final_project.Ui.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private ArrayList<Deck> allDecks = new ArrayList<>();
    private ArrayList<Deck> newestDecks = new ArrayList<>();
    ArrayList<String> myDeckKeys = new ArrayList<>();
    Map<String,Deck> keyedDecks = new HashMap<>();
    ArrayList<Deck> personalDecks = new ArrayList<>();
    private ListView lvDecks;
    private boolean isGuest =  true;
    private boolean inPublic = true;
    HomeDeckListAdapter homeDeckAdap,homeDeckAdapNew ;
    DeckListTypeAdapter deckListTypeAdaper;
    private RecyclerView RvPublicDeck,RvListDeckType;
    private TextView tvUserName;
    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;
    FirebaseUser user ;
    ProgressDialog loading;
    private TextView myDecks, publicDecks, logout;
    private String m_Text = "";
    Context thiscontext;
    private boolean firstTime = true;

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
        user  = checkGuest();

        tvUserName = view.findViewById(R.id.tvHelloUserName);
//      Get data
        String userName="Guest";
        if(user != null){
            userName =  user.getDisplayName();
        };
        tvUserName.setText(userName);

        // Authentication
        if(firstTime) {
            DeckDao.readAllDecks(new FirebaseCallback() {
                @Override
                public void onResponse(ArrayList<Deck> allDecks, Deck changeDeck, int type) {
                    if (type == 0) {
                        if(changeDeck != null)  newestDecks.add(changeDeck);

                    }
                    ;
                    if (type == 1) {
                        int changeDeckIndex = Methods.indexDeck(newestDecks, changeDeck);
                        if (changeDeckIndex != -1) {
                            newestDecks.set(changeDeckIndex, changeDeck);
                        }
                        ;
                    }
                    ;
                    if (type == 2) {
                        newestDecks.remove(changeDeck);
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
                    homeDeckAdap.notifyDataSetChanged();
                }
            }, allDecks);
        firstTime = false;
        };
        logout =  view.findViewById(R.id.tvLogout);
        logout.setOnClickListener(this::onClick);

        RvPublicDeck = view.findViewById(R.id.RvDecksPublic);
        RvListDeckType = view.findViewById(R.id.RvListDeckType);
        ArrayList<DeckListType> listIem = initDeckList();
        deckListTypeAdaper = new DeckListTypeAdapter(listIem);
        RvListDeckType.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        RvListDeckType.setAdapter((deckListTypeAdaper));



// Set style For ListView (Adjust )
//        homeDeckAdapNew = new HomeDeckListAdapter(thiscontext,newestDecks);

        homeDeckAdap = new HomeDeckListAdapter(thiscontext,allDecks);
// Set For List Newest

        RvPublicDeck.setAdapter( homeDeckAdap);
        RvPublicDeck.setLayoutManager(new LinearLayoutManager((getActivity())));

//        initSlideCardNewEst(viewPager2New);

        //// Set for list Popular
//        viewPager2Popular.setAdapter(homeDeckAdap);
//        initSlideCardNewEst(viewPager2Popular);
//        /// Set for list Recomend
//        viewPager2Reco.setAdapter(homeDeckAdap);
//        initSlideCardNewEst(viewPager2Reco);
//         Click vao 1 deck bat ky

        return view;
    }

    private ArrayList<DeckListType> initDeckList() {
        ArrayList<DeckListType> listIem = new ArrayList<>();
        listIem.add(new DeckListType(R.drawable.public_icon,"Public Deck"));
        listIem.add(new DeckListType(R.drawable.rectnly_icon,"Recently Deck"));
        listIem.add(new DeckListType(R.drawable.popular_deck,"Popular Deck"));
return listIem;
    }

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

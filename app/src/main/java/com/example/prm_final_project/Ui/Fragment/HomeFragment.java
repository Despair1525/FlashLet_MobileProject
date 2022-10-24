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
import com.example.prm_final_project.Ui.Activity.MainActivity;
import com.example.prm_final_project.Ui.Activity.NoInternetActivity;
import com.example.prm_final_project.Ui.Activity.ViewAllActivity;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.Util.recomendSystem;
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
import java.util.Hashtable;
import java.util.Map;

public class HomeFragment extends Fragment {

    private ArrayList<Deck> allDecks = new ArrayList<>();

    private ArrayList<Deck> originDecks = new ArrayList<>();
    private ArrayList<Deck> newestDecks = new ArrayList<>();
    private ArrayList<Deck> recentDecks = new ArrayList<>();

    private ArrayList<RecentDeck> recentDeckKeys = new ArrayList<>();
    private ArrayList<Deck> slopeOneDeck = new ArrayList<>();

    public static Hashtable<String,Deck> originDeckHm = new Hashtable<>();


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

        thiscontext = container.getContext();
        View view =  inflater.inflate(R.layout.activity_homepage, container, false);
        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Data acccess
        user  = UserDao.getUser();
        currentUser = UserDao.allUserHT.get(user.getUid());
        originDeckHm = DeckDao.HmAllDeck;

        Log.i("numberDeck",  originDeckHm.size()+"");
        Log.i("currentUser",   currentUser+"");
        // init Ui
        tvUserName = view.findViewById(R.id.tvHelloUserName);
        PbLoading =  view.findViewById(R.id.pbLoadingData);
        RvPublicDeck = view.findViewById(R.id.RvDecksPublic);
        RvListDeckType = view.findViewById(R.id.RvListDeckType);


        String userName="Guest";
        if(user != null){
            userName =  user.getDisplayName();
        };
        tvUserName.setText(userName);



            PbLoading.setVisibility(ProgressBar.VISIBLE);
            ArrayList<DeckListType> listIem = initDeckList();





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
                        ArrayList<Deck>  allDeckArray = new ArrayList<>();
                        allDeckArray.addAll(originDeckHm.values());

                        changeRecle(allDecks,allDeckArray);

                    };
                    if(type == 1){
                        recentDeckKeys = currentUser.getRecentDecks();
                        Collections.sort(recentDeckKeys, (o1, o2) -> {
                            return (int) (o2.getTimeStamp() - o1.getTimeStamp());
                        });
                        ArrayList<Deck> recentDeck = new ArrayList<>();
                        recentDeck =  matchHashMapRecentDeck();
                        changeRecle(allDecks,recentDeck);

                    };
                    if(type == 2){
//                        RvPublicDeck.setVisibility(RecyclerView.GONE);
//                        PbLoading.setVisibility(ProgressBar.VISIBLE);
                        ArrayList<Deck>  allDeckArray = new ArrayList<>();
                        ArrayList<User>  allUserArray = new ArrayList<>();
                        allDeckArray.addAll(originDeckHm.values());
                        allUserArray.addAll(UserDao.allUserHT.values());

                        recomendSystem rs = new recomendSystem();

                        rs.slopeOne(allDeckArray,allUserArray);
                        ArrayList<Deck> listReco = new ArrayList<>();
                        Log.i("RecoSize",UserDao.allUsers.size()+"");
                        HashMap<Deck, Double> listRecomend = rs.outputData.get(UserDao.getUser().getUid());
                        listReco.addAll(listRecomend.keySet());

                        Collections.sort(listReco, (o1, o2) -> (listRecomend.get(o1) - listRecomend.get(o2)) > 0 ? 1 : -1);
//                        Toast.makeText(getActivity(),"You enter Popular",Toast.LENGTH_SHORT).show();\
                        changeRecle(allDecks,listReco);
                        rs.printData();
                    };
                    homeDeckAdap.notifyDataSetChanged();
                }
            }, RvListDeckType);




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

    public  ArrayList<Deck> matchHashMapRecentDeck(){
        ArrayList<Deck> recentDeck = new ArrayList<>();
        Log.i("USERDAO_childChangedfound",recentDeckKeys.size()+"");
        for(RecentDeck key: recentDeckKeys) {
            Deck temp = DeckDao.HmAllDeck.get(key.getDeckId());
            if(temp != null) {
                recentDeck.add(temp);
                Log.i("USERDAO_childChangedfound",temp.getTitle());
            }

        };
        return recentDeck;

    };
}

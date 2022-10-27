package com.example.prm_final_project.Ui.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Activity.NoInternetActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentDeckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentDeckFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecentDeckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentDeckFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentDeckFragment newInstance(String param1, String param2) {
        RecentDeckFragment fragment = new RecentDeckFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ArrayList<Deck> recentDecks ;
    private ArrayList<RecentDeck> recentDeckKeys = new ArrayList<>();
    public static Hashtable<String,Deck> originDeckHt = new Hashtable<>();

    RecyclerView RvPublicDeck;
    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;
    FirebaseUser user ;
    User currentUser;

    HomeDeckListAdapter homeDeckAdap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent_deck, container, false);
        recentDecks = new ArrayList<>();
        originDeckHt = DeckDao.HmAllDeck;
        // Get User
        user  = UserDao.getUser();
        currentUser = UserDao.allUserHT.get(user.getUid());

        if(currentUser == null) {
            Log.i("here","fuk");
            Intent i = new Intent(getActivity(), NoInternetActivity.class);
            startActivity(i);
            getActivity().finish();

        }else {
            // Get RecentDeck;
            Log.i("eror", currentUser.getUserId());
            recentDeckKeys = currentUser.getRecentDecks();

            Collections.sort(recentDeckKeys, (o1, o2) -> {
                return (int) (o2.getTimeStamp() - o1.getTimeStamp());
            });
            ArrayList<Deck> recentDeck = new ArrayList<>();
            recentDecks = matchHashMapRecentDeck();

            RvPublicDeck = view.findViewById(R.id.RvDecksRecent);
            RvPublicDeck.setNestedScrollingEnabled(false);
            homeDeckAdap = new HomeDeckListAdapter(getActivity(), recentDecks);

            RvPublicDeck.setAdapter(homeDeckAdap);
            RvPublicDeck.setLayoutManager(new LinearLayoutManager((getActivity())));
            homeDeckAdap.notifyDataSetChanged();
        };
            return view;
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
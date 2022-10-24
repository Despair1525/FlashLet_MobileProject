package com.example.prm_final_project.Ui.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Hashtable;


public class PublicDeckFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PublicDeckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment PublicDeckFragment.
     */
    // TODO: Rename and change types and number of parameters


    private ArrayList<Deck> allDecks ;
    public static Hashtable<String,Deck> originDeckHt = new Hashtable<>();

    RecyclerView RvPublicDeck;
    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;
    FirebaseUser user ;
    User currentUser;

    ProgressBar pb;
    HomeDeckListAdapter homeDeckAdap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_public_deck, container, false);
        pb = view.findViewById(R.id.loadPb);
        pb.setVisibility(ProgressBar.VISIBLE);
        allDecks = new ArrayList<>();
        originDeckHt = DeckDao.HmAllDeck;
        Log.i("HmAllDeckSize",originDeckHt.size()+"");
        allDecks.addAll(originDeckHt.values());

        RvPublicDeck = view.findViewById(R.id.RvDecksPublic);
        RvPublicDeck.setNestedScrollingEnabled(false);
        homeDeckAdap = new HomeDeckListAdapter(getActivity(),allDecks);

        RvPublicDeck.setAdapter( homeDeckAdap);
        RvPublicDeck.setLayoutManager(new LinearLayoutManager((getActivity())));

        homeDeckAdap.notifyDataSetChanged();

        pb.setVisibility(ProgressBar.GONE);
        return view;
    }
}
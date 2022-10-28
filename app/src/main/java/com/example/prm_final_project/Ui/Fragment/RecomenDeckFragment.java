package com.example.prm_final_project.Ui.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.prm_final_project.Util.recomendSystem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecomenDeckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecomenDeckFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecomenDeckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecomenDeckFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecomenDeckFragment newInstance(String param1, String param2) {
        RecomenDeckFragment fragment = new RecomenDeckFragment();
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
    private ArrayList<Deck> popularDecks ;
    private ArrayList<Deck> allDecks = new ArrayList<>();

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
        View view =  inflater.inflate(R.layout.fragment_recomen_deck, container, false);

        popularDecks = new ArrayList<>();
        allDecks = new ArrayList<>();
        originDeckHt = DeckDao.HmAllDeck;

        allDecks.addAll(getPublicCard());
        if(allDecks.size() > 11) {
            popularDecks.addAll(allDecks.subList(0, 10));
        }

        else{
            popularDecks.addAll(allDecks);
        };
        Collections.sort( popularDecks, (o1, o2) -> {
            return (int) (o2.getView() - o1.getView());
        });

        RvPublicDeck = view.findViewById(R.id.RvDecksPopular);
        RvPublicDeck.setNestedScrollingEnabled(false);
        homeDeckAdap = new HomeDeckListAdapter(getActivity(),popularDecks);

        RvPublicDeck.setAdapter( homeDeckAdap);
        RvPublicDeck.setLayoutManager(new LinearLayoutManager((getActivity())));
        homeDeckAdap.notifyDataSetChanged();
        return view;
    }

    public List<Deck> getPublicCard(){
        return originDeckHt.values().stream().filter(deck -> deck.isPublic()).collect(Collectors.toList());
    };
}
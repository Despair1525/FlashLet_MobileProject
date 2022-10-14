package com.example.prm_final_project.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm_final_project.Activity.EditDeckActivity;
import com.example.prm_final_project.Activity.HomePageActivity;
import com.example.prm_final_project.Activity.LoginActivity;
import com.example.prm_final_project.Activity.MainActivity;
import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Module.Deck;
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
    private boolean isGuest = true;
    private boolean inPublic = true;
    HomeDeckListAdapter homeDeckAdap,homeDeckAdapNew ;
    private ViewPager2 viewPager2New, viewPager2Popular, viewPager2Reco;
    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;
    ProgressDialog loading;
    private ImageView addDeck;
    private TextView myDecks, publicDecks, logout;
    private SearchView svDecks;
    private String m_Text = "";

    Context thiscontext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        thiscontext = container.getContext();
        View view =  inflater.inflate(R.layout.activity_homepage, container, false);

//      Get data
        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Authentication
        isGuest = checkGuest();
        DeckDao.readAllDecks(new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Deck> allDecks,Deck changeDeck, int type) {
                if(type == 0) {
                    newestDecks.add(changeDeck);
                };
                if(type == 1) {
                    int changeDeckIndex = Methods.indexDeck(newestDecks,changeDeck);
                    if(changeDeckIndex != -1) {
                        newestDecks.set(changeDeckIndex,changeDeck);
                    };
                };
                if(type == 2) {
                    newestDecks.remove(changeDeck);
                };
                Collections.sort( newestDecks, (o1, o2) -> {
                    try {
                        return (Methods.compareStringDate(o1.getDate(),o2.getDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                });
                homeDeckAdap.notifyDataSetChanged();
                homeDeckAdapNew.notifyDataSetChanged();
            }
        },allDecks);

/////////////////////////////
        svDecks = view.findViewById(R.id.svSearchPublic);
        logout =  view.findViewById(R.id.tvLogout);
        addDeck = view.findViewById(R.id.abPlusPublic);
        addDeck.setOnClickListener(this::onClick);
        logout.setOnClickListener(this::onClick);
//        RcListDeck = findViewById(R.id.RvDecksPublic);
        viewPager2New = view.findViewById(R.id.RvDecksPublic);
        viewPager2Popular = view.findViewById(R.id.RvDecksPublicPopular);
        viewPager2Reco = view.findViewById(R.id.RvDecksPublicReco);

/////////////////////// Set style For ListView (Adjust )
        homeDeckAdap = new HomeDeckListAdapter(thiscontext,allDecks);
        homeDeckAdapNew = new HomeDeckListAdapter(thiscontext,newestDecks);
//      Set For List Newest


        viewPager2New.setAdapter(homeDeckAdapNew);
        initSlideCardNewEst(viewPager2New);

        //// Set for list Popular
        viewPager2Popular.setAdapter(homeDeckAdap);
        initSlideCardNewEst(viewPager2Popular);
        /// Set for list Recomend
        viewPager2Reco.setAdapter(homeDeckAdap);
        initSlideCardNewEst(viewPager2Reco);
//         Click vao 1 deck bat ky

        return view;
    }

    public void initSlideCardNewEst(ViewPager2 viewPager2){

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1- Math.abs(position);
                page.setScaleY(0.85f + r* 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
    };

    public boolean checkGuest(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user==null){
            return true;
        }
        Toast.makeText(thiscontext, "Hello"+user.getDisplayName(), Toast.LENGTH_SHORT).show();
        return false;
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
        if(view == addDeck) {
            if(isGuest) {
                Toast.makeText(thiscontext,"You have to login !",Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(thiscontext);
                builder.setTitle("Title");
                final EditText input = new EditText(thiscontext);
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Intent i =  new Intent(getActivity(), EditDeckActivity.class);
                        i.putExtra("editTitle",m_Text);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            };

        };

    }
}

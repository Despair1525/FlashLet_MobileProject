package com.example.prm_final_project.Ui.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm_final_project.Adapter.DailyAdapter;
import com.example.prm_final_project.Adapter.HomePageAdapter;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Deck> allDecks = new ArrayList<>();

    private ArrayList<Deck> originDecks = new ArrayList<>();
    private ArrayList<Deck> newestDecks = new ArrayList<>();
    private ArrayList<Deck> recentDecks = new ArrayList<>();
    private ArrayList<Deck> slopeOneDeck = new ArrayList<>();
    public static Hashtable<String, Deck> originDeckHt = new Hashtable<>();

    ArrayList<String> myDeckKeys = new ArrayList<>();
    Map<String, Deck> keyedDecks = new HashMap<>();
    ArrayList<Deck> personalDecks = new ArrayList<>();
    int typeDeck = 0;
    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    User currentUser;

    private ListView lvDecks;
    private boolean inPublic = true;
    HomeDeckListAdapter homeDeckAdap, homeDeckAdapNew;
    DailyAdapter deckListTypeAdaper;
    private RecyclerView RvPublicDeck,RvTimetable;
    private TextView tvUserName;
    private TextView tvActivity;
    private TextView myDecks, publicDecks;
    private String m_Text = "";
    private ProgressBar PbLoading;
    private ViewPager2 viewPager;

    DailyAdapter dailyAdapter;
    HomePageAdapter adapter;

    Context thiscontext;
    private boolean firstTime = true;
    TabLayout tabDeckType;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        thiscontext = container.getContext();
        View view = inflater.inflate(R.layout.activity_homepage, container, false);
        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Data acccess
        user = UserDao.getUser();
        User currentUser = UserDao.allUserHT.get(user.getUid());
        Log.i("currentUser", currentUser + "");
        // init Ui
        tvActivity = view.findViewById(R.id.tvActivity);
        viewPager = view.findViewById(R.id.viewPageHomePage);
        tvUserName = view.findViewById(R.id.tvHelloUserName);
        tabDeckType = view.findViewById(R.id.tabDeckType);
        RvTimetable = view.findViewById(R.id.Rvtimetable);

        tabDeckType.addTab(tabDeckType.newTab());
        tabDeckType.addTab(tabDeckType.newTab());
        tabDeckType.addTab(tabDeckType.newTab());

        tvActivity.setOnClickListener(this);
        // set time table adapter
        dailyAdapter = new DailyAdapter(RvTimetable);
        RvTimetable.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        RvTimetable.setAdapter(dailyAdapter);
        RvTimetable.scrollToPosition(2);

        String userName = "Guest";
        if (currentUser != null) {
            userName = currentUser.getUsername();
        }
        tvUserName.setText(userName);
        // Set ViewPager
        setupViewPager(viewPager);
        new TabLayoutMediator(tabDeckType, viewPager,
                (tab, position) -> {
                    tab.setText(adapter.mFragmentTitleList.get(position));
//                tab.setCustomView(R.layout.custom_tab);
                }).attach();

        for (int i = 0; i < tabDeckType.getTabCount(); i++) {

            if (i == 1) {
                TextView tv = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.custom_tab2, null);
                tabDeckType.getTabAt(i).setCustomView(tv);
            } else if (i == 2) {
                TextView tv = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.custom_tab3, null);
                tabDeckType.getTabAt(i).setCustomView(tv);
            } else {
                TextView tv = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.custom_tab, null);
                tabDeckType.getTabAt(i).setCustomView(tv);
            }
        }
        return view;
    }

    private void setupViewPager(ViewPager2 viewPager) {

        adapter = new HomePageAdapter(getActivity().getSupportFragmentManager(),
                getActivity().getLifecycle());
        adapter.addFragment(new PublicDeckFragment(), "Public");
        adapter.addFragment(new RecentDeckFragment(), "Recent");
        adapter.addFragment(new RecomenDeckFragment(), "Popular");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

    }


    @Override
    public void onClick(View v) {
        if(v == tvActivity){
            Toast.makeText(getActivity(), "YOUR ACTIVITY", Toast.LENGTH_SHORT).show();
        };
    }
}

package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.os.Bundle;

import com.example.prm_final_project.Adapter.AllViewDeckAdapter;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Fragment.ViewAllDeckFragment;
import com.example.prm_final_project.Ui.Fragment.ViewAllMyDeckFragment;
import com.google.android.material.tabs.TabLayout;

public class ViewAllActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        // init Support Action Bar
        initSupportActionBar();

        // init Tab And View Pager
        tabLayout = findViewById(R.id.typeDeck_tab);
        viewPager = findViewById(R.id.search_result_page);
        tabLayout.setupWithViewPager(viewPager);

        AllViewDeckAdapter allViewDeckAdapter = new AllViewDeckAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        // add Fragment into Adapter
        addFragment(allViewDeckAdapter);

        viewPager.setAdapter(allViewDeckAdapter);
    }

    private void addFragment(AllViewDeckAdapter allViewDeckAdapter) {
        allViewDeckAdapter.addFragment(new ViewAllDeckFragment(),"All Decks");
        allViewDeckAdapter.addFragment(new ViewAllMyDeckFragment(),"My Decks");

    }

    private void initSupportActionBar() {
        getSupportActionBar().setTitle("View All Decks");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
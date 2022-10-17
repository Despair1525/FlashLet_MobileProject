package com.example.prm_final_project.Adapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.prm_final_project.Ui.Fragment.AllDeckFragment;
import com.example.prm_final_project.Ui.Fragment.AllResultFragment;
import com.example.prm_final_project.Ui.Fragment.AllUserFragment;

public class SearchPageAdapter extends FragmentStatePagerAdapter {
    int numberOfTabs;
    private final static int ALL_RESULT_FRAGMENT = 0;
    private final static int ALL_DECK_FRAGMENT = 1;
    private final static int ALL_USER_FRAGMENT = 2;

    public SearchPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public SearchPageAdapter(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm);
        this.numberOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case ALL_RESULT_FRAGMENT:
                return new AllResultFragment();
            case ALL_DECK_FRAGMENT:
                return new AllDeckFragment();
            case ALL_USER_FRAGMENT:
                return new AllUserFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    // restore
    @Override
    public void restoreState(final Parcelable state, final ClassLoader loader) {
        try {
            super.restoreState(state, loader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

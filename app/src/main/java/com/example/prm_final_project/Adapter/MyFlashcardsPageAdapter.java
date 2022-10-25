package com.example.prm_final_project.Adapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.prm_final_project.Ui.Fragment.FavouriteFragment;
import com.example.prm_final_project.Ui.Fragment.MyFlashcardsFragment;

public class MyFlashcardsPageAdapter extends FragmentPagerAdapter {
    int numberOfTabs = 2;
    private final static int MY_FLASHCARD_FRAGMENT = 0;
    private final static int FAVOURITE_FRAGMENT = 1;

    private FragmentManager fragmentManager;
    public MyFlashcardsPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    public MyFlashcardsPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MY_FLASHCARD_FRAGMENT:
                fragment = new MyFlashcardsFragment();
                break;
            case FAVOURITE_FRAGMENT:
                fragment = new FavouriteFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public String getPageTitle(int postion) {
        super.getPageTitle(postion);
        switch(postion) {
            case 0:
                return "My Flashcards";
            case 1:
                return "Favourite";
        }
        return null;
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

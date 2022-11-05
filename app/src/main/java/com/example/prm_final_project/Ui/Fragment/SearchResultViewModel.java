package com.example.prm_final_project.Ui.Fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;

import java.util.ArrayList;

public class SearchResultViewModel extends ViewModel {
    MutableLiveData<ArrayList<Deck>> allDecks = new MutableLiveData<>();
    MutableLiveData<Boolean> isSearch = new MutableLiveData<>();
    MutableLiveData<ArrayList<User>> allUsers = new MutableLiveData<>();

    public void setAllDecks(ArrayList<Deck> decks){
        allDecks.setValue(decks);
    }

    public MutableLiveData<ArrayList<Deck>> getAllDecks(){
        return allDecks;
    }

    public void setAllUsers(ArrayList<User> users) {
        allUsers.setValue(users);
    }

    public MutableLiveData<ArrayList<User>> getAllUsers() {
        return allUsers;
    }

    public void setIsSearch(Boolean flag){
        isSearch.setValue(flag);
    }

    public MutableLiveData<Boolean> getIsSearch(){
        return isSearch;
    }
}

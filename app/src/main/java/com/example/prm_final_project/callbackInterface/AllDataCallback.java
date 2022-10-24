package com.example.prm_final_project.callbackInterface;

import com.example.prm_final_project.Model.Deck;

import java.util.ArrayList;
import java.util.Hashtable;

public interface AllDataCallback {
    void onResponse(Hashtable<String,Deck> allDecks);

}

package com.example.prm_final_project.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prm_final_project.Util.Methods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Deck implements Serializable {
    String title,descriptions , Uid, deckId,date;
    boolean isPublic;
    int view;
    List<List<String>> cards = new ArrayList<>();

    public Deck(){
        this.cards = new ArrayList<>();
    }

    public Deck(String title,String uid){
        this.title = title;
        this.cards = new ArrayList<>();
        this.isPublic = true;
        this.view = 0;
        this.date = Methods.getDate();
        this.Uid = uid;
    }

    public Deck (String deckId, String Uid, String title, String descriptions ,String date ,boolean isPublic ,int view,List<List<String>> cards){
        this.deckId = deckId;
        this.title = title;
        this.cards = cards;
        this.Uid = Uid;
        this.isPublic =isPublic;
        this.date = date;
        this.view = view;
        this.descriptions = descriptions;
    }
    public Deck (String deckId, String Uid, String title, String date ,boolean isPublic ,List<List<String>> cards){
        this.deckId = deckId;
        this.title = title;
        this.cards = cards;
        this.Uid = Uid;
        this.isPublic =isPublic;
        this.date = date;
    }



    public String getTitle() {
        return title;
    }
    public String getDeckId() {return deckId;}
    public String getUid(){
        return Uid;
    }
    public List<List<String>> getCards(){
        return cards;
    }

    public boolean isPublic() {
        return isPublic;
    }
    public void setUid(String uid) {
        Uid = uid;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public void setCards(List<List<String>> cards) {
        this.cards = cards;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    @NonNull
    @Override
    public String toString() {
        return "DeckID"+this.deckId+"|"+"Title"+this.title;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Deck compareDeck = (Deck) obj;
        if(compareDeck.getDeckId().equals(this.getDeckId())) return true;
        return false;

    }
}

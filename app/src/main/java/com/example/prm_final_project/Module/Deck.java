package com.example.prm_final_project.Module;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deck implements Serializable {
    String title, author, Uid, deckId,date;
    boolean isPublic;
    List<List<String>> cards = new ArrayList<>();

    public Deck(){

    }

    public Deck (String deckId, String Uid, String title, String author,String date ,boolean isPublic ,List<List<String>> cards){
        this.deckId = deckId;
        this.title = title;
        this.author = author;
        this.cards = cards;
        this.Uid = Uid;
        this.isPublic =isPublic;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}

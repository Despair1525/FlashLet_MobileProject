package com.example.prm_final_project.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Deck implements Serializable {
    String title, author, Uid, deckId,date;
    boolean isPublic;
    int view;
    List<List<String>> cards = new ArrayList<>();

    public Deck(){

    }

    public Deck (String deckId, String Uid, String title, String author,String date ,boolean isPublic ,int view,List<List<String>> cards){
        this.deckId = deckId;
        this.title = title;
        this.author = author;
        this.cards = cards;
        this.Uid = Uid;
        this.isPublic =isPublic;
        this.date = date;
        this.view = view;
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

    public void setAuthor(String author) {
        this.author = author;
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

}

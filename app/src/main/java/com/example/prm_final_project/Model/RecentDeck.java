package com.example.prm_final_project.Model;

public class RecentDeck {

    private String DeckId;
    private Long TimeStamp;

    public RecentDeck() {
    }

    public RecentDeck( String deckId, Long timeStamp) {


        DeckId = deckId;
        TimeStamp = timeStamp;
    }


    public String getDeckId() {
        return DeckId;
    }

    public void setDeckId(String deckId) {
        DeckId = deckId;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }
}

package com.example.prm_final_project.Model;

public class RecentDeck {
    private String id;
    private String UserId;
    private String DeckId;
    private Long TimeStamp;

    public RecentDeck() {
    }

    public RecentDeck(String id, String userId, String deckId, Long timeStamp) {
        this.id = id;
        UserId = userId;
        DeckId = deckId;
        TimeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
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

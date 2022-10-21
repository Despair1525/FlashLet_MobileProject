package com.example.prm_final_project.Model;

public class Rate  {
    private String userId;
    private String Id;
    private int Score;

    public Rate(String userId, String id, int score) {
        this.userId = userId;
        Id = id;
        Score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}
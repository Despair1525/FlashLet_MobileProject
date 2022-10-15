package com.example.prm_final_project.Model;

public class Quiz {
    private String Question;
    private String Answer;

    public Quiz(String question, String answer) {
        Question = question;
        Answer = answer;
    }

    public Quiz() {
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "Question='" + Question + '\'' +
                ", Answer='" + Answer + '\'' +
                '}';
    }
}

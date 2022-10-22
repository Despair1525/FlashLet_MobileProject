package com.example.prm_final_project.Util;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import android.util.Log;

import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.Rate;
import com.example.prm_final_project.Model.User;

import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author smileymask
 */
public class recomendSystem {
    private  HashMap<Deck, Map<Deck, Double>> diff = new HashMap<>();
    private  HashMap<Deck, Map<Deck, Integer>> freq = new HashMap<>();
    private  HashMap<String, HashMap<Deck, Double>> inputData;

    public   HashMap<String, HashMap<Deck, Double>> outputData = new HashMap<>();

    private ArrayList<Deck> deckList;
    private ArrayList<User> userList;

    public HashMap<String, HashMap<Deck, Double>> getOutputData() {
        return outputData;
    }

    public void setOutputData(HashMap<String, HashMap<Deck, Double>> outputData) {
        this.outputData = outputData;
    }

    public void slopeOne(ArrayList<Deck> deckList ,ArrayList<User> userList ) {

        this.deckList = deckList;
        this.userList = userList;

        inputData = initializeData();
        buildDifferencesMatrix(inputData);
        predict(inputData);
    }



    public HashMap<String, HashMap<Deck, Double>> initializeData() {
        HashMap<String, HashMap<Deck, Double>> data = new HashMap<>();
        HashMap<Deck, Double> newUser;
        Set<Deck> newRecommendationSet;

        for (User user : userList) {
            newUser = new HashMap<>();
            newRecommendationSet = new HashSet<>();

            // Add all or  all sub random
//            while(!(newRecommendationSet.size() == 10)) {
//                Random random = new Random();
//                int number = random.nextInt(songList.size());
//                newRecommendationSet.add(songList.get(number));
//            }

            newRecommendationSet.addAll(deckList) ;
            for (Deck deck : newRecommendationSet) {

                // Random Value
               double score =  Math.random() ;

               if(user.getRate().containsKey(deck.getDeckId())) {
                   score = user.getRate().get(deck.getDeckId());
               };
                newUser.put(deck, score);
            }
            data.put(user.getUserId(), newUser);
        }
        return data;
    }

    private  void buildDifferencesMatrix(HashMap<String, HashMap<Deck, Double>> data) {

        for (HashMap<Deck, Double> user : data.values()) {

            for (Entry<Deck, Double> e : user.entrySet()) { //e List song User Rate
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<Deck, Double>());
                    freq.put(e.getKey(), new HashMap<Deck, Integer>());
                }
                for (Entry<Deck, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey());
                    }

                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey());
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (Deck j : diff.keySet()) {
            for (Deck i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i);
                int count = freq.get(j).get(i);
                diff.get(j).put(i, oldValue/count);
            }
        }
//        printData(data);
    }

    private  void predict(HashMap<String, HashMap<Deck, Double>> data) {
        HashMap<Deck, Double> uPred = new HashMap<>();
        HashMap<Deck, Integer> uFreq = new HashMap<>();

        for (Deck j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (Entry<String, HashMap<Deck, Double>> e : data.entrySet()) {
            for (Deck j : e.getValue().keySet()) {
                for (Deck k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j).doubleValue() + e.getValue().get(j).doubleValue();
                        double finalValue = predictedValue * freq.get(k).get(j).intValue();
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
                    } catch (NullPointerException e1) {
                    }
                }
            }
            HashMap<Deck, Double> clean = new HashMap<Deck, Double>();
            for (Deck j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j) / uFreq.get(j));
                }
            }
            for (Deck j : deckList) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else if (!clean.containsKey(j)) {
                    clean.put(j, -1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }
//        printData(outputData);
    }

    public  void printData() {
        for (String user : outputData.keySet()) {
            Log.i("Reco",user + ":");
            print(outputData.get(user));
        }
    }

    private  void print(HashMap<Deck, Double> hashMap) {
        for (Deck j : hashMap.keySet()) {
           Log.i("Reco"," " + j.getTitle() + " --> " + hashMap.get(j));
        }
    }




}

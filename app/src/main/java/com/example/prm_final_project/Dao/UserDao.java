package com.example.prm_final_project.Dao;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Activity.LoginActivity;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.callbackInterface.UserCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;

public class UserDao {

    public static FirebaseDatabase data = FirebaseDatabase.getInstance();
    public static DatabaseReference ref;
    public static ArrayList<User> allUsers = new ArrayList<>();
    public static Hashtable<String, User> allUserHT = new Hashtable<>();

    public static FirebaseUser getUser() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            return null;
        }
        return user;
    }

    public static User getCurrentUser() {
        return allUserHT.get(getUser().getUid());
    }

    public static void addUser(User user) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("avatar").setValue(user.getAvatar());
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("email").setValue(user.getEmail());
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("phone").setValue(user.getPhone());
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("userId").setValue(user.getUserId());
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("username").setValue(user.getUsername());
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("longestStreak").setValue(user.getLongestStreak());
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("currentStreak").setValue(user.getCurrentStreak());


        if (user.getRate() != null) {
            for (String key : user.getRate().keySet()) {
                Double score = user.getRate().get(key);
                FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("rate").child(key).setValue(score);
            }
            ;
        }
        ;
        if (user.getRecentDecks() != null) {
            for (RecentDeck deck : user.getRecentDecks()) {
                String key = deck.getDeckId();
                Long value = deck.getTimeStamp();
                FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).
                        child("recentDecks").child(key).setValue(value);
            }
            ;
        }

        if (user.getMyDeck() != null) {
            for (RecentDeck deck : user.getMyDeck()) {
                String key = deck.getDeckId();
                Long value = deck.getTimeStamp();
                FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).
                        child("myDeck").child(key).setValue(value);
            }
            ;
        }
        ;
        if (user.getFavoriteDeck() != null) {
            for (RecentDeck deck : user.getFavoriteDeck()) {
                String key = deck.getDeckId();
                Long value = deck.getTimeStamp();
                FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).
                        child("favoriteDeck").child(key).setValue(value);
            }
            ;
        }
        ;
    }

    ;

    public static User getSnapshotUser(DataSnapshot snapshot) {
        User newUser = new User();
        newUser.setUserId(snapshot.child("userId").getValue(String.class));
        newUser.setUsername(snapshot.child("username").getValue(String.class));
        newUser.setAvatar(snapshot.child("avatar").getValue(String.class));
        newUser.setPhone(snapshot.child("phone").getValue(String.class));
        newUser.setEmail(snapshot.child("email").getValue(String.class));



        if(snapshot.child("longestStreak").getValue(Long.class) != null) newUser.setLongestStreak(snapshot.child("longestStreak").getValue(Integer.class));
        if(snapshot.child("currentStreak").getValue(Long.class) != null) newUser.setCurrentStreak(snapshot.child("currentStreak").getValue(Integer.class));

        Log.i("main-date-firebase",newUser.getLongestStreak()+"");

        // setMyDeck;
        ArrayList<RecentDeck> recentDecks = new ArrayList<>();
        ArrayList<RecentDeck> myDeck = new ArrayList<>();
        ArrayList<RecentDeck> favoriteDeck = new ArrayList<>();
        ArrayList<String> daily = new ArrayList<>();

        for (DataSnapshot ds : snapshot.child("recentDecks").getChildren()) {
            String deckId = ds.getKey();
            Long timeStamp = ds.getValue(Long.class);
            RecentDeck newRecent = new RecentDeck(deckId, timeStamp);
            recentDecks.add(newRecent);
        }
        ;
        newUser.setRecentDecks(recentDecks);

        for (DataSnapshot ds : snapshot.child("myDeck").getChildren()) {
            String deckId = ds.getKey();
            Long timeStamp = ds.getValue(Long.class);
            RecentDeck newRecent = new RecentDeck(deckId, timeStamp);
            myDeck.add(newRecent);
        }
        ;
        newUser.setMyDeck(myDeck);

        // set favorite Deck
        for (DataSnapshot ds : snapshot.child("favoriteDeck").getChildren()) {
            String deckId = ds.getKey();
            Long timeStamp = ds.getValue(Long.class);
            RecentDeck newRecent = new RecentDeck(deckId, timeStamp);
            favoriteDeck.add(newRecent);
        }
        ;
        newUser.setFavoriteDeck(favoriteDeck);
        // set daily
        for (DataSnapshot ds : snapshot.child("daily").getChildren()) {
            String dailyDate = ds.getKey();
            daily.add(dailyDate);
        }
        ;
        newUser.setDaily(daily);

        // set Rate User;
        Hashtable<String, Double> userRate = new Hashtable<String, Double>();
        for (DataSnapshot ds : snapshot.child("rate").getChildren()) {
            userRate.put(ds.getKey(), ds.getValue(Double.class));
            Log.i("addUser", ds.getKey() + "|" + ds.getValue() + "");
        }
        ;
        newUser.setRate(userRate);
        return newUser;
    }


    public static void logout() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }
    public static void readAllUserFirst(UserCallback callback) {
        data.getReference("Users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("USERDAO_childAdded", task.getException() + "-erre");
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User u = getSnapshotUser(ds);

                        if (u.getUserId() != null) {
                            allUserHT.put(u.getUserId(), u);
                            allUsers.add(u);
                            Log.d("USERDAO_childAdded", "id: " + u.getUserId() + "-" + u.getUsername());
                        }

                    }
                    callback.onResponse(null, null, 0);
                    ;
                } else {
                    callback.onResponse(null, null, 1);
                }
                ;
            }
        });

        ref = data.getReference("Users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u = getSnapshotUser(snapshot);
                if (u.getUserId() != null) {
                    allUserHT.put(u.getUserId(), u);
                    allUsers.add(u);
                }
                ;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u = getSnapshotUser(snapshot);
                allUserHT.put(u.getUserId(), u);

                Log.d("USERDAO_childChanged", "id " + u.getUsername());
                Log.d("USERDAO_childChanged", "RecendDeck- " + u.getRecentDecks().size() + "");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User u = getSnapshotUser(snapshot);
                allUserHT.remove(u.getUserId());

                Log.d("USERDAO_Remoce", "id " + u.getUsername());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("USERDAO_childMoved", "username " + snapshot.getValue(User.class).getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("USERDAO_cancelled", "error " + error.getMessage());
            }
        });
    }

    public static ArrayList<User> searchByUserName(ArrayList<User> allUsers, String keyword) {
        ArrayList<User> results = new ArrayList<>();
        User u;
        for (int i = 0; i < allUsers.size(); i++) {
            u = allUsers.get(i);
            if (u.getUsername() != null) {
                if (u.getUsername().toLowerCase().contains(keyword.toLowerCase())) {
                    results.add(u);
                }
            }
        }

        return results;
    }

    public static ArrayList<Deck> getDeckByUser(ArrayList<Deck> decks, String userId) {
        ArrayList<Deck> results = new ArrayList<>();
        for (int i = 0; i < decks.size(); i++) {
            if (decks.get(i).getUid().equals(userId)) {
                results.add(decks.get(i));
            }
        }
        return results;
    }

    public static User readUserById(String id) {
        for (User u : allUsers) {
            if (u.getUserId().equals(id)) return u;

        }
        ;
        return null;
    }

    public static void addFavoriteDeck(String deckId) {
        User currentUser = allUserHT.get(UserDao.getUser().getUid());
        if (currentUser != null) {
            RecentDeck newRecent = new RecentDeck(deckId, Methods.getTimeLong());
            currentUser.getFavoriteDeck().add(newRecent);
            addUser(currentUser);
        }
    }

    public static void addDaily(String userId) {

        String currentDate = Methods.getDate();
        Log.i("date-firebase",currentDate);
        FirebaseDatabase.getInstance().getReference("Users").
                child(userId).child("daily").child(currentDate).
                setValue("");


    }
}

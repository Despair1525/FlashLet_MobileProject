package com.example.prm_final_project.Dao;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
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
    public static Hashtable<String,User> allUserHT = new Hashtable<>();

    public static ChildEventListener childEventListener;

    public static FirebaseUser getUser(){
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user==null){
            return null;
        }
        return user;
    }

    public static void addUser(User user) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("avatar").setValue(user.getAvatar());
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("email").setValue(user.getEmail());
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("phone").setValue(user.getPhone());
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("userId").setValue(user.getUserId());
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("username").setValue(user.getUsername());

        if(user.getRate() != null ) {
            for (String key : user.getRate().keySet()) {
                Double score = user.getRate().get(key);
                FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).child("rate").child(key).setValue(score);
            }
            ;
        };
        if(user.getRecentDecks() != null) {
            for(RecentDeck deck : user.getRecentDecks()) {
                String key = deck.getDeckId();
                Long value = deck.getTimeStamp();
                FirebaseDatabase.getInstance().getReference("Users").child(user.getUserId()).
                        child("recentDecks").child(key).setValue(value);
            };

        };
    };

    public static void logout(){
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }



    public static User getSnapshotUser(DataSnapshot snapshot) {
        User newUser = new User();
        newUser.setUserId(snapshot.child("userId").getValue(String.class));
        newUser.setUsername(snapshot.child("username").getValue(String.class));
        newUser.setAvatar(snapshot.child("avatar").getValue(String.class));
        newUser.setPhone(snapshot.child("phone").getValue(String.class));
        newUser.setEmail(snapshot.child("email").getValue(String.class));
        // setMyDeck;
        ArrayList<String> userDeck = new ArrayList<>();
        for(DataSnapshot ds : snapshot.child("myDeck").getChildren()) {
            userDeck.add(ds.getValue(String.class));
        };

        ArrayList<RecentDeck> recentDecks = new ArrayList<>();
        for(DataSnapshot ds : snapshot.child("recentDecks").getChildren()) {
            String deckId = ds.getKey();
            Long timeStamp = ds.getValue(Long.class);
            RecentDeck newRecent = new RecentDeck(deckId,timeStamp);
            recentDecks.add(newRecent);
        };
        newUser.setRecentDecks(recentDecks);

        newUser.setMyDeck(userDeck);
        // set Rate User;
        Hashtable<String,Double> userRate = new Hashtable<String, Double>();
        for(DataSnapshot ds : snapshot.child("rate").getChildren()) {
            userRate.put(ds.getKey(),ds.getValue(Double.class));
            Log.i("addUser",ds.getKey()+"|"+ds.getValue()+"");
        };
        newUser.setRate(userRate);
        return newUser;
    };

    public static void readAllUsers(ArrayList<User> allUsersList){
        allUsers = new ArrayList<>();
        ref = data.getReference("Users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                User u = snapshot.getValue(User.class);
                User u = getSnapshotUser(snapshot);
                allUsersList.add(u);
                allUsers.add(u);
                Log.d("USERDAO_childAdded", "id " + u.getUsername());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u = getSnapshotUser(snapshot);
                int index = getIndexByKey( allUsersList, snapshot);
                if(index != -1) {
                    allUsersList.set(index, u);
                    allUsers.set(index,u);
                }

                Log.d("USERDAO_childChanged", "username " + u.getUsername());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = getIndexByKey(allUsers, snapshot);
                if(index != -1) {
                    allUsersList.remove(index);
                }

                Log.d("USERDAO_childRemoved", "username " + snapshot.getValue(User.class).getUsername());
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

    public static void readAllUserFirst(){
        ref = data.getReference("Users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u = getSnapshotUser(snapshot);
                allUserHT.put(u.getUserId(),u);
                Log.d("USERDAO_childAdded", "id " + u.getUsername());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u = getSnapshotUser(snapshot);
                allUserHT.put(u.getUserId(),u);
                Log.d("USERDAO_childChanged", "id " + u.getUsername());
                Log.d("USERDAO_childChanged", "RecendDeck- " + u.getRecentDecks().size()+"");
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


    public static void readUsersByName(ArrayList<User> allUsers){
        ref = data.getReference("Users");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u = snapshot.getValue(User.class);
                allUsers.add(u);
                Log.d("USERDAO_childAdded", "id " + u.getUsername());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u = snapshot.getValue(User.class);
                int index = getIndexByKey(allUsers, snapshot);
                if(index != -1) {
                    allUsers.set(index, u);
                }

                Log.d("USERDAO_childChanged", "username " + u.getUsername());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = getIndexByKey(allUsers, snapshot);
                if(index != -1) {
                    allUsers.remove(index);
                }

                Log.d("USERDAO_childRemoved", "username " + snapshot.getValue(User.class).getUsername());
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
    public static int getIndexByKey(ArrayList<User> allUsers, DataSnapshot ds){
        if(ds == null){
            return -1;
        }
        User u = getSnapshotUser(ds);
        for (int i = 0; i < allUsers.size(); i++) {
            if(u.getUserId().equals(allUsers.get(i).getUserId())){
                return i;
            }
        }
        return -1;
    }

    public static ArrayList<User> searchByUserName(ArrayList<User> allUsers, String keyword){
        ArrayList<User> results = new ArrayList<>();
        User u;
        for (int i = 0; i < allUsers.size(); i++) {
            u = allUsers.get(i);
            if(u.getUsername() != null){
                if(u.getUsername().toLowerCase().contains(keyword.toLowerCase())) {
                    results.add(u);
                }
            }
        }

        return results;
    }

    public static ArrayList<Deck> getDeckByUser (ArrayList<Deck> decks, String userId){
        ArrayList<Deck> results = new ArrayList<>();
        for (int i = 0; i < decks.size(); i++) {
            if(decks.get(i).getUid().equals(userId)){
                results.add(decks.get(i));
            }
        }
        return results;
    }

    public static User readUserById(String id) {
        for(User u : allUsers) {
            if(u.getUserId().equals(id) ) return u;

        };
        return  null;
    }
    

}

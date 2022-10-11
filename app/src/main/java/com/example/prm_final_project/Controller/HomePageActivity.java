package com.example.prm_final_project.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.ProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Deck> allDecks = new ArrayList<>();
    ArrayList<String> myDeckKeys = new ArrayList<>();
    Map<String,Deck> keyedDecks = new HashMap<>();
    ArrayList<Deck> personalDecks = new ArrayList<>();
    private ListView lvDecks;
    private boolean isGuest = true;
    private boolean inPublic = true;
    HomeDeckListAdapter homeDeckAdap;

    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;
    ProgressDialog loading;
    private ImageView addDeck;
    private RecyclerView RcListDeck, RcListDeckLike, RcListDeckReco;
    private TextView myDecks, publicDecks, logout;
    private SearchView svDecks;
    private String m_Text = "";

//
//public Deck findDeck(String id ) {
//
//
//}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);


/////// Connect with Database
//      Lấy dữ liệu ra ở đây
        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Authentication
        isGuest = checkGuest();
//  Read Databae
        readAllDecks(rootRef.getReference("Decks"), new MainActivity.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onFailure() {
                Log.i("HomeActivity_readData:","fail ");
            }
        });
//        if ((ArrayList<Deck>) getIntent().getSerializableExtra("allDecks") != null){
//            allDecks = (ArrayList<Deck>) getIntent().getSerializableExtra("allDecks");
//        }

/////////////////////////////
        svDecks = findViewById(R.id.svSearchPublic);
        logout =  findViewById(R.id.tvLogout);
        addDeck = findViewById(R.id.abPlusPublic);
        RcListDeck = findViewById(R.id.RvDecksPublic);
        RcListDeckLike = findViewById(R.id.RvDecksPublicPopular);
        RcListDeckReco = findViewById(R.id.RvDecksPublicReco);
        addDeck.setOnClickListener(this);
        logout.setOnClickListener(this);

/////////////////////// Set style For ListView (Adjust )
        homeDeckAdap = new HomeDeckListAdapter(this,allDecks);
        RcListDeck.setAdapter(homeDeckAdap);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(HomePageActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(HomePageActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManagaer3 = new LinearLayoutManager(HomePageActivity.this, LinearLayoutManager.HORIZONTAL, false);
        RcListDeck.setLayoutManager(horizontalLayoutManagaer);

        //// Set for list Liked
        RcListDeckLike.setAdapter(homeDeckAdap);
        RcListDeckLike.setLayoutManager(horizontalLayoutManagaer2);
        /// Set for list Recomend
        RcListDeckReco.setAdapter(homeDeckAdap);
        RcListDeckReco.setLayoutManager(horizontalLayoutManagaer3);
//         Click vao 1 deck bat ky

    }

    public void readAllDecks(DatabaseReference rr, final MainActivity.OnGetDataListener listener){
        loading = new ProgressDialog(HomePageActivity.this);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String previousChildName) {
                loading.show();
                String uid = ds.child("uid").getValue(String.class);
                String author = ds.child("author").getValue(String.class);
                String title = ds.child("title").getValue(String.class);
                List<List<String>> cards = (List<List<String>>) ds.child("cards").getValue();
                String did = ds.child("deckid").getValue(String.class);
                String date = ds.child("date").getValue(String.class);
                boolean isPublic = ds.child("public").getValue(Boolean.class);
                Deck thisDeck = new Deck(did, uid, title, author,date,isPublic ,cards);
                allDecks.add(thisDeck);
                homeDeckAdap.notifyDataSetChanged();
//                DeckListAdapter.notifyDataSetChanged();

                loading.dismiss();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        rr.addChildEventListener(childEventListener);
    }


    public boolean checkGuest(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user==null){
            return true;
        }
        Toast.makeText(HomePageActivity.this, "Hello"+user.getDisplayName(), Toast.LENGTH_SHORT).show();
        return false;
    }


    public void logout(){
        mAuth.signOut();
        Intent i = new Intent(HomePageActivity.this, LoginActivity.class);
        i.putExtra("allDecks", allDecks);
        startActivity(i);
    }
    @Override
    public void onClick(View view) {
        if(view == logout) {
            logout();
        }
        if(view == addDeck) {
            if(isGuest) {
                Toast.makeText(HomePageActivity.this,"You have to login !",Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Title");
// Set up the input
                final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);
// Set up the buttons
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Intent i =  new Intent(HomePageActivity.this,EditDeckActivity.class);
                        i.putExtra("editTitle",m_Text);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            };

        };

    }


}
package com.example.prm_final_project.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import android.app.ProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
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
        setContentView(R.layout.activity_homepage);


/////// Connect with Database
//      Lấy dữ liệu ra ở đây
        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Authentication
        isGuest = checkGuest();
        DeckDao.readAllDecks(new FirebaseCallback() {
            @Override
            public void onResponse(ArrayList<Deck> allDecks) {
                homeDeckAdap.notifyDataSetChanged();
            }
        },allDecks);

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
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);
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
package com.example.prm_final_project.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Adapter.DeckListAdapter;
import com.example.prm_final_project.Adapter.MyDeckListAdapter;
import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Deck> allDecks = new ArrayList<>();
    ArrayList<String> myDeckKeys = new ArrayList<>();
    Map<String,Deck> keyedDecks = new HashMap<>();
    ArrayList<Deck> personalDecks = new ArrayList<>();
    private ListView lvDecks;
    private boolean isGuest = true;
    private boolean inPublic = true;

    FirebaseDatabase rootRef;
    FirebaseAuth mAuth;

    private ImageView addDeck;
    private TextView myDecks, publicDecks, logout;
    private SearchView svDecks;
    private String m_Text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_decks);


/////// Connect with Database
//      Lấy dữ liệu ra ở đây
        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Authentication
        isGuest = checkGuest();

        if ((ArrayList<Deck>) getIntent().getSerializableExtra("allDecks") != null){
            allDecks = (ArrayList<Deck>) getIntent().getSerializableExtra("allDecks");
        }
        Log.i("HMData",allDecks.size()+"");

/////////////////////////////
        svDecks = findViewById(R.id.svSearchPublic);
        myDecks =  findViewById(R.id.tvMyDecks);
        publicDecks =  findViewById(R.id.tvPublicDecks);
        logout =  findViewById(R.id.tvLogout);
        addDeck = findViewById(R.id.abPlusPublic);
        lvDecks = findViewById(R.id.lvDecksPublic);
        addDeck.setOnClickListener(this);
        logout.setOnClickListener(this);

/////////////////////// Set style For ListView (Adjust )
        if (inPublic){
            DeckListAdapter adapter = new DeckListAdapter(isGuest,this, R.layout.deck_lv_item, allDecks);
            lvDecks.setAdapter(adapter);
        }
        else {
            MyDeckListAdapter adapter = new MyDeckListAdapter(isGuest,this, R.layout.deck_lv_item, personalDecks);
            publicDecks.setTextAppearance(this, android.R.style.TextAppearance_Material_Body1);
            myDecks.setTextAppearance(this, android.R.style.TextAppearance_Large);
            lvDecks.setAdapter(adapter);
        };


//         Click vao 1 deck bat ky
        lvDecks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(HomePageActivity.this, ViewCardActivity.class);
                i.putExtra("viewDeck", allDecks.get(position));
                startActivity(i);
            }
        });

    }


    public boolean checkGuest(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user==null){
            return true;
        }
        Toast.makeText(HomePageActivity.this, "Hello"+user.getEmail(), Toast.LENGTH_SHORT).show();
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
                        i.putExtra("editDeck",allDecks.get(1));
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
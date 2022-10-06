package com.example.prm_final_project.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.prm_final_project.Adapter.DeckListAdapter;
import com.example.prm_final_project.Adapter.EditCardAdapter;
import com.example.prm_final_project.Adapter.cardViewAdapter1;
import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;

import java.util.ArrayList;
import java.util.List;

public class EditDeckActivity extends AppCompatActivity {
private String title;
private EditText EdTitle;
private Deck editDeck;
private RecyclerView   recyclerViewList;
private ImageButton IbAdd;
    EditCardAdapter dla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deck);
        title = getIntent().getStringExtra("editTitle");
        if ((Deck) getIntent().getSerializableExtra("editDeck") != null){
           editDeck = (Deck) getIntent().getSerializableExtra("editDeck");
        }else{
            editDeck = new Deck();
        };
        // Set UI
        IbAdd = findViewById(R.id.imageButtonAdd);
        EdTitle = findViewById(R.id.editTitle);
        EdTitle.setText(title);
        // Set RV
        recyclerViewList = findViewById(R.id.rcListEdit);
        dla = new EditCardAdapter(this,editDeck);
        recyclerViewList.setAdapter( dla);
        recyclerViewList.setLayoutManager(new LinearLayoutManager((this)));
        IbAdd.setOnClickListener(view -> onAdd());
    }

    private void onAdd(){
        List<String> newCard = new ArrayList<>();
        newCard.add("");
        newCard.add("");
      editDeck.getCards().add(newCard);
      dla.notifyDataSetChanged();
      }
    };



package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.prm_final_project.Adapter.cardViewAdapter1;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class EditDeckActivity extends AppCompatActivity {
private String title;
private EditText EdTitle;
private Deck editDeck;
private RecyclerView   recyclerViewList;
private ImageButton IbAdd,IbSave;
public cardViewAdapter1 cardViewAdapter;
private SwitchCompat isPublic;

private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deck);
        title = getIntent().getStringExtra("editTitle");
        if ((Deck) getIntent().getSerializableExtra("editDeck") != null){
           editDeck = (Deck) getIntent().getSerializableExtra("editDeck");
           title= editDeck.getTitle();
        }else{
            editDeck = new Deck();
        };
        // Set UI
        IbAdd = findViewById(R.id.imageButtonAdd);
        IbSave = findViewById(R.id.ImgaeButtonSaveEdit);
        EdTitle = findViewById(R.id.editTitle);
        EdTitle.setText(title);
        isPublic = findViewById(R.id.SwitchCompatIsPublic);
        // Set RV
        recyclerViewList = findViewById(R.id.rcListEdit);
        cardViewAdapter = new cardViewAdapter1(this,editDeck);
        recyclerViewList.setAdapter( cardViewAdapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager((this)));
        IbAdd.setOnClickListener(view -> onAdd());
        IbSave.setOnClickListener(view -> onSave());
    }

    private void onSave() {
        dialog = new ProgressDialog(EditDeckActivity.this);
        dialog.setMessage("Loading");
        dialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you want to save Flashcard ?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user = UserDao.getUser();
                String date = Methods.getTime();
                String deckid = Methods.generateFlashCardId();
                List<List<String>> cards = editDeck.getCards();
                String title = EdTitle.getText().toString();
                String uid = user.getUid();
                String author = user.getDisplayName();
                Boolean Public = isPublic.isChecked();
                int view =1;
//                Deck (String deckId, String Uid, String title, String author, boolean isPublic ,List<List<String>> cards)

                Deck newDeck = new Deck(deckid,uid,title,author,date,Public,view,cards);
                DeckDao.addDeck(newDeck, new FirebaseCallback() {
                    @Override
                    public void onResponse(ArrayList<Deck> allDecks, Deck changeDeck, int type) {
                        Intent i = new Intent(EditDeckActivity.this, ViewCardActivity.class);
                        i.putExtra("viewDeck", changeDeck);
                        dialog.dismiss();
                        startActivity(i);
                        Toast.makeText(EditDeckActivity.this, "Upload flashcard success !", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

//                Toast.makeText(EditDeckActivity.this, editDeck.getCards().size()+"|"+ Methods.generateFlashCardId()
//                        +"|"+Methods.getTime()+"|"+userName+"|"+Public, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void onAdd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a card");
        // Set up the input
        final EditText inputFront = new EditText(this);
        inputFront.setHint("Enter Front Card");
        final EditText inputBack = new EditText(this);
        inputBack.setHint("Enter Back Card");
        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.HORIZONTAL);
        lay.addView(inputFront);
        lay.addView(inputBack);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        inputFront.setInputType(InputType.TYPE_CLASS_TEXT );
        inputBack.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(lay);
// Set up the buttons
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String front;
                String back;
                front = inputFront.getText().toString();
                back = inputBack.getText().toString();
                List<String> newCard = new ArrayList<>();
                newCard.add(front);
                newCard.add(back);
                editDeck.getCards().add(newCard);
                cardViewAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
      }

    public Deck getEditDeck() {
        return editDeck;
    }

    public void setEditDeck(Deck editDeck) {
        this.editDeck = editDeck;
    }

    public cardViewAdapter1 getCardViewAdapter() {
        return cardViewAdapter;
    }

    public void setCardViewAdapter(cardViewAdapter1 cardViewAdapter) {
        this.cardViewAdapter = cardViewAdapter;
    }


//    private void addDeck(Deck deck, FirebaseCallback callback){
//        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).setValue(deck);
//                .setValue(deck).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete( @NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//
//                            Intent i = new Intent(EditDeckActivity.this, ViewCardActivity.class);
//                            i.putExtra("viewDeck", deck);
//                            startActivity(i);
//                            Toast.makeText(EditDeckActivity.this, "Upload flashcard success !", Toast.LENGTH_LONG).show();
//                            finish();
//                        }
//                        else{
//                            Toast.makeText(EditDeckActivity.this, "Failed to upload new deck.", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });

//    };

};



package com.example.prm_final_project.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Adapter.EditCardAdapt;
import com.example.prm_final_project.Adapter.cardViewAdapter1;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.FavoriteDeck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.Util.Regex;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class EditDeckActivity extends AppCompatActivity implements View.OnClickListener {
private String title;
private TextView tvImport;
private EditText EdTitle, EdDes;
private Deck editDeck;
private RecyclerView   recyclerViewList;
private ImageButton IbAdd;
public EditCardAdapt cardViewAdapter;
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

            FirebaseUser user = UserDao.getUser();
            String date = Methods.getTime();
            String deckid = Methods.generateFlashCardId();
            List<List<String>> cards = new ArrayList<>();
            String description = "";
            String uid = user.getUid();
            Boolean Public = true;
            int view = 1;
            editDeck = new Deck(deckid,uid,title,description,date,Public,view,cards);

        };
        // Set UI
        IbAdd = findViewById(R.id.imageButtonAdd);
        EdTitle = findViewById(R.id.editTitle);
        EdDes = findViewById(R.id.editDes);
        EdTitle.setText(title);
        isPublic = findViewById(R.id.SwitchCompatIsPublic);
        tvImport = findViewById(R.id.tvImport);

        isPublic.setChecked(editDeck.isPublic());
        EdDes.setText(editDeck.getDescriptions());

        // Set RV
        recyclerViewList = findViewById(R.id.rcListEdit);
        cardViewAdapter = new EditCardAdapt(this,editDeck);
        recyclerViewList.setAdapter( cardViewAdapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager((this)));

        IbAdd.setOnClickListener(view -> onAdd());
        tvImport.setOnClickListener(this);
        User editUser = UserDao.allUserHT.get(editDeck.getUid());
        String userName = editUser==null? "": (editUser.getUsername()+" ") ;
        getSupportActionBar().setTitle("Edit "+userName+ "Flashcard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void onSave() {

        if(editDeck.getCards().size() < 2){
            Toast.makeText(EditDeckActivity.this,"Number of cards must be more than 4 !",Toast.LENGTH_SHORT).show();
            return;
        };
        String title = EdTitle.getText().toString();
        if(title.trim().isEmpty()) {
            Toast.makeText(EditDeckActivity.this,"Title can not be empty !",Toast.LENGTH_SHORT).show();
            return;
        };
        dialog = new ProgressDialog(EditDeckActivity.this);
        dialog.setMessage("Loading");
        dialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you want to save Flashcard ?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseUser user = UserDao.getUser();
                editDeck.setTitle(Regex.textNormalization(EdTitle.getText().toString()));
                editDeck.setDescriptions(EdDes.getText().toString());
                editDeck.setPublic(isPublic.isChecked());

                if(editDeck.getDeckId() == null) editDeck.setDeckId(Methods.generateFlashCardId());

                DeckDao.addDeck(editDeck, new FirebaseCallback() {
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
                User curentUser = UserDao.getCurrentUser();

                String deckId = editDeck.getDeckId();
                ArrayList<RecentDeck> recentDecks = DeckDao.createRecentDeck(deckId,curentUser.getRecentDecks());
                curentUser.setRecentDecks(recentDecks);
                RecentDeck recentDeck= new RecentDeck(editDeck.getDeckId(),Methods.getTimeLong());
                curentUser.getMyDeck().add(recentDeck);
                UserDao.addUser(curentUser);
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

        // Set up Layout for dialog

        LayoutInflater inflater =EditDeckActivity.this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.item_edit_card,null);
        builder.setView(layout);
        EditText inputFront =layout.findViewById(R.id.Editfront);
        EditText inputBack =layout.findViewById(R.id.Editback);

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
                recyclerViewList.scrollToPosition( editDeck.getCards().size()-1);

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

    public EditCardAdapt getCardViewAdapter() {
        return cardViewAdapter;
    }

    public void setCardViewAdapter(EditCardAdapt cardViewAdapter) {
        this.cardViewAdapter = cardViewAdapter;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_edit_menu,menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 15) {
                Bundle args = data.getBundleExtra("BUNDLE");
                List<List<String>> importCard = (List<List<String>>) args.getSerializable("importCard");
//                Log.i("editdeckImport","Number of card"+importCard.size());
                editDeck.getCards().addAll(importCard);
                cardViewAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.SaveEditAction:
                onSave();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == tvImport) {
            Intent i = new Intent(this,ImportDataActivity.class);
//            i.putExtra("editDeck",editDeck);
            startActivityForResult(i, 15);
//            startActivity(i);
        };
    }
};



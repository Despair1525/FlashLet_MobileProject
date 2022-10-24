package com.example.prm_final_project.Ui.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Ui.Activity.ChangePasswordActivity;
import com.example.prm_final_project.Ui.Activity.EditProfileActivity;
import com.example.prm_final_project.Ui.Activity.LoginActivity;
import com.example.prm_final_project.Ui.Activity.MyFlashcardsActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imageAvatar;
    private TextView tvUsername, tvEmail, tvPhone;
    private Button btnEditProfile, btnMyFlashCards, btnChangePassword, btnNotification, btnLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        initUi(view);
        ArrayList<RecentDeck> decks = new ArrayList<>();
        decks.add(new RecentDeck("1665069398251",(long)1));
        decks.add(new RecentDeck("1665072589374",(long)2));
        decks.add(new RecentDeck("1665073056163",(long)3));

        //String deckId, String Uid, String title, String descriptions ,String author,String date ,boolean isPublic ,int view,List<List<String>> cards
//        decks.add(new Deck("1","1","asdf","asdf","ertsdf","123412",true,4, null));
//        decks.add(new Deck("1","1","asdf","asdf","ertsdf","123412",true,4, null));
//        decks.add(new Deck("1","1","asdf","asdf","ertsdf","123412",true,4, null));
//        decks.add(new Deck("1","1","asdf","asdf","ertsdf","123412",true,4, null));
//        decks.add(new Deck("1","1","asdf","asdf","ertsdf","123412",true,4, null));
        User user1 = new User("123", "Thanh", "https://t2.ea.ltmcdn.com/en/posts/4/0/9/10_things_you_should_know_about_golden_retrievers_904_600_square.jpg",
                "0123123123", "thanh@gmail.com", decks);
        Glide.with(this).load(user1.getAvatar()).error(R.drawable.default_avatar).into(imageAvatar);
        tvUsername.setText(user1.getUsername());
        tvEmail.setText(user1.getEmail());
        tvPhone.setText(user1.getPhone());
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        btnMyFlashCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyFlashcardsActivity.class);
                startActivity(intent);
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDao.logout();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        return view;
    }

    private void initUi(View view) {
        imageAvatar = view.findViewById(R.id.imageAvatar);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnMyFlashCards = view.findViewById(R.id.btnMyFlashCards);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnNotification = view.findViewById(R.id.btnNotification);
        btnLogout = view.findViewById(R.id.btnLogout);
    }
}
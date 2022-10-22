package com.example.prm_final_project.Ui.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prm_final_project.Adapter.UserListAdapter;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;

import java.util.ArrayList;

public class AllUserFragment extends Fragment {

    private Context thiscontext;
    private TextView textView;
    private LinearLayout recyclerLayout;
    private SearchResultViewModel viewModel;
    private ArrayList<User> allUsers;
    private boolean isSearch;

    public AllUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_user, container, false);
        init(view);

        viewModel = new ViewModelProvider(getParentFragment()).get(SearchResultViewModel.class);
        allUsers = viewModel.getAllUsers().getValue();
        isSearch = viewModel.getIsSearch().getValue();

        if(allUsers.size() > 0 && isSearch){
            textView.setVisibility(View.GONE);
            UserListAdapter userListAdapter = new UserListAdapter(thiscontext, allUsers);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_all_users);
            recyclerView.setAdapter(userListAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(thiscontext));
            recyclerLayout.setVisibility(View.VISIBLE);
        }
        else if(isSearch){
            textView.setText("No result found!");
        }

        return view;
    }

    public void init(View view){
        textView = view.findViewById(R.id.greeting_all_user);
        recyclerLayout = view.findViewById(R.id.layout_all_users);
    }
}
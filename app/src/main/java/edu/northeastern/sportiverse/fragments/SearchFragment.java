package edu.northeastern.sportiverse.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import edu.northeastern.sportiverse.adapters.SearchAdapter;
import edu.northeastern.sportiverse.databinding.FragmentSearchBinding;
import edu.northeastern.sportiverse.Models.User;
import edu.northeastern.sportiverse.Utils.Constants;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private SearchAdapter adapter;
    private ArrayList<User> userList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SearchAdapter(requireContext(), userList);
        binding.rv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection(Constants.USER_NODE).get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<User> tempList = new ArrayList<>();
            userList.clear();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                if (!document.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    User user = document.toObject(User.class);
                    if (user != null) { // Add null check for safety
                        tempList.add(user);
                    }
                }
            }
            userList.addAll(tempList);
            adapter.notifyDataSetChanged();
        });

        binding.searchButton.setOnClickListener(v -> {
            String text = binding.searchView.getText().toString();
            FirebaseFirestore.getInstance().collection(Constants.USER_NODE).whereEqualTo("name", text).get().addOnSuccessListener(queryDocumentSnapshots -> {
                ArrayList<User> tempList = new ArrayList<>();
                userList.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        if (!document.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            User user = document.toObject(User.class);
                            if (user != null) { // Add null check for safety
                                tempList.add(user);
                            }
                        }
                    }
                    userList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                }
            });
        });

        return binding.getRoot();
    }
}

package edu.northeastern.sportiverse.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

import edu.northeastern.sportiverse.Models.Reel;
import edu.northeastern.sportiverse.adapters.ReelAdapter;
import edu.northeastern.sportiverse.databinding.FragmentReelBinding;
import edu.northeastern.sportiverse.Utils.Constants;

public class ReelFragment extends Fragment {
    private FragmentReelBinding binding;
    private ReelAdapter adapter;
    private ArrayList<Reel> reelList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReelBinding.inflate(inflater, container, false);
        adapter = new ReelAdapter(requireContext(), reelList);
        binding.viewPager.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection(Constants.REEL).get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<Reel> tempList = new ArrayList<>();
            reelList.clear();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) { // Changed QueryDocumentSnapshot to DocumentSnapshot
                Reel reel = document.toObject(Reel.class);
                if (reel != null) {
                    tempList.add(reel);
                }
            }
            reelList.addAll(tempList);
            Collections.reverse(reelList);
            adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }
}

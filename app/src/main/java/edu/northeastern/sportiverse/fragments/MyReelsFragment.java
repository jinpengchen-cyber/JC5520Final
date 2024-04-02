package edu.northeastern.sportiverse.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import edu.northeastern.sportiverse.Models.Reel;
import edu.northeastern.sportiverse.adapters.MyReelAdapter;
import edu.northeastern.sportiverse.databinding.FragmentMyReelsBinding;
import edu.northeastern.sportiverse.Utils.Constants;

import java.util.ArrayList;

public class MyReelsFragment extends Fragment {

    private FragmentMyReelsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyReelsBinding.inflate(inflater, container, false);
        ArrayList<Reel> reelList = new ArrayList<>();
        MyReelAdapter adapter = new MyReelAdapter(requireContext(), reelList);
        binding.rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        binding.rv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + Constants.REEL).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Reel> tempList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Reel reel = documentSnapshot.toObject(Reel.class);
                        tempList.add(reel);
                    }
                    reelList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                });

        return binding.getRoot();
    }
}

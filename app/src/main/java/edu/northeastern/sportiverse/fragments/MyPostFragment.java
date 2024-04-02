package edu.northeastern.sportiverse.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import edu.northeastern.sportiverse.Models.Post;
import edu.northeastern.sportiverse.Utils.Constants;
import edu.northeastern.sportiverse.adapters.MyPostRvAdapter;
import edu.northeastern.sportiverse.databinding.FragmentMyPostBinding;

import java.util.ArrayList;

public class MyPostFragment extends Fragment {

    private FragmentMyPostBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyPostBinding.inflate(inflater, container, false);
        ArrayList<Post> postList = new ArrayList<>();
        MyPostRvAdapter adapter = new MyPostRvAdapter(requireContext(), postList);
        binding.rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        binding.rv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid()+ Constants.USER_POSTS).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("MyPostFragment", "Query executed successfully. Number of documents: " + queryDocumentSnapshots.size());
                    ArrayList<Post> tempList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        Log.d("PostLoad", "URL: " + post.getImage());
                        tempList.add(post);
                    }
                    postList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                });

        return binding.getRoot();
    }
}

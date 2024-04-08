package edu.northeastern.sportiverse.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import edu.northeastern.sportiverse.Models.Post;
import edu.northeastern.sportiverse.Utils.Constants;
import edu.northeastern.sportiverse.adapters.MyPostRvAdapter;
import edu.northeastern.sportiverse.databinding.FragmentMyPostBinding;
import java.util.ArrayList;
import edu.northeastern.sportiverse.EditPostActivity; // Ensure you have the correct import for your activity

public class MyPostFragment extends Fragment implements MyPostRvAdapter.OnPostClickListener {
    private FragmentMyPostBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyPostBinding.inflate(inflater, container, false);
        ArrayList<Post> postList = new ArrayList<>();
        MyPostRvAdapter adapter = new MyPostRvAdapter(requireContext(), postList, this);
        binding.rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        binding.rv.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + Constants.USER_POSTS).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Post> tempList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        post.setId(documentSnapshot.getId()); // Set the ID of the Post object
                        tempList.add(post);
                    }
                    postList.addAll(tempList);
                    adapter.notifyDataSetChanged();
                });


        return binding.getRoot();
    }

    @Override
    public void onPostClick(Post post) {
        if (post.getId() != null) {
            Log.d("MyPostFragment", "Post ID: " + post.getId());
            Intent intent = new Intent(getActivity(), EditPostActivity.class);
            intent.putExtra("postId", post.getId());
            startActivity(intent);
        } else {
            Log.e("MyPostFragment", "Error: Post ID is null.");
        }
    }

}


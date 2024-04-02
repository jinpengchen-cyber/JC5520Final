package edu.northeastern.sportiverse.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import edu.northeastern.sportiverse.Models.Post;
import edu.northeastern.sportiverse.Models.User;
import edu.northeastern.sportiverse.R;
import edu.northeastern.sportiverse.adapters.FollowAdapter;
import edu.northeastern.sportiverse.adapters.PostAdapter;
import edu.northeastern.sportiverse.databinding.FragmentHomeBinding;
import static edu.northeastern.sportiverse.Utils.Constants.FOLLOW;
import static edu.northeastern.sportiverse.Utils.Constants.POST;



import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ArrayList<Post> postList = new ArrayList<>();
    private PostAdapter adapter;
    private ArrayList<User> followList = new ArrayList<>();
    private FollowAdapter followAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        adapter = new PostAdapter(requireContext(), postList);
        binding.postRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.postRv.setAdapter(adapter);

        followAdapter = new FollowAdapter(requireContext(), followList);
        binding.followRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.followRv.setAdapter(followAdapter);

        setHasOptionsMenu(true);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.materialToolbar2);

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + FOLLOW).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<User> tempList = new ArrayList<>();
                    followList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        User user = documentSnapshot.toObject(User.class);
                        tempList.add(user);
                    }
                    followList.addAll(tempList);
                    followAdapter.notifyDataSetChanged();
                });

        FirebaseFirestore.getInstance().collection(POST).get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<Post> tempList = new ArrayList<>();
            postList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Post post = documentSnapshot.toObject(Post.class);
                tempList.add(post);
            }
            postList.addAll(tempList);
            adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

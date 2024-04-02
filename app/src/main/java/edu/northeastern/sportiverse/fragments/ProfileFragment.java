package edu.northeastern.sportiverse.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.northeastern.sportiverse.LoginActivity;
import edu.northeastern.sportiverse.Models.User;
import edu.northeastern.sportiverse.R;
import edu.northeastern.sportiverse.SignUpActivity;
import edu.northeastern.sportiverse.adapters.ViewPagerAdapter;
import edu.northeastern.sportiverse.databinding.FragmentProfileBinding;
import edu.northeastern.sportiverse.Utils.Constants;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        binding.editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            intent.putExtra("MODE", 1);
            startActivity(intent);
            getActivity().finish();
        });

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragments(new MyPostFragment(), "My Post");
        viewPagerAdapter.addFragments(new MyReelsFragment(), "My Reels");
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get the current user from Firebase Auth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is signed in
        if (currentUser != null) {
            // User is signed in, proceed with loading the user's data
            FirebaseFirestore.getInstance().collection(Constants.USER_NODE).document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            binding.name.setText(user.getName());
                            binding.bio.setText(user.getEmail());
                            if (user.getImage() != null && !user.getImage().isEmpty()) {
                                Picasso.get().load(user.getImage()).into(binding.profileImage);
                            }
                        }
                    });
        } else {
            // No user is signed in, redirect to the sign-in activity
            goToSignInActivity();
        }
    }

    private void goToSignInActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        // If you want to clear the task stack and start fresh
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

}

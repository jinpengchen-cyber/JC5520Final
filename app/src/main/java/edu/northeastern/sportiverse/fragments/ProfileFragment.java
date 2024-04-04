package edu.northeastern.sportiverse.fragments;

import android.app.AlertDialog;
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
import edu.northeastern.sportiverse.activities.EditProfileActivity;
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        // Setup edit profile button
        binding.editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        // Setup sign out button
        binding.signOutButton.setOnClickListener(view -> confirmSignOut());

        // Setup ViewPager
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragments(new MyPostFragment(), "My Post");
        viewPagerAdapter.addFragments(new MyCoursesFragment(), "My Courses");
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                goToSignInActivity();
            } else {
                // User is signed in, proceed with loading the user's data
                FirebaseFirestore.getInstance().collection(Constants.USER_NODE).document(user.getUid()).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            User profileUser = documentSnapshot.toObject(User.class);
                            if (profileUser != null) {
                                binding.name.setText(profileUser.getName());
                                binding.bio.setText(profileUser.getEmail());
                                if (profileUser.getImage() != null && !profileUser.getImage().isEmpty()) {
                                    Picasso.get().load(profileUser.getImage()).into(binding.profileImage);
                                }
                                binding.editProfile.setOnClickListener(view -> {
                                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                                    intent.putExtra("userName", profileUser.getName());
                                    intent.putExtra("userEmail", profileUser.getEmail());
                                    startActivity(intent);
                                });

                            }
                        });
            }
        });
    }

    private void goToSignInActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void confirmSignOut() {
        new AlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Sign Out", (dialog, which) -> signOut())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        goToSignInActivity();
    }

    @Override
    public void onStop() {
        super.onStop();
        // FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        // If you were using an AuthStateListener field, you would remove it here.
    }
}

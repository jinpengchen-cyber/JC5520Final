package edu.northeastern.sportiverse.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import edu.northeastern.sportiverse.activities.PostActivity;
import edu.northeastern.sportiverse.activities.ReelsActivity;
import edu.northeastern.sportiverse.databinding.FragmentAddBinding;

public class AddFragment extends BottomSheetDialogFragment {

    private FragmentAddBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment with view binding
        binding = FragmentAddBinding.inflate(inflater, container, false);

        // Set up click listeners for your post and reel buttons
        binding.post.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().startActivity(new Intent(getContext(), PostActivity.class));
                getActivity().finish();
            }
        });

        binding.reel.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().startActivity(new Intent(getContext(), ReelsActivity.class));
            }
        });

        return binding.getRoot();
    }

    // Optionally, if you have any static factory methods or companion object functionality, convert them here.
}

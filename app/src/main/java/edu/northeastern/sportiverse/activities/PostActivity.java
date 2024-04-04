package edu.northeastern.sportiverse.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.northeastern.sportiverse.Models.Post;
import edu.northeastern.sportiverse.Utils.Constants; // Ensure this import matches your project structure
import edu.northeastern.sportiverse.databinding.ActivityPostBinding;
import edu.northeastern.sportiverse.HomeActivity;
import edu.northeastern.sportiverse.Utils.utils; // Assuming uploadImage is a method in Utils

public class PostActivity extends AppCompatActivity {
    private ActivityPostBinding binding;
    private String imageUrl = null;
    private final ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    utils.uploadImage(uri, Constants.POST_FOLDER, this, new utils.UploadCallback() {
                        @Override
                        public void onUploadComplete(String url) {
                            if (url != null) {
                                runOnUiThread(() -> {
                                    binding.selectImage.setImageURI(uri);
                                    imageUrl = url;
                                });
                            }
                        }
                    });
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.materialToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        binding.materialToolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(PostActivity.this, HomeActivity.class));
            finish();
        });

        binding.selectImage.setOnClickListener(view -> launcher.launch("image/*"));

        binding.cancelButton.setOnClickListener(view -> {
            startActivity(new Intent(PostActivity.this, HomeActivity.class));
            finish();
        });

        binding.postButton.setOnClickListener(view -> {
            Post post = new Post(
                    imageUrl,
                    binding.caption.getEditText().getText().toString(),
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    System.currentTimeMillis() // Directly set the timestamp here
            );

            FirebaseFirestore.getInstance().collection(Constants.POST).document().set(post).addOnSuccessListener(unused -> {
                FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + Constants.USER_POSTS).document()
                        .set(post)
                        .addOnSuccessListener(unused1 -> {
                            startActivity(new Intent(PostActivity.this, HomeActivity.class));
                            finish();
                        });
            });
        });
    }
}

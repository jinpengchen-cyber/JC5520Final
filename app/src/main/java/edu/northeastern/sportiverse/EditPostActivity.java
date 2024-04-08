package edu.northeastern.sportiverse;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class EditPostActivity extends AppCompatActivity {
    private ImageView postImageView;
    private TextView captionTextView;
    private Button deleteButton;
    private String postId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        postImageView = findViewById(R.id.imageViewPost);
        captionTextView = findViewById(R.id.textViewCaption);
        deleteButton = findViewById(R.id.buttonDelete);

        postId = getIntent().getStringExtra("postId");
        Log.d("EditPostActivity", "Received Post ID: " + postId);

        // Validate postId
        if (postId == null || postId.trim().isEmpty()) {
            Log.e("EditPostActivity", "Error: Post ID is null or empty.");
            Toast.makeText(this, "Error: Post cannot be found.", Toast.LENGTH_LONG).show();
            finish(); // Close the activity
            return;
        }
        Log.d("Current User ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        // Fetch and display the post details
        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()+ "USER_POSTS").document(postId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String imageUrl = documentSnapshot.getString("image");
                        String caption = documentSnapshot.getString("caption"); // Make sure this is the correct field name

                        // Set the image using Picasso
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get().load(imageUrl).into(postImageView);
                        } else {
                            // Handle case where there is no image URL
                        }

                        // Set the caption
                        captionTextView.setText(caption != null ? caption : "");
                    } else {
                        Toast.makeText(this, "Post does not exist.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading post.", Toast.LENGTH_LONG).show();
                    Log.e("EditPostActivity", "Error getting document", e);
                });

        deleteButton.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()+ "USER_POSTS")
                    .document(postId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error deleting post", Toast.LENGTH_LONG).show();
                        Log.e("EditPostActivity", "Error deleting post", e);
                    });
        });
    }
}

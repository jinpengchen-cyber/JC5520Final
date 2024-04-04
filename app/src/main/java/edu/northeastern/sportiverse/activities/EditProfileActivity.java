package edu.northeastern.sportiverse.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import edu.northeastern.sportiverse.Utils.utils;

import java.util.List;

import edu.northeastern.sportiverse.R;

public class EditProfileActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private Button saveProfileButton;

    private ImageView profileImageView;
    private Uri imageUri; // This will hold the selected image URI
    private static final int PICK_IMAGE_REQUEST = 1; // Request code for picking an image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImageView = findViewById(R.id.profileImageView);

        profileImageView.setOnClickListener(v -> openFileChooser());

        // Initialize EditText fields
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        saveProfileButton = findViewById(R.id.saveProfileButton);

        // Retrieve the data from the intent
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");

        // Set the data to EditText fields
        nameEditText.setText(userName);
        emailEditText.setText(userEmail);

        // Set the OnClickListener for the save button
        saveProfileButton.setOnClickListener(view -> saveUserProfile());
    }


    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri uri) {
        utils.uploadImage(uri, utils.USER_PROFILE_FOLDER, this, url -> {
            // After the image is uploaded, we get back the URL
            saveProfileImageUrl(url);
        });
    }

    private void saveProfileImageUrl(String imageUrl) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users")
                    .document(firebaseUser.getUid())
                    .update("image", imageUrl)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditProfileActivity.this, "Image updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditProfileActivity.this, "Failed to update image", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveUserProfile() {
        String newName = nameEditText.getText().toString().trim();
        String newEmail = emailEditText.getText().toString().trim();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Query the collection to find the document where the 'authUID' field matches the user's UID
            db.collection("Users")
                    .whereEqualTo("authUID", firebaseUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            if (!documents.isEmpty()) {
                                // Get the first (and should be only) document in the query result
                                DocumentSnapshot documentSnapshot = documents.get(0);
                                Log.d("EditProfileActivity", "Document found with data: " + documentSnapshot.getData());

                                // Now update the document with the new name and email
                                documentSnapshot.getReference()
                                        .update("name", newName, "email", newEmail)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("EditProfileActivity", "Profile updated successfully.");
                                            Toast.makeText(EditProfileActivity.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                                            finish(); // Go back to the previous activity
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("EditProfileActivity", "Error updating profile: " + e.getMessage(), e);
                                            Toast.makeText(EditProfileActivity.this, "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Log.d("EditProfileActivity", "No document with matching UID found: " + firebaseUser.getUid());
                                Toast.makeText(EditProfileActivity.this, "Error: Profile not found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("EditProfileActivity", "Error querying for user document: " + task.getException(), task.getException());
                            Toast.makeText(EditProfileActivity.this, "Error checking for profile: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.d("EditProfileActivity", "User not logged in.");
            Toast.makeText(this, "You're not logged in.", Toast.LENGTH_LONG).show();
        }
    }

}


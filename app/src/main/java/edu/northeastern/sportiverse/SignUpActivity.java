package edu.northeastern.sportiverse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import edu.northeastern.sportiverse.databinding.ActivitySignUpBinding;
import edu.northeastern.sportiverse.Models.User;
import edu.northeastern.sportiverse.Utils.utils;

import com.squareup.picasso.Picasso;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private User user;
    private ActivityResultLauncher<String> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String text = "<font color=#FF000000>Already have an Account</font> <font color=#1E88E5>Login ?</font>";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            binding.login.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            binding.login.setText(Html.fromHtml(text));
        }
        user = new User(); // Make sure this User class import matches your project structure

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                utils.uploadImage(uri, utils.USER_PROFILE_FOLDER, url -> {
                    if (url != null) {
                        user.setImage(String.valueOf(url));
                        Picasso.get().load(uri).into(binding.profileImage);
                    }
                });
            }
        });

        if (getIntent().hasExtra("MODE")) {
            if (getIntent().getIntExtra("MODE", -1) == 1) {
                binding.signUpBtn.setText("Update Profile");
                FirebaseFirestore.getInstance().collection(utils.USER_NODE).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            user = documentSnapshot.toObject(User.class);
                            if (user != null && user.getImage() != null && !user.getImage().isEmpty()) {
                                Picasso.get().load(user.getImage()).into(binding.profileImage);
                            }
                            binding.name.getEditText().setText(user.getName());
                            binding.email.getEditText().setText(user.getEmail());
                            binding.password.getEditText().setText(user.getPassword());
                        });
            }
        }

        binding.signUpBtn.setOnClickListener(view -> {
            if (getIntent().hasExtra("MODE") && getIntent().getIntExtra("MODE", -1) == 1) {
                FirebaseFirestore.getInstance().collection(utils.USER_NODE)
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user)
                        .addOnSuccessListener(aVoid -> {
                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                            finish();
                        });
            } else {
                if (binding.name.getEditText().getText().toString().isEmpty() ||
                        binding.email.getEditText().getText().toString().isEmpty() ||
                        binding.password.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all the information", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            binding.email.getEditText().getText().toString(),
                            binding.password.getEditText().getText().toString()
                    ).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.setName(binding.name.getEditText().getText().toString());
                            user.setPassword(binding.password.getEditText().getText().toString());
                            user.setEmail(binding.email.getEditText().getText().toString());
                            FirebaseFirestore.getInstance().collection(utils.USER_NODE)
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                        finish();
                                    });
                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        binding.addImage.setOnClickListener(view -> launcher.launch("image/*"));
        binding.login.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }
}

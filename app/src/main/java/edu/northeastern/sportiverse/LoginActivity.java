package edu.northeastern.sportiverse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import edu.northeastern.sportiverse.Models.User;
import edu.northeastern.sportiverse.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginBtn.setOnClickListener(view -> {
            String email = binding.email.getEditText().getText().toString();
            String password = binding.pass.getEditText().getText().toString();

            if (email.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            } else {
                User user = new User(email, password);

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Find the TextView for signup prompt
        TextView signUpPrompt = findViewById(R.id.login);

        // HTML styled text
        String styledText = "Don't have an Account <font color='#1E88E5'>Signup?</font>";

        // Apply the styled text depending on Android version
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            signUpPrompt.setText(Html.fromHtml(styledText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            signUpPrompt.setText(Html.fromHtml(styledText));
        }

        // Set OnClickListener to navigate to SignUpActivity
        signUpPrompt.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}

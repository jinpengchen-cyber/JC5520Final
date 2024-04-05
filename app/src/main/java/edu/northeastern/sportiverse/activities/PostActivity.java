package edu.northeastern.sportiverse.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.northeastern.sportiverse.Models.Post;
import edu.northeastern.sportiverse.Utils.Constants;
import edu.northeastern.sportiverse.databinding.ActivityPostBinding;
import edu.northeastern.sportiverse.HomeActivity;
import edu.northeastern.sportiverse.Utils.utils;

public class PostActivity extends AppCompatActivity {
    private ActivityPostBinding binding;
    private String imageUrl = null;
    private FusedLocationProviderClient fusedLocationClient;
    private Double currentLatitude = null;
    private Double currentLongitude = null;

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();

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
                    System.currentTimeMillis(),
                    currentLatitude,
                    currentLongitude
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

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLatitude = location.getLatitude();
                            currentLongitude = location.getLongitude();
                        }
                    }
                });
    }
}

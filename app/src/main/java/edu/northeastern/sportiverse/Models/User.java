package edu.northeastern.sportiverse.Models;

import com.google.firebase.firestore.FirebaseFirestore;

public class User {


    private String name;
    private String email;
    private String password;
    private String image;
    private String authUID;
    private String fcmToken; // FCM token field

    public User() {
    }

    public User(String name, String email, String password, String image, String fcmToken) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.fcmToken = fcmToken;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthUID() {
        return authUID;
    }

    public void setAuthUID(String authUID) {
        this.authUID = authUID;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    // Add a method to update the FCM token in Firestore
    public void updateTokenToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (authUID != null && !authUID.isEmpty() && fcmToken != null) {
            db.collection("Users").document(authUID).update("fcmToken", fcmToken);
        }
    }
}

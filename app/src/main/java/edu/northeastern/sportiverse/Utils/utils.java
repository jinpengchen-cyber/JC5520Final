package edu.northeastern.sportiverse.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class utils {

    public static final String USER_NODE = "Users";
    public static final String USER_PROFILE_FOLDER = "User_Profile";

    public interface UploadCallback {
        void onUploadComplete(String url);
    }

    public static void uploadImage(Uri uri, String folderName, Context context, UploadCallback callback) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference(folderName)
                .child(UUID.randomUUID().toString());
        fileRef.putFile(uri)
                .continueWithTask(task -> task.isSuccessful() ? fileRef.getDownloadUrl() : null)
                .addOnSuccessListener(downloadUri -> {
                    if (downloadUri != null) {
                        callback.onUploadComplete(downloadUri.toString());

                    }
                });
    }

    public static void uploadVideo(Uri uri, String folderName, ProgressDialog progressDialog, Context context, UploadCallback callback) {
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference fileRef = FirebaseStorage.getInstance().getReference(folderName)
                .child(UUID.randomUUID().toString());
        UploadTask uploadTask = fileRef.putFile(uri);

        uploadTask.addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            progressDialog.setMessage("Uploaded " + (int) progress + "%");
        });

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful() && task.getException() != null) {
                throw task.getException();
            }
            return fileRef.getDownloadUrl();
        }).addOnSuccessListener(downloadUri -> {
            progressDialog.dismiss();
            if (downloadUri != null) {
                callback.onUploadComplete(downloadUri.toString());
            }
        });
    }
}

package edu.northeastern.sportiverse.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import edu.northeastern.sportiverse.Models.Post;
import edu.northeastern.sportiverse.Models.User;
import edu.northeastern.sportiverse.R;
import edu.northeastern.sportiverse.databinding.PostRvBinding;
import static edu.northeastern.sportiverse.Utils.Constants.USER_NODE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> {

    private Context context;
    private ArrayList<Post> postList;

    public PostAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostRvBinding binding = PostRvBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Post post = postList.get(position);

        FirebaseFirestore.getInstance().collection(USER_NODE).document(post.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            if (user != null && user.getImage() != null) {
                Glide.with(context).load(user.getImage()).placeholder(R.drawable.user).into(holder.binding.profileImage);
                holder.binding.name.setText(user.getName());
                Log.d("PostAdapter", "User: " + user.getName());
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });

        Glide.with(context).load(post.getPostUrl()).placeholder(R.drawable.loading).into(holder.binding.postImage);

        try {
            String text = TimeAgo.using(post.getTime());
            holder.binding.time.setText(text);
        } catch (Exception e) {
            holder.binding.time.setText("");
        }

        // Check for valid location data
        if (post.getLatitude() != null && post.getLongitude() != null) {
            new Thread(() -> {
                try {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(post.getLatitude(), post.getLongitude(), 1);
                    if (!addresses.isEmpty()) {
                        // Attempting to get a more descriptive part of the address
                        String address = addresses.get(0).getAddressLine(0); // This should give you a full address
                        // Update UI on the main thread
                        ((Activity) context).runOnUiThread(() -> holder.binding.locationText.setText(address));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            holder.binding.locationText.setVisibility(View.GONE);
        }


        holder.binding.share.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, post.getPostUrl());
            context.startActivity(Intent.createChooser(i, "Share via"));
        });

        holder.binding.csption.setText(post.getCaption());
        holder.binding.like.setOnClickListener(v -> {
            holder.binding.like.setImageResource(R.drawable.heart_like);
            // Assuming post and user IDs are correctly set
            String likerUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // ID of the user who liked the post
            String postOwnerId = post.getUid(); // ID of the user who owns the post
            String postId = post.getId(); // The ID of the post being liked

            // Prepare notification data
            Map<String, Object> notificationDetails = new HashMap<>();
            notificationDetails.put("likerId", likerUserId);
            notificationDetails.put("type", "like");
            notificationDetails.put("postId", postId);
            notificationDetails.put("message", "Someone liked your post!");

            // Update Realtime Database with notification data
            FirebaseDatabase.getInstance().getReference("postLikesNotifications")
                    .child(postOwnerId) // Notify the owner of the post
                    .push() // Generates a unique ID for the notification
                    .setValue(notificationDetails);
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        PostRvBinding binding;

        MyHolder(PostRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}


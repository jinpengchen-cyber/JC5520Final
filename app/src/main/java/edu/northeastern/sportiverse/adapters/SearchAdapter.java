package edu.northeastern.sportiverse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import edu.northeastern.sportiverse.R;
import edu.northeastern.sportiverse.databinding.SearchRvBinding;
import edu.northeastern.sportiverse.Models.User;
import edu.northeastern.sportiverse.Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<User> userList;

    public SearchAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchRvBinding binding = SearchRvBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final boolean[] isFollow = {false};
        User user = userList.get(position);

        Glide.with(context).load(user.getImage()).placeholder(R.drawable.user)
                .into(holder.binding.profileImage);
        holder.binding.name.setText(user.getName());

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + Constants.FOLLOW)
                .whereEqualTo("email", user.getEmail()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        isFollow[0] = false;
                        holder.binding.follow.setText("Follow");
                    } else {
                        holder.binding.follow.setText("Unfollow");
                        isFollow[0] = true;
                    }
                });

        holder.binding.follow.setOnClickListener(view -> {
            User targetUser = userList.get(position); // The user to be followed/unfollowed
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // The current user's ID

            if (isFollow[0]) {
                // Unfollow logic
                FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + Constants.FOLLOW)
                        .whereEqualTo("email", user.getEmail()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + Constants.FOLLOW)
                                        .document(queryDocumentSnapshots.getDocuments().get(0).getId()).delete()
                                        .addOnSuccessListener(aVoid -> {
                                            holder.binding.follow.setText("Follow");
                                            isFollow[0] = false;
                                        });
                            }
                        });
            } else {
                // Follow logic
                Map<String, Object> notificationDetails = new HashMap<>();
                notificationDetails.put("senderId", currentUserId);
                notificationDetails.put("type", "follow");
                notificationDetails.put("message", "You have a new follower!");

                FirebaseDatabase.getInstance().getReference("notifications")
                        .child(targetUser.getAuthUID()) // Use the target user's ID to store the notification
                        .push() // Generates a unique ID for the notification
                        .setValue(notificationDetails)
                        .addOnSuccessListener(aVoid -> {
                            holder.binding.follow.setText("Unfollow");
                            isFollow[0] = true;

                            // Now add the user to the follow collection
                            FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + Constants.FOLLOW)
                                    .add(user);
                        });
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SearchRvBinding binding;

        ViewHolder(SearchRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

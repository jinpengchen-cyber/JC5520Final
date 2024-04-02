package edu.northeastern.sportiverse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import edu.northeastern.sportiverse.R;
import edu.northeastern.sportiverse.databinding.SearchRvBinding;
import edu.northeastern.sportiverse.Models.User;
import edu.northeastern.sportiverse.Utils.Constants;

import java.util.ArrayList;

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
            if (isFollow[0]) {
                FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + Constants.FOLLOW)
                        .whereEqualTo("email", user.getEmail()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + Constants.FOLLOW)
                                        .document(queryDocumentSnapshots.getDocuments().get(0).getId()).delete();
                                holder.binding.follow.setText("Follow");
                                isFollow[0] = false;
                            }
                        });
            } else {
                FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid() + Constants.FOLLOW)
                        .add(user);
                holder.binding.follow.setText("Unfollow");
                isFollow[0] = true;
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

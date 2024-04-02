package edu.northeastern.sportiverse.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.firestore.FirebaseFirestore;
import edu.northeastern.sportiverse.Models.Post;
import edu.northeastern.sportiverse.Models.User;
import edu.northeastern.sportiverse.R;
import edu.northeastern.sportiverse.databinding.PostRvBinding;
import static edu.northeastern.sportiverse.Utils.Constants.USER_NODE;
import java.util.ArrayList;

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

        holder.binding.share.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, post.getPostUrl());
            context.startActivity(Intent.createChooser(i, "Share via"));
        });

        holder.binding.csption.setText(post.getCaption());
        holder.binding.like.setOnClickListener(v -> holder.binding.like.setImageResource(R.drawable.heart_like));
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


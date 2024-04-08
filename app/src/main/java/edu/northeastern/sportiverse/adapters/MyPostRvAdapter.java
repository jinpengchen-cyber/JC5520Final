package edu.northeastern.sportiverse.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import edu.northeastern.sportiverse.Models.Post;
import edu.northeastern.sportiverse.databinding.MyPostRvDesignBinding;
import java.util.ArrayList;

public class MyPostRvAdapter extends RecyclerView.Adapter<MyPostRvAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> postList;
    private OnPostClickListener listener;

    public MyPostRvAdapter(Context context, ArrayList<Post> postList, OnPostClickListener listener) {
        this.context = context;
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyPostRvDesignBinding binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        Picasso.get().load(post.getPostUrl()).into(holder.binding.postImage);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                Log.d("MyPostRvAdapter", "Post clicked with ID: " + post.getId()); // Debug log
                listener.onPostClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MyPostRvDesignBinding binding;

        ViewHolder(MyPostRvDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

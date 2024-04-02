package edu.northeastern.sportiverse.adapters;

import android.content.Context;
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

    public MyPostRvAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyPostRvDesignBinding binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(postList.get(position).getPostUrl()).into(holder.binding.postImage);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MyPostRvDesignBinding binding;

        ViewHolder(MyPostRvDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package edu.northeastern.sportiverse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import edu.northeastern.sportiverse.Models.Reel;
import edu.northeastern.sportiverse.databinding.MyPostRvDesignBinding;

import java.util.ArrayList;

public class MyReelAdapter extends RecyclerView.Adapter<MyReelAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Reel> reelList;

    public MyReelAdapter(Context context, ArrayList<Reel> reelList) {
        this.context = context;
        this.reelList = reelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyPostRvDesignBinding binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(reelList.get(position).getReelUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.postImage);
    }

    @Override
    public int getItemCount() {
        return reelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MyPostRvDesignBinding binding;

        ViewHolder(MyPostRvDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

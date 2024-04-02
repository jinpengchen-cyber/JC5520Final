package edu.northeastern.sportiverse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import edu.northeastern.sportiverse.R;
import edu.northeastern.sportiverse.databinding.ReelDgBinding;
import edu.northeastern.sportiverse.Models.Reel;

import java.util.ArrayList;

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Reel> reelList;

    public ReelAdapter(Context context, ArrayList<Reel> reelList) {
        this.context = context;
        this.reelList = reelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReelDgBinding binding = ReelDgBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reel reel = reelList.get(position);
        Picasso.get().load(reel.getProfileLink()).placeholder(R.drawable.user).into(holder.binding.profileImage);
        holder.binding.caption.setText(reel.getCaption());
        holder.binding.videoView.setVideoPath(reel.getReelUrl());
        holder.binding.videoView.setOnPreparedListener(mediaPlayer -> {
            holder.binding.progressBar.setVisibility(View.GONE);
            holder.binding.videoView.start();
        });
    }

    @Override
    public int getItemCount() {
        return reelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ReelDgBinding binding;

        ViewHolder(ReelDgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

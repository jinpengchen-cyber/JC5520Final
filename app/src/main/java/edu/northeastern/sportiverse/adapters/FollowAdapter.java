package edu.northeastern.sportiverse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import edu.northeastern.sportiverse.Models.User;
import edu.northeastern.sportiverse.R;
import edu.northeastern.sportiverse.databinding.FollowRvBinding;
import java.util.ArrayList;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {
    private Context context;
    private ArrayList<User> followList;

    public FollowAdapter(Context context, ArrayList<User> followList) {
        this.context = context;
        this.followList = followList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FollowRvBinding binding = FollowRvBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = followList.get(position);
        Glide.with(context)
                .load(user.getImage())
                .placeholder(R.drawable.user)
                .into(holder.binding.profileImage);
        holder.binding.name.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return followList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FollowRvBinding binding;

        public ViewHolder(FollowRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package com.brainzone.mindgames;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<LeaderboardItem> items;

    public LeaderboardAdapter(List<LeaderboardItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardItem item = items.get(position);
        holder.tvGameName.setText(item.getGameName());
        holder.tvDifficulty.setText(item.getDifficulty());
        holder.tvScore.setText(String.valueOf(item.getScore()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGameName, tvDifficulty, tvScore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            tvScore = itemView.findViewById(R.id.tvScore);
        }
    }
}
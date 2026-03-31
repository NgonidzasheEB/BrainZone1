package com.brainzone.mindgames.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.brainzone.mindgames.R;

import java.util.List;

/**
 * Drives the leaderboard list.
 * Supports two view types: section headers and score rows.
 */
public class LeaderboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ROW    = 1;

    public static class LeaderboardEntry {
        public final String label;
        public final String value;
        public final boolean isHeader;

        public LeaderboardEntry(String label, String value, boolean isHeader) {
            this.label    = label;
            this.value    = value;
            this.isHeader = isHeader;
        }
    }

    private final Context context;
    private final List<LeaderboardEntry> entries;

    public LeaderboardAdapter(Context context, List<LeaderboardEntry> entries) {
        this.context = context;
        this.entries = entries;
    }

    @Override
    public int getItemViewType(int position) {
        return entries.get(position).isHeader ? TYPE_HEADER : TYPE_ROW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_HEADER) {
            View v = inflater.inflate(R.layout.item_score_header, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = inflater.inflate(R.layout.item_score_row, parent, false);
            return new RowViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LeaderboardEntry entry = entries.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).tvTitle.setText(entry.label);
        } else {
            RowViewHolder row = (RowViewHolder) holder;
            row.tvLabel.setText(entry.label);
            row.tvValue.setText(entry.value);
        }
    }

    @Override
    public int getItemCount() { return entries.size(); }

    // ── ViewHolders ───────────────────────────────────────────────────────────
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        HeaderViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvScoreHeader);
        }
    }

    static class RowViewHolder extends RecyclerView.ViewHolder {
        TextView tvLabel, tvValue;
        RowViewHolder(View v) {
            super(v);
            tvLabel = v.findViewById(R.id.tvScoreLabel);
            tvValue = v.findViewById(R.id.tvScoreValue);
        }
    }
}

package com.brainzone.mindgames;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private List<MemoryCard> cards;
    private OnCardClickListener listener;

    public interface OnCardClickListener {
        void onCardClick(int position);
    }

    public MemoryAdapter(List<MemoryCard> cards, OnCardClickListener listener) {
        this.cards = cards;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemoryCard card = cards.get(position);
        if (card.isFlipped() || card.isMatched()) {
            holder.tvCardValue.setText(String.valueOf(card.getValue()));
            holder.tvCardValue.setBackgroundResource(R.color.white);
            holder.tvCardValue.setTextColor(R.color.primary_text);
        } else {
            holder.tvCardValue.setText("?");
            holder.tvCardValue.setBackgroundResource(R.color.purple_500);
            holder.tvCardValue.setTextColor(R.color.white);
        }

        holder.itemView.setOnClickListener(v -> listener.onCardClick(position));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardValue;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardValue = itemView.findViewById(R.id.tvCardValue);
        }
    }
}
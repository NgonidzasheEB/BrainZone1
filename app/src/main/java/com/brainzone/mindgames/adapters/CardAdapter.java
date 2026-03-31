package com.brainzone.mindgames.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.brainzone.mindgames.R;
import com.brainzone.mindgames.models.CardModel;

import java.util.List;

/**
 * Drives the Memory Match grid. Each item is a flippable card.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    public interface OnCardClickListener {
        void onCardClick(int position);
    }

    private final Context context;
    private final List<CardModel> cards;
    private final OnCardClickListener listener;

    public CardAdapter(Context context, List<CardModel> cards, OnCardClickListener listener) {
        this.context  = context;
        this.cards    = cards;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardModel card = cards.get(position);

        // ── Decide which face to show ─────────────────────────────────────────
        if (card.isFaceDown()) {
            holder.cardFront.setVisibility(View.GONE);
            holder.cardBack.setVisibility(View.VISIBLE);
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorCardBack));
            holder.cardView.setAlpha(1f);

        } else if (card.isFaceUp()) {
            holder.cardBack.setVisibility(View.GONE);
            holder.cardFront.setVisibility(View.VISIBLE);
            holder.cardFront.setText(card.getSymbol());
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorCardFront));
            holder.cardView.setAlpha(1f);

        } else { // MATCHED
            holder.cardBack.setVisibility(View.GONE);
            holder.cardFront.setVisibility(View.VISIBLE);
            holder.cardFront.setText(card.getSymbol());
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorCorrect));
            holder.cardView.setAlpha(0.7f);
        }

        // ── Click ─────────────────────────────────────────────────────────────
        holder.cardView.setOnClickListener(v -> {
            if (card.isFaceDown()) {
                listener.onCardClick(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() { return cards.size(); }

    // ── Perform a simple scale-flip animation on a card ───────────────────────
    public void animateFlip(RecyclerView recyclerView, int position) {
        RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(position);
        if (vh == null) return;
        View cardView = vh.itemView;

        cardView.animate()
            .scaleX(0f)
            .setDuration(120)
            .withEndAction(() -> {
                notifyItemChanged(position);
                cardView.animate()
                    .scaleX(1f)
                    .setDuration(120)
                    .start();
            })
            .start();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView cardFront;
        TextView cardBack;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView  = itemView.findViewById(R.id.cardView);
            cardFront = itemView.findViewById(R.id.tvCardFront);
            cardBack  = itemView.findViewById(R.id.tvCardBack);
        }
    }
}

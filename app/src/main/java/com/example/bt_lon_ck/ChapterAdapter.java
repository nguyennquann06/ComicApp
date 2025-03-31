package com.example.bt_lon_ck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    private List<Chapter> chapterList;
    private final Context context;
    private OnChapterEditListener editListener;
    private OnChapterDeleteListener deleteListener;

    public ChapterAdapter(List<Chapter> chapterList, Context context) {
        this.chapterList = chapterList;
        this.context = context;
    }

    public void setOnChapterEditListener(OnChapterEditListener listener) {
        this.editListener = listener;
    }

    public void setOnChapterDeleteListener(OnChapterDeleteListener listener) {
        this.deleteListener = listener;
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_item, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);

        holder.chapterTitleTextView.setText(chapter.getChapterTitle());

        int resId = context.getResources().getIdentifier(
                chapter.getChapterImage(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.chapterImageView.setImageResource(resId);
        } else {
            holder.chapterImageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.editButton.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEdit(chapter);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(chapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList != null ? chapterList.size() : 0;
    }

    public void updateList(List<Chapter> newList) {
        this.chapterList = newList;
        notifyDataSetChanged();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterTitleTextView;
        ImageView chapterImageView;
        Button editButton, deleteButton;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            chapterTitleTextView = itemView.findViewById(R.id.chapterTitleTextView);
            chapterImageView = itemView.findViewById(R.id.chapterImageView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnChapterEditListener {
        void onEdit(Chapter chapter);
    }

    public interface OnChapterDeleteListener {
        void onDelete(Chapter chapter);
    }
}
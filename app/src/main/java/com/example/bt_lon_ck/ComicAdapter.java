package com.example.bt_lon_ck;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {
    private List<Comic> comicList;
    private Context context;
    private boolean isAdmin;
    private OnComicEditListener editListener;
    private OnComicDeleteListener deleteListener;
    private OnComicClickListener clickListener;

    public ComicAdapter(List<Comic> comicList, Context context, boolean isAdmin) {
        this.comicList = comicList;
        this.context = context;
        this.isAdmin = isAdmin;
    }

    public void setOnComicEditListener(OnComicEditListener listener) {
        this.editListener = listener;
    }

    public void setOnComicDeleteListener(OnComicDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setOnComicClickListener(OnComicClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public ComicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_item, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ComicViewHolder holder, int position) {
        Comic comic = comicList.get(position);
        holder.titleTextView.setText(comic.getTitle());
        holder.authorTextView.setText(comic.getAuthor());

        int resId = context.getResources().getIdentifier(comic.getImage(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.comicImageView.setImageResource(resId);
        } else {
            holder.comicImageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(comic);
            } else if (!isAdmin) {
                Intent intent = new Intent(context, ChapterActivity.class);
                intent.putExtra("comic_id", comic.getId());
                context.startActivity(intent);
            }
        });

        if (isAdmin) {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);

            holder.editButton.setOnClickListener(v -> {
                if (editListener != null) {
                    editListener.onEdit(comic);
                }
            });

            holder.deleteButton.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDelete(comic);
                }
            });
        } else {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public void updateList(List<Comic> newList) {
        comicList = newList;
        notifyDataSetChanged();
    }

    public class ComicViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView;
        ImageView comicImageView;
        Button editButton, deleteButton;

        public ComicViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            comicImageView = itemView.findViewById(R.id.comicImageView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnComicEditListener {
        void onEdit(Comic comic);
    }

    public interface OnComicDeleteListener {
        void onDelete(Comic comic);
    }

    public interface OnComicClickListener {
        void onClick(Comic comic);
    }
}
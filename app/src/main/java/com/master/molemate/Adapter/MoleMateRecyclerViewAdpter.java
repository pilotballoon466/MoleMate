package com.master.molemate.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.master.molemate.ImageFileStorage.SupporterClasses.RecyclerViewMoleImageItem;
import com.master.molemate.R;

import java.util.ArrayList;

public class MoleMateRecyclerViewAdpter extends RecyclerView.Adapter<MoleMateRecyclerViewAdpter.MoleMateRecyclerViewViewHolder> {

    private ArrayList<RecyclerViewMoleImageItem> moleItemsList;

    public static class MoleMateRecyclerViewViewHolder extends RecyclerView.ViewHolder{

        public ImageView cardViewMoleThumbnailImage;
        public TextView cardviewMoleTitle;
        public TextView cardviewMoleText;

        public MoleMateRecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewMoleThumbnailImage = itemView.findViewById(R.id.cardviewMoleThumbnailImage);
            cardviewMoleTitle = itemView.findViewById(R.id.cardviewMoleTitle);
            cardviewMoleText = itemView.findViewById(R.id.cardviewMoleText);

        }
    }

    public MoleMateRecyclerViewAdpter(ArrayList<RecyclerViewMoleImageItem> moleItemsList) {
        this.moleItemsList = moleItemsList;
    }

    @NonNull
    @Override
    public MoleMateRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_mole_image_item, parent, false);

        MoleMateRecyclerViewViewHolder viewHolder = new MoleMateRecyclerViewViewHolder(layout);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoleMateRecyclerViewViewHolder holder, int position) {
        RecyclerViewMoleImageItem currentItem = moleItemsList.get(position);

        holder.cardViewMoleThumbnailImage.setImageResource(currentItem.getMoleThumbnail());
        holder.cardviewMoleTitle.setText(currentItem.getCardViewTitle());
        holder.cardviewMoleText.setText(currentItem.getCardviewText());

    }

    @Override
    public int getItemCount() {
        return moleItemsList.size();
    }

}

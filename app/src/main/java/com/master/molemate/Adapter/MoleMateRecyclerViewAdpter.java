package com.master.molemate.Adapter;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.master.molemate.ImageFileStorage.SupporterClasses.RecyclerViewMoleImageItem;
import com.master.molemate.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MoleMateRecyclerViewAdpter extends RecyclerView.Adapter<MoleMateRecyclerViewAdpter.MoleMateRecyclerViewViewHolder> implements Filterable {

    private ArrayList<RecyclerViewMoleImageItem> moleItemsList;
    private ArrayList<RecyclerViewMoleImageItem> fullItemList;
    private OnMoleListener onMoleListener;


    static class MoleMateRecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         ImageView cardViewMoleThumbnailImage;
         TextView cardviewMoleTitle;
         TextView cardviewMoleText;
         OnMoleListener onMoleListener;

         MoleMateRecyclerViewViewHolder(@NonNull View itemView, OnMoleListener onMoleListener) {
            super(itemView);

            cardViewMoleThumbnailImage = itemView.findViewById(R.id.cardviewMoleThumbnailImage);
            cardviewMoleTitle = itemView.findViewById(R.id.cardviewMoleTitle);
            cardviewMoleText = itemView.findViewById(R.id.cardviewMoleText);

            this.onMoleListener = onMoleListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
             onMoleListener.onMoleClick(getAdapterPosition());

        }
    }

    public MoleMateRecyclerViewAdpter(ArrayList<RecyclerViewMoleImageItem> moleItemsList, OnMoleListener onMoleListener) {
        this.moleItemsList = moleItemsList;
        fullItemList = new ArrayList<>(moleItemsList);
        this.onMoleListener = onMoleListener;
    }

    @NonNull
    @Override
    public MoleMateRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_mole_image_item, parent, false);

        return new MoleMateRecyclerViewViewHolder(layout, onMoleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoleMateRecyclerViewViewHolder holder, int position) {
        RecyclerViewMoleImageItem currentItem = moleItemsList.get(position);

        Bitmap bitmap = BitmapFactory.decodeFile(currentItem.getMoleThumbnail().getPath());


        holder.cardViewMoleThumbnailImage.setImageResource(currentItem.getMoleThumbnail());
        holder.cardviewMoleTitle.setText(currentItem.getCardViewTitle());
        holder.cardviewMoleText.setText(currentItem.getCardviewText());

    }

    @Override
    public int getItemCount() {
        return moleItemsList.size();
    }



    @Override
    public Filter getFilter() {
        return moleFilter;
    }

    private Filter moleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RecyclerViewMoleImageItem> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(fullItemList);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (RecyclerViewMoleImageItem item : fullItemList){
                    if(item.getCardviewText().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            moleItemsList.clear();
            moleItemsList.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };

    public interface OnMoleListener{
        void onMoleClick(int position);
    }

}

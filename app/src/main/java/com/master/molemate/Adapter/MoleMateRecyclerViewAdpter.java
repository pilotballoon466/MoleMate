package com.master.molemate.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.master.molemate.ImageFileStorage.SupporterClasses.RecyclerViewMoleImageItem;
import com.master.molemate.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;

public class MoleMateRecyclerViewAdpter extends RecyclerView.Adapter<MoleMateRecyclerViewAdpter.MoleMateRecyclerViewViewHolder> implements Filterable {

    private static final String TAG = "MoleMateRecyclerViewAdp";
    
    private ArrayList<RecyclerViewMoleImageItem> moleItemsList;
    private ArrayList<RecyclerViewMoleImageItem> fullItemList;
    private OnMoleListener onMoleListener;


    static class MoleMateRecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         ImageView cardviewMoleThumbnailImage;
         TextView cardviewMoleTitle;
         TextView cardviewMoleText;
         CardView cardviewView;
         OnMoleListener onMoleListener;

         MoleMateRecyclerViewViewHolder(@NonNull View itemView, OnMoleListener onMoleListener) {
            super(itemView);

            cardviewMoleThumbnailImage = itemView.findViewById(R.id.cardviewMoleThumbnailImage);
            cardviewMoleTitle = itemView.findViewById(R.id.cardviewMoleTitle);
            cardviewMoleText = itemView.findViewById(R.id.cardviewMoleText);
            cardviewView = itemView.findViewById(R.id.recyclerViewMoleItem);

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

        Log.d(TAG, "MoleMateRecyclerViewAdpter: moleList " + moleItemsList.toString() + " fullItem " + fullItemList.toString());
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
        holder.cardviewMoleThumbnailImage.setImageBitmap(bitmap);
        holder.cardviewMoleThumbnailImage.setRotation(90f);
        //holder.cardviewMoleThumbnailImage.setRotation(holder.cardviewMoleThumbnailImage.getRotation()*90f);

        double tmpPropDiagnosis = currentItem.getMoleDiagnosis();
        boolean isHandled = currentItem.isHandeld();
        String tmpDate = currentItem.getCardViewDate();

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date d = null;
        try{
            d = sdf.parse(tmpDate);
        }catch (ParseException ex){
            ex.printStackTrace();
        }

        if(d != null) {
            sdf.applyPattern("dd.MM.yyyy");
            tmpDate = sdf.format(d);
            Log.d(TAG, "onBindViewHolder: Date: " + tmpDate);
            holder.cardviewMoleText.setText(tmpDate);
        }

        Log.d(TAG, "onBindViewHolder: Color should be: " + tmpPropDiagnosis);

        if(tmpPropDiagnosis<25.0 && !isHandled){
            holder.cardviewView.setBackgroundColor(Color.RED);
        } else if (tmpPropDiagnosis<75.0 && tmpPropDiagnosis > 25.0 && !isHandled){
            holder.cardviewView.setBackgroundColor(Color.YELLOW);
        }else if (tmpPropDiagnosis>= 75.0 && !isHandled){
            holder.cardviewView.setBackgroundColor(Color.GREEN);
        }

        holder.cardviewMoleTitle.setText(currentItem.getCardviewPosText());

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

            if(constraint == null || constraint.length() == 0 || constraint.equals("")){
                filteredList.addAll(fullItemList);
                Log.d(TAG, "performFiltering: added fullList: " + fullItemList.toString() );
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                Log.d(TAG, "performFiltering: filterPattern: " + filterPattern );

                for (RecyclerViewMoleImageItem item : fullItemList){
                    if(item.getCardviewText().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                        Log.d(TAG, "performFiltering: added search Item: " + item.getCardviewPosText());
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
            Log.d(TAG, "publishResults: values " + filterResults.values.toString());
            moleItemsList.addAll((ArrayList<RecyclerViewMoleImageItem>) filterResults.values);
            notifyDataSetChanged();

        }
    };

    public void setFullList() {
        fullItemList = new ArrayList<>(moleItemsList);
    }

    public interface OnMoleListener{
        void onMoleClick(int position);
    }

}

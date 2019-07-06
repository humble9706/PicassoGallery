package com.toborehumble.picassogallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter {
    List<GalleryItem> galleryItemList;
    Context context;
    GalleryAdapterCallBacks mAdapterCallBacks;

    public GalleryAdapter(Context context){
        this.context = context;
        this.mAdapterCallBacks = (GalleryAdapterCallBacks) context;
        this.galleryItemList = new ArrayList<>();
    }

    public void addGalleryItems(List<GalleryItem> galleryItems){
        int previousSize = galleryItems.size();
        this.galleryItemList.addAll(galleryItems);
        notifyItemRangeChanged(previousSize, galleryItemList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_row_gallery_item, parent, false);
        return new GalleryItemHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        GalleryItem currentItem = galleryItemList.get(position);
        File imageViewThumb = new File(currentItem.imageUri);
        GalleryItemHolder galleryItemHolder = (GalleryItemHolder) viewHolder;
        Picasso.get().load(imageViewThumb).centerCrop().resize(ScreenUtils.getScreenWidth(context) / 2, ScreenUtils.getScreenHeight(context) / 3).into(galleryItemHolder.imageViewThumbnail);
        galleryItemHolder.textViewImageName.setText(currentItem.imageName);

        galleryItemHolder.imageViewThumbnail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mAdapterCallBacks.onItemSelected(position);
            }
        });
    }

    @Override
    public int getItemCount(){
        return galleryItemList.size();
    }

    public class GalleryItemHolder extends RecyclerView.ViewHolder{
        ImageView imageViewThumbnail;
        TextView textViewImageName;

        public GalleryItemHolder(View itemView){
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewImageName = itemView.findViewById(R.id.textViewImageName);
        }
    }

    public interface GalleryAdapterCallBacks{
        void onItemSelected(int position);
    }
}

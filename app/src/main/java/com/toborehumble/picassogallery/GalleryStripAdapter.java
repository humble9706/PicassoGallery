package com.toborehumble.picassogallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class GalleryStripAdapter extends RecyclerView.Adapter {
    List<GalleryItem> galleryItems;
    Context context;
    GalleryStripCallBacks galleryStripCallBacks;
    GalleryItem currentSelected;

    public GalleryStripAdapter(List<GalleryItem> galleryItems, Context context, GalleryStripCallBacks galleryStripCallBacks, int currentPosition){
        this.galleryItems = galleryItems;
        this.context = context;
        this.galleryStripCallBacks = galleryStripCallBacks;
        currentSelected = galleryItems.get(currentPosition);
        currentSelected.isSelected = true;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_row_gallery_strip_item, parent, false);
        SquareLayout squareLayout = row.findViewById(R.id.squareLayout);
        return new GalleryStripItemHolder(squareLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        GalleryItem currentItem = galleryItems.get(position);
        final int thumbSize = ScreenUtils.getScreenWidth(context)/6;
        //cast holder to galleryStripItemHolder
        GalleryStripItemHolder galleryStripItemHolder = (GalleryStripItemHolder) holder;
        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(currentItem.imageUri), thumbSize, thumbSize);
        //set thumbnail
        galleryStripItemHolder.imageViewThumbnail.setImageBitmap(thumbImage);
        //set current selected
        if(currentItem.isSelected){
            galleryStripItemHolder.imageViewThumbnail.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
        }else {
            //value 0 removes any bg color
            galleryStripItemHolder.imageViewThumbnail.setBackgroundColor(0);
        }
        galleryStripItemHolder.imageViewThumbnail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                galleryStripCallBacks.onGalleryStripItemSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public class GalleryStripItemHolder extends RecyclerView.ViewHolder{
        ImageView imageViewThumbnail;

       public GalleryStripItemHolder(View itemView){
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
        }
    }

    //method to remove selection
    public void removeSelection(){
        currentSelected.isSelected = false;
    }

    //method to highlight selected item on gallery strip
    public void setSelected(int position){
        currentSelected.isSelected = false;
        //notify recycler view to update
        notifyItemChanged(galleryItems.indexOf(currentSelected));
        //select gallery item
        galleryItems.get(position).isSelected = true;
        //notify recycler view to update
        notifyItemChanged(position);
        //set current selected
        currentSelected = galleryItems.get(position);
    }

    //interface for comm. on gallery strip interactions
    public interface GalleryStripCallBacks{
        void onGalleryStripItemSelected(int position);
    }
}

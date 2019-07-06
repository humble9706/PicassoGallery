package com.toborehumble.picassogallery;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.VolumeShaper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GalleryAdapter.GalleryAdapterCallBacks {
    public List<GalleryItem> galleryItems;
    private static final int RC_READ_STORAGE = 5;
    GridLayoutManager gridLayoutManager;
    GalleryAdapter galleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerViewGallery = (RecyclerView) findViewById(R.id.recyclerViewGallery);
        Configuration configuration = new Configuration();
        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridLayoutManager = new GridLayoutManager(this, 3);
            Toast.makeText(this, "land", Toast.LENGTH_SHORT).show();
        }else {
            gridLayoutManager = new GridLayoutManager(this, 2);
            Toast.makeText(this, "port", Toast.LENGTH_SHORT).show();
        }
        recyclerViewGallery.setLayoutManager(gridLayoutManager);
        galleryAdapter = new GalleryAdapter(this);
        recyclerViewGallery.setAdapter(galleryAdapter);
        recyclerViewGallery.setHasFixedSize(true);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            galleryItems = GalleryUtils.getImages(this);
            galleryAdapter.addGalleryItems(galleryItems);
        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RC_READ_STORAGE);
        }
    }

    @Override
    public void onItemSelected(int position){
        //create slide show fragment
        SlideShowFragment slideShowFragment = SlideShowFragment.newInstance(position);
        //set up style for slide show fragment
        slideShowFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        //finally, show dialog
        slideShowFragment.show(getSupportFragmentManager(), "slide");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RC_READ_STORAGE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                galleryItems = GalleryUtils.getImages(this);
                galleryAdapter.addGalleryItems(galleryItems);
            }else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.toborehumble.picassogallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
Remember to implement GalleryStripAdapter.GalleryStripCallBacks in fragment for
communication between fragment and GalleryStripAdapter
*/
public class SlideShowFragment extends DialogFragment implements GalleryStripAdapter.GalleryStripCallBacks {
    private static final String ARG_CURRENT_POSITION = "position";
    List<GalleryItem> galleryItems;
    GalleryStripAdapter galleryStripAdapter;
    SlideShowPagerAdapter slideShowPagerAdapter;
    Context context;

    ViewPager viewPagerGallery;
    TextView textViewImageName;
    RecyclerView recyclerViewGalleryStrip;


    private int currentPosition;
    //set bottom to visible on first load
    boolean isBottomVisible = true;

    public SlideShowFragment(){
        //required empty constructor
    }

    //creates new instance of SlideShowFragment
    public static SlideShowFragment newInstance(int position){
        SlideShowFragment fragment = new SlideShowFragment();
        //create bundle
        Bundle args = new Bundle();
        //put current position in bundle
        args.putInt(ARG_CURRENT_POSITION, position);
        //set arguments of slide show fragment
        fragment.setArguments(args);
        //return fragment instance
        return fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_slide_show, container, false);
        viewPagerGallery = view.findViewById(R.id.viewPagerGallery);
        //set onTouch listener on view pager to hide and show bottom bar
        viewPagerGallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    if(isBottomVisible){
                        //make bottom bar invisible
                        fadeOutBottomBar();
                    }else {
                        //bottom bar is invisible make it visible
                        fadeInBottomBar();
                    }
                }
                return false;
            }
        });

        galleryItems = new ArrayList<>();
        if (getArguments() != null) {
            //get Current selected position from arguments
            currentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
            //get GalleryItems from activity
            galleryItems = ((MainActivity) getActivity()).galleryItems;
        }

        textViewImageName  = view.findViewById(R.id.textViewImageName);
        //initialize view pager adapter
        slideShowPagerAdapter = new SlideShowPagerAdapter(getContext(), galleryItems);
        viewPagerGallery.setAdapter(slideShowPagerAdapter);
        recyclerViewGalleryStrip = view.findViewById(R.id.recyclerViewGalleryStrip);
        final RecyclerView.LayoutManager galleryStripLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewGalleryStrip.setLayoutManager(galleryStripLayoutManager);
        galleryStripAdapter = new GalleryStripAdapter(galleryItems, getContext(), this, currentPosition);
        recyclerViewGalleryStrip.setAdapter(galleryStripAdapter);
        //tell view pager to open currently selected item and pass
        //position of current item
        viewPagerGallery.setCurrentItem(currentPosition);

        textViewImageName.setText(galleryItems.get(currentPosition).imageName);

        //add onPageChangeListener to view pager to handle pager changes
        viewPagerGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                //set image name on any page selected
                textViewImageName.setText(galleryItems.get(position).imageName);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //first check whrn page scrolled and gets stable
                if(state == ViewPager.SCROLL_STATE_IDLE){
                    //get current item on view pager
                    int currentlySelected = viewPagerGallery.getCurrentItem();
                    //scroll strip smoothly to current position of view pager
                    galleryStripLayoutManager.smoothScrollToPosition(recyclerViewGalleryStrip, null, currentlySelected);
                    //select current item of view pager on gallery strip at bottom
                    galleryStripAdapter.setSelected(currentlySelected);
                }
            }
        });
        return view;
    }

    //overridden method by GalleryStripAdapter G.CallBacks for communication
    @Override
    public void onGalleryStripItemSelected(int position){
        //set current item on view pager
        viewPagerGallery.setCurrentItem(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //remove selected item to destroy
        galleryStripAdapter.removeSelection();
    }

    //method to fade in bottom bar (image name)
    public void fadeInBottomBar(){
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1200);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewImageName.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //start animation
        textViewImageName.startAnimation(fadeIn);
        isBottomVisible = true;
    }

    public void fadeOutBottomBar(){
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(1200);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewImageName.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //start animation
        textViewImageName.startAnimation(fadeOut);
        isBottomVisible = false;
    }
}

package com.montek.tohafaa.Adapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.*;
import android.view.*;
import android.widget.ImageView;
//import com.bumptech.glide.Glide;
import com.montek.tohafaa.R;
import com.montek.tohafaa.model.categoryitems;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.InputStream;
import java.util.ArrayList;

import static com.montek.tohafaa.Adapter.ProductAdapter.imgloader;

public class SliderPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<categoryitems> image_arraylist;
    public SliderPagerAdapter(Activity activity, ArrayList<categoryitems> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);
        Log.d("image_arraylist",image_arraylist.get(i).getimage_url());
//         Glide.with(activity).load(image_arraylist.get(i).getimage_url())
//                .thumbnail(0.5f)
//                .crossFade()
//                .skipMemoryCache(true)
//                //.error(R.drawable.placeholder)
//                .into(im_slider);
        imgloader.init(ImageLoaderConfiguration.createDefault(activity));
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_flag_blank)
                .showImageOnFail(R.drawable.ic_flag_blank)
                .showImageOnLoading(R.drawable.ic_flag_blank)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        //download and display image from url
        imgloader.displayImage(image_arraylist.get(i).getimage_url(), im_slider, options);
        container.addView(view);
        return view;
    }
    @Override
    public int getCount() {
        return image_arraylist.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
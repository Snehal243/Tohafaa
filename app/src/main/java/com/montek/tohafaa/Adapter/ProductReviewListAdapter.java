package com.montek.tohafaa.Adapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.montek.tohafaa.R;
import com.montek.tohafaa.model.categoryitems;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import static com.montek.tohafaa.Adapter.ProductAdapter.imgloader;
import static com.montek.tohafaa.CorporateClient.corpoarateclient;

public class ProductReviewListAdapter extends ArrayAdapter<categoryitems> {
    public ArrayList<categoryitems> MainList;
    public ArrayList<categoryitems> SortinglistFreelancerListTemp;
   // public ProductReviewListAdapter.SortingListFreelancerDataFilter SortinglistFreelancerDataFilter ;
    public ProductReviewListAdapter(Context context, int id, ArrayList<categoryitems> SortinglistFreelancerArrayList) {
        super(context, id, SortinglistFreelancerArrayList);
        this.SortinglistFreelancerListTemp = new ArrayList<categoryitems>();
        this.SortinglistFreelancerListTemp.addAll(SortinglistFreelancerArrayList);
        this.MainList = new ArrayList<categoryitems>();
        this.MainList.addAll(SortinglistFreelancerArrayList);
    }

    public class ViewHolder {
        TextView txv_customerreview,txv_name,reviewtitle,review;
        RatingBar ratingbar;
        android.support.v7.widget.CardView CardView,customerReviews;
        ImageView profile,imageView;
        LinearLayout GridView;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ProductReviewListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.activity_productreviewelement, null);
            holder = new ProductReviewListAdapter.ViewHolder();
            holder.reviewtitle = (TextView) convertView.findViewById(R.id.txv_reviewtitle);
            holder.review = (TextView) convertView.findViewById(R.id.txv_review);
            holder.txv_customerreview = (TextView)convertView.findViewById(R.id.txv_customerreview);
            holder.txv_name = (TextView)convertView.findViewById(R.id.txv_name);
            holder.ratingbar = (RatingBar) convertView.findViewById(R.id.ratingbar);
            LayerDrawable a1 = (LayerDrawable) holder.ratingbar.getProgressDrawable();
            a1.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            a1.getDrawable(2).setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            a1.getDrawable(1).setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            holder.CardView = (CardView) convertView.findViewById(R.id.CardView);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.profile = (ImageView) convertView.findViewById(R.id.profile);
            holder.GridView = (LinearLayout) convertView.findViewById(R.id.GridView);
            holder.customerReviews = (CardView) convertView.findViewById(R.id.customerReviews);
            convertView.setTag(holder);
        } else {
            holder = (ProductReviewListAdapter.ViewHolder) convertView.getTag();
        }


        //((holder) convertView.getTag()).checkbox.setTag(FilterListTemp.get(position));
        categoryitems test = SortinglistFreelancerListTemp.get(position);
        if(corpoarateclient==1)
        {
            holder.GridView.setVisibility(View.VISIBLE);
            holder.CardView.setVisibility(View.GONE);
            holder.customerReviews.setVisibility(View.GONE);
            imgloader.init(ImageLoaderConfiguration.createDefault(getContext()));
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .build();
            imgloader.displayImage("http://www.tohafaa.com/"+test.getreq_bank_detail_status(),  holder.imageView, options);
        }
        else  if(corpoarateclient==0) {
            holder.GridView.setVisibility(View.GONE);
            holder.CardView.setVisibility(View.VISIBLE);
            holder.customerReviews.setVisibility(View.GONE);
            holder.reviewtitle.setText(test.getproduct_name());
            holder.review.setText(test.getreq_bank_detail_status());
            holder.ratingbar.setRating(Float.parseFloat(test.getordertotal()));
        }
        else  if(corpoarateclient==2)
        {
            holder.GridView.setVisibility(View.GONE);
            holder.CardView.setVisibility(View.GONE);
            holder.customerReviews.setVisibility(View.VISIBLE);
            holder.txv_customerreview.setText(test.getordertotal());
            holder.txv_name.setText(test.getorderid()+" "+test.getproduct_name());
            imgloader.init(ImageLoaderConfiguration.createDefault(getContext()));
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageOnFail(R.drawable.yourlogo)
                    .build();
            imgloader.displayImage("http://responsive.tohafaa.com/"+test.getreq_bank_detail_status(),  holder.profile, options);
        }

        return convertView;
    }

}
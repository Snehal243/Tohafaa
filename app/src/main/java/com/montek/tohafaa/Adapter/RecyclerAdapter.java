package com.montek.tohafaa.Adapter;
import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;
//import com.bumptech.glide.Glide;
import com.montek.tohafaa.DashBordActivity;
import com.montek.tohafaa.R;
import com.montek.tohafaa.TabFragments.DisplayItemsFragment;
import com.montek.tohafaa.interfaces.ItemClickListener;
import com.montek.tohafaa.model.categoryitems;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import static com.montek.tohafaa.Adapter.ProductAdapter.imgloader;
import static com.montek.tohafaa.DashBordActivity.*;
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<categoryitems> item;
    private Context context;
    private ItemClickListener clickListener;
    public RecyclerAdapter(Context context, ArrayList<categoryitems> item) {
        this.item = item;
        this.context = context;
    }
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = null;
             view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, final int i) {
        if(DashBordActivity.categorieshorizantal.equalsIgnoreCase("1")) {
            viewHolder.card_view1.setVisibility(View.VISIBLE);
            viewHolder.layout.setVisibility(View.GONE);
            viewHolder.tv_name1.setText(item.get(i).getname());
//            Glide.with(context).load(item.get(i).getimage_url())
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .skipMemoryCache(true)
//                    .into(viewHolder.img1);
            imgloader.init(ImageLoaderConfiguration.createDefault(context));

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
            imgloader.displayImage(item.get(i).getimage_url(), viewHolder.img1, options);

            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {
                    DashBordActivity.sc_category_id = item.get(i).getid();
                    DashBordActivity.name = item.get(i).getname();
                    switch (v.getId()) {
                        default:
                           Intent i=new Intent(v.getContext(),DashBordActivity.class);
                            v.getContext().startActivity(i);
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            activity.finish();
                            break;
                    }
                }
            });
        }
        else
        {
            viewHolder.card_view1.setVisibility(View.GONE);
            viewHolder.layout.setVisibility(View.VISIBLE);
            viewHolder.tv_name.setText(item.get(i).getname());
//            Glide.with(context).load(item.get(i).getimage_url())
//                .thumbnail(0.5f)
//                .crossFade()
//                .skipMemoryCache(true)
//                .into(viewHolder.img);

            imgloader.init(ImageLoaderConfiguration.createDefault(context));
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
            imgloader.displayImage(item.get(i).getimage_url(), viewHolder.img, options);

        Log.d("item.get(i)",item.get(i).getimage_url());
        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                if(sc_category_id.equalsIgnoreCase("4") ||  sc_category_id.equalsIgnoreCase("101") ||sc_category_id.equalsIgnoreCase("") ) {
                    DisplayItemsFragment.sc_spc_id = item.get(i).getid();
                    DashBordActivity.SubCategoryName = item.get(i).getname();
                    sc_category_id = "";
                    sc_subcategory_id = "";
                }
                else
                {
                    DisplayItemsFragment.sc_spc_id="";
                    sc_subcategory_id = item.get(i).getid();
                }
                searchproductname="";
                includelayout.setVisibility(View.GONE);
                switch (v.getId()) {
                    default:
                        Intent i=new Intent(v.getContext(),DisplayItemsFragment.class);
                        v.getContext().startActivity(i);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        activity.finish();
                        break;
                }
            }
        });}
    }
    @Override
    public int getItemCount() {
        return item.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView tv_name,price,tv_name1;
        private ImageView img,img_wishlist,img1;
        CardView card_view1;
        LinearLayout layout;
        private ItemClickListener clickListener;
        public ViewHolder(View view) {
            super(view);
            card_view1 = (CardView) view.findViewById(R.id.card_view1);
            layout = (LinearLayout) view.findViewById(R.id.layout);
            tv_name = (TextView)view.findViewById(R.id.grid_item_title);
            price = (TextView)view.findViewById(R.id.grid_item_price);
            price.setVisibility(View.GONE);
            img_wishlist = (ImageView) view.findViewById(R.id.img_wishlist);
            img_wishlist.setVisibility(View.GONE);
            img = (ImageView) view.findViewById(R.id.grid_item_image);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            tv_name1 = (TextView)view.findViewById(R.id.grid_item_title1);
            img1 = (ImageView) view.findViewById(R.id.grid_item_image1);
        }
        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getPosition(), false);
        }
        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getPosition(), true);
            return true;
        }
    }
}
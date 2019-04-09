package com.montek.tohafaa.Adapter;
import android.app.ProgressDialog;
import android.content.*;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;
//import com.bumptech.glide.Glide;
import com.montek.tohafaa.Activity_OrdersDetails;
import com.montek.tohafaa.Activity_OrdersList;
import com.montek.tohafaa.Activity_Uploadimage;
import com.montek.tohafaa.Activity_WishList;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.R;
import com.montek.tohafaa.TabFragments.DisplayItemsFragment;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.interfaces.ItemClickListener;
import com.montek.tohafaa.model.categoryitems;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.*;
import static com.montek.tohafaa.Activity_WishList.*;
import static com.montek.tohafaa.Adapter.ProductAdapter.imgloader;
import static com.montek.tohafaa.DashBordActivity.*;
import static com.montek.tohafaa.LoginActivity.p;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder>  {
    public static ArrayList<categoryitems> item1;
    private Context context;
    public static String sv_id;//,sv_product_id,sv_cartname,sv_cartprice,sv_cartsrc,sv_product_shipping_cost,sv_product_qty,sv_qty,sv_order_size,sv_order_color,sv_superimpose_image,sv_customer_pincode,sv_product_with_package;
    public static int pos;
    public WishListAdapter(Context context, ArrayList<categoryitems> item) {
        this.item1 = item;
        this.context = context;
    }
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_wishlist_item, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(WishListAdapter.ViewHolder viewHolder, final int i) {
        pos=i;
        viewHolder.tv_name.setText(item1.get(i).getproduct_name());
        viewHolder.price.setText(item1.get(i).getproduct_price());
        //p=new ProgressDialog(context);
//        Glide.with(context).load(item1.get(i).getproduct_image())
//                .thumbnail(0.5f)
//                .crossFade()
//                .skipMemoryCache(true)
//              //  .error(R.drawable.placeholder)
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
        imgloader.displayImage(item1.get(i).getproduct_image(), viewHolder.img, options);
        Log.d("Glideimages",item1.get(i).getproduct_image());
        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                DisplayItemsFragment.sc_spc_id =item1.get(i).getid();
            }
        });
        //for delete item from wishlist
        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv_id = item1.get(i).getid();
              //  Toast.makeText(g,i,Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                alertDialog.setTitle("Delete this item?");
                alertDialog.setMessage("Are you sure you want to delete this?");
                alertDialog.setPositiveButton(
                        "yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            new Delete_wishlistproduct().execute();
                              //  item1.remove(i);
                               // notifyDataSetChanged();
                            }
                        }
                );
                alertDialog.setNegativeButton(
                        "no",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                            }
                        }
                );
               alertDialog.show();
            }
        });
        viewHolder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos=i;
                estimated_days="";
                sv_id="";
                sv_product_id="";
                sv_cartname="";
                sv_cartprice="";
                sv_cartsrc="";
                sv_product_shipping_cost="";
                sv_product_qty="";
                sv_id = item1.get(i).getid();
                sv_product_id = item1.get(i).getproduct_id();
                sv_cartname = item1.get(i).getproduct_name();
                sv_cartprice = item1.get(i).getproduct_price();
                sv_cartsrc = item1.get(i).getproduct_image();
                sv_product_shipping_cost = item1.get(i).getproduct_shipping_cost();
                sv_product_qty = item1.get(i).getproduct_quantity();
                 //diaglog for insert user input qty, picode
                Intent i=new Intent(view.getContext(),Activity_Uploadimage.class);
                view.getContext().startActivity(i);
               }
          });
    }
    @Override
    public int getItemCount() {
        return item1.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView tv_name,price;
        private ImageView img_edit,img,img_delete;
        private ItemClickListener clickListener;
        LinearLayout qtylayout;
        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView)view.findViewById(R.id.txv_name);
            price = (TextView)view.findViewById(R.id.txv_price);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);
            img = (ImageView) view.findViewById(R.id.image_wishlist);
            img_edit = (ImageView) view.findViewById(R.id.img_cart);
            img_edit.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
            qtylayout = (LinearLayout) view.findViewById(R.id.qtylayout);
            qtylayout.setVisibility(View.GONE);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
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
    class Delete_wishlistproduct extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("sv_id",sv_id);
            Log.d("Params:",params.toString());
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"Delete_wishlist", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Json response:",s);
            try {
                Log.d("posposition", Integer.toString(pos));
                item1.remove(pos);
                if (item1.size() == 0) {
                    recyclerView.setAdapter(null);
                    cardview.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                Intent i1=new Intent(context,Activity_WishList.class);
                context.startActivity(i1);
                AppCompatActivity activity = (AppCompatActivity) context.getApplicationContext();
                activity.finish();

                notifyDataSetChanged();
            }
            catch (Exception e){e.getMessage();}
        }
    }
}
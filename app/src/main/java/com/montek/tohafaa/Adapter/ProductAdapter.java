package com.montek.tohafaa.Adapter;
import android.app.ProgressDialog;
import android.content.*;
import android.graphics.*;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.montek.tohafaa.*;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.TabFragments.DisplayItemsFragment;
import com.montek.tohafaa.extra.GlobalVariable;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.interfaces.ItemClickListener;
import com.montek.tohafaa.model.categoryitems;
import com.nostra13.universalimageloader.core.*;
import java.util.*;
import static com.montek.tohafaa.LoginActivity.p;
import static com.montek.tohafaa.ProductDetails.FlagForProductList;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<categoryitems> item;
    private Context context;
    public static int whishlistimage=0;
    public static ImageLoader imgloader  = ImageLoader.getInstance();;
    String product_id,product_name,product_price,product_image,product_shipping_cost,Quantity;
    public ProductAdapter(Context context, ArrayList<categoryitems> item) {
        this.item = item;
        this.context = context;
    }
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final ProductAdapter.ViewHolder viewHolder, final int i) {
            p=new ProgressDialog(context);
            viewHolder.tv_name.setText(item.get(i).getproduct_name());
            if (item.get(i).getproduct_wishlistid() != "0") {
                viewHolder.ImageViewWishlist.setImageResource(R.drawable.ic_favorite_black_24dp);
            }
        imgloader.init(ImageLoaderConfiguration.createDefault(context));
//            Glide.with(context).load(item.get(i).getproduct_image())
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .skipMemoryCache(true)
//                   // .error(R.drawable.placeholder)
//                    .into(viewHolder.img);

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
        imgloader.displayImage(item.get(i).getproduct_image(), viewHolder.img, options);
        viewHolder.dprice.setText(item.get(i).getpo_discount()+"% OFF");
        String[] parts = item.get(i).getproduct_price().split("\\.");
        float x=0;
        if(item.get(i).getpo_discount()!="0" ) {
            x =  (1 - (Float.parseFloat(item.get(i).getpo_discount()) / 100));
                viewHolder.grid_item_price1.setText("₹"+parts[0]);
                viewHolder.price.setText("₹"+String.valueOf(Float.parseFloat(parts[0])*x));
        }
        else {
            viewHolder.grid_item_price1.setVisibility(View.GONE);
            viewHolder.dprice.setTextColor(Color.WHITE);
              viewHolder.price.setText("₹"+parts[0]);
        }
      //  viewHolder.price.setText(x);
        viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int pos, boolean isLongClick) {
                    notifyDataSetChanged();
                    Intent val = new Intent(v.getContext(), ProductDetails.class);
                    val.putExtra("product_id", item.get(pos).getproduct_id());
                    Log.d("product_idrecada", item.get(pos).getproduct_id());
                    val.putExtra("product_name", item.get(pos).getproduct_name());
                    val.putExtra("product_description", item.get(pos).getproduct_description());
                    val.putExtra("product_specification", item.get(pos).getproduct_specification());
                    val.putExtra("product_model", item.get(pos).getproduct_model());
                    val.putExtra("product_code", item.get(pos).getproduct_code());
                    val.putExtra("product_quantity", item.get(pos).getproduct_quantity());
                    val.putExtra("product_price", item.get(pos).getproduct_price());
                    val.putExtra("product_sku", item.get(pos).getproduct_sku());
                    val.putExtra("product_substract_stock", item.get(pos).getproduct_substract_stock());
                    val.putExtra("product_free_shipping", item.get(pos).getproduct_free_shipping());
                    val.putExtra("product_free_shipping_limit", item.get(pos).getproduct_free_shipping_limit());
                    val.putExtra("product_shipping_cost", item.get(pos).getproduct_shipping_cost());
                    val.putExtra("product_tax_status", item.get(pos).getproduct_tax_status());
                    val.putExtra("product_tax", item.get(pos).getproduct_tax());
                    val.putExtra("product_category_id", item.get(pos).getproduct_category_id());
                    val.putExtra("product_subcategory_id", item.get(pos).getproduct_subcategory_id());
                    val.putExtra("product_sub2category_id", item.get(pos).getproduct_sub2category_id());
                    val.putExtra("product_sub3category_id", item.get(pos).getproduct_sub3category_id());
                    val.putExtra("product_sub4category_id", item.get(pos).getproduct_sub4category_id());
                    val.putExtra("product_spc_id", item.get(pos).getproduct_spc_id());
                    val.putExtra("product_image", item.get(pos).getproduct_image());
                    val.putExtra("product_offer", item.get(pos).getproduct_offer());
                    val.putExtra("product_cod", item.get(pos).getproduct_cod());
                    val.putExtra("product_brand_id", item.get(pos).getproduct_brand_id());
                    val.putExtra("product_superimpose_image", item.get(pos).getproduct_superimpose_image());
                    val.putExtra("product_size", item.get(pos).getproduct_size());
                    val.putExtra("product_color", item.get(pos).getproduct_color());
                    val.putExtra("product_with_package", item.get(pos).getproduct_with_package());
                    val.putExtra("product_name", item.get(pos).getproduct_name());
                    val.putExtra("product_wishlistid", item.get(pos).getproduct_wishlistid());
                    val.putExtra("po_discount", item.get(pos).getpo_discount());
                    v.getContext().startActivity(val);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.finish();
                }
            });
            //Set click action for wishlist
            viewHolder.ImageViewWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product_id = item.get(i).getproduct_id();
                    product_name = item.get(i).getproduct_name();
                    // product_price = item.get(i).getproduct_price();
                    String[] parts = item.get(i).getproduct_price().split("\\.");
                    product_price=(parts[0]);
                    product_image = item.get(i).getproduct_image();
                    product_shipping_cost = item.get(i).getproduct_shipping_cost();
                    Quantity = item.get(i).getproduct_quantity();
                    if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                        if (viewHolder.ImageViewWishlist.getDrawable().getConstantState() == context.getResources().getDrawable( R.drawable.ic_favorite_black_24dp).getConstantState())
                        {   whishlistimage=1;
                            viewHolder.ImageViewWishlist.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        }
                        else
                        {    whishlistimage=0;
                             viewHolder.ImageViewWishlist.setImageResource(R.drawable.ic_favorite_black_24dp);
                        }
                        notifyDataSetChanged();
                        new AddToWishlist().execute();
                    }
                    else
                    {
                        notifyDataSetChanged();
                        Intent intent = new Intent(v.getContext(),LoginActivity.class);
                        intent.putExtra("sv_key","my_wishlist");
                        intent.putExtra("id",product_id);
                        intent.putExtra("name",product_name);
                        intent.putExtra("price",product_price);
                        intent.putExtra("src", product_image);
                        intent.putExtra("product_shipping_cost",product_shipping_cost);
                        intent.putExtra("max_quantity",Quantity);
                        v.getContext().startActivity(intent);
                  }
                }
            });
    }
    @Override
    public int getItemCount() {
        return item.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView tv_name,price,grid_item_price1,dprice;
        private ImageView img,ImageViewWishlist;
        private ItemClickListener clickListener;
        LinearLayout layout;
        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView)view.findViewById(R.id.grid_item_title);
            img = (ImageView) view.findViewById(R.id.grid_item_image);
            price = (TextView) view.findViewById(R.id.grid_item_price);
            grid_item_price1 = (TextView) view.findViewById(R.id.grid_item_price1);
            grid_item_price1.setVisibility(View.VISIBLE);
            dprice=(TextView)view.findViewById(R.id.dprice);
            dprice.setVisibility(View.VISIBLE);
            grid_item_price1.setPaintFlags(grid_item_price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ImageViewWishlist = (ImageView) view.findViewById(R.id.img_wishlist);
            layout = (LinearLayout) view.findViewById(R.id.layout);
            if(FlagForProductList.equalsIgnoreCase("1"))
            {
                ImageViewWishlist.setVisibility(View.GONE);
            }
            else
            {
                ImageViewWishlist.setVisibility(View.VISIBLE);
            }
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
    public void setFilter(ArrayList<categoryitems> countryModels) {
        item = new ArrayList<>();
        item.addAll(countryModels);
        notifyDataSetChanged();
    }
    class AddToWishlist extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("sv_user_id", GlobalVariable.user_id);
            params.put("sv_key","my_wishlist");
            params.put("id",product_id);
            params.put("name",product_name);
            params.put("price",product_price);
            params.put("src",product_image);
            params.put("product_shipping_cost",product_shipping_cost);
            params.put("max_quantity",Quantity);
            Log.d("Params:",params.toString());
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"addtowishlist", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // Activity_OrdersList.dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          //  p.dismiss();
            Log.d("Json response:",s);
            Toast.makeText(context,s, Toast.LENGTH_LONG).show();
            if(whishlistimage==1) {
                Intent i = new Intent(context, DisplayItemsFragment.class);
                context.startActivity(i);
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.finish();
            }
            new DashBordActivity.Retrive_mywishlist().execute();
        }
    }
}
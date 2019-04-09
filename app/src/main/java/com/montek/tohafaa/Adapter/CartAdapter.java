package com.montek.tohafaa.Adapter;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.*;
//import com.bumptech.glide.Glide;
import com.montek.tohafaa.*;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.GlobalVariable;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.model.CartModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.*;
import java.util.*;
import static com.montek.tohafaa.Activity_CartList.*;
import static com.montek.tohafaa.Adapter.ProductAdapter.imgloader;

public class CartAdapter extends ArrayAdapter<CartModel>  {
    Context mCtx;
    int id;
    public static int listLayoutRes,shippcost=0,subtotal=0,ordertotal=0;
     List<CartModel> CartList;
    SQLiteDatabase mDatabase;
    ImageView img,img_edit,img_delete;
    TextView txv_qty,tv_name,price;
    String[] QuantityArray;
    String val,product_price;
    String product_id;
    public CartAdapter(Context mCtx, int listLayoutRes, List<CartModel> CartList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, CartList);
        this.mCtx = mCtx;
        CartAdapter.listLayoutRes = listLayoutRes;
        this.CartList = CartList;
        this.mDatabase = mDatabase;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);
       try {
           final CartModel cart = CartList.get(position);
           tv_name = (TextView) view.findViewById(R.id.txv_name);
           price = (TextView) view.findViewById(R.id.txv_price);
           img_delete = (ImageView) view.findViewById(R.id.img_delete);
           img = (ImageView) view.findViewById(R.id.image_wishlist);
           img_edit = (ImageView) view.findViewById(R.id.img_cart);
           txv_qty = (TextView) view.findViewById(R.id.txv_qty);
           tv_name.setText(cart.getName());
           price.setText(cart.getprice());
           txv_qty.setText(cart.getqty());
           shippcost = 0;
           subtotal = 0;
           ordertotal = 0;
           for (int i = 0; i < CartList.size(); i++) {
               shippcost = shippcost + Integer.parseInt(CartList.get(i).getproduct_shipping_cost());
               subtotal = subtotal + Integer.parseInt(CartList.get(i).getsubtotal());
               ordertotal = shippcost + subtotal;
           }
           Log.d("ordertotal", Integer.toString(ordertotal));
           txv_OrderTotal1.setText(Integer.toString(ordertotal));
           txv_cartsubtotal1.setText(Integer.toString(subtotal));
           txv_ShippingandHandling1.setText(Integer.toString(shippcost));
           product_id = cart.getproduct_id();
//           Glide.with(getContext()).load(cart.getsrc())
//                   .thumbnail(0.5f)
//                   .crossFade()
//                   .skipMemoryCache(true)
//                   .into(img);
           imgloader.init(ImageLoaderConfiguration.createDefault(getContext()));
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
           imgloader.displayImage(cart.getsrc(), img, options);

           Log.d("cart.getsrc()", cart.getsrc());
           //adding a clicklistener to button
           img_edit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   id = Integer.parseInt(cart.getId());
                   product_price = cart.getprice();
                   if (cart.getproduct_quantity().intern() != "0") {
                       QuantityArray = new String[Integer.parseInt(cart.getproduct_quantity())];
                       for (int i = 0; i < Integer.parseInt(cart.getproduct_quantity()); i++) {
                           QuantityArray[i] = Integer.toString(i + 1);
                       }
                       try {
                           dialog_qty(id);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   } else {
                       new Retrive_qtyofproduct().execute();
                   }
               }
           });
           //the delete operation
           img_delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                   builder.setTitle("Are you sure?");
                   builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           String sql = "DELETE FROM CartTable WHERE cid = ?";
                           mDatabase.execSQL(sql, new Integer[]{Integer.parseInt(cart.getId())});
                           try {
                               showCartFromDatabase();
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       }
                   });
                   builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                       }
                   });
                   AlertDialog dialog = builder.create();
                   dialog.show();
               }
           });
       }catch (Exception e){}
        return view;
    }
    void dialog_qty(final int id) throws Exception
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle("Select Quantity");
        builder.setItems(QuantityArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(getContext(),QuantityArray[which] + " Selected", Toast.LENGTH_LONG).show();
                val = QuantityArray[which];
                if(Integer.parseInt(val)>100)
                {
                    Toast.makeText(getContext(),"sorry,Quantity limit cannot exceed more than 100!!",Toast.LENGTH_LONG).show();
                }
                else {
                    String subtotalstr = Float.toString(Float.parseFloat(product_price) * Float.parseFloat(val));
                    String[] parts = subtotalstr.split("\\.");
                    String subtotalval = parts[0];
                    Log.d("subtotalval", subtotalval);
                    String sql = "UPDATE CartTable \n" +
                            "SET qty = ? ,subtotal= ?  \n" +
                            "WHERE cid = ?;\n";
                    mDatabase.execSQL(sql, new String[]{val, subtotalval, String.valueOf(id)});
                    Toast.makeText(mCtx, "Quantity updated successfully", Toast.LENGTH_SHORT).show();
                    // reloadCartFromDatabase();
                    try {
                        showCartFromDatabase();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void showCartFromDatabase() throws Exception {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM CartTable", null);
        if(cursor.getCount()==0)
        {
            Activity_CartList.Count=0;
            txv_OrderTotal1.setText("0.00");
            txv_cartsubtotal1.setText("0.00");
            txv_ShippingandHandling1.setText("0.00");
            cardview.setVisibility(View.VISIBLE);
            layoutbuttons.setVisibility(View.GONE);
            carttatallayout.setVisibility(View.GONE);
        }
       resultSet = new JSONArray();  //if the cursor has some data
        CartList.clear();
            if (cursor.moveToFirst()) {
            do {

                Log.d("Count", String.valueOf(cursor.getCount()));
                CartModel CartModel = new CartModel();
                CartModel.setid(cursor.getString(0));
                CartModel.setproduct_id(cursor.getString(1));
                CartModel.setproduct_quantity(cursor.getString(2));
                CartModel.setqty(cursor.getString(3));
                CartModel.setdiscount(cursor.getString(4));
                CartModel.setprice(cursor.getString(5));
                CartModel.setName(cursor.getString(6));
                CartModel.setsrc(cursor.getString(7));
                CartModel.setproduct_shipping_cost(cursor.getString(8));
                CartModel.setoptions(cursor.getString(9));
                CartModel.setorder_size_id(cursor.getString(10));
                CartModel.setorder_color_id(cursor.getString(11));
                CartModel.setorder_size(cursor.getString(12));
                CartModel.setorder_color(cursor.getString(13));
                CartModel.setsuperimpose_image(cursor.getString(14));
                CartModel.setsuperimpose_text(cursor.getString(15));
                CartModel.setcustomer_pincode(cursor.getString(16));
                CartModel.setproduct_with_package(cursor.getString(17));
                CartModel.setrowid(cursor.getString(0));
                CartModel.setsubtotal(cursor.getString(19));
                CartModel.setestimated_days(cursor.getString(20));

                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();
                for( int i=0 ;  i< totalColumn ; i++ )
                {
                    if( cursor.getColumnName(i) != null )
                    {
                        try
                        {
                            if( cursor.getString(i) != null )
                            {
                                if(i==3 || i==5 ||i==6 ||i==4 || i==7 ||i==8 || i==16 || i==18 || i==19) {
                                    rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                                }
                                else if(i==1)
                                {
                                    rowObject.put("id",cursor.getString(i));
                                }
                                else if(i==14 || i==15 )
                                {
                                    if( cursor.getString(i).length()!=0) {
                                        Log.d("cursor.getString(i)", String.valueOf(cursor.getString(14).length()));
                                        rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                                    }
                                }
                            }
                            else
                            {
                                if(i==3 || i==5 ||i==6 ||i==4 || i==7 ||i==8 || i==16 || i==18 || i==19) {
                                    rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                                }
                                else if(i==1)
                                {
                                    rowObject.put("id",cursor.getString(i));
                                }
                                else if(i==14 || i==15 )
                                {
                                    if( cursor.getString(i).length()!=0) {
                                        Log.d("cursor.getString(i)", String.valueOf(cursor.getString(14).length()));
                                        rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                                    }
                                }
                            }
                        }
                        catch( Exception e )
                        {
                            Log.d("TAG_NAME", e.getMessage()  );
                        }
                    }
                }
                resultSet.put(rowObject);
                //pushing each record in the employee list
                CartList.add(new CartModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16),
                        cursor.getString(17),
                        cursor.getString(0),
                        cursor.getString(19),
                        cursor.getString(20)
                ));
            } while (cursor.moveToNext());
            Log.d("Cartlist Final:",resultSet.toString());
        }
        cursor.close();
        notifyDataSetChanged();
    }
    class Retrive_qtyofproduct extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("product_id",product_id);
            Log.d("Params:",params.toString());
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category+"Retrive_qtyofproduct", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String  product_quantity="0";
            Log.d("Json response:",s);
            JSONArray jsonArray = null;
            try {
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                         product_quantity  = jsonObject.getString("product_quantity");
                        Log.d("product_quantity ",product_quantity );
                    }
                QuantityArray = new String[Integer.parseInt(product_quantity)];
                for(int i=0;i<Integer.parseInt(product_quantity);i++)
                {
                    QuantityArray[i] = Integer.toString(i+1);
                }
                dialog_qty(id);
            }
            catch (Exception e) {  e.printStackTrace(); }
        }
    }
}

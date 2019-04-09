package com.montek.tohafaa.Adapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.*;
import android.view.*;
import android.widget.*;
//import com.bumptech.glide.Glide;
import com.montek.tohafaa.Activity_OrdersList;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.R;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.model.CartModel;
import com.montek.tohafaa.model.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.*;
import java.text.SimpleDateFormat;
import java.util.*;
import static com.montek.tohafaa.Activity_CartList.*;
import static com.montek.tohafaa.Activity_CheckoutPage.*;
import static com.montek.tohafaa.Adapter.ProductAdapter.imgloader;
import static com.montek.tohafaa.DashBordActivity.dialog;
import static com.montek.tohafaa.LoginActivity.p;
import static com.montek.tohafaa.RegisterActivity.getIndex;
public class CartCheckoutAdapter extends ArrayAdapter<CartModel> {
    Context mCtx;
    int id;
    public static int listLayoutRes;
    List<CartModel> CartList;
    SQLiteDatabase mDatabase;
    TextView txv_qty;
    ImageView img;
    ImageView img_edit;
    ImageView img_delete;
    TextView  txv_state,txv_city,txv_totalprice,txv_delivery,tv_name,txv_price,txv_open;
    String[] QuantityArray;
    public static Spinner qty_spinner;
    public static TextInputLayout taptoopeninputlayout;
     String country_id, state_id, city_id;
    String product_id,Validationdone="0",val,qty,discount,price,name,product_shipping_cost,order_size_id,order_color_id,order_size,order_color,superimpose_image,superimpose_text,product_with_package
            ,total,delivery_date,shipping_firstname,shipping_lastname, shipping_contact, shipping_email, order_product_address,
             shipping_pincode;

    public CartCheckoutAdapter(Context mCtx, int listLayoutRes, List<CartModel> CartList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, CartList);
        this.mCtx = mCtx;
        CartCheckoutAdapter.listLayoutRes = listLayoutRes;
        this.CartList = CartList;
        this.mDatabase = mDatabase;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);
      //  final CartModel cart = CartList.get(position);
      try {
          p = new ProgressDialog(getContext());
          tv_name = (TextView) view.findViewById(R.id.txv_name);
          txv_price = (TextView) view.findViewById(R.id.txv_price);
          img_delete = (ImageView) view.findViewById(R.id.img_delete);
          img = (ImageView) view.findViewById(R.id.image_wishlist);
          img_edit = (ImageView) view.findViewById(R.id.img_cart);
          // qty_spinner = (Spinner) view.findViewById(R.id.spinner1);
          txv_open = (TextView) view.findViewById(R.id.txv_open);
          if (shipping_address_same_as_billing_address.equalsIgnoreCase("1")) {
              txv_open.setVisibility(View.GONE);
          } else {
              txv_open.setVisibility(View.VISIBLE);
          }
          txv_delivery = (TextView) view.findViewById(R.id.txv_delivery);
          int estimated_days = Integer.parseInt(CartList.get(position).getestimated_days());
          txv_totalprice = (TextView) view.findViewById(R.id.txv_totalprice);
          txv_qty = (TextView) view.findViewById(R.id.txv_qty);
          //to find date day from estimated days
          final Calendar calendar = Calendar.getInstance();
          calendar.add(Calendar.DATE, estimated_days);
          SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); //Date and time
          String currentDate = sdf.format(calendar.getTime());
          //for day
          SimpleDateFormat sdf_ = new SimpleDateFormat("EE");
          Date date = new Date();
          String dayName = sdf_.format(date);
          txv_delivery.setText("Delivery by" + dayName + "," + currentDate);
          txv_qty.setText(CartList.get(position).getqty());
          String total1 = Float.toString(Float.parseFloat(CartList.get(position).getqty()) * Float.parseFloat(CartList.get(position).getprice()));
          Log.d("totalofproductval", total1);
          txv_totalprice.setText(total1);
          taptoopeninputlayout = (TextInputLayout) view.findViewById(R.id.taptoopeninputlayout);
          tv_name.setText(CartList.get(position).getName());
          tv_name.setTextColor(Color.BLACK);
          for (int x = 0; x < CheckoutTablearray.size(); x++) {
              if (CheckoutTablearray.get(x).equals(CartList.get(position).getName())) {
                  Log.d("matchingvalues", CartList.get(position).getName() + " " + CheckoutTablearray.get(x));
                  tv_name.setTextColor(Color.RED);
              }
          }
          txv_price.setText(CartList.get(position).getprice());
          //values to set
//          Glide.with(getContext()).load(CartList.get(position).getsrc())
//                  .thumbnail(0.5f)
//                  .crossFade()
//                  .skipMemoryCache(true)
//                  //.error(R.drawable.placeholder)
//                  .into(img);
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
          imgloader.displayImage(CartList.get(position).getsrc(), img, options);

          Log.d("cart.getsrc()", CartList.get(position).getsrc());
          //adding a clicklistener to button
          img_edit.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  id = Integer.parseInt(CartList.get(position).getId());
                  price = CartList.get(position).getprice();
                  product_id = CartList.get(position).getproduct_id();
                  if ("0" != CartList.get(position).getproduct_quantity().intern()) {

                      try {
                          dialog_qty(CartList.get(position).getproduct_quantity());
                      } catch (Exception e) { e.printStackTrace();}
                  } else {
                      new Retrive_qtyofproduct().execute();
                  }
              }
          });
          txv_open.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  //  id= Integer.parseInt(cart.getId());
                  city_id = "";
                  state_id = "";
                  country_id = "";
                  delivery_date = ((new SimpleDateFormat("yyyy-MM-dd"))).format(calendar.getTime());
                  product_id = CartList.get(position).getproduct_id();
                  discount = CartList.get(position).getdiscount();
                  price = CartList.get(position).getprice();
                  name = CartList.get(position).getName();
                  qty = CartList.get(position).getqty();
                  product_shipping_cost = CartList.get(position).getproduct_shipping_cost();
                  order_size_id = CartList.get(position).getorder_size_id();
                  order_color_id = CartList.get(position).getorder_color_id();
                  order_color = CartList.get(position).getorder_color();
                  order_size = CartList.get(position).getorder_size();
                  superimpose_image = CartList.get(position).getsuperimpose_image();
                  superimpose_text = CartList.get(position).getsuperimpose_text();
                  product_with_package = CartList.get(position).getproduct_with_package();
                  total = txv_totalprice.getText().toString().trim();
                  dialog = new Dialog(view.getContext());
                  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                  dialog.setContentView(R.layout.activity_product_shippingaddress);
                  TextView  txv_productname = (TextView) dialog.findViewById(R.id.txv_productname);
                  txv_productname.setVisibility(View.VISIBLE);
                  txv_productname.setText(name);
                  edtx_firstname = (EditText) dialog.findViewById(R.id.edtx_firstname);
                  edtx_lastname = (EditText) dialog.findViewById(R.id.edtx_lastname);
                  edtx_email = (EditText) dialog.findViewById(R.id.edtx_email);
                  edtx_mobile = (EditText) dialog.findViewById(R.id.edtx_mobile);
                  edtx_addressline1 = (EditText) dialog.findViewById(R.id.edtx_addressline1);
                  edtx_addressline2 = (EditText) dialog.findViewById(R.id.edtx_addressline2);
                  address2inputlayout.setVisibility(View.GONE);
                  edtx_pincode = (EditText) dialog.findViewById(R.id.edtx_pincode);
                  edtx_pincode.setText(CartList.get(position).getcustomer_pincode());
                  edtx_pincode.setEnabled(false);
                  edtx_pincode.setTextColor(getContext().getResources().getColor(android.R.color.black));
                  firstinputlayout = (TextInputLayout) dialog.findViewById(R.id.firstinputlayout);
                  lastinputlayout = (TextInputLayout) dialog.findViewById(R.id.lastinputlayout);
                  mobileinputlayout = (TextInputLayout) dialog.findViewById(R.id.mobileinputlayout);
                  address1inputlayout = (TextInputLayout) dialog.findViewById(R.id.address1inputlayout);
                  address1inputlayout.setHint("Enter Your Address ");
                  address2inputlayout = (TextInputLayout) dialog.findViewById(R.id.address2inputlayout);
                  address2inputlayout.setVisibility(View.GONE);
                  emailinputlayout = (TextInputLayout) dialog.findViewById(R.id.emailinputlayout);
                  emailinputlayout.setHint("Shipping Email");
                  pincodeinputlayout = (TextInputLayout) dialog.findViewById(R.id.pincodeinputlayout);
                  TextView txv_shippingaddress = (TextView) dialog.findViewById(R.id.txv_shippingaddress);
                  txv_shippingaddress.setVisibility(View.VISIBLE);
                  View viewline = dialog.findViewById(R.id.view);
                  viewline.setVisibility(View.VISIBLE);
                  Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
                  btn_submit.setVisibility(View.VISIBLE);
                  spin_City_District_Town.setVisibility(View.GONE);
                  spin_State.setVisibility(View.GONE);
                  txv_state = (TextView) dialog.findViewById(R.id.txv_state);
                  txv_city = (TextView) dialog.findViewById(R.id.txv_city);
                  txv_city.setVisibility(View.GONE);
                  txv_state.setVisibility(View.GONE);
                  cursor = mDatabase.rawQuery("select * from CheckoutTable where product_id=?", new String[]{product_id});
                  if (cursor.getCount() > 0) {
                      cursor.moveToFirst();
                      do {
                          String shipping_firstname = cursor.getString(cursor.getColumnIndex("shipping_firstname"));
                          String shipping_lastname = cursor.getString(cursor.getColumnIndex("shipping_lastname"));
                          String shipping_contact = cursor.getString(cursor.getColumnIndex("shipping_contact"));
                          String shipping_email = cursor.getString(cursor.getColumnIndex("shipping_email"));
                          String order_product_address = cursor.getString(cursor.getColumnIndex("order_product_address"));
                          city_id = cursor.getString(cursor.getColumnIndex("shipping_city"));
                          state_id = cursor.getString(cursor.getColumnIndex("shipping_state"));
                          country_id = cursor.getString(cursor.getColumnIndex("shipping_country"));
                          Log.d("country_id", country_id);
                          Log.d("state_id", state_id);
                          Log.d("city_id", city_id);
                          String shipping_pincode = cursor.getString(cursor.getColumnIndex("shipping_pincode"));
                          edtx_lastname.setText(shipping_lastname);
                          edtx_firstname.setText(shipping_firstname);
                          edtx_email.setText(shipping_email);
                          edtx_mobile.setText(shipping_contact);
                          edtx_pincode.setText(shipping_pincode);
                          edtx_addressline1.setText(order_product_address);
                      } while (cursor.moveToNext());
                  }
                  //for spinner
                  spin_country = (Spinner) dialog.findViewById(R.id.spin_country);
                  spin_State = (Spinner) dialog.findViewById(R.id.spin_State);
                  spin_City_District_Town = (Spinner) dialog.findViewById(R.id.spin_City_District_Town);
                  spin_State.setVisibility(View.GONE);
                  spin_City_District_Town.setVisibility(View.GONE);
                  new RetriveCountry().execute();
                  spin_City_District_Town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                      @Override
                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                          Item ii = (Item) parent.getSelectedItem();
                          city_id = Integer.toString(ii.getid());
                      }
                      @Override
                      public void onNothingSelected(AdapterView<?> parent) {
                      }
                  });
                  spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                      @Override
                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                          Item ii = (Item) parent.getSelectedItem();
                          country_id = Integer.toString(ii.getid());
                          Log.d("Countryid", country_id);
                          // shipping_pincode = ii.getphonecode();
                          if (country_id.equalsIgnoreCase("0")) {
                              spin_City_District_Town.setVisibility(View.GONE);
                              spin_State.setVisibility(View.GONE);
                              txv_city.setVisibility(View.GONE);
                              txv_state.setVisibility(View.GONE);
                          } else {
                              spin_State.setVisibility(View.VISIBLE);
                              txv_state.setVisibility(View.VISIBLE);
                              new RetriveState().execute();
                          }
                      }
                      @Override
                      public void onNothingSelected(AdapterView<?> parent) {
                      }
                  });
                  spin_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                      @Override
                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                          Item ii = (Item) parent.getSelectedItem();
                          state_id = Integer.toString(ii.getid());
                          Log.d("state_id", state_id);

                          if (state_id.equalsIgnoreCase("0")) {
                              spin_City_District_Town.setVisibility(View.GONE);
                              txv_city.setVisibility(View.GONE);

                          } else {
                              spin_City_District_Town.setVisibility(View.VISIBLE);
                              new Retrivecity().execute();
                          }
                      }

                      @Override
                      public void onNothingSelected(AdapterView<?> parent) {
                      }
                  });
                  btn_submit.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          shipping_lastname = edtx_lastname.getText().toString().trim();
                          shipping_firstname = edtx_firstname.getText().toString().trim();
                          shipping_email = edtx_email.getText().toString().trim();
                          shipping_contact = edtx_mobile.getText().toString().trim();
                          shipping_pincode = edtx_pincode.getText().toString().trim();
                          order_product_address = edtx_addressline1.getText().toString().trim();
                          try {
                              signUp();
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      }
                  });
                  dialog.show();
                  Window window = dialog.getWindow();
                  assert window != null;
                  window.setLayout(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

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
                          mDatabase.execSQL(sql, new Integer[]{Integer.parseInt(CartList.get(position).getId())});

                          cursor = mDatabase.rawQuery("SELECT product_id from CheckoutTable where product_id=?", new String[]{CartList.get(position).getproduct_id()});
                          if (cursor.getCount() > 0) {
                              String sql1 = "DELETE FROM CheckoutTable WHERE product_id = ?";
                              mDatabase.execSQL(sql1, new Integer[]{Integer.parseInt(CartList.get(position).getproduct_id())});
                          }
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
    void dialog_qty(String qty) throws Exception
    {
                QuantityArray = new String[Integer.parseInt(qty)];
                for (int i = 0; i < Integer.parseInt(qty); i++) {
                    QuantityArray[i] = Integer.toString(i + 1);
                }
                Log.d("product_price",price);
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
                            String subtotalstr = Float.toString(Float.parseFloat(price) * Float.parseFloat(val));
                            String[] parts = subtotalstr.split("\\.");
                            String subtotalval = parts[0];
                            Log.d("subtotalval", subtotalval);
                            String sql = "UPDATE CartTable \n" +
                                    "SET qty = ? ,subtotal= ?  \n" +
                                    "WHERE cid = ?;\n";
                            mDatabase.execSQL(sql, new String[]{val, subtotalval, String.valueOf(id)});
                            cursor = mDatabase.rawQuery("SELECT product_id from CheckoutTable where product_id=?", new String[]{product_id});
                            if (cursor.getCount() > 0) {
                                total = txv_totalprice.getText().toString().trim();
                                String sql1 = "UPDATE CheckoutTable \n" +
                                        "SET qty = ? ,total= ?  \n" +
                                        "WHERE product_id = ?;\n";

                                mDatabase.execSQL(sql1, new String[]{val, total, product_id});
                            }
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
    //In this method we will do the create operation
    private void InsertTOCheckouttable() throws Exception {
         cursor  =  mDatabase.rawQuery("SELECT product_id from CheckoutTable where product_id=?",new String[] {product_id});
        if (cursor.getCount()>0)
        {
            Log.d("true","true");
            String sql = "UPDATE CheckoutTable  \n" +
                    "SET qty=?,total=?,shipping_firstname=?,shipping_lastname=?, shipping_contact=?, shipping_email=?, order_product_address=?, shipping_city= ?,  shipping_state= ?, shipping_country= ?, shipping_pincode= ? "
                    +" WHERE product_id = ?;";
           // String city = Integer.toString(city_id);
          //  String state = Integer.toString(state_id);
           // String country = Integer.toString(country_id);
            mDatabase.execSQL(sql, new String[]{ qty,total,shipping_firstname,shipping_lastname,shipping_contact,shipping_email,order_product_address,city_id,state_id ,country_id, shipping_pincode, product_id});
        }
        else
        {
            Log.d("false","false") ;
            if (inputsAreCorrect(product_id,qty,discount,price,name,product_shipping_cost,order_size_id,order_color_id,order_size,order_color,superimpose_image,superimpose_text,product_with_package
                    ,total,delivery_date,shipping_firstname,shipping_lastname, shipping_contact, shipping_email, order_product_address,
                    city_id, state_id,country_id, shipping_pincode, shipping_address_same_as_billing_address)) {

                String insertSQL = "INSERT INTO CheckoutTable \n" +
                        "(product_id,qty,discount,price,name,product_shipping_cost,order_size_id,order_color_id,order_size,order_color,superimpose_image,superimpose_text,product_with_package\n" +
                        "                ,total,delivery_date,shipping_firstname,shipping_lastname, shipping_contact, shipping_email, order_product_address,\n" +
                        "                 shipping_city,  shipping_state, shipping_country, shipping_pincode, shipping_status)\n" +
                        "SELECT * FROM  \n" +
                        "( SELECT  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,?)AS tmp " +
                        "WHERE  NOT EXISTS \n" +
                        "        (   SELECT  product_id FROM CheckoutTable \n" +
                        "            WHERE   product_id = '"+product_id +"');";

                Log.d("query",insertSQL);
                mDatabase.execSQL(insertSQL, new String[]{product_id,qty,discount,price,name,product_shipping_cost,order_size_id,order_color_id,order_size,order_color,superimpose_image,superimpose_text,product_with_package
                        ,total,delivery_date,shipping_firstname,shipping_lastname, shipping_contact, shipping_email, order_product_address,
                        city_id, state_id,country_id, shipping_pincode,shipping_address_same_as_billing_address});
               // Toast.makeText(getContext(), "Cart Updated Successfully!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean inputsAreCorrect(String product_id,String qty,String discount, String price,String name,String product_shipping_cost,
                                     String order_size_id,String order_color_id, String order_size,String order_color, String superimpose_image,
                                     String superimpose_text,String product_with_package, String total,String delivery_date, String shipping_firstname,
                                     String shipping_lastname, String shipping_contact,String shipping_email,String order_product_address,
                                     String shipping_city, String shipping_state,String shipping_country,String shipping_pincode,String shipping_address_same_as_billing_address ) {

        return true;
    }
    private void showCartFromDatabase() throws Exception {
        //we used rawQuery(sql, selectionargs) for fetching all the employees
         cursor = mDatabase.rawQuery("SELECT * FROM CartTable", null);
        //if the cursor has some data
        resultSet = new JSONArray();
        CartList.clear();
        Log.d("hii","hhhhhhhhhhhhhhh");
        if (cursor.moveToFirst()) {
            Log.d("nii","hhhhhhhhhhhhhhh");
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
                                // Log.d("TAG_NAME", cursor.getString(i) );
                                if(i!=2 || i!=20) {
                                    rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                                }
                            }
                            else
                            {
                                if(i!=2 || i!=20) {
                                    rowObject.put(cursor.getColumnName(i), "");
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
        //closing the cursor
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
            Activity_OrdersList.dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            String  product_quantity="0";
            Log.d("Json response:",s);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(s);
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject = jsonArray.getJSONObject(i);
                    product_quantity  = jsonObject.getString("product_quantity");
                   // Log.d("product_quantity ",product_quantity );
                }
                dialog_qty(product_quantity);
            }
            catch (Exception e) {  e.printStackTrace();  }
        }
    }
    private void signUp() throws Exception {
        boolean isValid = true;
        if (shipping_firstname.isEmpty()) {
            firstinputlayout.setError("First Name is mandatory");
            isValid = false;
        } else {
            firstinputlayout.setErrorEnabled(false);
        }
        if (shipping_lastname.isEmpty()) {
            lastinputlayout.setError("Last Name is mandatory");
            isValid = false;
        } else {
            lastinputlayout.setErrorEnabled(false);
        }
        if (shipping_email.isEmpty()) {
            emailinputlayout.setError("email is mandatory");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(shipping_email).matches()) {
            emailinputlayout.setError("email should be valid");
            isValid = false;
        } else {
            emailinputlayout.setErrorEnabled(false);
        }
        if (shipping_contact.isEmpty()) {
            mobileinputlayout.setError("mobile should be valid");
            isValid = false;
        } else if (shipping_contact.length() < 10) {
            mobileinputlayout.setError("mobile should be valid");
            isValid = false;
        } else {
            mobileinputlayout.setErrorEnabled(false);
        }
        if (shipping_pincode.isEmpty()) {
            pincodeinputlayout.setError("pincode is mandatory");
            isValid = false;
        }
        else {
            pincodeinputlayout.setErrorEnabled(false);
        }
        if (order_product_address.isEmpty()) {
            address1inputlayout.setError("Please Enter Address");
            isValid = false;
        } else {
            address1inputlayout.setErrorEnabled(false);
        }
        if (isValid) {
            if (spin_country.getSelectedItem().toString().trim().equals("Select_Country")) {
                Toast.makeText(getContext(), "Please Select Country", Toast.LENGTH_SHORT).show();
                // ((TextView)editTextlocation.getSelectedView()).setError("Please Select Location");
            } else if (spin_State.getSelectedItem().toString().trim().equals("Select_State")) {
                Toast.makeText(getContext(), "Please Select State", Toast.LENGTH_SHORT).show();
                // ((Spinner)spinner_subcategory.getSelectedView()).setError("Please Select Category");
            } else if (spin_City_District_Town.getSelectedItem().toString().trim().equals("Select_City")) {
                Toast.makeText(getContext(), "Please Select City", Toast.LENGTH_SHORT).show();
                //  ((TextView)spinner_subcategory.getSelectedView()).setError("Please Select SubCategory");
            } else {
                // new UpdateUser().execute();
                dialog.dismiss();
                InsertTOCheckouttable();
               // finalvaluesofaddress.put(GlobalVariable.user_id,new TempClass(GlobalVariable.user_id, product_id,shipping_firstname,lastname,email,mobile,order_product_address,Integer.toString(city_id),Integer.toString(state_id),Integer.toString(country_id),shipping_address_same_as_billing_address));
            }
        }
    }
    class RetriveCountry extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "country", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            Log.d("Locatio", s);
            if (s.equalsIgnoreCase(" \"No Results Found\"")) {
                Log.d("error", s);
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    ArrayList<Item> itemList = new ArrayList<>();
                    if (country_id.equalsIgnoreCase("")) {
                        itemList.add(new Item(0, "Select_Country", "000000"));
                    }
                    JSONObject obj;
                    String name;
                    for (int J = 0; J < jsonArray.length(); J++) {

                        obj = jsonArray.getJSONObject(J);
                        int id = obj.getInt("id");
                        name = obj.getString("name").trim();
                        String phonecode = obj.getString("phonecode").trim();
                        itemList.add(new Item(id, name, phonecode));
                    }
                    ArrayAdapter<Item> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, itemList);
                    // Drop down layout style - list view with radio button
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                    // attaching data adapter to spinner
                    spin_country.setAdapter(spinnerAdapter);
                    spin_country.setSelection(getIndex(spin_country, country_id));
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getMessage();
                }
            }
        }
    }
    class RetriveState extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("country_id", country_id);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "states", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Locatio", s);
            p.dismiss();
            if (s.equalsIgnoreCase(" \"No Results Found\"")) {
                Log.d("error", s);
                spin_State.setVisibility(View.GONE);
                txv_state.setVisibility(View.GONE);

            } else {
                spin_State.setVisibility(View.VISIBLE);
                txv_state.setVisibility(View.VISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    ArrayList<Item> itemList = new ArrayList<>();
                    if (state_id.equalsIgnoreCase("")) {
                        itemList.add(new Item(0, "Select_State"));
                    }
                    JSONObject obj;
                    String name;
                    for (int J = 0; J < jsonArray.length(); J++) {
                        obj = jsonArray.getJSONObject(J);
                        int id = obj.getInt("id");
                        name = obj.getString("name").trim();
                        itemList.add(new Item(id, name));
                    }
                    ArrayAdapter<Item> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, itemList);
                    // Drop down layout style - list view with radio button
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                    // attaching data adapter to spinner
                    spin_State.setAdapter(spinnerAdapter);
                    Log.d("state_id",state_id);
                    spin_State.setSelection(getIndex(spin_State,state_id));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class Retrivecity extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("state_id",state_id);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "cities", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Locatio", s);
            p.dismiss();
            if (s.equalsIgnoreCase(" \"No Results Found\"")) {
                Log.d("error", s);
                spin_City_District_Town.setVisibility(View.GONE);
                txv_city.setVisibility(View.GONE);

            } else {
                spin_City_District_Town.setVisibility(View.VISIBLE);
                txv_city.setVisibility(View.VISIBLE);
                //  progressBar.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    Log.d("city_id",city_id);
                    ArrayList<Item> itemList = new ArrayList<>();
                    if (city_id.equalsIgnoreCase("")) {
                        itemList.add(new Item(0, "Select_City"));
                    }
                    JSONObject obj;
                    String name;
                    for (int J = 0; J < jsonArray.length(); J++) {

                        obj = jsonArray.getJSONObject(J);
                        int id = obj.getInt("id");
                        name = obj.getString("name").trim();
                        itemList.add(new Item(id, name));
                    }

                    ArrayAdapter<Item> spinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, itemList);
                    // Drop down layout style - list view with radio button
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                    // attaching data adapter to spinner
                    spin_City_District_Town.setAdapter(spinnerAdapter);
                    spin_City_District_Town.setSelection(getIndex(spin_City_District_Town,city_id));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

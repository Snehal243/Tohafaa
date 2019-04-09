package com.montek.tohafaa;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.montek.tohafaa.Adapter.CartAdapter;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.*;
import com.montek.tohafaa.model.CartModel;
import org.json.*;
import java.util.*;
import static com.montek.tohafaa.Activity_WebViewofInvoice.*;
import static com.montek.tohafaa.LoginActivity.flagsqliteretrivedata;
import static com.montek.tohafaa.LoginActivity.session;
import static com.montek.tohafaa.ProductDetails.addcart;
import static com.montek.tohafaa.ProductDetails.mDatabase;

public class Activity_CartList extends AppCompatActivity implements View.OnClickListener {
    public static List<CartModel> CartList;
    public static SQLiteDatabase mDatabase;
    public static ListView listView;
    private CartAdapter adapter;
    private RecyclerView recyclerView;
    public static LinearLayout layoutbuttons,carttatallayout;
    private Button btn_continue,btn_emptycart,btn_checkout;
    public static int Count;
    public static JSONArray resultSet;
    public static CardView cardview;
    private TextView txv_emptylist,txv_listtext;
    public static  Cursor cursor;
    public static int shippcost=0,subtotal=0,ordertotal=0;
    public static TextView txv_OrderTotal1,txv_ShippingandHandling1,txv_cartsubtotal1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishactivity);
        try{UI();
        CartList = new ArrayList<>();
        actionBar = getSupportActionBar();
        Actionbar("Shopping Cart");
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDatabase = openOrCreateDatabase(ProductDetails.DATABASE_NAME, MODE_PRIVATE, null);  //opening the database
        showCartFromDatabase(); }catch (Exception e){}
    }
    private void UI() throws Exception{
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setVisibility(View.GONE);
        btn_emptycart = (Button)findViewById(R.id.btn_emptycart);
        layoutbuttons = (LinearLayout)findViewById(R.id.layoutbuttons);
        carttatallayout = (LinearLayout)findViewById(R.id.carttatallayout);
        carttatallayout.setVisibility(View.GONE);
        btn_checkout = (Button)findViewById(R.id.btn_checkout);
        btn_emptycart.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setVisibility(View.VISIBLE);
        cardview = (CardView)findViewById(R.id.cardview);
        cardview.setVisibility(View.GONE);
        cardview.setVisibility(View.GONE);
        txv_listtext = (TextView)findViewById(R.id.txv_listtext);
        txv_listtext.setText("My Cart");
        txv_emptylist = (TextView)findViewById(R.id.txv_emptylist);
        txv_emptylist.setText("You dont have any Product in your Cart");
        txv_cartsubtotal1 = (TextView)findViewById(R.id.txv_cartsubtotal);
        txv_ShippingandHandling1 = (TextView)findViewById(R.id.txv_ShippingandHandling);
        txv_OrderTotal1 = (TextView)findViewById(R.id.txv_OrderTotal);
        btn_continue = (Button)findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
        createCheckoutTable();
    }
    public static void createCheckoutTable() throws Exception {
        ProductDetails.mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS CheckoutTable (\n" +
                        "    checkoutid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                        "    product_id INTEGER NOT NULL,\n" +
                        "    qty varchar(200) NOT NULL,\n" +
                        "    discount varchar(200) NOT NULL,\n" +
                        "    price varchar(200) NOT NULL,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    product_shipping_cost varchar(200) NOT NULL,\n" +
                        "    order_size_id INTEGER NOT NULL,\n" +
                        "    order_color_id INTEGER NOT NULL,\n" +
                        "    order_size varchar(200) NOT NULL,\n" +
                        "    order_color varchar(200) NOT NULL,\n" +
                        "    superimpose_image varchar(255) NOT NULL,\n" +
                        "    superimpose_text varchar(200) NOT NULL,\n" +
                        "    product_with_package varchar(200) NOT NULL,\n" +
                        "    total varchar(200) NOT NULL,\n" +
                        "    delivery_date varchar(200) NOT NULL,\n" +
                        "    shipping_firstname varchar(200) NOT NULL,\n" +
                        "    shipping_lastname varchar(200) NOT NULL,\n" +
                        "    shipping_contact varchar(200) NOT NULL,\n" +
                        "    shipping_email varchar(200) NOT NULL,\n" +
                        "    order_product_address varchar(200) NOT NULL,\n" +
                        "    shipping_city varchar(200) NOT NULL,\n" +
                        "    shipping_state varchar(200) NOT NULL,\n" +
                        "    shipping_country varchar(200) NOT NULL,\n" +
                        "    shipping_pincode varchar(200) NOT NULL,\n" +
                        "    shipping_status varchar(200) NOT NULL \n" +
                        ");"
        );
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(), DashBordActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed()  {
        Intent i = new Intent(getBaseContext(), DashBordActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        this.finish();
    }
    private void showCartFromDatabase()  throws Exception{
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        cursor = mDatabase.rawQuery("SELECT * FROM CartTable", null);
        Count=cursor.getCount();
        CartList.clear();
        cardview.setVisibility(View.VISIBLE);
        layoutbuttons.setVisibility(View.GONE);
        resultSet = new JSONArray();
        if (cursor.moveToFirst()) {
            CartList.clear();
            cardview.setVisibility(View.VISIBLE);
            layoutbuttons.setVisibility(View.GONE);
            do {
                cardview.setVisibility(View.GONE);
                carttatallayout.setVisibility(View.VISIBLE);
                layoutbuttons.setVisibility(View.VISIBLE);
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
                    {   try
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
                        catch( Exception ignored){}
                    }
                }
                resultSet.put(rowObject);
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
        }
        cursor.close();       //closing the cursor
        adapter = new CartAdapter(this, R.layout.layout_wishlist_item, CartList, mDatabase);        //creating the adapter object
        listView.setAdapter(adapter);//adding the adapter to listview
        Log.d("result",resultSet.toString());
  }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_continue:
                Intent i1=new Intent(getBaseContext(),DashBordActivity.class);
                startActivity(i1);
                finish();
                new Delete_Cartlistproduct().execute();
                break;
            case R.id.btn_checkout:
                try{ if(Count>0) {
                      ProductDetails.product_iddetail="";
                      addcart="1";
                    deleteallrecordsofCheckoutTable();
                    new AddToCartlist().execute();
                    Intent i = new Intent(getBaseContext(), Activity_CheckoutPage.class);
                    startActivity(i);
                      finish();
                }}catch (Exception e){}
                break;
            case R.id.btn_emptycart:
                if(Count>0) {
                    try {
                        cartempty();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    public static void deleteallrecordsofcartTable() throws Exception  {
        mDatabase.execSQL(
                "delete from CartTable;"
        );
   }
    public static void deleteallrecordsofCheckoutTable() throws Exception{
        mDatabase.execSQL(
                "Delete from CheckoutTable;"
        );
    }
    class AddToCartlist extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("sv_user_id", GlobalVariable.user_id);
            params.put("sv_key","my_cart");
            params.put("cartarray",resultSet.toString());
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"addtocart", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
   public static class Delete_Cartlistproduct extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("sv_user_id", GlobalVariable.user_id);
            params.put("sv_key","my_cart");
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"Empty_Cartlist", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    public void cartempty()  throws Exception{
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Empty Cart?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                try{ new Delete_Cartlistproduct().execute();
                    deleteallrecordsofcartTable();
                    Intent i11 = new Intent(getBaseContext(), Activity_CartList.class);
                    startActivity(i11);
                    finish();
                }catch (Exception e){}
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }
}

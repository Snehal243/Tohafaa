package com.montek.tohafaa.TabFragments;
import android.app.*;
import android.content.*;
import android.os.*;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.montek.tohafaa.*;
import com.montek.tohafaa.Adapter.ListAdapterFilterSortlist;
import com.montek.tohafaa.Adapter.ProductAdapter;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.*;
import com.montek.tohafaa.model.*;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;
import org.json.*;
import java.util.*;
import static com.montek.tohafaa.Activity_WebViewofInvoice.*;
import static com.montek.tohafaa.DashBordActivity.*;
import static com.montek.tohafaa.LoginActivity.p;
import static com.montek.tohafaa.ProductDetails.*;
public class DisplayItemsFragment extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, android.support.v7.widget.SearchView.OnQueryTextListener {
    public static String Retrive_productvalue="",sc_spc_id;
    public static RecyclerView recyclerView;
    public static String[]  product_id = null,product_specification=null,product_description = null,product_model = null,product_code  = null,product_quantity = null,
    product_price  = null,product_sku  = null,product_substract_stock  = null,product_free_shipping  = null,product_free_shipping_limit  = null,
    product_shipping_cost  = null,product_tax_status  = null,product_tax  = null,product_category_id = null,product_subcategory_id  = null,
    product_sub2category_id  = null,product_sub3category_id  = null,product_sub4category_id  = null,product_spc_id  = null,
    product_image = null,product_offer  = null,product_cod  = null,product_brand_id  = null, product_superimpose_image  = null,
    product_size  = null, product_color  = null,product_with_package  = null,product_name=null,po_discount=null;
    public static LinearLayout sort,filter;
    public static CoordinatorLayout limain;
    AlertDialog alertDialog1;
    private Menu menu;
    String[] values = {"Price: Low to High","Price: High to Low"};
    ListView listView2;
    public static ArrayList<String> pf_end_price,pf_start_price;
    ListAdapterFilterSortlist SortlistAdapter;
    ArrayList<Filtersortlist> Sortinglist= new ArrayList<>();
    Button btn_ApplyFilters,btn_back,btn_Clear;
    LinearLayout no_item,pricelayout,seek_bar;
    Spinner spin1,spin2;
    Dialog dialog;
    public  static ArrayList<categoryitems> item;
    public static ProductAdapter adapter;
    TextView txv_categoryname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
        actionBar = getSupportActionBar();
       try {
           Actionbar(name);
           actionBar.setTitle(name);
           //   actionBar.setSubtitle(SubCategoryName);
           actionBar.setDisplayHomeAsUpEnabled(true);
           DashBordActivity.FragmentFlag = "1";
           iUI();
           p = new ProgressDialog(DisplayItemsFragment.this);
           GlobalVariable.Min_Price = "";
           GlobalVariable.Max_Price = "";
           new Retrive_Product().execute();
       }catch(Exception e){  }
    }
    private void iUI() throws Exception{
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        RecyclerView recyclerviewscroll = (RecyclerView) findViewById(R.id.recyclerviewscroll);
        recyclerviewscroll.setVisibility(View.GONE);
        txv_categoryname = (TextView)  findViewById(R.id.txv_categoryname);
        txv_categoryname.setVisibility(View.GONE);
        RelativeLayout li_slider = (RelativeLayout) findViewById(R.id.li_slider);
        li_slider.setVisibility(View.GONE);
        limain = (CoordinatorLayout) findViewById(R.id.limain);
        filter = (LinearLayout) findViewById(R.id.filter);
        sort = (LinearLayout) findViewById(R.id.sort);
        sort.setOnClickListener(this);
        filter.setOnClickListener(this);
        no_item = (LinearLayout)findViewById(R.id.no_item);
        no_item.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.filter:
                try {
                    ActivityAdvanceFilterdialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.sort:
                try {
                    Sortdialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        // DashBordActivity.fragment = new DisplayItemsFragment();
        Intent i=new Intent(getBaseContext(),DashBordActivity.class);
        startActivity(i);
        this.finish();
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
            case R.id.action_cart:
                if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                    Intent i1 = new Intent(getBaseContext(), Activity_CartList.class);
                    startActivity(i1);
                    finish();
                }
                else
                {
                    Intent i11 = new Intent(this, LoginActivity.class);
                    startActivity(i11);
                    finish();
                }
                return true;
            case R.id.action_wishlist:
                if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                    Intent i2 = new Intent(getBaseContext(), Activity_WishList.class);
                    startActivity(i2);
                    finish();
                }
                else
                {
                     i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
      try {  // final MenuItem account = menu.findItem(R.id.account);
          // account.setTitle(GlobalVariable.first_name);//action bar menu set account username
          items = menu.findItem(R.id.action_search);
          final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(items);
          searchView.setOnQueryTextListener(this);
          items.setVisible(true);
          final MenuItem cart = menu.findItem(R.id.action_cart);
          View cartView = MenuItemCompat.getActionView(cart);
          textCartItemCount = (TextView) cartView.findViewById(R.id.cart_badge);
          cartView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  onOptionsItemSelected(cart);
              }
          });
          final MenuItem wishlist = menu.findItem(R.id.action_wishlist);
          View wishlistView = MenuItemCompat.getActionView(wishlist);
          textwishItemCount = (TextView) wishlistView.findViewById(R.id.wishlist_badge);
          wishlistView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  onOptionsItemSelected(wishlist);
              }
          });
          if (GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
              showCartFromDatabase();
              setcartvalueonicon();
              setwishlistvalueonicon();
          }
          MenuItemCompat.setOnActionExpandListener(items,
                  new MenuItemCompat.OnActionExpandListener() {
                      @Override
                      public boolean onMenuItemActionCollapse(MenuItem items) {
                          adapter.setFilter(item);// Do something when collapsed
                          return true; // Return true to collapse action view
                      }

                      @Override
                      public boolean onMenuItemActionExpand(MenuItem item) {
                          return true; // Do something when expanded// Return true to expand action view
                      }
                  });
      }catch (Exception e){}
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<categoryitems> filteredModelList = filter(item, newText);
        adapter.setFilter(filteredModelList);
        if(filteredModelList.size()==0)
        {   no_item.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE); }
        else {
            no_item.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    private ArrayList<categoryitems> filter(ArrayList<categoryitems> models, String query) {
        query = query.toLowerCase();
        final ArrayList<categoryitems> filteredModelList = new ArrayList<>();
        for (categoryitems model : models) {
            final String text = model.getproduct_name().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
    public void ActivityAdvanceFilterdialog() throws Exception{
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_layoutfilter);
        final TextView txv_min = (TextView)dialog.findViewById(R.id.txv_min);
        final TextView txv_max = (TextView)dialog.findViewById(R.id.txv_max);
        DialogUI();
        final RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(this);
        seekBar.setRangeValues(0, 5000);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                GlobalVariable.Max_Price=maxValue.toString();
                GlobalVariable.Min_Price=minValue.toString();
                txv_min.setText(Integer.toString(minValue));
                txv_max.setText(Integer.toString(maxValue));
            }
        });
        seekBar.setNotifyWhileDragging(true);
        (seek_bar).addView(seekBar);
     //   new Retrive_AndroidFilterOptions().execute();
//        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Filtersortlist ListViewClickData = (Filtersortlist)parent.getItemAtPosition(position);
//                GlobalVariable.sortelement =ListViewClickData.getsorting_title();
//                //  Toast.makeText(getBaseContext(), ListViewClickData.getsorting_title(), Toast.LENGTH_SHORT).show();
//            }
//        });
        btn_ApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    new Retrive_Product().execute();
                    dialog.dismiss();

            }
        });
        btn_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariable.Max_Price="";
                GlobalVariable.Min_Price="";
                spin1.setSelection(0);
                spin2.setSelection(0);
            }
        });
        dialog.show();
        //Window window = dialog.getWindow();
       // window.setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }
   void  DialogUI() throws Exception
    {
        spin1 = (Spinner)dialog. findViewById(R.id.spinner1);
        spin1.setOnItemSelectedListener(this);
        spin2 = (Spinner)dialog. findViewById(R.id.spinner2);
        spin2.setOnItemSelectedListener(this);
        pricelayout = (LinearLayout)dialog.findViewById(R.id.pricelayout);
        seek_bar = (LinearLayout)dialog.findViewById(R.id.seek_bar);
      //  listView2 = (ListView)dialog.findViewById(R.id.list2);
        btn_ApplyFilters = (Button)dialog.findViewById(R.id.btn_ApplyFilters);
      //  btn_back = (Button)dialog.findViewById(R.id.btn_menu);
        btn_Clear = (Button)dialog.findViewById(R.id.btn_Clear);
    }


    public void Sortdialog() throws Exception{
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
//                    case 0:
//                        Retrive_productvalue="Retrive_SortNew";
//                        new Retrive_Product().execute();
//                        break;
                    case 0:
                     //   Toast.makeText(getContext(), "Price: Low to High ASC", Toast.LENGTH_LONG).show();
                        Retrive_productvalue="Retrive_Sortasc";
                        new Retrive_Product().execute();
                        break;
                    case 1:
                        Retrive_productvalue="Retrive_SortDESC";
                        new Retrive_Product().execute();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }
        private void initViews() throws Exception//Gridview using recyclerview
     {
        FlagForProductList="0";//relative products n dashbord product managed on flag for wishlisticon hide
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        item = prepareData();
        adapter = new ProductAdapter(this,item);
        recyclerView.setAdapter(adapter);
        DashBordActivity.items.setVisible(true);
     }
    public static ArrayList<categoryitems> prepareData () throws Exception {
        ArrayList<categoryitems> item = new ArrayList<>();
        for (int i = 0; i < product_id.length; i++) {
            categoryitems items = new categoryitems();
            if(FlagForProductList.equalsIgnoreCase("1") && product_iddetail.equalsIgnoreCase(product_id[i])) {
            }
            else {
                items.setproduct_id(product_id[i]);
                items.setproduct_name(product_name[i]);
                items.setproduct_description(product_description[i]);
                items.setproduct_specification(product_specification[i]);
                items.setproduct_model(product_model[i]);
                items.setproduct_code(product_code[i]);
                items.setproduct_quantity(product_quantity[i]);
                items.setproduct_price(product_price[i]);
                items.setproduct_sku(product_sku[i]);
                items.setproduct_substract_stock(product_substract_stock[i]);
                items.setproduct_free_shipping(product_free_shipping[i]);
                items.setproduct_free_shipping_limit(product_free_shipping_limit[i]);
                items.setproduct_shipping_cost(product_shipping_cost[i]);
                items.setproduct_tax_status(product_tax_status[i]);
                items.setproduct_tax(product_tax[i]);
                items.setproduct_category_id(product_category_id[i]);
                items.setproduct_subcategory_id(product_subcategory_id[i]);
                items.setproduct_sub2category_id(product_sub2category_id[i]);
                items.setproduct_sub3category_id(product_sub3category_id[i]);
                items.setproduct_sub4category_id(product_sub4category_id[i]);
                items.setproduct_spc_id(product_spc_id[i]);
                items.setproduct_image("https://www.tohafaa.com/" + product_image[i]);
                items.setproduct_offer(product_offer[i]);
                items.setproduct_cod(product_cod[i]);
                items.setproduct_brand_id(product_brand_id[i]);
                items.setproduct_superimpose_image(product_superimpose_image[i]);
                items.setproduct_size(product_size[i]);
                items.setproduct_color(product_color[i]);
                items.setproduct_with_package(product_with_package[i]);
                items.setproduct_name(product_name[i]);
                items.setpo_discount(po_discount[i]);
                if (svproduct_id.length != 0) {
                    for (int j = 0; j < svproduct_id.length; j++) {
                        if ((svproduct_id[j].equals(product_id[i]))) {
                            items.setproduct_wishlistid(svproduct_id[j]);
                            break;
                        } else {
                            items.setproduct_wishlistid("0");
                        }
                    }
                } else {
                    items.setproduct_wishlistid("0");
                }
                item.add(items);
            }
        }
        return item;
    }
    //spinner choice
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Toast.makeText(getBaseContext(),parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinner1)
        {
            GlobalVariable.Min_Price=pf_start_price.get(position);
            if(GlobalVariable.Min_Price.equalsIgnoreCase("Select Min Price"))
            {
              //  Toast.makeText(getContext(),"Please Select min value " ,Toast.LENGTH_LONG).show();
                GlobalVariable.Min_Price="";
            }
            if(GlobalVariable.Max_Price != null && !GlobalVariable.Max_Price.isEmpty() && !GlobalVariable.Max_Price.equals("null") && GlobalVariable.Min_Price != null && !GlobalVariable.Min_Price.isEmpty() && !GlobalVariable.Min_Price.equals("null"))
            {
                btn_ApplyFilters.setEnabled(true);
                Toast.makeText(getBaseContext(),"Please Select Proper Values",Toast.LENGTH_LONG).show();
            }
            else{
                btn_ApplyFilters.setEnabled(false);
            }
        }
        else if(spinner.getId() == R.id.spinner2)
        {
            GlobalVariable.Max_Price=pf_end_price.get(position);
            if( GlobalVariable.Max_Price.equalsIgnoreCase("Select Max Price"))
            {
                GlobalVariable.Max_Price="";
            }
            if(GlobalVariable.Max_Price != null && !GlobalVariable.Max_Price.isEmpty() && !GlobalVariable.Max_Price.equals("null") && GlobalVariable.Min_Price != null && !GlobalVariable.Min_Price.isEmpty() && !GlobalVariable.Min_Price.equals("null"))
            {
                btn_ApplyFilters.setEnabled(true);
            }
            else{
                btn_ApplyFilters.setEnabled(false);
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
//    class Retrive_AndroidFilterOptions extends AsyncTask<Void, Void, String> {
//        @Override
//        protected String doInBackground(Void... voids) {
//            //creating request handler object
//            RequestHandler requestHandler = new RequestHandler();
//            //creating request parameters
//            HashMap<String, String> params = new HashMap<>();
//            //returing the response
//            //   params.put("Approve_Post", "1");
//            return requestHandler.sendPostRequest(URLs.Retrive_AndroidFilterOptions+"Retrive_AndroidFilterOption", params);
//        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //displaying the progress bar while user registers on the server
//            Activity_OrdersList.dialog();
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            //hiding the progressbar after completion
//            Log.d("s",s);
//            p.dismiss();
//            JSONArray jsonArray = null;
//            try {
//                jsonArray = new JSONArray(s);
//                JSONObject jsonObject;
//                Filtersortlist Sort;
//                Sortinglist = new ArrayList<>();
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    jsonObject = jsonArray.getJSONObject(i);
//                    int filter_id = jsonObject.getInt("filter_id");
//                    String filter_name = jsonObject.getString("filter_name");
//                    Sort = new Filtersortlist(filter_id,filter_name ,null);
//                    Sortinglist.add(Sort);
//                }
//                SortlistAdapter = new ListAdapterFilterSortlist(getBaseContext(), R.layout.list_view_items, Sortinglist);
//                listView2.setAdapter(SortlistAdapter);
//                listView2.setItemsCanFocus(false);
//                listView2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//                new Retrive_AndroidFilterOptionsvalues().execute();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                e.getMessage();
//                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }

//    class Retrive_AndroidFilterOptionsvalues extends AsyncTask<Void, Void, String> {
//        @Override
//        protected String doInBackground(Void... voids) {
//            //creating request handler object
//            RequestHandler requestHandler = new RequestHandler();
//            //creating request parameters
//            HashMap<String, String> params = new HashMap<>();
//            //returing the response
//            return requestHandler.sendPostRequest(URLs.Retrive_AndroidFilterOptionsfromtable+GlobalVariable.sortelement, params);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Activity_OrdersList.dialog();
//            //displaying the progress bar while user registers on the server
//             }
//
//        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            //hiding the progressbar after completion
//            Log.d("s",s);
//            p.dismiss();
//            JSONArray jsonArray = null;
//            try {
//                jsonArray = new JSONArray(s);
//                JSONObject jsonObject;
//                pf_start_price=new ArrayList<>();
//                pf_end_price=new ArrayList<>();
//                pf_start_price.add("Select Min Price");
//                pf_end_price.add("Select Max Price");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    jsonObject = jsonArray.getJSONObject(i);
//                  //  int pf_id = jsonObject.getInt("pf_id");
//                    String start_price = jsonObject.getString("pf_start_price");
//                    String end_price  = jsonObject.getString("pf_end_price");
//                    pf_start_price.add(start_price);
//                    pf_end_price.add(end_price);
//                }
//
//                ArrayAdapter spinner1 = new ArrayAdapter(getBaseContext(), R.layout.spinner_item, pf_start_price);
//                // Drop down layout style - list view with radio button
//                spinner1.setDropDownViewResource(R.layout.spinner_item);
//                // attaching data adapter to spinner
//                spin1.setAdapter(spinner1);
//                ArrayAdapter spinner2 = new ArrayAdapter(getBaseContext(), R.layout.spinner_item, pf_end_price);
//                // Drop down layout style - list view with radio button
//                spinner2.setDropDownViewResource(R.layout.spinner_item);
//                // attaching data adapter to spinner
//                spin2.setAdapter(spinner2);
//            } catch (Exception e) {
//                e.printStackTrace();
//                e.getMessage();
//                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    class Retrive_Product extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();
            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
                params.put("product_spc_id", sc_spc_id);
                params.put("product_category_id", sc_category_id);
                params.put("product_subcategory_id", sc_subcategory_id);
                params.put("pf_start_price", GlobalVariable.Min_Price);
                params.put("pf_end_price", GlobalVariable.Max_Price);
                params.put("Retrive_productvalue", Retrive_productvalue);
                params.put("product_name", searchproductname);
               Log.d("url", URLs.Url_Retrive_Category+"Retrive_product");
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category+"Retrive_product", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //displaying the progress bar while user registers on the server
                  Activity_OrdersList.dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //hiding the progressbar after completion
            Log.d("Json response:",s);
            if(s.contains("No Results Found")) {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                p.dismiss();
                recyclerView.setAdapter(null);
                no_item.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                p.dismiss();
                no_item.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                //converting response to json objec
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    product_name = new String[jsonArray.length()];
                    product_id = new String[jsonArray.length()];
                    product_description = new String[jsonArray.length()];
                    product_specification= new String[jsonArray.length()];
                    product_model = new String[jsonArray.length()];
                    product_code = new String[jsonArray.length()];
                    product_quantity = new String[jsonArray.length()];
                    product_price = new String[jsonArray.length()];
                    product_sku = new String[jsonArray.length()];
                    product_substract_stock = new String[jsonArray.length()];
                    product_free_shipping = new String[jsonArray.length()];
                    product_free_shipping_limit = new String[jsonArray.length()];
                    product_shipping_cost = new String[jsonArray.length()];
                    product_tax_status = new String[jsonArray.length()];
                    product_tax = new String[jsonArray.length()];
                    product_category_id = new String[jsonArray.length()];
                    product_subcategory_id = new String[jsonArray.length()];
                    product_sub2category_id = new String[jsonArray.length()];
                    product_sub3category_id = new String[jsonArray.length()];
                    product_sub4category_id = new String[jsonArray.length()];
                    product_spc_id = new String[jsonArray.length()];
                    product_image = new String[jsonArray.length()];
                    product_offer = new String[jsonArray.length()];
                    product_cod = new String[jsonArray.length()];
                    product_brand_id = new String[jsonArray.length()];
                    product_superimpose_image = new String[jsonArray.length()];
                    product_size = new String[jsonArray.length()];
                    product_color = new String[jsonArray.length()];
                    product_with_package = new String[jsonArray.length()];
                    po_discount = new String[jsonArray.length()];
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        product_id[i] = jsonObject.getString("product_id");
                        product_name[i] = jsonObject.getString("product_name");
                        product_description[i] = jsonObject.getString("product_description");
                        product_specification[i] = jsonObject.getString("product_specification");
                        product_model[i] = jsonObject.getString("product_model");
                        product_code[i] = jsonObject.getString("product_code");
                        product_quantity[i] = jsonObject.getString("product_quantity");
                        product_price[i] = jsonObject.getString("product_price");
                        product_sku[i] = jsonObject.getString("product_sku");
                        product_substract_stock[i] = jsonObject.getString("product_substract_stock");
                        product_free_shipping[i] = jsonObject.getString("product_free_shipping");
                        product_free_shipping_limit[i] = jsonObject.getString("product_free_shipping_limit");
                        product_shipping_cost[i] = jsonObject.getString("product_shipping_cost");
                        product_tax_status[i] = jsonObject.getString("product_tax_status");
                        product_tax[i] = jsonObject.getString("product_tax");
                        product_category_id[i] = jsonObject.getString("product_category_id");
                        product_subcategory_id[i] = jsonObject.getString("product_subcategory_id");
                        product_sub2category_id[i] = jsonObject.getString("product_sub2category_id");
                        product_sub3category_id[i] = jsonObject.getString("product_sub3category_id");
                        product_sub4category_id[i] = jsonObject.getString("product_sub4category_id");
                        product_spc_id[i] = jsonObject.getString("product_spc_id");
                        product_image[i] = jsonObject.getString("product_image");
                        product_offer[i] = jsonObject.getString("product_offer");
                        product_cod[i] = jsonObject.getString("product_cod");
                        product_brand_id[i] = jsonObject.getString("product_brand_id");
                        product_superimpose_image[i] = jsonObject.getString("product_superimpose_image");
                        product_size[i] = jsonObject.getString("product_size");
                        product_color[i] = jsonObject.getString("product_color");
                        product_with_package[i] = jsonObject.getString("product_with_package");
                        if(jsonObject.isNull("po_discount"))
                        {
                            po_discount[i] = "0";
                        }else
                        {
                            po_discount[i] = jsonObject.getString("po_discount");
                        }
                        initViews();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getMessage();
                }
            }
        }
    }
}
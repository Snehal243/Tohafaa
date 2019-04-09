package com.montek.tohafaa;
import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.*;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.*;
import android.text.style.ImageSpan;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.montek.tohafaa.Adapter.*;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.TabFragments.DisplayItemsFragment;
import com.montek.tohafaa.extra.*;
import com.montek.tohafaa.model.*;
import org.json.*;
import java.util.*;
import static com.montek.tohafaa.Activity_CartList.CartList;
import static com.montek.tohafaa.CorporateClient.corpoarateclient;
import static com.montek.tohafaa.LoginActivity.*;
import static com.montek.tohafaa.ProductDetails.*;
import static com.montek.tohafaa.TabFragments.DisplayItemsFragment.*;
public  class  DashBordActivity extends AppCompatActivity implements View.OnClickListener{
    public static Dialog dialog;
    String reviewtitle,writereview,EenquiryDetails,Eemail,Emobile,Esubject, fname,femail,fmobile,faddress,fremark;
    TextInputLayout  EnquiryDetailsinputlayout,subjectinputlayout,remarkinputlayout,firstinputlayout,emailinputlayout,mobileinputlayout,address1inputlayout,reviewtitleTextInputLayout,writereviewTextInputLayout;
    RecyclerView recyclerView,Categoryscrollbar;
    public static Fragment fragment = null;
    public static DrawerLayout mDrawerLayout;
    private ListView simpleList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;
    ActionBar actionBar;
    NavigationAdapter myAdaptersc,myAdaptercategory;
    public static LinearLayout ll_dots, backnavigation, left_drawer;
    public static TextView txv_categoryname;
    public static TextView textwishItemCount, textCartItemCount, backtext, txv_tohafaa_special;
    public static String searchproductname="",categorieshorizantal="",SubCategoryName, sc_category_id="", sc_subcategory_id;
    String[] spc_id = null;
    String[] spc_name = null;
    String[] spc_image = null;
    public static String FragmentFlag = "0";
    private ViewPager vp_slider;
    public static LinearLayout includelayout, filtermenu;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<categoryitems> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    String[] banner_id = null, file_name = null;
    public static String[] sv_id = new String[0], sv_name = null, sv_price = null, svproduct_id = new String[0], sv_src = null, sv_productsheepingcost = null, sv_maxQantity = null;
    public static String name,estimated_days="0",sv_product_qty="0",sv_cartprice = null, sv_cartsrc = null, sv_cartname = null, sv_options = null, sv_customer_pincode = null, sv_rowid = null, sv_product_shipping_cost = null, sv_superimpose_image = null, sv_superimpose_text = null, sv_discount = null, sv_order_color = null, sv_cid = null, sv_subtotal = null, sv_qty = null, sv_order_size_id = "0", sv_order_size = null, sv_order_color_id = "0", sv_product_id = null, sv_product_with_package = null;
    private Menu menu;
    public static MenuItem items;
    public static int cartcount = 0;
    private LinearLayoutManager HorizontalLayout;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
       try {
           mTitle = mDrawerTitle = getTitle();
           iUI();
           mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
           //   ProductDetails.DropTable();
           p = new ProgressDialog(this);
           session = new UserSessionManager(getApplicationContext());
           new Retrive_specialCategory().execute();
           new Retrive_banner().execute();
           if (GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
               new Retrive_mywishlist().execute();
               createCartTable();
               showCartFromDatabase();
           } else {
               sv_id = new String[0];
               svproduct_id = new String[0];
           }
           setupToolbar();
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setHomeButtonEnabled(true);
           actionBar = getSupportActionBar();
           actionBar.setIcon(R.drawable.tohafaa_logoo);
           myAdaptercategory = new NavigationAdapter(this, R.layout.list_view_items, GlobalVariable.Category);
           simpleList.setAdapter(myAdaptercategory);
           simpleList.setOnItemClickListener(new DrawerItemClickListener());
           mDrawerLayout.setDrawerListener(mDrawerToggle);
           setupDrawerToggle();
           initViewsforHorizantalCategories();
       }catch (Exception e){}
    }
    @Override
    public void onBackPressed() {
        mDrawerLayout.closeDrawer(left_drawer);
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    void iUI() throws Exception {
        Categoryscrollbar = (RecyclerView)findViewById(R.id.recyclerviewscroll);
        filtermenu = (LinearLayout) findViewById(R.id.filtermenu);
        filtermenu.setVisibility(View.GONE);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        simpleList = (ListView) findViewById(R.id.left_drawer);
        backnavigation = (LinearLayout) findViewById(R.id.backnavigation);
        backtext = (TextView) findViewById(R.id.backtext);
        backnavigation.setOnClickListener(this);
        backnavigation.setVisibility(View.GONE);
        left_drawer = (LinearLayout) findViewById(R.id.left_drawer1);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        includelayout = (LinearLayout) findViewById(R.id.includelayout);
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        txv_tohafaa_special = (TextView) findViewById(R.id.txv_tohafaa_special);
        txv_categoryname = (TextView) findViewById(R.id.txv_categoryname);
        txv_tohafaa_special.setOnClickListener(this);
    }
    private void initViewsforHorizantalCategories()  throws Exception { //Recycleview for Horizantal categories using recyclerview
        categorieshorizantal = "1";
        Categoryscrollbar.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        Categoryscrollbar.setLayoutManager(layoutManager);
        ArrayList<categoryitems> item = prepareDataCategory();
        HorizontalLayout = new LinearLayoutManager(DashBordActivity.this, LinearLayoutManager.HORIZONTAL, false);
        Categoryscrollbar.setLayoutManager(HorizontalLayout);
        RecyclerAdapter adapter = new RecyclerAdapter(getBaseContext(), item);
        Categoryscrollbar.setAdapter(adapter);
    }
    private ArrayList<categoryitems> prepareDataCategory()  throws Exception {//
        ArrayList<categoryitems> item = new ArrayList<>();
        for (int i = 0; i < cat_id.length; i++) {
            categoryitems items = new categoryitems();
            items.setid(cat_id[i]);
            items.setname(cat_name[i]);
            items.setimage_url("https://www.tohafaa.com/" + cat_image[i]);
            item.add(items);
        }
        return item;
    }
    private void initViews()  throws Exception { //Gridview using recyclerview
        categorieshorizantal="0";
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<categoryitems> item = prepareData();
        RecyclerAdapter adapter = new RecyclerAdapter(getBaseContext(), item);
        recyclerView.setAdapter(adapter);
    }
    private ArrayList<categoryitems> prepareData()  throws Exception {
        ArrayList<categoryitems> item = new ArrayList<>();
        for (int i = 0; i < spc_id.length; i++) {
            categoryitems items = new categoryitems();
            items.setid(spc_id[i]);
            items.setname(spc_name[i]);
            items.setimage_url("http://responsive.tohafaa.com/" + spc_image[i]);
            item.add(items);
        }
        return item;
    }
    private void init()  throws Exception {//imageslider
        slider_image_list = new ArrayList<>();
        //Add few items to slider_image_list ,this should contain url of images which should be displayed in slider here i am adding few sample image links, you can add your own
        for (int i = 0; i < banner_id.length; i++) {
            categoryitems items = new categoryitems();
            items.setname(banner_id[i]);
            items.setimage_url("http://www.tohafaa.com/uploads/files/"+ file_name[i]);
            slider_image_list.add(items);
        }
        sliderPagerAdapter = new SliderPagerAdapter(DashBordActivity.this, slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);
        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {   }
            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {   }
        });
    }
    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];
        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backnavigation:
                backnavigation.setVisibility(View.GONE);
                try {
                    if ( myAdaptersc.getCount()>0) {
                        myAdaptersc.clear();
                    }
                }
                catch (Exception e){e.getMessage();}
                myAdaptercategory = new NavigationAdapter(this, R.layout.list_view_items, GlobalVariable.Category);
                simpleList.setAdapter(myAdaptercategory);
                simpleList.setOnItemClickListener(new DrawerItemClickListener());
                mDrawerLayout.setDrawerListener(mDrawerToggle);
                break;
            case R.id.txv_tohafaa_special:
                items.setVisible(false);
                filtermenu.setVisibility(View.GONE);
                includelayout.setVisibility(View.VISIBLE);
                if (FragmentFlag.equalsIgnoreCase("1")) {
                    limain.setVisibility(View.GONE);
                }
                sc_category_id="";
                backtext.setText("Tohafaa Special");
                new Retrive_specialCategory().execute();
                mDrawerLayout.closeDrawer(left_drawer);
                break;
        }
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            backnavigation.setVisibility(View.VISIBLE);
            Item ListViewClickData = (Item) parent.getItemAtPosition(position);
            name = ListViewClickData.getName();
            sc_category_id = Integer.toString(ListViewClickData.getid());
            new Retrive_subCategory().execute();
            backtext.setPaintFlags(backtext.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            backtext.setText(name);
        }
    }
    private class DrawerItemClickListenersubcategory implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Item ListViewClickData = (Item) parent.getItemAtPosition(position);
            sc_subcategory_id = Integer.toString(ListViewClickData.getid());
            DisplayItemsFragment.sc_spc_id = ListViewClickData.getsc_spc_id();
            SubCategoryName = ListViewClickData.getName();
            selectItem(position);
        }
    }
    private void selectItem(int position) {
        includelayout.setVisibility(View.GONE);
        switch (position) {
            default:
               // fragment = new DisplayItemsFragment();
                Intent i=new Intent(this,DisplayItemsFragment.class);
                startActivity(i);
                finish();
                mDrawerLayout.closeDrawer(left_drawer);
                break;
        }
    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    void setupToolbar() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.tohafaa_logoo);
    }
    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout// toolbar
                , R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }
     class Retrive_subCategory extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("sc_category_id", sc_category_id);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "subcategory", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("sddd",s);
            GlobalVariable.SubCategory.clear();
            if (s.contains("No Results Found")) {
                Toast.makeText(getApplication(),"No items are Found at "+name, Toast.LENGTH_LONG).show();
                simpleList.setAdapter(null);
            } else {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    String sc_name = null, sc_id = null;
                    Item item;
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        sc_id = jsonObject.getString("sc_id");
                        sc_name = jsonObject.getString("sc_name");
                        item = new Item(sc_name, Integer.parseInt(sc_id), "subcategory", "0");
                        GlobalVariable.SubCategory.add(item);
                    }
                    myAdaptersc = new NavigationAdapter(getBaseContext(), R.layout.list_view_items, GlobalVariable.SubCategory);
                    simpleList.setAdapter(myAdaptersc);
                    simpleList.setOnItemClickListener(new DrawerItemClickListenersubcategory());
                    mDrawerLayout.setDrawerListener(mDrawerToggle);
                    if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                        new Retrive_mywishlist().execute();     }
                    GlobalVariable.Max_Price = "";
                    GlobalVariable.Min_Price = "";
                    pf_end_price.clear();
                    pf_start_price.clear();
                } catch (Exception e) { e.getMessage();}
            }
        }
    }
    class Retrive_specialCategory extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            String s="";
            if(sc_category_id.equalsIgnoreCase("4") || sc_category_id.equalsIgnoreCase("") ||sc_category_id.equalsIgnoreCase("101"))//not equlato string check
            {
                s="specialcategory";
            }
            else {params.put("sc_category_id", sc_category_id);
                 s="subcategory";
             }
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + s, params);
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
            Log.d("Special",s);
            if (s.contains("No Results Found")) {
                Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
            } else {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    spc_id = new String[jsonArray.length()];
                    spc_name = new String[jsonArray.length()];
                    spc_image = new String[jsonArray.length()];
                    JSONObject jsonObject;
                    if(sc_category_id.equalsIgnoreCase("4") || sc_category_id.equalsIgnoreCase("") || sc_category_id.equalsIgnoreCase("101")) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                spc_id[i] = jsonObject.getString("spc_id");
                                spc_name[i] = jsonObject.getString("spc_name");
                                spc_image[i] = jsonObject.getString("spc_image");
                            }
                        txv_categoryname.setText("Tohafaa Special");
                        name="Special Gifting";
                        }
                        else
                        {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                spc_id[i] = jsonObject.getString("sc_id");
                                spc_name[i] = jsonObject.getString("sc_name");
                                spc_image[i] = jsonObject.getString("sc_image");
                            }
                            txv_categoryname.setText(name);
                        }
                    initViews();
                } catch (Exception e) { e.getMessage(); }
            }
        }
    }
    class Retrive_banner extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            if(sc_category_id.equalsIgnoreCase("4") || sc_category_id.equalsIgnoreCase("") )
            {
                sc_category_id="101";
            }
           // params.put("banner_category_id", "101");
            params.put("banner_category_id", sc_category_id);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "Retrive_banner", params);
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
            if (s.contains("No Results Found")) {
                Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
            } else {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    banner_id = new String[jsonArray.length()];
                    file_name = new String[jsonArray.length()];
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        banner_id[i] = jsonObject.getString("banner_id");
                        file_name[i] = jsonObject.getString("file_name");
                    }
                    init(); // method for initialisation
                    addBottomDots(0);// method for adding indicators
                    final Handler handler = new Handler();
                    final Runnable update = new Runnable() {
                        public void run() {
                            if (page_position == slider_image_list.size()) {
                                page_position = 0;
                            } else {
                                page_position = page_position + 1;
                            }
                            vp_slider.setCurrentItem(page_position, true);
                        }
                    };
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(update);
                        }
                    }, 100, 5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getMessage();
                }
            }
        }
    }
    public void Enquiry_diaglog()  throws Exception {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_enquiryfororder);
        final EditText edtx_email = (EditText)dialog. findViewById(R.id.edtx_email);
        final EditText edtx_mobile = (EditText)dialog. findViewById(R.id.edtx_mobile);
        final EditText edtx_subject = (EditText)dialog. findViewById(R.id.edtx_subject);
        final EditText edtx_EnquiryDetails = (EditText)dialog. findViewById(R.id.edtx_EnquiryDetails);
        emailinputlayout = (TextInputLayout) dialog.findViewById(R.id.emailinputlayout);
        mobileinputlayout = (TextInputLayout) dialog.findViewById(R.id.mobileinputlayout);
        subjectinputlayout = (TextInputLayout) dialog.findViewById(R.id.subjectinputlayout);
        EnquiryDetailsinputlayout = (TextInputLayout) dialog.findViewById(R.id.EnquiryDetailsinputlayout);
        Button btn_submit1 = (Button)dialog.findViewById(R.id.btn_submit);
        btn_submit1.setText("Send Enquiry");
        Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);
        // if button is clicked, close the custom dialog
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eemail = edtx_email.getText().toString();
                Emobile = edtx_mobile.getText().toString();
                Esubject = edtx_subject.getText().toString();
                EenquiryDetails = edtx_EnquiryDetails.getText().toString();
                try {
                    ValidationEnquiry();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.FILL_PARENT);
    }
    private void ValidationEnquiry()  throws Exception {
        boolean isValid = true;
        if (Eemail.isEmpty()) {
            emailinputlayout.setError("Please fill out this field");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Eemail).matches()) {
            emailinputlayout.setError("email should be valid");
            isValid = false;
        } else {
            emailinputlayout.setErrorEnabled(false);
        }
         if (Emobile.isEmpty()) {
            mobileinputlayout.setError("Please fill out this field");
            isValid = false;
        }else if (Emobile.length() < 10) {
            mobileinputlayout.setError("mobile should be valid");
            isValid = false;
        } else {
            mobileinputlayout.setErrorEnabled(false);
        }
        if (Esubject.isEmpty()) {
            subjectinputlayout.setError("Please fill out this field.");
            isValid = false;
        } else {
            subjectinputlayout.setErrorEnabled(false);
        }
        if (EenquiryDetails.isEmpty()) {
            EnquiryDetailsinputlayout.setError("Please fill out this field.");
            isValid = false;
        } else {
            EnquiryDetailsinputlayout.setErrorEnabled(false);
        }
        if (isValid) {
            new InsertEnquiry().execute();
        }
    }
    public void Franchise_diaglog()  throws Exception {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_franchise);
        final EditText edtx_firstname = (EditText)dialog. findViewById(R.id.edtx_firstname);
        final EditText edtx_email = (EditText)dialog. findViewById(R.id.edtx_email);
        final EditText edtx_mobile = (EditText)dialog. findViewById(R.id.edtx_mobile);
        final EditText edtx_addressline1 = (EditText)dialog. findViewById(R.id.edtx_addressline1);
        final EditText edtx_remark = (EditText)dialog. findViewById(R.id.edtx_remark);
        firstinputlayout = (TextInputLayout) dialog.findViewById(R.id.firstinputlayout);
        emailinputlayout = (TextInputLayout) dialog.findViewById(R.id.emailinputlayout);
        mobileinputlayout = (TextInputLayout) dialog.findViewById(R.id.mobileinputlayout);
        address1inputlayout = (TextInputLayout) dialog.findViewById(R.id.address1inputlayout);
        remarkinputlayout = (TextInputLayout) dialog.findViewById(R.id.remarkinputlayout);
        Button btn_submit1 = (Button)dialog.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = edtx_firstname.getText().toString();
                femail = edtx_email.getText().toString();
                fmobile = edtx_mobile.getText().toString();
                faddress = edtx_addressline1.getText().toString();
                fremark = edtx_remark.getText().toString();
                try {
                    ValidationFranchise();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.FILL_PARENT);
    }
    private void ValidationFranchise()  throws Exception {
        boolean isValid = true;
        if (fname.isEmpty()) {
            firstinputlayout.setError("Please fill out this field.");
            isValid = false;
        } else {
            firstinputlayout.setErrorEnabled(false);
        }
        if (femail.isEmpty()) {
            emailinputlayout.setError("Please fill out this field");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(femail).matches()) {
            emailinputlayout.setError("email should be valid");
            isValid = false;
        } else {
            emailinputlayout.setErrorEnabled(false);
        }
        if (fmobile.isEmpty()) {
            mobileinputlayout.setError("Please fill out this field");
            isValid = false;
        } else  if (fmobile.length() < 10) {
            mobileinputlayout.setError("mobile should be valid");
            isValid = false;
        } else {
            mobileinputlayout.setErrorEnabled(false);
        }
        if (fremark.isEmpty()) {
            remarkinputlayout.setError("Please fill out this field.");
            isValid = false;
        } else {
            remarkinputlayout.setErrorEnabled(false);
        }
        if (isValid) {
            new InsertFranchise().execute();
        }
    }
    public void Customerreviewdiaglog() throws Exception{
        dialog = new Dialog(DashBordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_customer_review);
        final EditText edtx_WriteReview = (EditText) dialog.findViewById(R.id.edtx_WriteReview);
        final EditText edtx_reviewtitle = (EditText) dialog.findViewById(R.id.edtx_reviewtitle);
        TextView customerReviews = (TextView) dialog.findViewById(R.id.customerReviews);
        customerReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent  ProductReviews=new Intent(getBaseContext(),ProductReviews.class);
                startActivity(ProductReviews);
                corpoarateclient=2;
            }
        });
        writereviewTextInputLayout = (TextInputLayout) dialog.findViewById(R.id.WriteReview);
        reviewtitleTextInputLayout = (TextInputLayout) dialog.findViewById(R.id.reviewtitle);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_done = (Button) dialog.findViewById(R.id.btn_done);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewtitle = edtx_reviewtitle.getText().toString();
                writereview = edtx_WriteReview.getText().toString();
                try {
                    Validationreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }
    private void Validationreview()  throws Exception{
        boolean isValid = true;
        if (reviewtitle.isEmpty()) {
            reviewtitleTextInputLayout.setError("Please fill out this field");
            isValid = false;
        } else {
            reviewtitleTextInputLayout.setErrorEnabled(false);
        }
        if (writereview.isEmpty()) {
            writereviewTextInputLayout.setError("Please fill out this field.");
            isValid = false;
        } else {
            writereviewTextInputLayout.setErrorEnabled(false);
        }
        if (isValid) {
                new InsertCustomerReview().execute();
        }
    }
    @Override//option menu start from here
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_cart:
                if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                    Intent i1 = new Intent(getBaseContext(), Activity_CartList.class);
                    startActivity(i1);
                    finish();
                }
                else
                {
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
            case R.id.action_wishlist:
                if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                    Intent i2 = new Intent(getBaseContext(), Activity_WishList.class);
                    startActivity(i2);
                    finish();
                }
                else
                {
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
            case 1:
                if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                    UpdateProfile=1;
                    Intent updatere=new Intent(this,RegisterActivity.class);
                    startActivity(updatere);
                }
                else
                {
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
            case 2:
                try {
                    Franchise_diaglog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                    try {
                        Customerreviewdiaglog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getBaseContext(),"Please Login, Only registered customers can give the review ",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
            case 4:
                     try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Tohafaa");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "http://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {}
                break;
            case 5:
                Intent TermsCondition = new Intent(getBaseContext(), TermsCondition.class);
                startActivity(TermsCondition);
                break;
            case 6:
                Intent CorporateClient = new Intent(getBaseContext(), CorporateClient.class);
                startActivity(CorporateClient);
               // Toast.makeText(getBaseContext(),"Under development",Toast.LENGTH_SHORT).show();
                break;
            case 7:
                String phone = "7447477070";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                break;
            case 9:
                Intent Activity_OrdersList = new Intent(getBaseContext(),Activity_OrdersList.class);
                startActivity(Activity_OrdersList);
                break;
            case 8:
                try {
                    Enquiry_diaglog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    Logout();
                }catch (Exception e){}
                break;

            default:
                break;
        }
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void Logout()  throws Exception{
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to LogOut?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                mDatabase.execSQL(
                        "delete from CartTable;"
                );
                session.logoutUser();//to clear all account details from session
                finish();
                flagsqliteretrivedata = 0;
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
    private CharSequence menuIconWithText(Drawable r) {
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("   " +  "7447477070");
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        items = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(items);
        //searchView.setOnQueryTextListener(this);
        items.setVisible(true);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // view.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //view.setVisibility(View.VISIBLE);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchproductname=query;
                        name="Tohafaa";
                        GlobalVariable.Min_Price="0";
                        GlobalVariable.Max_Price="1000";
                        sc_category_id="";sc_spc_id="";
                        sc_subcategory_id="";
                        Retrive_productvalue="";
                        Intent intent = new Intent();
                        intent.setClass(getBaseContext(),DisplayItemsFragment.class);
                        intent.setAction(Intent.ACTION_SEARCH);
                        intent.putExtra(SearchManager.QUERY, query);
                        startActivity(intent);
                        return true; // Handle search search yourself so return true
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

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
        menu.add(0, 1, 1, "MY Profile");
        menu.add(0, 2, 2, "Franchise");
        menu.add(0, 3, 3, "Customer Review");
        menu.add(0, 4, 4, "Share");
        menu.add(0, 5, 5, "Terms & Conditions");
        menu.add(0, 6, 6, "Corporate Clients");
        menu.add(0, 7, 7, menuIconWithText(getResources().getDrawable(android.R.drawable.ic_menu_call)));
        menu.add(0, 8, 8, "Enquiry For Order");
        if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
            menu.add(0, 9, 9, "Track Your Order");
            menu.add(0, 10, 10, "LogOut");
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
        return super.onCreateOptionsMenu(menu);
    }
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        final ArrayList<categoryitems> filteredModelList = filter(item, newText);
//       // adapter.setFilter(filteredModelList);
//        return true;
//    }
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//    private ArrayList<categoryitems> filter(ArrayList<categoryitems> models, String query) {
//        query = query.toLowerCase();
//        final ArrayList<categoryitems> filteredModelList = new ArrayList<>();
////        for (categoryitems model : models) {
////            final String text = model.getproduct_name().toLowerCase();
////            if (text.contains(query)) {
////                filteredModelList.add(model);
////            }
////        }
//        Toast.makeText(getBaseContext(),query,Toast.LENGTH_SHORT).show();
//
//        return filteredModelList;
//    }
   public static class Retrive_mywishlist extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            try {
                params.put("sv_user_id", GlobalVariable.user_id);
                params.put("sv_key", "my_wishlist");
            }catch (Exception e){}
                return requestHandler.sendPostRequest(URLs.URL_InsertOperations + "Retrive_whishlist", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{   Activity_OrdersList.dialog();}catch (Exception e){}
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            if(s.contains("No Results Found")) {
                sv_id=new String[0];
                svproduct_id=new String[0];
                try {
                    setwishlistvalueonicon();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    sv_id = new String[jsonArray.length()];
                    sv_name = new String[jsonArray.length()];
                    sv_price = new String[jsonArray.length()];
                    sv_src = new String[jsonArray.length()];
                    sv_productsheepingcost = new String[jsonArray.length()];
                    sv_maxQantity = new String[jsonArray.length()];
                    svproduct_id = new String[jsonArray.length()];
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        svproduct_id[i] = jsonObject.getString("id");
                        sv_name[i] = jsonObject.getString("name");
                        sv_price[i] = jsonObject.getString("price");
                        sv_src[i] = jsonObject.getString("src");
                        sv_productsheepingcost[i] = jsonObject.getString("product_shipping_cost");
                        sv_maxQantity[i] = jsonObject.getString("max_quantity");
                        sv_id[i] = jsonObject.getString("0");
                    }
                  setwishlistvalueonicon();

                } catch (Exception e) { e.getMessage();}
            }
        }
    }
    public static void setcartvalueonicon() {
        if (textCartItemCount != null) {
            if (cartcount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(cartcount, cartcount + 1)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    public static void setwishlistvalueonicon() throws Exception
    {
        if (textwishItemCount != null) {
            if (sv_id.length == 0) {
                if (textwishItemCount.getVisibility() != View.GONE) {
                    textwishItemCount.setVisibility(View.GONE);
                }
            } else {
                textwishItemCount.setText(String.valueOf(Math.min(sv_id.length, sv_id.length+1)));
                if (textwishItemCount.getVisibility() != View.VISIBLE) {
                    textwishItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void createCartTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS CartTable (\n" +
                        "    cid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                        "    product_id INTEGER NOT NULL,\n" +
                        "    product_quantity varchar(200) NOT NULL,\n" +
                        "    qty varchar(200) NOT NULL,\n" +
                        "    discount varchar(200) NOT NULL,\n" +
                        "    price varchar(200) NOT NULL,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    src varchar(200) NOT NULL,\n" +
                        "    product_shipping_cost varchar(200) NOT NULL,\n" +
                        "    options varchar(200) NOT NULL,\n" +
                        "    order_size_id INTEGER NOT NULL,\n" +
                        "    order_color_id INTEGER NOT NULL,\n" +
                        "    order_size varchar(200) NOT NULL,\n" +
                        "    order_color varchar(200) NOT NULL,\n" +
                        "    superimpose_image varchar(255) NOT NULL,\n" +
                        "    superimpose_text varchar(200) NOT NULL,\n" +
                        "    customer_pincode varchar(200) NOT NULL,\n" +
                        "    product_with_package varchar(200) NOT NULL,\n" +
                        "    rowid varchar(200) NOT NULL,\n" +
                        "    subtotal varchar(200) NOT NULL,\n" +
                        "    estimated_days varchar(200) NOT NULL\n" +
                        ");"
        );
       // if(flagsqliteretrivedata==0) {
            new Retrive_mycartlist().execute();
        //}

    }
    public static void showCartFromDatabase() {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM CartTable", null);
        cartcount=cursor.getCount();
        if (cursor.moveToFirst()) {
            do { cartcount=cursor.getCount();
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    class Retrive_mycartlist extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("sv_user_id", GlobalVariable.user_id);
            params.put("sv_key","my_cart");
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"Retrive_cartlist", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("cartlist",s);
               p.dismiss();
            if(s.contains("No Results Found")) {
                showCartFromDatabase();
                try {
                    setwishlistvalueonicon();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setcartvalueonicon();
            }
            else {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    CartList = new ArrayList<>();
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        //sv_order_color_id = jsonObject.getString("order_color_id");
                        sv_product_id = jsonObject.getString("id");
                        //sv_product_with_package = jsonObject.getString("product_with_package");
                        //sv_order_size = jsonObject.getString("order_size");
                       // sv_order_size_id = jsonObject.getString("order_size_id");
                        sv_qty = jsonObject.getString("qty");
                        sv_subtotal = jsonObject.getString("subtotal");
                        sv_cid = jsonObject.getString("rowid");
                       // sv_order_color = jsonObject.getString("order_color");
                        sv_discount = jsonObject.getString("discount");
                        if(jsonObject.isNull("field-name")){sv_superimpose_text="";sv_superimpose_image="";}
                        else
                        {
                            sv_superimpose_text = jsonObject.getString("superimpose_text");
                            sv_superimpose_image = jsonObject.getString("superimpose_image");
                        }
                        sv_cartprice = jsonObject.getString("price");
                        sv_product_shipping_cost = jsonObject.getString("product_shipping_cost");
                        sv_cartname = jsonObject.getString("name");
                        sv_cartsrc = jsonObject.getString("src");
                        sv_rowid = jsonObject.getString("rowid");
                        sv_customer_pincode = jsonObject.getString("customer_pincode");
                   //     sv_options = jsonObject.getString("options");
                        estimated_days = "0";
                        sv_order_color_id="";
                        sv_product_with_package="";
                        sv_order_size="";
                        sv_order_size_id="";
                        sv_order_color="";
                        sv_options="";
                       Insertvaluesintodb();
                 }
                    flagsqliteretrivedata=1;
                    showCartFromDatabase();
                    setwishlistvalueonicon();
                    setcartvalueonicon();
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getMessage();
                }
            }
        }
    }
  public static  void Insertvaluesintodb() throws Exception
    {
        if (inputsAreCorrect(sv_product_id,sv_product_qty,sv_qty,"0",sv_cartprice,sv_cartname,sv_cartsrc,sv_product_shipping_cost,"0","0","0",sv_order_size,sv_order_color,sv_superimpose_image,"0",sv_customer_pincode,sv_product_with_package,"0",sv_subtotal,estimated_days)) {
            String insertSQL = "INSERT INTO CartTable \n" +
                    "(product_id,product_quantity,qty, discount, price,name, src,product_shipping_cost, options,order_size_id,order_color_id, order_size, order_color, superimpose_image,superimpose_text,customer_pincode, product_with_package, rowid , subtotal,estimated_days)\n" +
                    "SELECT * FROM  \n" +
                    "( SELECT  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)AS tmp " +
                    "WHERE  NOT EXISTS \n" +
                    "        (   SELECT  product_id FROM    CartTable \n" +
                    "            WHERE   product_id = '" + sv_product_id + "');";
            mDatabase.execSQL(insertSQL, new String[]{sv_product_id,sv_product_qty,sv_qty,"0",sv_cartprice,sv_cartname,sv_cartsrc,sv_product_shipping_cost,"0","0","0",sv_order_size,sv_order_color,sv_superimpose_image,sv_superimpose_text,sv_customer_pincode,sv_product_with_package,"0",sv_subtotal,estimated_days});
        }
    }
    public static boolean inputsAreCorrect(String product_id,String product_quantity,String qty, String discount,String price,String name,
                                     String src,String product_shipping_cost, String options,String order_size_id, String order_color_id,
                                     String order_size,String order_color, String superimpose_image,String superimpose_text, String customer_pincode,String product_with_package, String rowid,String subtotal,String estimated_days) {
        return true;
    }


    class InsertCustomerReview extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", GlobalVariable.user_id);
            params.put("name",GlobalVariable.first_name);
            params.put("email_id", GlobalVariable.email);
            params.put("contact_no", GlobalVariable.mobile);
            params.put("subject", reviewtitle);
            params.put("review", writereview);
            params.put("status", "0");
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"InsertCustomerReview", params);
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
            Toast.makeText(getBaseContext(),"Thank you for your ! valuable review", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }
    class InsertEnquiry extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("Eemail", Eemail);
            params.put("Emobile",Emobile);
            params.put("Esubject", Esubject);
            params.put("EenquiryDetails",EenquiryDetails);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"InsertEnquiryforOrder", params);
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
            Toast.makeText(getBaseContext(),"Thank you. We will get back to you soon !!!", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }
    class InsertFranchise extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("name",fname);
            params.put("email", femail);
            params.put("mobile", fmobile);
            params.put("address", faddress);
            params.put("remark", fremark);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"InsertFranchise", params);
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
            Toast.makeText(getBaseContext(),"Thank you !!!", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }
}

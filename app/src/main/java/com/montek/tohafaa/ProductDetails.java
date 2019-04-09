package com.montek.tohafaa;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.montek.tohafaa.Adapter.ProductAdapter;
import com.montek.tohafaa.Adapter.SliderPagerAdapter;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.TabFragments.DisplayItemsFragment;
import com.montek.tohafaa.extra.*;
import com.montek.tohafaa.model.*;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import net.gotev.uploadservice.MultipartUploadRequest;
import org.json.*;
import java.io.File;
import java.util.*;
import static com.montek.tohafaa.Activity_WebViewofInvoice.*;
import static com.montek.tohafaa.Adapter.ProductAdapter.imgloader;
import static com.montek.tohafaa.CorporateClient.corpoarateclient;
import static com.montek.tohafaa.RegisterActivity.pincodeerror;
import static com.montek.tohafaa.TabFragments.DisplayItemsFragment.*;
public class ProductDetails extends AppCompatActivity implements View.OnClickListener ,Spinner.OnItemSelectedListener  {
    TextView txv_seereview,grid_item_price1,dprice,txv_Specification,reviewnotavailable,txv_outofstock,txv_pincodenotavailable,txv_addcart, txv_buy_now, txv_productdescription, txv_wishlist, txv_productname, txv_productprice, txv_ProductCode, txv_productshipingcost, txv_description, txv_reviews;
    ImageView img_wishlist, grid_item_image;
    TextInputLayout reviewtitleinputlayout,pincodeinputlayout,addreviewinputlayout, emailinputlayout, mobileinputlayout, firstinputlayout;
    LinearLayout li_layout,ll_dots,colorlayout,sizelayout,imagelayout,layout_wishlist,layoutshipping,layout_reviews, layout_description, reviewslayout, productdecriptionlayout;
    RatingBar rb_ratingBar;
    EditText edtx_reviewtitle,edtx_pincode,edtx_mobile, edtx_addreview, edtx_firstname, edtx_email;
    Spinner colorspinner,sizespinner,spin1;
    ArrayList<Integer> QuantityArray;
    String  orderforreview="false",imageName,superimpose_text,pcolor,psize,variant_id,product_sizeid="0",product_colorid="0",product_quantity="0",product_with_package,product_color="0",product_size="0",product_superimpose_image,product_wishlistid,product_price,product_shipping_cost,product_image,product_name,estimated_days,retrivepin_code,pincode,Quantity,review,reviewtitle,mobile,email,firstname,ratingvalue="0";
    Button btn_submit;
    public static String addcart="",FlagForProductList,product_iddetail;
    LinearLayoutManager HorizontalLayout;
    RecyclerView RelatedProductscrollbar;
    ImageView upload_img;
    public static ArrayList<Item> sizearray,colorarray;
    public static SQLiteDatabase mDatabase;
    ArrayList<categoryitems> slider_image_list;
    String[] file_name,varientid;
    SliderPagerAdapter sliderPagerAdapter;
    private ViewPager vp_slider;
    RelativeLayout viewpagelayout;
    TextView[] dots;
    int page_position = 0;
    private Uri filePath;
    private Bitmap bitmap;
    EditText edtx_text;
    public static ArrayList<categoryitems> ProductReviewslist = new ArrayList<categoryitems>();
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int STORAGE_PERMISSION_CODE = 123;
    public static final String DATABASE_NAME = "mydatabase";
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
       try{ iUI();
        //createdelete();
        actionBar = getSupportActionBar();
        Actionbar("Product Details");
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        QuantityArray = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
            if (extras != null) {
                product_iddetail = extras.getString("product_id");
                new Retrive_skus().execute();
                product_name = extras.getString("product_name");
                txv_productname.setText(product_name);
                String product_description = extras.getString("product_description");
                Spanned desc = Html.fromHtml(product_description);
                txv_productdescription.setText(desc);
                String product_specification = extras.getString("product_specification");
                Spanned sp = Html.fromHtml(product_specification);
                if(product_specification.equalsIgnoreCase("") ||product_specification.equalsIgnoreCase("null") || product_specification.equalsIgnoreCase(null))
                {
                    txv_Specification.setText("Not Applicable");
                }
                else {
                    txv_Specification.setText(sp);
                }
                // txv_Specification.setText(product_specification);
                String product_model = extras.getString("product_model");
                String product_code = extras.getString("product_code");
                String product_offer = extras.getString("product_offer");
                txv_ProductCode.setText(product_code);
                product_quantity = extras.getString("product_quantity");
                Log.d("txv_Specification",product_specification);
                if(product_quantity.equalsIgnoreCase("0")||product_quantity.isEmpty() || product_quantity.equalsIgnoreCase(null) ||product_quantity.equalsIgnoreCase("")) {
                    li_layout.setVisibility(View.GONE);
                    txv_outofstock.setVisibility(View.VISIBLE);
                }
                else
                {   li_layout.setVisibility(View.VISIBLE);
                    txv_outofstock.setVisibility(View.GONE);
                    qty();
                }

                String product_price1 = extras.getString("product_price");
                String[] parts = product_price1.split("\\.");
                product_price = parts[0];
                Log.d("product_price", product_price);
                txv_productprice.setText(product_price);

                String product_sku = extras.getString("product_sku");
                //  editTextcompanyname.setText(product_sku);
                String product_substract_stock = extras.getString("product_substract_stock");
                // editTextdesignation.setText(product_substract_stock);
                product_with_package = extras.getString("product_with_package");
                // editTextcompanyname.setText(product_with_package);
                pcolor = extras.getString("product_color");
                psize = extras.getString("product_size");
                //editTextpassword.setText(product_size);
                product_superimpose_image = extras.getString("product_superimpose_image");
                if (product_superimpose_image.equalsIgnoreCase("1")) {
                    imagelayout.setVisibility(View.VISIBLE);
                } else {
                    imagelayout.setVisibility(View.GONE);
                }
                if (psize.equalsIgnoreCase("1")) {
                    sizelayout.setVisibility(View.VISIBLE);
                } else {
                    sizelayout.setVisibility(View.GONE);
                }
                if (pcolor.equalsIgnoreCase("1")) {
                    colorlayout.setVisibility(View.VISIBLE);
                } else {
                    colorlayout.setVisibility(View.GONE);
                }
                String product_brand_id = extras.getString("product_brand_id");
                //  editTextdesignation.setText(product_brand_id);
                String product_cod = extras.getString("product_cod");
                // p.setText(product_cod);
                String po_discount = extras.getString("po_discount");
                dprice.setText(po_discount+"% OFF");
                float x=0;
                if( "0" != po_discount.intern() ) {
                    x =  (1 - (Float.parseFloat(po_discount) / 100));
                    grid_item_price1.setText("₹"+product_price);
                    txv_productprice.setText("₹"+String.valueOf(Float.parseFloat(product_price)*x));
                }
                else {
                    grid_item_price1.setVisibility(View.GONE);
                    dprice.setVisibility(View.GONE);
                    txv_productprice.setText("₹"+product_price);
                }
                product_image = extras.getString("product_image");
                String product_spc_id = extras.getString("product_spc_id");
                //String product_sub4category_id = extras.getString("product_sub4category_id");
                //  String product_sub3category_id = extras.getString("product_sub3category_id");
                // String product_sub2category_id = extras.getString("product_sub2category_id");
                String product_free_shipping = extras.getString("product_free_shipping");
                // editTextcompanyname.setText(product_free_shipping);
                String product_free_shipping_limit = extras.getString("product_free_shipping_limit");
                //   editTextdesignation.setText(product_free_shipping_limit);
                product_shipping_cost = extras.getString("product_shipping_cost");
                txv_productshipingcost.setText(product_shipping_cost);
                String product_tax_status = extras.getString("product_tax_status");
                //  editTextcompanyname.setText(product_tax_status);
                String product_tax = extras.getString("product_tax");
                // editTextpassword.setText(product_tax);
                String product_category_id = extras.getString("product_category_id");
                //  editTextpassword.setText(product_category_id);
                String product_subcategory_id = extras.getString("product_subcategory_id");
                //  editTextcompanyname.setText(product_subcategory_id);
                product_wishlistid = extras.getString("product_wishlistid");
                if ("0" != product_wishlistid.intern())//not equlato string check
                {
                    //layout_wishlist.setEnabled(false);
                    img_wishlist.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                }
//                Glide.with(getBaseContext()).load(product_image)
//                        .thumbnail(0.5f)
//                        .crossFade()
//                        .skipMemoryCache(true)
//                        // .error(R.drawable.placeholder)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .into(grid_item_image);
                imgloader = ImageLoader.getInstance();
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
                imgloader.displayImage(product_image, grid_item_image, options);
            }
        rb_ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = rb_ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    rb_ratingBar.setRating(stars);
                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }});
        edtx_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                pincode = edtx_pincode.getText().toString().trim();
                if (s.length() != 0 && s.length() > 5) {
                    new CkeckPincode().execute();
                }
            }
        });
        initViews();
        if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                   new checkproductorderforreviews().execute();
        }
        else {
            reviewnotavailable.setText(Html.fromHtml("You must <font color='#EE0000'>Login</font> to add review for this product. \nYou can add review once you purchase this product!!"));
            reviewnotavailable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
       }catch (Exception e){}
    }
    void qty() throws Exception
    {
       try {  QuantityArray.clear();
           for (int i = 1; i <= Integer.parseInt(product_quantity); i++) {
               QuantityArray.add(i);
           }
           ArrayAdapter spinner1 = new ArrayAdapter(getBaseContext(), R.layout.spinner_item, QuantityArray);
           spinner1.setDropDownViewResource(R.layout.spinner_item);
           spin1.setAdapter(spinner1);
         }catch (Exception e){
           li_layout.setVisibility(View.GONE);
           txv_outofstock.setVisibility(View.VISIBLE);
       }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        switch (item.getItemId()) {
            case android.R.id.home:
               // DashBordActivity.fragment = new DisplayItemsFragment();
                Intent i=new Intent(getBaseContext(),DisplayItemsFragment.class);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed()  {
       // DashBordActivity.fragment = new DisplayItemsFragment();
        Intent i=new Intent(getBaseContext(),DisplayItemsFragment.class);
        startActivity(i);
        this.finish();
    }
    private void iUI() throws Exception {
        reviewnotavailable = (TextView)findViewById(R.id.reviewnotavailable);
        edtx_text = (EditText)findViewById(R.id.edtx_text);
        li_layout=(LinearLayout)findViewById(R.id.li_layout);
        txv_outofstock = (TextView) findViewById(R.id.txv_outofstock);
        grid_item_price1 = (TextView) findViewById(R.id.grid_item_price1);
        grid_item_price1.setPaintFlags(grid_item_price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        dprice = (TextView) findViewById(R.id.dprice);
        txv_Specification = (TextView) findViewById(R.id.txv_Specification);
        RelatedProductscrollbar = (RecyclerView)findViewById(R.id.recyclerviewscroll);
        grid_item_image = (ImageView) findViewById(R.id.grid_item_image);
        txv_productname = (TextView) findViewById(R.id.txv_productname);
        txv_pincodenotavailable = (TextView) findViewById(R.id.txv_pincodenotavailable);
        txv_pincodenotavailable.setVisibility(View.GONE);
        txv_productprice = (TextView) findViewById(R.id.txv_productprice);
        txv_ProductCode = (TextView) findViewById(R.id.txv_ProductCode);
        txv_productshipingcost = (TextView) findViewById(R.id.txv_productshipingcost);
        txv_description = (TextView) findViewById(R.id.text_description);
        txv_description.setOnClickListener(this);
        txv_reviews = (TextView) findViewById(R.id.text_reviews);
        img_wishlist = (ImageView) findViewById(R.id.img_wishlist);
        img_wishlist.setOnClickListener(this);
        txv_wishlist = (TextView) findViewById(R.id.text_wishlist);
        productdecriptionlayout = (LinearLayout) findViewById(R.id.productdecriptionlayout);
        txv_productdescription = (TextView) findViewById(R.id.txv_productdescription);
        reviewslayout = (LinearLayout) findViewById(R.id.reviewslayout);
        reviewslayout.setVisibility(View.GONE);
        rb_ratingBar = (RatingBar) findViewById(R.id.rb_ratingBar);
        Drawable drawable = rb_ratingBar.getProgressDrawable();
        //  drawable.setColorFilter(Color.parseColor("#83AAE5"), PorterDuff.Mode.SRC_ATOP);
        addreviewinputlayout = (TextInputLayout) findViewById(R.id.addreviewinputlayout);
        reviewtitleinputlayout = (TextInputLayout) findViewById(R.id.reviewtitleinputlayout);
        edtx_addreview = (EditText) findViewById(R.id.edtx_addreview);
        edtx_reviewtitle = (EditText) findViewById(R.id.edtx_reviewtitle);
        firstinputlayout = (TextInputLayout) findViewById(R.id.firstinputlayout);
        edtx_firstname = (EditText) findViewById(R.id.edtx_firstname);
        edtx_firstname.setText(GlobalVariable.first_name);
        emailinputlayout = (TextInputLayout) findViewById(R.id.emailinputlayout);
        edtx_email = (EditText) findViewById(R.id.edtx_email);
        edtx_email.setText(GlobalVariable.email);
        mobileinputlayout = (TextInputLayout) findViewById(R.id.mobileinputlayout);
        edtx_mobile = (EditText) findViewById(R.id.edtx_mobile);
        edtx_mobile.setText(GlobalVariable.mobile);
        txv_addcart = (TextView) findViewById(R.id.txv_addcart);
        txv_addcart.setOnClickListener(this);
        txv_buy_now = (TextView) findViewById(R.id.txv_buy_now);
        txv_buy_now.setOnClickListener(this);
        txv_seereview = (TextView) findViewById(R.id.txv_seereview);
        txv_seereview.setOnClickListener(this);
        layout_description = (LinearLayout) findViewById(R.id.layout_description);
        layout_description.setOnClickListener(this);
        layout_reviews = (LinearLayout) findViewById(R.id.layout_reviews);
        layout_reviews.setOnClickListener(this);
        layoutshipping = (LinearLayout) findViewById(R.id.layoutshipping);
        layoutshipping.setVisibility(View.GONE);
        spin1 = (Spinner) findViewById(R.id.spinner1);
        spin1.setOnItemSelectedListener(this);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        txv_buy_now = (TextView) findViewById(R.id.txv_buy_now);
        txv_buy_now.setOnClickListener(this);
        pincodeinputlayout = (TextInputLayout) findViewById(R.id.pincodeinputlayout);
        edtx_pincode = (EditText) findViewById(R.id.edtx_pincode);
        layout_wishlist = (LinearLayout) findViewById(R.id.layout_wishlist);
        layout_wishlist.setOnClickListener(this);
        imagelayout = (LinearLayout) findViewById(R.id.imagelayout);
        colorlayout = (LinearLayout) findViewById(R.id.colorlayout);
        sizelayout = (LinearLayout) findViewById(R.id.sizelayout);
        sizespinner = (Spinner) findViewById(R.id.sizespinner);
        sizespinner.setOnItemSelectedListener(this);
        colorspinner = (Spinner) findViewById(R.id.colorspinner);
        colorspinner.setOnItemSelectedListener(this);
        upload_img = (ImageView) findViewById(R.id.upload_img);
        upload_img.setOnClickListener(this);
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        viewpagelayout = (RelativeLayout) findViewById(R.id.viewpagelayout);
        viewpagelayout.setVisibility(View.GONE);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
    }
    public static void DropTable() throws Exception {
        mDatabase.execSQL(
                "DROP TABLE IF EXISTS CartTable ;"
        );
    }
    private boolean inputsAreCorrect(String product_id,String product_quantity,String qty, String discount,String price,String name,
                                     String src,String product_shipping_cost, String options,String order_size_id, String order_color_id,
                                     String order_size,String order_color, String superimpose_image,String superimpose_text, String customer_pincode,String product_with_package, String rowid,String subtotal,String estimated_days) {
        return true;
    }
       private void initViews() throws Exception{ //Gridview using recyclerview
        FlagForProductList="1";
        RelatedProductscrollbar.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        RelatedProductscrollbar.setLayoutManager(layoutManager);
        item = prepareData();
        HorizontalLayout = new LinearLayoutManager(ProductDetails.this, LinearLayoutManager.HORIZONTAL, false);
        RelatedProductscrollbar.setLayoutManager(HorizontalLayout);
        adapter = new ProductAdapter(getBaseContext(),item);
        RelatedProductscrollbar.setAdapter(adapter);
    }
    @Override
    public void onClick(View v)  {
        switch (v.getId()) {
            case R.id.upload_img:
                try {
                    requestStoragePermission();
                    showFileChooser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.img_wishlist:
                if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                    // img_wishlist.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                    if (img_wishlist.getDrawable().getConstantState() == getBaseContext().getResources().getDrawable(R.drawable.ic_favorite_black_24dp).getConstantState()) {
                        img_wishlist.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    } else {
                        img_wishlist.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                    new AddToWishlist().execute();
                }
                break;
            case R.id.layout_wishlist:
                productdecriptionlayout.setVisibility(View.GONE);
                reviewslayout.setVisibility(View.GONE);
                reviewnotavailable.setVisibility(View.GONE);
                if(product_wishlistid.equalsIgnoreCase("0")) {
                    if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                       // img_wishlist.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                        if (img_wishlist.getDrawable().getConstantState() == getBaseContext().getResources().getDrawable( R.drawable.ic_favorite_black_24dp).getConstantState())
                        {
                            img_wishlist.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        }
                        else
                        {
                            img_wishlist.setImageResource(R.drawable.ic_favorite_black_24dp);
                        }
                        new AddToWishlist().execute();
                    }
                    else{
                        Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                        intent.putExtra("sv_key","my_wishlist");
                        intent.putExtra("id",product_iddetail);
                        intent.putExtra("name",product_name);
                        intent.putExtra("price",product_price);
                        intent.putExtra("src", product_image);
                        intent.putExtra("product_shipping_cost",product_shipping_cost);
                        intent.putExtra("max_quantity",product_quantity);
                        startActivity(intent);
                        finish();
                    }
                }
                break;

            case R.id.txv_seereview:
                corpoarateclient=0;
                Intent i=new Intent(getBaseContext(),ProductReviews.class);
                startActivity(i);
                break;
            case R.id.layout_description:
                productdecriptionlayout.setVisibility(View.VISIBLE);
                reviewslayout.setVisibility(View.GONE);
                reviewnotavailable.setVisibility(View.GONE);
                break;
            case R.id.text_description:
                productdecriptionlayout.setVisibility(View.VISIBLE);
                reviewslayout.setVisibility(View.GONE);
                reviewnotavailable.setVisibility(View.GONE);
                break;
            case R.id.layout_reviews:
                productdecriptionlayout.setVisibility(View.GONE);
                if(orderforreview.equalsIgnoreCase("true"))
                {
                    reviewnotavailable.setVisibility(View.GONE);
                    reviewslayout.setVisibility(View.VISIBLE);
                }
                else {
                    reviewnotavailable.setVisibility(View.VISIBLE);
                    reviewnotavailable.setText("No review available. Be the First customer to review this product.\n You can add review once you purchase this product!!");
                }
                break;
            case R.id.btn_submit:
//                mobile=edtx_mobile.getText().toString();
//                email=edtx_email.getText().toString();
//                firstname=edtx_firstname.getText().toString();
                reviewtitle=edtx_reviewtitle.getText().toString();
                review=edtx_addreview.getText().toString();
                ratingvalue=String.valueOf(rb_ratingBar.getRating());
                if(ratingvalue.equalsIgnoreCase(""))
                {ratingvalue="0"; }
                try {
                    InsertReview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.txv_addcart:
               try{
                   if(product_quantity.equalsIgnoreCase("0") || product_quantity.isEmpty() || product_quantity.equalsIgnoreCase(null))
                    {  Toast.makeText(getBaseContext(),"Quantity out of stock!",Toast.LENGTH_LONG).show();
                    }
                   else if(Integer.parseInt(Quantity)>100)
                   {
                       Toast.makeText(getBaseContext(),"sorry,Quantity limit cannot exceed more than 100!!",Toast.LENGTH_LONG).show();
                   }
                   else {
                    addcart = "1";
                    pincode = edtx_pincode.getText().toString();
                    if(pincode.isEmpty())
                    {
                        Toast.makeText(this, "Please fill out this Pincode field", Toast.LENGTH_SHORT).show();
                    }else if (psize.equalsIgnoreCase("1") && sizespinner.getSelectedItem().toString().trim().equals("Select_Size")) {
                        Toast.makeText(this, "Please Select Size", Toast.LENGTH_SHORT).show();
                    } else if (pcolor.equalsIgnoreCase("1") && colorspinner.getSelectedItem().toString().trim().equals("Select_Color")) {
                        Toast.makeText(this, "Please Select Color", Toast.LENGTH_SHORT).show();
                    } else if (pincode.equalsIgnoreCase("") || pincode.isEmpty() || pincode.length() < 6 || pincodeerror.equalsIgnoreCase("0")) {
                        checkpincode();
                    } else {
                            addCart();
                    }
                }
               } catch (Exception e) {
                   e.printStackTrace();
               }
                break;
            case R.id.txv_buy_now:
                try {
                    if (product_quantity.equalsIgnoreCase("0") || product_quantity.isEmpty() || product_quantity.equalsIgnoreCase(null)) {
                        Toast.makeText(getBaseContext(), "Quantity out of stock!", Toast.LENGTH_LONG).show();
                    }else if(Integer.parseInt(Quantity)>100)
                    {
                        Toast.makeText(getBaseContext(),"sorry,Quantity limit cannot exceed more than 100!!",Toast.LENGTH_LONG).show();
                    }
                    else {

                        addcart = "0";
                        pincode = edtx_pincode.getText().toString();
                        if (psize.equalsIgnoreCase("1") && sizespinner.getSelectedItem().toString().trim().equals("Select_Size")) {
                            Toast.makeText(this, "Please Select Size", Toast.LENGTH_SHORT).show();
                        } else if (pcolor.equalsIgnoreCase("1") && colorspinner.getSelectedItem().toString().trim().equals("Select_Color")) {
                            Toast.makeText(this, "Please Select Color", Toast.LENGTH_SHORT).show();
                        } else if (pincode.equalsIgnoreCase("") || pincode.isEmpty() || pincode.length() < 6 || pincodeerror.equalsIgnoreCase("0")) {
                            checkpincode();
                        } else {
                            addCart();
                        }
                    }
                }catch (Exception e){}
                break;
        }
    }
    private void showFileChooser() throws Exception {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override//handling the image chooser activity result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                upload_img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public String getPath(Uri uri) throws Exception {//method to get the file path from uri
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        assert cursor != null;
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
     private void requestStoragePermission() throws Exception {   //Requesting permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {      }
        //And finally ask for the permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
    @Override    //This method will be called when the user will tap on allow or deny
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    //In this method we will do the create operation
    private void addCart() throws Exception {
        if (filePath != null) {
            uploadMultipart();
            Log.d("path", product_superimpose_image);
            superimpose_text = edtx_text.getText().toString();
            if (superimpose_text.isEmpty() || superimpose_text.equalsIgnoreCase("") || superimpose_text.equalsIgnoreCase(null)) {
                superimpose_text = "";
            }
            imageName = "uploads/order/" + GlobalVariable.user_id + "/" + imageName;
        } else {
            imageName = "";
            product_superimpose_image = "";
            superimpose_text = "";
        }
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM CartTable  WHERE   product_id = '" + product_iddetail + "' ", null);
        Log.d("cursor.getCount()", Integer.toString(cursor.getCount()));
        if (cursor.getCount() == 0)
        {
            String subtotal = "0";
            String subtotalstr = Float.toString(Float.parseFloat(product_price) * Float.parseFloat(Quantity));
            String[] parts = subtotalstr.split("\\.");
            subtotal = parts[0];
                if (inputsAreCorrect(product_iddetail, product_quantity, Quantity, "0", product_price, product_name, product_image, product_shipping_cost, "0", product_sizeid, product_colorid, product_size, product_color, imageName, superimpose_text, pincode, product_with_package, "0", subtotal, estimated_days)) {

                    String insertSQL = "INSERT INTO CartTable \n" +
                            "(product_id,product_quantity,qty, discount, price,name, src,product_shipping_cost, options,order_size_id,order_color_id, order_size, order_color, superimpose_image,superimpose_text,customer_pincode, product_with_package, rowid , subtotal,estimated_days)\n" +
                            "SELECT * FROM  \n" +
                            "( SELECT  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)AS tmp " +
                            "WHERE  NOT EXISTS \n" +
                            "        (   SELECT  product_id FROM    CartTable \n" +
                            "            WHERE   product_id = '" + product_iddetail + "');";

                    Log.d("query", insertSQL);
                    //using the same method execsql for inserting values
                    mDatabase.execSQL(insertSQL, new String[]{product_iddetail, product_quantity, Quantity, "0", product_price, product_name, product_image, product_shipping_cost, "0", product_sizeid, product_colorid, product_size, product_color, imageName, superimpose_text, pincode, product_with_package, "0", subtotal, estimated_days});
                }
                if (addcart.equalsIgnoreCase("1")) {
                        Toast.makeText(this, "Product Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                 }
        }else
        {
                if (cursor.moveToFirst()) {
                    do {
                        String qty = Float.toString(Float.parseFloat(cursor.getString(cursor.getColumnIndex("qty"))) + Float.parseFloat(Quantity));
                        String[] parts1 = qty.split("\\.");
                        Quantity = parts1[0];
                        String subtotal1 = Float.toString(Float.parseFloat(product_price) * Float.parseFloat(Quantity));
                        String[] parts11 = subtotal1.split("\\.");
                        subtotal1 = parts11[0];
                        mDatabase.execSQL("Update CartTable set product_quantity='" + product_quantity + "',qty='" + Quantity + "',discount='" + "0" + "', price='" + product_price + "',name='" + product_name + "'" +
                                ",src='" + product_image + "',product_shipping_cost='" + product_shipping_cost + "',options='" + "0" + "',order_size_id='" + product_sizeid + "',order_color_id='" + product_colorid + "',order_size='" + product_size + "'" +
                                ",order_color='" + product_color + "',superimpose_image='" + imageName + "',superimpose_text='" + superimpose_text + "',customer_pincode='" + pincode + "',product_with_package='" + product_with_package + "',rowid='" + "0" + "',subtotal='" + subtotal1 + "',estimated_days='" + estimated_days + "' WHERE   product_id = '" + product_iddetail + "';");

                        if (addcart.equalsIgnoreCase("1")) {
                            Toast.makeText(this, "Product Updated to Cart Successfully", Toast.LENGTH_SHORT).show();
                        }
                     } while (cursor.moveToNext());
                }
        }
            cursor.close();
            clear();
            if (addcart.equalsIgnoreCase("0")) {
                Intent i = new Intent(ProductDetails.this, Activity_CheckoutPage.class);
                startActivity(i);
                finish();
            }
    }
    void clear()
    {
        edtx_pincode.setText("");
        layoutshipping.setVisibility(View.GONE);
        txv_pincodenotavailable.setVisibility(View.GONE);
        upload_img.setImageResource(R.drawable.yourlogo);
        edtx_text.setText("");
    }
    private void InsertReview() throws Exception {
        boolean isValid = true;

        if (reviewtitle.isEmpty()) {
            reviewtitleinputlayout.setError("Please fill out this field.");
            isValid = false;
        } else {
            reviewtitleinputlayout.setErrorEnabled(false);
        }
        if (review.isEmpty()) {
            addreviewinputlayout.setError("Please fill out this field.");
            isValid = false;
        } else {
            addreviewinputlayout.setErrorEnabled(false);
        }
//        if (firstname.isEmpty()) {
//            firstinputlayout.setError("Please fill out this field.");
//            isValid = false;
//        } else {
//            firstinputlayout.setErrorEnabled(false);
//        }
//        if (mobile.isEmpty()) {
//            mobileinputlayout.setError("mobile should be valid");
//            isValid = false;
//        } else if (mobile.length()<10) {
//            mobileinputlayout.setError("mobile should be valid");
//            isValid = false;
//        } else {
//            mobileinputlayout.setErrorEnabled(false);
//        }
//        if (email.isEmpty()) {
//            emailinputlayout.setError("email is mandatory");
//            isValid = false;
//        } else  if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
//        {
//            emailinputlayout.setError("email should be valid");
//            isValid = false;
//        } else {
//            emailinputlayout.setErrorEnabled(false);
//        }
        if (isValid) {
            new InsertProductReview().execute();
        }
    }
    private void checkpincode() throws Exception {
        boolean isValid = true;
        if (pincode.isEmpty()) {
            pincodeinputlayout.setError("Please fill out this field.");
            isValid = false;
        }else if(pincodeerror.equalsIgnoreCase("0")){
            pincodeinputlayout.setError("Sorry! Service not available");
            isValid = false;
        }
        else {
            pincodeinputlayout.setErrorEnabled(false);
        }
        if (pincode.length()<6) {
            pincodeinputlayout.setError("picode should be valid");
            isValid = false;
        } else {
            pincodeinputlayout.setErrorEnabled(false);
        }
        if (isValid) {
            new CkeckPincode().execute();
        }
    }
    void Clear()
    {
        edtx_mobile.setText("");
        edtx_email.setText("");
        edtx_firstname.setText("");
        edtx_addreview.setText("");
        edtx_reviewtitle.setText("");
        rb_ratingBar.setRating(0.0f);
        edtx_text.setText("");
    }
    class Retrive_skus extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("product_id", product_iddetail);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category+"Retrive_skus", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
       }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Retrive_mycartlist:",s);
            if(s.contains("No Results Found")) {
            }
            else {
                JSONArray jsonArray = null;
                try {
                    sizearray=new ArrayList<>();
                    colorarray=new ArrayList<>();
                    sizearray.add(new Item("Select_Size",0,product_price,"0",product_quantity));
                    colorarray.add(new Item("Select_Color",0,product_price,"0",product_quantity));
                    Item item,item1;
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String vid = jsonObject.getString("sku_id");
                        String size_option = jsonObject.getString("size_option");
                        String size_id = jsonObject.getString("id");
                        String size_name = jsonObject.getString("size");
                        String color_id = jsonObject.getString("color_id");
                        String color_name = jsonObject.getString("color_name");
                        String product_model = jsonObject.getString("product_model");
                        String price = jsonObject.getString("price");
                        String discount = jsonObject.getString("discount");
                        String discounted_price = jsonObject.getString("discounted_price");
                        String quantity = jsonObject.getString("quantity");
                        if(size_id!= null && !size_id.isEmpty() && !size_id.equals("null"))
                        {
                            item = new Item(size_name, Integer.parseInt(size_id), price, vid,quantity);
                            sizearray.add(item);
                        }
                        if(color_id!= null && !color_id.isEmpty() && !color_id.equals("null"))
                        {
                            item1 = new Item(color_name, Integer.parseInt(color_id), price, vid,quantity);
                            colorarray.add(item1);
                        }
                    }
                    ArrayAdapter spinner1 = new ArrayAdapter(getBaseContext(), R.layout.spinner_item, sizearray);
                    spinner1.setDropDownViewResource(R.layout.spinner_item);
                    sizespinner.setAdapter(spinner1);
                    ArrayAdapter spinner2 = new ArrayAdapter(getBaseContext(), R.layout.spinner_item, colorarray);
                    spinner2.setDropDownViewResource(R.layout.spinner_item);
                    colorspinner.setAdapter(spinner2);
                } catch (Exception e) {e.getMessage();}
            }
        }
    }
    class InsertProductReview extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("product_id", product_iddetail);
            params.put("name","");
            params.put("email","");
            params.put("mobile","");
            params.put("user_id",GlobalVariable.user_id);
            params.put("reviewtitle", reviewtitle);
            params.put("review", review);
            params.put("review_status", ratingvalue);
            params.put("rating", ratingvalue);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"InsertProductReview", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            Log.d("ss",s);
            super.onPostExecute(s);
            Toast.makeText(getBaseContext(),s, Toast.LENGTH_LONG).show();
            Clear();
        }
    }
    class CkeckPincode extends AsyncTask<Void, Void, String> {
        private static final String TAG ="errormsg" ;
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("pincode", pincode);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"CkeckPincode", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("No Results Found")) {
                txv_pincodenotavailable.setVisibility(View.VISIBLE);
                txv_pincodenotavailable.setTextColor(Color.parseColor("#ED0707"));
                txv_pincodenotavailable.setText("Sorry! Service not available.");
                layoutshipping.setVisibility(View.GONE);
                pincodeerror="0";
            }
            else {
                pincodeerror="1";
                pincodeinputlayout.setErrorEnabled(false);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String pin_id = jsonObject.getString("pin_id");
                        String city_id = jsonObject.getString("city_id");
                        String  area = jsonObject.getString("area");
                        retrivepin_code = jsonObject.getString("pin_code");
                        estimated_days = jsonObject.getString("estimated_days");
                    }
                    if(retrivepin_code.toString().equalsIgnoreCase(pincode.toString()))
                    {
                        layoutshipping.setVisibility(View.VISIBLE);
                        txv_pincodenotavailable.setVisibility(View.VISIBLE);
                        txv_pincodenotavailable.setTextColor(Color.parseColor("#50a4b9"));
                        txv_pincodenotavailable.setText("estimated_days " +estimated_days);
                    }
                        if(GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null")) {
                         //  addCart();
                        }
                        else
                        {
                            Intent i = new Intent(ProductDetails.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                } catch (Exception e) { e.printStackTrace();}
            }
        }
    }
      class AddToWishlist extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            if(product_quantity.equalsIgnoreCase("0") || product_quantity.isEmpty() || product_quantity.equalsIgnoreCase(null))
            {
                product_quantity="0";
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("sv_user_id", GlobalVariable.user_id);
            params.put("sv_key","my_wishlist");
            params.put("id",product_iddetail);
            params.put("name",product_name);
            params.put("price",product_price);
            params.put("src",product_image);
            params.put("product_shipping_cost",product_shipping_cost);
            params.put("max_quantity",product_quantity);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"addtowishlist", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getBaseContext(), s.toString(), Toast.LENGTH_LONG).show();
//            if (s.equalsIgnoreCase("\"Item Successfully removed from Wishlist\"")) {
//                Intent i = new Intent(getBaseContext(), ProductDetails.class);
//                startActivity(i);
//                finish();
//            }
        }

    }
    class Retrive_varients extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("product_id", product_iddetail);
            params.put("variant_id", variant_id);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category+"Retrive_product_variant", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("No Results Found")) {
                viewpagelayout.setVisibility(View.GONE);
            }
            else {
                viewpagelayout.setVisibility(View.VISIBLE);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    file_name = new String[jsonArray.length()];
                    varientid = new String[jsonArray.length()];
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        varientid[i] = jsonObject.getString("id");
                        file_name[i] = jsonObject.getString("file_name");
                    }
                    init();
                    addBottomDots(0);                    // method for adding indicators
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
       private void init() throws Exception{ //imageslider
        slider_image_list = new ArrayList<>();
        for (int i = 0; i < file_name.length; i++) {
            categoryitems items = new categoryitems();
            items.setimage_url("http://www.tohafaa.com/uploads/variant/"+variant_id+"/"+file_name[i]);
            slider_image_list.add(items);
        }
        sliderPagerAdapter = new SliderPagerAdapter(ProductDetails.this, slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);
           grid_item_image.setVisibility(View.GONE);
        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {            }
            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void uploadMultipart() throws Exception{
        product_superimpose_image = getPath(filePath);
        File f = new File(product_superimpose_image);        //for image name
        imageName = f.getName();
        try {
            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(this, uploadId, URLs.URL_upload) //Creating a multi part request
                    .addFileToUpload(product_superimpose_image, "image") //Adding file
                    .addParameter("name",imageName)
                    .addParameter("user_id",GlobalVariable.user_id)//Adding text parameter to the request
                    //  .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];
        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getBaseContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }
    //spinner choice
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinner1) {
            Quantity =  QuantityArray.get(position).toString();
        }
        else if(spinner.getId() == R.id.colorspinner)
        {
                product_color=  colorarray.get(position).getName().trim();
                product_colorid=  Integer.toString(colorarray.get(position).getid());
                variant_id=  colorarray.get(position).getvarient_id().trim();
                product_price=  colorarray.get(position).getprice().trim();
                product_quantity=  colorarray.get(position).getqty().trim();
                txv_productprice.setText(product_price);
            try {
                qty();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if("0" != variant_id.intern()) {
                    new Retrive_varients().execute();
                }
        }
        else if(spinner.getId() == R.id.sizespinner)
        {
            product_size=  sizearray.get(position).getName().trim().toString();
            product_sizeid=  Integer.toString(sizearray.get(position).getid());
            variant_id=  sizearray.get(position).getvarient_id().trim();
            product_price=  sizearray.get(position).getprice().trim();
            product_quantity=  sizearray.get(position).getqty().trim();
            try {
                qty();
            } catch (Exception e) {
                e.printStackTrace();
            }
            txv_productprice.setText(product_price);
            if("0" != variant_id.intern()) {
                new Retrive_varients().execute();
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
    class checkproductorderforreviews extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("product_id", product_iddetail);
            params.put("customer_id",GlobalVariable.user_id);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category+"checkproductorderforreviews", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Retrive_mycartlisthhhh:", s);
            if (s.contains("No Results Found")) {
                orderforreview = "false";
            } else {
                orderforreview = "true";
                try {

                    JSONArray jsonArray = null;
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    categoryitems Sort;
                    ProductReviewslist.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String review_title = jsonObject.getString("review_title");
                        String id = jsonObject.getString("id");
                        String review = jsonObject.getString("review");
                        String rating = jsonObject.getString("rating");
                        Sort = new categoryitems(id,review_title, review,rating);
                        ProductReviewslist.add(Sort);
                    }
                }catch (Exception e){}
            }
           }
         }

}
package com.montek.tohafaa;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.*;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.*;
import android.view.View;
import android.widget.*;

import com.montek.tohafaa.Adapter.WishListAdapter;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.GlobalVariable;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.model.Item;
import net.gotev.uploadservice.MultipartUploadRequest;
import org.json.*;
import java.io.File;
import java.util.*;

import static com.montek.tohafaa.Activity_WishList.cardview;
import static com.montek.tohafaa.Activity_WishList.recyclerView;
import static com.montek.tohafaa.Adapter.WishListAdapter.sv_id;
import static com.montek.tohafaa.DashBordActivity.*;
import static com.montek.tohafaa.LoginActivity.p;
import static com.montek.tohafaa.ProductDetails.*;
import static com.montek.tohafaa.RegisterActivity.pincodeerror;
public class Activity_Uploadimage extends AppCompatActivity  implements View.OnClickListener ,Spinner.OnItemSelectedListener{
    EditText edtx_pincode;
    LinearLayout sizelayout,li_qtypincode,imagelayout,colorlayout;
    ImageView upload_img;
    TextInputLayout pincodeinputlayout;
    TextView txv_outofstock,txv_pincodenotavailable,  txv_productshipingcost;
    LinearLayout li_qty,layoutshipping;
    ArrayList<Integer> QuantityArray;
    Spinner spin1,sizespinner,colorspinner;
    private Uri filePath;
    private Bitmap bitmap;
    EditText edtx_text;
    TextView txv_check;
    Button btn_addtocart;
    String imageName,retrivepin_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpincodeqty);
       try {
           ActionBar actionBar = getSupportActionBar();
           if (actionBar != null) {
               actionBar.setTitle("Product Details");
           }
           p = new ProgressDialog(this);
           UI();
           new Retrive_productwishlisttocart().execute();
           new Retrive_skus().execute();
           edtx_pincode.addTextChangedListener(new TextWatcher() {
               @Override
               public void afterTextChanged(Editable s) {
               }
               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               }
               @Override
               public void onTextChanged(CharSequence s, int start,
                                         int before, int count) {
                   sv_customer_pincode = edtx_pincode.getText().toString().trim();
                   if (s.length() != 0 && s.length() > 5) {
                       new CkeckPincode().execute();
                   }
               }
           });
           if (sv_product_qty.equalsIgnoreCase("0") || sv_product_qty.isEmpty() || sv_product_qty.equalsIgnoreCase(null) || sv_product_qty.equalsIgnoreCase("")) {
               li_qty.setEnabled(false);
               li_qtypincode.setVisibility(View.GONE);
               txv_outofstock.setVisibility(View.VISIBLE);
           } else {
               li_qtypincode.setVisibility(View.VISIBLE);
               li_qty.setEnabled(true);
               txv_outofstock.setVisibility(View.GONE);
           }
           Quantity(sv_product_qty);
       }catch (Exception e){}
    }
    void UI() throws Exception
    {
        imagelayout = (LinearLayout) findViewById(R.id.imagelayout);
        colorlayout = (LinearLayout) findViewById(R.id.colorlayout);
        sizelayout = (LinearLayout) findViewById(R.id.sizelayout);
        edtx_pincode = (EditText) findViewById(R.id.edtx_pincode);
        sizespinner = (Spinner) findViewById(R.id.sizespinner);
        sizespinner.setOnItemSelectedListener(this);
        colorspinner = (Spinner) findViewById(R.id.colorspinner);
        colorspinner.setOnItemSelectedListener(this);
        pincodeinputlayout = (TextInputLayout) findViewById(R.id.pincodeinputlayout);
        btn_addtocart = (Button)findViewById(R.id.btn_addtocart);
        btn_addtocart.setOnClickListener(this);
        txv_check = (TextView) findViewById(R.id.txv_check);
        txv_outofstock = (TextView) findViewById(R.id.txv_outofstock);
        li_qtypincode = (LinearLayout) findViewById(R.id.li_qtypincode);
        edtx_text = (EditText) findViewById(R.id.edtx_text);
        txv_check.setOnClickListener(this);
        li_qty = (LinearLayout)findViewById(R.id.li_qty);
        upload_img = (ImageView) findViewById(R.id.upload_img);
        upload_img.setOnClickListener(this);
        txv_productshipingcost = (TextView) findViewById(R.id.txv_productshipingcost);
        txv_pincodenotavailable = (TextView)findViewById(R.id.txv_pincodenotavailable);
        txv_pincodenotavailable.setVisibility(View.GONE);
        layoutshipping = (LinearLayout)findViewById(R.id.layoutshipping);
        layoutshipping.setVisibility(View.GONE);
        spin1 = (Spinner) findViewById(R.id.spinner1);
        spin1.setOnItemSelectedListener(this);
    }
    void Quantity(String quantity) throws Exception
    {
        QuantityArray = new ArrayList<>();
        for(int i=1;i<=Integer.parseInt(quantity);i++)
        {
            QuantityArray.add(i);
        }
        ArrayAdapter spinner1 = new ArrayAdapter(getBaseContext(), R.layout.spinner_item, QuantityArray);//Quantity Spinner
        spinner1.setDropDownViewResource(R.layout.spinner_item);
        spin1.setAdapter(spinner1);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txv_check:
                sv_customer_pincode = edtx_pincode.getText().toString();
                try {
                    checkpincode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.upload_img:
                requestStoragePermission();
                try {
                    showFileChooser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_addtocart:
                try {
                    if (estimated_days.isEmpty() || estimated_days.equalsIgnoreCase("") || estimated_days.equalsIgnoreCase(null) || estimated_days.equalsIgnoreCase("0")) {
                        Toast.makeText(getBaseContext(), "Please Check PinCode", Toast.LENGTH_LONG).show();
                    } else if (sv_product_qty.equalsIgnoreCase("0") || sv_product_qty.isEmpty() || sv_product_qty.equalsIgnoreCase(null) || sv_product_qty.equalsIgnoreCase("")) {
                        li_qty.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(), "Sorry,quantity out of stock.Please Try Again Later", Toast.LENGTH_LONG).show();

                    } else if(Integer.parseInt(sv_qty)>100)
                    {
                        Toast.makeText(getBaseContext(),"sorry,Quantity limit cannot exceed more than 100!!",Toast.LENGTH_LONG).show();
                    }
                    else if ((sv_order_size.equalsIgnoreCase("1") || sv_order_size.equalsIgnoreCase("Select_Size")) && sizespinner.getSelectedItem().toString().trim().equals("Select_Size")) {
                        Toast.makeText(this, "Please Select Size", Toast.LENGTH_SHORT).show();
                    } else if ((sv_order_color.equalsIgnoreCase("1") || sv_order_color.equalsIgnoreCase("Select_Color")) && colorspinner.getSelectedItem().toString().trim().equals("Select_Color")) {
                        Toast.makeText(this, "Please Select Color", Toast.LENGTH_SHORT).show();
                    } else if (sv_customer_pincode.equalsIgnoreCase("") || sv_customer_pincode.isEmpty() || sv_customer_pincode.length() < 6 || pincodeerror.equalsIgnoreCase("0")) {
                        checkpincode();
                    } else {
                        insert();
                    }
                  }catch (Exception e){}
                break;
        }
    }
    private void checkpincode() throws Exception {
        boolean isValid = true;
        if (sv_customer_pincode.isEmpty()) {
            pincodeinputlayout.setError("Please fill out this field.");
            isValid = false;
        }else if(pincodeerror.equalsIgnoreCase("0")){
            pincodeinputlayout.setError("Sorry! Service not available");
            isValid = false;
        }
        else {
            pincodeinputlayout.setErrorEnabled(false);
        }
        if (sv_customer_pincode.length()<6) {
            pincodeinputlayout.setError("pincode should be valid");
            isValid = false;
        } else {
            pincodeinputlayout.setErrorEnabled(false);
        }
        if (isValid) {
            new CkeckPincode().execute();
        }
    }
    void insert() throws Exception
    {    mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        String subtotal= Float.toString(Float.parseFloat(sv_cartprice)*Float.parseFloat(sv_qty));
        String[] parts1 = subtotal.split("\\.");
        sv_subtotal = parts1[0];
        if (filePath != null) {
            uploadMultipart();
            sv_superimpose_text=edtx_text.getText().toString();
            if(sv_superimpose_text.isEmpty())
            {
                sv_superimpose_text="";
            }
            imageName="uploads/order/"+ GlobalVariable.user_id+"/"+imageName;
        }
        else
        {
            imageName="";
            sv_superimpose_image="";
            sv_superimpose_text="";
        }
        qtyupdate();
        new Delete_wishlistproduct().execute();
    }
      private void showFileChooser() throws Exception {   //method to show file chooser
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
            }catch (Exception e) {e.getMessage();}
        }
    }
      public String getPath(Uri uri) throws Exception  {  //method to get the file path from uri
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
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void uploadMultipart() throws Exception {
        sv_superimpose_image = getPath(filePath);
        File f = new File(sv_superimpose_image); //for image name
        imageName = f.getName();
               try {
            String uploadId = UUID.randomUUID().toString(); //Uploading code
            new MultipartUploadRequest(getBaseContext(), uploadId, URLs.URL_upload)  //Creating a multi part request
                    .addFileToUpload(sv_superimpose_image, "image") //Adding file
                    .addParameter("name",imageName)
                    .addParameter("user_id",GlobalVariable.user_id)//Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(getBaseContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public static void Updatevaluesintodb()
    {     mDatabase.execSQL("Update CartTable set product_quantity='"+sv_product_qty+"',qty='"+sv_qty+"',discount='"+"0"+"', price='"+sv_cartprice+"',name='"+sv_cartname+"'" +
                ",src='"+sv_cartsrc+"',product_shipping_cost='"+sv_product_shipping_cost+"',options='"+"0"+"',order_size_id='"+sv_order_size_id+"',order_color_id='"+sv_order_color_id+"',order_size='"+sv_order_size+"'" +
                ",order_color='"+sv_order_color+"',superimpose_image='"+sv_superimpose_image+"',superimpose_text='"+sv_superimpose_text+"',customer_pincode='"+sv_customer_pincode+"',product_with_package='"+sv_product_with_package+"',rowid='"+"0"+ "',subtotal='"+sv_subtotal+"',estimated_days='"+estimated_days+"' WHERE   product_id = '" + sv_product_id +"';");
    }
    private void qtyupdate() throws Exception {
        if(imageName.isEmpty())
        {
            imageName="";
            sv_superimpose_text="";
        }
        sv_superimpose_image=imageName;
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM CartTable  WHERE   product_id = '" + sv_product_id +"' ", null);
        if(cursor.getCount()==0)
        {
            Insertvaluesintodb();
        }
        if (cursor.moveToFirst()) {
            do {
                String qty = Float.toString(Float.parseFloat(cursor.getString(cursor.getColumnIndex("qty")))+Float.parseFloat(sv_qty));
                String[] parts = qty.split("\\.");
                sv_qty = parts[0];
                String subtotal= Float.toString(Float.parseFloat(sv_cartprice)*Float.parseFloat(sv_qty));
                String[] parts1 = subtotal.split("\\.");
                sv_subtotal = parts1[0];
                Updatevaluesintodb();
                Toast.makeText(getBaseContext(),"Product Successfully added to cart !!",Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    class Retrive_productwishlisttocart extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("product_id",sv_product_id);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category+"Retrive_productwishlisttocart", params);
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
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(s);
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    sv_order_size  = jsonObject.getString("product_size");
                    sv_order_color  = jsonObject.getString("product_color");
                    sv_superimpose_image  = jsonObject.getString("product_superimpose_image");
                    sv_product_with_package  = jsonObject.getString("product_with_package");
                    if(sv_superimpose_image.equalsIgnoreCase("1"))
                    {
                        imagelayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        imagelayout.setVisibility(View.GONE);
                    }
                    if(sv_order_size.equalsIgnoreCase("1"))
                    {
                        sizelayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        sizelayout.setVisibility(View.GONE);
                    }
                    if(sv_order_color.equalsIgnoreCase("1"))
                    {
                        colorlayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        colorlayout.setVisibility(View.GONE);
                    }
                }
            }
            catch (Exception e) { e.getMessage();}
        }
    }
    class CkeckPincode extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("pincode", sv_customer_pincode);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"CkeckPincode", params);
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
                    if(retrivepin_code.equalsIgnoreCase(sv_customer_pincode))
                    {
                        layoutshipping.setVisibility(View.VISIBLE);
                        txv_pincodenotavailable.setVisibility(View.VISIBLE);
                        txv_pincodenotavailable.setTextColor(Color.parseColor("#50a4b9"));
                        txv_pincodenotavailable.setText("Estimated_days : " +estimated_days);
                        txv_productshipingcost.setText(sv_product_shipping_cost);
                    }
                } catch (Exception e) { e.printStackTrace();}
            }
        }
    }
    class Retrive_skus extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("product_id", sv_product_id);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category+"Retrive_skus", params);
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
            if(s.contains("No Results Found")) {
                //     Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            else {
                JSONArray jsonArray = null;
                try {
                    sizearray=new ArrayList<>();
                    colorarray=new ArrayList<>();
                    sizearray.add(new Item("Select_Size",0,sv_cartprice,"0",sv_product_qty));
                    colorarray.add(new Item("Select_Color",0,sv_cartprice,"0",sv_product_qty));
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
                        Quantity(quantity);
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
                } catch (Exception e) { e.getMessage();}
           }
        }
    }
    class Delete_wishlistproduct extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("sv_id",sv_id);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"Delete_wishlist", params);
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
//            WishListAdapter.item1.remove(pos);
//            if (item1.size() == 0) {
//                recyclerView.setAdapter(null);
//                cardview.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.GONE);
//            }
////            Intent i=new Intent(context,WishList.cl)
//            notifyDataSetChanged();
            Intent i=new Intent(getBaseContext(), Activity_WishList.class);
            startActivity(i);
            finish();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId()==R.id.spinner1)
        {
            sv_qty = parent.getItemAtPosition(position).toString();
        }
         if(spinner.getId() == R.id.colorspinner)
        {
            sv_order_color=  colorarray.get(position).getName().trim();
            sv_order_color_id=  Integer.toString(colorarray.get(position).getid());
            sv_cartprice=  colorarray.get(position).getprice().trim();
            sv_product_qty=  colorarray.get(position).getqty().trim();
        }
        else if(spinner.getId() == R.id.sizespinner)
        {
            sv_order_size=  sizearray.get(position).getName().trim();
            sv_order_size_id=  Integer.toString(sizearray.get(position).getid());
            sv_cartprice=  sizearray.get(position).getprice().trim();
            sv_product_qty=  sizearray.get(position).getqty().trim();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

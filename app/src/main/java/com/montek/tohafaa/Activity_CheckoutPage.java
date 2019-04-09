package com.montek.tohafaa;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.*;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.montek.tohafaa.Adapter.CartCheckoutAdapter;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.GlobalVariable;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.model.*;
import org.json.*;
import java.util.*;
import static com.montek.tohafaa.Activity_CartList.*;
import static com.montek.tohafaa.Activity_OrdersList.dialog;
import static com.montek.tohafaa.Activity_WebViewofInvoice.*;
import static com.montek.tohafaa.LoginActivity.p;
import static com.montek.tohafaa.ProductDetails.addcart;
import static com.montek.tohafaa.ProductDetails.product_iddetail;
import static com.montek.tohafaa.RegisterActivity.*;
public class Activity_CheckoutPage  extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    CheckBox checkbox;
    LinearLayout li_chechbox,li_ordersummarydetails,li_ordersummary,li_billingaddressdetail,li_billingaddress;
    public static CardView cardview;
    TextView orderconfirmation,txv_state,txv_city,txv_ordersummaryup,txv_ordersummarydown,txv_billingaddressdownicon,txv_billingaddressupicon,txv_email,txv_name;
    public static EditText edtx_pincode,edtx_firstname, edtx_lastname, edtx_email, edtx_mobile, edtx_addressline1, edtx_addressline2;
    public static TextInputLayout pincodeinputlayout,mobileinputlayout, emailinputlayout, address2inputlayout, address1inputlayout, firstinputlayout, lastinputlayout;
    public static Spinner spin_country, spin_City_District_Town, spin_State;
    Button btn_confirmorder,btn_update, btn_signup;
    String country_id, state_id, city_id;
    public static String payment_method,shipping_address_same_as_billing_address="1";
    CartCheckoutAdapter adapter;
    RadioGroup rbt_stausGroup;
    public static  ArrayList<String> CheckoutTablearray= new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutpage);
       try {
           shipping_address_same_as_billing_address="1";
           actionBar = getSupportActionBar();
           Actionbar("CHECKOUT");
           UI();
           p = new ProgressDialog(this);
           new RetriveCountry().execute();
           orderconfirmation.setText("Order confirmation email will be sent to " + GlobalVariable.email);
           CartList = new ArrayList<>();
           mDatabase = openOrCreateDatabase(ProductDetails.DATABASE_NAME, MODE_PRIVATE, null);        //opening the database
           createCheckoutTable();   //this method will display the employees in the list
           CheckoutTablearray.clear();
           showCartFromDatabase();
           checkbox.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (((CheckBox) v).isChecked()) {                //is chkIos checked?
                       li_billingaddressdetail.setVisibility(View.VISIBLE);
                       txv_billingaddressupicon.setVisibility(View.GONE);
                       txv_billingaddressdownicon.setVisibility(View.VISIBLE);
                       shipping_address_same_as_billing_address = "1";
                   } else {
                       shipping_address_same_as_billing_address = "0";
                   }
               }
           });
           edtx_pincode.addTextChangedListener(new TextWatcher() {
               @Override
               public void afterTextChanged(Editable s) {
               }
               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               }
               @Override
               public void onTextChanged(CharSequence s, int start,int before, int count) {
                   pincode = edtx_pincode.getText().toString().trim();
                   if (s.length() != 0 && s.length() > 5) {
                       new CkeckPincode().execute();
                   }
               }
           });
       }catch (Exception e){}
    }

    void hiddenkeybord()
    {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void showCartFromDatabase() throws Exception{
        if(addcart.equalsIgnoreCase("0")) {
            cursor = mDatabase.rawQuery("SELECT * FROM CartTable where product_id = '" + product_iddetail + "' ", null);
        }else{
        cursor = mDatabase.rawQuery("SELECT * FROM CartTable", null);
        }
        Count=cursor.getCount();
        Log.d("Count", String.valueOf(Count));
          resultSet = new JSONArray();
        if (cursor.moveToFirst()) {
            CartList.clear();
            do {
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
                        {   if( cursor.getString(i) != null )
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
                        catch( Exception e ){}
                     }
                }
                resultSet.put(rowObject);
                CartList.add(new CartModel(  //pushing each record in the employee list
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
        cursor.close();  //closing the cursor
        adapter = new CartCheckoutAdapter(this, R.layout.layout_ordersummary_item, CartList, mDatabase);//creating the adapter object
        listView.setAdapter(adapter); //adding the adapter to listview
    }
   void UI() throws Exception
   {
//       rbt_stausGroup = (RadioGroup) findViewById(R.id.rbt_stausGroup);
       checkbox = (CheckBox)findViewById(R.id.checkbox);
       li_billingaddress = (LinearLayout)findViewById(R.id.li_billingaddress);
       li_billingaddressdetail = (LinearLayout)findViewById(R.id.li_billingaddressdetail);
      // btn_confirmorder = (Button)findViewById(R.id.btn_confirmorder);
      // btn_confirmorder.setOnClickListener(this);
       li_ordersummarydetails = (LinearLayout)findViewById(R.id.li_ordersummarydetails);
       li_ordersummary = (LinearLayout)findViewById(R.id.li_ordersummary);
//       li_paymentoption = (LinearLayout)findViewById(R.id.li_paymentoption);
//       li_paymentoption.setVisibility(View.GONE);
       orderconfirmation = (TextView)findViewById(R.id.orderconfirmation);
//       txv_paymentoptiondown = (TextView)findViewById(R.id.txv_paymentoptiondown);
//       txv_paymentoptiondown.setOnClickListener(this);
//       txv_paymentoptionup = (TextView)findViewById(R.id.txv_paymentoptionup);
//       txv_paymentoptionup.setOnClickListener(this);
       txv_billingaddressupicon = (TextView)findViewById(R.id.txv_billingaddressupicon);
       txv_billingaddressupicon.setOnClickListener(this);
       txv_billingaddressdownicon = (TextView)findViewById(R.id.txv_billingaddressdownicon);
       txv_billingaddressdownicon.setOnClickListener(this);
       txv_ordersummarydown = (TextView)findViewById(R.id.txv_ordersummarydown);
       txv_ordersummarydown.setOnClickListener(this);
       txv_ordersummaryup = (TextView)findViewById(R.id.txv_ordersummaryup);
       listView = (ListView) findViewById(R.id.listView);
       txv_ordersummaryup.setOnClickListener(this);
       txv_name = (TextView)findViewById(R.id.txv_name);
       txv_name.setText(GlobalVariable.first_name+" "+GlobalVariable.last_name);
       txv_email = (TextView)findViewById(R.id.txv_email);
       txv_email.setText(GlobalVariable.email);
       edtx_firstname = (EditText) findViewById(R.id.edtx_firstname);
       edtx_firstname.setText(GlobalVariable.first_name);
       edtx_lastname = (EditText) findViewById(R.id.edtx_lastname);
       edtx_email = (EditText) findViewById(R.id.edtx_email);
       edtx_mobile = (EditText) findViewById(R.id.edtx_mobile);
       edtx_addressline1 = (EditText) findViewById(R.id.edtx_addressline1);
       edtx_addressline2 = (EditText) findViewById(R.id.edtx_addressline2);
       edtx_pincode = (EditText) findViewById(R.id.edtx_pincode);
       firstinputlayout = (TextInputLayout) findViewById(R.id.firstinputlayout);
       lastinputlayout = (TextInputLayout) findViewById(R.id.lastinputlayout);
       mobileinputlayout = (TextInputLayout) findViewById(R.id.mobileinputlayout);
       address1inputlayout = (TextInputLayout) findViewById(R.id.address1inputlayout);
       address2inputlayout = (TextInputLayout) findViewById(R.id.address2inputlayout);
       emailinputlayout = (TextInputLayout) findViewById(R.id.emailinputlayout);
       pincodeinputlayout = (TextInputLayout) findViewById(R.id.pincodeinputlayout);
       edtx_lastname.setText(GlobalVariable.last_name);
       edtx_email.setText(GlobalVariable.email);
       edtx_email.setEnabled(false);
       edtx_email.setTextColor(getResources().getColor(android.R.color.black));
       edtx_mobile.setText(GlobalVariable.mobile);
       edtx_addressline1.setText(GlobalVariable.address_line_1);
       address1inputlayout.setHint("Locality");
       edtx_addressline2.setText(GlobalVariable.address_line_2);
       spin_country = (Spinner) findViewById(R.id.spin_country);
       spin_country.setOnItemSelectedListener(this);
       spin_City_District_Town = (Spinner) findViewById(R.id.spin_City_District_Town);
       spin_City_District_Town.setOnItemSelectedListener(this);
       spin_State = (Spinner) findViewById(R.id.spin_State);
       spin_State.setOnItemSelectedListener(this);
       btn_signup=(Button)findViewById(R.id.btn_signup);
       btn_signup.setOnClickListener(this);
       btn_signup.setText("Submit");
       btn_update=(Button)findViewById(R.id.btn_update);
       btn_update.setOnClickListener(this);
       Button btn_submit=(Button)findViewById(R.id.btn_submit);
       btn_submit.setVisibility(View.GONE);
       spin_City_District_Town.setVisibility(View.GONE);
       spin_State.setVisibility(View.GONE);
       txv_billingaddressupicon.setVisibility(View.GONE);
       txv_billingaddressdownicon.setVisibility(View.VISIBLE);
       txv_ordersummaryup.setVisibility(View.GONE);
       txv_ordersummarydown.setVisibility(View.VISIBLE);
       li_ordersummary.setVisibility(View.GONE);
       li_ordersummarydetails.setVisibility(View.GONE);
       li_chechbox=(LinearLayout)findViewById(R.id.li_chechbox);
       txv_state = (TextView) findViewById(R.id.txv_state);
       txv_city = (TextView) findViewById(R.id.txv_city);
       txv_city.setVisibility(View.GONE);
       txv_state.setVisibility(View.GONE);
   }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getBaseContext(), Activity_CartList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), Activity_CartList.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        this.finish();
    }

    void diglogbox()
    {
        final String[] select={"Pay Online"};//,"BHIM","Paytm","Mobikwik"};
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("Select Payment Mode")
                .setSingleChoiceItems(select,0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        payment_method = String.valueOf(which);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Toast.makeText(getBaseContext(), "Your State is "+ which , Toast.LENGTH_SHORT).show();
                        try {
                            TakevalueCarttableFromDatabase();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .create();
        dialog.show();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
               try {
                   Cursor cursor1;
                   if (addcart.equalsIgnoreCase("0")) {
                       cursor = mDatabase.rawQuery("SELECT * FROM CartTable where product_id = '" + product_iddetail + "' ", null);
                       cursor1 = mDatabase.rawQuery("select * from CheckoutTable where product_id = '" + product_iddetail + "' ", null);
                   } else {
                       cursor1 = mDatabase.rawQuery("select * from CheckoutTable", null);
                       cursor = mDatabase.rawQuery("SELECT * FROM CartTable", null);
                   }
                   if (shipping_address_same_as_billing_address.equalsIgnoreCase("0")) {
                       if (cursor1.getCount() == cursor.getCount()) {
                           diglogbox();
                           CheckoutTablearray.clear();
                           showCartFromDatabase();
                       } else {
                           if (cursor1.getCount() > 0) {
                               ArrayList<String> NotFilledValues = new ArrayList<>();
                               ArrayList<String> CartTablearray = new ArrayList<>();
                               CheckoutTablearray.clear();
                               cursor.moveToFirst();
                               do {
                                   String retrivename = cursor.getString(cursor.getColumnIndex("name"));
                                   CartTablearray.add(retrivename);
                               } while (cursor.moveToNext());
                               Log.d("CartTablearray", CartTablearray.toString());
                               cursor1.moveToFirst();
                               do {
                                   String retrivename = cursor1.getString(cursor1.getColumnIndex("name"));
                                   CheckoutTablearray.add(retrivename);
                               } while (cursor1.moveToNext());
                               cursor.close();
                               cursor1.close();
                               CartTablearray.removeAll(CheckoutTablearray);
                               NotFilledValues.addAll(CartTablearray);
                               CheckoutTablearray.clear();
                               CartTablearray.clear();
                               CheckoutTablearray.addAll(NotFilledValues);
                               showCartFromDatabase();
                               View view = null;
                               String value;
                               for (int i = 0; i < adapter.getCount(); i++) {
                                   view = adapter.getView(i, view, listView);
                                   TextView et = (TextView) view.findViewById(R.id.txv_name);
                                   value = et.getText().toString();
                                   for (int j = 0; j < CheckoutTablearray.size(); j++) {
                                       if (CheckoutTablearray.get(j).equals(value)) {
                                           et.setTextColor(Color.parseColor("#50a4b9"));
                                           Log.d("CheckoutTablearray)", CheckoutTablearray.get(j) + (value));
                                           Toast.makeText(getBaseContext(), "Please Fill Shipping details of this " + value, Toast.LENGTH_SHORT).show();
                                           break;
                                       }
                                   }
                               }
                           } else {
                               Toast.makeText(getBaseContext(), "Please Fill Shipping details of all products", Toast.LENGTH_SHORT).show();
                           }
                       }
                   } else {
                       diglogbox();
                   }
               }catch (Exception e){}
                break;
            case R.id.btn_signup:
                firstname = edtx_firstname.getText().toString().trim();
                lastname = edtx_lastname.getText().toString().trim();
                email = edtx_email.getText().toString().trim();
                mobile = edtx_mobile.getText().toString().trim();
                pincode = edtx_pincode.getText().toString().trim();
                addressline2 = edtx_addressline2.getText().toString().trim();
                addressline1 = edtx_addressline1.getText().toString().trim();
                try {
                    signUp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
//            case R.id.btn_confirmorder:
//               try{ payment_method = ((RadioButton) findViewById(rbt_stausGroup.getCheckedRadioButtonId())).getText().toString();
//                TakevalueCarttableFromDatabase();}catch (Exception e){}
//              break;
//            case R.id.txv_paymentoptiondown:
//                txv_paymentoptiondown.setVisibility(View.GONE);
//                txv_paymentoptionup.setVisibility(View.VISIBLE);
//                li_paymentoption.setVisibility(View.GONE);
//                break;
//            case R.id.txv_paymentoptionup:
//                txv_paymentoptionup.setVisibility(View.GONE);
//                txv_paymentoptiondown.setVisibility(View.VISIBLE);
//                li_paymentoption.setVisibility(View.VISIBLE);
//                break;
            case R.id.txv_billingaddressdownicon:
                txv_billingaddressdownicon.setVisibility(View.GONE);
                txv_billingaddressupicon.setVisibility(View.VISIBLE);
                li_billingaddressdetail.setVisibility(View.GONE);
                break;
            case R.id.txv_billingaddressupicon:
                txv_billingaddressupicon.setVisibility(View.GONE);
                txv_billingaddressdownicon.setVisibility(View.VISIBLE);
                li_billingaddressdetail.setVisibility(View.VISIBLE);
                break;
            case R.id.txv_ordersummarydown:
                txv_ordersummarydown.setVisibility(View.GONE);
                txv_ordersummaryup.setVisibility(View.VISIBLE);
                li_ordersummarydetails.setVisibility(View.GONE);
                break;
            case R.id.txv_ordersummaryup:
                txv_ordersummaryup.setVisibility(View.GONE);
                txv_ordersummarydown.setVisibility(View.VISIBLE);
                li_ordersummarydetails.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void TakevalueCarttableFromDatabase() throws Exception {
         int total=0,shipping_cost=0;
        if(addcart.equalsIgnoreCase("0")) {
            cursor = mDatabase.rawQuery("SELECT * FROM CartTable where product_id = '" + product_iddetail + "' ", null);
        }else {
            cursor = mDatabase.rawQuery("SELECT * FROM CartTable", null);
        }
        if (cursor.moveToFirst()) {
           do {
                Log.d("Count", String.valueOf(cursor.getCount()));
               total = total +(cursor.getInt(cursor.getColumnIndex("subtotal")));
               shipping_cost = shipping_cost +(cursor.getInt(cursor.getColumnIndex("product_shipping_cost")));
            } while (cursor.moveToNext());
        }
        Intent intent = new Intent(getBaseContext(),CheckoutConfirmation.class);
        intent.putExtra("payment_firstname",firstname);
        intent.putExtra("payment_lastname",lastname);
        intent.putExtra("email",email);
        intent.putExtra("mobile",mobile);
        intent.putExtra("payment_address_1",addressline1);
        intent.putExtra("payment_address_2", addressline2);
        intent.putExtra("payment_city",city_id);
        intent.putExtra("payment_state", state_id);
        intent.putExtra("payment_postcode", pincode);
        intent.putExtra("payment_country", country_id);
        intent.putExtra("total",Integer.toString(total));
        intent.putExtra("shipping_cost",Integer.toString(shipping_cost));
        intent.putExtra("gross_total",Integer.toString(total +shipping_cost));
        intent.putExtra("shipping_address_same_as_billing_address",shipping_address_same_as_billing_address);
        startActivity(intent);
        cursor.close();
    }
    private void signUp() throws Exception{
        boolean isValid = true;
        if (firstname.isEmpty()) {
            firstinputlayout.setError("First Name is mandatory");
            isValid = false;
        } else {
            firstinputlayout.setErrorEnabled(false);
        }
        if (lastname.isEmpty()) {
            lastinputlayout.setError("Last Name is mandatory");
            isValid = false;
        } else {
            lastinputlayout.setErrorEnabled(false);
        }
        if (email.isEmpty()) {
            emailinputlayout.setError("email is mandatory");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailinputlayout.setError("email should be valid");
            isValid = false;
        } else {
            emailinputlayout.setErrorEnabled(false);
        }
        if (mobile.isEmpty()) {
            mobileinputlayout.setError("mobile should be valid");
            isValid = false;
        } else if (mobile.length() < 10) {
            mobileinputlayout.setError("mobile should be valid");
            isValid = false;
        } else {
            mobileinputlayout.setErrorEnabled(false);
        }
        if (pincode.isEmpty()) {
            pincodeinputlayout.setError("pincode is mandatory");
            isValid = false;
        }else if(pincodeerror.equalsIgnoreCase("0")){
            pincodeinputlayout.setError("Sorry! Service not available");
            isValid = false;
        }
        else {
            pincodeinputlayout.setErrorEnabled(false);
        }
        if (addressline1.isEmpty()) {
            address1inputlayout.setError("Locality is mandatory");
            isValid = false;
        } else {
            address1inputlayout.setErrorEnabled(false);
        }
        if (addressline2.isEmpty()) {
            address2inputlayout.setError("address is mandatory");
            isValid = false;
        } else {
            address2inputlayout.setErrorEnabled(false);
        }
        if (isValid) {
            if (spin_country.getSelectedItem().toString().trim().equals("Select_Country")) {
                Toast.makeText(this, "Please Select Country", Toast.LENGTH_SHORT).show();
                // ((TextView)editTextlocation.getSelectedView()).setError("Please Select Location");
            } else if (spin_State.getSelectedItem().toString().trim().equals("Select_State")) {
                Toast.makeText(this, "Please Select State", Toast.LENGTH_SHORT).show();
                // ((Spinner)spinner_subcategory.getSelectedView()).setError("Please Select Category");
            } else if (spin_City_District_Town.getSelectedItem().toString().trim().equals("Select_City")) {
                Toast.makeText(this, "Please Select City", Toast.LENGTH_SHORT).show();
            } else {
                li_billingaddressdetail.setVisibility(View.GONE);
                txv_billingaddressupicon.setVisibility(View.VISIBLE);
                txv_billingaddressdownicon.setVisibility(View.GONE);
                li_ordersummary.setVisibility(View.VISIBLE);
                li_ordersummarydetails.setVisibility(View.VISIBLE);
                li_chechbox.setVisibility(View.GONE);
                hiddenkeybord();
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
             dialog();
            }
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                p.dismiss();
                Log.d("Locatio", s);
                if (s.equalsIgnoreCase(" \"No Results Found\"")) {
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        ArrayList<Item> itemList = new ArrayList<>();
                        if (GlobalVariable.country.equalsIgnoreCase("")) {
                            itemList.add(new Item(0, "Select_Country", "000000"));
                        }
                        JSONObject obj;
                        String name;
                        for (int J = 0; J < jsonArray.length(); J++) {

                            obj = jsonArray.getJSONObject(J);
                            int id = obj.getInt("id");
                            name = obj.getString("name").trim();
                            phonecode = obj.getString("phonecode").trim();
                            itemList.add(new Item(id, name, phonecode));
                        }
                        ArrayAdapter<Item> spinnerAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_item, itemList);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spin_country.setAdapter(spinnerAdapter);
                        spin_country.setSelection(getIndex(spin_country, GlobalVariable.country));
                    } catch (Exception e) {}
                }
            }
        }
        class RetriveState extends AsyncTask<Void, Void, String> {
            private static final String TAG = "errormsg";
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
                dialog();
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
                        if (GlobalVariable.state.equalsIgnoreCase("")) {
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
                        ArrayAdapter<Item> spinnerAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_item, itemList);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spin_State.setAdapter(spinnerAdapter);
                        spin_State.setSelection(getIndex(spin_State, GlobalVariable.state));
                    } catch (Exception e) {e.getMessage();}
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
               dialog();
            }
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                p.dismiss();
                if (s.equalsIgnoreCase(" \"No Results Found\"")) {
                    spin_City_District_Town.setVisibility(View.GONE);
                    txv_city.setVisibility(View.GONE);
                } else {
                    spin_City_District_Town.setVisibility(View.VISIBLE);
                    txv_city.setVisibility(View.VISIBLE);
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        ArrayList<Item> itemList = new ArrayList<>();
                        if (GlobalVariable.city.equalsIgnoreCase("")) {
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
                        ArrayAdapter<Item> spinnerAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_item, itemList);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                        spin_City_District_Town.setAdapter(spinnerAdapter);
                        spin_City_District_Town.setSelection(getIndex(spin_City_District_Town, GlobalVariable.city));

                    } catch (Exception e) { e.getMessage();}
                 }
            }
        }
    class CkeckPincode extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("pincode", pincode);
            Log.d("Params:",params.toString());
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"CkeckPincode", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            if(s.contains("No Results Found")) {
                pincodeinputlayout.setError("Sorry! Service not available");
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
                       // String pin_id = jsonObject.getString("pin_id");
                       // String city_id = jsonObject.getString("city_id");
                       // String  area = jsonObject.getString("area");
                       // String retrivepin_code = jsonObject.getString("pin_code");
                       // String estimated_days = jsonObject.getString("estimated_days");
                    }
                } catch (Exception e) {e.getMessage();}
            }
        }
    }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                if (spinner.getId() == R.id.spin_country) {
                    Item ii = (Item) parent.getSelectedItem();
                    country_id = Integer.toString(ii.getid());
                    phonecode = ii.getphonecode();
                    if(country_id.equalsIgnoreCase("0"))
                    {
                        spin_City_District_Town.setVisibility(View.GONE);
                        spin_State.setVisibility(View.GONE);
                        txv_city.setVisibility(View.GONE);
                        txv_state.setVisibility(View.GONE);
                    }
                    else
                    {
                        spin_State.setVisibility(View.VISIBLE);
                        txv_state.setVisibility(View.VISIBLE);
                        new RetriveState().execute();
                    }
                } else if (spinner.getId() == R.id.spin_State) {
                    Item ii = (Item) parent.getSelectedItem();
                    state_id = Integer.toString(ii.getid());
                    if(state_id.equalsIgnoreCase("0"))
                    {
                        spin_City_District_Town.setVisibility(View.GONE);
                        txv_city.setVisibility(View.GONE);
                    }
                    else
                    {
                        spin_City_District_Town.setVisibility(View.VISIBLE);
                        new Retrivecity().execute();
                    }
                } else if (spinner.getId() == R.id.spin_City_District_Town) {
                    Item ii = (Item) parent.getSelectedItem();
                    city_id = Integer.toString(ii.getid());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
}

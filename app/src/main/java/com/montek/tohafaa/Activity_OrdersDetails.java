package com.montek.tohafaa;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.montek.tohafaa.Adapter.OrderListAdapter;
import com.montek.tohafaa.Adapter.TrackOrderProductAdapter;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.model.categoryitems;
import org.json.*;
import java.util.*;
import static com.montek.tohafaa.Activity_OrdersList.dialog;
import static com.montek.tohafaa.Activity_WebViewofInvoice.*;
import static com.montek.tohafaa.LoginActivity.p;
public class Activity_OrdersDetails extends AppCompatActivity {
    TextView txv_orderproduct, txv_ShippingCost, txv_GrossTotal, txv_GiftTo, txv_orderpartyname, txv_orderid, txv_OrderDate, txv_OrderTotal, txv_OrderStatus, txv_PaymentBy, txv_InvoiceNo;
     RecyclerView listView;
    String [] req_reason=null,product_gst=null,req_from=null,req_bank_detail_status=null,product_cod=null,order_product_status=null,status_by_admin=null,quantity=null,model=null,product_image=null,req_type=null,req_status=null,admin_req_status=null,shipping_pincode=null,shipping_country=null,shipping_state=null,shipping_city=null,order_product_address=null,shipping_email=null,shipping_contact=null,shipping_firstname=null,product_purchased_on=null,delivery_date=null,superimpose_text=null,superimpose_image=null,total=null,order_id=null,product_id=null,product_with_package=null,product_name=null,price=null,percent_discount=null,product_shipping_cost=null;
    public static String[] order_product_id=null;
    public  static ArrayList<categoryitems> item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       try{ setContentView(R.layout.activity_order_detail);
        actionBar = getSupportActionBar();
        Actionbar("Order Details");
        p=new ProgressDialog(this);
        UI();
            txv_orderid.setText(OrderListAdapter.orderid);
            txv_orderpartyname.setText(OrderListAdapter.name);
            txv_OrderDate.setText(OrderListAdapter.orderdate);
            txv_OrderTotal.setText(OrderListAdapter.totalofcart);
            txv_OrderStatus.setText(OrderListAdapter.orderstatus);
            txv_PaymentBy.setText(OrderListAdapter.paymentname);
            txv_InvoiceNo.setText(OrderListAdapter.invoice_no);
            txv_GiftTo.setText(OrderListAdapter.shippingname);
            txv_ShippingCost.setText(OrderListAdapter.shipping_cost);
            txv_GrossTotal.setText(OrderListAdapter.ordergross_total);
            new Retrive_Orders_Productdetail().execute();
        } catch (Exception e) {      e.getMessage();       }
    }
    void UI() throws Exception {
        listView = (RecyclerView) findViewById(R.id.listView);
        txv_orderid = (TextView) findViewById(R.id.txv_orderid);
        txv_orderpartyname = (TextView) findViewById(R.id.txv_name);
        txv_OrderDate = (TextView) findViewById(R.id.txv_OrderDate);
        txv_OrderTotal = (TextView) findViewById(R.id.txv_OrderTotal);
        txv_OrderStatus = (TextView) findViewById(R.id.txv_OrderStatus);
        txv_PaymentBy = (TextView) findViewById(R.id.txv_PaymentBy);
        txv_InvoiceNo = (TextView) findViewById(R.id.txv_InvoiceNo);
        txv_GiftTo = (TextView) findViewById(R.id.txv_GiftTo);
        txv_ShippingCost = (TextView) findViewById(R.id.txv_ShippingCost);
        txv_GrossTotal = (TextView) findViewById(R.id.txv_GrossTotal);
        txv_orderproduct = (TextView) findViewById(R.id.txv_orderproduct);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i1=new Intent(getBaseContext(),Activity_OrdersList.class);
                startActivity(i1);
                finish();
                return true;
            case R.id.action_pdf:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent i = new Intent(this, PDFViewActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Print pdf Not Supported in this device",Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed()
    {
        Intent i1=new Intent(getBaseContext(),Activity_OrdersList.class);
        startActivity(i1);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.menu_pdf, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class Retrive_Orders_Productdetail extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("order_id", OrderListAdapter.orderid);
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "retrive_orders_productdetail", params);
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
            Log.d("ss", s);
            if (s.contains("No Results Found")) {
                listView.setAdapter(null);
                listView.setVisibility(View.GONE);
            } else {
                listView.setVisibility(View.VISIBLE);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    order_product_id = new String[jsonArray.length()];
                    order_id = new String[jsonArray.length()];
                    product_id = new String[jsonArray.length()];
                    product_with_package= new String[jsonArray.length()];
                    product_name = new String[jsonArray.length()];
                    price = new String[jsonArray.length()];
                    percent_discount = new String[jsonArray.length()];
                    product_shipping_cost = new String[jsonArray.length()];
                    total = new String[jsonArray.length()];
                    superimpose_image = new String[jsonArray.length()];
                    superimpose_text = new String[jsonArray.length()];
                    delivery_date = new String[jsonArray.length()];
                    product_purchased_on = new String[jsonArray.length()];
                    shipping_firstname = new String[jsonArray.length()];
                    shipping_contact = new String[jsonArray.length()];
                    shipping_email = new String[jsonArray.length()];
                    order_product_address = new String[jsonArray.length()];
                    shipping_city = new String[jsonArray.length()];
                    shipping_state = new String[jsonArray.length()];
                    shipping_country = new String[jsonArray.length()];
                    shipping_pincode = new String[jsonArray.length()];
                    product_cod = new String[jsonArray.length()];
                    order_product_status = new String[jsonArray.length()];
                    status_by_admin = new String[jsonArray.length()];
                    superimpose_image = new String[jsonArray.length()];
                    req_type = new String[jsonArray.length()];
                    req_status = new String[jsonArray.length()];
                    admin_req_status = new String[jsonArray.length()];
                    req_from = new String[jsonArray.length()];
                    req_bank_detail_status = new String[jsonArray.length()];
                    req_reason = new String[jsonArray.length()];
                    product_image = new String[jsonArray.length()];
                    model = new String[jsonArray.length()];
                    quantity = new String[jsonArray.length()];
                    product_gst = new String[jsonArray.length()];
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                         order_product_id[i] = jsonObject.getString("order_product_id");
                         order_id[i] = jsonObject.getString("order_id");
                         product_id[i] = jsonObject.getString("product_id");
                         product_with_package[i] = jsonObject.getString("product_with_package");
                         product_name[i] = jsonObject.getString("name");
                         product_image[i] = jsonObject.getString("product_image");
                         model[i] = jsonObject.getString("model");
                         quantity[i] = jsonObject.getString("quantity");
                         price[i] = jsonObject.getString("price");
                         percent_discount[i] = jsonObject.getString("percent_discount");
                         product_shipping_cost[i] = jsonObject.getString("shipping_cost");
                         total[i] = jsonObject.getString("total");
                         superimpose_image[i] = jsonObject.getString("superimpose_image");
                         superimpose_text[i] = jsonObject.getString("superimpose_text");
                         delivery_date [i]= jsonObject.getString("delivery_date");
                         product_purchased_on [i]= jsonObject.getString("product_purchased_on");
                         shipping_firstname[i] = jsonObject.getString("shipping_firstname") + " " + jsonObject.getString("shipping_lastname");
                         shipping_contact[i] = jsonObject.getString("shipping_contact");
                         shipping_email[i] = jsonObject.getString("shipping_email");
                         order_product_address[i] = jsonObject.getString("order_product_address");
                         shipping_city[i] = jsonObject.getString("shipping_city");
                         shipping_state[i] = jsonObject.getString("shipping_state");
                         shipping_pincode[i] = jsonObject.getString("shipping_pincode");
                         shipping_country[i] = jsonObject.getString("shipping_country");
                         product_cod[i] = jsonObject.getString("product_cod");
                         order_product_status[i] = jsonObject.getString("order_product_status");
                         status_by_admin[i] = jsonObject.getString("status_by_admin");
                         req_type[i] = jsonObject.getString("req_type");
                         req_status[i] = jsonObject.getString("req_status");
                         admin_req_status[i] = jsonObject.getString("admin_req_status");
                         req_from[i] = jsonObject.getString("req_from");
                         req_bank_detail_status[i] = jsonObject.getString("req_bank_detail_status");
                         req_reason[i] = jsonObject.getString("req_reason");
                        product_gst[i] = jsonObject.getString("product_gst");
                        initViews();
                    }
                   txv_orderproduct.setText("Order Products(" + order_product_id.length + ")");
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }
    private void initViews()//LinerLayout using recyclerview
    {
        listView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(layoutManager);
        ArrayList<categoryitems> item = prepareData();
        TrackOrderProductAdapter adapter = new TrackOrderProductAdapter( getBaseContext(),item);
        listView.setAdapter(adapter);
    }
    private ArrayList<categoryitems> prepareData() {
       item = new ArrayList<>();
        for (int i = 0; i < order_product_id.length; i++) {
            categoryitems items = new categoryitems();
            items.setshippingname(shipping_firstname[i]);
            items.setorderdate(product_purchased_on[i]);
            items.setproduct_size(delivery_date[i]);
            items.setproduct_color(superimpose_text[i]);
            items.setproduct_superimpose_image(superimpose_image[i]);
            items.setordertotal(total[i]);
            items.setproduct_id(order_product_id[i]);
            items.setproduct_price(price[i]);
            items.setorderid(order_id[i]);
            items.setproduct_id(product_id[i]);
            items.setproduct_with_package(product_with_package[i]);
            items.setproduct_name(product_name[i]);
            items.setdiscount(percent_discount[i]);
            items.setproduct_shipping_cost(product_shipping_cost[i]);
            items.setreq_reason(req_reason[i]);
            items.setreq_from(req_from[i]);
            items.setreq_bank_detail_status(req_bank_detail_status[i]);
            items.setproduct_cod(product_cod[i]);
            items.setorderproductstatus(order_product_status[i]);
            items.setstatus_by_admin(status_by_admin[i]);
            items.setproduct_quantity(quantity[i]);
            items.setproduct_model(model[i]);
            items.setproduct_image(product_image[i]);
            items.setproduct_superimpose_image(superimpose_image[i]);
            items.setproduct_tax(req_type[i]);
            items.setproduct_brand_id(req_status[i]);
            items.setproduct_offer(admin_req_status[i]);
            items.setpincode(shipping_pincode[i]);
            items.setproduct_category_id(shipping_country[i]);
            items.setproduct_description(shipping_state[i]);
            items.setproduct_spc_id(shipping_city[i]);
            items.setname(order_product_address[i]);
            items.setimage_url(shipping_email[i]);
            items.setproduct_sub2category_id(shipping_contact[i]);
            items.setproduct_gst(product_gst[i]);
            item.add(items);
        }
        return item;
    }
}

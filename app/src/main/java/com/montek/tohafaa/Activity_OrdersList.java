package com.montek.tohafaa;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.montek.tohafaa.Adapter.OrderListAdapter;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.*;
import com.montek.tohafaa.model.categoryitems;
import org.json.*;
import java.util.*;
import static com.montek.tohafaa.Activity_WebViewofInvoice.actionBar;
import static com.montek.tohafaa.LoginActivity.p;
public class Activity_OrdersList extends AppCompatActivity implements View.OnClickListener{
    ListView listView;
    ArrayList<categoryitems> List;
    OrderListAdapter adapter;
    CardView cardview;
    EditText editTextSearch;
    Button btn_continue,backbutton;
    TextView txv_listtext,txv_emptylist;
    LinearLayout layoutbuttons;
    public static String flagforpdfororderlist="";
    Button bt_clear,btn_search;
    FrameLayout li_edtxsearch;
    LinearLayout li_search;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishactivity);
        p=new ProgressDialog(this);
        UI();
        actionBar = getSupportActionBar();
        actionBar.hide();
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryitems ListViewClickData = (categoryitems) parent.getItemAtPosition(position);
//                freelancer_id = ListViewClickData.getfreelancer_id();
            }
        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence stringVar, int start, int before, int count) {
                if (adapter == null) {
                    Toast.makeText(getBaseContext(), "No Item Found", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.getFilter().filter(stringVar.toString());
                }
            }
        });
        new Retrive_OrderList().execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
    void UI() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setVisibility(View.VISIBLE);
        cardview=(CardView)findViewById(R.id.cardview);
        cardview.setVisibility(View.GONE);
        btn_continue=(Button)findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
        backbutton=(Button)findViewById(R.id.backbutton);
        backbutton.setOnClickListener(this);
        txv_emptylist=(TextView)findViewById(R.id.txv_emptylist);
        txv_emptylist.setText("Not Orderd Any Item");
        txv_listtext=(TextView)findViewById(R.id.txv_listtext);
        layoutbuttons=(LinearLayout)findViewById(R.id.layoutbuttons);
        layoutbuttons.setVisibility(View.GONE);
        LinearLayout ll_editext=(LinearLayout)findViewById(R.id.ll_editext);
        ll_editext.setVisibility(View.VISIBLE);
        li_search = (LinearLayout)findViewById(R.id.li_search);
        li_edtxsearch = (FrameLayout)findViewById(R.id.li_edtxsearch);
        li_search.setVisibility(View.VISIBLE);
        li_edtxsearch.setVisibility(View.GONE);
        bt_clear = (Button)findViewById(R.id.btn_clear);
        bt_clear.setOnClickListener(this);
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        editTextSearch = (EditText)findViewById(R.id.edtx_search);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backbutton:
                 this.finish();
                break;
            case R.id.btn_continue:
                Intent i=new Intent(getBaseContext(),DashBordActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btn_clear:
                editTextSearch.setText("");
                break;
            case R.id.btn_search:
                li_edtxsearch.setVisibility(View.VISIBLE);
                li_search.setVisibility(View.GONE);
                break;
        }
    }
    public static void dialog()
    {  try {
            p.setMessage("Loading.....");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
            p.show();
        }catch (Exception e){}
    }
    class Retrive_OrderList extends AsyncTask<Void, Void, String> {
         @Override
         protected String doInBackground(Void... voids) {
             RequestHandler requestHandler = new RequestHandler();
             HashMap<String, String> params = new HashMap<>();
             params.put("customer_id", GlobalVariable.user_id);
             return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "retrive_orders_detail", params);
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
             if (s.contains("No Results Found")) {
                 listView.setAdapter(null);
                 cardview.setVisibility(View.VISIBLE);
             } else {
                 JSONArray jsonArray = null;
                 try {
                     cardview.setVisibility(View.GONE);
                     jsonArray = new JSONArray(s);
                     JSONObject jsonObject;
                     categoryitems item;
                      List = new ArrayList<categoryitems>();
                     for (int i = 0; i < jsonArray.length(); i++) {
                         jsonObject = jsonArray.getJSONObject(i);
                         String order_id = jsonObject.getString("order_id").trim();
                         String txnid = jsonObject.getString("txnid").trim();
                         String date_added = jsonObject.getString("date_added").trim();
                         String gross_total = jsonObject.getString("gross_total").trim();
                         String order_status = jsonObject.getString("order_status").trim();
                         String totalofcart = jsonObject.getString("total").trim();
                         String shipping_cost = jsonObject.getString("shipping_cost").trim();
                         String name = jsonObject.getString("firstname").trim()+" "+jsonObject.getString("lastname").trim();
                         String paymentname = jsonObject.getString("payment_firstname").trim()+" "+jsonObject.getString("payment_lastname").trim();
                         String shippingname = jsonObject.getString("shipping_firstname").trim()+" "+jsonObject.getString("shipping_firstname").trim();
                         String invoice_no = jsonObject.getString("invoice_no").trim();
                         String shipping_contact = jsonObject.getString("shipping_contact").trim();
                         String shipping_email = jsonObject.getString("shipping_email").trim();
                         String shipping_address_1=jsonObject.getString("shipping_address_1").trim();
                         String shipping_address_2=jsonObject.getString("shipping_address_2").trim();
                         String shipping_city = jsonObject.getString("shipping_city").trim();
                         String shipping_state=jsonObject.getString("shipping_state").trim();
                         String shipping_pincode = jsonObject.getString("shipping_postcode").trim();
                         String shipping_country=jsonObject.getString("shipping_country").trim();
                         String delivery_date=jsonObject.getString("delivery_date").trim();
                         String shipping_address_same_as_billing_address=jsonObject.getString("shipping_address_same_as_billing_address").trim();
                         item = new categoryitems(order_id,txnid, date_added, gross_total,order_status,totalofcart,shipping_cost,name,paymentname,shippingname,invoice_no,shipping_contact,shipping_email,shipping_address_1,shipping_address_2,shipping_city,shipping_state,shipping_pincode,shipping_country,shipping_address_same_as_billing_address,delivery_date);
                         List.add(item);
                     }
                     flagforpdfororderlist="OrderList";
                     adapter = new OrderListAdapter(getBaseContext(), R.layout.activity_trackorderlist, List);
                     listView.setAdapter(adapter);
                 } catch (Exception e) {e.getMessage();}
             }
         }
     }
}

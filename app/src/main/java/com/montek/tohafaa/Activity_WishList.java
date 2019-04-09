package com.montek.tohafaa;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.montek.tohafaa.Adapter.WishListAdapter;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.GlobalVariable;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.model.categoryitems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.montek.tohafaa.Activity_WebViewofInvoice.*;
import static com.montek.tohafaa.Adapter.WishListAdapter.item1;
import static com.montek.tohafaa.DashBordActivity.*;
import static com.montek.tohafaa.LoginActivity.p;

public class Activity_WishList  extends AppCompatActivity implements View.OnClickListener {
    public static RecyclerView recyclerView;
    ListView listView;
    Button btn_continue;
    public static CardView cardview;
    TextView txv_emptylist,txv_listtext;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{setContentView(R.layout.activity_wishactivity);
            p = new ProgressDialog(this);
        UI();
           // item1.clear();
        actionBar = getSupportActionBar();
        Actionbar("Your Wishlist");
        actionBar.setDisplayHomeAsUpEnabled(true);
          new  Retrive_mywishlist().execute();
        }catch (Exception e){}
    }
    void UI() throws Exception
    {
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.listView);
        listView.setVisibility(View.GONE);
        LinearLayout layoutbuttons = (LinearLayout) findViewById(R.id.layoutbuttons);
        layoutbuttons.setVisibility(View.GONE);
        cardview = (CardView)findViewById(R.id.cardview);
        cardview.setVisibility(View.GONE);
        txv_listtext = (TextView)findViewById(R.id.txv_listtext);
        txv_listtext.setText("My Wishlist");
        txv_emptylist = (TextView)findViewById(R.id.txv_emptylist);
        txv_emptylist.setText("Your Wishlist is Empty");
        btn_continue = (Button)findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
    }
    private void initViews() throws Exception{
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<categoryitems> item = prepareData();
        WishListAdapter adapter = new WishListAdapter(this,item);
        recyclerView.setAdapter(adapter);
    }
    private ArrayList<categoryitems> prepareData() throws Exception {
        ArrayList<categoryitems> item = new ArrayList<>();
        item.clear();
        if(sv_id.length!=0) {
            cardview.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            for (int i = 0; i < sv_id.length; i++) {
                categoryitems items = new categoryitems();
                items.setproduct_id(svproduct_id[i]);
                items.setid(sv_id[i]);
                items.setproduct_name(sv_name[i]);
                items.setproduct_price(sv_price[i]);
                items.setproduct_image(sv_src[i]);
                items.setproduct_shipping_cost(sv_productsheepingcost[i]);
                items.setproduct_quantity(sv_maxQantity[i]);
                item.add(items);
            }
        }
        else
        {   recyclerView.setAdapter(null);
            cardview.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        return item;
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
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), DashBordActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        this.finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
           case R.id.btn_continue:
                Intent i1=new Intent(getBaseContext(),DashBordActivity.class);
                startActivity(i1);
                break;
        }
    }

      class Retrive_mywishlist extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("sv_user_id", GlobalVariable.user_id);
            params.put("sv_key","my_wishlist");
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"Retrive_whishlist", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{ Activity_OrdersList.dialog();}catch (Exception e){}
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            if(s.contains("No Results Found")) {
                sv_id=new String[0];
                svproduct_id=new String[0];
                recyclerView.setAdapter(null);
                cardview.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
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

                    initViews();
                } catch (Exception e) { e.getMessage();}
            }
        }
    }
}
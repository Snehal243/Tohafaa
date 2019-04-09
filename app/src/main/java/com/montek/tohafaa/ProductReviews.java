package com.montek.tohafaa;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.montek.tohafaa.Adapter.ProductReviewListAdapter;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.GlobalVariable;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.model.categoryitems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.montek.tohafaa.Activity_WebViewofInvoice.Actionbar;
import static com.montek.tohafaa.Activity_WebViewofInvoice.actionBar;
import static com.montek.tohafaa.CorporateClient.corpoarateclient;
import static com.montek.tohafaa.ProductDetails.ProductReviewslist;
public class ProductReviews extends AppCompatActivity {
    ListView listView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishactivity);
        try {
            actionBar = getSupportActionBar();
            Actionbar("Product Reviews");
            CardView cardView = (CardView) findViewById(R.id.cardview);
            cardView.setVisibility(View.GONE);
            LinearLayout li_button = (LinearLayout) findViewById(R.id.layoutbuttons);
            li_button.setVisibility(View.GONE);
            LinearLayout li_cartbuttons = (LinearLayout) findViewById(R.id.carttatallayout);
            li_cartbuttons.setVisibility(View.GONE);
             listView = (ListView) findViewById(R.id.listView);
            listView.setVisibility(View.VISIBLE);
            if (corpoarateclient == 0) {
                ProductReviewListAdapter adapter = new ProductReviewListAdapter(getBaseContext(), R.layout.activity_productreviewelement, ProductReviewslist);
                listView.setAdapter(adapter);
                listView.setItemsCanFocus(false);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            } else if (corpoarateclient == 2) {
                 new Retrivecustomerreview().execute();
                }
            }catch(Exception e){}
    }


    class Retrivecustomerreview extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category+"Retrivecustomerreview", params);
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
            } else {
                try {
                    JSONArray jsonArray = null;
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    categoryitems Sort;
                    ProductReviewslist.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String first_name = jsonObject.getString("first_name");
                        String last_name = jsonObject.getString("last_name");
                        String picture = jsonObject.getString("picture");
                        String review = jsonObject.getString("review");
                        Sort = new categoryitems(first_name,last_name, picture,review);
                        ProductReviewslist.add(Sort);
                    }

                    ProductReviewListAdapter adapter = new ProductReviewListAdapter(getBaseContext(), R.layout.activity_productreviewelement, ProductReviewslist);
                    listView.setAdapter(adapter);
                    listView.setItemsCanFocus(false);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                }catch (Exception e){}
            }
        }
    }
}
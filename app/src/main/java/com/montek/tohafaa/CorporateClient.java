package com.montek.tohafaa;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.montek.tohafaa.Adapter.OrderListAdapter;
import com.montek.tohafaa.Adapter.ProductReviewListAdapter;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.GlobalVariable;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.model.categoryitems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import static com.montek.tohafaa.Activity_OrdersList.dialog;
import static com.montek.tohafaa.Activity_WebViewofInvoice.Actionbar;
import static com.montek.tohafaa.Activity_WebViewofInvoice.actionBar;
import static com.montek.tohafaa.LoginActivity.p;
public class CorporateClient extends AppCompatActivity {
    public static int corpoarateclient=0;
    GridView GridView;
    private ArrayList<categoryitems> List;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishactivity);
        try {
            actionBar = getSupportActionBar();
            Actionbar("Corporate Clients");
            CardView cardView = (CardView) findViewById(R.id.cardview);
            cardView.setVisibility(View.GONE);
            LinearLayout li_button = (LinearLayout) findViewById(R.id.layoutbuttons);
            li_button.setVisibility(View.GONE);
            LinearLayout li_cartbuttons = (LinearLayout) findViewById(R.id.carttatallayout);
            li_cartbuttons.setVisibility(View.GONE);
            ListView  listView = (ListView) findViewById(R.id.listView);
            GridView=(GridView)findViewById(R.id.GridView);
            GridView.setVisibility(View.VISIBLE);
            new RetriveClient().execute();

        } catch (Exception e) {

    }
}
    class RetriveClient extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "RetriveClient", params);
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
                GridView.setAdapter(null);
            } else {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    categoryitems item;
                    List = new ArrayList<categoryitems>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String client_id = jsonObject.getString("client_id").trim();
                        String client_name = jsonObject.getString("client_name").trim();
                        String client_image = jsonObject.getString("client_image").trim();
                        String client_address = jsonObject.getString("client_address").trim();

                        item = new categoryitems(client_id,client_name, client_image,client_address);
                        List.add(item);
                    }
                    corpoarateclient=1;
                    ProductReviewListAdapter adapter = new ProductReviewListAdapter(getBaseContext(), R.layout.activity_productreviewelement, List);
                    GridView.setAdapter(adapter);
                    GridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                } catch (Exception e) {e.getMessage();}
            }
        }
    }
}

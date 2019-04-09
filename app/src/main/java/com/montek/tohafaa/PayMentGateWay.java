package com.montek.tohafaa;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.graphics.Bitmap;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.webkit.JavascriptInterface;
import android.webkit.*;
import android.widget.Toast;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.GlobalVariable;
import com.montek.tohafaa.extra.URLs;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import static android.webkit.WebSettings.LOAD_DEFAULT;
import static com.montek.tohafaa.Activity_CartList.*;
import static com.montek.tohafaa.CheckoutConfirmation.*;
import static com.montek.tohafaa.LoginActivity.p;
import static java.lang.Thread.sleep;
public class PayMentGateWay extends AppCompatActivity {
    private String Order_id,txnId;
    WebView webView ;
    final Activity activity = this;
    private String tag = "PayMentGateWay";
    private String hash;
    ProgressDialog progressDialog ;
    String shipping_firstname,shipping_lastname, shipping_contact,shipping_email,order_product_address,shipping_city,shipping_state,shipping_pincode,shipping_country,product_color,product_size,product_delivery_date,product_superimpose_text,product_superimpose_image,product_id,product_with_package,product_name,product_shipping_cost,product_subtotal,product_percent_discount,product_qty,product_price;
    String merchant_key="rcjQUPktU"; // test
    String salt="ec5iIg1jwi8"; // test
    String action1 ="";
    String base_url="https://test.payu.in";
    int error=0;
    String hashString="";
    Map<String,String> params;
    String txnid ="";
    String SUCCESS_URL = "https://www.payumoney.com/mobileapp/payumoney/success.php" ; // failed
    String FAILED_URL = "https://www.payumoney.com/mobileapp/payumoney/failure.php" ;
    Handler mHandler = new Handler();
    @SuppressLint("JavascriptInterface") @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        p = new ProgressDialog(this);
        progressDialog=new ProgressDialog(activity);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webView = new WebView(this);
        setContentView(webView);
        params= new HashMap<>();
        params.put("key", merchant_key);
        params.put("amount", gross_total);
        params.put("firstname", payment_firstname);
        params.put("email", payment_email);
        params.put("phone", payment_mobile);
        params.put("productinfo", "Recharge Wallet");
        params.put("surl", SUCCESS_URL);
        params.put("furl", FAILED_URL);
        params.put("service_provider", "payu_paisa");
        params.put("lastname", payment_lastname);
        params.put("address1", payment_address_1);
        params.put("address2", payment_address_2);
        params.put("city", payment_city);
        params.put("state", payment_state);
        params.put("country", payment_country);
        params.put("zipcode", payment_postcode);
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");
        params.put("pg", "");
        if(empty(params.get("txnid"))){
            Random rand = new Random();
            String rndm = Integer.toString(rand.nextInt())+(System.currentTimeMillis() / 1000L);
            txnid=hashCal("SHA-256",rndm).substring(0,20);
            params.put("txnid", txnid);
        }
        else
            txnid=params.get("txnid");
        hash="";
        String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
        if(empty(params.get("hash")) && params.size()>0)
        {
            if( empty(params.get("key"))
                    || empty(params.get("txnid"))
                    || empty(params.get("amount"))
                    || empty(params.get("firstname"))
                    || empty(params.get("email"))
                    || empty(params.get("phone"))
                    || empty(params.get("productinfo"))
                    || empty(params.get("surl"))
                    || empty(params.get("furl"))
                    || empty(params.get("service_provider"))

                    ){
                error=1;
            }
            else{
                String[] hashVarSeq=hashSequence.split("\\|");
                for(String part : hashVarSeq)
                {
                    hashString= (empty(params.get(part)))?hashString.concat(""):hashString.concat(params.get(part));
                    hashString=hashString.concat("|");
                }
                hashString=hashString.concat(salt);
                hash=hashCal("SHA-512",hashString);
                action1=base_url.concat("/_payment");
            }
        }
        else if(!empty(params.get("hash")))
        {
            hash=params.get("hash");
            action1=base_url.concat("/_payment");
        }
        webView.setWebViewClient(new MyWebViewClient(){

            public void onPageFinished(WebView view, final String url) {
                progressDialog.dismiss();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //make sure dialog is showing
                if(! progressDialog.isShowing()){
                    progressDialog.show();
                }
            }
		/*@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "SslError! " +  error, Toast.LENGTH_SHORT).show();
				 handler.proceed();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "Page Started! " + url, Toast.LENGTH_SHORT).show();
				if(url.startsWith(SUCCESS_URL)){
					Toast

					.makeText(activity, "Payment Successful! " + url, Toast.LENGTH_SHORT).show();
					 Intent intent = new Intent(PayMentGateWay.this, MainActivity.class);
					    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
					    startActivity(intent);
					    finish();
					    return false;
				}else if(url.startsWith(FAILED_URL)){
					Toast.makeText(activity, "Payment Failed! " + url, Toast.LENGTH_SHORT).show();
				    return false;
				}else if(url.startsWith("http")){
					return true;
				}
				//return super.shouldOverrideUrlLoading(view, url);
				return false;
			}*/


        });
        //webView.setVisibility(0);
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(LOAD_DEFAULT);//2
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        //webView.addJavascriptInterface(new PayUJavaScriptInterface(getApplicationContext()), "PayUMoney");
        webView.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("key",merchant_key);
        mapParams.put("hash",PayMentGateWay.this.hash);
        mapParams.put("txnid",(empty(PayMentGateWay.this.params.get("txnid"))) ? "" : PayMentGateWay.this.params.get("txnid"));
        mapParams.put("service_provider","payu_paisa");
        mapParams.put("amount",(empty(PayMentGateWay.this.params.get("amount"))) ? "" : PayMentGateWay.this.params.get("amount"));
        mapParams.put("firstname",(empty(PayMentGateWay.this.params.get("firstname"))) ? "" : PayMentGateWay.this.params.get("firstname"));
        mapParams.put("email",(empty(PayMentGateWay.this.params.get("email"))) ? "" : PayMentGateWay.this.params.get("email"));
        mapParams.put("phone",(empty(PayMentGateWay.this.params.get("phone"))) ? "" : PayMentGateWay.this.params.get("phone"));
        mapParams.put("productinfo",(empty(PayMentGateWay.this.params.get("productinfo"))) ? "" : PayMentGateWay.this.params.get("productinfo"));
        mapParams.put("surl",(empty(PayMentGateWay.this.params.get("surl"))) ? "" : PayMentGateWay.this.params.get("surl"));
        mapParams.put("furl",(empty(PayMentGateWay.this.params.get("furl"))) ? "" : PayMentGateWay.this.params.get("furl"));
        mapParams.put("lastname",(empty(PayMentGateWay.this.params.get("lastname"))) ? "" : PayMentGateWay.this.params.get("lastname"));
        mapParams.put("address1",(empty(PayMentGateWay.this.params.get("address1"))) ? "" : PayMentGateWay.this.params.get("address1"));
        mapParams.put("address2",(empty(PayMentGateWay.this.params.get("address2"))) ? "" : PayMentGateWay.this.params.get("address2"));
        mapParams.put("city",(empty(PayMentGateWay.this.params.get("city"))) ? "" : PayMentGateWay.this.params.get("city"));
        mapParams.put("state",(empty(PayMentGateWay.this.params.get("state"))) ? "" : PayMentGateWay.this.params.get("state"));
        mapParams.put("country",(empty(PayMentGateWay.this.params.get("country"))) ? "" : PayMentGateWay.this.params.get("country"));
        mapParams.put("zipcode",(empty(PayMentGateWay.this.params.get("zipcode"))) ? "" : PayMentGateWay.this.params.get("zipcode"));
        mapParams.put("udf1",(empty(PayMentGateWay.this.params.get("udf1"))) ? "" : PayMentGateWay.this.params.get("udf1"));
        mapParams.put("udf2",(empty(PayMentGateWay.this.params.get("udf2"))) ? "" : PayMentGateWay.this.params.get("udf2"));
        mapParams.put("udf3",(empty(PayMentGateWay.this.params.get("udf3"))) ? "" : PayMentGateWay.this.params.get("udf3"));
        mapParams.put("udf4",(empty(PayMentGateWay.this.params.get("udf4"))) ? "" : PayMentGateWay.this.params.get("udf4"));
        mapParams.put("udf5",(empty(PayMentGateWay.this.params.get("udf5"))) ? "" : PayMentGateWay.this.params.get("udf5"));
        mapParams.put("pg",(empty(PayMentGateWay.this.params.get("pg"))) ? "" : PayMentGateWay.this.params.get("pg"));
        webview_ClientPost(webView, action1, mapParams.entrySet());
    }
	/*public class PayUJavaScriptInterface {

		@JavascriptInterface
        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;

                    new PostRechargeData().execute();

            		Toast.makeText(getApplicationContext(),"Successfully payment\n redirect from PayUJavaScriptInterface" ,Toast.LENGTH_LONG).show();

                }
            });
        }
	}*/
    private final class PayUJavaScriptInterface {
        PayUJavaScriptInterface() {
        }
        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void success(long id, final String paymentId1) {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;
                  //  paymentId=paymentId1;
                    txnId = params.get("txnid");
                   new webservice_order().execute();
                }
            });
        }
        @JavascriptInterface
        public void failure(final String id, String error) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Cancel payment" ,Toast.LENGTH_LONG).show();                    //cancelPayment();
                }
            });
        }
        @JavascriptInterface
        public void failure() {
            failure("");
        }
        @JavascriptInterface
        public void failure(final String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Failed payment" ,Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void webview_ClientPost(WebView webView, String url, Collection< Map.Entry<String, String>> postData){
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }
    public void success(long id, final String paymentId) {
        mHandler.post(new Runnable() {
            public void run() {
                mHandler = null;
                Toast.makeText(getApplicationContext(),"Successfully payment\n redirect from Success Function" ,Toast.LENGTH_LONG).show();
            }
        });
    }
    public boolean empty(String s)
    {
        return s == null || s.trim().equals("");
    }
    public String hashCal(String type,String str){
        byte[] hashseq=str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try{
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (int i=0;i<messageDigest.length;i++) {
                String hex=Integer.toHexString(0xFF & messageDigest[i]);
                if(hex.length()==1) hexString.append("0");
                hexString.append(hex);
            }
        }catch(Exception e){ }
        return hexString.toString();
    }
     //override the override loading method for the webview client
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	/*if(url.contains("response.php") || url.equalsIgnoreCase(SUCCESS_URL)){
        		new PostRechargeData().execute();
        		Toast.makeText(getApplicationContext(),"Successfully payment\n redirect from webview" ,Toast.LENGTH_LONG).show();
               return false;
        	}else  */if(url.startsWith("http")){
                progressDialog.show();
                view.loadUrl(url);
                System.out.println("myresult "+url);
            } else {
                return false;
            }
            return true;
        }
    }
    /******************************************* closed send record to back end ************************************/
    class webservice_order extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("customer_id", GlobalVariable.user_id);
            params.put("txnid",txnId);
            params.put("firstname", GlobalVariable.first_name);
            params.put("lastname", GlobalVariable.last_name);
            params.put("email", GlobalVariable.email);
            params.put("telephone",GlobalVariable.mobile);
            params.put("payment_firstname", payment_firstname);
            params.put("payment_lastname", payment_lastname);
            params.put("payment_address_1",payment_address_1);
            params.put("payment_address_2",payment_address_2);
            params.put("payment_city", payment_city);
            params.put("payment_state", payment_state);
            params.put("payment_postcode", payment_postcode);
            params.put("payment_country", payment_country);
            params.put("shipping_firstname",payment_firstname);
            params.put("shipping_lastname", payment_lastname);
            params.put("shipping_address_1", payment_address_1);
            params.put("shipping_address_2",payment_address_2);
            params.put("shipping_contact",payment_mobile);
            params.put("shipping_email", payment_email);
            params.put("shipping_city", payment_city);
            params.put("shipping_state",payment_state);
            params.put("shipping_postcode", payment_postcode);
            params.put("shipping_country",payment_country);
            params.put("total", payment_total);
            params.put("shipping_cost", shipping_cost);
            params.put("gross_total",gross_total);
            params.put("shipping_address_same_as_billing_address",shipping_address_same_as_billing_address);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"orders", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("sss",s);
            p.dismiss();
            try {
                if(shipping_address_same_as_billing_address.equalsIgnoreCase("1")) {
                    TakevalueCarttableFromDatabase(s);
                }
                else {
                    TakevalueCheckouttableFromDatabase(s);
                }
            } catch (Exception e) {
                e.getMessage();
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void TakevalueCarttableFromDatabase(String order_id) throws Exception {
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        //ArrayList<CartModel> finalList = new ArrayList<>();
        cursor = mDatabase.rawQuery("SELECT * FROM CartTable", null);
        if (cursor.moveToFirst()) {
            do {
                product_id = cursor.getString(cursor.getColumnIndex("product_id"));
                product_with_package = cursor.getString(cursor.getColumnIndex("product_with_package"));
                product_name = cursor.getString(cursor.getColumnIndex("name"));
                product_qty = cursor.getString(cursor.getColumnIndex("qty"));
                product_price = cursor.getString(cursor.getColumnIndex("price"));
                product_percent_discount = cursor.getString(cursor.getColumnIndex("discount"));
                product_shipping_cost = cursor.getString(cursor.getColumnIndex("product_shipping_cost"));
                product_subtotal = cursor.getString(cursor.getColumnIndex("subtotal"));
                product_superimpose_image = cursor.getString(cursor.getColumnIndex("superimpose_image"));
                product_superimpose_text = cursor.getString(cursor.getColumnIndex("superimpose_text"));
                int estimated_days = cursor.getInt(cursor.getColumnIndex("estimated_days"));
                final Calendar calendar = Calendar.getInstance(); //to find date day from estimated days
                calendar.add(Calendar.DATE,estimated_days);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //Date and time
                String Date = sdf.format(calendar.getTime());
                Order_id=order_id;
                product_delivery_date= Date;
                shipping_firstname=payment_firstname;
                shipping_lastname=payment_lastname;
                shipping_contact=payment_mobile;
                shipping_email=payment_email;
                order_product_address =payment_address_1;
                shipping_city=payment_city;
                shipping_state=payment_state;
                shipping_pincode=payment_postcode;
                shipping_country=payment_country;
                product_size = cursor.getString(cursor.getColumnIndex("order_size"));
                product_color = cursor.getString(cursor.getColumnIndex("order_color"));
                new webservice_order_product().execute();
                new webservice_order_option().execute();
                try {
                    sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();//closing the cursor
        Toast.makeText(getApplicationContext(), "Successfully Received payment", Toast.LENGTH_LONG).show();
        new Activity_CartList.Delete_Cartlistproduct().execute();
        try {
            deleteallrecordsofCheckoutTable();
            deleteallrecordsofcartTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
        addNotification("Payment Successfully");
        DisplayOrderStatus();
    }
    private void TakevalueCheckouttableFromDatabase(String order_id) throws Exception{
        cursor = mDatabase.rawQuery("SELECT * FROM CheckoutTable", null);
        if (cursor.moveToFirst()) {
            do {
                Order_id=order_id;
                product_id = cursor.getString(cursor.getColumnIndex("product_id"));
                product_with_package = cursor.getString(cursor.getColumnIndex("product_with_package"));
                product_name = cursor.getString(cursor.getColumnIndex("name"));
                product_qty = cursor.getString(cursor.getColumnIndex("qty"));
                product_price = cursor.getString(cursor.getColumnIndex("price"));
                product_percent_discount = cursor.getString(cursor.getColumnIndex("discount"));
                product_shipping_cost = cursor.getString(cursor.getColumnIndex("product_shipping_cost"));
                product_subtotal = cursor.getString(cursor.getColumnIndex("total"));
                product_superimpose_image = cursor.getString(cursor.getColumnIndex("superimpose_image"));
                product_superimpose_text = cursor.getString(cursor.getColumnIndex("superimpose_text"));
                product_delivery_date = cursor.getString(cursor.getColumnIndex("delivery_date"));
                shipping_firstname=cursor.getString(cursor.getColumnIndex("shipping_firstname"));
                shipping_lastname=cursor.getString(cursor.getColumnIndex("shipping_lastname"));
                shipping_contact=cursor.getString(cursor.getColumnIndex("shipping_contact"));
                shipping_email=cursor.getString(cursor.getColumnIndex("shipping_email"));
                order_product_address =cursor.getString(cursor.getColumnIndex("order_product_address"));
                shipping_city=cursor.getString(cursor.getColumnIndex("shipping_city"));
                shipping_state=cursor.getString(cursor.getColumnIndex("shipping_state"));
                shipping_pincode=cursor.getString(cursor.getColumnIndex("shipping_pincode"));
                shipping_country=cursor.getString(cursor.getColumnIndex("shipping_country"));
                product_size = cursor.getString(cursor.getColumnIndex("order_size"));
                product_color = cursor.getString(cursor.getColumnIndex("order_color"));
                new webservice_order_product().execute();
                new webservice_order_option().execute();
                try {
                    sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        Toast.makeText(getApplicationContext(), "Successfully Received payment", Toast.LENGTH_LONG).show();
        new Activity_CartList.Delete_Cartlistproduct().execute();
        deleteallrecordsofCheckoutTable();
        deleteallrecordsofcartTable();
        addNotification("Payment Successfully");
        DisplayOrderStatus();
    }
    class webservice_order_product extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("order_id",Order_id);
            params.put("product_id", product_id);
            params.put("product_with_package",product_with_package);
            params.put("name", product_name);
            params.put("quantity",product_qty);
            params.put("price",product_price);
            params.put("percent_discount", product_percent_discount);
            params.put("shipping_cost",product_shipping_cost);
            params.put("total",product_subtotal);
            params.put("superimpose_image", product_superimpose_image);
            params.put("superimpose_text", product_superimpose_text);
            params.put("delivery_date",product_delivery_date);
            params.put("shipping_firstname", shipping_firstname);
            params.put("shipping_lastname",shipping_lastname);
            params.put("shipping_contact", shipping_contact);
            params.put("shipping_email", shipping_email);
            params.put("order_product_address",order_product_address);
            params.put("shipping_city",shipping_city);
            params.put("shipping_state", shipping_state);
            params.put("shipping_pincode", shipping_pincode);
            params.put("shipping_country",shipping_country);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"orders_product", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           Activity_OrdersList.dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ssss",s);
            p.dismiss();
            try {
                if(s.equalsIgnoreCase("Inserted Successfully")) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }
            } catch (Exception e) {
                e.getMessage();
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    class webservice_order_option extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("order_id", Order_id);
            params.put("order_product_id", product_id);
            params.put("product_size",product_size);
            params.put("product_color", product_color);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"order_option", params);
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
            try {
                if(s.equalsIgnoreCase("Inserted Successfully")) {
                    Toast.makeText(getApplicationContext(), "Successfully Inserted", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.getMessage();
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
           }
        }
    }
    void DisplayOrderStatus () throws Exception
    {    String alert1 = "Your Transction ID for this transaction is " + txnId+".";
        String alert2 = "We have received a payment of Rs." + gross_total +". Your order will be dispatched soon.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Transaction Success"+" (Order_id: "+Order_id+")");
        alertDialogBuilder.setMessage("Thank you,Your Order Placed Successfully." +"\n"+ alert1+"\n"+alert2);
                alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                Intent i=new Intent(getBaseContext(),DashBordActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void addNotification(String s) throws Exception{
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.tohafaa_logo)
                        .setContentTitle("Order ConFirmed Successfully")
                        .setContentText(s);
       // DashBordActivity.fragment = new Employer_HiredCandidatelistFragment();
        Intent notificationIntent = new Intent(this, DashBordActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
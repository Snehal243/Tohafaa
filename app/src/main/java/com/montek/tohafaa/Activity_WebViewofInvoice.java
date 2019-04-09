package com.montek.tohafaa;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.*;
import android.print.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import static android.webkit.WebSettings.LOAD_DEFAULT;
import static com.montek.tohafaa.Adapter.OrderListAdapter.orderid;
import static com.montek.tohafaa.LoginActivity.p;
public class Activity_WebViewofInvoice extends AppCompatActivity {
    WebView webView;
    public static ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
       try {
           p = new ProgressDialog(this);
           actionBar = getSupportActionBar();
           Actionbar("Invoice");
           webView = (WebView) findViewById(R.id.webView);
           ((LinearLayout) findViewById(R.id.li_pdf)).setVisibility(View.GONE);
           webView.getSettings().setJavaScriptEnabled(true);
           webView.setVisibility(View.VISIBLE);
           webView.getSettings().setBuiltInZoomControls(true);
           webView.getSettings().setCacheMode(LOAD_DEFAULT);//2
           webView.getSettings().setDomStorageEnabled(true);
           webView.clearHistory();
           webView.clearCache(true);
           webView.getSettings().setSupportZoom(true);
           webView.getSettings().setUseWideViewPort(false);
           webView.getSettings().setLoadWithOverviewMode(false);
           webView.loadUrl("http://responsive.tohafaa.com/home/print-order/" + orderid);
           webView.setWebViewClient(new myWebClient());
       }catch (Exception e){}
    }
    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            Activity_OrdersList.dialog();
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
//            Toast.makeText(this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            //super.onReceivedError(view, errorCode, description, failingUrl);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            p.dismiss();
            try {
                createWebPrintJob(webView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        else
        {    finish();
            return true;
        }
    }
    private void createWebPrintJob(WebView webView) throws  Exception {
        try {
            PrintManager printManager = (PrintManager) this
                    .getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter printAdapter =  null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                printAdapter = webView.createPrintDocumentAdapter("MyDocument");
            }
            else{
                Toast.makeText(getBaseContext(),"Print pdf Not Supported in this device",Toast.LENGTH_LONG).show();
            }
            String jobName = getString(R.string.app_name) +" Print Test";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                printManager.print(jobName, printAdapter,
                        new PrintAttributes.Builder().build());
            }
        }
        catch (Exception e){}
    }
    public static void Actionbar(String title)
    {
        if (actionBar != null)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(title);
            actionBar.setIcon(R.drawable.tohafaa_logoo);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
}

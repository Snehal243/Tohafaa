package com.montek.tohafaa;
import android.app.ProgressDialog;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.*;
import com.montek.tohafaa.extra.GlobalVariable;
import static com.montek.tohafaa.Activity_OrdersDetails.*;
import static com.montek.tohafaa.Adapter.OrderListAdapter.*;
public class PDFViewActivity extends AppCompatActivity {//implements OnPageChangeListener,OnLoadCompleteListener {
    TextView txv_name,txv_billno,txv_Companyname,txv_gstno,txv_useremail,txv_usercontact,txv_useraddress,txv_dateonpurchesed;
    LinearLayout liparent,pdf;
    ScrollView ll_pdflayout;
    WebView webView;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;
 //   PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG="PDFViewActivity";
    int position=-1;
    private Menu menu;
    public static ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().requestFeature(Window.FEATURE_NO_TITLE);
      //  getSupportActionBar().hide();
        setContentView(R.layout.activity_invoice);
        Ui();
     //   PDFView pdfViewer=(PDFView) findViewById(R.id.pdfviewer);
      //  pdfViewer.fromAsset("linux.pdf").load();
         actionBar = getSupportActionBar();
        txv_name.setText(paymentname);
        txv_billno.setText(invoice_no);
        txv_Companyname.setText(paymentname);
        txv_gstno.setText("27AUJPK6805C1ZQ");
        txv_useremail.setText(GlobalVariable.email);
        txv_usercontact.setText(GlobalVariable.mobile);
        txv_useraddress.setText(shipping_address_1);
//       // fn_permission();
        // create the layout params that will be used to define how your
        // button will be displayed
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

        TableRow ll1 = new TableRow (this);
        ll1.setOrientation(TableRow.HORIZONTAL);
        // Create TextView
        TextView product1 = new TextView(this);
        product1.setText("Particulars");
        TableRow.LayoutParams Params11 = new TableRow.LayoutParams(350,TableRow.LayoutParams.WRAP_CONTENT);
        product1.setLayoutParams(Params11);
        product1.setPadding(1,5,1,5);
        product1.setTextColor(Color.BLACK);
        product1.setGravity(Gravity.CENTER );
        // product.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            product1.setBackground(getResources().getDrawable(R.drawable.button_border));
        }
        ll1.addView(product1);
        TextView qty1 = new TextView(this);
        qty1.setText("Qty");
        TableRow.LayoutParams Params21 = new TableRow.LayoutParams(140,TableRow.LayoutParams.MATCH_PARENT);
        qty1.setLayoutParams(Params21);
        qty1.setPadding(1,5,1,5);
        qty1.setTextColor(Color.BLACK);
        qty1.setGravity(Gravity.CENTER );
        // qty.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            qty1.setBackground(getResources().getDrawable(R.drawable.button_border));
        }
        ll1.addView(qty1);
        TextView rate1 = new TextView(this);
        rate1.setText("Rate");
        TableRow.LayoutParams Params31 = new TableRow.LayoutParams(240,TableRow.LayoutParams.MATCH_PARENT);
        rate1.setLayoutParams(Params31);
        rate1.setPadding(1,5,1,5);
        rate1.setTextColor(Color.BLACK);
        rate1.setGravity(Gravity.CENTER );
        // rate.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rate1.setBackground(getResources().getDrawable(R.drawable.button_border));
        }
        ll1.addView(rate1);
        TextView discount = new TextView(this);
        discount.setText("Discount");
        TableRow.LayoutParams Params = new TableRow.LayoutParams(240,TableRow.LayoutParams.MATCH_PARENT);
        discount.setLayoutParams(Params);
        discount.setPadding(1,5,1,5);
        discount.setTextColor(Color.BLACK);
        discount.setGravity(Gravity.CENTER );
        // rate.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            discount.setBackground(getResources().getDrawable(R.drawable.button_border));
        }
        ll1.addView(discount);

        TextView Taxable_Amount = new TextView(this);
        Taxable_Amount.setText("Taxable Amt");
        TableRow.LayoutParams Params41 = new TableRow.LayoutParams(240,TableRow.LayoutParams.MATCH_PARENT);
        Taxable_Amount.setLayoutParams(Params41);
        Taxable_Amount.setPadding(1,5,1,5);
        Taxable_Amount.setTextColor(Color.BLACK);
        Taxable_Amount.setGravity(Gravity.CENTER );
        // rate.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Taxable_Amount.setBackground(getResources().getDrawable(R.drawable.button_border));
        }
        ll1.addView(Taxable_Amount);

        TextView gstamt = new TextView(this);
        gstamt.setText("GST Amt");
        TableRow.LayoutParams Params223 = new TableRow.LayoutParams(240,TableRow.LayoutParams.MATCH_PARENT);
        gstamt.setLayoutParams(Params223);
        gstamt.setPadding(1,5,1,5);
        gstamt.setTextColor(Color.BLACK);
        gstamt.setGravity(Gravity.CENTER );
        // rate.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            gstamt.setBackground(getResources().getDrawable(R.drawable.button_border));
        }
        ll1.addView(gstamt);

        TextView delchargamt = new TextView(this);
        delchargamt.setText("Delivery charge");
        TableRow.LayoutParams Params8 = new TableRow.LayoutParams(240,TableRow.LayoutParams.MATCH_PARENT);
        delchargamt.setLayoutParams(Params8);
        delchargamt.setPadding(1,5,1,5);
        delchargamt.setTextColor(Color.BLACK);
        delchargamt.setGravity(Gravity.CENTER );
        // rate.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            delchargamt.setBackground(getResources().getDrawable(R.drawable.button_border));
        }
        ll1.addView(delchargamt);

        // Create TextView
        TextView amount1 = new TextView(this);
        amount1.setText("Amount");
        TableRow.LayoutParams Params411 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1f);
        amount1.setLayoutParams(Params411);
        amount1.setPadding(1,5,1,5);
        amount1.setTextColor(Color.BLACK);
        amount1.setGravity(Gravity.CENTER );
        //amount.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            amount1.setBackground(getResources().getDrawable(R.drawable.button_border));
        }
        ll1.addView(amount1);

        //Add button to LinearLayout defined in XML
        liparent.addView(ll1);
        //Create four
        for(int i=0;i<order_product_id.length;i++)
        {
            // Create LinearLayout
            TableRow  ll = new TableRow (this);
            ll.setOrientation(TableRow.HORIZONTAL);

            // Create TextView
            TextView product = new TextView(this);
            product.setText(item.get(i).getproduct_name());
            TableRow.LayoutParams Params1 = new TableRow.LayoutParams(350,TableRow.LayoutParams.WRAP_CONTENT);
            product.setLayoutParams(Params1);
            product.setPadding(1,5,1,5);
            product.setTextColor(Color.BLACK);
            product.setGravity(Gravity.CENTER );
           // product.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                product.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll.addView(product);

            TextView qty = new TextView(this);
            qty.setText(item.get(i).getproduct_quantity());
            TableRow.LayoutParams Params2 = new TableRow.LayoutParams(140,TableRow.LayoutParams.MATCH_PARENT);
            qty.setLayoutParams(Params2);
            qty.setPadding(1,5,1,5);
            qty.setTextColor(Color.BLACK);
            qty.setGravity(Gravity.CENTER );
           // qty.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                qty.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll.addView(qty);

            TextView rate = new TextView(this);
            rate.setText(item.get(i).getproduct_price());
            TableRow.LayoutParams Params3 = new TableRow.LayoutParams(240,TableRow.LayoutParams.MATCH_PARENT);
            rate.setLayoutParams(Params3);
            rate.setPadding(1,5,1,5);
            rate.setTextColor(Color.BLACK);
            rate.setGravity(Gravity.CENTER );
           // rate.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rate.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll.addView(rate);

            TextView txvdiscount = new TextView(this);
            txvdiscount.setText(item.get(i).getdiscount());
            TableRow.LayoutParams Params34= new TableRow.LayoutParams(240,TableRow.LayoutParams.MATCH_PARENT);
            txvdiscount.setLayoutParams(Params34);
            txvdiscount.setPadding(1,5,1,5);
            txvdiscount.setTextColor(Color.BLACK);
            txvdiscount.setGravity(Gravity.CENTER );
            // rate.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                txvdiscount.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll.addView(txvdiscount);

            float txableamt=100*Float.parseFloat(item.get(i).getproduct_price())/(100+Float.parseFloat(item.get(i).getproduct_gst()));
            TextView txvtaxableamount = new TextView(this);
            txvtaxableamount.setText(Float.toString(txableamt));
            TableRow.LayoutParams Params341= new TableRow.LayoutParams(240,TableRow.LayoutParams.MATCH_PARENT);
            txvtaxableamount.setLayoutParams(Params341);
            txvtaxableamount.setPadding(1,5,1,5);
            txvtaxableamount.setTextColor(Color.BLACK);
            txvtaxableamount.setGravity(Gravity.CENTER );
            // rate.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                txvtaxableamount.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll.addView(txvtaxableamount);

            TextView gstamttxv = new TextView(this);
            gstamttxv.setText(Float.toString(txableamt*(Float.parseFloat(item.get(i).getproduct_gst())/100)));
            TableRow.LayoutParams Params32 = new TableRow.LayoutParams(240,TableRow.LayoutParams.MATCH_PARENT);
            gstamttxv.setLayoutParams(Params32);
            gstamttxv.setPadding(1,5,1,5);
            gstamttxv.setTextColor(Color.BLACK);
            gstamttxv.setGravity(Gravity.CENTER );
            // rate.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                gstamttxv.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll.addView(gstamttxv);

            TextView Delchargtxv = new TextView(this);
            Delchargtxv.setText(item.get(i).getproduct_shipping_cost());
            TableRow.LayoutParams Params5 = new TableRow.LayoutParams(240,TableRow.LayoutParams.MATCH_PARENT);
            Delchargtxv.setLayoutParams(Params5);
            Delchargtxv.setPadding(1,5,1,5);
            Delchargtxv.setTextColor(Color.BLACK);
            Delchargtxv.setGravity(Gravity.CENTER );
            // rate.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Delchargtxv.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll.addView(Delchargtxv);

            // Create TextView
            TextView amount = new TextView(this);
            amount.setText(Float.toString(Float.parseFloat(item.get(i).getproduct_shipping_cost())+Float.parseFloat(item.get(i).getordertotal())));
            TableRow.LayoutParams Params45 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1f);
            amount.setLayoutParams(Params45);
            amount.setPadding(1,5,1,5);
            amount.setTextColor(Color.BLACK);
            amount.setGravity(Gravity.CENTER );
            //amount.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                amount.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll.addView(amount);
            //Add button to LinearLayout defined in XML
            liparent.addView(ll);
        }
        for(int i=0;i<1;i++) {
            TableRow ll2 = new TableRow(this);
            ll2.setOrientation(TableRow.HORIZONTAL);
            String Producttxt="",Amounttxt="";

             if(i==0)
            {
                Producttxt="Gross Total";
                Amounttxt=ordergross_total;
            }
            // Create TextView
            TextView product2 = new TextView(this);
            product2.setText(Producttxt);
            TableRow.LayoutParams Params12 = new TableRow.LayoutParams(350, TableRow.LayoutParams.WRAP_CONTENT);
            product2.setLayoutParams(Params12);
            product2.setPadding(1, 5, 1, 5);
            product2.setTextColor(Color.BLACK);
            product2.setGravity(Gravity.CENTER);
            // product.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                product2.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll2.addView(product2);

            TextView qty2 = new TextView(this);
            qty2.setText("-");
            TableRow.LayoutParams Params22 = new TableRow.LayoutParams(140, TableRow.LayoutParams.MATCH_PARENT);
            qty2.setLayoutParams(Params22);
            qty2.setPadding(1, 5, 1, 5);
            qty2.setTextColor(Color.BLACK);
            qty2.setGravity(Gravity.CENTER);
            // qty.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                qty2.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll2.addView(qty2);

            TextView rate2 = new TextView(this);
            rate2.setText("-");
            TableRow.LayoutParams Params32 = new TableRow.LayoutParams(240, TableRow.LayoutParams.MATCH_PARENT);
            rate2.setLayoutParams(Params32);
            rate2.setPadding(1, 5, 1, 5);
            rate2.setTextColor(Color.BLACK);
            rate2.setGravity(Gravity.CENTER);
            // rate.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rate2.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll2.addView(rate2);

            TextView discount2 = new TextView(this);
            discount2.setText("-");
            TableRow.LayoutParams Params312 = new TableRow.LayoutParams(240, TableRow.LayoutParams.MATCH_PARENT);
            discount2.setLayoutParams(Params312);
            discount2.setPadding(1, 5, 1, 5);
            discount2.setTextColor(Color.BLACK);
            discount2.setGravity(Gravity.CENTER);
            // rate.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                discount2.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll2.addView(discount2);


            TextView 	TaxableAmount = new TextView(this);
            TaxableAmount.setText("-");
            TableRow.LayoutParams Params3 = new TableRow.LayoutParams(240, TableRow.LayoutParams.MATCH_PARENT);
            TaxableAmount.setLayoutParams(Params3);
            TaxableAmount.setPadding(1, 5, 1, 5);
            TaxableAmount.setTextColor(Color.BLACK);
            TaxableAmount.setGravity(Gravity.CENTER);
            // rate.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                TaxableAmount.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll2.addView(TaxableAmount);

            TextView delcharge2 = new TextView(this);
            delcharge2.setText("-");
            TableRow.LayoutParams Params6 = new TableRow.LayoutParams(240, TableRow.LayoutParams.MATCH_PARENT);
            delcharge2.setLayoutParams(Params6);
            delcharge2.setPadding(1, 5, 1, 5);
            delcharge2.setTextColor(Color.BLACK);
            delcharge2.setGravity(Gravity.CENTER);
            // rate.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                delcharge2.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll2.addView(delcharge2);

            TextView gstamount2 = new TextView(this);
            gstamount2.setText("-");
            TableRow.LayoutParams Params321 = new TableRow.LayoutParams(240, TableRow.LayoutParams.MATCH_PARENT);
            gstamount2.setLayoutParams(Params321);
            gstamount2.setPadding(1, 5, 1, 5);
            gstamount2.setTextColor(Color.BLACK);
            gstamount2.setGravity(Gravity.CENTER);
            // rate.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                gstamount2.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll2.addView(gstamount2);

            // Create TextView
            TextView amount2 = new TextView(this);
            amount2.setText(Amounttxt);
            TableRow.LayoutParams Params42 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
            amount2.setLayoutParams(Params42);
            amount2.setPadding(1, 5, 1, 5);
            amount2.setTextColor(Color.BLACK);
            amount2.setGravity(Gravity.CENTER);
            //amount.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                amount2.setBackground(getResources().getDrawable(R.drawable.button_border));
            }
            ll2.addView(amount2);
            //Add button to LinearLayout defined in XML
            liparent.addView(ll2);
        }
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
    private void Ui(){
        txv_name=(TextView)findViewById(R.id.txv_name);
        txv_billno=(TextView)findViewById(R.id.txv_billno);
        txv_Companyname=(TextView)findViewById(R.id.txv_Companyname);
        txv_gstno=(TextView)findViewById(R.id.txv_gstno);
        txv_useremail=(TextView)findViewById(R.id.txv_useremail);
        txv_usercontact=(TextView)findViewById(R.id.txv_usercontact);
        txv_useraddress=(TextView)findViewById(R.id.txv_useraddress);
        txv_dateonpurchesed=(TextView)findViewById(R.id.txv_dateonpurchesed);
        liparent = (LinearLayout)findViewById(R.id.liparent);
        ll_pdflayout = (ScrollView) findViewById(R.id.ll_pdflayout);
        pdf = (LinearLayout) findViewById(R.id.pdf);
    }
}

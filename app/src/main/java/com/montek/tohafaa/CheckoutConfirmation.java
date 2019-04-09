package com.montek.tohafaa;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import static com.montek.tohafaa.Activity_WebViewofInvoice.*;
public class CheckoutConfirmation extends AppCompatActivity implements View.OnClickListener {
    TextView txv_totalpayment,txv_name,txv_email,txv_phone,txv_Address;
    Button btn_paynow;
    public static  String payment_mobile,payment_email,payment_firstname,payment_lastname,payment_address_1,payment_address_2,payment_city,payment_state,payment_postcode,payment_country,payment_total,shipping_cost,shipping_address_same_as_billing_address,gross_total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkoutconfirmation);
        try{actionBar = getSupportActionBar();
        Actionbar("Checkout Page");
        UI();
            Intent oIntent = getIntent();
            payment_firstname = oIntent.getStringExtra("payment_firstname");
            payment_lastname = oIntent.getStringExtra("payment_lastname");
            payment_email = oIntent.getStringExtra("email");
            payment_mobile = oIntent.getStringExtra("mobile");
            payment_address_1 = oIntent.getStringExtra("payment_address_1");
            payment_address_2 = oIntent.getStringExtra("payment_address_2");
            payment_city = oIntent.getStringExtra("payment_city");
            payment_state = oIntent.getStringExtra("payment_state");
            payment_postcode = oIntent.getStringExtra("payment_postcode");
            payment_country = oIntent.getStringExtra("payment_country");
            payment_total = oIntent.getStringExtra("total");
            shipping_cost = oIntent.getStringExtra("shipping_cost");
            gross_total = oIntent.getStringExtra("gross_total");
            shipping_address_same_as_billing_address = oIntent.getStringExtra("shipping_address_same_as_billing_address");
            txv_totalpayment.setText(gross_total);
            txv_name.setText(payment_firstname + " " + payment_lastname);
            txv_email.setText(payment_email);
            txv_phone.setText(payment_mobile);
            txv_Address.setText(payment_address_1);
        }catch (Exception e){ e.getMessage(); }
    }
    void UI() throws Exception
    {
        btn_paynow = (Button)findViewById(R.id.btn_paynow);
        btn_paynow.setOnClickListener(this);
        txv_totalpayment = (TextView)findViewById(R.id.txv_totalpayment);
        txv_name = (TextView)findViewById(R.id.txv_name);
        txv_email = (TextView)findViewById(R.id.txv_email);
        txv_phone = (TextView)findViewById(R.id.txv_phone);
        txv_Address = (TextView)findViewById(R.id.txv_Address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_paynow:
                Intent i=new Intent(this,PayMentGateWay.class);
                startActivity(i);
                finish();
                break;
        }
    }
}
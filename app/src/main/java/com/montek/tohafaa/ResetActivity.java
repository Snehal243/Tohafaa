package com.montek.tohafaa;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.*;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.*;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.URLs;
import java.util.HashMap;
import java.util.Random;
import static com.montek.tohafaa.LoginActivity.p;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtx_email,edtx_otp;
    private Button btn_go,btn_submit;
    public static long random_no;
    public static String email;
    TextView link_signup;
    TextInputLayout otpinputlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        p = new ProgressDialog(this);
        try {
            UI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void UI() throws Exception
    {
        otpinputlayout = (TextInputLayout) findViewById(R.id.otpinputlayout);
        edtx_email = (EditText) findViewById(R.id.edtx_email);
        edtx_otp = (EditText) findViewById(R.id.edtx_otp);
        btn_go = (Button) findViewById(R.id.btn_go);
        btn_go.setOnClickListener(this);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        link_signup = (TextView) findViewById(R.id.link_signup);
        link_signup.setOnClickListener(this);
        edtx_otp.setEnabled(false);
        btn_submit.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.link_signup:
                    Intent i = new Intent(this, RegisterActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case R.id.btn_go:
                    email = edtx_email.getText().toString().trim();
                    if (TextUtils.isEmpty(email)) {
                        edtx_email.setError("Please enter valid email");
                        edtx_email.requestFocus();
                        return;
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        edtx_email.setError("Please enter valid email");
                        edtx_email.requestFocus();
                        return;
                    }
                    random_no = generateRandom(6);
                    new Reset_Password().execute();
                    break;
                case R.id.btn_submit:
                    boolean isValid = true;
                    String OTP = edtx_otp.getText().toString().trim();
                    if (OTP.isEmpty()) {
                        otpinputlayout.setError("Please enter valid OTP.");
                        isValid = false;
                    } else {
                        otpinputlayout.setErrorEnabled(false);
                    }
                    String a = String.valueOf(random_no);

                    if (isValid) {
                        if (OTP.equalsIgnoreCase(a)) {
                            Intent ii = new Intent(ResetActivity.this, ResetPassword.class);
                            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(ii);
                        } else {
                            Toast.makeText(getBaseContext(), "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }catch (Exception e){}
    }
    @Override
    public void onBackPressed() {
        Intent forgotpassword=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(forgotpassword);
        finish();
    }
    public static long generateRandom(int length) throws Exception {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }
    class Reset_Password extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("OTP",String.valueOf(random_no));
            Log.d("url",URLs.URL_REGISTER+"SendOTP");
            return requestHandler.sendPostRequest(URLs.URL_REGISTER+"SendOTP", params);
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
            Log.d("hhh",s);
            if(s.contains("send otp Successfully"))
            {
                edtx_otp.setEnabled(true);
                btn_submit.setEnabled(true);
                Toast.makeText(getBaseContext(),"send otp Successfully to your email",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
            }
        }
    }
}

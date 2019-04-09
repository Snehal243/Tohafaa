package com.montek.tohafaa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.CheckForConnection;
import com.montek.tohafaa.extra.GlobalVariable;
import com.montek.tohafaa.extra.URLs;
import com.montek.tohafaa.extra.UserSessionManager;
import com.montek.tohafaa.model.Item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.montek.tohafaa.extra.GlobalVariable.user_id;
import static java.lang.Thread.sleep;
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public static int UpdateProfile=0,flagsqliteretrivedata=0;
    AppCompatButton login,btnSignIn;
    Button btn_skip;
    EditText edtx_email,edtx_password ;
    TextView link_signup,txv_forgotpassword;
    String oauth_uid,lastname,email,password,firstname;
    private static final int RC_SIGN_IN = 420;
    String Flag="";
    TextInputLayout passwordinputlayout,emailinputlayout ;
  //  private SignInButton btnSignIn;
    private GoogleApiClient mGoogleApiClient;
    public static UserSessionManager session;
    LoginButton btnFacebookLogin;
    private CallbackManager callbackManager;
    public static ProgressDialog p;
    CheckForConnection cf;
    LinearLayout coordinatorLayout;
    String sv_key,product_id,product_name,product_price,product_image,product_shipping_cost,Quantity;
    public static String [] cat_id,cat_name,cat_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//for layout adjust when keybord
        FacebookSdk.sdkInitialize(getApplicationContext());
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        iUI();
        GlobalVariable.Category.clear();
        p=new ProgressDialog(this);
        cf = new CheckForConnection();//to check internet connection
        boolean mobile = cf.getMobileDataStatus(this);
        boolean wifi = cf.getWifiStatus(this);
        if(mobile||wifi) {
            session = new UserSessionManager(getApplicationContext());//get session data
            HashMap<String, String> user = session.getUserDetails();
            user_id = user.get(UserSessionManager.KEY_Userid);
            GlobalVariable.first_name = user.get(UserSessionManager.key_first_name);
            GlobalVariable.last_name = user.get(UserSessionManager.key_last_name);
            GlobalVariable.mobile = user.get(UserSessionManager.key_mobile);
            GlobalVariable.email = user.get(UserSessionManager.key_email);
            GlobalVariable.address_line_1 = user.get(UserSessionManager.key_add1);
            GlobalVariable.address_line_2 = user.get(UserSessionManager.key_add2);
            GlobalVariable.registration_status = user.get(UserSessionManager.key_registration_status);
            GlobalVariable.country = user.get(UserSessionManager.key_country);
            GlobalVariable.state = user.get(UserSessionManager.key_state);
            GlobalVariable.city = user.get(UserSessionManager.key_city);
            GlobalVariable.postcode = user.get(UserSessionManager.key_postcode);
            GlobalVariable.picture = user.get(UserSessionManager.key_picture);
            new Retrive_Category().execute();
        }
        else
        {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Check Internet Connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    private void iUI() {
        coordinatorLayout = (LinearLayout) findViewById(R.id.parent_view);
        edtx_email= (EditText) findViewById(R.id.edtx_email);
        edtx_password = (EditText) findViewById(R.id.edtx_password);
        passwordinputlayout = (TextInputLayout) findViewById(R.id.passwordinputlayout);
        emailinputlayout = (TextInputLayout) findViewById(R.id.emailinputlayout);
        link_signup = (TextView) findViewById(R.id.link_signup);
        login = (AppCompatButton) findViewById(R.id.btn_login);
        btnSignIn = (AppCompatButton) findViewById(R.id.btn_googlesignin);
        btnFacebookLogin = (LoginButton)findViewById(R.id.login_facebookbutton);
        btnFacebookLogin.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        login.setOnClickListener(this);
        link_signup.setOnClickListener(this);
        btn_skip = (Button)findViewById(R.id.btn_skip);
        btn_skip.setOnClickListener(this);
        txv_forgotpassword = (TextView) findViewById(R.id.txv_forgotpassword);
        txv_forgotpassword.setOnClickListener(this);
    }
    private void initializeGPlusSettings() throws Exception{
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Log.d("running",mGoogleApiClient.toString());
        // Customizing G+ button
      //  btnSignIn.setSize(SignInButton.SIZE_WIDE);
      //  btnSignIn.setScopes(gso.getScopeArray());
    }
//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setMessage("Do you want to Exit?");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//
//            }
//        });
//        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog alert=builder.create();
//        alert.show();
//    }

    private void signIn() {    //for google login
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d("RC_SIGN_IN",Integer.toString(RC_SIGN_IN));
    }
    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }
    private void handleGPlusSignInResult (GoogleSignInResult result){
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();// Signed in successfully, show authenticated UI.
             if (acct != null) { //Fetch values
                 firstname = acct.getDisplayName();
                lastname = acct.getFamilyName();
                email = acct.getEmail();
                 oauth_uid = acct.getId();
                Flag="google";
                new RegisterUser().execute();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Flag.equalsIgnoreCase("facebook")) {
            callbackManager.onActivityResult(requestCode, resultCode, data);   //for facebook
        }
        else if(Flag.equalsIgnoreCase("google"))//for google
        {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleGPlusSignInResult(result);
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if( Flag.equalsIgnoreCase("google")) {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // and the GoogleSignInResult will be available instantly.
                Log.d("tag", "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleGPlusSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        handleGPlusSignInResult(googleSignInResult);
                    }
                });
            }
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not be available.
        Log.d("tag", "onConnectionFailed:" + connectionResult);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txv_forgotpassword:
                finish();
                Intent forgotpassword=new Intent(getApplicationContext(),ResetActivity.class);
                startActivity(forgotpassword);
                break;
            case R.id.btn_skip:
                Intent i1=new Intent(this,DashBordActivity.class);
                startActivity(i1);
                finish();
                break;
            case R.id.btn_login:
                email = edtx_email.getText().toString().trim();
                password = edtx_password.getText().toString().trim();
                signUp();
                break;
            case R.id.link_signup:
                UpdateProfile=0;
                Intent i=new Intent(this,RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.btn_googlesignin:
                try {
                    initializeGPlusSettings();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Flag="google";
                signIn();
                break;
            case R.id.login_facebookbutton:
                Flag="facebook";
                btnFacebookLogin.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
                callbackManager = CallbackManager.Factory.create();
                                btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {// Callback registration
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {

                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("Main", response.toString());
                                        setProfileToView(object);
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }
                    @Override
                    public void onCancel() {
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "error to Login Facebook", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
       private void setProfileToView(JSONObject jsonObject) {//for facebook login
        try {
            email = jsonObject.getString("email");
            // String  first_name=jsonObject.getString("first_name");
            String gender = jsonObject.getString("gender");
            oauth_uid = jsonObject.getString("id");
                // String  locale=jsonObject.getString("locale");
                //  String  profile_pic=jsonObject.getString("profile_pic");
               // String link = jsonObject.getString("link");
            firstname=jsonObject.getString("name");
            Flag="facebook";
            Toast.makeText(getApplication(), email+gender+oauth_uid+firstname, Toast.LENGTH_SHORT).show();
//			profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
//			profilePictureView.setProfileId(jsonObject.getString("id"));
//			infoLayout.setVisibility(View.VISIBLE);
            new RegisterUser().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void signUp() {
        boolean isValid = true;
        if (email.isEmpty()) {
            emailinputlayout.setError("Please fill out this field");
            isValid = false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailinputlayout.setError("email should be valid");
            isValid = false;
        }
        else {
            emailinputlayout.setErrorEnabled(false);
        }
         if (password.isEmpty()) {
            passwordinputlayout.setError("Please fill out this field");
            isValid = false;
        }
        else  if (!isValidPassword(password)) {
            passwordinputlayout.setError("password should be valid (for e.g Example@123)");
            isValid = false;
        } else {
            passwordinputlayout.setErrorEnabled(false);
        }
        if (isValid) {
            new  RegisterLogin().execute();
        }
    }
    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
    void clear()
    {
        edtx_email.setText("");
        edtx_password.setText("");
    }
    class RegisterLogin extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);
            return requestHandler.sendPostRequest(URLs.URL_REGISTER+"login", params);
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
            Log.d("sss",s.toString());
            JSONArray jsonArray = null;
            try {
                if (s.contains("user_id")) {
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        user_id = jsonObject.getString("user_id");
                        GlobalVariable.first_name  = jsonObject.getString("first_name");
                        GlobalVariable.last_name=jsonObject.getString("last_name");
                        GlobalVariable.mobile  = jsonObject.getString("user_mobile");
                        GlobalVariable.email  = jsonObject.getString("email");
                        GlobalVariable.address_line_1  = jsonObject.getString("address_line_1");
                        GlobalVariable.address_line_2  = jsonObject.getString("address_line_2");
                        GlobalVariable.registration_status=jsonObject.getString("registration_status");
                        GlobalVariable.city=jsonObject.getString("city");
                        GlobalVariable.state=jsonObject.getString("state");
                        GlobalVariable.country=jsonObject.getString("country");
                        GlobalVariable.postcode=jsonObject.getString("postcode");
                        GlobalVariable.picture=jsonObject.getString("picture");
                    }
                    UserSessionManager.createUserLoginSession(user_id,GlobalVariable.first_name,GlobalVariable.last_name,GlobalVariable.mobile,GlobalVariable.email,GlobalVariable.address_line_1,GlobalVariable.address_line_2,GlobalVariable.registration_status,GlobalVariable.postcode,GlobalVariable.country,GlobalVariable.state,GlobalVariable.city,GlobalVariable.picture);
                    addcart();
                }
                else
                {
                    Toast.makeText(getBaseContext(),s.toString(), Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {  e.getMessage(); }
        }
    }

    void addcart()
    {
        if (user_id != null && !user_id.isEmpty() && !user_id.equals("null") )
        {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                sv_key = extras.getString("sv_key");
                product_id = extras.getString("id");
                Log.d("product_id",product_id);
                product_name = extras.getString("name");
                product_price = extras.getString("price");
                product_image = extras.getString("src");
                product_shipping_cost = extras.getString("product_shipping_cost");
                Quantity =extras.getString("max_quantity");
                new AddToWishlist().execute();
            }
            else
            {
                startActivity(new Intent(getApplicationContext(), DashBordActivity.class));
            }
        }
        else
        {
            Toast.makeText(getBaseContext(),"Your Account Was not Verified" ,Toast.LENGTH_LONG).show();
        }
    }
    class RegisterUser extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            if(firstname.equalsIgnoreCase(null))
            {
                firstname="";
            }
            params.put("oauth_provider", Flag);
            params.put("oauth_uid", oauth_uid);
            params.put("first_name", firstname);
             if(lastname!=(null))
            {
               params.put("last_name", lastname);
            }
            else {
                 params.put("last_name", "");
             }
            params.put("user_password", "");
            params.put("email", email);
            params.put("user_mobile", "");
            params.put("gender","");
            params.put("user_dob","");
            params.put("locale", "");
            params.put("picture", "");
            params.put("link", "");
            params.put("created", "");
            params.put("modified", "");
            params.put("is_deleted", "");
            params.put("profile_url", "");
            params.put("picture_url", "");
            params.put("password", "");
            params.put("forgot_password","");
            params.put("forgot_password_reset","");
            params.put("registration_status", "1");
            params.put("status","1");
            params.put("address_line_1","");
            params.put("address_line_2", "");
            params.put("postcode","");
            params.put("city","");
            params.put("state", "");
            params.put("country","");
            params.put("user_keep_me_signed","");
            return requestHandler.sendPostRequest(URLs.URL_REGISTER+"signup", params);
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
           JSONArray jsonArray = null;
            try {
                if(Flag.equalsIgnoreCase("facebook"))
                {
                    LoginManager.getInstance().logOut();
                }
                else if(Flag.equalsIgnoreCase("google"))
                {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<com.google.android.gms.common.api.Status>() {
                                @Override
                                public void onResult(com.google.android.gms.common.api.Status status) {
                                }
                            });
                }
                if (s.contains("user_id")) {
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        user_id = jsonObject.getString("user_id");
                        GlobalVariable.first_name  = jsonObject.getString("first_name");
                        GlobalVariable.last_name=jsonObject.getString("last_name");
                        GlobalVariable.mobile  = jsonObject.getString("user_mobile");
                        GlobalVariable.email  = jsonObject.getString("email");
                        GlobalVariable.address_line_1  = jsonObject.getString("address_line_1");
                        GlobalVariable.address_line_2  = jsonObject.getString("address_line_2");
                        GlobalVariable.registration_status=jsonObject.getString("registration_status");
                        GlobalVariable.city=jsonObject.getString("city");
                        GlobalVariable.state=jsonObject.getString("state");
                        GlobalVariable.country=jsonObject.getString("country");
                        GlobalVariable.postcode=jsonObject.getString("postcode");
                        GlobalVariable.picture=jsonObject.getString("picture");
                    }
                    UserSessionManager.createUserLoginSession(user_id,GlobalVariable.first_name,GlobalVariable.last_name,GlobalVariable.mobile,GlobalVariable.email,GlobalVariable.address_line_1,GlobalVariable.address_line_2,GlobalVariable.registration_status,GlobalVariable.postcode,GlobalVariable.country,GlobalVariable.state,GlobalVariable.city,GlobalVariable.picture);
                    addcart();
                }
            }
            catch (Exception e) {e.getMessage();}
        }
    }
    public  class Retrive_Category extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category+"category", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("category",s);
              p.dismiss();
            if(s.contains("No Results Found")) {
                Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
            }
            else {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    String categoryid = null, categoryname = null, category_image = null;
                    Item item;
                    cat_id = new String[jsonArray.length()];
                    cat_name = new String[jsonArray.length()];
                    cat_image = new String[jsonArray.length()];
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        categoryid = jsonObject.getString("category_id");
                        categoryname = jsonObject.getString("category_name");
                        category_image = jsonObject.getString("category_image");
                        cat_id[i] = jsonObject.getString("category_id");
                        cat_name[i] = jsonObject.getString("category_name");
                        cat_image[i] = jsonObject.getString("category_image");
                       if(cat_name[i].equalsIgnoreCase("Special")) {}
                       else {
                           item = new Item(categoryname + " Gifting", Integer.parseInt(categoryid), "category", category_image);
                           GlobalVariable.Category.add(item);
                       }
                    }
                    if (user_id != null && !user_id.isEmpty() && !user_id.equals("null") ) {
                        Intent i = new Intent(LoginActivity.this, DashBordActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) { e.getMessage(); }
            }
        }
    }
    class AddToWishlist extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("sv_user_id", user_id);
            params.put("sv_key","my_wishlist");
            params.put("id",product_id);
            params.put("name",product_name);
            params.put("price",product_price);
            params.put("src",product_image);
            params.put("product_shipping_cost",product_shipping_cost);
            params.put("max_quantity",Quantity);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations+"addtowishlist", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ss",s);
            p.dismiss();
            Toast.makeText(getBaseContext(),s.toString(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), DashBordActivity.class));
        }
    }
}

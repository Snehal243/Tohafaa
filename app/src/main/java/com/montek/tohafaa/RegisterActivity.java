package com.montek.tohafaa;
import android.app.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.montek.tohafaa.JsonConnection.RequestHandler;
import com.montek.tohafaa.extra.*;
import com.montek.tohafaa.model.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import net.gotev.uploadservice.MultipartUploadRequest;
import org.json.*;
import java.io.File;
import java.util.*;
import static com.montek.tohafaa.Adapter.ProductAdapter.imgloader;
import static com.montek.tohafaa.DashBordActivity.dialog;
import static com.montek.tohafaa.LoginActivity.*;
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner spin_country, spin_City_District_Town, spin_State;
    AppCompatButton signup;
    int country_id, state_id, city_id;
    EditText edtx_pincode, edtx_firstname, edtx_lastname, edtx_email, edtx_mobile, edtx_password, edtx_cnfpassword, edtx_addressline1, edtx_addressline2;
    TextView link_login;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;
    private Bitmap bitmap;
    ImageView uploadimage;
    String imageName="";
    public static String phonecode, pincodeerror="0",emailmobileerror="0",emailerror = "0",mobileerror="0", pincode,emailmobile, addressline2, addressline1, cnfpassword, firstname, lastname, email, mobile, password;
    LinearLayout li_img,pincodelayout, passwordlayout, Spinnerlayout;
    TextInputLayout pincodeinputlayout, mobileinputlayout, emailinputlayout, address2inputlayout, address1inputlayout, cnfpasswordinputlayout, firstinputlayout, lastinputlayout, passwordinputlayout;
    public static TextInputLayout otpinputlayout;
    public static int OTP, resendotpflag = 0;
    public static String edtx_OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
       try {
           requestStoragePermission();
           iUI();
           p = new ProgressDialog(this);
           if (UpdateProfile == 1)//for update profile flag 1
           {
               ActionBar actionBar = getSupportActionBar();
               assert actionBar != null;
               actionBar.setHomeButtonEnabled(true);
               new RetriveCountry().execute();
               new RetriveState().execute();
               new Retrivecity().execute();
               if (GlobalVariable.postcode.equalsIgnoreCase("0") || GlobalVariable.postcode.equalsIgnoreCase("")) {
                   edtx_pincode.setText("");
               } else {
                   edtx_pincode.setText(GlobalVariable.postcode);
               }
           }
           edtx_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

               @Override
               public void onFocusChange(View v, boolean hasFocus) {
                   if (!hasFocus) {
                       if (edtx_email.getText().toString().trim().length() != 0 && Patterns.EMAIL_ADDRESS.matcher(edtx_email.getText().toString().trim()).matches()) {
                           emailerror = "1";
                           emailmobileerror = "1";
                           emailmobile = edtx_email.getText().toString().trim();
                           new CkeckMobileEmail().execute();
                       }
                   }
               }
           });
           edtx_mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
               @Override
               public void onFocusChange(View v, boolean hasFocus) {
                   if (!hasFocus) {
                       if (edtx_mobile.getText().toString().trim().length() != 0 && edtx_mobile.getText().toString().trim().length() > 9) {
                           mobileerror = "1";
                           emailmobileerror = "2";
                           emailmobile = edtx_mobile.getText().toString().trim();
                           new CkeckMobileEmail().execute();
                       }
                   }
               }
           });
       }catch (Exception e){}
    }
    private void iUI() throws Exception {
        edtx_firstname = (EditText) findViewById(R.id.edtx_firstname);
        edtx_lastname = (EditText) findViewById(R.id.edtx_lastname);
        edtx_email = (EditText) findViewById(R.id.edtx_email);
        edtx_mobile = (EditText) findViewById(R.id.edtx_mobile);
        edtx_password = (EditText) findViewById(R.id.edtx_password);
        edtx_cnfpassword = (EditText) findViewById(R.id.edtx_cnfpassword);
        edtx_addressline1 = (EditText) findViewById(R.id.edtx_addressline1);
        edtx_addressline2 = (EditText) findViewById(R.id.edtx_addressline2);
        edtx_pincode = (EditText) findViewById(R.id.edtx_pincode);
        link_login = (TextView) findViewById(R.id.link_login);
        li_img = (LinearLayout) findViewById(R.id.upload_img);
        li_img.setVisibility(View.GONE);
        uploadimage=(ImageView)findViewById(R.id.imageView);
        uploadimage.setOnClickListener(this);
        link_login.setOnClickListener(this);
        signup = (AppCompatButton) findViewById(R.id.btn_signup);
        signup.setOnClickListener(this);
        Spinnerlayout = (LinearLayout) findViewById(R.id.Spinner);
        Spinnerlayout.setVisibility(View.GONE);
        pincodelayout = (LinearLayout) findViewById(R.id.pincode);
        pincodelayout.setVisibility(View.GONE);
        passwordlayout = (LinearLayout) findViewById(R.id.password);
        passwordlayout.setVisibility(View.VISIBLE);
        firstinputlayout = (TextInputLayout) findViewById(R.id.firstinputlayout);
        lastinputlayout = (TextInputLayout) findViewById(R.id.lastinputlayout);
        mobileinputlayout = (TextInputLayout) findViewById(R.id.mobileinputlayout);
        passwordinputlayout = (TextInputLayout) findViewById(R.id.passwordinputlayout);
        cnfpasswordinputlayout = (TextInputLayout) findViewById(R.id.cnfpasswordinputlayout);
        address1inputlayout = (TextInputLayout) findViewById(R.id.address1inputlayout);
        address2inputlayout = (TextInputLayout) findViewById(R.id.address2inputlayout);
        emailinputlayout = (TextInputLayout) findViewById(R.id.emailinputlayout);
        pincodeinputlayout = (TextInputLayout) findViewById(R.id.pincodeinputlayout);
        if (UpdateProfile == 1) {
            li_img.setVisibility(View.VISIBLE);
            pincodelayout.setVisibility(View.VISIBLE);
            edtx_firstname.setText(GlobalVariable.first_name);
            edtx_lastname.setText(GlobalVariable.last_name);
            edtx_email.setText(GlobalVariable.email);
            edtx_email.setEnabled(false);
            edtx_email.setTextColor(getResources().getColor(android.R.color.black));
            edtx_mobile.setText(GlobalVariable.mobile);
            edtx_addressline1.setText(GlobalVariable.address_line_1);
            address1inputlayout.setHint("Locality");
            edtx_addressline2.setText(GlobalVariable.address_line_2);
            link_login.setVisibility(View.GONE);
            signup.setText("Update Profile");
            Spinnerlayout.setVisibility(View.VISIBLE);
            passwordlayout = (LinearLayout) findViewById(R.id.password);
            passwordlayout.setVisibility(View.GONE);
            spin_country = (Spinner) findViewById(R.id.spin_country);
            spin_country.setOnItemSelectedListener(this);
            spin_City_District_Town = (Spinner) findViewById(R.id.spin_City_District_Town);
            spin_City_District_Town.setOnItemSelectedListener(this);
            spin_State = (Spinner) findViewById(R.id.spin_State);
            spin_State.setOnItemSelectedListener(this);
            spin_City_District_Town.setVisibility(View.GONE);
            spin_State.setVisibility(View.GONE);
            String s="http://responsive.tohafaa.com/"+GlobalVariable.picture;
            imgloader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
               DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                       .cacheOnDisc(true).resetViewBeforeLoading(true)
                       .build();
            imgloader.displayImage(s, uploadimage, options);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                try {
                    showFileChooser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_signup:
                firstname = edtx_firstname.getText().toString().trim();
                lastname = edtx_lastname.getText().toString().trim();
                email = edtx_email.getText().toString().trim();
                mobile = edtx_mobile.getText().toString().trim();
                password = edtx_password.getText().toString().trim();
                cnfpassword = edtx_cnfpassword.getText().toString().trim();
                pincode = edtx_pincode.getText().toString().trim();
                addressline2 = edtx_addressline2.getText().toString().trim();
                addressline1 = edtx_addressline1.getText().toString().trim();
                addressline2 = edtx_addressline2.getText().toString().trim();
                try {
                    signUp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.link_login:
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                break;
        }
    }
    private void signUp() throws Exception{
        boolean isValid = true;
        if (firstname.isEmpty()) {
            firstinputlayout.setError("Please fill out this field");
            isValid = false;
        } else {
            firstinputlayout.setErrorEnabled(false);
        }
        if (lastname.isEmpty()) {
            lastinputlayout.setError("Please fill out this field");
            isValid = false;
        } else {
            lastinputlayout.setErrorEnabled(false);
        }
        if (email.isEmpty()) {
            emailinputlayout.setError("Please fill out this field");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailinputlayout.setError("email should be valid");
            isValid = false;
        }else if (emailerror.equalsIgnoreCase("1")) {
            emailinputlayout.setError("User AlReady Registered");
            isValid = false;
        }
        else {
            emailinputlayout.setErrorEnabled(false);
        }
        if (mobile.isEmpty()) {
            mobileinputlayout.setError("Please fill out this field");
            isValid = false;
        } else if (mobile.length() < 10) {
            mobileinputlayout.setError("mobile should be valid");
            isValid = false;
        }else if (mobileerror.equalsIgnoreCase("1")) {
            mobileinputlayout.setError("Mobile Number AlReady Registered");
            isValid = false;
        }
        else {
            mobileinputlayout.setErrorEnabled(false);
        }
        if (UpdateProfile == 0) { //for only registration
            if (password.isEmpty()) {
                passwordinputlayout.setError("Please fill out this field");
                isValid = false;
            } else if (!isValidPassword(password)) {
                passwordinputlayout.setError("password should be valid (for e.g Example@123)");
                isValid = false;
            } else {
                passwordinputlayout.setErrorEnabled(false);
            }
            if (cnfpassword.isEmpty()) {
                cnfpasswordinputlayout.setError("Please fill out this field");
                isValid = false;
            } else if (!isValidPassword(cnfpassword)) {
                cnfpasswordinputlayout.setError("confirm password should be valid (for e.g Example@123)");
                isValid = false;
            } else {
                cnfpasswordinputlayout.setErrorEnabled(false);
            }
            if (addressline1.isEmpty()) {
                address1inputlayout.setError("Please fill out this field");
                isValid = false;
            } else {
                address1inputlayout.setErrorEnabled(false);
            }
        }
        if (UpdateProfile == 1) {
            if (pincode.isEmpty()) {
                pincodeinputlayout.setError("Please fill out this field");
                isValid = false;
            } else if(pincode.length() < 6)
            {
                pincodeinputlayout.setError("pincode should be valid");
                isValid = false;
            }
            else {
                pincodeinputlayout.setErrorEnabled(false);
            }
            if (addressline1.isEmpty()) {
                address1inputlayout.setError("Please fill out this field");
                isValid = false;
            } else {
                address1inputlayout.setErrorEnabled(false);
            }
        }
        if (addressline2.isEmpty()) {
            address2inputlayout.setError("Please fill out this field");
            isValid = false;
        } else {
            address2inputlayout.setErrorEnabled(false);
        }
        if (isValid) {
            if (UpdateProfile == 0) {
                if (password.equalsIgnoreCase(cnfpassword)) {
                    new RegisterUser().execute();
                } else {
                    Toast.makeText(getBaseContext(), "passwords not matching.please try again", Toast.LENGTH_SHORT).show();
                }
            } else if (UpdateProfile == 1) {
                if (spin_country.getSelectedItem().toString().trim().equals("Select_Country")) {
                    Toast.makeText(this, "Please Select Country", Toast.LENGTH_SHORT).show();
                } else if (spin_State.getSelectedItem().toString().trim().equals("Select_State")) {
                    Toast.makeText(this, "Please Select State", Toast.LENGTH_SHORT).show();
                } else if (spin_City_District_Town.getSelectedItem().toString().trim().equals("Select_City")) {
                    Toast.makeText(this, "Please Select City", Toast.LENGTH_SHORT).show();
                } else {
                    if(filePath!=null)
                    {
                        uploadMultipart();
                    }
                    if(imageName.equalsIgnoreCase(""))
                    {
                        imageName= "profile.jpg";
                    }
                    new UpdateUser().execute();
                }
            }
        }
    }
    public void uploadMultipart() throws Exception  {
        String  imgpath = getPath(filePath);
        File f = new File(imgpath);
        imageName = f.getName();
        try {
            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(this, uploadId, URLs.UPLOAD_profile)
                    .addFileToUpload(imgpath, "image")
                    .addParameter("user_id",GlobalVariable.user_id)//Adding file
                    .addParameter("name",imageName) //Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) { }
    }
    private void showFileChooser() throws Exception  {//method to show file chooser
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override   //handling the image chooser activity result
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadimage.setImageBitmap(bitmap);
            } catch (Exception e) {e.printStackTrace();}
        }
    }
    public String getPath(Uri uri) throws Exception  {    //method to get the file path from uri
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
    //Requesting permission
    private void requestStoragePermission() throws Exception {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    void clear() {
        edtx_firstname.setText("");
        edtx_lastname.setText("");
        edtx_email.setText("");
        edtx_mobile.setText("");
        edtx_password.setText("");
        edtx_cnfpassword.setText("");
        edtx_addressline1.setText("");
        edtx_addressline2.setText("");
    }
    class RegisterUser extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("oauth_provider", "");
            params.put("oauth_uid", "");
            params.put("first_name", firstname);
            params.put("last_name", lastname);
            params.put("user_password", "");
            params.put("email", email);
            params.put("user_mobile", mobile);
            params.put("gender", "");
            params.put("user_dob", "");
            params.put("locale", "");
            params.put("picture", "");
            params.put("link", "");
            params.put("created", "");
            params.put("modified", "");
            params.put("is_deleted", "");
            params.put("profile_url", "");
            params.put("picture_url", "");
            params.put("password", password);
            params.put("forgot_password", "");
            params.put("forgot_password_reset", "");
            params.put("registration_status", "0");
            params.put("status", "0");
            params.put("address_line_1", addressline1);
            params.put("address_line_2", addressline2);
            params.put("postcode", "");
            params.put("city", "");
            params.put("state", "");
            params.put("country", "");
            params.put("user_keep_me_signed", "");
            return requestHandler.sendPostRequest(URLs.URL_REGISTER + "signup", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONArray jsonArray = null;
            try {
                if (s.contains("user_id") || s.contains("Please Verify By OTP")) {
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        GlobalVariable.user_id = jsonObject.getString("user_id");
                        GlobalVariable.first_name = jsonObject.getString("first_name");
                        GlobalVariable.last_name = jsonObject.getString("last_name");
                        GlobalVariable.mobile = jsonObject.getString("user_mobile");
                        GlobalVariable.email = jsonObject.getString("email");
                        GlobalVariable.address_line_1 = jsonObject.getString("address_line_1");
                        GlobalVariable.address_line_2 = jsonObject.getString("address_line_2");
                        GlobalVariable.registration_status = jsonObject.getString("registration_status");
                        GlobalVariable.city = jsonObject.getString("city");
                        GlobalVariable.state = jsonObject.getString("state");
                        GlobalVariable.country = jsonObject.getString("country");
                        GlobalVariable.postcode = jsonObject.getString("postcode");
                    }
                    Toast.makeText(getBaseContext(),"Thank you For Register.", Toast.LENGTH_LONG).show();
                  //  UserSessionManager.createUserLoginSession(GlobalVariable.user_id, GlobalVariable.first_name, GlobalVariable.last_name, GlobalVariable.mobile, GlobalVariable.email, GlobalVariable.address_line_1, GlobalVariable.address_line_2, GlobalVariable.registration_status, GlobalVariable.postcode, GlobalVariable.country, GlobalVariable.state, GlobalVariable.city);
                   clear();
                    OTP = gen();
                    resendotpflag = 0;
                    new InsertOTP().execute();
                } else {
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
    public static int gen() {//to generate otp random 6 digits
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 100000 + r.nextInt(100000));
    }
    void verifyotpdialog() throws Exception {    //verify otp dialog box
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_otp);
        dialog.setCancelable(false);
        final EditText edtx_otp = (EditText) dialog.findViewById(R.id.edtx_otp);
        final TextView txv_resendotp = (TextView) dialog.findViewById(R.id.txv_resendotp);
        otpinputlayout = (TextInputLayout) dialog.findViewById(R.id.otpinputlayout);
        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtx_OTP = edtx_otp.getText().toString();
                validationOTP();
            }
        });
        txv_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTP = gen();
                resendotpflag = 1;
                new InsertOTP().execute();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);// (optional) set whether to dismiss dialog when touching outside
        Window window = dialog.getWindow();
        window.setLayout(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }
    void validationOTP() {
        boolean isValid = true;
        if (edtx_OTP.isEmpty()) {
            otpinputlayout.setError("Please fill out this field.");
            isValid = false;
        } else {
            otpinputlayout.setErrorEnabled(false);
        }
        if (isValid) {
            if (edtx_OTP.equalsIgnoreCase(Integer.toString(OTP))) {
                new VerifyOTPRegistration().execute();
                dialog.dismiss();
            } else {
                Toast.makeText(getBaseContext(), "Incorrect OTP", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class UpdateUser extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", GlobalVariable.user_id);
            params.put("first_name", firstname);
            params.put("last_name", lastname);
            params.put("email", email);
            params.put("user_mobile", mobile);
            params.put("postcode", pincode);
            params.put("address_line_1", addressline1);
            params.put("address_line_2", addressline2);
            params.put("city", Integer.toString(city_id));
            params.put("state", Integer.toString(state_id));
            params.put("country", Integer.toString(country_id));
            params.put("profile", "uploads/user/"+GlobalVariable.user_id+"/"+imageName);
            return requestHandler.sendPostRequest(URLs.URL_REGISTER + "Update", params);
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
            JSONArray jsonArray = null;
            try {
                if (s.contains("user_id")) {
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        GlobalVariable.user_id = jsonObject.getString("user_id");
                        GlobalVariable.first_name = jsonObject.getString("first_name");
                        GlobalVariable.last_name = jsonObject.getString("last_name");
                        GlobalVariable.mobile = jsonObject.getString("user_mobile");
                        GlobalVariable.email = jsonObject.getString("email");
                        GlobalVariable.address_line_1 = jsonObject.getString("address_line_1");
                        GlobalVariable.address_line_2 = jsonObject.getString("address_line_2");
                        GlobalVariable.registration_status = jsonObject.getString("registration_status");
                        GlobalVariable.city = jsonObject.getString("city");
                        GlobalVariable.state = jsonObject.getString("state");
                        GlobalVariable.country = jsonObject.getString("country");
                        GlobalVariable.postcode = jsonObject.getString("postcode");
                        GlobalVariable.picture = jsonObject.getString("picture");
                        Log.d("Sortfeild", GlobalVariable.user_id + GlobalVariable.first_name);
                    }
                    UserSessionManager.createUserLoginSession(GlobalVariable.user_id, GlobalVariable.first_name, GlobalVariable.last_name, GlobalVariable.mobile, GlobalVariable.email, GlobalVariable.address_line_1, GlobalVariable.address_line_2, GlobalVariable.registration_status, GlobalVariable.postcode, GlobalVariable.country, GlobalVariable.state, GlobalVariable.city,  GlobalVariable.picture);
                    startActivity(new Intent(getApplicationContext(), DashBordActivity.class));
                    Toast.makeText(getBaseContext(),"Successfully updated profile", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
    class RetriveCountry extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "country", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            if (s.contains("No Results Found")) {
                spin_City_District_Town.setVisibility(View.GONE);
                spin_State.setVisibility(View.GONE);
            } else {
                spin_City_District_Town.setVisibility(View.GONE);
                spin_State.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    ArrayList<Item> itemList = new ArrayList<>();
                    if (GlobalVariable.country.equalsIgnoreCase("")) {
                        itemList.add(new Item(0, "Select_Country", "000000"));
                    }
                    JSONObject obj;
                    String name;
                    for (int J = 0; J < jsonArray.length(); J++) {
                        obj = jsonArray.getJSONObject(J);
                        int id = obj.getInt("id");
                        name = obj.getString("name").trim();
                        phonecode = obj.getString("phonecode").trim();
                        itemList.add(new Item(id, name, phonecode));
                    }
                    ArrayAdapter<Item> spinnerAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_item, itemList);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spin_country.setAdapter(spinnerAdapter);
                    spin_country.setSelection(getIndex(spin_country, GlobalVariable.country));
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }
    class RetriveState extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("country_id", Integer.toString(country_id));
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "states", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            if (s.toString().contains("No Results Found")) {
                Log.d("error", s);
                spin_City_District_Town.setVisibility(View.GONE);
            } else {
                spin_City_District_Town.setVisibility(View.GONE);
                spin_State.setVisibility(View.VISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    ArrayList<Item> itemList = new ArrayList<>();
                    if (GlobalVariable.state.equalsIgnoreCase("")) {
                        itemList.add(new Item(0, "Select_State"));
                    }
                    JSONObject obj;
                    String name;
                    for (int J = 0; J < jsonArray.length(); J++) {
                        obj = jsonArray.getJSONObject(J);
                        int id = obj.getInt("id");
                        name = obj.getString("name").trim();
                        itemList.add(new Item(id, name));
                    }
                    ArrayAdapter<Item> spinnerAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_item, itemList);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spin_State.setAdapter(spinnerAdapter);
                    spin_State.setSelection(getIndex(spin_State, GlobalVariable.state));
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }
    class Retrivecity extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("state_id", Integer.toString(state_id));
            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category + "cities", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            if (s.contains("No Results Found")) {
                spin_City_District_Town.setVisibility(View.GONE);
            } else {
                spin_City_District_Town.setVisibility(View.VISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    ArrayList<Item> itemList = new ArrayList<>();
                    if (GlobalVariable.city.equalsIgnoreCase("")) {
                        itemList.add(new Item(0, "Select_City"));
                    }
                    JSONObject obj;
                    String name;
                    for (int J = 0; J < jsonArray.length(); J++) {
                        obj = jsonArray.getJSONObject(J);
                        int id = obj.getInt("id");
                        name = obj.getString("name").trim();
                        itemList.add(new Item(id, name));
                    }
                    ArrayAdapter<Item> spinnerAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_item, itemList);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spin_City_District_Town.setAdapter(spinnerAdapter);
                    spin_City_District_Town.setSelection(getIndex(spin_City_District_Town, GlobalVariable.city));
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }
    class InsertOTP extends AsyncTask<Void, Void, String> { //webservice for otp
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", GlobalVariable.user_id);
            params.put("otp", Integer.toString(OTP));
            params.put("mobile", GlobalVariable.mobile);
            params.put("resendotp", Integer.toString(resendotpflag));
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations + "InsertOTP", params);
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
                jsonArray = new JSONArray(s);
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    OTP = jsonObject.getInt("otp");
                    Log.d("OTP", Integer.toString(OTP));
                }
                verifyotpdialog();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
    class VerifyOTPRegistration extends AsyncTask<Void, Void, String> {   //webservice for otp
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", GlobalVariable.user_id);
            return requestHandler.sendPostRequest(URLs.URL_REGISTER + "VerifyOTPRegistration", params);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity_OrdersList.dialog();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();//hiding the progressbar after completion
            JSONArray jsonArray = null;
            try {
                if (s.contains("user_id")) {
                    jsonArray = new JSONArray(s);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        GlobalVariable.user_id = jsonObject.getString("user_id");
                        GlobalVariable.first_name = jsonObject.getString("first_name");
                        GlobalVariable.last_name = jsonObject.getString("last_name");
                        GlobalVariable.mobile = jsonObject.getString("user_mobile");
                        GlobalVariable.email = jsonObject.getString("email");
                        GlobalVariable.address_line_1 = jsonObject.getString("address_line_1");
                        GlobalVariable.address_line_2 = jsonObject.getString("address_line_2");
                        GlobalVariable.registration_status = jsonObject.getString("registration_status");
                        GlobalVariable.city = jsonObject.getString("city");
                        GlobalVariable.state = jsonObject.getString("state");
                        GlobalVariable.country = jsonObject.getString("country");
                        GlobalVariable.postcode = jsonObject.getString("postcode");
                        GlobalVariable.picture = jsonObject.getString("picture");
                    }
                    UserSessionManager.createUserLoginSession(GlobalVariable.user_id, GlobalVariable.first_name, GlobalVariable.last_name, GlobalVariable.mobile, GlobalVariable.email, GlobalVariable.address_line_1, GlobalVariable.address_line_2, GlobalVariable.registration_status, GlobalVariable.postcode, GlobalVariable.country, GlobalVariable.state, GlobalVariable.city, GlobalVariable.picture);
                    startActivity(new Intent(getApplicationContext(), DashBordActivity.class));
                    Toast.makeText(getBaseContext(),"Successfully verified Account.",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
    class CkeckMobileEmail extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("emailmobile", emailmobile);
            return requestHandler.sendPostRequest(URLs.URL_InsertOperations + "CkeckMobileEmail", params);
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
            p.dismiss(); // mobileinputlayout,emailinputlayout
            if (s.contains("No Results Found")) {
                if(emailerror.equalsIgnoreCase("1") && emailmobileerror.equalsIgnoreCase("1") )
                {
                    emailerror="0";
                   emailinputlayout.setErrorEnabled(false);
                }
                else if(mobileerror.equalsIgnoreCase("1") && emailmobileerror.equalsIgnoreCase("2"))
                {  mobileerror="0";
                   mobileinputlayout.setErrorEnabled(false);
                }
            } else if(s.contains("already exists")) {
                if(emailerror.equalsIgnoreCase("1") && emailmobileerror.equalsIgnoreCase("1"))
                {
                    emailinputlayout.setError("User Already Registered");
                }
                 if(mobileerror.equalsIgnoreCase("1") && emailmobileerror.equalsIgnoreCase("2"))
                {
                    mobileinputlayout.setError("Mobile Number AlReady Registered");
                }
            }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spin_country) {
            Item ii = (Item) parent.getSelectedItem();
            country_id = ii.getid();
            phonecode = ii.getphonecode();
            if(Integer.toString(country_id).equalsIgnoreCase("0"))
            {
                spin_City_District_Town.setVisibility(View.GONE);
                spin_State.setVisibility(View.GONE);
            }
            else
            {  new RetriveState().execute();   }
        } else if (spinner.getId() == R.id.spin_State) {
            Item ii = (Item) parent.getSelectedItem();
            state_id = ii.getid();
            if(Integer.toString(state_id).equalsIgnoreCase("0"))
            { spin_City_District_Town.setVisibility(View.GONE); }
            else
            {  new Retrivecity().execute();            }
        } else if (spinner.getId() == R.id.spin_City_District_Town) {
            Item ii = (Item) parent.getSelectedItem();
            city_id = ii.getid();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {   }
    public static int getIndex(Spinner spinner, String myString)//set spinner values
    {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
}

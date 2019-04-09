//package com.montek.tohafaa;
//
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.FrameLayout;
//import android.widget.Toast;
//
//import com.montek.tohafaa.JsonConnection.RequestHandler;
//import com.montek.tohafaa.extra.GlobalVariable;
//import com.montek.tohafaa.extra.URLs;
//import com.montek.tohafaa.extra.UserSessionManager;
//import com.montek.tohafaa.model.Item;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//
//
//public class ActivitySplash extends AppCompatActivity implements Animation.AnimationListener {
//    public static int UpdateProfile=0;
//    Thread myThread;
//    String currentAppVersion;
//    public static UserSessionManager session;
//    public static ProgressDialog p,p1;
//    Animation animFadeIn;
//    FrameLayout linearLayout;
//    public static int flagsqliteretrivedata=0;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//         getSupportActionBar().hide();
//        setContentView(R.layout.activity_splash);
//        GlobalVariable.Category.clear();
//        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.animation_fade_in);
//        // set animation listener
//        animFadeIn.setAnimationListener(this);
//        // animation for image
//        linearLayout = (FrameLayout) findViewById(R.id.spalsh);
//        // start the animation
//        linearLayout.setVisibility(View.VISIBLE);
//        linearLayout.startAnimation(animFadeIn);
//
//        p=new ProgressDialog(ActivitySplash.this);
//        session = new UserSessionManager(getApplicationContext());
//        // get user data from session
//        HashMap<String, String> user = session.getUserDetails();
//        // get name
//        GlobalVariable.user_id = user.get(UserSessionManager.KEY_Userid);
//        GlobalVariable.first_name = user.get(UserSessionManager.key_first_name);
//        GlobalVariable.last_name = user.get(UserSessionManager.key_last_name);
//        GlobalVariable.mobile = user.get(UserSessionManager.key_mobile);
//        GlobalVariable.email = user.get(UserSessionManager.key_email);
//        GlobalVariable.address_line_1 = user.get(UserSessionManager.key_add1);
//        GlobalVariable.address_line_2 = user.get(UserSessionManager.key_add2);
//        GlobalVariable.registration_status = user.get(UserSessionManager.key_registration_status);
//        GlobalVariable.country = user.get(UserSessionManager.key_country);
//        GlobalVariable.state = user.get(UserSessionManager.key_state);
//        GlobalVariable.city = user.get(UserSessionManager.key_city);
//      //  Toast.makeText(getBaseContext(), GlobalVariable.registration_status, Toast.LENGTH_SHORT).show();
//
//
//
////        //version code
////        try {
////            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
////            int versionCode = packageInfo.versionCode;
////            currentAppVersion = packageInfo.versionName;
////        } catch (PackageManager.NameNotFoundException e) {
////            e.printStackTrace();
////        }
//    }
//
//
//
//
//    @Override
//    public void onAnimationStart(Animation animation) {
//        //under Implementation
//    }
//
//    public void onAnimationEnd(Animation animation) {
//        // Start Main Screen
//        startActivity();
//    }
//
//    @Override
//    public void onAnimationRepeat(Animation animation) {
//        //under Implementation
//    }
//
//
//
//      private void startActivity() {
//            myThread = new Thread(){
//                @Override
//                public void run() {
//                    try {
//                        sleep(2);
//                        new Retrive_Category().execute();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            myThread.start();
//        }
//
//    public  class Retrive_Category extends AsyncTask<Void, Void, String> {
//        ProgressDialog loading;
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            //creating request handler object
//            RequestHandler requestHandler = new RequestHandler();
//
//            //creating request parameters
//            HashMap<String, String> params = new HashMap<>();
//            //  params.put("email", email);
//
//           // Log.d("url",URLs.Url_Retrive_Category+"category");
//            return requestHandler.sendPostRequest(URLs.Url_Retrive_Category+"category", params);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //displaying the progress bar while user registers on the server
////            p.setMessage("Loading.....");
////            p.setIndeterminate(false);
////            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////            p.setCancelable(false);
////            p.show();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.d("Json response:",s.toString());
//            Log.d("id",s.toString());
//            //converting response to json objec
//            if(s.contains("No Results Found")) {
//                Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
//            }
//            else {
//                JSONArray jsonArray = null;
//                try {
//
//                    jsonArray = new JSONArray(s);
//                    String categoryid = null, categoryname = null;
//                    Item item;
//                    JSONObject jsonObject;
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        jsonObject = jsonArray.getJSONObject(i);
//                        categoryid = jsonObject.getString("category_id");
//                        categoryname = jsonObject.getString("category_name");
//                        item = new Item(categoryname+" Gifting", Integer.parseInt(categoryid), "category","0");
//                        GlobalVariable.Category.add(item);
//                    }
//                  //  p.dismiss();
//                    if (GlobalVariable.user_id != null && !GlobalVariable.user_id.isEmpty() && !GlobalVariable.user_id.equals("null") && !GlobalVariable.registration_status.equals("0"))
//                    {
//                        Intent i = new Intent(ActivitySplash.this, DashBordActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//                    else
//                    {
//                            Intent i = new Intent(ActivitySplash.this, LoginActivity.class);
//                            startActivity(i);
//                            finish();
//                    }
//
//                    //     Toast.makeText(getBaseContext(),GlobalVariable.user_id, Toast.LENGTH_SHORT).show();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    e.getMessage();
//                    //Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//
//                }
//            }
//        }
//
//    }
//
//
////    class RetriveVersion extends AsyncTask<Void, Void, String> {
////
////        private static final String TAG ="errormsg" ;
////
////
////        @Override
////        protected String doInBackground(Void... voids) {
////            //creating request handler object
////            RequestHandler requestHandler = new RequestHandler();
////
////            //creating request parameters
////            HashMap<String, String> params = new HashMap<>();
////            //returing the response
////            return requestHandler.sendPostRequest(URLs.URL_RetriveVersion, params);
////        }
////
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////            //displaying the progress bar while user registers on the server
////            //  progressBar.setVisibility(View.VISIBLE);
////        }
////
////        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
////        @Override
////        protected void onPostExecute(String s) {
////            super.onPostExecute(s);
////            Log.d("Locatio",s);
////            String latestversion = null;
////            String googleplaystorelink=null;
////                try {
////                    JSONArray jArray = new JSONArray(s);
////                    for(int i=0; i < jArray.length(); i++) {
////
////                        JSONObject jObject = jArray.getJSONObject(i);
////                        String tbl_versionid = jObject.getString("tbl_versionid");
////                        latestversion = jObject.getString("latestversion");
////                        googleplaystorelink  = jObject.getString("googleplaystorelink");
////
////                    } // End Loop
////                   // Log.d("version",getCurrentAppVersion()+""+latestversion);
////                    if(latestversion.equalsIgnoreCase(currentAppVersion))
////                    {
////                        startActivity();
////                    }
////                    else
////                    {
////                        progressBar.setVisibility(View.GONE);
////                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySplash.this);
////                        builder.setCancelable(false);
////                        builder.setTitle("Monsite Update");
////                        builder.setMessage("A new version is available.\nPlease visit play store to update.");
////                        final String finalGoogleplaystorelink = googleplaystorelink;
////                        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int id) {
////
////                                Intent version = new Intent(Intent.ACTION_VIEW);
////                                version.setData(Uri.parse( finalGoogleplaystorelink));
////                                startActivity(version);
////                            }
////                        });
////                        builder.create();
////                        builder.show();
////                    }
////
////                } catch (Exception e) {
////                    Log.e("JSONException", "Error: " + e.toString());
////                }
////        }
////    }
//}

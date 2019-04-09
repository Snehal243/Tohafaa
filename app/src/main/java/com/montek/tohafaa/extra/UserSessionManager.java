package com.montek.tohafaa.extra;
import android.content.*;
import android.content.SharedPreferences.Editor;
import com.montek.tohafaa.LoginActivity;
import java.util.*;
public class UserSessionManager {
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
   public static Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    public static final String KEY_Userid = "user_id";
    // Email address (make variable public to access from outside)
    public static final String key_first_name = "first_name";
    public static final String key_last_name = "last_name";
    public static final String key_mobile = "mobile";
    public static final String key_email = "email";
    public static final String key_add1 = "address_line_1";
    public static final String key_add2 = "address_line_2";
    public static final String key_registration_status = "registration_status";
    public static final String key_country = "country";
    public static final String key_state = "state";
    public static final String key_city = "city";
    public static final String key_postcode = "postcode";
    public static final String key_picture = "picture";
    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences("AndroidExamplePref", PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }
    //Create login session
    public static void createUserLoginSession(String user_id,String first_name,String last_name,String mobile,String email,String address_line_1,String address_line_2,String registration_status,String postcode,String county,String state,String city,String picture){
        // Storing login value as TRUE
        editor.putBoolean("IsUserLoggedIn", true);
        // Storing name in pref
        editor.putString(KEY_Userid, user_id);
        editor.putString(key_first_name, first_name);
        editor.putString(key_last_name, last_name);
        editor.putString(key_mobile, mobile);
        editor.putString(key_email, email);
        editor.putString(key_add1, address_line_1);
        editor.putString(key_add2, address_line_2);
        editor.putString(key_registration_status, registration_status);
        editor.putString(key_postcode, postcode);
        editor.putString(key_city, city);
        editor.putString(key_state, state);
        editor.putString(key_country, county);
        editor.putString(key_picture, picture);
      //  Log.d("hiiiiiiiiiiiiiii",user_id);
        // Storing email in pref
        //editor.putString(KEY_EMAIL, email);
        // commit changes
        editor.apply();
        editor.commit();
    }

    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails(){
        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_Userid, pref.getString(KEY_Userid, null));
        user.put(key_first_name, pref.getString(key_first_name, null));
        user.put(key_last_name, pref.getString(key_last_name, null));
        user.put(key_mobile, pref.getString(key_mobile, null));
        user.put(key_email, pref.getString(key_email, null));
        user.put(key_add1, pref.getString(key_add1, null));
        user.put(key_add2, pref.getString(key_add2, null));
        user.put(key_registration_status, pref.getString(key_registration_status, null));
        user.put(key_postcode, pref.getString(key_postcode, null));
        user.put(key_country, pref.getString(key_country, null));
        user.put(key_state, pref.getString(key_state, null));
        user.put(key_city, pref.getString(key_city, null));
        user.put(key_picture, pref.getString(key_picture, null));
        return user; // return user
    }
    public void logoutUser(){// Clearing all user data from Shared Preferences
        editor.clear();
        editor.apply();
        editor.commit();
        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
    }
    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean("IS_USER_LOGIN", false);
    }
}
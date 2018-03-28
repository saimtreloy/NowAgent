package saim.com.nowagent.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by saim on 3/1/17.
 */

public class SharedPrefDatabase {

    public static final String VENDOR_ID = "VENDOR_ID";
    public static final String VENDOR_NAME = "USER_ID";
    public static final String VENDOR_ICON = "USER_NAME";
    public static final String VENDOR_LOCATION = "USER_EMAIL";
    public static final String VENDOR_MOBILE = "USER_MOBILE";
    public static final String VENDOR_LOCATION_TAG = "VENDOR_LOCATION_TAG";
    public static final String VENDOR_USERNAME = "USER_PASS";
    public static final String VENDOR_PASSWORD = "USER_IMAGE";
    public static final String VENDOR_TOKEN = "USER_SHOP_VENDOR_TOKEN";


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    public SharedPrefDatabase(Context ctx) {
        this.context = ctx;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sharedPreferences.edit();
    }

    public void StoreVendorID(String data){
        editor.putString(VENDOR_ID, data);
        editor.commit();
    }
    public String RetriveVendorID(){
        String text = sharedPreferences.getString(VENDOR_ID, null);
        return text;
    }

    public void StoreVendorName(String data){
        editor.putString(VENDOR_NAME, data);
        editor.commit();
    }
    public String RetriveVendorName(){
        String text = sharedPreferences.getString(VENDOR_NAME, null);
        return text;
    }


    public void StoreVendroIcon(String data){
        editor.putString(VENDOR_ICON, data);
        editor.commit();
    }
    public String RetriveVendorIcon(){
        String text = sharedPreferences.getString(VENDOR_ICON, null);
        return text;
    }


    public void StoreVendorLocation(String data){
        editor.putString(VENDOR_LOCATION, data);
        editor.commit();
    }
    public String RetriveVendorLocation(){
        String text = sharedPreferences.getString(VENDOR_LOCATION, null);
        return text;
    }

    public void StoreVendorMobile(String data){
        editor.putString(VENDOR_MOBILE, data);
        editor.commit();
    }
    public String RetriveVendorMobile(){
        String text = sharedPreferences.getString(VENDOR_MOBILE, null);
        return text;
    }


    public void StoreVendorLocationTag(String data){
        editor.putString(VENDOR_LOCATION_TAG, data);
        editor.commit();
    }
    public String RetriveVendorLocationTag(){
        String text = sharedPreferences.getString(VENDOR_LOCATION_TAG, null);
        return text;
    }


    public void StoreVendorUsername(String data){
        editor.putString(VENDOR_USERNAME, data);
        editor.commit();
    }
    public String RetriveVendorUsername(){
        String text = sharedPreferences.getString(VENDOR_USERNAME, null);
        return text;
    }

    public void StoreVendorPassword(String data){
        editor.putString(VENDOR_PASSWORD, data);
        editor.commit();
    }
    public String RetriveVendorPassword(){
        String text = sharedPreferences.getString(VENDOR_PASSWORD, null);
        return text;
    }


    public void StoreVendorToken(String data){
        editor.putString(VENDOR_TOKEN, data);
        editor.commit();
    }
    public String RetriveVendorToken(){
        String text = sharedPreferences.getString(VENDOR_TOKEN, null);
        return text;
    }
}

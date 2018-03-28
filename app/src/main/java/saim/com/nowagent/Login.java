package saim.com.nowagent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import saim.com.nowagent.Utilities.ApiURL;
import saim.com.nowagent.Utilities.MySingleton;
import saim.com.nowagent.Utilities.SharedPrefDatabase;

public class Login extends AppCompatActivity {

    ProgressDialog progressDialog;
    LinearLayout layoutLogin;
    ProgressBar progressBar;
    TextInputEditText inputEmailLogin, inputPasswordLogin;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (CheckUserInfo()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            init();
        }
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        layoutLogin = (LinearLayout) findViewById(R.id.layoutLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputEmailLogin = (TextInputEditText) findViewById(R.id.inputEmailLogin);
        inputPasswordLogin = (TextInputEditText) findViewById(R.id.inputPasswordLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogin(inputEmailLogin.getText().toString(), inputPasswordLogin.getText().toString(), new SharedPrefDatabase(getApplicationContext()).RetriveVendorToken());
            }
        });


        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                String token = FirebaseInstanceId.getInstance().getToken();
                while(token == null) {
                    token = FirebaseInstanceId.getInstance().getToken();
                }
                Log.d("TOKEN ID", FirebaseInstanceId.getInstance().getToken());
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                new SharedPrefDatabase(getApplicationContext()).StoreVendorToken(FirebaseInstanceId.getInstance().getToken());
                progressBar.setVisibility(View.GONE);
                layoutLogin.setVisibility(View.VISIBLE);
            }
        }.execute();
    }


    public void UserLogin(final String vendor_username, final String vendor_password, final String vendor_token){
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiURL.Login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("success")){

                                JSONArray jsonArrayUser = jsonObject.getJSONArray("user");
                                JSONObject jsonObjectUser = jsonArrayUser.getJSONObject(0);

                                String id = jsonObjectUser.getString("id");
                                String service_shop_vendor_id = jsonObjectUser.getString("service_shop_vendor_id");
                                String service_shop_vendor_name = jsonObjectUser.getString("service_shop_vendor_name");
                                String service_shop_vendor_icon = jsonObjectUser.getString("service_shop_vendor_icon");
                                String service_shop_vendor_location = jsonObjectUser.getString("service_shop_vendor_location");
                                String service_shop_vendor_mobile = jsonObjectUser.getString("service_shop_vendor_mobile");
                                String vendor_search_tag = jsonObjectUser.getString("vendor_search_tag");
                                String vendor_username = jsonObjectUser.getString("vendor_username");
                                String vendor_password = jsonObjectUser.getString("vendor_password");
                                String vendor_token = jsonObjectUser.getString("vendor_token");

                                StoreUserData(service_shop_vendor_id, service_shop_vendor_name, service_shop_vendor_icon, service_shop_vendor_location, service_shop_vendor_mobile, vendor_search_tag, vendor_username, vendor_password, vendor_token);

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Log.d("HDHD 1", e.toString() + "\n" + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("vendor_username", vendor_username);
                params.put("vendor_password", vendor_password);
                params.put("vendor_token", vendor_token);

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public void StoreUserData(String VENDOR_ID, String VENDOR_NAME, String VENDOR_ICON,
                              String VENDOR_LOCATION, String VENDOR_MOBILE, String VENDOR_LOCATION_TAG,
                              String VENDOR_USERNAME, String VENDOR_PASSWORD, String VENDOR_TOKEN){

        new SharedPrefDatabase(getApplicationContext()).StoreVendorID(VENDOR_ID);
        new SharedPrefDatabase(getApplicationContext()).StoreVendorName(VENDOR_NAME);
        new SharedPrefDatabase(getApplicationContext()).StoreVendroIcon(VENDOR_ICON);
        new SharedPrefDatabase(getApplicationContext()).StoreVendorLocation(VENDOR_LOCATION);
        new SharedPrefDatabase(getApplicationContext()).StoreVendorMobile(VENDOR_MOBILE);
        new SharedPrefDatabase(getApplicationContext()).StoreVendorLocationTag(VENDOR_LOCATION_TAG);
        new SharedPrefDatabase(getApplicationContext()).StoreVendorUsername(VENDOR_USERNAME);
        new SharedPrefDatabase(getApplicationContext()).StoreVendorPassword(VENDOR_PASSWORD);
        new SharedPrefDatabase(getApplicationContext()).StoreVendorToken(VENDOR_TOKEN);

    }

    public boolean CheckUserInfo(){
        String name, pass, token;
        name = new SharedPrefDatabase(getApplicationContext()).RetriveVendorUsername();
        pass = new SharedPrefDatabase(getApplicationContext()).RetriveVendorPassword();
        token = new SharedPrefDatabase(getApplicationContext()).RetriveVendorToken();
        Log.d("SAIM SAIM SSS", name + " " + pass + " " + token);
        if (name==null || pass==null || token==null || name.isEmpty() || pass.isEmpty() || token.isEmpty() ){
            return false;
        } else {
            return true;
        }
    }
}

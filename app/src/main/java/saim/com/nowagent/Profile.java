package saim.com.nowagent;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import saim.com.nowagent.Utilities.ApiURL;
import saim.com.nowagent.Utilities.MySingleton;
import saim.com.nowagent.Utilities.SharedPrefDatabase;

public class Profile extends AppCompatActivity {

    public static Toolbar toolbar;
    ProgressDialog progressDialog;

    ImageView imgProfileLogo;
    TextInputEditText inputProfileName, inputProfileMobile;
    Spinner spinnerLocation;

    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        init();
    }


    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        imgProfileLogo = (ImageView) findViewById(R.id.imgProfileLogo);
        inputProfileName = (TextInputEditText) findViewById(R.id.inputProfileName);
        inputProfileMobile = (TextInputEditText) findViewById(R.id.inputProfileMobile);
        spinnerLocation = (Spinner) findViewById(R.id.spinnerLocation);

        LocationList();

    }


    public void LocationList(){
        final ArrayList<String> catList = new ArrayList<>();
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiURL.Location,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("success")){
                                JSONArray jsonArrayServiceList = jsonObject.getJSONArray("service_list");
                                for (int i=0; i<jsonArrayServiceList.length(); i++){
                                    JSONObject jsonObjectServiceList = jsonArrayServiceList.getJSONObject(i);

                                    String id = jsonObjectServiceList.getString("id");
                                    String location_name = jsonObjectServiceList.getString("location_name");
                                    String location_lat = jsonObjectServiceList.getString("location_lat");
                                    String location_lon = jsonObjectServiceList.getString("location_lon");

                                    catList.add(location_name);
                                }

                                populateSpinnerLocation(catList);
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
        });
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public void populateSpinnerLocation(ArrayList<String> locationList) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, locationList);
        spinnerLocation.setAdapter(arrayAdapter);

        for (int i=0; i<locationList.size(); i++){
            if (locationList.get(i).equals(new SharedPrefDatabase(getApplicationContext()).RetriveVendorLocation())){
                final int finalI = i;
                spinnerLocation.post(new Runnable() {
                    @Override
                    public void run() {
                        spinnerLocation.setSelection(finalI);
                    }
                });
                break;
            }
        }


        inputProfileName.setText(new SharedPrefDatabase(getApplicationContext()).RetriveVendorName());
        inputProfileMobile.setText(new SharedPrefDatabase(getApplicationContext()).RetriveVendorMobile());

        Log.d("SAIM LOGO", new SharedPrefDatabase(getApplicationContext()).RetriveVendorIcon());
        Log.d("SAIM LOCATION", new SharedPrefDatabase(getApplicationContext()).RetriveVendorLocation());

        Picasso.with(getApplicationContext())
                .load(new SharedPrefDatabase(getApplicationContext()).RetriveVendorIcon())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap1, Picasso.LoadedFrom from) {
                        bitmap = bitmap1;
                        imgProfileLogo.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {}
                });

        /*btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = spinnerLocation.getSelectedItem().toString();
                new SharedPrefDatabase(getApplicationContext()).StoreUserLocation(s);
                layoutLocation.setVisibility(View.GONE);
                recyclerViewServiceList.setVisibility(View.VISIBLE);
                ServiceList();
            }
        });*/
    }

}

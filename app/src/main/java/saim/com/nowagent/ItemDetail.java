package saim.com.nowagent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saim.com.nowagent.Adapter.AdapterSpinner;
import saim.com.nowagent.Model.ModelCategory;
import saim.com.nowagent.Utilities.ApiURL;
import saim.com.nowagent.Utilities.MySingleton;
import saim.com.nowagent.Utilities.SharedPrefDatabase;

public class ItemDetail extends AppCompatActivity {

    public static Toolbar toolbar;
    ProgressDialog progressDialog;

    ImageView imgItem;
    Bitmap bitmap = null;
    Spinner spinnerLocation;
    EditText input_p_name, input_p_detail, input_p_price, input_p_price_d, input_p_quantity;
    CheckBox checkOffer, checkPopular, checkCollection;
    Button btnAddItem;

    String item_offer = "";
    String item_popular = "";
    String item_collection = "";

    String item_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        item_id = getIntent().getExtras().getString("ITEM_ID");

        init();
    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Item Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        imgItem = (ImageView) findViewById(R.id.imgItem);
        spinnerLocation = (Spinner) findViewById(R.id.spinnerLocation);
        input_p_name = (EditText) findViewById(R.id.input_p_name);
        input_p_detail = (EditText) findViewById(R.id.input_p_detail);
        input_p_price = (EditText) findViewById(R.id.input_p_price);
        input_p_price_d = (EditText) findViewById(R.id.input_p_price_d);
        input_p_quantity = (EditText) findViewById(R.id.input_p_quantity);
        checkOffer = (CheckBox) findViewById(R.id.checkOffer);
        checkPopular = (CheckBox) findViewById(R.id.checkPopular);
        checkCollection = (CheckBox) findViewById(R.id.checkCollection);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);

        GetItem();

        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity().setAspectRatio(1,1).getIntent(ItemDetail.this);
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        checkOffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    item_offer = "shop_0001";
                }
            }
        });

        checkPopular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    item_popular = "shop_0002";
                }
            }
        });

        checkCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    item_collection = "shop_0003";
                }
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void GetItem(){
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiURL.GetItem,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("success")){
                                JSONArray jsonArrayItem = jsonObject.getJSONArray("service_list");
                                JSONObject jsonObjectItem = jsonArrayItem.getJSONObject(0);

                                String id = jsonObjectItem.getString("id");
                                String item_id = jsonObjectItem.getString("item_id");
                                String service_shop_ic_id = jsonObjectItem.getString("service_shop_ic_id");
                                String item_name = jsonObjectItem.getString("item_name");
                                String item_detail = jsonObjectItem.getString("item_detail");
                                String item_price = jsonObjectItem.getString("item_price");
                                String item_d_price = jsonObjectItem.getString("item_d_price");
                                String item_quantity = jsonObjectItem.getString("item_quantity");
                                String item_icon = jsonObjectItem.getString("item_icon");
                                String item_vendor = jsonObjectItem.getString("item_vendor");
                                String item_offer = jsonObjectItem.getString("item_offer");
                                String item_popular = jsonObjectItem.getString("item_popular");
                                String item_collection = jsonObjectItem.getString("item_collection");
                                String item_status = jsonObjectItem.getString("item_status");

                                PopulateView(service_shop_ic_id, item_name, item_detail, item_price, item_d_price, item_quantity, item_icon, item_offer, item_popular, item_collection);
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
                params.put("item_vendor", new SharedPrefDatabase(getApplicationContext()).RetriveVendorID());
                params.put("item_id", item_id);

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void CategoryList(final String cat){
        final List<ModelCategory> catList = new ArrayList<>();
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiURL.Category_List,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("success")){
                                JSONArray jsonArrayServiceList = jsonObject.getJSONArray("user");
                                for (int i=0; i<jsonArrayServiceList.length(); i++){
                                    JSONObject jsonObjectServiceList = jsonArrayServiceList.getJSONObject(i);

                                    String id = jsonObjectServiceList.getString("id");
                                    String service_shop_ic_id = jsonObjectServiceList.getString("service_shop_ic_id");
                                    String service_shop_ic_name = jsonObjectServiceList.getString("service_shop_ic_name");
                                    String service_shop_ic_icon = jsonObjectServiceList.getString("service_shop_ic_icon");
                                    String service_shop_ic_color = jsonObjectServiceList.getString("service_shop_ic_color");
                                    String service_shop_id = jsonObjectServiceList.getString("service_shop_id");

                                    ModelCategory modelCategory = new ModelCategory(id, service_shop_ic_id, service_shop_ic_name, service_shop_ic_icon, service_shop_ic_color, service_shop_id);
                                    catList.add(modelCategory);
                                }

                                AdapterSpinner sAdapter = new AdapterSpinner(getApplicationContext(), R.layout.spinner_item, catList);
                                spinnerLocation.setAdapter(sAdapter);

                                for (int i=0; i<catList.size(); i++){
                                    if (catList.get(i).getService_shop_ic_id().equals(cat)){
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
                params.put("service_shop_id", new SharedPrefDatabase(getApplicationContext()).RetriveVendorID());

                return params;
            }
        };;
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void PopulateView(String cat, String name, String detail, String price, String price_d, String quantity, String icon, String offer, String popular, String collection) {

        Picasso.with(getApplicationContext())
                .load(icon)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(imgItem);

        input_p_name.setText(name);
        input_p_detail.setText(detail);
        input_p_price.setText(price);
        input_p_price_d.setText(price_d);
        input_p_quantity.setText(quantity);

        if (!TextUtils.isEmpty(offer)) {
            checkOffer.post(new Runnable() {
                @Override
                public void run() {
                    checkOffer.setChecked(true);
                }
            });
        }

        if (!TextUtils.isEmpty(popular)) {
            checkPopular.post(new Runnable() {
                @Override
                public void run() {
                    checkPopular.setChecked(true);
                }
            });
        }

        if (!TextUtils.isEmpty(collection)) {
            checkCollection.post(new Runnable() {
                @Override
                public void run() {
                    checkCollection.setChecked(true);
                }
            });
        }

        CategoryList(cat);

    }
}

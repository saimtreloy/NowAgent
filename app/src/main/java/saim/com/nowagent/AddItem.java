package saim.com.nowagent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iceteck.silicompressorr.SiliCompressor;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saim.com.nowagent.Adapter.AdapterSpinner;
import saim.com.nowagent.Model.ModelCategory;
import saim.com.nowagent.Utilities.ApiURL;
import saim.com.nowagent.Utilities.MySingleton;
import saim.com.nowagent.Utilities.SharedPrefDatabase;

public class AddItem extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        haveStoragePermission();
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Item");
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

        CategoryList();

        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity().setAspectRatio(1,1).getIntent(AddItem.this);
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

                if (bitmap == null) {
                    Snackbar.make(v, "Please capture image first", Snackbar.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(input_p_name.getText().toString()) ||
                        TextUtils.isEmpty(input_p_name.getText().toString()) ||
                        TextUtils.isEmpty(input_p_detail.getText().toString()) ||
                        TextUtils.isEmpty(input_p_price.getText().toString()) ||
                        TextUtils.isEmpty(input_p_price_d.getText().toString()) ||
                        TextUtils.isEmpty(input_p_quantity.getText().toString()) ) {
                    Snackbar.make(v, "Input field can not be empty!!!", Snackbar.LENGTH_SHORT).show();
                } else {


                    TextView textView = (TextView) spinnerLocation.getSelectedView();

                    String service_shop_ic_id = textView.getTag().toString();
                    String item_name = input_p_name.getText().toString();
                    String item_detail = input_p_detail.getText().toString();
                    String item_price = input_p_price.getText().toString();
                    String item_d_price = input_p_price_d.getText().toString();
                    String item_quantity = input_p_quantity.getText().toString();
                    String item_icon = getStringImage(bitmap);
                    String item_vendor = new SharedPrefDatabase(getApplicationContext()).RetriveVendorID();

                    /*Log.d("SAIM", service_shop_ic_id + "\n" + item_name + "\n" + item_detail + "\n" + item_price + "\n" + item_d_price
                            + "\n" + item_quantity + "\n" + item_vendor + "\n" + item_offer + "\n" + item_popular + "\n" + item_collection + "\n" + item_icon);*/

                    AddItem(service_shop_ic_id, item_name, item_detail, item_price, item_d_price, item_quantity, item_icon, item_vendor, item_offer, item_popular, item_collection);

                }

            }
        });
    }


    public void AddItem(final String service_shop_ic_id, final String item_name, final String item_detail, final String item_price, final String item_d_price, final String item_quantity, final String item_icon, final String item_vendor,
                        final String item_offer, final String item_popular, final String item_collection){
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiURL.AddItem,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("success")){
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                params.put("service_shop_ic_id", service_shop_ic_id);
                params.put("item_name", item_name);
                params.put("item_detail", item_detail);
                params.put("item_price", item_price);
                params.put("item_d_price", item_d_price);
                params.put("item_quantity", item_quantity);
                params.put("item_icon", item_icon);
                params.put("item_vendor", item_vendor);
                params.put("item_offer", item_offer);
                params.put("item_popular", item_popular);
                params.put("item_collection", item_collection);

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public void CategoryList(){
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), resultUri);
                    bitmap = SiliCompressor.with(getApplicationContext()).getCompressBitmap(String.valueOf(resultUri));
                    imgItem.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    getApplicationContext().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(AddItem.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

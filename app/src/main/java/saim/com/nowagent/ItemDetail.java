package saim.com.nowagent;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
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

public class ItemDetail extends AppCompatActivity {

    public static Toolbar toolbar;
    ProgressDialog progressDialog;

    ImageView imgItem;
    Bitmap bitmap = null;
    Spinner spinnerLocation;
    TextInputEditText input_p_name, input_p_detail, input_p_price, input_p_price_d, input_p_quantity;
    CheckBox checkOffer, checkPopular, checkCollection;
    Button btnAddItem;

    String id, item_id, service_shop_ic_id, item_name, item_detail, item_price, item_d_price, item_quantity, item_icon, item_vendor, item_offer, item_popular, item_collection, item_status;

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
        input_p_name = (TextInputEditText) findViewById(R.id.input_p_name);
        input_p_detail = (TextInputEditText) findViewById(R.id.input_p_detail);
        input_p_price = (TextInputEditText) findViewById(R.id.input_p_price);
        input_p_price_d = (TextInputEditText) findViewById(R.id.input_p_price_d);
        input_p_quantity = (TextInputEditText) findViewById(R.id.input_p_quantity);
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

                    UpdateItem(item_id, service_shop_ic_id, item_name, item_detail, item_price, item_d_price, item_quantity, item_icon, item_vendor, item_offer, item_popular, item_collection);
                }
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

                                id = jsonObjectItem.getString("id");
                                item_id = jsonObjectItem.getString("item_id");
                                service_shop_ic_id = jsonObjectItem.getString("service_shop_ic_id");
                                item_name = jsonObjectItem.getString("item_name");
                                item_detail = jsonObjectItem.getString("item_detail");
                                item_price = jsonObjectItem.getString("item_price");
                                item_d_price = jsonObjectItem.getString("item_d_price");
                                item_quantity = jsonObjectItem.getString("item_quantity");
                                item_icon = jsonObjectItem.getString("item_icon");
                                item_vendor = jsonObjectItem.getString("item_vendor");
                                item_offer = jsonObjectItem.getString("item_offer");
                                item_popular = jsonObjectItem.getString("item_popular");
                                item_collection = jsonObjectItem.getString("item_collection");
                                item_status = jsonObjectItem.getString("item_status");

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

    public void DeleteItem(final String item_id){
        final List<ModelCategory> catList = new ArrayList<>();
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiURL.DeleteItem,
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
                params.put("item_id", item_id);

                return params;
            }
        };;
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void PopulateView(String cat, String name, String detail, String price, String price_d, String quantity, String icon, String offer, String popular, String collection) {

        Picasso.with(getApplicationContext())
                .load(icon)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap1, Picasso.LoadedFrom from) {
                        bitmap = bitmap1;
                        imgItem.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {}
                });

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

    public void UpdateItem(final String item_id1, final String service_shop_ic_id1, final String item_name1, final String item_detail1, final String item_price1, final String item_d_price1, final String item_quantity1, final String item_icon1, final String item_vendor1,
                           final String item_offer1, final String item_popular1, final String item_collection1){
        final List<ModelCategory> catList = new ArrayList<>();
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiURL.UpdateItem,
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
                params.put("item_id", item_id1);
                params.put("service_shop_ic_id", service_shop_ic_id1);
                params.put("item_name", item_name1);
                params.put("item_detail", item_detail1);
                params.put("item_price", item_price1);
                params.put("item_d_price", item_d_price1);
                params.put("item_quantity", item_quantity1);
                params.put("item_icon", item_icon1);
                params.put("item_vendor", item_vendor1);
                params.put("item_offer", item_offer1);
                params.put("item_popular", item_popular1);
                params.put("item_collection", item_collection1);

                return params;
            }
        };;
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_delete_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnOptionDelete:
                AlertDeleteItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AlertDeleteItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this item?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        DeleteItem(item_id);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}

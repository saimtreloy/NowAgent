package saim.com.nowagent;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import saim.com.nowagent.Utilities.ApiURL;
import saim.com.nowagent.Utilities.MySingleton;

public class OrderDetail extends AppCompatActivity {
    public static Toolbar toolbar;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    TextView txtOrderDetail, txtOrderName, txtOrderPhone, txtOrderLocation, txtOrderTime, txtOrderStatus;
    Button btnProcessOrder;
    ImageView imgOrderView;

    String id, order_user_id, order_user_name, order_user_phone, order_vendor_id, order_user_location, order_time,
            order_detail, order_total_price, order_service_chrge, order_type, order_status, order_user_message, order_vendor_message, order_bill_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
    }


    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        txtOrderDetail = (TextView) findViewById(R.id.txtOrderDetail);
        txtOrderName = (TextView) findViewById(R.id.txtOrderName);
        txtOrderPhone = (TextView) findViewById(R.id.txtOrderPhone);
        txtOrderLocation = (TextView) findViewById(R.id.txtOrderLocation);
        txtOrderTime = (TextView) findViewById(R.id.txtOrderTime);
        txtOrderStatus = (TextView) findViewById(R.id.txtOrderStatus);

        btnProcessOrder = (Button) findViewById(R.id.btnProcessOrder);
        imgOrderView = (ImageView) findViewById(R.id.imgOrderView);


        id = getIntent().getExtras().getString("id");
        order_user_id = getIntent().getExtras().getString("order_user_id");
        order_user_name = getIntent().getExtras().getString("order_user_name");
        order_user_phone = getIntent().getExtras().getString("order_user_phone");
        order_vendor_id = getIntent().getExtras().getString("order_vendor_id");
        order_user_location = getIntent().getExtras().getString("order_user_location");
        order_time = getIntent().getExtras().getString("order_time");
        order_detail = getIntent().getExtras().getString("order_detail");
        order_total_price = getIntent().getExtras().getString("order_total_price");
        order_service_chrge = getIntent().getExtras().getString("order_service_chrge");
        order_type = getIntent().getExtras().getString("order_type");
        order_status = getIntent().getExtras().getString("order_status");
        order_user_message = getIntent().getExtras().getString("order_user_message");
        order_vendor_message = getIntent().getExtras().getString("order_vendor_message");
        order_bill_number = getIntent().getExtras().getString("order_bill_number");

        PopulateView();
        Log.d("SAIM SAIM", order_detail);
    }


    public void PopulateView() {
        if (order_type.equals("Image")) {
            txtOrderDetail.setVisibility(View.GONE);
            imgOrderView.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext())
                    .load(order_detail)
                    .placeholder(android.R.drawable.gallery_thumb)
                    .error(android.R.drawable.gallery_thumb)
                    .into(imgOrderView);
        } else if (order_type.equals("Text")) {
            txtOrderDetail.setVisibility(View.VISIBLE);
            imgOrderView.setVisibility(View.GONE);
            txtOrderDetail.setText(order_detail);
        }
        txtOrderName.setText("Name : " + order_user_name);
        txtOrderPhone.setText("Phone : " +order_user_phone);
        txtOrderLocation.setText("Location : " +order_user_location);
        txtOrderTime.setText("Time : " +order_time);
        txtOrderStatus.setText("Status : " +order_status);

        if (order_status.equals("Proccessing") || order_status.equals("Success") || order_status.equals("Cancel")) {
            btnProcessOrder.setVisibility(View.GONE);
        } else {
            btnProcessOrder.setVisibility(View.VISIBLE);
        }

        btnProcessOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlaceOrderDialog(id);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showPlaceOrderDialog(final String id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_shop_placeorder, null);
        dialogBuilder.setView(dialogView);

        final EditText txtDPrice = (EditText) dialogView.findViewById(R.id.txtDPrice);
        final EditText txtDCharge = (EditText) dialogView.findViewById(R.id.txtDCharge);
        final EditText txtDBillNo = (EditText) dialogView.findViewById(R.id.txtDBillNo);
        final EditText txtDMessage = (EditText) dialogView.findViewById(R.id.txtDMessage);

        dialogBuilder.setTitle("Place your order");
        dialogBuilder.setMessage("Please provide your information");
        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            PlaceOrderFinal(id, txtDPrice.getText().toString(), txtDCharge.getText().toString(), txtDBillNo.getText().toString(), txtDMessage.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.show();
    }


    public void PlaceOrderFinal(final String id, final String order_total_price, final String order_service_chrge, final String order_bill_number, final String order_vendor_message){

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiURL.OrderUpdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("success")){
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                finish();
                                progressDialog.dismiss();
                            }else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
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
                params.put("id", id);
                params.put("order_total_price", order_total_price);
                params.put("order_service_chrge", order_service_chrge);
                params.put("order_bill_number", order_bill_number);
                params.put("order_vendor_message", order_vendor_message);

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}

package saim.com.nowagent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import saim.com.nowagent.Adapter.AdapterShopOrderList;
import saim.com.nowagent.Model.ModelShopOrder;
import saim.com.nowagent.Utilities.ApiURL;
import saim.com.nowagent.Utilities.MySingleton;
import saim.com.nowagent.Utilities.SharedPrefDatabase;

public class OrderList extends AppCompatActivity {

    public static Toolbar toolbar;
    ProgressBar progressBar;

    ArrayList<ModelShopOrder> orderListArrayList = new ArrayList<>();
    RecyclerView recyclerViewOrderList;
    RecyclerView.LayoutManager layoutManagerOrderList;
    RecyclerView.Adapter orderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        init();
    }


    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerViewOrderList = (RecyclerView) findViewById(R.id.recyclerViewOrderList);
        layoutManagerOrderList = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewOrderList.setLayoutManager(layoutManagerOrderList);
        recyclerViewOrderList.setHasFixedSize(true);
        Log.d("SAO<<<<<<<", new SharedPrefDatabase(getApplicationContext()).RetriveVendorID());
        OrderList(new SharedPrefDatabase(getApplicationContext()).RetriveVendorID());
    }



    public void OrderList(final String order_vendor_id){
        progressBar.setVisibility(View.VISIBLE);
        orderListArrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiURL.OrderList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("success")){

                                JSONArray jsonArrayUser = jsonObject.getJSONArray("user");

                                for (int i=0; i<jsonArrayUser.length(); i++){
                                    JSONObject jsonObjectUser = jsonArrayUser.getJSONObject(i);

                                    String id = jsonObjectUser.getString("id");
                                    String order_user_id = jsonObjectUser.getString("order_user_id");
                                    String order_user_name = jsonObjectUser.getString("order_user_name");
                                    String order_user_phone = jsonObjectUser.getString("order_user_phone");
                                    String order_vendor_id = jsonObjectUser.getString("order_vendor_id");
                                    String order_user_location = jsonObjectUser.getString("order_user_location");
                                    String order_time = jsonObjectUser.getString("order_time");
                                    String order_detail = jsonObjectUser.getString("order_detail");
                                    String order_total_price = jsonObjectUser.getString("order_total_price");
                                    String order_service_chrge = jsonObjectUser.getString("order_service_chrge");
                                    String order_type = jsonObjectUser.getString("order_type");
                                    String order_status = jsonObjectUser.getString("order_status");
                                    String order_user_message = jsonObjectUser.getString("order_user_message");
                                    String order_vendor_message = jsonObjectUser.getString("order_vendor_message");
                                    String order_bill_number = jsonObjectUser.getString("order_bill_number");

                                    Log.d("SAIM SSSS", order_user_name);

                                    ModelShopOrder modelShopOrder = new ModelShopOrder(id, order_user_id, order_user_name, order_user_phone, order_vendor_id, order_user_location, order_time, order_detail,
                                            order_total_price, order_service_chrge, order_type, order_status, order_user_message, order_vendor_message, order_bill_number);
                                    orderListArrayList.add(modelShopOrder);
                                }

                                orderListAdapter = new AdapterShopOrderList(orderListArrayList);
                                recyclerViewOrderList.setAdapter(orderListAdapter);

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
                params.put("order_vendor_id", order_vendor_id);

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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
}

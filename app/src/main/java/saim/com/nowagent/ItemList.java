package saim.com.nowagent;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

import saim.com.nowagent.Adapter.AdapterShopItemList;
import saim.com.nowagent.Model.ModelItem;
import saim.com.nowagent.Utilities.ApiURL;
import saim.com.nowagent.Utilities.MySingleton;
import saim.com.nowagent.Utilities.SharedPrefDatabase;

public class ItemList extends AppCompatActivity {

    public static Toolbar toolbar;
    public SearchView searchView;
    ProgressBar progressBar;

    ArrayList<ModelItem> itemListArrayList = new ArrayList<>();
    RecyclerView recyclerViewItemList;
    RecyclerView.LayoutManager layoutManagerItemList;
    RecyclerView.Adapter itemListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        init();
    }


    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerViewItemList = (RecyclerView) findViewById(R.id.recyclerViewItemList);
        layoutManagerItemList = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewItemList.setLayoutManager(layoutManagerItemList);
        recyclerViewItemList.setHasFixedSize(true);

        ItemList(new SharedPrefDatabase(getApplicationContext()).RetriveVendorID());

    }

    public void ItemList(final String service_shop_id){
        progressBar.setVisibility(View.VISIBLE);
        itemListArrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiURL.ItemList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if (code.equals("success")){

                                JSONArray jsonArrayUser = jsonObject.getJSONArray("service_list");

                                for (int i=0; i<jsonArrayUser.length(); i++){
                                    JSONObject jsonObjectUser = jsonArrayUser.getJSONObject(i);

                                    String id = jsonObjectUser.getString("id");
                                    String item_id = jsonObjectUser.getString("item_id");
                                    String item_name = jsonObjectUser.getString("item_name");
                                    String item_price = jsonObjectUser.getString("item_price");
                                    String item_d_price = jsonObjectUser.getString("item_d_price");
                                    String item_quantity = jsonObjectUser.getString("item_quantity");
                                    String item_icon = jsonObjectUser.getString("item_icon");
                                    String item_vendor = jsonObjectUser.getString("item_vendor");
                                    String item_vendor_icon = jsonObjectUser.getString("item_vendor_icon");

                                    ModelItem modelItem = new ModelItem(id, item_id, item_name, item_price, item_d_price, item_quantity, item_icon, item_vendor, item_vendor_icon);
                                    itemListArrayList.add(modelItem);
                                }

                                itemListAdapter = new AdapterShopItemList(itemListArrayList);
                                recyclerViewItemList.setAdapter(itemListAdapter);

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
                params.put("service_shop_id", service_shop_id);

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_add_item, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.btnOptionSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                filter(query);
                return false;
            }
        });
        return true;
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


    void filter(String text){
        ArrayList<ModelItem> temp = new ArrayList();
        for(ModelItem d: itemListArrayList){
            if(d.getItem_name().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        itemListAdapter = new AdapterShopItemList(temp);
        recyclerViewItemList.setAdapter(itemListAdapter);
        itemListAdapter.notifyDataSetChanged();
    }
}

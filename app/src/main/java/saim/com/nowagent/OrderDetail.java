package saim.com.nowagent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OrderDetail extends AppCompatActivity {
    public static Toolbar toolbar;
    ProgressBar progressBar;

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
        Log.d("SAIM SAIM", "http://www.globalearnmoney.com/now_api/" + order_detail);
    }


    public void PopulateView() {
        if (order_type.equals("Image")) {
            txtOrderDetail.setVisibility(View.GONE);
            imgOrderView.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext())
                    .load("http://www.globalearnmoney.com/now_api/" + order_detail)
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

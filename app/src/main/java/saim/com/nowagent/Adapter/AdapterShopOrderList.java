package saim.com.nowagent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import saim.com.nowagent.Model.ModelShopOrder;
import saim.com.nowagent.OrderDetail;
import saim.com.nowagent.R;

/**
 * Created by Android on 8/6/2017.
 */

public class AdapterShopOrderList extends RecyclerView.Adapter<AdapterShopOrderList.ShopOrderListViewHolder>{

    ArrayList<ModelShopOrder> adapterList = new ArrayList<>();
    Context mContext;

    public static String post_id = "";

    public AdapterShopOrderList(ArrayList<ModelShopOrder> adapterList) {
        this.adapterList = adapterList;
    }

    public AdapterShopOrderList(ArrayList<ModelShopOrder> adapterList, Context mContext) {
        this.adapterList = adapterList;
        this.mContext = mContext;
    }

    @Override
    public ShopOrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shop_order_list, parent, false);
        ShopOrderListViewHolder serviceListViewHolder = new ShopOrderListViewHolder(view);
        return serviceListViewHolder;
    }


    @Override
    public void onBindViewHolder(ShopOrderListViewHolder holder, int position) {
        /*Picasso.with(holder.listVendor.getContext())
                .load(adapterList.get(position).getItem_vendor_icon())
                .placeholder(R.drawable.ic_placeholder_icon)
                .error(R.drawable.ic_placeholder_icon)
                .into(holder.listVendor);*/
        //holder.listImageView.setColorFilter(Color.parseColor(adapterList.get(position).getService_shop_color()));

        holder.listOrderName.setText(adapterList.get(position).getOrder_user_name());
        holder.listOrderMobile.setText(adapterList.get(position).getOrder_user_phone());
        holder.listOrderStatus.setText(adapterList.get(position).getOrder_status());
        if (adapterList.get(position).getOrder_status().equals("Panding")){
            holder.listOrderStatus.setTextColor(Color.parseColor("#ff0000"));
        } else if (adapterList.get(position).getOrder_status().equals("Success")) {
            holder.listOrderStatus.setTextColor(Color.parseColor("#00ff00"));
        } else if (adapterList.get(position).getOrder_status().equals("Proccessing")) {
            holder.listOrderStatus.setTextColor(Color.parseColor("#ff00ff"));
        } else if (adapterList.get(position).getOrder_status().equals("Cancel")) {
            holder.listOrderStatus.setTextColor(Color.parseColor("#ff0000"));
        }

    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class ShopOrderListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int itemQ = 0;

        TextView listOrderName, listOrderMobile, listOrderStatus;

        public ShopOrderListViewHolder(View itemView) {
            super(itemView);

            listOrderName = (TextView) itemView.findViewById(R.id.listOrderName);
            listOrderMobile = (TextView) itemView.findViewById(R.id.listOrderMobile);
            listOrderStatus = (TextView) itemView.findViewById(R.id.listOrderStatus);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), OrderDetail.class);
            intent.putExtra("id", adapterList.get(getAdapterPosition()).getId());
            intent.putExtra("order_user_id", adapterList.get(getAdapterPosition()).getOrder_user_id());
            intent.putExtra("order_user_name", adapterList.get(getAdapterPosition()).getOrder_user_name());
            intent.putExtra("order_user_phone", adapterList.get(getAdapterPosition()).getOrder_user_phone());
            intent.putExtra("order_vendor_id", adapterList.get(getAdapterPosition()).getOrder_vendor_id());
            intent.putExtra("order_user_location", adapterList.get(getAdapterPosition()).getOrder_user_location());
            intent.putExtra("order_time", adapterList.get(getAdapterPosition()).getOrder_time());
            intent.putExtra("order_detail", adapterList.get(getAdapterPosition()).getOrder_detail());
            intent.putExtra("order_total_price", adapterList.get(getAdapterPosition()).getOrder_total_price());
            intent.putExtra("order_service_chrge", adapterList.get(getAdapterPosition()).getOrder_service_chrge());
            intent.putExtra("order_type", adapterList.get(getAdapterPosition()).getOrder_type());
            intent.putExtra("order_status", adapterList.get(getAdapterPosition()).getOrder_status());
            intent.putExtra("order_user_message", adapterList.get(getAdapterPosition()).getOrder_user_message());
            intent.putExtra("order_vendor_message", adapterList.get(getAdapterPosition()).getOrder_vendor_message());
            intent.putExtra("order_bill_number", adapterList.get(getAdapterPosition()).getOrder_bill_number());
            v.getContext().startActivity(intent);

        }
    }
}

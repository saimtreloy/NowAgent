package saim.com.nowagent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import saim.com.nowagent.ItemDetail;
import saim.com.nowagent.Model.ModelItem;
import saim.com.nowagent.R;

/**
 * Created by Android on 8/6/2017.
 */

public class AdapterShopItemList extends RecyclerView.Adapter<AdapterShopItemList.ShopItemListViewHolder>{

    ArrayList<ModelItem> adapterList = new ArrayList<>();
    Context mContext;

    public static String post_id = "";

    public AdapterShopItemList(ArrayList<ModelItem> adapterList) {
        this.adapterList = adapterList;
    }

    public AdapterShopItemList(ArrayList<ModelItem> adapterList, Context mContext) {
        this.adapterList = adapterList;
        this.mContext = mContext;
    }

    @Override
    public AdapterShopItemList.ShopItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_list, parent, false);
        ShopItemListViewHolder serviceListViewHolder = new ShopItemListViewHolder(view);
        return serviceListViewHolder;
    }


    @Override
    public void onBindViewHolder(ShopItemListViewHolder holder, int position) {
        Picasso.with(holder.listImageView.getContext())
                .load(adapterList.get(position).getItem_icon())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.listImageView);
        //holder.listImageView.setColorFilter(Color.parseColor(adapterList.get(position).getService_shop_color()));

        holder.listName.setText(adapterList.get(position).getItem_name());
        holder.listQuentity.setText(adapterList.get(position).getItem_quantity());
        holder.listPrice.setText(adapterList.get(position).getItem_price() + "tk");
        holder.listPriceD.setText(adapterList.get(position).getItem_d_price() + "tk");
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class ShopItemListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int itemQ = 0;

        ImageView listImageView;
        TextView listName, listQuentity, listPrice, listPriceD;

        public ShopItemListViewHolder(View itemView) {
            super(itemView);

            listImageView = (ImageView) itemView.findViewById(R.id.listImageView);
            listName = (TextView) itemView.findViewById(R.id.listName);
            listQuentity = (TextView) itemView.findViewById(R.id.listQuentity);
            listPrice = (TextView) itemView.findViewById(R.id.listPrice);
            listPriceD = (TextView) itemView.findViewById(R.id.listPriceD);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ItemDetail.class);
            intent.putExtra("ITEM_ID", adapterList.get(getAdapterPosition()).getItem_id());
            v.getContext().startActivity(intent);
        }
    }
}

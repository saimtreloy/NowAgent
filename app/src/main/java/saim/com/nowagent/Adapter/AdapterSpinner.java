package saim.com.nowagent.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import saim.com.nowagent.Model.ModelCategory;
import saim.com.nowagent.R;

/**
 * Created by NREL on 3/21/18.
 */

public class AdapterSpinner extends ArrayAdapter<ModelCategory> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<ModelCategory> items;
    private final int mResource;

    public AdapterSpinner(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView spinnerText = (TextView) view.findViewById(R.id.spinnerText);

        spinnerText.setText(items.get(position).getService_shop_ic_name());
        spinnerText.setTag(items.get(position).getService_shop_ic_id());

        return view;
    }
}

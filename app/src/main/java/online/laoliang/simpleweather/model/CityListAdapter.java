package online.laoliang.simpleweather.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import online.laoliang.simpleweather.R;

/**
 * 已选城市列表CityList的自定义适配器
 *
 * @author liang
 */
public class CityListAdapter extends ArrayAdapter<CityList> {

    private int resourceId;

    public CityListAdapter(Context context, int textViewResourceId, List<CityList> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId; // ListView子布局Id
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityList citylist = getItem(position); // 获取当前项的CityList实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView city_name = (TextView) view.findViewById(R.id.city_name);
        // Button delete_city = (Button) view.findViewById(R.id.delete_city);
        city_name.setText(citylist.getCityName());
        return view;
    }

}

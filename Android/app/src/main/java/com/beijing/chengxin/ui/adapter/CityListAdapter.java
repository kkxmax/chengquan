package com.beijing.chengxin.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.model.CityModel;
import com.beijing.chengxin.ui.listener.OnItemClickListener;

import java.util.ArrayList;

public class CityListAdapter extends BaseAdapter implements SectionIndexer {

    private Context mContext;
    private ArrayList<CityModel> mCityList;
    private OnItemClickListener listener;
    String currentCityName;

    public CityListAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(ArrayList<CityModel> datas, String currentCityName) {
        this.mCityList = datas;
        this.currentCityName = currentCityName;
    }

    public void setCurrentCityName(String name) {
        currentCityName = name;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return (mCityList == null) ? 0 : mCityList.size();
    }

    @Override
    public Object getItem(int position) {
        return (mCityList == null) ? null : mCityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_condition_main_city_item, null);

            holder.txtIndex = (TextView) convertView.findViewById(R.id.txt_index);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setId(position);

        final CityModel item = (CityModel) getItem(position);

        holder.id = item.getId();
        holder.txtTitle.setText(item.getName());

        String pre_index = null, index = null;
        if (position == 0) {
            pre_index = null;
            index = item.getCityAlias();
        } else {
            pre_index = ((CityModel) getItem(position - 1)).getCityAlias();
            index = item.getCityAlias();
        }
        if (pre_index != null && pre_index.length() > 0) {
            pre_index = pre_index.substring(0, 1);
        }
        if (index != null && index.length() > 0) {
            index = index.substring(0, 1);
        }
        holder.txtIndex.setText(index == null ? "" : index.toUpperCase());
        if (!index.equals(pre_index)) {
            holder.txtIndex.setVisibility(View.VISIBLE);
        } else {
            holder.txtIndex.setVisibility(View.GONE);
        }

        if (item.getName().equals(currentCityName)) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_blue_white));
            holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.color_main_blue));
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_white));
            holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.txt_dark));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCityName = item.getName();
                if (listener != null)
                    listener.onListItemClick(position, v);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        CityModel city;
        int size = getCount();
        for (int i = 0; i < size; i++) {
            city = (CityModel) getItem(i);
            String name = city.getCityAlias();
            char firstChar = name.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    private static class ViewHolder {
        public int id;
        public TextView txtIndex;
        public TextView txtTitle;
    }

}
package com.beijing.chengxin.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.model.XyleixingModel;

import java.util.ArrayList;
import java.util.List;

public class MeHangyeListAdapter extends BaseAdapter {

    private List<XyleixingModel> mXyList;
    private int mSelectedIndex = -1;
    private int mCurrentDataId = -1;
    private String mCurrentDataTitle = null;

    public void setDatas(List<XyleixingModel> datas, int currentId) {
        this.mXyList = datas;
        this.mCurrentDataId = currentId;

        for (int i = 0; i < datas.size(); i++) {
            XyleixingModel tmp = datas.get(i);
            for (int j = 0; j < tmp.getList().size(); j++) {
                XyleixingModel tmp2 = tmp.getList().get(j);
                if (tmp2.getId() == currentId) {
                    mSelectedIndex = i;
                    break;
                }
            }
            if (mSelectedIndex != -1)
                break;
        }

        notifyDataSetChanged();
    }

    public int getCurrentDataId() {
        return mCurrentDataId;
    }

    public String getCurrentDataTitle() {
        return mCurrentDataTitle;
    }

    @Override
    public int getCount() {
        return (mXyList == null) ? 0 : mXyList.size();
    }

    @Override
    public Object getItem(int position) {
        return (mXyList == null) ? null : mXyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_condition_hangye_item, null);

            holder.viewTitle = (LinearLayout) convertView.findViewById(R.id.view_title);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            holder.imgArrow = (ImageView) convertView.findViewById(R.id.img_arrow);
            holder.viewItem = (LinearLayout) convertView.findViewById(R.id.view_item);
            holder.gridView = (GridView) convertView.findViewById(R.id.grid_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setId(position);

        XyleixingModel item = (XyleixingModel) getItem(position);

        holder.id = item.getId();
        holder.txtTitle.setText(item.getTitle());

        HangyeItemListAdapter adapter = new HangyeItemListAdapter();
        holder.gridView.setAdapter(adapter);
        adapter.setDatas(item.getList());
        adapter.notifyDataSetChanged();

        if (position == mSelectedIndex) {
            holder.viewItem.setVisibility(View.VISIBLE);
            holder.imgArrow.setImageResource(R.drawable.retract);
        } else {
            holder.viewItem.setVisibility(View.GONE);
            holder.imgArrow.setImageResource(R.drawable.unfold);
        }

        holder.viewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedIndex == position)
                    mSelectedIndex = -1;
                else
                    mSelectedIndex = position;
                MeHangyeListAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public int id;
        public LinearLayout viewTitle;
        public TextView txtTitle;
        public ImageView imgArrow;
        public LinearLayout viewItem;
        public GridView gridView;
    }

    public class HangyeItemListAdapter extends BaseAdapter {

        private List<XyleixingModel> mHangyeItemList;

        public void setDatas(List<XyleixingModel> datas) {
            this.mHangyeItemList = datas;
        }

        @Override
        public int getCount() {
            return (mHangyeItemList == null) ? 0 : mHangyeItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return (mHangyeItemList == null) ? null : mHangyeItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGridHolder holder = new ViewGridHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_condition_hangye_griditem, null);

                holder.txtTitle = (ToggleButton) convertView.findViewById(R.id.txt_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewGridHolder) convertView.getTag();
            }

            convertView.setId(position);

            final XyleixingModel item = (XyleixingModel) getItem(position);

            holder.id = item.getId();
            holder.txtTitle.setText(item.getTitle());
            holder.txtTitle.setTextOn(item.getTitle());
            holder.txtTitle.setTextOff(item.getTitle());

            if (mCurrentDataId == item.getId())
                holder.txtTitle.setChecked(true);
            else
                holder.txtTitle.setChecked(false);

            final ViewGridHolder finalHolder = holder;
            holder.txtTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mCurrentDataId = item.getId();
                        mCurrentDataTitle = item.getTitle();
                    } else {
                        finalHolder.txtTitle.setChecked(true);
                    }
                    MeHangyeListAdapter.this.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    private static class ViewGridHolder {
        public int id;
        public ToggleButton txtTitle;
    }

}
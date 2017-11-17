package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.ItemModel;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.ItemDetailFragment;
import com.beijing.chengxin.ui.widget.GridView;
import com.beijing.chengxin.ui.widget.ListViewNoScroll;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TabItemView extends BaseView {

    LinearLayout viewBodyPart, viewBlankPart;
    ListViewNoScroll listView;

    private List<ItemModel> mDatas;
    private ListAdapter mAdapter;

    public TabItemView(Context context) {
        super(context);
        initialize();
    }

    public void setData(List<ItemModel> datas) {
        this.mDatas = datas;
        initData();
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.view_tab_item);

        viewBodyPart = (LinearLayout) findViewById(R.id.view_body_part);
        viewBlankPart = (LinearLayout) findViewById(R.id.view_blank_part);
        listView = (ListViewNoScroll) findViewById(R.id.list_view);

        mAdapter = new ListAdapter();
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (mDatas != null && mDatas.size() > 0) {
            viewBodyPart.setVisibility(View.VISIBLE);
            viewBlankPart.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        } else {
            viewBodyPart.setVisibility(View.GONE);
            viewBlankPart.setVisibility(View.VISIBLE);
        }
    }

    public class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return (mDatas == null) ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return (mDatas == null) ? null : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGridHolder holder = new ViewGridHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_serve, null);

                holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
                holder.txtType = (TextView) convertView.findViewById(R.id.txt_type);
                holder.txtDescript = (TextView) convertView.findViewById(R.id.txt_descript);
                convertView.setTag(holder);
            } else {
                holder = (ViewGridHolder) convertView.getTag();
            }
            convertView.setId(position);

            final ItemModel item = mDatas.get(position);

            Picasso.with(mActivity)
                    .load(Constants.FILE_ADDR + item.getLogo())
                    .placeholder(R.drawable.no_image)
                    .into(holder.imgAvatar);

            holder.txtName.setText(item.getName());
            holder.txtType.setText(item.getFenleiName());
            holder.txtDescript.setText(item.getComment());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setItem(item);
                    ((BaseFragmentActivity)mActivity).showFragment(fragment, true);
                }
            });

            return convertView;
        }
    }

    private static class ViewGridHolder {
        public int id;
        public ImageView imgAvatar;
        public TextView txtName;
        public TextView txtType;
        public TextView txtDescript;
    }

}

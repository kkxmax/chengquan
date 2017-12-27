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
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.ComedityModel;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.ComedityDetailFragment;
import com.beijing.chengxin.ui.widget.GridView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TabComidityView extends BaseView {

    LinearLayout viewBodyPart, viewBlankPart;
    GridView gridView;

    private List<ComedityModel> mDatas;
    private ListAdapter mAdapter;

    public TabComidityView(Context context) {
        super(context);
        initialize();
    }

    public void setData(List<ComedityModel> datas) {
        this.mDatas = datas;
        initData();
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.view_tab_comedity);

        viewBodyPart = (LinearLayout) findViewById(R.id.view_body_part);
        viewBlankPart = (LinearLayout) findViewById(R.id.view_blank_part);
        gridView = (GridView) findViewById(R.id.grid_view);

        mAdapter = new ListAdapter();
        gridView.setAdapter(mAdapter);
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comedity, null);

                holder.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
                holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
                holder.txtPrice = (TextView) convertView.findViewById(R.id.txt_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewGridHolder) convertView.getTag();
            }
            convertView.setId(position);

            final ComedityModel item = mDatas.get(position);

            holder.id = item.getId();
            String imgLogoPath = null;
            if (item.getImgPaths() != null && item.getImgPaths().size() > 0)
                imgLogoPath = item.getImgPaths().get(0);
            Picasso.with(mActivity)
                    .load(Constants.FILE_ADDR + imgLogoPath)
                    .placeholder(R.drawable.no_image)
                    .into(holder.imgPhoto);
            holder.txtName.setText(item.getName());
            holder.txtPrice.setText(String.format("¥%.02f", item.getPrice()));

            holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ComedityDetailFragment fragment = new ComedityDetailFragment();
                    fragment.setId(item.getId());
                    ((BaseFragmentActivity)mActivity).showFragment(fragment, true);
                }
            });

            return convertView;
        }
    }

    private static class ViewGridHolder {
        public int id;
        public ImageView imgPhoto;
        public TextView txtName;
        public TextView txtPrice;
    }

}

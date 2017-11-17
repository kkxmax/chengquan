package com.beijing.chengxin.ui.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.view.FollowAccountListView;
import com.beijing.chengxin.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FollowAccountListAdapter extends BaseAdapter implements SectionIndexer {

    private AppConfig mAppConfig;
    private ArrayList<UserModel> mDataList;
    private Set<String> mFollowIdList;

    private int mWhoIndex = FollowAccountListView.INDEX_PERSON;
    private int mIndexSelected = -1;

    public void setDatas(int indexFlag, AppConfig appConfig, ArrayList<UserModel> datas, Set<String> idList) {
        this.mWhoIndex = indexFlag;
        this.mAppConfig = appConfig;
        this.mDataList = datas;
        this.mFollowIdList = idList;
    }

    @Override
    public int getCount() {
        return (mDataList == null) ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return (mDataList == null) ? null : mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_account, null);

            holder.viewBody = (View) convertView.findViewById(R.id.view_body);
            holder.viewPreview = (View) convertView.findViewById(R.id.view_preview);
            holder.viewMain = (View) convertView.findViewById(R.id.view_main);
            holder.txtIndex = (TextView) convertView.findViewById(R.id.txt_index);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtFollowMe = (TextView) convertView.findViewById(R.id.txt_follow_me);
            holder.txtFollowNotme = (TextView) convertView.findViewById(R.id.txt_follow_notme);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setId(position);

        final UserModel item = (UserModel) getItem(position);

        holder.id = item.getId();
        holder.txtName.setText(item.getAkind() == 1 ? item.getRealname() : item.getEnterName());

        String pre_index = null;
        String index = item.alias;
        if (position == 0) {
            pre_index = null;
        } else {
            pre_index = ((UserModel) getItem(position - 1)).alias;
        }
        if (pre_index != null && pre_index.length() > 0) {
            pre_index = pre_index.substring(0, 1);
        }
        if (index != null && index.length() > 0) {
            index = index.substring(0, 1);
        }

        String index_title = parent.getContext().getString(R.string.str_dis_person);
        if (index.equals(Constants.STR_STAR)) {
            index_title = (item.getAkind() == 1) ? parent.getContext().getString(R.string.str_dis_person) : parent.getContext().getString(R.string.str_dis_enterprise);
        } else {
            index_title = index.toUpperCase();
        }
        holder.txtIndex.setText(index == null ? "" : index_title);
        if (index != null && !index.equals(pre_index)) {
            holder.txtIndex.setVisibility(View.VISIBLE);
        } else {
            holder.txtIndex.setVisibility(View.GONE);
        }

        holder.txtFollowMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFollowIdList == null)
                    mFollowIdList = new HashSet<String>();
                if (!mFollowIdList.contains("" + item.getId())) {
                    mFollowIdList.add("" + item.getId());

                    item.alias = Constants.STR_STAR + item.alias;
                    CommonUtils.sortAccountByChinese(mDataList);
                    mIndexSelected = -1;

                    String title = FollowAccountListView.getIndexTitle(mWhoIndex);
                    mAppConfig.setStringSetValue(title, mFollowIdList);

                    notifyDataSetChanged();
                }
            }
        });

        holder.txtFollowNotme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        final ViewHolder finalHolder = holder;
        holder.viewBody.setOnTouchListener(new View.OnTouchListener() {
            float x1 = 0, x2 = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x1 = x2 = event.getX();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    x2 = event.getX();
                    if ((x1 - x2) > 200) {
                        finalHolder.viewMain.setVisibility(View.GONE);
                        mIndexSelected = position;
                    } else {
                        finalHolder.viewMain.setVisibility(View.VISIBLE);
                        mIndexSelected = -1;
                    }
                    notifyDataSetChanged();
                }

                return true;
            }
        });

        if (mIndexSelected == position) {
            if (holder.viewMain.getVisibility() == View.GONE) {
                CommonUtils.animationShowFromRight(holder.viewMain);
                holder.viewMain.setVisibility(View.VISIBLE);
            }
        } else {
                holder.viewMain.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        UserModel info;
        int size = getCount();
        for (int i = 0; i < size; i++) {
            info = (UserModel) getItem(i);
            String name = info.alias;
            if (name.length() > 0) {
                char firstChar = name.toUpperCase().charAt(0);
                if (firstChar == sectionIndex) {
                    return i;
                }
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
        public View viewBody;
        public View viewPreview;
        public View viewMain;
        public TextView txtIndex;
        public TextView txtName;
        public TextView txtFollowMe;
        public TextView txtFollowNotme;
    }

}
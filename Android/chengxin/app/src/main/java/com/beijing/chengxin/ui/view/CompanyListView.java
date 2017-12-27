package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.listener.OnCancelListener;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.IndexBar;
import com.beijing.chengxin.utils.CommonUtils;
import com.beijing.chengxin.utils.PinyinUtils;

import java.util.ArrayList;

public class CompanyListView extends BaseView {

    private Context mContext;
    private TextView mTxtTitle;
    private ListView mListView;
    private Button mBtnCancel;
    private Button mBtnOk;
    private OnCompanySelectListener selectListener;
    private OnCancelListener cancelListener;

    private ListAdapter mAdapter;
    ArrayList<UserModel> mDatas;
    String mTitle;
    int companyId;
    String currentCompanyName;
    int xyleixingId;
    String xyName;

    public interface OnCompanySelectListener {
        void onCompanySelected(int id, String name, int xyleixingId, String xyName);
    }

    public CompanyListView(Context context, String title, OnCompanySelectListener listener, OnCancelListener cancelListener) {
        super(context);
        mContext = context;
        mTitle = title;
        selectListener = listener;
        this.cancelListener = cancelListener;
        initialize();
    }

    public void setCurrentCompanyName(String name) {
        currentCompanyName = name;
        if (mAdapter != null)
            mAdapter.setCurrentCompanyName(name);
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_condition_main_city);

        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnOk = (Button) findViewById(R.id.btn_ok);

        mAdapter = new ListAdapter(mContext);
        mAdapter.setOnItemClickListener(mListener);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        IndexBar indexbar = (IndexBar) findViewById(R.id.indexbar);
        indexbar.setWidgets(mListView, mAdapter, (TextView) findViewById(R.id.txt_popup));

        mBtnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectListener != null)
                    selectListener.onCompanySelected(companyId, currentCompanyName, xyleixingId, xyName);
                CompanyListView.this.setVisibility(View.GONE);
            }
        });
        mBtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null)
                    cancelListener.onCancel();
                CompanyListView.this.setVisibility(View.GONE);
            }
        });

        if (mTitle != null)
            mTxtTitle.setText(mTitle);
    }

    OnItemClickListener mListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
            UserModel user = mDatas.get(position);
            companyId = user.getId();
            currentCompanyName = user.getEnterName();
            xyleixingId = user.getXyleixingId();
            xyName = user.getXyName();
        }
    };

    @Override
    protected void initData() {
        super.initData();
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
            }
            @Override
            protected Object doInBackground(Object... params) {
                PinyinUtils.create(mActivity, R.array.pinyin);

                mDatas = (ArrayList<UserModel>) new SyncInfo(mContext).syncCompanyList("").getList();

                if (mDatas != null) {
                    for (int i = 0; i < mDatas.size(); i++) {
                        mDatas.get(i).setAlias(PinyinUtils.convert(mDatas.get(i).getEnterName()));
                    }
                    CommonUtils.sortAccountByChinese(mDatas);
                }

                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                mAdapter.setDatas(mDatas, currentCompanyName);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public class ListAdapter extends BaseAdapter implements SectionIndexer {

        private Context mContext;
        private ArrayList<UserModel> mCompanyList;
        private OnItemClickListener listener;
        String currentCompanyName;

        public ListAdapter(Context context) {
            mContext = context;
        }

        public void setDatas(ArrayList<UserModel> datas, String currentCompanyName) {
            this.mCompanyList = datas;
            this.currentCompanyName = currentCompanyName;
        }

        public void setCurrentCompanyName(String name) {
            currentCompanyName = name;
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return (mCompanyList == null) ? 0 : mCompanyList.size();
        }

        @Override
        public Object getItem(int position) {
            return (mCompanyList == null) ? null : mCompanyList.get(position);
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

            final UserModel item = (UserModel) getItem(position);

            holder.id = item.getId();
            holder.txtTitle.setText(item.getEnterName());

            String pre_index = null, index = null;
            if (position == 0) {
                pre_index = null;
                index = item.getAlias();
            } else {
                pre_index = ((UserModel) getItem(position - 1)).getAlias();
                index = item.getAlias();
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

            if (item.getEnterName().equals(currentCompanyName)) {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_blue_white));
                holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.color_main_blue));
            } else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_white));
                holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.txt_dark));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentCompanyName = item.getEnterName();
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
            UserModel user;
            int size = getCount();
            for (int i = 0; i < size; i++) {
                user = (UserModel) getItem(i);
                String name = user.getAlias();
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

        private class ViewHolder {
            public int id;
            public TextView txtIndex;
            public TextView txtTitle;
        }

    }

}

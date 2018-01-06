package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.ComedityListModel;
import com.beijing.chengxin.network.model.ComedityModel;
import com.beijing.chengxin.network.model.XyleixingListModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.fragment.MainHomeFragment;
import com.beijing.chengxin.ui.listener.OnConditionClickListener;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.GridView;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.PAGE_ITEM_COUNT;

public class HomeComedityView extends BaseView {

    public final String TAG = HomeComedityView.class.getName();

    MainHomeFragment mParentFragment;
    GridView mGridView;
    BaseAdapter mAdapter;

    SyncInfo info;
    AppConfig appConfig;

    public List<ComedityModel> listComedity;

    public int mOrder;
    public String mCityName = "";
    public List<Integer> xyList;
    public String keyword = "";
    boolean isDataLoading = false;

    public HomeComedityView(Context context) {
        super(context);
        initialize();
    }

    public HomeComedityView(Context context, MainHomeFragment parent) {
        super(context);
        this.mParentFragment = parent;
        initialize();
    }

    @Override
    protected void initUI() {
        super.initUI();
        setContentView(R.layout.fragment_home_comedity);

        mGridView = (GridView) findViewById(R.id.grid_view);
        mAdapter = new ItemDetailAdapter(mActivity, listItemClickListener);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();

        info = new SyncInfo(mActivity);
        appConfig = new AppConfig(mActivity);

        if (listComedity == null)
            listComedity = new ArrayList<ComedityModel>();

        if (xyList == null)
            xyList = new ArrayList<Integer>();

        if (AppConfig.getInstance().pleixingList.size() == 0)
            new PleixingListAsync().execute("");

        if (listComedity.size() == 0)
            loadData();
    }

    public void loadData() {
        if (isDataLoading)
            return;
        String start = String.valueOf(listComedity.size());
        String length = String.valueOf(PAGE_ITEM_COUNT);
        String order = String.valueOf(mOrder + 1);
        String xyleixingId = "";
        for (int i = 0; i < xyList.size(); i++) {
            if (!xyleixingId.equals(""))
                xyleixingId += ",";
            xyleixingId += String.valueOf(xyList.get(i));
        }

        new ComedityListAsync().execute(start, length, order, mCityName, xyleixingId, keyword);
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        if (info != null)
            reloadData();
    }

    public void reloadData() {
        listComedity.clear();
        loadData();
    }

    public OnConditionClickListener mConditionClickListener = new OnConditionClickListener() {
        @Override
        public void onClickReset() {
        }

        @Override
        public void onClickOk(String cityName, int kind, List<Integer> typeList) {
            mCityName = cityName;
            xyList = typeList;

            reloadData();
            MainActivity.mainActivity.hideConditionView(Constants.INDEX_COMEDITY);
        }
    };

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
            Intent intent = new Intent(mActivity, DetailActivity.class);
            intent.putExtra("type", Constants.INDEX_COMEDITY);
            intent.putExtra("id", listComedity.get(position).getId());
            mActivity.startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
        }
    };

    public class ItemDetailAdapter extends BaseAdapter {
        private Context mContext;
        private OnItemClickListener itemClickListener;

        public ItemDetailAdapter(Context context, OnItemClickListener listener) {
            mContext = context;
            itemClickListener = listener;
        }

        @Override
        public int getCount() {
            return (listComedity == null) ? 0 : listComedity.size();
        }

        @Override
        public Object getItem(int position) {
            return (listComedity == null) ? null : listComedity.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comedity, null);

                holder.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
                holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                holder.txt_price = (TextView) convertView.findViewById(R.id.txt_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            convertView.setId(position);

            ComedityModel item = listComedity.get(position);

            Picasso.with(mActivity)
                    .load(Constants.FILE_ADDR + item.getImgPaths().get(0))
                    .placeholder(R.drawable.no_image)
                    .into(holder.imgPhoto);
            holder.txt_name.setText(item.getName());
            holder.txt_price.setText(String.format("¥%.02f", item.getPrice()));

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                        itemClickListener.onListItemClick(position, v);
                }
            });

            return convertView;
        }

        public class ViewHolder {
            ImageView imgPhoto;
            TextView txt_name;
            TextView txt_price;
        }
    }

    class ComedityListAsync extends AsyncTask<String, String, ComedityListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDataLoading = true;
            Utils.displayProgressDialog(mActivity);
        }

        @Override
        protected ComedityListModel doInBackground(String... strs) {
            return info.syncComedityList(strs[0], strs[1], strs[2], strs[3], strs[4], strs[5]);
        }

        @Override
        protected void onPostExecute(ComedityListModel result) {
            super.onPostExecute(result);
            if (result.isValid()) {
                if (result.getRetCode() == ERROR_OK) {
                    listComedity.addAll(result.getList());
                    mAdapter.notifyDataSetChanged();
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishAndLoginActivityFromDuplicate(mActivity);
                } else {
                    Toast.makeText(mActivity, result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mActivity, getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
            isDataLoading = false;
            Utils.disappearProgressDialog();
            mParentFragment.mPullRefreshScrollView.onRefreshComplete();
            mParentFragment.callBackDataLoad();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            isDataLoading = false;
            Utils.disappearProgressDialog();
        }
    }

    class PleixingListAsync extends AsyncTask<String, String, XyleixingListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected XyleixingListModel doInBackground(String... strs) {
            return info.syncPleixingList(strs[0]);
        }

        @Override
        protected void onPostExecute(XyleixingListModel result) {
            super.onPostExecute(result);
            if (result.isValid()) {
                if (result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().pleixingList.clear();
                    AppConfig.getInstance().pleixingList.addAll(result.getList());
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishAndLoginActivityFromDuplicate(mActivity);
                } else {
                    Toast.makeText(mActivity, result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mActivity, getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}

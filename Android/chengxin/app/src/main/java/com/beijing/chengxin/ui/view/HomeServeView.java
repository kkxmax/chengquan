package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.FenleiListModel;
import com.beijing.chengxin.network.model.ServeListModel;
import com.beijing.chengxin.network.model.ServeModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.fragment.MainHomeFragment;
import com.beijing.chengxin.ui.listener.OnConditionClickListener;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.PAGE_ITEM_COUNT;

public class HomeServeView extends BaseView {

    public final String TAG = HomeServeView.class.getName();

    MainHomeFragment mParentFragment;
    LinearLayout mListView;

    SyncInfo info;
    AppConfig appConfig;

    public List<ServeModel> listServe;
    List<View> listServeView;

    public int mOrder;
    public String mCityName = "";
    public int aKind;
    public List<Integer> xyList;
    public String keyword = "";
    boolean isDataLoading = false;

    public HomeServeView(Context context) {
        super(context);
        initialize();
    }

    public HomeServeView(Context context, MainHomeFragment parent) {
        super(context);
        this.mParentFragment = parent;
        initialize();
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.fragment_home);

        mListView = (LinearLayout)findViewById(R.id.listView);
    }

    @Override
    protected void initData() {
        super.initData();

        info = new SyncInfo(mActivity);
        appConfig = new AppConfig(mActivity);

        if (listServe == null)
            listServe = new ArrayList<ServeModel>();
        if (listServeView == null)
            listServeView = new ArrayList<View>();

        if (xyList == null)
            xyList = new ArrayList<Integer>();

        if (AppConfig.getInstance().serveFenleiList.size() == 0)
            new FenleiListAsync().execute("");

        if (listServe.size() == 0)
            loadData();
    }

    public void loadData() {
        if (isDataLoading)
            return;
        String start = String.valueOf(listServe.size());
        String length = String.valueOf(PAGE_ITEM_COUNT);
        String order = String.valueOf(mOrder + 1);
        String akindStr = String.valueOf(aKind);
        String xyleixingId = "";
        for (int i = 0; i < xyList.size(); i ++) {
            if (!xyleixingId.equals(""))
                xyleixingId += ",";
            xyleixingId += String.valueOf(xyList.get(i));
        }
        new ServeListAsync().execute(start, length, order, mCityName, akindStr, xyleixingId, keyword);
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        if (info != null)
            reloadData();
    }

    public void reloadData() {
        listServe.clear();
        listServeView.clear();
        mListView.removeAllViews();
        loadData();
    }

    public OnConditionClickListener mConditionClickListener = new OnConditionClickListener() {
        @Override
        public void onClickReset() {
        }
        @Override
        public void onClickOk(String cityName, int kind, List<Integer>typeList) {
            mCityName = cityName;
            aKind = kind;
            xyList = typeList;

            reloadData();
            MainActivity.mainActivity.hideConditionView(Constants.INDEX_SERVE);
        }
    };

    private void initListData(List<ServeModel> listData) {
        for (int i = 0; i < listData.size(); i++) {
            View itemView = getItemView(listData.get(i));

            listServe.add(listData.get(i));
            listServeView.add(itemView);

            mListView.addView(itemView);
        }
    }

    private View getItemView(final ServeModel item) {
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_item_serve, null);

        ImageView img_avatar = (ImageView) convertView.findViewById(R.id.img_avatar);
        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView txt_type = (TextView) convertView.findViewById(R.id.txt_type);
        TextView txt_descript = (TextView) convertView.findViewById(R.id.txt_descript);

        Picasso.with(mActivity)
                .load(Constants.FILE_ADDR + item.getLogo())
                .placeholder(R.drawable.no_image_item)
                .into(img_avatar);

        txt_name.setText(item.getName());
        txt_type.setText(item.getFenleiName());
        txt_descript.setText("简介：" + item.getComment());

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, DetailActivity.class);
                intent.putExtra("type", Constants.INDEX_SERVE);
                AppConfig.getInstance().currentServe= item;
                mActivity.startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
            }
        });

        return convertView;
    }

    class ServeListAsync extends AsyncTask<String, String, ServeListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDataLoading = true;
            Utils.displayProgressDialog(mActivity);
        }
        @Override
        protected ServeListModel doInBackground(String... strs) {
            return info.syncServeList(strs[0], strs[1], strs[2], strs[3], strs[4], strs[5], strs[6]);
        }
        @Override
        protected void onPostExecute(ServeListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    initListData(result.getList());
                    mListView.invalidate();
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

    class FenleiListAsync extends AsyncTask<String, String, FenleiListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected FenleiListModel doInBackground(String... strs) {
            return info.syncServeFenleiList();
        }
        @Override
        protected void onPostExecute(FenleiListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().serveFenleiList.clear();
                    AppConfig.getInstance().serveFenleiList.addAll(result.getList());
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

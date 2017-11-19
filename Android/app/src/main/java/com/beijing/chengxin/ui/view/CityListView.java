package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.network.model.CityModel;
import com.beijing.chengxin.network.model.ProvinceModel;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.adapter.CityListAdapter;
import com.beijing.chengxin.ui.listener.OnCancelListener;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.IndexBar;
import com.beijing.chengxin.utils.CommonUtils;
import com.beijing.chengxin.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.List;

public class CityListView extends BaseView {

    private Context mContext;
    private ListView mListView;
    private Button mBtnCancel;
    private Button mBtnOk;
    private OnCitySelectListener cityListener;
    private OnCancelListener cancelListener;

    private CityListAdapter mAdapter;
    ArrayList<CityModel> cityInfos;
    int cityId;
    String currentCityName;

    public interface OnCitySelectListener {
        void onCitySelected(int id, String name);
    }

    public CityListView(Context context, OnCitySelectListener listener) {
        super(context);
        mContext = context;
        cityListener = listener;
        initialize();
    }

    public CityListView(Context context, OnCitySelectListener listener, OnCancelListener cancelListener) {
        super(context);
        mContext = context;
        cityListener = listener;
        this.cancelListener = cancelListener;
        initialize();
    }

    public void setCurrentCityName(String name) {
        currentCityName = name;
        if (mAdapter != null)
            mAdapter.setCurrentCityName(name);
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_condition_main_city);

        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnOk = (Button) findViewById(R.id.btn_ok);

        mAdapter = new CityListAdapter(mContext);
        mAdapter.setOnItemClickListener(mListener);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        IndexBar indexbar = (IndexBar) findViewById(R.id.indexbar);
        indexbar.setWidgets(mListView, mAdapter, (TextView) findViewById(R.id.txt_popup));

        mBtnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityListener != null)
                    cityListener.onCitySelected(cityId, currentCityName);
                CityListView.this.setVisibility(View.GONE);
            }
        });
        mBtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null)
                    cancelListener.onCancel();
                CityListView.this.setVisibility(View.GONE);
            }
        });
    }

    OnItemClickListener mListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
            cityId = cityInfos.get(position).getId();
            currentCityName = cityInfos.get(position).getName();
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

                cityInfos = new ArrayList<CityModel>();
                List<ProvinceModel> provinceList = AppConfig.getInstance().cityList;
                for (int i = 0; i < provinceList.size(); i++) {
                    ProvinceModel province = provinceList.get(i);
                    List<CityModel> cityList = province.getCities();
                    for (int j = 0; j < cityList.size(); j++) {
                        CityModel city = cityList.get(j);
                        city.setCityAlias(PinyinUtils.convert(city.getName()));
                        cityInfos.add(city);
                    }
                }

                if (cityInfos != null) {
                    CommonUtils.sortCityByChinese(cityInfos);
                }

                return cityInfos;
            }

            @Override
            protected void onPostExecute(Object o) {
                mAdapter.setDatas(cityInfos, currentCityName);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}

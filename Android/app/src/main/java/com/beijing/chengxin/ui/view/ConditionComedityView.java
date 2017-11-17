package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.XyleixingModel;
import com.beijing.chengxin.ui.adapter.HangyeListAdapter;
import com.beijing.chengxin.ui.listener.OnConditionClickListener;

import java.util.ArrayList;
import java.util.List;

public class ConditionComedityView extends BaseView implements View.OnClickListener {

    private FrameLayout layoutBody;
    private ToggleButton txtCityAll, txtCityBeijing, txtCityShanghai, txtCityGuangzhou, txtCityShenzhen;
    private Button txtCityMore;
    private ListView mListView;
    private HangyeListAdapter mAdapter;
    private Button btnReset, btnOk;

    public CityListView mCityListView;

    private OnConditionClickListener mConditionClickListener;

    String cityName;
    List<Integer> pList;
    List<XyleixingModel> xyList;

    public ConditionComedityView(Context context) {
        super(context);
        initialize();
    }

    public void setOnConditionClickListener(OnConditionClickListener listener) {
        this.mConditionClickListener = listener;
    }

    public void setData(String cityName, int akind, List<Integer>pList) {
        this.cityName = cityName;
        this.pList = pList;

        selectCityButton(cityName);
        if (mAdapter != null)
            mAdapter.setDatas(xyList, this.pList);
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_condition_comedity);

        layoutBody = (FrameLayout) findViewById(R.id.layout_body);
        txtCityAll = (ToggleButton) findViewById(R.id.txt_city_all);
        txtCityBeijing = (ToggleButton) findViewById(R.id.txt_city_beijing);
        txtCityShanghai = (ToggleButton) findViewById(R.id.txt_city_shanghai);
        txtCityGuangzhou = (ToggleButton) findViewById(R.id.txt_city_guangzhou);
        txtCityShenzhen = (ToggleButton) findViewById(R.id.txt_city_shenzhen);
        txtCityMore = (Button) findViewById(R.id.txt_city_more);

        btnReset = (Button) findViewById(R.id.btn_reset);
        btnOk = (Button) findViewById(R.id.btn_ok);

        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new HangyeListAdapter();
        mAdapter.setDatas(AppConfig.getInstance().pleixingList, pList);
        mListView.setAdapter(mAdapter);

        txtCityAll.setOnClickListener(this);
        txtCityBeijing.setOnClickListener(this);
        txtCityShanghai.setOnClickListener(this);
        txtCityGuangzhou.setOnClickListener(this);
        txtCityShenzhen.setOnClickListener(this);
        txtCityMore.setOnClickListener(this);

        btnReset.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        cityName = "";
        pList = new ArrayList<Integer> ();
        xyList = AppConfig.getInstance().pleixingList;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txt_city_all:
                selectCityButton("");
                break;
            case R.id.txt_city_beijing:
                selectCityButton(getString(R.string.city_beijing));
                break;
            case R.id.txt_city_shanghai:
                selectCityButton(getString(R.string.city_shanghai));
                break;
            case R.id.txt_city_guangzhou:
                selectCityButton(getString(R.string.city_guangzhou));
                break;
            case R.id.txt_city_shenzhen:
                selectCityButton(getString(R.string.city_shenzhen));
                break;
            case R.id.txt_city_more:
                if (mCityListView == null) {
                    mCityListView = new CityListView(getContext(), cityListener);
                    layoutBody.addView(mCityListView);
                    mCityListView.setVisibility(View.VISIBLE);
                }
                if (mCityListView.getVisibility() == View.GONE) {
                    mCityListView.setVisibility(View.VISIBLE);
                }
                mCityListView.setCurrentCityName(cityName);
                break;
            case R.id.btn_reset:
                initData();
                selectCityButton(cityName);
                mAdapter.setDatas(xyList, pList);
                break;
            case R.id.btn_ok:
                if (mConditionClickListener != null)
                    mConditionClickListener.onClickOk(cityName, 0, pList);
                break;
        }
    }

    CityListView.OnCitySelectListener cityListener = new CityListView.OnCitySelectListener() {
        @Override
        public void onCitySelected(int id, String name) {
            cityName = name;
            selectCityButton(cityName);
        }
    };

    HangyeListView.OnHangyeSelectListener hyListener = new HangyeListView.OnHangyeSelectListener() {
        @Override
        public void OnHangyeSelected(List<Integer> list) {
            pList = list;
        }
    };

    private void resetCityButtons() {
        txtCityAll.setChecked(false);
        txtCityBeijing.setChecked(false);
        txtCityShanghai.setChecked(false);
        txtCityGuangzhou.setChecked(false);
        txtCityShenzhen.setChecked(false);
    }

    private void selectCityButton(String name) {
        cityName = name;
        resetCityButtons();

        if (name.equals(""))
            txtCityAll.setChecked(true);
        else if (name.equals(getString(R.string.city_beijing)))
            txtCityBeijing.setChecked(true);
        else if (name.equals(getString(R.string.city_shanghai)))
            txtCityShanghai.setChecked(true);
        else if (name.equals(getString(R.string.city_guangzhou)))
            txtCityGuangzhou.setChecked(true);
        else if (name.equals(getString(R.string.city_shenzhen)))
            txtCityShenzhen.setChecked(true);
    }
}

package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;

import java.util.ArrayList;
import java.util.List;

public class ConditionMainView extends BaseView implements View.OnClickListener {

    private FrameLayout layoutBody;
    private ToggleButton txtCityAll, txtCityBeijing, txtCityShanghai, txtCityGuangzhou, txtCityShenzhen;
    private Button txtCityMore;
    private ToggleButton txtKindAll, txtKindEnterprise, txtKindPerson;
    private LinearLayout layoutHangye;
    private Button btnReset, btnOk;

    public CityListView mCityListView;
    public HangyeListView mHangyeListView;

    private OnConditionClickListener mConditionClickListener;

    String cityName;
    int akind;
    List<Integer> xyList;

    public ConditionMainView(Context context) {
        super(context);
        initialize();
    }

    public void setOnConditionClickListener(OnConditionClickListener listener) {
        this.mConditionClickListener = listener;
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_condition);

        layoutBody = (FrameLayout) findViewById(R.id.layout_body);
        txtCityAll = (ToggleButton) findViewById(R.id.txt_city_all);
        txtCityBeijing = (ToggleButton) findViewById(R.id.txt_city_beijing);
        txtCityShanghai = (ToggleButton) findViewById(R.id.txt_city_shanghai);
        txtCityGuangzhou = (ToggleButton) findViewById(R.id.txt_city_guangzhou);
        txtCityShenzhen = (ToggleButton) findViewById(R.id.txt_city_shenzhen);
        txtCityMore = (Button) findViewById(R.id.txt_city_more);

        txtKindAll = (ToggleButton) findViewById(R.id.txt_kind_all);
        txtKindEnterprise = (ToggleButton) findViewById(R.id.txt_kind_enterprise);
        txtKindPerson = (ToggleButton) findViewById(R.id.txt_kind_person);

        layoutHangye = (LinearLayout) findViewById(R.id.layout_hangye);

        btnReset = (Button) findViewById(R.id.btn_reset);
        btnOk = (Button) findViewById(R.id.btn_ok);

        txtCityAll.setOnClickListener(this);
        txtCityBeijing.setOnClickListener(this);
        txtCityShanghai.setOnClickListener(this);
        txtCityGuangzhou.setOnClickListener(this);
        txtCityShenzhen.setOnClickListener(this);
        txtCityMore.setOnClickListener(this);
        txtKindAll.setOnClickListener(this);
        txtKindEnterprise.setOnClickListener(this);
        txtKindPerson.setOnClickListener(this);
        layoutHangye.setOnClickListener(this);

        btnReset.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        cityName = AppConfig.getInstance().getStringValue(Constants.SEARCH_CITYNAME, "");
        akind = AppConfig.getInstance().getIntValue(Constants.SEARCH_AKIND, 0);
        xyList = AppConfig.getInstance().getIntArray(Constants.SEARCH_XYLEIXINGID);

        selectCityButton(cityName);
    }

    @Override
    protected void initData() {
        super.initData();
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
            case R.id.txt_kind_all:
                selectLeixingButton(0);
                break;
            case R.id.txt_kind_enterprise:
                selectLeixingButton(Constants.ACCOUNT_TYPE_ENTERPRISE);
                break;
            case R.id.txt_kind_person:
                selectLeixingButton(Constants.ACCOUNT_TYPE_PERSON);
                break;
            case R.id.layout_hangye:
                if (mHangyeListView == null) {
                    mHangyeListView = new HangyeListView(getContext(), hyListener);
                    layoutBody.addView(mHangyeListView);
                    mHangyeListView.setVisibility(View.VISIBLE);
                }
                if (mHangyeListView.getVisibility() == View.GONE) {
                    mHangyeListView.setVisibility(View.VISIBLE);
                }
                mHangyeListView.setCurrentIds(xyList);
                break;
            case R.id.btn_reset:
                cityName = "";
                akind = 0;
                xyList = new ArrayList<Integer>();

                selectCityButton(cityName);
                selectLeixingButton(akind);
                break;
            case R.id.btn_ok:
                if (mConditionClickListener != null)
                    mConditionClickListener.onClickOk(0, 0, 0);
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
            xyList = list;
        }
    };

    public static interface OnConditionClickListener {
        void onClickReset();
        void onClickOk(int cityId, int kind, int hangyeId);
    }

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

    private void resetLeixingButtons(){
        txtKindAll.setChecked(false);
        txtKindEnterprise.setChecked(false);
        txtKindPerson.setChecked(false);
    }

    private void selectLeixingButton(int type) {
        resetLeixingButtons();
        akind = type;
        if (type == 0)
            txtKindAll.setChecked(true);
        else if (type == Constants.ACCOUNT_TYPE_ENTERPRISE)
            txtKindEnterprise.setChecked(true);
        else if (type == Constants.ACCOUNT_TYPE_PERSON)
            txtKindPerson.setChecked(true);
    }
}

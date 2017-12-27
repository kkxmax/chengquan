package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.FenleiModel;
import com.beijing.chengxin.ui.listener.OnConditionClickListener;

import java.util.ArrayList;
import java.util.List;

public class ConditionItemServeView extends BaseView implements View.OnClickListener {

    private FrameLayout layoutBody;
    private ToggleButton txtCityAll, txtCityBeijing, txtCityShanghai, txtCityGuangzhou, txtCityShenzhen;
    private Button txtCityMore;
    private ToggleButton txtKindAll, txtKindEnterprise, txtKindPerson;
    private TextView txtFenlei;
    private Button btnReset, btnOk;

    public CityListView mCityListView;
    private GridView gridView;
    private GridViewAdapter mAdapter;

    private OnConditionClickListener mConditionClickListener;

    String cityName;
    int akind;
    List<Integer> xyList;
    int mType; // 0: item view , 1: serve view

    public ConditionItemServeView(Context context, int type) {
        super(context);
        mType = type;
        initialize();
    }

    public void setOnConditionClickListener(OnConditionClickListener listener) {
        this.mConditionClickListener = listener;
    }

    public void setData(String cityName, int akind, List<Integer>xyList) {
        this.cityName = cityName;
        this.akind = akind;
        this.xyList = xyList;

        selectCityButton(cityName);
        selectLeixingButton(akind);
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_condition_item_serve);

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

        txtFenlei = (TextView) findViewById(R.id.txt_fenlei);
        if (mType == 1)
            txtFenlei.setText(getResources().getText(R.string.serve_kind));
        gridView = (GridView)findViewById(R.id.grid_view);
        mAdapter = new GridViewAdapter(getContext());
        gridView.setAdapter(mAdapter);

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

        btnReset.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        cityName = "";
        akind = 0;
        xyList = new ArrayList<Integer> ();
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
            case R.id.btn_reset:
                cityName = "";
                akind = 0;
                xyList.clear();

                selectCityButton(cityName);
                selectLeixingButton(akind);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_ok:
                if (mConditionClickListener != null)
                    mConditionClickListener.onClickOk(cityName, akind, xyList);
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
        @Override
        public void OnHangyeCanceled() {}
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

    public class GridViewAdapter extends BaseAdapter {

        public final String TAG = GridViewAdapter.class.getName();

        private List<FenleiModel> fenleiList;
        private Context mContext;
        private View mRootView;

        public GridViewAdapter(Context context) {
            mContext = context;
            if (mType == 0)
                fenleiList = AppConfig.getInstance().itemFenleiList;
            else
                fenleiList = AppConfig.getInstance().serveFenleiList;
        }

        @Override
        public int getCount() {
            return fenleiList.size();
        }

        @Override
        public Object getItem(int position) {
            return fenleiList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final FenleiModel item = (FenleiModel)getItem(position);
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_condition_toggle_button, parent, false);

            ToggleButton btnToggle = (ToggleButton)convertView.findViewById(R.id.btn_toggle);
            btnToggle.setTextOn(item.getTitle());
            btnToggle.setTextOff(item.getTitle());

            if (xyList.contains(item.getId()))
                btnToggle.setChecked(true);
            else
                btnToggle.setChecked(false);

            btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!xyList.contains(item.getId()))
                            xyList.add(item.getId());
                    } else
                        xyList.remove((Integer)item.getId());
                }
            });
            return convertView;
        }

    }
}

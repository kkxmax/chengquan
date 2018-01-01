package com.beijing.chengxin.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.network.model.CityModel;
import com.beijing.chengxin.network.model.ProvinceModel;
import com.beijing.chengxin.ui.widget.wheelView.OnWheelChangedListener;
import com.beijing.chengxin.ui.widget.wheelView.OnWheelScrollListener;
import com.beijing.chengxin.ui.widget.wheelView.WheelView;
import com.beijing.chengxin.ui.widget.wheelView.adapters.AbstractWheelTextAdapter;

import java.util.List;

public class SelectCityDialog extends Dialog {
    Context mContext;

    private Button btn_cancel, btn_ok;
    private WheelView countryWheel, cityWheel;

    private List<ProvinceModel> cityList;

    // Scrolling flag
    private boolean scrolling = false;

    private int mCurrentProvinceIndex = -1;
    private int mCurrentCityIndex = -1;
    private int mCurrentCityId;
    private CityModel mCurrentCity;

    private OnCitySelectListener mListener;

    public interface OnCitySelectListener {
        void OnCitySelected(CityModel city);
    }

    public SelectCityDialog(Context context) {
        super(context);
        mContext = context;
    }

    public SelectCityDialog(Context context, int id, OnCitySelectListener listener) {
        super(context);
        mContext = context;
        mCurrentCityId = id;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inf = LayoutInflater.from(mContext);
        View dlg = inf.inflate(R.layout.dialog_select_city, null);
        setContentView(dlg);

        cityList = AppConfig.getInstance().cityList;
        for (int i = 0; i < cityList.size(); i++) {
            List<CityModel> cityTmpList = cityList.get(i).getCities();
            for (int j = 0; j < cityTmpList.size(); j++) {
                CityModel tmp = cityTmpList.get(j);
                if (mCurrentCityId == tmp.getId()) {
                    mCurrentProvinceIndex = i;
                    mCurrentCityIndex = j;
                    break;
                }
                if (mCurrentProvinceIndex != -1)
                    break;
            }
        }
        if (mCurrentProvinceIndex == -1)
            mCurrentProvinceIndex = 0;
        if (mCurrentCityIndex == -1)
            mCurrentCityIndex = 0;

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        countryWheel = (WheelView) findViewById(R.id.country_wheel);
        cityWheel = (WheelView) findViewById(R.id.city_wheel);

        countryWheel.setVisibleItems(3);
        countryWheel.setViewAdapter(new CountryAdapter(getContext()));

        cityWheel.setVisibleItems(3);

        countryWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!scrolling) {
                    updateCities(cityWheel, newValue, mCurrentCityIndex);
                }
            }
        });

        countryWheel.addScrollingListener( new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }
            @Override
            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                updateCities(cityWheel, countryWheel.getCurrentItem(), 0);
            }
        });

        cityWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!scrolling) {
                    if (newValue > 0)
                        mCurrentCity = cityList.get(countryWheel.getCurrentItem()).getCities().get(newValue);
                    else
                        mCurrentCity = null;
                }
            }
        });

        countryWheel.setCurrentItem(mCurrentProvinceIndex);
        updateCities(cityWheel, mCurrentProvinceIndex, mCurrentCityIndex);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null)
                    mListener.OnCitySelected(mCurrentCity);
                SelectCityDialog.this.cancel();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelectCityDialog.this.cancel();
            }
        });
    }

    private void updateCities(WheelView city, int countryIndex, int cityIndex) {
        if (cityList.size() - 1 < countryIndex)
            return;
        CityAdapter adapter = new CityAdapter(getContext(), cityList.get(countryIndex).getCities());
        city.setViewAdapter(adapter);
        city.setCurrentItem(cityIndex);
        mCurrentCityIndex = cityIndex;
        if (cityList.get(countryIndex).getCities().size() > 0)
            mCurrentCity = cityList.get(countryIndex).getCities().get(cityIndex);
        else
            mCurrentCity = null;
    }

    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        protected CountryAdapter(Context context) {
            super(context, R.layout.item_city, NO_RESOURCE);
            setItemTextResource(R.id.txt_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return cityList.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return cityList.get(index).getName();
        }
    }

    private class CityAdapter extends AbstractWheelTextAdapter {
        private List<CityModel> items;

        protected CityAdapter(Context context, List<CityModel> items) {
            super(context, R.layout.item_city, NO_RESOURCE);
            this.items = items;

            setItemTextResource(R.id.txt_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return items.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return items.get(index).getName();
        }
    }
}
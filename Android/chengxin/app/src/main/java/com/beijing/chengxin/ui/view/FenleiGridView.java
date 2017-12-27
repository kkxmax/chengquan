package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.model.FenleiModel;
import com.beijing.chengxin.ui.listener.OnCancelListener;

import java.util.ArrayList;
import java.util.List;

public class FenleiGridView extends BaseView {

    private Context mContext;
    private GridView mGridView;
    private Button mBtnCancel;
    private Button mBtnOk;
    private OnFenleiSelectListener hlListener;
    private OnCancelListener cancelListener;

    private FenleiGridAdapter mAdapter;
    List<FenleiModel> fenleiList;

    public interface OnFenleiSelectListener {
        void OnFenleiSelected(int curId, String curTitle);
    }

    public FenleiGridView(Context context, OnFenleiSelectListener listener, OnCancelListener canListener) {
        super(context);
        mContext = context;
        this.hlListener = listener;
        this.cancelListener = canListener;

        initialize();
    }

    public void setData(List<FenleiModel> fenleiList, int currentId) {
        this.fenleiList = fenleiList;
        if (mAdapter != null)
            mAdapter.setDatas(fenleiList, currentId);
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_condition_fenlei);

        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mAdapter = new FenleiGridAdapter();
        mGridView.setAdapter(mAdapter);

        mBtnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hlListener != null)
                    hlListener.OnFenleiSelected(mAdapter.getCurrentDataId(), mAdapter.getCurrentDataTitle());
                FenleiGridView.this.setVisibility(GONE);
            }
        });

        mBtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null)
                    cancelListener.onCancel();
                FenleiGridView.this.setVisibility(GONE);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        fenleiList = new ArrayList<FenleiModel>();
        mAdapter.setDatas(fenleiList, -1);
    }

    public class FenleiGridAdapter extends BaseAdapter {

        private List<FenleiModel> mFlList;
        private int mSelectedIndex = -1;
        private int mCurrentDataId = -1;
        private String mCurrentDataTitle = null;

        public void setDatas(List<FenleiModel> datas, int currentId) {
            this.mFlList = datas;
            this.mCurrentDataId = currentId;

            notifyDataSetChanged();
        }

        public int getCurrentDataId() {
            return mCurrentDataId;
        }

        public String getCurrentDataTitle() {
            return mCurrentDataTitle;
        }

        @Override
        public int getCount() {
            return (mFlList == null) ? 0 : mFlList.size();
        }

        @Override
        public Object getItem(int position) {
            return (mFlList == null) ? null : mFlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_condition_toggle_button, null);

            final FenleiModel item = (FenleiModel) getItem(position);

            final ToggleButton button = (ToggleButton)convertView.findViewById(R.id.btn_toggle);
            button.setTextOn(item.getTitle());
            button.setTextOff(item.getTitle());

            if (mSelectedIndex == position || item.getId()==mCurrentDataId)
                button.setChecked(true);
            else
                button.setChecked(false);

            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != mSelectedIndex) {
                        mSelectedIndex = position;
                        mCurrentDataId = item.getId();
                        mCurrentDataTitle = item.getTitle();
                        notifyDataSetChanged();
                    } else
                        button.setChecked(true);
                }
            });

            return convertView;
        }
    }

}
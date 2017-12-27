package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.model.XyleixingModel;
import com.beijing.chengxin.ui.adapter.HangyeListAdapter;

import java.util.ArrayList;
import java.util.List;

public class HangyeListView extends BaseView {

    private Context mContext;
    private TextView mTxtEmpty;
    private ListView mListView;
    private Button mBtnCancel;
    private Button mBtnOk;
    private OnHangyeSelectListener hyListener;

    private HangyeListAdapter mAdapter;
    List<XyleixingModel> xyList;
    List<Integer> currentList;

    public interface OnHangyeSelectListener {
        void OnHangyeSelected(List<Integer>list);
        void OnHangyeCanceled();
    }

    public HangyeListView(Context context, OnHangyeSelectListener listener) {
        super(context);
        mContext = context;
        hyListener = listener;
        initialize();
    }

    public void setData(List<XyleixingModel> xyList, List<Integer> list) {
        this.xyList = xyList;
        currentList = list;
        if (mAdapter != null)
            mAdapter.setDatas(xyList, currentList);
    }

    public void setData(List<XyleixingModel> xyList, List<Integer> list, boolean is_reset) {
        this.xyList = xyList;
        currentList = list;
        if (mAdapter != null)
            mAdapter.setDatas(xyList, currentList);

        if (is_reset) {
            mBtnCancel.setText(R.string.reset);
            mBtnCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentList.clear();
                    mAdapter.setDatas(HangyeListView.this.xyList, currentList);
                    if (hyListener != null)
                        hyListener.OnHangyeSelected(currentList);
                    HangyeListView.this.setVisibility(GONE);
                }
            });
        }
    }

    public void setCurrentIds(List<Integer> list) {
        currentList = list;
        if (mAdapter != null)
            mAdapter.setXyList(currentList);
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_condition_hangye);

        mTxtEmpty = (TextView) findViewById(R.id.txt_empty);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new HangyeListAdapter();
        mListView.setAdapter(mAdapter);

        mTxtEmpty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentList.clear();
                mAdapter.setDatas(xyList, currentList);
            }
        });
        mBtnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hyListener != null)
                    hyListener.OnHangyeSelected(currentList);
                HangyeListView.this.setVisibility(GONE);
            }
        });

        mBtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hyListener != null)
                    hyListener.OnHangyeCanceled();
                HangyeListView.this.setVisibility(GONE);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        xyList = new ArrayList<XyleixingModel>();
        mAdapter.setDatas(xyList, currentList);
        mAdapter.notifyDataSetChanged();
    }

}
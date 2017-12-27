package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.model.XyleixingModel;
import com.beijing.chengxin.ui.adapter.MeHangyeListAdapter;
import com.beijing.chengxin.ui.listener.OnCancelListener;

import java.util.ArrayList;
import java.util.List;

public class MeHangyeListView extends BaseView {

    private Context mContext;
    private TextView mTxtTitle;
    private ListView mListView;
    private Button mBtnCancel;
    private Button mBtnOk;
    private OnHangyeSelectListener hyListener;
    private OnCancelListener cancelListener;

    private String mTitle;
    private MeHangyeListAdapter mAdapter;
    List<XyleixingModel> xyList;

    public interface OnHangyeSelectListener {
        void OnHangyeSelected(int curId, String curTitle);
    }

    public MeHangyeListView(Context context, OnHangyeSelectListener listener, OnCancelListener canListener) {
        super(context);
        mContext = context;
        hyListener = listener;
        cancelListener = canListener;
        initialize();
    }

    public void setData(String title, List<XyleixingModel> xyList, int currentId) {
        this.mTitle = title;
        this.xyList = xyList;
        if (mAdapter != null)
            mAdapter.setDatas(xyList, currentId);

        if (mTitle != null) {
            mTxtTitle.setText(mTitle);
        }
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_condition_hangye);

        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new MeHangyeListAdapter();
        mListView.setAdapter(mAdapter);
        findViewById(R.id.txt_empty).setVisibility(View.GONE);

        mBtnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hyListener != null)
                    hyListener.OnHangyeSelected(mAdapter.getCurrentDataId(), mAdapter.getCurrentDataTitle());
                MeHangyeListView.this.setVisibility(GONE);
            }
        });

        mBtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null)
                    cancelListener.onCancel();
                MeHangyeListView.this.setVisibility(GONE);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        xyList = new ArrayList<XyleixingModel>();
        mAdapter.setDatas(xyList, -1);
        mAdapter.notifyDataSetChanged();
    }

}
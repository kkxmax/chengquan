package com.beijing.chengxin.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.model.XyleixingModel;
import com.beijing.chengxin.ui.widget.GridView;

import java.util.ArrayList;

public class MeHangyeListDialog extends Dialog {
    Context mContext;

    private boolean mIsWatched = true;
    private String mTitle;
    private ArrayList<XyleixingModel> mDatas;

    private ListAdapter mAdapter;

    TextView txtTitle;
    Button btnOk;
    GridView gridView;

    public interface OnOkClickListener {
        void onOk();
    }

    private OnOkClickListener mListener;

    public MeHangyeListDialog(Context context) {
        super(context);
        mContext = context;
    }

    public MeHangyeListDialog(Context context, boolean isWatched, String title, ArrayList<XyleixingModel> datas, OnOkClickListener listener) {
        super(context);
        mContext = context;
        mIsWatched = isWatched;
        mTitle = title;
        mDatas = datas;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inf = LayoutInflater.from(mContext);
        View dlg = inf.inflate(R.layout.dialog_me_hangye_list, null);
        setContentView(dlg);

        txtTitle = (TextView) findViewById(R.id.txt_title);
        gridView = (GridView) findViewById(R.id.grid_view);

        txtTitle.setText(mTitle);
        mAdapter = new ListAdapter();
        gridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onOk();
                MeHangyeListDialog.this.cancel();
            }
        });
    }

    public class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return (mDatas == null) ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return (mDatas == null) ? null : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGridHolder holder = new ViewGridHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_condition_hangye_griditem, null);

                holder.txtTitle = (ToggleButton) convertView.findViewById(R.id.txt_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewGridHolder) convertView.getTag();
            }

            convertView.setId(position);

            final XyleixingModel item = (XyleixingModel) getItem(position);

            holder.id = item.getId();
            holder.txtTitle.setText(item.getTitle());
            holder.txtTitle.setTextOn(item.getTitle());
            holder.txtTitle.setTextOff(item.getTitle());

            if (mIsWatched == true) {
                if (item.getIsMyWatched() == 1)
                    holder.txtTitle.setChecked(true);
                else
                    holder.txtTitle.setChecked(false);
            } else {
                if (item.getIsMyWatch() == 1)
                    holder.txtTitle.setChecked(true);
                else
                    holder.txtTitle.setChecked(false);
            }

            holder.txtTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mIsWatched == true) {
                        if (isChecked)
                            item.setIsMyWatched(1);
                        else
                            item.setIsMyWatched(0);
                    } else {
                        if (isChecked)
                            item.setIsMyWatch(1);
                        else
                            item.setIsMyWatch(0);
                    }
                }
            });

            return convertView;
        }
    }

    private static class ViewGridHolder {
        public int id;
        public ToggleButton txtTitle;
    }

}
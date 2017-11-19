package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.MarkLogListModel;
import com.beijing.chengxin.network.model.MarkLogModel;
import com.beijing.chengxin.ui.activity.ChengxinLogActivity;
import com.beijing.chengxin.ui.view.ChengXinRateView;
import com.beijing.chengxin.ui.widget.Utils;

import java.util.ArrayList;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class ChengXinLogFragment extends Fragment {

    public final String TAG = ChengXinLogFragment.class.getName();

    private View rootView;
    ListView listView;
    LogListAdapter listAdapter;
    ChengXinRateView rate_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chengxin_log, container, false);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.chengxin_log));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_rule).setOnClickListener(mButtonClickListener);

        rate_view = (ChengXinRateView)rootView.findViewById(R.id.rate_view);
        rate_view.setRateValue(SessionInstance.getInstance().getLoginData().getUser().getCredit());
        rate_view.start();

        listView = (ListView)rootView.findViewById(R.id.listView);
        listAdapter = new LogListAdapter(getActivity());
        listView.setAdapter(listAdapter);

        getDataTask();

        return rootView;
    }

    private void getDataTask() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(getActivity());
            }
            @Override
            protected Object doInBackground(Object... params) {
                return new SyncInfo(getActivity()).syncMarkLogList();
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                MarkLogListModel result = (MarkLogListModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        listAdapter.setData(result.getList());
                        listAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_SHORT).show();
                }
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChengxinLogActivity parent = (ChengxinLogActivity)getActivity();
            switch (v.getId()) {
                case R.id.btn_rule:
                    parent.showFragment(new ChengXinRuleFragment(), true);
                    break;
                case R.id.btn_back:
                    parent.onBackActivity();
                    break;
            }
        }
    };

    public class LogListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<MarkLogModel> mDatas;

        public LogListAdapter(Context context) {
            mContext = context;
        }

        public void setData(ArrayList<MarkLogModel> datas) {
            mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas == null ? null : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);

                holder.txtMsg = (TextView) convertView.findViewById(R.id.txt_msg);
                holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
                holder.txtEvalCnt = (TextView) convertView.findViewById(R.id.txt_eval_cnt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            convertView.setId(position);

            MarkLogModel item = (MarkLogModel) getItem(position);
            holder.txtMsg.setText(item.getMsg());
            holder.txtTime.setText(item.getWriteTimeString());
            String markStr = item.getPmark() > 0 ? "+" + item.getPmark() + " " + getString(R.string.marklog_plus) : "-" + item.getNmark() + " " + getString(R.string.marklog_minus);

            int markStrColor = getActivity().getResources().getColor(R.color.color_main_blue);
            if (item.getPmark() > 0)
                markStrColor = getActivity().getResources().getColor(R.color.color_main_blue);
            else
                markStrColor = getActivity().getResources().getColor(R.color.color_orange);

            holder.txtEvalCnt.setText(markStr);
            holder.txtEvalCnt.setTextColor(markStrColor);

            return convertView;
        }

        public class ViewHolder {
            int id;
            TextView txtMsg;
            TextView txtTime;
            TextView txtEvalCnt;
        }

    }
}

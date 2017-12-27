package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.content.Intent;
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

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.ErrorDetailModel;
import com.beijing.chengxin.network.model.ErrorModel;
import com.beijing.chengxin.network.model.EvalDetailModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.network.model.NoticeCountModel;
import com.beijing.chengxin.network.model.SystemNoticeListModel;
import com.beijing.chengxin.network.model.SystemNoticeModel;
import com.beijing.chengxin.ui.activity.ChengxinLogActivity;
import com.beijing.chengxin.ui.activity.MyErrorCorrectDetailActivity;
import com.beijing.chengxin.ui.activity.MyRealnameCertActivity;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;

import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_DUPLICATE;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class SystemNotifyFragment extends Fragment {

	public final String TAG = SystemNotifyFragment.class.getName();

    private View rootView;
    View viewBlankPart;
    ListView listView;
    ListAdapter listAdapter;
    SyncInfo info;
    List<SystemNoticeModel> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.system_notify));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        viewBlankPart = (View) rootView.findViewById(R.id.view_blank_part);
        listView = (ListView)rootView.findViewById(R.id.listView);
        listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);

        info = new SyncInfo(getContext());
        new NoticeListAsync().execute();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
            switch (v.getId()) {
                case R.id.btn_back:
                    parent.goBack();
                    break;
            }
        }
    };

    public class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return (list != null) ? list.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_system_notify, parent, false);
            final SystemNoticeModel item = list.get(position);
            TextView txt_name = (TextView)convertView.findViewById(R.id.txt_name);
            TextView txt_time = (TextView)convertView.findViewById(R.id.txt_time);
            TextView txt_count_eval = (TextView)convertView.findViewById(R.id.txt_count_eval);

            txt_name.setText(item.getMsgTitle());
            txt_time.setText(CommonUtils.getDateStrFromStrFormat(item.getWriteTimeString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
            txt_count_eval.setText(item.getMsgContent());
            if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == Constants.TEST_STATUS_PASSED) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int kind = item.getKind();
                        switch (kind) {
                            case Constants.NOTICE_KIND_ESTIMATE:
                                int estimateId = item.getEstimateId();
                                new EstimateDetailAsync().execute(String.valueOf(estimateId));
                                break;
                            case Constants.NOTICE_KIND_CORRECTION:
                                int errorId = item.getErrorId();
                                new ErrorDetailAsync().execute(String.valueOf(errorId));
                                break;
                            case Constants.NOTICE_KIND_AUTH:
                                Intent intent = new Intent(getActivity(), MyRealnameCertActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case Constants.NOTICE_KIND_INVITE:
                                int akind = item.getInviteeAkind();
                                Fragment fragment;
                                if (akind == Constants.ACCOUNT_TYPE_ENTERPRISE) {
                                    fragment = new EnterpriseDetailFragment();
                                    ((EnterpriseDetailFragment)fragment).setId(item.getInviteeId());
                                }
                                else {
                                    fragment = new PersonDetailFragment();
                                    ((PersonDetailFragment)fragment).setId(item.getInviteeId());
                                }
                                ((BaseFragmentActivity)getActivity()).showFragment(fragment, true);
                                break;
                        }
                    }
                });
            }
            return convertView;
        }

    }

    class NoticeListAsync extends AsyncTask<String, String, SystemNoticeListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected SystemNoticeListModel doInBackground(String... strs) {
            return info.syncSystemNoticeList();
        }
        @Override
        protected void onPostExecute(SystemNoticeListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    list = result.getList();
                    listAdapter.notifyDataSetChanged();
                } else if (result.getRetCode() == ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_SHORT).show();
            }
            Utils.disappearProgressDialog();
            if (list == null || list.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Utils.disappearProgressDialog();
        }
    }

    class EstimateDetailAsync extends AsyncTask<String, String, EvalDetailModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected EvalDetailModel doInBackground(String... strs) {
            return info.syncEvalDetail(strs[0]);
        }
        @Override
        protected void onPostExecute(EvalDetailModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    EvalModel eval = result.getEval();
                    EvalDetailFragment fragment = new EvalDetailFragment();
                    fragment.setEvalModel(eval);
                    ((BaseFragmentActivity)getActivity()).showFragment(fragment, true);
                } else if (result.getRetCode() == ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
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
    }

    class ErrorDetailAsync extends AsyncTask<String, String, ErrorDetailModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected ErrorDetailModel doInBackground(String... strs) {
            return info.syncErrorDetail(strs[0]);
        }
        @Override
        protected void onPostExecute(ErrorDetailModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    ErrorModel error = result.getErrorInfo();
                    Intent intent = new Intent(getActivity(), MyErrorCorrectDetailActivity.class);
                    intent.putExtra("data", error);
                    startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (result.getRetCode() == ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
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
    }
}

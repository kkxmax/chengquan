package com.beijing.chengxin.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.UserListModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.activity.MakeEvaluationActivity;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.view.HangyeListView;
import com.beijing.chengxin.ui.widget.CustomToast;
import com.beijing.chengxin.ui.widget.RefreshListView;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.PAGE_ITEM_COUNT;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class MainEvalFragment extends Fragment {
    public final String TAG = MainEvalFragment.class.getName();

    private static final int INDEX_GET_DATA_PERSON = 1;
    private static final int INDEX_GET_DATA_COMPANY = 2;

    private View rootView;
    public HangyeListView mHangyeListView;

    View viewBlankPart;
    ToggleButton btnPerson, btnEnterprise;
    ImageButton btnWrite;
    Button btnConditionSet;

    RefreshListView mRefreshView1, mRefreshView2;
    ItemDetailAdapter mAdapter1, mAdapter2;
    List<UserModel> listPerson;
    List<UserModel> listEnterprise;
    UserModel selectUser;

    SyncInfo info;

    private List<Integer> xyList1;
    private List<Integer> xyList2;
    public String keyword1 = "";
    public String keyword2 = "";

    boolean isDataLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        info = new SyncInfo(getActivity());

        if (listPerson == null)
            listPerson = new ArrayList<UserModel>();
        if (listEnterprise == null)
            listEnterprise = new ArrayList<UserModel>();

        if (xyList1 == null)
            xyList1 = new ArrayList<Integer>();
        if (xyList2 == null)
            xyList2 = new ArrayList<Integer>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main_eval, container, false);
        btnPerson = (ToggleButton)rootView.findViewById(R.id.btn_person);
        btnEnterprise = (ToggleButton)rootView.findViewById(R.id.btn_enterprise);
        btnWrite = (ImageButton)rootView.findViewById(R.id.btn_write);
        btnConditionSet = (Button) rootView.findViewById(R.id.btn_condition_set);
        viewBlankPart = (View) rootView.findViewById(R.id.view_blank_part);

        btnPerson.setOnClickListener(mButtonClickListener);
        btnEnterprise.setOnClickListener(mButtonClickListener);
        btnWrite.setOnClickListener(mButtonClickListener);
        btnConditionSet.setOnClickListener(mButtonClickListener);

        mRefreshView1 = (RefreshListView) rootView.findViewById(R.id.refreshView1);
        mRefreshView1.showFooter(true);
        mAdapter1 = new ItemDetailAdapter(getActivity(), listPerson, listItemClickListener);
        mRefreshView1.setAdapter(mAdapter1);
        mRefreshView1.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshHeader(View view) {
                reloadData(INDEX_GET_DATA_PERSON);
            }
            @Override
            public void onRefreshFooter(View view) {
                loadData(INDEX_GET_DATA_PERSON);
            }
        });

        mRefreshView2 = (RefreshListView) rootView.findViewById(R.id.refreshView2);
        mRefreshView2.showFooter(true);
        mAdapter2 = new ItemDetailAdapter(getActivity(), listEnterprise, listItemClickListener);
        mRefreshView2.setAdapter(mAdapter2);
        mRefreshView2.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshHeader(View view) {
                reloadData(INDEX_GET_DATA_COMPANY);
            }
            @Override
            public void onRefreshFooter(View view) {
                loadData(INDEX_GET_DATA_COMPANY);
            }
        });

        if (btnPerson.isChecked())
            MainActivity.mainActivity.showKeyword(keyword1);
        else
            MainActivity.mainActivity.showKeyword(keyword2);

        if (listPerson.size() < 1)
            loadData(INDEX_GET_DATA_PERSON);

        return rootView;
    }

    public void setKeyword(String keyword) {
        if (btnPerson.isChecked()) {
            keyword1 = keyword;
            reloadData(INDEX_GET_DATA_PERSON);
        } else {
            keyword2 = keyword;
            reloadData(INDEX_GET_DATA_COMPANY);
        }
    }

    private void loadData(int get_index) {
        if (get_index == INDEX_GET_DATA_PERSON) {
            String start = String.valueOf(listPerson.size());
            String length = String.valueOf(PAGE_ITEM_COUNT);
            String xyleixingId = "";
            for (int i = 0; i < xyList1.size(); i++) {
                if (!xyleixingId.equals(""))
                    xyleixingId += ",";
                xyleixingId += String.valueOf(xyList1.get(i));
            }

            new PersonListAsync().execute(start, length, String.valueOf(Constants.ACCOUNT_TYPE_PERSON), xyleixingId, keyword1);
        }
        if (get_index == INDEX_GET_DATA_COMPANY) {
            String start = String.valueOf(listEnterprise.size());
            String length = String.valueOf(PAGE_ITEM_COUNT);
            String xyleixingId = "";
            for (int i = 0; i < xyList2.size(); i ++) {
                if (!xyleixingId.equals(""))
                    xyleixingId += ",";
                xyleixingId += String.valueOf(xyList2.get(i));
            }

            new EnterListAsync().execute(start, length, String.valueOf(Constants.ACCOUNT_TYPE_ENTERPRISE), xyleixingId, keyword2);
        }
    }

    public void reloadData(int get_index) {
        if (get_index == INDEX_GET_DATA_PERSON)
            listPerson = new ArrayList<>();
        if (get_index == INDEX_GET_DATA_COMPANY)
            listEnterprise = new ArrayList<>();
        loadData(get_index);
    }

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
            UserModel user = btnPerson.isChecked() ? listPerson.get(position) : listEnterprise.get(position);
            if (user.getTestStatus() == TEST_STATUS_PASSED) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                if (btnPerson.isChecked()) {
                    intent.putExtra("id", listPerson.get(position).getId());
                    intent.putExtra("akind", Constants.ACCOUNT_TYPE_PERSON);
                } else {
                    intent.putExtra("id", listEnterprise.get(position).getId());
                    intent.putExtra("akind", Constants.ACCOUNT_TYPE_ENTERPRISE);
                }
                startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
            } else {
                CustomToast.makeText(getActivity(), getString(R.string.err_not_test_passed_person), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (btnPerson.isChecked()) {
            mRefreshView1.setVisibility(View.VISIBLE);
            mRefreshView2.setVisibility(View.GONE);
        } else {
            mRefreshView1.setVisibility(View.GONE);
            mRefreshView2.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_person:
                    btnPerson.setChecked(true);
                    btnEnterprise.setChecked(false);
                    mRefreshView1.setVisibility(View.VISIBLE);
                    mRefreshView2.setVisibility(View.GONE);
                    MainActivity.mainActivity.showKeyword(keyword1);
                    if (listPerson.size() == 0)
                        viewBlankPart.setVisibility(View.VISIBLE);
                    else
                        viewBlankPart.setVisibility(View.GONE);
                    if (listPerson.size() < 1)
                        loadData(INDEX_GET_DATA_PERSON);
                    break;
                case R.id.btn_enterprise:
                    btnPerson.setChecked(false);
                    btnEnterprise.setChecked(true);
                    mRefreshView1.setVisibility(View.GONE);
                    mRefreshView2.setVisibility(View.VISIBLE);
                    MainActivity.mainActivity.showKeyword(keyword2);
                    if (listEnterprise.size() == 0)
                        viewBlankPart.setVisibility(View.VISIBLE);
                    else
                        viewBlankPart.setVisibility(View.GONE);
                    if (listEnterprise.size() < 1)
                        loadData(INDEX_GET_DATA_COMPANY);
                    break;
                case R.id.btn_write:
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == Constants.TEST_STATUS_PASSED) {
                        Intent intent = new Intent(getActivity(), MakeEvaluationActivity.class);
                        startActivityForResult(intent, Constants.ACTIVITY_MAKE_EVAL);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        CommonUtils.showRealnameCertAlert(getActivity());
                    }
                    break;
                case R.id.btn_condition_set:
                    onClickedConditionSet();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == Constants.ACTIVITY_MAKE_EVAL) {
            reloadData(INDEX_GET_DATA_PERSON);
            reloadData(INDEX_GET_DATA_COMPANY);
        }
    }

    public class ItemDetailAdapter extends BaseAdapter {

        public List<UserModel> mUsers;
        private Context mContext;
        private OnItemClickListener itemClickListener;

        public ItemDetailAdapter(Context context, List items, OnItemClickListener listener) {
            mContext = context;
            itemClickListener = listener;
            mUsers = items;
        }

        public void setUserList(List<UserModel>users) {
            mUsers = users;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return (mUsers == null) ? 0 : mUsers.size();
        }

        @Override
        public Object getItem(int position) {
            return (mUsers == null) ? null : mUsers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eval_main, null);

                viewHolder.img_avatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                viewHolder.txt_item_type = (TextView) convertView.findViewById(R.id.txt_item_type);
                viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                viewHolder.txt_job_type = (TextView) convertView.findViewById(R.id.txt_job_type);
                viewHolder.txt_chengxin_id = (TextView) convertView.findViewById(R.id.txt_chengxin_id);
                viewHolder.txt_chengxin_rate = (TextView) convertView.findViewById(R.id.txt_chengxin_rate);
                viewHolder.txt_eval_count = (TextView) convertView.findViewById(R.id.txt_eval_count);
                viewHolder.txt_front_eval = (TextView) convertView.findViewById(R.id.txt_front_eval);
                viewHolder.txt_back_eval = (TextView) convertView.findViewById(R.id.txt_back_eval);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            convertView.setId(position);

            final UserModel user = mUsers.get(position);

            if (user.getAkind() == Constants.ACCOUNT_TYPE_PERSON) {
                Picasso.with(getActivity())
                        .load(Constants.FILE_ADDR + user.getLogo())
                        .placeholder(R.drawable.no_image_person)
                        .into(viewHolder.img_avatar);
                viewHolder.txt_item_type.setText(getString(R.string.str_person));
                String name = user.getRealname();
                if (name.equals(""))
                    name = user.getMobile();
                viewHolder.txt_name.setText(name);
            } else {
                Picasso.with(getActivity())
                        .load(Constants.FILE_ADDR + user.getLogo())
                        .placeholder(R.drawable.no_image_enter)
                        .into(viewHolder.img_avatar);
                viewHolder.txt_item_type.setText(getString(R.string.str_enterprise));
                viewHolder.txt_name.setText(user.getEnterName());
            }

            String xyname = user.getXyName();
            if (xyname.equals("")) {
                viewHolder.txt_job_type.setVisibility(View.GONE);
            } else {
                viewHolder.txt_job_type.setText(xyname);
                viewHolder.txt_job_type.setVisibility(View.VISIBLE);
            }

            viewHolder.txt_chengxin_id.setText(user.getCode());

            if (user.getCredit() < Constants.LEVEL_ZERO)
                viewHolder.txt_chengxin_rate.setText(getText(R.string.rate_zero));
            else
                viewHolder.txt_chengxin_rate.setText(String.format("%d%%", user.getCredit()));

            viewHolder.txt_eval_count.setText(String.valueOf(user.getFeedbackCnt()));
            viewHolder.txt_front_eval.setText(String.valueOf(user.getPositiveFeedbackCnt()));
            viewHolder.txt_back_eval.setText(String.valueOf(user.getNegativeFeedbackCnt()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                        itemClickListener.onListItemClick(position, v);
                }
            });

            return convertView;
        }

        public class ViewHolder {
            ImageView img_avatar;
            TextView txt_item_type;
            TextView txt_name;
            TextView txt_job_type;
            TextView txt_chengxin_id;
            TextView txt_chengxin_rate;
            TextView txt_eval_count;
            TextView txt_front_eval;
            TextView txt_back_eval;
        }
    }

    private void onClickedConditionSet() {
        int visibility = MainActivity.mainActivity.layoutCondition.getVisibility();
        if (visibility == View.GONE) {
            MainActivity.mainActivity.layoutCondition.setVisibility(View.VISIBLE);
            MainActivity.mainActivity.layoutConditionBody.removeAllViews();
            if (mHangyeListView == null) {
                mHangyeListView = new HangyeListView(getContext(), hyListener);
            }
            MainActivity.mainActivity.layoutConditionBody.addView(mHangyeListView);
            if (mHangyeListView.getVisibility() == View.GONE)
                mHangyeListView.setVisibility(View.VISIBLE);
            if (btnPerson.isChecked())
                mHangyeListView.setData(AppConfig.getInstance().xyleixingList, xyList1, true);
            else
                mHangyeListView.setData(AppConfig.getInstance().xyleixingList, xyList2, true);
            CommonUtils.animationShowFromRight(mHangyeListView);
        } else {
            MainActivity.mainActivity.layoutCondition.setVisibility(View.GONE);
            if (mHangyeListView != null && mHangyeListView.getVisibility() == View.VISIBLE) {
                mHangyeListView.setVisibility(View.GONE);
            }
        }
    }

    HangyeListView.OnHangyeSelectListener hyListener = new HangyeListView.OnHangyeSelectListener() {
        @Override
        public void OnHangyeSelected(List<Integer> list) {
            if (btnPerson.isChecked()) {
                xyList1 = list;
                reloadData(INDEX_GET_DATA_PERSON);
            } else {
                xyList2 = list;
                reloadData(INDEX_GET_DATA_COMPANY);
            }
            onClickedConditionSet();
        }
        @Override
        public void OnHangyeCanceled() {
            onClickedConditionSet();
        }
    };

    class PersonListAsync extends AsyncTask<String, String, UserListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected UserListModel doInBackground(String... strs) {
            return info.syncAccountListForEstimate(strs[0], strs[1], strs[2], strs[3], strs[4]);
        }
        @Override
        protected void onPostExecute(UserListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    listPerson.addAll(result.getList());
                    mAdapter1.setUserList(listPerson);
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishAndLoginActivityFromDuplicate(getActivity());
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
            isDataLoading = false;
            Utils.disappearProgressDialog();
            mRefreshView1.onRefreshCompleteHeader();
            mRefreshView1.onRefreshCompleteFooter();
            if (btnPerson.isChecked()) {
                if (listPerson.size() == 0)
                    viewBlankPart.setVisibility(View.VISIBLE);
                else
                    viewBlankPart.setVisibility(View.GONE);
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            isDataLoading = false;
            Utils.disappearProgressDialog();
            mRefreshView1.onRefreshCompleteHeader();
            mRefreshView1.onRefreshCompleteFooter();
        }
    }

    class EnterListAsync extends AsyncTask<String, String, UserListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected UserListModel doInBackground(String... strs) {
            return info.syncAccountListForEstimate(strs[0], strs[1], strs[2], strs[3], strs[4]);
        }
        @Override
        protected void onPostExecute(UserListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    listEnterprise.addAll(result.getList());
                    mAdapter2.setUserList(listEnterprise);
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishAndLoginActivityFromDuplicate(getActivity());
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
            isDataLoading = false;
            Utils.disappearProgressDialog();
            mRefreshView2.onRefreshCompleteHeader();
            mRefreshView2.onRefreshCompleteFooter();
            if (btnEnterprise.isChecked()) {
                if (listEnterprise.size() == 0)
                    viewBlankPart.setVisibility(View.VISIBLE);
                else
                    viewBlankPart.setVisibility(View.GONE);
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            isDataLoading = false;
            Utils.disappearProgressDialog();
            mRefreshView2.onRefreshCompleteHeader();
            mRefreshView2.onRefreshCompleteFooter();
        }
    }
}

package com.beijing.chengxin.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.UserListModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.listener.OnUserSelectListener;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class AccountSelectFragment extends Fragment {

	public final String TAG = AccountSelectFragment.class.getName();
    private View rootView;

    ListView listView;
    UserListAdapter mAdapter;

    SyncInfo info;

    List<UserModel> listEnter;
    List<UserModel> listPerson;

    public int aKind;
    public int userId;
    public int currentIndex;

    OnUserSelectListener mListener;

    public void setData(int aKind, int userId) {
        this.aKind = aKind;
        this.userId = userId;
        currentIndex = -1;
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void setOnUserSelectListener(OnUserSelectListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        info = new SyncInfo(getActivity());
        listEnter = new ArrayList<UserModel>();
        listPerson = new ArrayList<UserModel>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_account_select, container, false);
        TextView txtTitle = (TextView)rootView.findViewById(R.id.txt_nav_title);
        txtTitle.setText((aKind == Constants.ACCOUNT_TYPE_PERSON) ? getText(R.string.select_person) : getText(R.string.select_enter));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_ok).setOnClickListener(mButtonClickListener);

        listView = (ListView) rootView.findViewById(R.id.listView);
        mAdapter = new UserListAdapter();
        listView.setAdapter(mAdapter);

        new UserListAsync().execute();

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancel:
                case R.id.btn_back:
                    if (mListener != null)
                        mListener.onCancel();
                    break;
                case R.id.btn_ok:
                    if (currentIndex < 0)
                        Toast.makeText(getActivity(), "请选择一个用户", Toast.LENGTH_LONG).show();
                    else
                        mListener.onUserSelected(aKind == Constants.ACCOUNT_TYPE_PERSON ? listPerson.get(currentIndex) : listEnter.get(currentIndex));
                    break;
            }
        }
    };

    public class UserListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (aKind == Constants.ACCOUNT_TYPE_PERSON)
                return listPerson.size();
            else
                return listEnter.size();
        }

        @Override
        public Object getItem(int position) {
            if (aKind == Constants.ACCOUNT_TYPE_PERSON)
                return listPerson.get(position);
            else
                return listEnter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final UserModel item = (UserModel)getItem(position);

            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);

            ImageView img_avatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);

            Picasso.with(getActivity())
                    .load(Constants.FILE_ADDR + item.getLogo())
                    .placeholder(item.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.drawable.no_image_person_center : R.drawable.no_image_item)
                    .into(img_avatar);

            txt_name.setText(CommonUtils.getUserName(item));
            if (item.getId() == userId) {
                currentIndex = position;
                convertView.setBackgroundColor(getResources().getColor(R.color.color_blue_white));
            }
            else
                convertView.setBackgroundColor(getResources().getColor(R.color.color_white));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userId = item.getId();
                    mAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    class UserListAsync extends AsyncTask<String, String, UserListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected UserListModel doInBackground(String... strs) {
            if (aKind == Constants.ACCOUNT_TYPE_PERSON)
                return info.syncPassedPersonalList();
            return info.syncPassedEnterList();
        }
        @Override
        protected void onPostExecute(UserListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    if (aKind == Constants.ACCOUNT_TYPE_PERSON)
                        listPerson.addAll(result.getList());
                    else
                        listEnter.addAll(result.getList());
                    mAdapter.notifyDataSetChanged();
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
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

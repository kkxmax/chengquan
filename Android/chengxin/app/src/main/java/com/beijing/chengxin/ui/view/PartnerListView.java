package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.GridView;
import com.beijing.chengxin.ui.widget.ListViewNoScroll;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.beijing.chengxin.config.Constants.ACCOUNT_TYPE_PERSON;
import static com.beijing.chengxin.config.Constants.ENTER_TYPE_PERSONAL;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class PartnerListView extends BaseView {

    LinearLayout viewBodyPart, viewBlankPart;
    ListViewNoScroll listView;

    private List<UserModel> listEnterprise;
    private ListAdapter mAdapter;
    private TextView txt_blank;
    int selectedIndex;
    private OnItemClickListener itemClickListener;

    SyncInfo info;

    public PartnerListView(Context context) {
        super(context);
        info = new SyncInfo(context);
        initialize();
    }

    public void setData(List<UserModel> datas, OnItemClickListener listener) {
        this.listEnterprise = datas;
        itemClickListener = listener;
        initData();
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.view_tab_item);

        viewBodyPart = (LinearLayout) findViewById(R.id.view_body_part);
        viewBlankPart = (LinearLayout) findViewById(R.id.view_blank_part);
        listView = (ListViewNoScroll) findViewById(R.id.list_view);
        txt_blank = (TextView)findViewById(R.id.txt_blank);
        txt_blank.setText("暂时还没有同行企业");

        mAdapter = new ListAdapter();
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (listEnterprise != null && listEnterprise.size() > 0) {
            viewBodyPart.setVisibility(View.VISIBLE);
            viewBlankPart.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        } else {
            viewBodyPart.setVisibility(View.GONE);
            viewBlankPart.setVisibility(View.VISIBLE);
        }
    }

    public class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return (listEnterprise == null) ? 0 : listEnterprise.size();
        }

        @Override
        public Object getItem(int position) {
            return (listEnterprise == null) ? null : listEnterprise.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_enterprise, null);

            final ViewHolder viewHolder = new ViewHolder(convertView);
            final UserModel user = listEnterprise.get(position);

            Picasso.with(getContext())
                    .load(Constants.FILE_ADDR + user.getLogo())
                    .placeholder(R.drawable.no_image)
                    .into(viewHolder.img_avatar);

            if (user.getAkind() == Constants.ACCOUNT_TYPE_PERSON)
                viewHolder.txt_item_type.setText(getString(R.string.str_person));
            else
                viewHolder.txt_item_type.setText(getString(R.string.str_enterprise));

            viewHolder.txt_name.setText(CommonUtils.getUserName(user));

            if (user.getReqCodeSenderAkind() == ACCOUNT_TYPE_PERSON) {
                String senderName = user.getReqCodeSenderRealname();
                if (!senderName.equals("")) {
                    if (!user.getInviterFriendLevel().equals(""))
                        senderName = user.getInviterFriendLevel() + " - " + senderName;
                    viewHolder.txt_suggest_man.setText(senderName);
                } else {
                    viewHolder.layout_recommender.setVisibility(View.GONE);
                }
            }
            else {
                String senderName = user.getReqCodeSenderEnterName();
                if (!senderName.equals("")) {
                    if (!user.getInviterFriendLevel().equals(""))
                        senderName = user.getInviterFriendLevel() + " - " + senderName;
                    viewHolder.txt_suggest_man.setText(senderName);
                } else {
                    viewHolder.layout_recommender.setVisibility(View.GONE);
                }
            }

            if (user.getXyName().equals(""))
                viewHolder.txt_job_type.setVisibility(View.GONE);
            else {
                viewHolder.txt_job_type.setText(user.getXyName());
                viewHolder.txt_job_type.setVisibility(View.VISIBLE);
            }

            viewHolder.txt_chengxin_id.setText(user.getCode());

            if (user.getProducts().size() > 0) {
                String productStr = CommonUtils.getComedityListName(user.getProducts());
                viewHolder.txt_main_comedity.setText(productStr);
                viewHolder.layout_comedity.setVisibility(View.VISIBLE);
            } else
                viewHolder.layout_comedity.setVisibility(View.GONE);

            if (user.getItems().size() > 0) {
                String itemStr = CommonUtils.getItemListName(user.getItems());
                viewHolder.txt_item.setText(itemStr);
                viewHolder.layout_item.setVisibility(View.VISIBLE);
            } else
                viewHolder.layout_item.setVisibility(View.GONE);

            if (user.getServices().size() > 0) {
                String serveStr = CommonUtils.getServeListName(user.getServices());
                viewHolder.txt_serve.setText(serveStr);
                viewHolder.layout_serve.setVisibility(View.VISIBLE);
            } else
                viewHolder.layout_serve.setVisibility(View.GONE);

            if (user.getCredit() < Constants.LEVEL_ZERO)
                viewHolder.txt_chengxin_rate.setText(getContext().getText(R.string.rate_zero));
            else
                viewHolder.txt_chengxin_rate.setText(String.format("%d%%", user.getCredit()));

            viewHolder.txt_like_count.setText(String.valueOf(user.getElectCnt()));
            viewHolder.txt_eval_count.setText(String.valueOf(user.getFeedbackCnt()));

            final boolean interest = (user.getInterested() != 0);
            viewHolder.btn_follow.setChecked(interest);

            viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.btn_follow.setChecked(interest);
                    selectedIndex = position;
                    int mInterest = (user.getInterested() + 1) % 2;
                    new SetInterestAsync().execute(String.valueOf(user.getId()), String.valueOf(mInterest));
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                        itemClickListener.onListItemClick(position, v);
                }
            });

            return convertView;
        }
    }

    public class ViewHolder{
        private ImageView img_avatar;
        private TextView txt_item_type;
        private TextView txt_name;
        private TextView txt_job_type;
        private TextView txt_chengxin_id;
        private TextView txt_main_comedity;
        private TextView txt_item;
        private TextView txt_serve;
        private TextView txt_suggest_man;
        private TextView txt_chengxin_rate;
        private TextView txt_like_count;
        private TextView txt_eval_count;
        private ToggleButton btn_follow;
        private LinearLayout layout_comedity;
        private LinearLayout layout_item;
        private LinearLayout layout_serve;
        private LinearLayout layout_recommender;

        public ViewHolder(View itemView) {
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            btn_follow = (ToggleButton) itemView.findViewById(R.id.btn_follow);
            txt_item_type = (TextView) itemView.findViewById(R.id.txt_item_type);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_job_type = (TextView) itemView.findViewById(R.id.txt_job_type);
            txt_chengxin_id = (TextView) itemView.findViewById(R.id.txt_chengxin_id);
            txt_main_comedity = (TextView) itemView.findViewById(R.id.txt_main_comedity);
            txt_item = (TextView) itemView.findViewById(R.id.txt_item);
            txt_serve = (TextView) itemView.findViewById(R.id.txt_serve);
            txt_suggest_man = (TextView) itemView.findViewById(R.id.txt_suggest_man);
            txt_chengxin_rate = (TextView) itemView.findViewById(R.id.txt_chengxin_rate);
            txt_like_count = (TextView) itemView.findViewById(R.id.txt_like_count);
            txt_eval_count = (TextView) itemView.findViewById(R.id.txt_eval_count);
            layout_comedity = (LinearLayout) itemView.findViewById(R.id.layout_comedity);
            layout_item = (LinearLayout) itemView.findViewById(R.id.layout_item);
            layout_serve = (LinearLayout) itemView.findViewById(R.id.layout_serve);
            layout_recommender = (LinearLayout) itemView.findViewById(R.id.layout_recommender);
        }
    }

    class SetInterestAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getContext());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncSetInterest(strs[0], strs[1]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    UserModel item = listEnterprise.get(selectedIndex);
                    item.setInterested((item.getInterested() + 1) % 2);
                    listEnterprise.set(selectedIndex, item);

                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
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

package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
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
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.EnterpriseDetailFragment;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.beijing.chengxin.config.Constants.ACCOUNT_TYPE_PERSON;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class PartnerListView extends BaseView {

    LinearLayout viewBodyPart, viewBlankPart;

    private List<UserModel> listEnterprise;
    private TextView txt_blank;

    SyncInfo info;

    public PartnerListView(Context context) {
        super(context);
        info = new SyncInfo(context);
        initialize();
    }

    public void setData(List<UserModel> datas) {
        this.listEnterprise = datas;
        initData();
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.view_tab_item_nolist);

        viewBodyPart = (LinearLayout) findViewById(R.id.view_body_part);
        viewBlankPart = (LinearLayout) findViewById(R.id.view_blank_part);
        txt_blank = (TextView)findViewById(R.id.txt_blank);
        txt_blank.setText(R.string.empty_data);
    }

    @Override
    protected void initData() {
        if (listEnterprise != null && listEnterprise.size() > 0) {
            viewBodyPart.setVisibility(View.VISIBLE);
            viewBlankPart.setVisibility(View.GONE);
            initListData(listEnterprise);
        } else {
            viewBodyPart.setVisibility(View.GONE);
            viewBlankPart.setVisibility(View.VISIBLE);
        }
    }

    private void initListData(List<UserModel> listData) {
        for (int i = 0; i < listData.size(); i++) {
            View itemView = getItemView(listData.get(i));

            viewBodyPart.addView(itemView);
        }
    }

    private View getItemView(final UserModel item) {
        final View convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_enterprise, null);

        ImageView img_avatar = (ImageView) convertView.findViewById(R.id.img_avatar);
        final ToggleButton btn_follow = (ToggleButton) convertView.findViewById(R.id.btn_follow);
        TextView txt_item_type = (TextView) convertView.findViewById(R.id.txt_item_type);
        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView txt_job_type = (TextView) convertView.findViewById(R.id.txt_job_type);
        TextView txt_chengxin_id = (TextView) convertView.findViewById(R.id.txt_chengxin_id);
        TextView txt_main_comedity = (TextView) convertView.findViewById(R.id.txt_main_comedity);
        TextView txt_item = (TextView) convertView.findViewById(R.id.txt_item);
        TextView txt_serve = (TextView) convertView.findViewById(R.id.txt_serve);
        TextView txt_suggest_man = (TextView) convertView.findViewById(R.id.txt_suggest_man);
        TextView txt_chengxin_rate = (TextView) convertView.findViewById(R.id.txt_chengxin_rate);
        TextView txt_like_count = (TextView) convertView.findViewById(R.id.txt_like_count);
        TextView txt_eval_count = (TextView) convertView.findViewById(R.id.txt_eval_count);
        LinearLayout layout_comedity = (LinearLayout) convertView.findViewById(R.id.layout_comedity);
        LinearLayout layout_item = (LinearLayout) convertView.findViewById(R.id.layout_item);
        LinearLayout layout_serve = (LinearLayout) convertView.findViewById(R.id.layout_serve);
        LinearLayout layout_recommender = (LinearLayout) convertView.findViewById(R.id.layout_recommender);

        Picasso.with(getContext())
                .load(Constants.FILE_ADDR + item.getLogo())
                .placeholder(item.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.drawable.no_image_person_center : R.drawable.no_image_item)
                .into(img_avatar);

        if (item.getAkind() == Constants.ACCOUNT_TYPE_PERSON)
            txt_item_type.setText(getString(R.string.str_person));
        else
            txt_item_type.setText(getString(R.string.str_enterprise));

        txt_name.setText(CommonUtils.getUserName(item));

        if (item.getReqCodeSenderAkind() == ACCOUNT_TYPE_PERSON) {
            String senderName = item.getReqCodeSenderRealname();
            if (!senderName.equals("")) {
                if (!item.getInviterFriendLevel().equals(""))
                    senderName = item.getInviterFriendLevel() + " - " + senderName;
                txt_suggest_man.setText(senderName);
            } else {
                layout_recommender.setVisibility(View.GONE);
            }
        }
        else {
            String senderName = item.getReqCodeSenderEnterName();
            if (!senderName.equals("")) {
                if (!item.getInviterFriendLevel().equals(""))
                    senderName = item.getInviterFriendLevel() + " - " + senderName;
                txt_suggest_man.setText(senderName);
            } else {
                layout_recommender.setVisibility(View.GONE);
            }
        }

        if (item.getXyName().equals(""))
            txt_job_type.setVisibility(View.GONE);
        else {
            txt_job_type.setText(item.getXyName());
            txt_job_type.setVisibility(View.VISIBLE);
        }

        txt_chengxin_id.setText(item.getCode());

        if (item.getProducts().size() > 0) {
            String productStr = CommonUtils.getComedityListName(item.getProducts());
            txt_main_comedity.setText(productStr);
            layout_comedity.setVisibility(View.VISIBLE);
        } else
            layout_comedity.setVisibility(View.GONE);

        if (item.getItems().size() > 0) {
            String itemStr = CommonUtils.getItemListName(item.getItems());
            txt_item.setText(itemStr);
            layout_item.setVisibility(View.VISIBLE);
        } else
            layout_item.setVisibility(View.GONE);

        if (item.getServices().size() > 0) {
            String serveStr = CommonUtils.getServeListName(item.getServices());
            txt_serve.setText(serveStr);
            layout_serve.setVisibility(View.VISIBLE);
        } else
            layout_serve.setVisibility(View.GONE);

        if (item.getCredit() < Constants.LEVEL_ZERO)
            txt_chengxin_rate.setText(getContext().getText(R.string.rate_zero));
        else
            txt_chengxin_rate.setText(String.format("%d%%", item.getCredit()));

        txt_like_count.setText(String.valueOf(item.getElectCnt()));
        txt_eval_count.setText(String.valueOf(item.getFeedbackCnt()));

        final boolean interest = (item.getInterested() != 0);
        btn_follow.setChecked(interest);

        btn_follow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_follow.setChecked(interest);
                int mInterest = (item.getInterested() + 1) % 2;
                setInterestTask(item, convertView, String.valueOf(item.getId()), String.valueOf(mInterest));
            }
        });

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterpriseDetailFragment fragment = new EnterpriseDetailFragment();
                fragment.setId(item.getId());
                ((BaseFragmentActivity) mActivity).addFragment(fragment);
            }
        });

        return convertView;
    }

    private void setInterestTask(final UserModel item, final View itemView, final String id, final String interest) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(getContext());
            }
            @Override
            protected BaseModel doInBackground(Object... strs) {
                return info.syncSetInterest(id, interest);
            }
            @Override
            protected void onPostExecute(Object obj) {
                BaseModel result = (BaseModel) obj;
                if (result .isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        item.setInterested((item.getInterested() + 1) % 2);
                        final boolean interest = (item.getInterested() != 0);
                        ToggleButton btn_follow = (ToggleButton) itemView.findViewById(R.id.btn_follow);
                        btn_follow.setChecked(interest);
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
        }.execute();
    }

}

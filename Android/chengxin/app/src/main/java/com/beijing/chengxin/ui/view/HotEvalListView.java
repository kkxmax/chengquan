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

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.EvalDetailFragment;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.ListViewNoScroll;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class HotEvalListView extends BaseView {

    public LinearLayout viewBodyPart, viewBlankPart;

    private List<EvalModel> listEval;
    private TextView txt_blank;

    SyncInfo info;

    public HotEvalListView(Context context) {
        super(context);
        info = new SyncInfo(context);
        initialize();
    }

    public void setData(List<EvalModel> datas) {
        this.listEval = datas;
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
        if (listEval != null && listEval.size() > 0) {
            viewBodyPart.setVisibility(View.VISIBLE);
            viewBlankPart.setVisibility(View.GONE);
            initListData(listEval);
        } else {
            viewBodyPart.setVisibility(View.GONE);
            viewBlankPart.setVisibility(View.VISIBLE);
        }
    }

    private void initListData(List<EvalModel> listData) {
        for (int i = 0; i < listData.size(); i++) {
            View itemView = getItemView(listData.get(i));

            viewBodyPart.addView(itemView);
        }
    }

    private View getItemView(final EvalModel item) {
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_eval_hot_detail, null);

        LinearLayout layout_content = (LinearLayout) convertView.findViewById(R.id.layout_content);
        ImageView img_avatar = (ImageView) convertView.findViewById(R.id.img_avatar);
        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView txt_content = (TextView) convertView.findViewById(R.id.txt_content);
        TextView txt_date = (TextView) convertView.findViewById(R.id.txt_date);
        final TextView txt_elect_cnt = (TextView) convertView.findViewById(R.id.txt_elect_cnt);
        TextView txt_evaluate = (TextView) convertView.findViewById(R.id.txt_evaluate);
        TextView txt_comment = (TextView) convertView.findViewById(R.id.txt_comment);
        TextView txt_comment_left = (TextView) convertView.findViewById(R.id.txt_comment_left);
        LinearLayout view_bottom = (LinearLayout) convertView.findViewById(R.id.view_bottom);
        LinearLayout layout_comment = (LinearLayout) convertView.findViewById(R.id.layout_comment);

        Picasso.with(getContext())
                .load(Constants.FILE_ADDR + item.getOwnerLogo())
                .placeholder(item.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.drawable.no_image_person_center : R.drawable.no_image_item)
                .into(img_avatar);

        if (item.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON)
            txt_name.setText(item.getOwnerRealname());
        else
            txt_name.setText(item.getOwnerEnterName());

        txt_content.setText(item.getContent());
        txt_date.setText(CommonUtils.getDateStrFromStrFormat(item.getWriteTimeString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
        txt_elect_cnt.setText(String.valueOf(item.getElectCnt()));
        List<EvalModel> replys = item.getReplys();
        if (replys != null && replys.size() > 0) {
            txt_evaluate.setText(String.valueOf(replys.size()));
            EvalModel evalModel = replys.get(0);
            String name = (evalModel.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON)? evalModel.getOwnerRealname() : evalModel.getOwnerEnterName();
            txt_comment.setText(name + " : " + evalModel.getContent());
            txt_comment_left.setText(String.format("查看全部%d条回复 >", replys.size()));
        } else {
            txt_evaluate.setText("0");
            layout_comment.setVisibility(View.GONE);
            txt_comment_left.setVisibility(View.GONE);
        }

        if (item.getIsElectedByMe() == 1)
            txt_elect_cnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_sel), null, null, null);
        else
            txt_elect_cnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_nor), null, null, null);

        txt_elect_cnt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getIsElectedByMe() == 1) {
                    item.setIsElectedByMe(0);
                    item.setElectCnt(item.getElectCnt() - 1);
                    txt_elect_cnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_nor), null, null, null);
                    txt_elect_cnt.setText(String.valueOf(item.getElectCnt()));
                }
                else {
                    item.setIsElectedByMe(1);
                    item.setElectCnt(item.getElectCnt() + 1);
                    txt_elect_cnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_sel), null, null, null);
                    txt_elect_cnt.setText(String.valueOf(item.getElectCnt()));
                }

                new AsyncTask<String, String, BaseModel>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        Utils.displayProgressDialog(getContext());
                    }
                    @Override
                    protected BaseModel doInBackground(String... strs) {
                        return info.syncElect(strs[0], strs[1]);
                    }
                    @Override
                    protected void onPostExecute(BaseModel result) {
                        super.onPostExecute(result);
                        if (result .isValid()) {
                            if(result.getRetCode() == ERROR_OK) {
//                                Toast.makeText(mActivity, "chenggong", Toast.LENGTH_LONG).show();
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
                }.execute(String.valueOf(item.getId()), String.valueOf(item.getIsElectedByMe()));
            }
        });

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EvalDetailFragment fragment = new EvalDetailFragment();
                fragment.setEvalModel(item);
                ((BaseFragmentActivity) mActivity).showFragment(fragment, true);
            }
        });

        return convertView;
    }

//    class SetInterestAsync extends AsyncTask<String, String, BaseModel> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Utils.displayProgressDialog(getContext());
//        }
//        @Override
//        protected BaseModel doInBackground(String... strs) {
//            return info.syncSetInterest(strs[0], strs[1]);
//        }
//        @Override
//        protected void onPostExecute(BaseModel result) {
//            super.onPostExecute(result);
//            if (result .isValid()) {
//                if(result.getRetCode() == ERROR_OK) {
//                    EvalModel item = listEval.get(selectedIndex);
//                    item.setInterested((item.getInterested() + 1) % 2);
//                    listEval.set(selectedIndex, item);
//
//                    mAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_LONG).show();
//                }
//            } else {
//                Toast.makeText(getContext(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
//            }
//            Utils.disappearProgressDialog();
//        }
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            Utils.disappearProgressDialog();
//        }
//    }
}

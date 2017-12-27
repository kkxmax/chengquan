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
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.ListViewNoScroll;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class HotEvalListView extends BaseView {

    LinearLayout viewBodyPart, viewBlankPart;
    ListViewNoScroll listView;

    private List<EvalModel> listEval;
    private ListAdapter mAdapter;
    private TextView txt_blank;
    int selectedIndex;
    private OnItemClickListener itemClickListener;

    SyncInfo info;

    public HotEvalListView(Context context) {
        super(context);
        info = new SyncInfo(context);
        initialize();
    }

    public void setData(List<EvalModel> datas, OnItemClickListener listener) {
        this.listEval = datas;
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
        txt_blank.setText("暂时还没有评价");

        mAdapter = new ListAdapter();
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (listEval != null && listEval.size() > 0) {
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
            return (listEval == null) ? 0 : listEval.size();
        }

        @Override
        public Object getItem(int position) {
            return (listEval == null) ? null : listEval.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_eval_hot_detail, null);

            final ViewHolder viewHolder = new ViewHolder(convertView);
            final EvalModel eval = listEval.get(position);

            Picasso.with(getContext())
                    .load(Constants.FILE_ADDR + eval.getOwnerLogo())
                    .placeholder(eval.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.drawable.no_image_person : R.drawable.no_image_enter)
                    .into(viewHolder.img_avatar);

            if (eval.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON)
                viewHolder.txt_name.setText(eval.getOwnerRealname());
            else
                viewHolder.txt_name.setText(eval.getOwnerEnterName());

            viewHolder.txt_content.setText(eval.getContent());
            viewHolder.txt_date.setText(CommonUtils.getDateStrFromStrFormat(eval.getWriteTimeString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
            viewHolder.txt_elect_cnt.setText(String.valueOf(eval.getElectCnt()));
            List<EvalModel> replys = eval.getReplys();
            if (replys != null && replys.size() > 0) {
                viewHolder.txt_evaluate.setText(String.valueOf(replys.size()));
                EvalModel evalModel = replys.get(0);
                String name = (evalModel.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON)? evalModel.getOwnerRealname() : evalModel.getOwnerEnterName();
                viewHolder.txt_comment.setText(name + " : " + evalModel.getContent());
                viewHolder.txt_comment_left.setText(String.format("查看全部%d条回复 >", replys.size()));
            } else {
                viewHolder.txt_evaluate.setText("0");
                viewHolder.layout_comment.setVisibility(View.GONE);
                viewHolder.txt_comment_left.setVisibility(View.GONE);
            }

            if (eval.getIsElectedByMe() == 1)
                viewHolder.txt_elect_cnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_sel), null, null, null);
            else
                viewHolder.txt_elect_cnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_nor), null, null, null);

            viewHolder.txt_elect_cnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(eval.getIsElectedByMe() == 1) {
                        eval.setIsElectedByMe(0);
                        eval.setElectCnt(eval.getElectCnt() - 1);
                        viewHolder.txt_elect_cnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_nor), null, null, null);
                        viewHolder.txt_elect_cnt.setText(String.valueOf(eval.getElectCnt()));
                    }
                    else {
                        eval.setIsElectedByMe(1);
                        eval.setElectCnt(eval.getElectCnt() + 1);
                        viewHolder.txt_elect_cnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_sel), null, null, null);
                        viewHolder.txt_elect_cnt.setText(String.valueOf(eval.getElectCnt()));
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
                    }.execute(String.valueOf(eval.getId()), String.valueOf(eval.getIsElectedByMe()));
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

    public class ViewHolder
    {
        ImageView img_avatar;
        TextView txt_name;
        TextView txt_content;
        TextView txt_date;
        TextView txt_elect_cnt;
        TextView txt_evaluate;
        TextView txt_comment;
        TextView txt_comment_left;
        LinearLayout layout_comment;

        public ViewHolder(View itemView) {
            img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_content = (TextView) itemView.findViewById(R.id.txt_content);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_elect_cnt = (TextView) itemView.findViewById(R.id.txt_elect_cnt);
            txt_evaluate = (TextView) itemView.findViewById(R.id.txt_evaluate);
            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment);
            txt_comment_left = (TextView) itemView.findViewById(R.id.txt_comment_left);
            layout_comment = (LinearLayout) itemView.findViewById(R.id.layout_comment);
        }
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

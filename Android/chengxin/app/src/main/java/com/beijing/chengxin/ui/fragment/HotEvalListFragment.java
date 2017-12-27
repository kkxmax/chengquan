package com.beijing.chengxin.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.network.model.HotModel;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class HotEvalListFragment extends Fragment {

	public final String TAG = HotEvalListFragment.class.getName();
    private View rootView;
    private ListView listView;
    private LinearLayout viewBlankPart;

    ListAdapter mAdapter;
    HotModel item;
    List<EvalModel> listEval;

    SyncInfo info;

    public void setHotData(HotModel model, List<EvalModel> eList) {
        item = model;
        listEval = eList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_hot_eval_list, container, false);

        // set title
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.tab_eval));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mClickListener);
        rootView.findViewById(R.id.txt_comment).setOnClickListener(mClickListener);

        listView = (ListView)rootView.findViewById(R.id.listView);
        mAdapter = new ListAdapter();
        listView.setAdapter(mAdapter);

        viewBlankPart = (LinearLayout) rootView.findViewById(R.id.view_blank_part);

        if (listEval.size() == 0) {
            listView.setVisibility(View.GONE);
            viewBlankPart.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            viewBlankPart.setVisibility(View.GONE);
        }

        info = new SyncInfo(getContext());

        return rootView;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    ((BaseFragmentActivity)getActivity()).onBackPressed();
                    break;
                case R.id.txt_comment:
                    HotEvalFragment fragment = new HotEvalFragment();
                    fragment.setId(item.getId());
                    ((BaseFragmentActivity)getActivity()).showFragment(fragment, true);
                    break;
            }
        }
    };
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
                    .placeholder(R.drawable.no_image)
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
                        viewHolder.txt_elect_cnt.setText(String.format("%d", eval.getElectCnt()));
                    }
                    else {
                        eval.setIsElectedByMe(1);
                        eval.setElectCnt(eval.getElectCnt() + 1);
                        viewHolder.txt_elect_cnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_sel), null, null, null);
                        viewHolder.txt_elect_cnt.setText(String.format("%d", eval.getElectCnt()));
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
//                                Toast.makeText(mActivity, "成功", Toast.LENGTH_LONG).show();
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
                    EvalDetailFragment fragment = new EvalDetailFragment();
                    fragment.setEvalModel(listEval.get(position));
                    ((BaseFragmentActivity)getActivity()).showFragment(fragment, true);
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
}

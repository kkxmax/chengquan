package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.network.model.MyEvalNoticeModel;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_DUPLICATE;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class EvalMeNotifyFragment extends Fragment {

	public final String TAG = EvalMeNotifyFragment.class.getName();
    private View rootView;
    View viewBlankPart;
    ToggleButton btnFrontEval, btnBackEval;
    TextView txtBadgeFront, txtBadgeBack;

    ListView mRecyclerView1, mRecyclerView2;
    ItemDetailAdapter mAdapter1, mAdapter2;
    SyncInfo info;
    int nCount;
    int pCount;
    EvalModel evalItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_eval_notify, container, false);

        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.eval_notify));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        viewBlankPart = (View) rootView.findViewById(R.id.view_blank_part);
        btnFrontEval = (ToggleButton)rootView.findViewById(R.id.btn_front_eval);
        btnBackEval = (ToggleButton)rootView.findViewById(R.id.btn_back_eval);
        btnFrontEval.setOnClickListener(mButtonClickListener);
        btnBackEval.setOnClickListener(mButtonClickListener);

        txtBadgeFront = (TextView)rootView.findViewById(R.id.txt_badge_front);
        txtBadgeBack = (TextView)rootView.findViewById(R.id.txt_badge_back);

        mRecyclerView1 = (ListView)rootView.findViewById(R.id.listView1);
        mAdapter1 = new ItemDetailAdapter(getActivity(), listItemClickListener);
        mRecyclerView1.setAdapter(mAdapter1);

        mRecyclerView2 = (ListView)rootView.findViewById(R.id.listView2);
        mAdapter2 = new ItemDetailAdapter(getActivity(), listItemClickListener);
        mRecyclerView2.setAdapter(mAdapter2);

        info = new SyncInfo(getContext());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        new NoticeListAsync().execute();
        if (btnFrontEval.isChecked()) {
            mRecyclerView1.setVisibility(View.VISIBLE);
            mRecyclerView2.setVisibility(View.GONE);
            if (mAdapter1.mList == null || mAdapter1.mList.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        } else {
            mRecyclerView1.setVisibility(View.GONE);
            mRecyclerView2.setVisibility(View.VISIBLE);
            if (mAdapter2.mList == null || mAdapter2.mList.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        }
    }

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
            new ReadEstimateAsync().execute(String.valueOf(evalItem.getId()));
        }
    };

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_front_eval:
                    mRecyclerView1.setVisibility(View.VISIBLE);
                    mRecyclerView2.setVisibility(View.GONE);
                    btnFrontEval.setChecked(true);
                    btnBackEval.setChecked(false);
                    if (mAdapter1.mList == null || mAdapter1.mList.size() == 0)
                        viewBlankPart.setVisibility(View.VISIBLE);
                    else
                        viewBlankPart.setVisibility(View.GONE);
                    break;
                case R.id.btn_back_eval:
                    mRecyclerView1.setVisibility(View.GONE);
                    mRecyclerView2.setVisibility(View.VISIBLE);
                    btnFrontEval.setChecked(false);
                    btnBackEval.setChecked(true);
                    if (mAdapter2.mList == null || mAdapter2.mList.size() == 0)
                        viewBlankPart.setVisibility(View.VISIBLE);
                    else
                        viewBlankPart.setVisibility(View.GONE);
                    break;
                case R.id.btn_back:
                    BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
                    parent.goBack();
                    break;
            }
        }
    };

    public class ItemDetailAdapter extends BaseAdapter {
        private Context mContext;
        private OnItemClickListener itemClickListener;
        private  List<EvalModel> mList;

        public ItemDetailAdapter(Context context, OnItemClickListener listener) {
            mContext = context;
            itemClickListener = listener;
            mList = new ArrayList<EvalModel>();
        }

        public void setListData(List<EvalModel> list) {
            mList.clear();
            mList.addAll(list);
//            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return (mList == null) ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return (mList == null) ? null : mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_eval, null);

                viewHolder.hsView = (HorizontalScrollView) convertView.findViewById(R.id.hs_view);
                viewHolder.layoutImages = (LinearLayout) convertView.findViewById(R.id.layout_images);
                viewHolder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                viewHolder.txtItemType = (TextView) convertView.findViewById(R.id.txt_item_type);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
                viewHolder.txtEvalType = (TextView) convertView.findViewById(R.id.txt_eval_type);
                viewHolder.txtEvalContent = (TextView) convertView.findViewById(R.id.txt_eval_content);
                viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
                viewHolder.txtLikeCount = (TextView) convertView.findViewById(R.id.txt_like_count);
                viewHolder.txtEvalCount = (TextView) convertView.findViewById(R.id.txt_eval_count);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            convertView.setId(position);

            final EvalModel item = mList.get(position);

            Picasso.with(getContext())
                    .load(Constants.FILE_ADDR + item.getOwnerLogo())
                    .placeholder(R.drawable.no_image)
                    .into(viewHolder.imgAvatar);
            viewHolder.txtItemType.setVisibility(View.GONE);
            viewHolder.txtName.setText(item.getOwnerAkind() == 1 ? item.getOwnerRealname() : item.getOwnerEnterName());
            viewHolder.txtEvalType.setText(item.getKindName());
            viewHolder.txtEvalContent.setText(getResources().getString(R.string.make_eval_content) + item.getContent());
            viewHolder.txtTime.setText(CommonUtils.getDateStrFromStrFormat(item.getWriteTimeString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
            viewHolder.txtLikeCount.setText("" + item.getTargetAccountElectCnt());
            viewHolder.txtEvalCount.setText("" + item.getTargetAccountFeedbackCnt());

            final List<String> imgList = item.getImgPaths();
            if (imgList != null && imgList.size() > 0) {
                viewHolder.layoutImages.removeAllViews();
                for (int i = 0; i < imgList.size(); i++) {
                    ImageView imgView = new ImageView(getContext());
                    imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    int height = (int) (getResources().getDimension(R.dimen.image_hot_item_width));
                    int width = height * 3 / 2;
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(width, height);
                    lparams.setMargins(0, 0, (int) (getResources().getDimension(R.dimen.margin_small)), 0);
                    imgView.setLayoutParams(lparams);

                    Picasso.with(getContext())
                            .load(Constants.FILE_ADDR + imgList.get(i))
                            .placeholder(R.drawable.no_image)
                            .into(imgView);
                    viewHolder.layoutImages.addView(imgView);
                }
                viewHolder.hsView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.hsView.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    evalItem = item;
                    if (itemClickListener != null)
                        itemClickListener.onListItemClick(position, v);
                }
            });

            return convertView;
        }

        public class ViewHolder {
            ImageView imgAvatar;
            TextView txtItemType;
            TextView txtName;
            TextView txtEvalType;
            TextView txtEvalContent;
            HorizontalScrollView hsView;
            LinearLayout layoutImages;
            TextView txtTime;
            TextView txtLikeCount;
            TextView txtEvalCount;
        }
    }

    class NoticeListAsync extends AsyncTask<String, String, MyEvalNoticeModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected MyEvalNoticeModel doInBackground(String... strs) {
            return info.syncEstimateToMeNoticeList();
        }
        @Override
        protected void onPostExecute(MyEvalNoticeModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    nCount = result.getnEstimateList().size();
                    pCount = result.getpEstimateList().size();

                    mAdapter1.setListData(result.getpEstimateList());
                    mAdapter2.setListData(result.getnEstimateList());
                    mAdapter1.notifyDataSetChanged();
                    mAdapter2.notifyDataSetChanged();

                    if (nCount > 0) {
                        txtBadgeBack.setText(String.valueOf(nCount));
                        txtBadgeBack.setVisibility(View.VISIBLE);
                    } else {
                        txtBadgeBack.setVisibility(View.GONE);
                    }

                    if (pCount > 0) {
                        txtBadgeFront.setText(String.valueOf(pCount));
                        txtBadgeFront.setVisibility(View.VISIBLE);
                    } else {
                        txtBadgeFront.setVisibility(View.GONE);
                    }
                } else if (result.getRetCode() == ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
                }
            }
            if (btnFrontEval.isChecked()) {
                if (mAdapter1.mList == null || mAdapter1.mList.size() == 0)
                    viewBlankPart.setVisibility(View.VISIBLE);
                else
                    viewBlankPart.setVisibility(View.GONE);
            } else {
                if (mAdapter2.mList == null || mAdapter2.mList.size() == 0)
                    viewBlankPart.setVisibility(View.VISIBLE);
                else
                    viewBlankPart.setVisibility(View.GONE);
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    class ReadEstimateAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncReadEstimate(strs[0]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    EvalDetailFragment fragment = new EvalDetailFragment();
                    fragment.setEvalModel(evalItem);
                    ((BaseFragmentActivity)getActivity()).showFragment(fragment, true);
                } else if (result.getRetCode() == ERROR_DUPLICATE) {
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

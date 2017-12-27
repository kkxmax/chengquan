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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.HotListModel;
import com.beijing.chengxin.network.model.HotModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.RefreshListView;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainHotFragment extends Fragment {

	public final String TAG = MainHotFragment.class.getName();
    private View rootView;
    RefreshListView mRefreshView;
    HotListAdapter listAdapter;
    View viewBlankPart;

    SyncInfo info;
    List<HotModel> listHot;
    int margin;
    int selectedIndex;
    boolean isDataLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_main_hot, container, false);

        margin = (int) getResources().getDimension(R.dimen.margin_normal);

        info = new SyncInfo(getActivity());
        listHot = new ArrayList<HotModel>();

        viewBlankPart = (View) rootView.findViewById(R.id.view_blank_part);
        mRefreshView = (RefreshListView) rootView.findViewById(R.id.refreshView);
        mRefreshView.showFooter(true);
        listAdapter = new HotListAdapter(getActivity(), listItemClickListener);
        mRefreshView.setAdapter(listAdapter);
        mRefreshView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshHeader(View view) {
                reloadData();
            }
            @Override
            public void onRefreshFooter(View view) {
                loadData();
            }
        });

        new HotListAsync().execute();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    break;
            }
        }
    };

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
        selectedIndex = position;
        HotModel item = listHot.get(position);

        new IncreaseViewCountAsync().execute(String.valueOf(Constants.VIEW_CNT_KIND_HOT), "", String.valueOf(item.getId()));
        }
    };

    private void loadData() {
        if (isDataLoading)
            return;

        new HotListAsync().execute();
    }

    public void reloadData() {
        listHot.clear();
        loadData();
    }

    public class HotListAdapter extends BaseAdapter {

        public final String TAG = HotListAdapter.class.getName();

        private Context mContext;
        private View mRootView;
        private OnItemClickListener itemClickListener;

        public HotListAdapter(Context context, OnItemClickListener listener) {
            mContext = context;
            itemClickListener = listener;
        }

        @Override
        public int getCount() {
            return listHot.size();
        }

        @Override
        public Object getItem(int position) {
            return listHot.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final HotModel item = listHot.get(position);

            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot, parent, false);

            TextView txt_item_title =  (TextView)convertView.findViewById(R.id.txt_item_title);
            TextView txt_descript =  (TextView)convertView.findViewById(R.id.txt_descript);
            TextView txt_read_count =  (TextView)convertView.findViewById(R.id.txt_read_count);
            TextView txt_eval_count =  (TextView)convertView.findViewById(R.id.txt_eval_count);
            TextView txt_time =  (TextView)convertView.findViewById(R.id.txt_time);

            final HorizontalScrollView hs_view =  (HorizontalScrollView)convertView.findViewById(R.id.hs_view);
            final LinearLayout layout_images =  (LinearLayout)convertView.findViewById(R.id.layout_images);

            txt_item_title.setText(item.getTitle());
            txt_descript.setText(item.getSummary());
            txt_read_count.setText(String.valueOf(item.getVisitCnt()));
            txt_eval_count.setText(String.valueOf(item.getCommentCnt()));
            txt_time.setText(item.getWriteTime().getDateHourMinString());
            txt_time.setText(CommonUtils.getDateStrFromStrFormat(item.getWriteTimeString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));

            final List<String> imgList = item.getImgPaths();

            if (imgList != null && imgList.size() > 0) {
                layout_images.removeAllViews();

                int listImgWidth = (int) (ChengxinApplication.getScreenWidth() - margin * 4) / 3;
                int listImgHeight = (listImgWidth  * 3) / 4;
                for (int i = 0; i < imgList.size(); i++) {
                    ImageView imgView = new ImageView(mContext);
                    imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    int height = (int) (getResources().getDimension(R.dimen.image_hot_item_width));
                    int width = height * 3 / 2;
                    //int width = height;
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(width, height);
                    lparams.setMargins(0, 0, (int) (getResources().getDimension(R.dimen.margin_small)), 0);
                    imgView.setLayoutParams(lparams);

                    Picasso.with(mContext).load(Constants.FILE_ADDR + imgList.get(i)).placeholder(R.drawable.no_image).into(imgView);
                    layout_images.addView(imgView);
                }
            } else {
                hs_view.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onListItemClick(position, v);
                    }
                }
            });

            return convertView;
        }
    }

    class HotListAsync extends AsyncTask<String, String, HotListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDataLoading = true;
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected HotListModel doInBackground(String... strs) {
            return info.syncHotList(listHot.size(), Constants.PAGE_ITEM_COUNT);
        }
        @Override
        protected void onPostExecute(HotListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == Constants.ERROR_OK) {
                    listHot.addAll(result.getList());
                    listAdapter.notifyDataSetChanged();
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
            mRefreshView.onRefreshCompleteHeader();
            mRefreshView.onRefreshCompleteFooter();
            if (listHot.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            isDataLoading = false;
            Utils.disappearProgressDialog();
            mRefreshView.onRefreshCompleteHeader();
            mRefreshView.onRefreshCompleteFooter();
        }
    }

    class IncreaseViewCountAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncIncreaseViewCount(strs[0], strs[1], strs[2]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            Utils.disappearProgressDialog();
            if (result .isValid()) {
                if(result.getRetCode() == Constants.ERROR_OK) {
                    HotModel item = listHot.get(selectedIndex);
                    item.setVisitCnt(item.getVisitCnt() + 1);
                    listHot.set(selectedIndex, item);
                    listAdapter.notifyDataSetChanged();

                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("type", Constants.INDEX_HOT);
                    intent.putExtra("id", item.getId());
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishAndLoginActivityFromDuplicate(getActivity());
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Utils.disappearProgressDialog();
        }
    }
}

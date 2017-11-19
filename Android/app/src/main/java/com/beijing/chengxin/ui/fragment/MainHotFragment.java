package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.HotListModel;
import com.beijing.chengxin.network.model.HotModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class MainHotFragment extends Fragment {

	public final String TAG = MainHotFragment.class.getName();
    private View rootView;
    ListView listView;
    HotListAdapter listAdapter;

    SyncInfo info;
    List<HotModel> listHot;
    int listImgWidth, listImgHeight;
    final int marginRight = 8;
    int selectedIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_main_hot, container, false);

        info = new SyncInfo(getActivity());
        listHot = new ArrayList<HotModel>();

        listView = (ListView)rootView.findViewById(R.id.listView);
        listAdapter = new HotListAdapter(getActivity(), listItemClickListener);
        listView.setAdapter(listAdapter);

        new HotListAsync().execute();

        listImgWidth = 0;
        listImgHeight = 0;

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        listAdapter.notifyDataSetChanged();
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
            if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == Constants.TEST_STATUS_PASSED) {
                selectedIndex = position;
                HotModel item = listHot.get(position);

                new IncreaseViewCountAsync().execute(String.valueOf(Constants.VIEW_CNT_KIND_HOT), "", String.valueOf(item.getId()));
            }
        }
    };

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
            txt_descript.setText(item.getContent());
            txt_read_count.setText(String.valueOf(item.getVisitCnt()));
            txt_eval_count.setText(String.valueOf(item.getCommentCnt()));
            txt_time.setText(item.getWriteTimeString());

            final List<String> imgList = item.getImgPaths();
            if (imgList != null && imgList.size() > 0) {
                layout_images.removeAllViews();
                if (listImgWidth > 0 && listImgHeight > 0) {
                    for (int i = 0; i < imgList.size(); i++) {
                        ImageView imgView = new ImageView(mContext);
                        imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(listImgWidth, listImgHeight);
                        lparams.setMargins(0, 0, marginRight, 0);
                        imgView.setLayoutParams(lparams);
                        imgView.setBackgroundColor(getResources().getColor(R.color.color_gray_eb));

                        Picasso.with(mContext)
                                .load(Constants.FILE_ADDR + imgList.get(i))
                                .placeholder(R.drawable.no_image)
                                .into(imgView);
                        layout_images.addView(imgView);
                    }
                } else {
                    ViewTreeObserver viewTreeObserver = hs_view.getViewTreeObserver();
                    if (viewTreeObserver.isAlive()) {
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                hs_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                listImgWidth = (int) Math.ceil(hs_view.getMeasuredWidth() / 3.0f) - marginRight;
                                listImgHeight = hs_view.getMeasuredHeight();

                                for (int i = 0; i < imgList.size(); i++) {
                                    ImageView imgView = new ImageView(mContext);
                                    imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(listImgWidth, listImgHeight);
                                    lparams.setMargins(0, 0, marginRight, 0);
                                    imgView.setLayoutParams(lparams);
                                    imgView.setBackgroundColor(getResources().getColor(R.color.color_gray_eb));

                                    Picasso.with(mContext)
                                            .load(Constants.FILE_ADDR + imgList.get(i))
                                            .placeholder(R.drawable.no_image)
                                            .into(imgView);
                                    layout_images.addView(imgView);
                                }
                            }
                        });
                    }
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
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected HotListModel doInBackground(String... strs) {
            return info.syncHotList();
        }
        @Override
        protected void onPostExecute(HotListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    listHot.addAll(result.getList());
                    listAdapter.notifyDataSetChanged();
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
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    HotModel item = listHot.get(selectedIndex);
                    item.setVisitCnt(item.getVisitCnt() + 1);
                    listHot.set(selectedIndex, item);
                    listAdapter.notifyDataSetChanged();

                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("type", Constants.INDEX_HOT);
                    AppConfig.getInstance().currentHot = item;
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

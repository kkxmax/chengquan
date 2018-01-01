package com.beijing.chengxin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.FavouriteListModel;
import com.beijing.chengxin.network.model.FavouriteModel;
import com.beijing.chengxin.network.model.HotModel;
import com.beijing.chengxin.ui.listener.OnCallbackListener;
import com.beijing.chengxin.ui.widget.RefreshListView;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.beijing.chengxin.config.Constants.ERROR_DUPLICATE;
import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class MyCollectActivity extends ParentFragmentActivity {

    public final String TAG = MyCollectActivity.class.getName();
    //setContentView(R.layout.activity_my_collect);

    private static final int INDEX_GET_DATA_ALL = 0;
    private static final int INDEX_GET_DATA_COMEDITY = 1;
    private static final int INDEX_GET_DATA_HOT = 2;

    ToggleButton btnComedity, btnHot;

    ViewPager pager;
    MyPagerAdapter pagerAdapter;
    ListAdapter adapter1, adapter2;
    //PullToRefreshGridView refreshGridView;
    GridView gridView;
    RefreshListView listView;
    View viewBlankPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_collect);

        ((TextView) findViewById(R.id.txt_nav_title)).setText(getString(R.string.my_collect));
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        btnComedity = (ToggleButton) findViewById(R.id.btn_comedity);
        btnHot = (ToggleButton) findViewById(R.id.btn_hot);
        viewBlankPart = (View) findViewById(R.id.view_blank_part);

        btnComedity.setOnClickListener(mButtonClickListener);
        btnHot.setOnClickListener(mButtonClickListener);

//        refreshGridView = new PullToRefreshGridView(this);
//        gridView = refreshGridView.getRefreshableView();
        gridView = new GridView(this);
        gridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.margin_small));
        gridView.setVerticalSpacing((int) getResources().getDimension(R.dimen.margin_small));
        gridView.setBackgroundColor(getResources().getColor(R.color.color_gray_f5));
        gridView.setNumColumns(2);
        gridView.setGravity(Gravity.CENTER);
        adapter1 = new ListAdapter(this, 1);
        gridView.setAdapter(adapter1);
//        refreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//                reloadData(INDEX_GET_DATA_COMEDITY);
//            }
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
//                loadData(INDEX_GET_DATA_COMEDITY);
//            }
//        });

        listView = new RefreshListView(this);
        listView.showFooter(true);
        adapter2 = new ListAdapter(this, 2);
        listView.setDividerHeight(10);
        //listView.setDivider(getResources().getDrawable(R.color.color_gray_f5));
        //listView.setDivider(new ColorDrawable(Color.BLACK));
        //listView.setDivider(new ColorDrawable(Color.parseColor("#11000000")));
        listView.setBackgroundColor(getResources().getColor(R.color.color_gray_f5));
        listView.setAdapter(adapter2);
        listView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshHeader(View view) {
                reloadData(INDEX_GET_DATA_HOT);
            }
            @Override
            public void onRefreshFooter(View view) {
                loadData(INDEX_GET_DATA_HOT);
            }
        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(params);
        gridView.setLayoutParams(params);

        pagerAdapter = new MyPagerAdapter();
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                selectTopTabButton(position);
                updatePageSelected(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        reloadData(INDEX_GET_DATA_ALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_CODE_LOGIN_DUPLICATE)
            ChengxinApplication.finishActivityFromDuplicate(this);
    }

    private void reloadData(int get_index) {
        if (get_index == INDEX_GET_DATA_ALL) {
            adapter1.mDatas.clear();
            adapter2.mDatas.clear();
        }
        if (get_index == INDEX_GET_DATA_COMEDITY) {
            adapter1.mDatas.clear();
        }
        if (get_index == INDEX_GET_DATA_HOT) {
            adapter2.mDatas.clear();
        }
        getDataTask(get_index);
    }

    private void loadData(int get_index) {
        getDataTask(get_index);
    }

    private void updatePageSelected(int position) {
        if (position == 0) {
            if (adapter1.getData() == null || adapter1.getData().size() == 0) {
                viewBlankPart.setVisibility(View.VISIBLE);
            } else {
                viewBlankPart.setVisibility(View.GONE);
            }
        }
        if (position == 1) {
            if (adapter2.getData() == null || adapter2.getData().size() == 0) {
                viewBlankPart.setVisibility(View.VISIBLE);
            } else {
                viewBlankPart.setVisibility(View.GONE);
            }
        }
    }

    private void getDataTask(final int get_index) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(MyCollectActivity.this);
            }
            @Override
            protected Object doInBackground(Object... params) {
                if (get_index == INDEX_GET_DATA_COMEDITY) {
                    FavouriteListModel comedityDatas = new SyncInfo(MyCollectActivity.this).syncMyFavouriteList(adapter1.mDatas.size(), Constants.PAGE_ITEM_COUNT, 1);
                    publishProgress(1, comedityDatas);
                }
                if (get_index == INDEX_GET_DATA_HOT) {
                    FavouriteListModel hotDatas = new SyncInfo(MyCollectActivity.this).syncMyFavouriteList(adapter2.mDatas.size(), Constants.PAGE_ITEM_COUNT, 2);
                    publishProgress(2, hotDatas);
                }
                if (get_index == INDEX_GET_DATA_ALL) {
                    FavouriteListModel comedityDatas = new SyncInfo(MyCollectActivity.this).syncMyFavouriteList(adapter1.mDatas.size(), Constants.PAGE_ITEM_COUNT, 1);
                    publishProgress(1, comedityDatas);

                    FavouriteListModel hotDatas = new SyncInfo(MyCollectActivity.this).syncMyFavouriteList(adapter2.mDatas.size(), Constants.PAGE_ITEM_COUNT, 2);
                    publishProgress(2, hotDatas);
                }

                return null;
            }
            @Override
            protected void onProgressUpdate(Object... values) {
                int index = (int) values[0];
                BaseModel result = (BaseModel) values[1];
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        if (index == 1) {
                            adapter1.setData((ArrayList<FavouriteModel>) ((FavouriteListModel) result).getList());
                            adapter1.notifyDataSetChanged();
                            if (adapter1.mDatas.size() == 0) {
                                gridView.setVisibility(View.GONE);
                            } else {
                                gridView.setVisibility(View.VISIBLE);
                            }
                        }
                        if (index == 2) {
                            adapter2.setData((ArrayList<FavouriteModel>) ((FavouriteListModel) result).getList());
                            adapter2.notifyDataSetChanged();
                            if (adapter2.mDatas.size() == 0) {
                                listView.setVisibility(View.GONE);
                            } else {
                                listView.setVisibility(View.VISIBLE);
                            }
                        }
                    } else if (result.getRetCode() == ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(MyCollectActivity.this);
                    } else {
                        Toast.makeText(MyCollectActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyCollectActivity.this, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
                }
                if (btnComedity.isChecked()) {
                    btnComedity.callOnClick();
                    updatePageSelected(0);
                } else if (btnHot.isChecked()) {
                    btnHot.callOnClick();
                    updatePageSelected(1);
                }
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Utils.disappearProgressDialog();
                listView.onRefreshCompleteHeader();
                listView.onRefreshCompleteFooter();
//                refreshGridView.onRefreshComplete();;
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute();
    }

    private class ListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<FavouriteModel> mDatas = new ArrayList<FavouriteModel>();
        private int mFlag;

        public ListAdapter(Context context, int flag) {
            mContext = context;
            mFlag = flag;
        }

        public void setData(ArrayList<FavouriteModel> datas) {
            mDatas.addAll(datas);
        }

        public ArrayList<FavouriteModel> getData() {
            return mDatas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas == null ? 0 : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (mFlag == 1) {
                ComedityViewHolder holder = new ComedityViewHolder();

                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comedity, parent, false);

                    holder.imgLogo = (ImageView) convertView.findViewById(R.id.imgPhoto);
                    holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
                    holder.txtPrice = (TextView) convertView.findViewById(R.id.txt_price);
                    convertView.setTag(holder);
                } else {
                    holder = (ComedityViewHolder) convertView.getTag();
                }

                convertView.setId(position);

                final FavouriteModel item = (FavouriteModel) getItem(position);

                holder.id = item.getId();
                Picasso.with(mContext).load(Constants.FILE_ADDR + item.getProductImgPath1()).placeholder(R.drawable.no_image).into(holder.imgLogo);
                holder.txtName.setText(item.getProductName());
                holder.txtPrice.setText("￥ " + item.getProductPrice());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getProductStatus() == Constants.COMEDITY_STATUS_DONWLOADED) {
                            Toast.makeText(MyCollectActivity.this, R.string.msg_comedity_already_down, Toast.LENGTH_SHORT).show();
                            setNoFavoriteTask(1, item.getProductId(), new OnCallbackListener() {
                                @Override
                                public void onCallback() {
                                    adapter1.mDatas.remove(item);
                                    adapter1.notifyDataSetChanged();
                                }
                            });
                        } else {
                            Intent intent = new Intent(mContext, DetailActivity.class);
                            intent.putExtra("type", Constants.INDEX_COMEDITY);
                            intent.putExtra("id", item.getProductId());
                            startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                        }
                    }
                });

                return convertView;
            } else {
                HotViewHolder holder = new HotViewHolder();

                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot, parent, false);

                    holder.txtItemTitle = (TextView) convertView.findViewById(R.id.txt_item_title);
                    holder.txtDescript = (TextView) convertView.findViewById(R.id.txt_descript);
                    holder.txtReadCount = (TextView) convertView.findViewById(R.id.txt_read_count);
                    holder.txtEvalCount = (TextView) convertView.findViewById(R.id.txt_eval_count);
                    holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
                    holder.layoutImages = (LinearLayout) convertView.findViewById(R.id.layout_images);
                    convertView.setTag(holder);
                } else {
                    holder = (HotViewHolder) convertView.getTag();
                }

                convertView.setId(position);

                final FavouriteModel item = (FavouriteModel) getItem(position);

                holder.id = item.getHotId();
                holder.txtItemTitle.setText(item.getHotTitle());
                holder.txtDescript.setText(item.getHotSummary());
                holder.txtReadCount.setText("" + item.getHotVisitCnt());
                holder.txtEvalCount.setText("" + item.getHotCommentCnt());
                if (item.getHotWriteTime() != null)
                    holder.txtTime.setText(CommonUtils.getDateStrFromStrFormat(item.getHotWriteTimeString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
                else
                    holder.txtTime.setText("");

                ArrayList<String> imgList = item.getHotImgPaths();
                if (imgList != null && imgList.size() > 0) {
                    holder.layoutImages.removeAllViews();

                    int width = (ChengxinApplication.getScreenWidth() - getResources().getDimensionPixelSize(R.dimen.margin_normal) * 3) / 3;
                    int height = width * 2 / 3;

                    for (int i = 0; i < imgList.size(); i++) {
                        ImageView imgView = new ImageView(mContext);
                        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        //int height = (int) (getResources().getDimension(R.dimen.image_hot_item_width));
                        //int width = height * 3 / 2;
                        //int width = height;
                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(width, height);
                        lparams.setMargins(0, 0, (int) (getResources().getDimension(R.dimen.margin_small)), 0);
                        imgView.setLayoutParams(lparams);

                        Picasso.with(mContext).load(Constants.FILE_ADDR + imgList.get(i)).placeholder(R.drawable.no_image).into(imgView);
                        holder.layoutImages.addView(imgView);
                    }
                    holder.layoutImages.setVisibility(View.VISIBLE);

                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                    holder.layoutImages.setLayoutParams(lparams);
                } else {
                    holder.layoutImages.setVisibility(View.GONE);
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getHotStatus() == Constants.COMEDITY_STATUS_DONWLOADED) {
                            Toast.makeText(MyCollectActivity.this, R.string.msg_hot_already_down, Toast.LENGTH_SHORT).show();
                            setNoFavoriteTask(2, item.getHotId(), new OnCallbackListener() {
                                @Override
                                public void onCallback() {
                                    adapter2.mDatas.remove(item);
                                    adapter2.notifyDataSetChanged();
                                }
                            });
                        } else {
                            increaseViewCountTask("" + Constants.VIEW_CNT_KIND_HOT, "", position);
                        }
                    }
                });

                return convertView;
            }
        }

        private class ComedityViewHolder {
            int id;
            ImageView imgLogo;
            TextView txtName, txtPrice;
        }
        private class HotViewHolder {
            int id;
            TextView txtItemTitle, txtDescript, txtReadCount, txtEvalCount, txtTime;
            LinearLayout layoutImages;
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position == 0) {
                container.removeView(gridView);
            } else {
                container.removeView(listView);
            }
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 0) {
                ((ViewPager) container).addView(gridView, 0);
                return gridView;
            } else {
                ((ViewPager) container).addView(listView, 0);
                return listView;
            }
        }
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_comedity:
                    selectTopTabButton(0);
                    pager.setCurrentItem(0);
                    break;
                case R.id.btn_hot:
                    selectTopTabButton(1);
                    pager.setCurrentItem(1);
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

    private void selectTopTabButton(int position) {
        btnComedity.setChecked(false);
        btnHot.setChecked(false);
        switch (position) {
            case 0:
                btnComedity.setChecked(true);
                break;
            case 1:
                btnHot.setChecked(true);
                break;
        }
    }

    private void increaseViewCountTask(final String kind, final String accountId, final int position) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(mContext);
            }
            @Override
            protected BaseModel doInBackground(Object... params) {
                return new SyncInfo(mContext).syncIncreaseViewCount(kind, accountId, "" + adapter2.getData().get(position).getHotId());
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                BaseModel result = (BaseModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        FavouriteModel item = adapter2.getData().get(position);
                        item.setHotVisitCnt(item.getHotVisitCnt() + 1);
                        //adapter2.getData().set(position, item);
                        adapter2.notifyDataSetChanged();

                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("type", Constants.INDEX_HOT);
                        intent.putExtra("id", item.getHotId());
                        startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else if (result.getRetCode() == ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(MyCollectActivity.this);
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.err_server), Toast.LENGTH_LONG).show();
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

    private void setNoFavoriteTask(final int type_index, final int id, final OnCallbackListener listener) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(mContext);
            }
            @Override
            protected BaseModel doInBackground(Object... params) {
                return new SyncInfo(mContext).syncSetFavourite("" + id, "0", "" + type_index);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                BaseModel result = (BaseModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        listener.onCallback();
                    } else if (result.getRetCode() == ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(MyCollectActivity.this);
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.err_server), Toast.LENGTH_LONG).show();
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

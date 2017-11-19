package com.beijing.chengxin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.ErrorListModel;
import com.beijing.chengxin.network.model.ErrorModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class MyErrorCorrectActivity extends ParentFragmentActivity {

    ToggleButton btnProgress, btnSuccess, btnFail;

    ViewPager pager;
    MyPagerAdapter pagerAdapter;
    ListAdapter adapter1, adapter2, adapter3;
    ListView listView1, listView2, listView3;
    View viewBlankPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_error_correct);

        ((TextView) findViewById(R.id.txt_nav_title)).setText(getString(R.string.my_error_correct));
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        btnProgress = (ToggleButton) findViewById(R.id.btn_progress);
        btnSuccess = (ToggleButton) findViewById(R.id.btn_success);
        btnFail = (ToggleButton) findViewById(R.id.btn_fail);

        btnProgress.setOnClickListener(mButtonClickListener);
        btnSuccess.setOnClickListener(mButtonClickListener);
        btnFail.setOnClickListener(mButtonClickListener);

        viewBlankPart = (View) findViewById(R.id.view_blank_part);
        listView1 = new ListView(this);
        listView2 = new ListView(this);
        listView3 = new ListView(this);
        adapter1 = new ListAdapter(this);
        adapter2 = new ListAdapter(this);
        adapter3 = new ListAdapter(this);
        listView1.setDividerHeight(0);
        listView2.setDividerHeight(0);
        listView3.setDividerHeight(0);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listView1.setLayoutParams(params);
        listView2.setLayoutParams(params);
        listView3.setLayoutParams(params);

        pagerAdapter = new MyPagerAdapter();
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                selectTabButton(position);
                updatePageSelected(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        getDataTask();
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
        if (position == 2) {
            if (adapter3.getData() == null || adapter3.getData().size() == 0) {
                viewBlankPart.setVisibility(View.VISIBLE);
            } else {
                viewBlankPart.setVisibility(View.GONE);
            }
        }
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_progress:
                    selectTabButton(0);
                    pager.setCurrentItem(0);
                    break;
                case R.id.btn_success:
                    selectTabButton(1);
                    pager.setCurrentItem(1);
                    break;
                case R.id.btn_fail:
                    selectTabButton(2);
                    pager.setCurrentItem(2);
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

    private void getDataTask() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(MyErrorCorrectActivity.this);
            }
            @Override
            protected Object doInBackground(Object... params) {
                ErrorListModel progressDatas = new SyncInfo(MyErrorCorrectActivity.this).syncErrorList(1);
                publishProgress(1, progressDatas);

                ErrorListModel successDatas = new SyncInfo(MyErrorCorrectActivity.this).syncErrorList(2);
                publishProgress(2, successDatas);

                ErrorListModel failDatas = new SyncInfo(MyErrorCorrectActivity.this).syncErrorList(3);
                publishProgress(3, failDatas);

                return null;
            }
            @Override
            protected void onProgressUpdate(Object... values) {
                int status = (int) values[0];
                ErrorListModel result = (ErrorListModel) values[1];
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        if (status == 1) {
                            if (result.getList() == null || result.getList().size() == 0) {
                                listView1.setVisibility(View.GONE);
                            } else {
                                listView1.setVisibility(View.VISIBLE);
                            }
                            adapter1.setData(result.getList(), 1);
                            adapter1.notifyDataSetChanged();
                        }
                        if (status == 2) {
                            if (result.getList() == null || result.getList().size() == 0) {
                                listView2.setVisibility(View.GONE);
                            } else {
                                listView2.setVisibility(View.VISIBLE);
                            }
                            adapter2.setData(result.getList(), 2);
                            adapter2.notifyDataSetChanged();
                        }
                        if (status == 3) {
                            if (result.getList() == null || result.getList().size() == 0) {
                                listView3.setVisibility(View.GONE);
                            } else {
                                listView3.setVisibility(View.VISIBLE);
                            }
                            adapter3.setData(result.getList(), 3);
                            adapter3.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(MyErrorCorrectActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyErrorCorrectActivity.this, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
                }
                if (btnProgress.isChecked()) {
                    btnProgress.callOnClick();
                    updatePageSelected(0);
                } else if (btnSuccess.isChecked()) {
                    btnSuccess.callOnClick();
                    updatePageSelected(1);
                } else if (btnFail.isChecked()) {
                    btnFail.callOnClick();
                    updatePageSelected(2);
                }
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute();
    }

    public class MyPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ListView itemView = listView1;
            switch (position) {
                case 0:
                    itemView = listView1;
                    break;
                case 1:
                    itemView = listView2;
                    break;
                case 2:
                    itemView = listView3;
                    break;
            }
            container.removeView(itemView);
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ListView itemView = listView1;
            switch (position) {
                case 0:
                    itemView = listView1;
                    break;
                case 1:
                    itemView = listView2;
                    break;
                case 2:
                    itemView = listView3;
                    break;
            }
            ((ViewPager) container).addView(itemView, 0);
            return itemView;
        }
    }

    public class ListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<ErrorModel> mDatas;
        private int mStatus;

        public ListAdapter(Context context) {
            mContext = context;
        }

        public void setData(ArrayList<ErrorModel> datas, int status) {
            mDatas = datas;
            mStatus = status;
        }

        public ArrayList<ErrorModel> getData() {
            return mDatas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas == null ? null : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_error_correct, parent, false);

                holder.imgLogo = (ImageView) convertView.findViewById(R.id.img_logo);
                holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
                holder.txtMark = (TextView) convertView.findViewById(R.id.txt_mark);
                holder.txtContent = (TextView) convertView.findViewById(R.id.txt_content);
                holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            convertView.setId(position);

            UserModel user = SessionInstance.getInstance().getLoginData().getUser();
            final ErrorModel item = (ErrorModel) getItem(position);
            Picasso.with(MyErrorCorrectActivity.this)
                    .load(Constants.FILE_ADDR + user.getLogo())
                    .placeholder(R.drawable.no_image).into(holder.imgLogo);
            holder.txtName.setText(user.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? user.getRealname() : user.getEnterName());
            holder.txtContent.setText(getString(R.string.error_evidence) + " " + item.getEstimateContent());
            holder.txtTime.setText(item.getWriteTimeString());
            if (mStatus == 2) {
                holder.txtMark.setVisibility(View.VISIBLE);
            } else {
                holder.txtMark.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyErrorCorrectActivity.this, MyErrorCorrectDetailActivity.class);
                    intent.putExtra("data", item);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            return convertView;
        }

        public class ViewHolder {
            int id;
            ImageView imgLogo;
            TextView txtName;
            TextView txtMark;
            TextView txtContent;
            TextView txtTime;
        }
    }

    private void selectTabButton(int position) {
        btnProgress.setChecked(false);
        btnSuccess.setChecked(false);
        btnFail.setChecked(false);
        switch (position) {
            case 0:
                btnProgress.setChecked(true);
                break;
            case 1:
                btnSuccess.setChecked(true);
                break;
            case 2:
                btnFail.setChecked(true);
                break;
        }
    }

}

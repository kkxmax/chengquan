package com.beijing.chengxin.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.EvalListModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.ui.widget.RefreshListView;
import com.beijing.chengxin.ui.widget.SimpleSortSpinnerEval;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class EvalMeActivity extends ParentFragmentActivity {
    public final String TAG = EvalMeActivity.class.getName();

    private static final int INDEX_GET_DATA_ALL = 0;
    private static final int INDEX_GET_DATA_PERSON = 1;
    private static final int INDEX_GET_DATA_COMPANY = 2;

    ToggleButton btnPerson, btnEnterprise;
    SimpleSortSpinnerEval btnSort;

    View mViewBlankPart;
    RefreshListView mRefreshView1, mRefreshView2;
    ItemDetailAdapter mAdapter1, mAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_eval);

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.eval_me));

        btnPerson = (ToggleButton)findViewById(R.id.btn_person);
        btnEnterprise = (ToggleButton)findViewById(R.id.btn_enterprise);
        btnSort = (SimpleSortSpinnerEval) findViewById(R.id.btn_sort_set);
        btnPerson.setOnClickListener(mButtonClickListener);
        btnEnterprise.setOnClickListener(mButtonClickListener);
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        findViewById(R.id.btn_write).setVisibility(View.GONE);

        mViewBlankPart = (View) findViewById(R.id.view_blank_part);

        mRefreshView1 = (RefreshListView) findViewById(R.id.refreshView1);
        mRefreshView1.showFooter(true);
        mAdapter1 = new ItemDetailAdapter();
        mRefreshView1.setAdapter(mAdapter1);
        mRefreshView1.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshHeader(View view) {
                reloadData(INDEX_GET_DATA_PERSON);
            }
            @Override
            public void onRefreshFooter(View view) {
                loadData(INDEX_GET_DATA_PERSON);
            }
        });

        mRefreshView2 = (RefreshListView) findViewById(R.id.refreshView2);
        mRefreshView2.showFooter(true);
        mAdapter2 = new ItemDetailAdapter();
        mRefreshView2.setAdapter(mAdapter2);
        mRefreshView2.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshHeader(View view) {
                reloadData(INDEX_GET_DATA_COMPANY);
            }
            @Override
            public void onRefreshFooter(View view) {
                loadData(INDEX_GET_DATA_COMPANY);
            }
        });

        btnSort.setOnItemSelectListener(new SimpleSortSpinnerEval.OnItemSelectListener() {
            @Override
            public void onItemSelected(int index) {
                reloadData(INDEX_GET_DATA_ALL);
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
            mAdapter1.mDatas.clear();
            mAdapter2.mDatas.clear();
        }
        if (get_index == INDEX_GET_DATA_PERSON) {
            mAdapter1.mDatas.clear();
        }
        if (get_index == INDEX_GET_DATA_COMPANY) {
            mAdapter2.mDatas.clear();
        }
        new EvalAsync().execute(get_index, btnSort.getSelection());
    }

    private void loadData(int get_index) {
        new EvalAsync().execute(get_index, btnSort.getSelection());
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_person:
                    mRefreshView1.setVisibility(View.VISIBLE);
                    mRefreshView2.setVisibility(View.GONE);
                    btnPerson.setChecked(true);
                    btnEnterprise.setChecked(false);
                    mRefreshView1.setVisibility((mAdapter1.getCount() == 0) ? View.GONE : View.VISIBLE);
                    break;
                case R.id.btn_enterprise:
                    mRefreshView1.setVisibility(View.GONE);
                    mRefreshView2.setVisibility(View.VISIBLE);
                    btnPerson.setChecked(false);
                    btnEnterprise.setChecked(true);
                    mRefreshView2.setVisibility((mAdapter2.getCount() == 0) ? View.GONE : View.VISIBLE);
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

    public class ItemDetailAdapter extends BaseAdapter {
        private ArrayList<EvalModel> mDatas = new ArrayList<EvalModel>();

        public void setData(ArrayList<EvalModel> datas) {
            this.mDatas.addAll(datas);
        }

        @Override
        public int getCount() {
            return (mDatas == null) ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return (mDatas == null) ? null : mDatas.get(position);
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

            final EvalModel item = (EvalModel) mDatas.get(position);

            Picasso.with(EvalMeActivity.this)
                    .load(Constants.FILE_ADDR + item.getOwnerLogo())
                    .placeholder(item.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.drawable.no_image_person_center : R.drawable.no_image_item)
                    .into(viewHolder.imgAvatar);
            viewHolder.txtItemType.setText(item.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.string.str_person : R.string.str_enterprise);
            viewHolder.txtName.setText(item.getOwnerAkind() == 1 ? item.getOwnerRealname() : item.getOwnerEnterName());
            viewHolder.txtEvalType.setText(item.getKindName());
            viewHolder.txtEvalContent.setText(item.getContent());
            viewHolder.txtTime.setText(item.getWriteTimeString());
            viewHolder.txtLikeCount.setText("" + item.getElectCnt());
            viewHolder.txtEvalCount.setText("" + item.getTargetAccountFeedbackCnt());

            final List<String> imgList = item.getImgPaths();
            viewHolder.hsView.setVisibility(View.VISIBLE);
            if (imgList != null && imgList.size() > 0) {
                viewHolder.layoutImages.removeAllViews();
                for (int i = 0; i < imgList.size(); i++) {
                    ImageView imgView = new ImageView(EvalMeActivity.this);
                    imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    int height = (int) (getResources().getDimension(R.dimen.image_hot_item_width));
                    int width = height * 3 / 2;
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(width, height);
                    lparams.setMargins(0, 0, (int) (getResources().getDimension(R.dimen.margin_small)), 0);
                    imgView.setLayoutParams(lparams);

                    Picasso.with(EvalMeActivity.this)
                            .load(Constants.FILE_ADDR + imgList.get(i))
                            .placeholder(R.drawable.no_image)
                            .into(imgView);
                    viewHolder.layoutImages.addView(imgView);
                }
            } else {
                viewHolder.hsView.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EvalMeActivity.this, DetailActivity.class);
                    intent.putExtra("id", item.getOwner());
                    intent.putExtra("type", Constants.INDEX_ENTERPRISE);
                    intent.putExtra("akind", item.getOwnerAkind());
                    startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    class EvalAsync extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(EvalMeActivity.this);
        }
        @Override
        protected EvalListModel doInBackground(Object... strs) {
            int get_index = (int) strs[0];
            int kind = (int) strs[1];
            if (get_index == INDEX_GET_DATA_PERSON) {
                EvalListModel personDatas = new SyncInfo(EvalMeActivity.this).syncEvalToMeList(mAdapter1.mDatas.size(), Constants.PAGE_ITEM_COUNT, 1, kind);
                publishProgress(1, personDatas);
            }

            if (get_index == INDEX_GET_DATA_COMPANY) {
                EvalListModel enterpriseDatas = new SyncInfo(EvalMeActivity.this).syncEvalToMeList(mAdapter2.mDatas.size(), Constants.PAGE_ITEM_COUNT, 2, kind);
                publishProgress(2, enterpriseDatas);
            }

            if (get_index == INDEX_GET_DATA_ALL) {
                EvalListModel personDatas = new SyncInfo(EvalMeActivity.this).syncEvalToMeList(mAdapter1.mDatas.size(), Constants.PAGE_ITEM_COUNT, 1, kind);
                publishProgress(1, personDatas);

                EvalListModel enterpriseDatas = new SyncInfo(EvalMeActivity.this).syncEvalToMeList(mAdapter2.mDatas.size(), Constants.PAGE_ITEM_COUNT, 2, kind);
                publishProgress(2, enterpriseDatas);
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Object... values) {
            int akind = (int) values[0];
            EvalListModel result = (EvalListModel) values[1];
            if (result.isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    if (akind == 1) {
                        mAdapter1.setData(result.getList());
                        mAdapter1.notifyDataSetChanged();
                        if (mAdapter1.mDatas.size() == 0) {
                            mRefreshView1.setVisibility(View.GONE);
                        } else {
                            mRefreshView1.setVisibility(View.VISIBLE);
                        }
                    }
                    if (akind == 2) {
                        mAdapter2.setData(result.getList());
                        mAdapter2.notifyDataSetChanged();
                        if (mAdapter2.mDatas.size() == 0) {
                            mRefreshView2.setVisibility(View.GONE);
                        } else {
                            mRefreshView2.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(EvalMeActivity.this);
                } else {
                    Toast.makeText(EvalMeActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EvalMeActivity.this, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
            }
            if (btnPerson.isChecked()) {
                btnPerson.callOnClick();
            } else {
                btnEnterprise.callOnClick();
            }
        }
        @Override
        protected void onPostExecute(Object result) {
            Utils.disappearProgressDialog();
            mRefreshView1.onRefreshCompleteHeader();
            mRefreshView1.onRefreshCompleteFooter();
            mRefreshView2.onRefreshCompleteHeader();
            mRefreshView2.onRefreshCompleteFooter();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Utils.disappearProgressDialog();
            mRefreshView1.onRefreshCompleteHeader();
            mRefreshView1.onRefreshCompleteFooter();
            mRefreshView2.onRefreshCompleteHeader();
            mRefreshView2.onRefreshCompleteFooter();
        }
    }


}

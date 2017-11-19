package com.beijing.chengxin.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.EvalListModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.ui.widget.SimpleSortSpinnerEval;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class MyEvalActivity extends ParentFragmentActivity {

    public final String TAG = MyEvalActivity.class.getName();

    ToggleButton btnPerson, btnEnterprise;
    SimpleSortSpinnerEval btnSort;

    View mViewBlankPart;
    RecyclerView mRecyclerView1, mRecyclerView2;
    ItemDetailAdapter mAdapter1, mAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_eval);

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.my_eval));

        btnPerson = (ToggleButton)findViewById(R.id.btn_person);
        btnEnterprise = (ToggleButton)findViewById(R.id.btn_enterprise);
        btnSort = (SimpleSortSpinnerEval) findViewById(R.id.btn_sort_set);
        btnPerson.setOnClickListener(mButtonClickListener);
        btnEnterprise.setOnClickListener(mButtonClickListener);
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        findViewById(R.id.btn_write).setOnClickListener(mButtonClickListener);

        mViewBlankPart = (View) findViewById(R.id.view_blank_part);

        mRecyclerView1 = (RecyclerView)findViewById(R.id.recyclerView1);
        mAdapter1 = new ItemDetailAdapter();
        mRecyclerView1.setAdapter(mAdapter1);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView1.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 8;
            }
        });

        mRecyclerView2 = (RecyclerView)findViewById(R.id.recyclerView2);
        mAdapter2 = new ItemDetailAdapter();
        mRecyclerView2.setAdapter(mAdapter2);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView2.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 8;
            }
        });

        btnSort.setOnItemSelectListener(new SimpleSortSpinnerEval.OnItemSelectListener() {
            @Override
            public void onItemSelected(int index) {
                new EvalAsync().execute(btnSort.getSelection());
            }
        });

        new EvalAsync().execute(btnSort.getSelection());
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_person:
                    mRecyclerView1.setVisibility(View.VISIBLE);
                    mRecyclerView2.setVisibility(View.GONE);
                    btnPerson.setChecked(true);
                    btnEnterprise.setChecked(false);
                    mRecyclerView1.setVisibility((mAdapter1.getItemCount() == 0) ? View.GONE : View.VISIBLE);
                    break;
                case R.id.btn_enterprise:
                    mRecyclerView1.setVisibility(View.GONE);
                    mRecyclerView2.setVisibility(View.VISIBLE);
                    btnPerson.setChecked(false);
                    btnEnterprise.setChecked(true);
                    mRecyclerView2.setVisibility((mAdapter2.getItemCount() == 0) ? View.GONE : View.VISIBLE);
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
                case R.id.btn_write:
                    Intent intent = new Intent(MyEvalActivity.this, MakeEvaluationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        }
    };

    public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder> {
        private ArrayList<EvalModel> mDatas;

        public void setData(ArrayList<EvalModel> datas) {
            this.mDatas = datas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_eval, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            final EvalModel item = (EvalModel) mDatas.get(position);

            Picasso.with(MyEvalActivity.this)
                    .load(Constants.FILE_ADDR + item.getTargetAccountLogo())
                    .placeholder(R.drawable.no_image)
                    .into(viewHolder.imgAvatar);
            viewHolder.txtName.setText(item.getTargetAccountAkind() == 1 ? item.getTargetAccountRealname() : item.getTargetAccountEnterName());
            viewHolder.txtEvalType.setText(item.getKindName());
            viewHolder.txtEvalContent.setText(getResources().getString(R.string.make_eval_content) + item.getContent());
            viewHolder.txtTime.setText(item.getWriteTimeString());
            viewHolder.txtLikeCount.setText("" + item.getTargetAccountElectCnt());
            viewHolder.txtEvalCount.setText("" + item.getTargetAccountFeedbackCnt());

            final List<String> imgList = item.getImgPaths();
            if (imgList != null && imgList.size() > 0) {
                viewHolder.layoutImages.removeAllViews();
                for (int i = 0; i < imgList.size(); i++) {
                    ImageView imgView = new ImageView(MyEvalActivity.this);
                    imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    int height = (int) (getResources().getDimension(R.dimen.image_hot_item_width));
                    int width = height * 3 / 2;
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(width, height);
                    lparams.setMargins(0, 0, (int) (getResources().getDimension(R.dimen.margin_small)), 0);
                    imgView.setLayoutParams(lparams);

                    Picasso.with(MyEvalActivity.this)
                            .load(Constants.FILE_ADDR + imgList.get(i))
                            .placeholder(R.drawable.no_image)
                            .into(imgView);
                    viewHolder.layoutImages.addView(imgView);
                }
                viewHolder.hsView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.hsView.setVisibility(View.GONE);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyEvalActivity.this, DetailActivity.class);
                    intent.putExtra("id", item.getAccountId());
                    intent.putExtra("type", Constants.INDEX_ENTERPRISE);
                    intent.putExtra("akind", item.getTargetAccountAkind());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public int getItemViewType(int position) {
            return 0;//mMessages.get(position).getType();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgAvatar;
            TextView txtName;
            TextView txtEvalType;
            TextView txtEvalContent;
            HorizontalScrollView hsView;
            LinearLayout layoutImages;
            TextView txtTime;
            TextView txtLikeCount;
            TextView txtEvalCount;

            public ViewHolder(View itemView) {
                super(itemView);
                hsView = (HorizontalScrollView) itemView.findViewById(R.id.hs_view);
                layoutImages = (LinearLayout) itemView.findViewById(R.id.layout_images);
                imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
                txtName = (TextView) itemView.findViewById(R.id.txt_name);
                txtEvalType = (TextView) itemView.findViewById(R.id.txt_eval_type);
                txtEvalContent = (TextView) itemView.findViewById(R.id.txt_eval_content);
                txtTime = (TextView) itemView.findViewById(R.id.txt_time);
                txtLikeCount = (TextView) itemView.findViewById(R.id.txt_like_count);
                txtEvalCount = (TextView) itemView.findViewById(R.id.txt_eval_count);
            }
        }
    }

    class EvalAsync extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(MyEvalActivity.this);
        }
        @Override
        protected EvalListModel doInBackground(Object... strs) {
            int kind = (int) strs[0];
            EvalListModel personDatas = new SyncInfo(MyEvalActivity.this).syncEvalList(1, kind);
            publishProgress(1, personDatas);

            EvalListModel enterpriseDatas = new SyncInfo(MyEvalActivity.this).syncEvalList(2, kind);
            publishProgress(2, enterpriseDatas);
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
                        if (result.getList() == null || result.getList().size() == 0) {
                            mRecyclerView1.setVisibility(View.GONE);
                        } else {
                            mRecyclerView1.setVisibility(View.VISIBLE);
                        }
                    }
                    if (akind == 2) {
                        mAdapter2.setData(result.getList());
                        mAdapter2.notifyDataSetChanged();
                        if (result.getList() == null || result.getList().size() == 0) {
                            mRecyclerView2.setVisibility(View.GONE);
                        } else {
                            mRecyclerView2.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(MyEvalActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MyEvalActivity.this, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
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
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Utils.disappearProgressDialog();
        }
    }

}

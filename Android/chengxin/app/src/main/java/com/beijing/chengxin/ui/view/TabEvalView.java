package com.beijing.chengxin.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.ui.activity.MakeErrorCorrectActivity;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.EvalDetailFragment;
import com.beijing.chengxin.ui.listener.OnViewSizeChangeListener;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class TabEvalView extends BaseView implements View.OnClickListener {

    LinearLayout viewBodyPart, viewBlankPart;
    ToggleButton btnAllCnt, btnFrontCnt, btnBackCnt;
    LinearLayout listBody;

    public ArrayList<TextView> txtEvalContentViewList;
    public ArrayList<TextView> txtRemarkFirstViewList;

    private OnViewSizeChangeListener listener;
    private List<EvalModel> mDatas;
    private List<EvalModel> mFrontDatas;
    private List<EvalModel> mBackDatas;

    public TabEvalView(Context context) {
        super(context);
        initialize();
    }

    public void setData(List<EvalModel> datas) {
        this.mDatas = datas;
        initData();
    }

    public void setOnViewSizeChangeListener(OnViewSizeChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.view_tab_eval);

        viewBodyPart = (LinearLayout) findViewById(R.id.view_body_part);
        viewBlankPart = (LinearLayout) findViewById(R.id.view_blank_part);
        btnAllCnt = (ToggleButton) findViewById(R.id.btn_all_cnt);
        btnFrontCnt = (ToggleButton) findViewById(R.id.btn_front_cnt);
        btnBackCnt = (ToggleButton) findViewById(R.id.btn_back_cnt);

        btnAllCnt.setOnClickListener(this);
        btnFrontCnt.setOnClickListener(this);
        btnBackCnt.setOnClickListener(this);

        listBody = (LinearLayout) findViewById(R.id.list_body);
    }

    @Override
    protected void initData() {
        if (txtEvalContentViewList != null) {
            txtEvalContentViewList.clear();
            txtEvalContentViewList = null;
        }
        txtEvalContentViewList = new ArrayList<TextView>();
        if (txtRemarkFirstViewList != null) {
            txtRemarkFirstViewList.clear();
            txtRemarkFirstViewList = null;
        }
        txtRemarkFirstViewList = new ArrayList<TextView>();

        if (mDatas != null && mDatas.size() > 0) {
            mFrontDatas = new ArrayList<EvalModel>();
            mBackDatas = new ArrayList<EvalModel>();
            for (int i = 0; i < mDatas.size(); i++) {
                if (mDatas.get(i).getKind() == Constants.ESTIMATE_KIND_FORWORD)
                    mFrontDatas.add(mDatas.get(i));
                else
                    mBackDatas.add(mDatas.get(i));
            }
            btnAllCnt.setTextOn(String.format("%s(%d)", getString(R.string.all), mDatas.size()));
            btnAllCnt.setTextOff(String.format("%s(%d)", getString(R.string.all), mDatas.size()));
            btnFrontCnt.setTextOn(String.format("%s(%d)", getString(R.string.front_eval), mFrontDatas.size()));
            btnFrontCnt.setTextOff(String.format("%s(%d)", getString(R.string.front_eval), mFrontDatas.size()));
            btnBackCnt.setTextOn(String.format("%s(%d)", getString(R.string.back_eval), mBackDatas.size()));
            btnBackCnt.setTextOff(String.format("%s(%d)", getString(R.string.back_eval), mBackDatas.size()));
            btnAllCnt.setChecked(btnAllCnt.isChecked());
            btnFrontCnt.setChecked(btnFrontCnt.isChecked());
            btnBackCnt.setChecked(btnBackCnt.isChecked());

            if (btnAllCnt.isChecked()) {
                listBody.removeAllViews();
                viewBodyPart.setVisibility(View.VISIBLE);
                viewBlankPart.setVisibility(View.GONE);
                for (int i = 0; i < mDatas.size(); i++) {
                    LinearLayout itemView = (LinearLayout) getEvalItemView(mDatas.get(i));
                    listBody.addView(itemView);
                }
            } else if (btnFrontCnt.isChecked()) {
                listBody.removeAllViews();
                if (mFrontDatas.size() > 0) {
                    viewBodyPart.setVisibility(View.VISIBLE);
                    viewBlankPart.setVisibility(View.GONE);
                    for (int i = 0; i < mFrontDatas.size(); i++) {
                        LinearLayout itemView = (LinearLayout) getEvalItemView(mFrontDatas.get(i));
                        listBody.addView(itemView);
                    }
                }
            } else if (btnBackCnt.isChecked()) {
                listBody.removeAllViews();
                if (mBackDatas.size() > 0) {
                    viewBodyPart.setVisibility(View.VISIBLE);
                    viewBlankPart.setVisibility(View.GONE);
                    for (int i = 0; i < mBackDatas.size(); i++) {
                        LinearLayout itemView = (LinearLayout) getEvalItemView(mBackDatas.get(i));
                        listBody.addView(itemView);
                    }
                }
            }
        } else {
            viewBodyPart.setVisibility(View.GONE);
            viewBlankPart.setVisibility(View.VISIBLE);
        }
        if (listener != null)
            listener.onChangedViewSize();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_all_cnt:
                btnAllCnt.setChecked(true);
                btnFrontCnt.setChecked(false);
                btnBackCnt.setChecked(false);
                initData();
                break;
            case R.id.btn_front_cnt:
                btnAllCnt.setChecked(false);
                btnFrontCnt.setChecked(true);
                btnBackCnt.setChecked(false);
                initData();
                break;
            case R.id.btn_back_cnt:
                btnAllCnt.setChecked(false);
                btnFrontCnt.setChecked(false);
                btnBackCnt.setChecked(true);
                initData();
                break;
            case R.id.txt_city_shenzhen:
                break;
        }
    }

    private View getEvalItemView(final EvalModel obj) {
        View itemView = mActivity.getLayoutInflater().inflate(R.layout.view_tab_eval_item, null);

        ImageView imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
        TextView txtAvatarName = (TextView) itemView.findViewById(R.id.txt_avatar_name);
        TextView txtEvalType = (TextView) itemView.findViewById(R.id.txt_eval_type);
        TextView txtEvalContent = (TextView) itemView.findViewById(R.id.txt_eval_content);
        HorizontalScrollView hscrollView = (HorizontalScrollView) itemView.findViewById(R.id.scroll_img);
        LinearLayout layout_images = (LinearLayout) itemView.findViewById(R.id.layout_images);
        TextView txtDate= (TextView) itemView.findViewById(R.id.txt_date);
        final TextView txtElectCnt = (TextView) itemView.findViewById(R.id.txt_elect_cnt);
        TextView txtEvaluate = (TextView) itemView.findViewById(R.id.txt_evaluate);
        TextView txtError = (TextView) itemView.findViewById(R.id.txt_error);
        LinearLayout layoutRemark = (LinearLayout)itemView.findViewById(R.id.layout_remark);
        TextView txtRemarkFirst = (TextView) itemView.findViewById(R.id.txt_remark_first);
        final TextView txtRemarkMore = (TextView) itemView.findViewById(R.id.txt_remark_more);
        final LinearLayout viewRemarkBody = (LinearLayout) itemView.findViewById(R.id.view_remark_body);
        ImageView imgMark = (ImageView) itemView.findViewById(R.id.img_mark);

        Picasso.with(mActivity)
                .load(Constants.FILE_ADDR + obj.getOwnerLogo())
                .placeholder(obj.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.drawable.no_image_person_center : R.drawable.no_image_item)
                .into(imgAvatar);
        if (obj.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON)
            txtAvatarName.setText(obj.getOwnerRealname());
        else
            txtAvatarName.setText(obj.getOwnerEnterName());
        txtEvalType.setText(obj.getKindName());
        txtEvalContent.setText(obj.getContent());
        txtDate.setText(CommonUtils.getDateStrFromStrFormat(obj.getWriteTimeString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
        txtElectCnt.setText(String.format("%d", obj.getElectCnt()));
        txtEvaluate.setText(String.format("%d", obj.getReplys().size()));
        if (obj.getIsElectedByMe() == 1)
            txtElectCnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_sel), null, null, null);
        else
            txtElectCnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_nor), null, null, null);

        if (obj.getIsFalse() == 1)
            imgMark.setVisibility(View.VISIBLE);
        else
            imgMark.setVisibility(View.GONE);

        txtElectCnt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(obj.getIsElectedByMe() == 1) {
                    obj.setIsElectedByMe(0);
                    obj.setElectCnt(obj.getElectCnt() - 1);
                    txtElectCnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_nor), null, null, null);
                    txtElectCnt.setText(String.format("%d", obj.getElectCnt()));
                }
                else {
                    obj.setIsElectedByMe(1);
                    obj.setElectCnt(obj.getElectCnt() + 1);
                    txtElectCnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_sel), null, null, null);
                    txtElectCnt.setText(String.format("%d", obj.getElectCnt()));
                }
                new AsyncTask<String, String, BaseModel>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        Utils.displayProgressDialog(mActivity);
                    }
                    @Override
                    protected BaseModel doInBackground(String... strs) {
                        return new SyncInfo(mActivity).syncElect(strs[0], strs[1]);
                    }
                    @Override
                    protected void onPostExecute(BaseModel result) {
                        super.onPostExecute(result);
                        if (result .isValid()) {
                            if(result.getRetCode() == ERROR_OK) {
//                                Toast.makeText(mActivity, "chenggong", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mActivity, result.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(mActivity, getString(R.string.err_server), Toast.LENGTH_LONG).show();
                        }
                        Utils.disappearProgressDialog();
                    }
                    @Override
                    protected void onCancelled() {
                        super.onCancelled();
                        Utils.disappearProgressDialog();
                    }
                }.execute(String.valueOf(obj.getId()), String.valueOf(obj.getIsElectedByMe()));
            }
        });

        if (obj.getOwner() == SessionInstance.getInstance().getLoginData().getUser().getId()) {
            txtError.setVisibility(View.GONE);
        } else {
            txtError.setVisibility(View.VISIBLE);
            txtError.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == Constants.TEST_STATUS_PASSED) {
                        Intent intent = new Intent(getContext(), MakeErrorCorrectActivity.class);
                        intent.putExtra("estimateId", obj.getId());
                        intent.putExtra("pname", obj.getTargetAccountAkind() == Constants.ACCOUNT_TYPE_PERSON ? obj.getTargetAccountRealname() : obj.getTargetAccountEnterName());
                        intent.putExtra("nname", obj.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON ? obj.getOwnerRealname() : obj.getOwnerEnterName());
                        intent.putExtra("ncontent", obj.getContent());
                        getContext().startActivity(intent);
                        ((Activity) getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        CommonUtils.showRealnameCertAlert(mActivity);
                    }
                }
            });
        }

        if (obj.getImgPaths().size() > 0) {
            List<String>imgList = obj.getImgPaths();
            int listImgWidth = (int) (getResources().getDisplayMetrics().density * 120);;
            int listImgHeight = (int) (getResources().getDisplayMetrics().density * 90);;
            for (int i = 0; i < imgList.size(); i++) {
                ImageView imgView = new ImageView(mActivity);
                imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(listImgWidth, listImgHeight);
                lparams.setMargins(0, 0, 8, 0);
                imgView.setLayoutParams(lparams);

                Picasso.with(mActivity)
                        .load(Constants.FILE_ADDR + imgList.get(i))
                        .placeholder(R.drawable.no_image)
                        .into(imgView);
                layout_images.addView(imgView);
            }
        } else
            hscrollView.setVisibility(View.GONE);

        List<EvalModel> replys = obj.getReplys();
        if (replys.size() == 0) {
            layoutRemark.setVisibility(GONE);
        } else {
            layoutRemark.setVisibility(VISIBLE);
            txtRemarkFirst.setText(replys.get(0).getContent());
            if (replys.size() > 0) {
                txtRemarkMore.setVisibility(View.VISIBLE);
                viewRemarkBody.setVisibility(View.GONE);
                txtRemarkMore.setText(String.format("查看全部%d条回复 >", replys.size()));
            } else {
                txtRemarkMore.setVisibility(View.GONE);
                viewRemarkBody.setVisibility(View.GONE);
            }
//            if (replys.size() > 1) {
//                txtRemarkMore.setVisibility(View.VISIBLE);
//                viewRemarkBody.setVisibility(View.GONE);
//                txtRemarkMore.setText(String.format("查看全部%d条回复 >", replys.size() -1 ));
//
//                for (int i = 0; i < obj.getReplys().size(); i++) {
//                    TextView txtRemark = new TextView(mActivity);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    txtRemark.setLayoutParams(params);
//
//                    SpannableStringBuilder sp = new SpannableStringBuilder("Name : ");
//                    sp.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.color_main_blue)), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    //sp.setSpan(new AbsoluteSizeSpan(12), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    SpannableStringBuilder sp2 = new SpannableStringBuilder("content");
//                    sp2.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.txt_gray)), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    //sp2.setSpan(new AbsoluteSizeSpan(14), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    sp.append(sp2);
//                    txtRemark.setText(sp);
//
//                    viewRemarkBody.addView(txtRemark);
//                }
//            }
        }
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                    txtRemarkMore.setVisibility(View.GONE);
//                    viewRemarkBody.setVisibility(View.VISIBLE);
//                    ((PersonDetailActivity) mActivity).onResetPagerViewSize();
                EvalDetailFragment fragment = new EvalDetailFragment();
                fragment.setEvalModel(obj);
                ((BaseFragmentActivity)mActivity).showFragment(fragment, true);
            }
        });

        txtEvalContent.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        txtEvalContentViewList.add(txtEvalContent);
        txtRemarkFirst.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        txtRemarkFirstViewList.add(txtRemarkFirst);

        return itemView;
    }

}

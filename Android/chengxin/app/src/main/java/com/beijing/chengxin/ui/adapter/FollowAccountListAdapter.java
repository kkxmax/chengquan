package com.beijing.chengxin.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.view.FollowAccountListView;
import com.beijing.chengxin.ui.widget.CustomToast;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class FollowAccountListAdapter extends BaseAdapter implements SectionIndexer {

    private Context mContext;
    private ArrayList<UserModel> mDataList;
    private ArrayList<String> mFollowIdList;

    private int mWhoIndex = FollowAccountListView.INDEX_PERSON;
    private int mIndexSelected = -1;

    public FollowAccountListAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(int indexFlag, ArrayList<UserModel> datas, ArrayList<String> idList) {
        this.mWhoIndex = indexFlag;
        this.mDataList = datas;
        this.mFollowIdList = idList;
    }

    @Override
    public int getCount() {
        return (mDataList == null) ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return (mDataList == null) ? null : mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_account, null);

            holder.viewBody = (View) convertView.findViewById(R.id.view_body);
            holder.viewPreview = (View) convertView.findViewById(R.id.view_preview);
            holder.viewMain = (View) convertView.findViewById(R.id.view_main);
            holder.txtIndex = (TextView) convertView.findViewById(R.id.txt_index);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtFollowMe = (TextView) convertView.findViewById(R.id.txt_follow_me);
            holder.txtFollowNotme = (TextView) convertView.findViewById(R.id.txt_follow_notme);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setId(position);

        final UserModel item = (UserModel) getItem(position);

        holder.id = item.getId();

        String tmpName = item.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? item.getRealname() : item.getEnterName();
        tmpName = tmpName.length() == 0 ? item.getMobile() : tmpName;
        holder.txtName.setText(tmpName);
        Picasso.with(parent.getContext())
                .load(Constants.FILE_ADDR +item.getLogo())
                .placeholder(item.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.drawable.no_image_person : R.drawable.no_image_enter)
                .skipMemoryCache()
                .into(holder.imgAvatar);

        String pre_index = null;
        String index = item.alias;
        if (position == 0) {
            pre_index = null;
        } else {
            pre_index = ((UserModel) getItem(position - 1)).alias;
        }
        if (pre_index != null && pre_index.length() > 0) {
            pre_index = pre_index.substring(0, 1);
        }
        if (index != null && index.length() > 0) {
            index = index.substring(0, 1);
        }

        String index_title = parent.getContext().getString(R.string.str_dis_person);
        if (index.equals(Constants.STR_STAR)) {
            index_title = (item.getAkind() == 1) ? parent.getContext().getString(R.string.str_dis_person) : parent.getContext().getString(R.string.str_dis_enterprise);
            if (mWhoIndex >= FollowAccountListView.INDEX_FRIEND_1_PERSON)
                index_title = FollowAccountListView.getStarTitle(mWhoIndex);
            holder.txtFollowMe.setText(R.string.str_follow_notstar);
        } else {
            index_title = index.toUpperCase();
            holder.txtFollowMe.setText(R.string.str_follow_me);
        }
        holder.txtIndex.setText(index == null ? "" : index_title);
        if (index != null && !index.equals(pre_index)) {
            holder.txtIndex.setVisibility(View.VISIBLE);
        } else {
            holder.txtIndex.setVisibility(View.GONE);
        }

        final String final_index = index;
        holder.txtFollowMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (final_index.equals(Constants.STR_STAR)) {
                    if (mFollowIdList.contains("" + item.getId())) {
                        mFollowIdList.remove("" + item.getId());

                        item.alias = item.alias.substring(1);
                        CommonUtils.sortAccountByChinese(mDataList);

                        String title = FollowAccountListView.getIndexTitle(mWhoIndex);
                        AppConfig.getInstance().setStringArrayValue(title, mFollowIdList);
                    }
                    mIndexSelected = -1;
                } else {
                    if (!mFollowIdList.contains("" + item.getId())) {
                        mFollowIdList.add("" + item.getId());

                        item.alias = Constants.STR_STAR + item.alias;
                        CommonUtils.sortAccountByChinese(mDataList);

                        String title = FollowAccountListView.getIndexTitle(mWhoIndex);
                        AppConfig.getInstance().setStringArrayValue(title, mFollowIdList);
                    }
                    mIndexSelected = -1;
                }
                notifyDataSetChanged();
            }
        });
        if (item.getInterested() == Constants.INTEREST_OK) {
            holder.txtFollowNotme.setText(R.string.str_follow_notme);
            holder.txtFollowNotme.setBackgroundResource(R.drawable.rect_gray_gradient);
            holder.txtFollowNotme.setTextColor(mContext.getResources().getColor(R.color.txt_gray));
        } else {
            holder.txtFollowNotme.setText(R.string.str_follow);
            holder.txtFollowNotme.setBackgroundResource(R.drawable.rect_orange_gradient);
            holder.txtFollowNotme.setTextColor(mContext.getResources().getColor(R.color.color_white));
        }
        holder.txtFollowNotme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int targetInterest = item.getInterested() == Constants.INTEREST_OK ? Constants.INTEREST_NO : Constants.INTEREST_OK;
                setInterestTask(item.getId(), targetInterest);
            }
        });

        final ViewHolder finalHolder = holder;
        holder.viewBody.setOnTouchListener(new View.OnTouchListener() {
            float x1 = 0, x2 = 0;
            float y1 = 0, y2 = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x1 = x2 = event.getX();
                    y1 = y2 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    x2 = event.getX();
                    y2 = event.getY();
                    if ((x1 - x2) > 200) {
                        finalHolder.viewMain.setVisibility(View.GONE);
                        mIndexSelected = position;
                    } else {
                        finalHolder.viewMain.setVisibility(View.VISIBLE);
                        mIndexSelected = -1;
                    }
                    if (Math.abs((x1 - x2)) < 10 && Math.abs((y1 - y2)) < 10) {
                        if (item.getTestStatus() == Constants.TEST_STATUS_PASSED) {
                            Intent intent = new Intent(mContext, DetailActivity.class);
                            intent.putExtra("type", Constants.INDEX_ENTERPRISE);
                            intent.putExtra("id", item.getId());
                            intent.putExtra("akind", item.getAkind());
                            mContext.startActivity(intent);
                            ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            CustomToast.makeText(mContext, mContext.getString(R.string.err_not_test_passed_person), Toast.LENGTH_SHORT).show();
                        }
                    }
                    notifyDataSetChanged();
                }

                return true;
            }
        });

        if (mIndexSelected == position) {
            if (holder.viewMain.getVisibility() == View.GONE) {
                CommonUtils.animationShowFromRight(holder.viewMain);
                holder.viewMain.setVisibility(View.VISIBLE);
            }
        } else {
            holder.viewMain.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        UserModel info;
        int size = getCount();
        for (int i = 0; i < size; i++) {
            info = (UserModel) getItem(i);
            String name = info.alias;
            if (name.length() > 0) {
                char firstChar = name.toUpperCase().charAt(0);
                if (firstChar == sectionIndex) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    private static class ViewHolder {
        public int id;
        public View viewBody;
        public View viewPreview;
        public View viewMain;
        public TextView txtIndex;
        public ImageView imgAvatar;
        public TextView txtName;
        public TextView txtFollowMe;
        public TextView txtFollowNotme;
    }

    private void setInterestTask(final int id, final int interest) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(mContext);
            }
            @Override
            protected BaseModel doInBackground(Object... strs) {
                return new SyncInfo(mContext).syncSetInterest("" + id, "" + interest);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                BaseModel result = (BaseModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        Intent intent = new Intent(Constants.NOTIFY_FOLLOW_INFO_CHANGED);
                        mContext.sendBroadcast(intent);
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.err_server), Toast.LENGTH_LONG).show();
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
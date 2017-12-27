package com.beijing.chengxin.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.MyInterestModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.FollowAccountActivity;
import com.beijing.chengxin.ui.activity.FollowTabAccountActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.view.FollowAccountListView;
import com.beijing.chengxin.ui.widget.CustomToast;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.beijing.chengxin.config.Constants.ACCOUNT_TYPE_PERSON;
import static com.beijing.chengxin.config.Constants.ENTER_TYPE_PERSONAL;
import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class MainFollowFragment extends Fragment {

    public final String TAG = MainFollowFragment.class.getName();
    private View rootView;
    private Context mContext;
    private ArrayList<UserModel> mDatas;
    private int mSelectedIndex;

    View viewBlankPart;
    TextView txtEnterpriseCount, txtPersonCount;
    TextView txtMyHome, txt1DuFriend, txt2DuFriend, txt3DuFriend;
    View viewEnterprise, viewPerson, viewMyhome, view1Friend, view2Friend, view3Friend;

    ToggleButton btnMyFollowing, btnConnection;
    LinearLayout layoutFollowing, layoutConnection;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.NOTIFY_FOLLOW_INFO_CHANGED_FRAGMENT) {
                getDataTask();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_follow, container, false);
        mContext = getContext();

        txtEnterpriseCount = (TextView) rootView.findViewById(R.id.txt_enterprise_count);
        txtPersonCount = (TextView) rootView.findViewById(R.id.txt_person_count);
        txtMyHome = (TextView) rootView.findViewById(R.id.txt_my_home);
        txt1DuFriend = (TextView) rootView.findViewById(R.id.txt_1_du_friend);
        txt2DuFriend = (TextView) rootView.findViewById(R.id.txt_2_du_friend);
        txt3DuFriend = (TextView) rootView.findViewById(R.id.txt_3_du_friend);

        viewEnterprise = (View) rootView.findViewById(R.id.view_enterprise);
        viewPerson = (View) rootView.findViewById(R.id.view_person);
        viewMyhome = (View) rootView.findViewById(R.id.view_myhome);
        view1Friend = (View) rootView.findViewById(R.id.view_1friend);
        view2Friend = (View) rootView.findViewById(R.id.view_2friend);
        view3Friend = (View) rootView.findViewById(R.id.view_3friend);

        btnMyFollowing = (ToggleButton)rootView.findViewById(R.id.btn_following);
        btnConnection = (ToggleButton)rootView.findViewById(R.id.btn_connection);
        btnMyFollowing.setOnClickListener(mButtonClickListener);
        btnConnection.setOnClickListener(mButtonClickListener);

        layoutFollowing = (LinearLayout)rootView.findViewById(R.id.layout_my_following);
        layoutConnection = (LinearLayout)rootView.findViewById(R.id.layout_connection);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mAdapter = new ItemDetailAdapter(mContext, listItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int height = (int) (getResources().getDimension(R.dimen.margin_normal));
                outRect.bottom = height;
            }
        });

        viewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(FollowAccountListView.INDEX_PERSON);
            }
        });
        viewEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(FollowAccountListView.INDEX_ENTERPRISE);
            }
        });
        viewMyhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(FollowAccountListView.INDEX_MYHOME);
            }
        });
        view1Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTabActivity(FollowAccountListView.INDEX_FRIEND_1_PERSON);
            }
        });
        view2Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTabActivity(FollowAccountListView.INDEX_FRIEND_2_PERSON);
            }
        });
        view3Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTabActivity(FollowAccountListView.INDEX_FRIEND_3_PERSON);
            }
        });

        getDataTask();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext.registerReceiver(mBroadcastReceiver, new IntentFilter(Constants.NOTIFY_FOLLOW_INFO_CHANGED_FRAGMENT));
        if (btnMyFollowing.isChecked())
            selectLayout(1);
        else
            selectLayout(2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mBroadcastReceiver);
    }

    private void gotoActivity(int index) {
        Intent intent = new Intent(getActivity(), FollowAccountActivity.class);
        intent.putExtra(FollowAccountListView.INDEX_TITLE, index);
        startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
    }
    private void gotoTabActivity(int index) {
        Intent intent = new Intent(getActivity(), FollowTabAccountActivity.class);
        intent.putExtra(FollowAccountListView.INDEX_TITLE, index);
        startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
    }

    private void initData(MyInterestModel data) {
        if (data != null) {
            txtEnterpriseCount.setText("" + data.getEnterCnt() + " 人");
            txtPersonCount.setText("" + data.getPersonalCnt() + " 人");
            txtMyHome.setText("" + data.getMyAncestorCnt() + " 人");
            txt1DuFriend.setText("" + data.getFriend1Cnt() + " 人");
            txt2DuFriend.setText("" + data.getFriend2Cnt() + " 人");
            txt3DuFriend.setText("" + data.getFriend3Cnt() + " 人");
        }
    }

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
            UserModel user = mDatas.get(position);
            if (user.getTestStatus() == TEST_STATUS_PASSED) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("type", Constants.INDEX_ENTERPRISE);
                intent.putExtra("id", mDatas.get(position).getId());
                intent.putExtra("akind", mDatas.get(position).getAkind());
                startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                CustomToast.makeText(getActivity(), getString(R.string.err_not_test_passed_person), Toast.LENGTH_SHORT).show();
            }

        }
    };

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_following:
                    selectLayout(1);
                    break;
                case R.id.btn_connection:
                    selectLayout(2);
                    break;
            }
        }
    };

    private void selectLayout(int index) {
        if (index == 1) {
            btnMyFollowing.setChecked(true);
            btnConnection.setChecked(false);
            if (layoutFollowing.getVisibility() != View.VISIBLE) {
                layoutFollowing.setVisibility(View.VISIBLE);
                layoutConnection.setVisibility(View.GONE);
            }
        } else {
            btnMyFollowing.setChecked(false);
            btnConnection.setChecked(true);
            if (layoutConnection.getVisibility() != View.VISIBLE) {
                layoutFollowing.setVisibility(View.GONE);
                layoutConnection.setVisibility(View.VISIBLE);
            }
        }
    }

    public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder> {
        private OnItemClickListener itemClickListener;

        public ItemDetailAdapter(Context context, OnItemClickListener listener) {
            itemClickListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enterprise, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            final UserModel user = mDatas.get(position);

            if (user.getAkind() == ACCOUNT_TYPE_PERSON) {
                Picasso.with(getActivity())
                        .load(Constants.FILE_ADDR + user.getLogo())
                        .placeholder(R.drawable.no_image_person)
                        .into(viewHolder.img_avatar);
                viewHolder.txt_item_type.setText(getString(R.string.str_person));
            } else {
                Picasso.with(getActivity())
                        .load(Constants.FILE_ADDR + user.getLogo())
                        .placeholder(R.drawable.no_image_enter)
                        .into(viewHolder.img_avatar);
                viewHolder.txt_item_type.setText(getString(R.string.str_enterprise));
            }

            viewHolder.txt_name.setText(CommonUtils.getUserName(user));

            if (user.getReqCodeSenderId() > 0) {
                if (user.getReqCodeSenderAkind() == ACCOUNT_TYPE_PERSON) {
                    String senderName = user.getReqCodeSenderRealname();
                    if (senderName.equals(""))
                        senderName = user.getReqCodeSenderMobile();
                    if (!user.getInviterFriendLevel().equals(""))
                        senderName = user.getInviterFriendLevel() + " - " + senderName;
                    viewHolder.txt_suggest_man.setText(senderName);
                } else {
                    String senderName = user.getReqCodeSenderEnterName();
                    if (senderName.equals(""))
                        senderName = user.getReqCodeSenderMobile();
                    if (!user.getInviterFriendLevel().equals(""))
                        senderName = user.getInviterFriendLevel() + " - " + senderName;
                    viewHolder.txt_suggest_man.setText(senderName);
                }
                viewHolder.layout_recommender.setVisibility(View.VISIBLE);
            } else {
                viewHolder.layout_recommender.setVisibility(View.GONE);
            }

            if (user.getXyName().equals(""))
                viewHolder.txt_job_type.setVisibility(View.GONE);
            else {
                viewHolder.txt_job_type.setText(user.getXyName());
                viewHolder.txt_job_type.setVisibility(View.VISIBLE);
            }
            viewHolder.txt_chengxin_id.setText(user.getCode());

            if (user.getProducts().size() > 0) {
                String productStr = CommonUtils.getComedityListName(user.getProducts());
                viewHolder.txt_main_comedity.setText(productStr);
                viewHolder.layout_comedity.setVisibility(View.VISIBLE);
            } else
                viewHolder.layout_comedity.setVisibility(View.GONE);

            if (user.getItems().size() > 0) {
                String itemStr = CommonUtils.getItemListName(user.getItems());
                viewHolder.txt_item.setText(itemStr);
                viewHolder.layout_item.setVisibility(View.VISIBLE);
            } else
                viewHolder.layout_item.setVisibility(View.GONE);

            if (user.getServices().size() > 0) {
                String serveStr = CommonUtils.getServeListName(user.getServices());
                viewHolder.txt_serve.setText(serveStr);
                viewHolder.layout_serve.setVisibility(View.VISIBLE);
            } else
                viewHolder.layout_serve.setVisibility(View.GONE);

            if (user.getCredit() < Constants.LEVEL_ZERO)
                viewHolder.txt_chengxin_rate.setText(getText(R.string.rate_zero));
            else
                viewHolder.txt_chengxin_rate.setText(String.format("%d%%", user.getCredit()));
            viewHolder.txt_like_count.setText(String.valueOf(user.getElectCnt()));
            viewHolder.txt_eval_count.setText(String.valueOf(user.getFeedbackCnt()));

            final boolean interest = (user.getInterested() != 0);
            viewHolder.btn_follow.setChecked(interest);

            viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.btn_follow.setChecked(interest);
                    mSelectedIndex = position;
                    int mInterest = (user.getInterested() + 1) % 2;
                    new SetInterestAsync().execute(String.valueOf(user.getId()), String.valueOf(mInterest));
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                        itemClickListener.onListItemClick(position, v);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView img_avatar;
            private TextView txt_item_type;
            private TextView txt_name;
            private TextView txt_job_type;
            private TextView txt_chengxin_id;
            private TextView txt_main_comedity;
            private TextView txt_item;
            private TextView txt_serve;
            private TextView txt_suggest_man;
            private TextView txt_chengxin_rate;
            private TextView txt_like_count;
            private TextView txt_eval_count;
            private ToggleButton btn_follow;
            private LinearLayout layout_comedity;
            private LinearLayout layout_item;
            private LinearLayout layout_serve;
            private LinearLayout layout_recommender;

            public ViewHolder(View itemView) {
                super(itemView);

                img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
                btn_follow = (ToggleButton) itemView.findViewById(R.id.btn_follow);
                txt_item_type = (TextView) itemView.findViewById(R.id.txt_item_type);
                txt_name = (TextView) itemView.findViewById(R.id.txt_name);
                txt_job_type = (TextView) itemView.findViewById(R.id.txt_job_type);
                txt_chengxin_id = (TextView) itemView.findViewById(R.id.txt_chengxin_id);
                txt_main_comedity = (TextView) itemView.findViewById(R.id.txt_main_comedity);
                txt_item = (TextView) itemView.findViewById(R.id.txt_item);
                txt_serve = (TextView) itemView.findViewById(R.id.txt_serve);
                txt_suggest_man = (TextView) itemView.findViewById(R.id.txt_suggest_man);
                txt_chengxin_rate = (TextView) itemView.findViewById(R.id.txt_chengxin_rate);
                txt_like_count = (TextView) itemView.findViewById(R.id.txt_like_count);
                txt_eval_count = (TextView) itemView.findViewById(R.id.txt_eval_count);
                layout_comedity = (LinearLayout) itemView.findViewById(R.id.layout_comedity);
                layout_item = (LinearLayout) itemView.findViewById(R.id.layout_item);
                layout_serve = (LinearLayout) itemView.findViewById(R.id.layout_serve);
                layout_recommender = (LinearLayout) itemView.findViewById(R.id.layout_recommender);
            }
        }
    }

    private void getDataTask() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(mContext);
            }
            @Override
            protected Object doInBackground(Object... params) {
                return new SyncInfo(mContext).syncMyInterest();
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                MyInterestModel result = (MyInterestModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        initData(result);
                        mDatas = result.getList();
                        mAdapter.notifyDataSetChanged();
                    } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                        ChengxinApplication.finishAndLoginActivityFromDuplicate(getActivity());
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
        }.execute();
    }

    class SetInterestAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return new SyncInfo(mContext).syncSetInterest(strs[0], strs[1]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    UserModel item = mDatas.get(mSelectedIndex);
                    item.setInterested((item.getInterested() + 1) % 2);
                    mDatas.set(mSelectedIndex, item);

                    mAdapter.notifyDataSetChanged();
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishAndLoginActivityFromDuplicate(getActivity());
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

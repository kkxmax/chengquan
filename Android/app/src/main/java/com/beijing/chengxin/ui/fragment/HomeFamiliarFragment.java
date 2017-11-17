package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.UserListModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.listener.OnConditionClickListener;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.view.CircleImageView;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ACCOUNT_TYPE_PERSON;
import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.PAGE_ITEM_COUNT;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class HomeFamiliarFragment extends Fragment {

	public final String TAG = HomeFamiliarFragment.class.getName();
    private View rootView;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    ImageButton btnAdd;

    SyncInfo info;
    AppConfig appConfig;

    List<UserModel> listFriend;
    int selectedIndex;

    public int mOrder;
    public String mCityName = "";
    public int aKind;
    public List<Integer> xyList;
    public String keyword = "";
    public String keywordCode = "";

    boolean isDataLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        info = new SyncInfo(getActivity());
        appConfig = new AppConfig(getActivity());

        if (listFriend == null)
            listFriend = new ArrayList<UserModel>();

        if (xyList == null)
            xyList = new ArrayList<Integer>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mAdapter = new ItemDetailAdapter(getActivity(), listItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 1;
            }
        });

        btnAdd = (ImageButton)rootView.findViewById(R.id.btn_add);

        return rootView;
    }

    private void loadData() {
        if (isDataLoading)
            return;
        String start = String.valueOf(listFriend.size());
        String length = String.valueOf(PAGE_ITEM_COUNT);
        String order = String.valueOf(mOrder + 1);
        String akindStr = String.valueOf(aKind);
        String xyleixingId = "";
        for (int i = 0; i < xyList.size(); i ++) {
            if (!xyleixingId.equals(""))
                xyleixingId += ",";
            xyleixingId += String.valueOf(xyList.get(i));
        }

        new FriendListAsync().execute(start, length, order, mCityName, akindStr, xyleixingId, keyword, keywordCode);
    }

    public void setKeyword(String word, String code) {
        keyword = word;
        keywordCode = code;

        if (info != null)
            reloadData();
    }

    public void reloadData() {
        listFriend.clear();
        loadData();
    }

    public OnConditionClickListener mConditionClickListener = new OnConditionClickListener() {
        @Override
        public void onClickReset() {
        }
        @Override
        public void onClickOk(String cityName, int kind, List<Integer>typeList) {
            mCityName = cityName;
            aKind = kind;
            xyList = typeList;

            reloadData();
            MainActivity.mainActivity.hideConditionView(Constants.INDEX_FAMILIAR);
        }
    };

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
            if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                selectedIndex = position;
                UserModel user = listFriend.get(position);

                new IncreaseViewCountAsync().execute(String.valueOf(Constants.VIEW_CNT_KIND_PERSONAL_OR_ENTER), String.valueOf(user.getId()), "");
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (listFriend.size() == 0)
            loadData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder> {
        private Context mContext;
        private OnItemClickListener itemClickListener;

        public ItemDetailAdapter(Context context, OnItemClickListener listener) {
            mContext = context;
            itemClickListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_detail, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            final UserModel user = listFriend.get(position);

            Picasso.with(getActivity())
                    .load(Constants.FILE_ADDR + user.getLogo())
                    .placeholder(R.drawable.no_image)
                    .into(viewHolder.img_avatar);

            if (user.getAkind() == ACCOUNT_TYPE_PERSON) {
                viewHolder.img_blank.setImageResource(R.drawable.blank_person);
                viewHolder.txt_name.setText(user.getRealname());
            }
            else {
                viewHolder.img_blank.setImageResource(R.drawable.blank_enterprise);
                viewHolder.txt_name.setText(user.getEnterName());
            }

            if (user.getReqCodeSenderAkind() == ACCOUNT_TYPE_PERSON) {
                String senderName = user.getReqCodeSenderRealname();
                if (!senderName.equals("")) {
                    if (!user.getInviterFriendLevel().equals(""))
                        senderName = user.getInviterFriendLevel() + " - " + senderName;
                    viewHolder.txt_suggest_man.setText(senderName);
                } else {
                    viewHolder.layout_recommender.setVisibility(View.GONE);
                }
            }
            else {
                String senderName = user.getReqCodeSenderEnterName();
                if (!senderName.equals("")) {
                    if (!user.getInviterFriendLevel().equals(""))
                        senderName = user.getInviterFriendLevel() + " - " + senderName;
                    viewHolder.txt_suggest_man.setText(senderName);
                } else {
                    viewHolder.layout_recommender.setVisibility(View.GONE);
                }
            }

            viewHolder.txt_job_type.setText(user.getXyName());
            viewHolder.txt_chengxin_id.setText(user.getCode());

            if (user.getProducts().size() > 0) {
                String productStr = CommonUtils.getComedityListName(user.getProducts());
                viewHolder.txt_main_comedity.setText(productStr);
            } else
                viewHolder.layout_comedity.setVisibility(View.GONE);

            if (user.getItems().size() > 0) {
                String itemStr = CommonUtils.getItemListName(user.getItems());
                viewHolder.txt_item.setText(itemStr);
            } else
                viewHolder.layout_item.setVisibility(View.GONE);

            if (user.getServices().size() > 0) {
                String serveStr = CommonUtils.getServeListName(user.getServices());
                viewHolder.txt_serve.setText(serveStr);
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
                    selectedIndex = position;
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
            return listFriend.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private CircleImageView img_avatar;
            private ImageView img_blank;
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

                img_avatar = (CircleImageView) itemView.findViewById(R.id.img_avatar);
                img_blank = (ImageView) itemView.findViewById(R.id.img_blank);
                btn_follow = (ToggleButton) itemView.findViewById(R.id.btn_follow);
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

    class FriendListAsync extends AsyncTask<String, String, UserListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDataLoading = true;
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected UserListModel doInBackground(String... strs) {
            return info.syncFriendList(strs[0], strs[1], strs[2], strs[3], strs[4], strs[5], strs[6], strs[7]);
        }
        @Override
        protected void onPostExecute(UserListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    listFriend.addAll(result.getList());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
            isDataLoading = false;
            Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            isDataLoading = false;
            Utils.disappearProgressDialog();
        }
    }

    class SetInterestAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncSetInterest(strs[0], strs[1]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    UserModel item = listFriend.get(selectedIndex);
                    item.setInterested((item.getInterested() + 1) % 2);
                    listFriend.set(selectedIndex, item);

                    mAdapter.notifyDataSetChanged();
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
                    UserModel user = listFriend.get(selectedIndex);
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("id", user.getId());
                    intent.putExtra("type", Constants.INDEX_ENTERPRISE);
                    intent.putExtra("akind", user.getAkind());
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

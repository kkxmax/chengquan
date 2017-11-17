package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.graphics.Rect;
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
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.config.Constants;
import com.beijing.chengxin.ui.listener.OnItemClickListener;

import java.util.List;

public class MyEvalNotifyFragment extends Fragment {

	public final String TAG = MyEvalNotifyFragment.class.getName();

    private View rootView;
    ToggleButton btnFrontEval, btnBackEval;
    TextView txtBadgeFront, txtBadgeBack;

    RecyclerView mRecyclerView1, mRecyclerView2;
    RecyclerView.Adapter mAdapter1, mAdapter2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_my_eval_notify, container, false);

        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.my_eval));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        btnFrontEval = (ToggleButton)rootView.findViewById(R.id.btn_front_eval);
        btnBackEval = (ToggleButton)rootView.findViewById(R.id.btn_back_eval);
        btnFrontEval.setOnClickListener(mButtonClickListener);
        btnBackEval.setOnClickListener(mButtonClickListener);

        txtBadgeFront = (TextView)rootView.findViewById(R.id.txt_badge_front);
        txtBadgeBack = (TextView)rootView.findViewById(R.id.txt_badge_back);

        if(Constants.DEBUG_MODE) {
            txtBadgeFront.setText("8");
            txtBadgeBack.setText("3");
        }

        mRecyclerView1 = (RecyclerView)rootView.findViewById(R.id.recyclerView1);
        mAdapter1 = new ItemDetailAdapter(getActivity(), null, listItemClickListener);
        mRecyclerView1.setAdapter(mAdapter1);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView1.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 8;
            }
        });

        mRecyclerView2 = (RecyclerView)rootView.findViewById(R.id.recyclerView2);
        mAdapter2 = new ItemDetailAdapter(getActivity(), null, listItemClickListener);
        mRecyclerView2.setAdapter(mAdapter2);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView2.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 8;
            }
        });

        return rootView;
    }

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
//            Intent intent = new Intent(this, EnterpriseDetailActivity.class);
//            startActivity(intent);
        }
    };

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_front_eval:
                    mRecyclerView1.setVisibility(View.VISIBLE);
                    mRecyclerView2.setVisibility(View.GONE);
                    btnFrontEval.setChecked(true);
                    btnBackEval.setChecked(false);
                    break;
                case R.id.btn_back_eval:
                    mRecyclerView1.setVisibility(View.GONE);
                    mRecyclerView2.setVisibility(View.VISIBLE);
                    btnFrontEval.setChecked(false);
                    btnBackEval.setChecked(true);
                    break;
                case R.id.btn_back:
                    BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
                    parent.goBack();
                    break;
            }
        }
    };

    public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder> {

        //        public List<Message> mMessages;
        private Context mContext;
        private OnItemClickListener itemClickListener;

        public ItemDetailAdapter(Context context, List items, OnItemClickListener listener) {
            mContext = context;
            itemClickListener = listener;
//            mMessages = messages;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_eval, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
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
            return 10;//mMessages.size();
        }

        @Override
        public int getItemViewType(int position) {
            return 0;//mMessages.get(position).getType();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout mContainerView;
            private TextView mLevelView;
            private TextView mUsernameView;
            private TextView mMessageView;
            private TextView mRecommandSuffix;
            private ImageView mTopLevelImage;
            private TextView mTopLevelText;

            public ViewHolder(View itemView) {

                super(itemView);
//                mContainerView = (LinearLayout) itemView.findViewById(R.id.layout_container);
//                mLevelView       = (TextView) itemView.findViewById(R.id.txt_level_id);
//                mUsernameView    = (TextView) itemView.findViewById(R.id.txt_username_id);
//                mMessageView     = (TextView) itemView.findViewById(R.id.txt_message_id);
//                mRecommandSuffix = (TextView) itemView.findViewById(R.id.txt_recommend_suffix_id);
//                mTopLevelImage   = (ImageView) itemView.findViewById(R.id.img_level_top_id);
//                mTopLevelText    = (TextView) itemView.findViewById(R.id.txt_level_top_id);
            }
        }
    }

}

package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.activity.MakeEvaluationActivity;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.view.HangyeListView;
import com.beijing.chengxin.utils.CommonUtils;

import java.util.List;

public class MainEvalFragment extends Fragment {

	public final String TAG = MainEvalFragment.class.getName();
    private View rootView;
    public HangyeListView conditionEvalView;

    ToggleButton btnPerson, btnEnterprise;
    ImageButton btnWrite;
    Button btnConditionSet;

    RecyclerView mRecyclerView1, mRecyclerView2;
    RecyclerView.Adapter mAdapter1, mAdapter2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
    	rootView = inflater.inflate(R.layout.fragment_main_eval, container, false);
        btnPerson = (ToggleButton)rootView.findViewById(R.id.btn_person);
        btnEnterprise = (ToggleButton)rootView.findViewById(R.id.btn_enterprise);
        btnWrite = (ImageButton)rootView.findViewById(R.id.btn_write);
        btnConditionSet = (Button) rootView.findViewById(R.id.btn_condition_set);

        btnPerson.setOnClickListener(mButtonClickListener);
        btnEnterprise.setOnClickListener(mButtonClickListener);
        btnWrite.setOnClickListener(mButtonClickListener);
        btnConditionSet.setOnClickListener(mButtonClickListener);

        mRecyclerView1 = (RecyclerView)rootView.findViewById(R.id.recyclerView1);
        mAdapter1 = new ItemDetailAdapter(getActivity(), null, listItemClickListener);
        mRecyclerView1.setAdapter(mAdapter1);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView1.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 12;
            }
        });

        mRecyclerView2 = (RecyclerView)rootView.findViewById(R.id.recyclerView2);
        mAdapter2 = new ItemDetailAdapter(getActivity(), null, listItemClickListener);
        mRecyclerView2.setAdapter(mAdapter2);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView2.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 12;
            }
        });

        return rootView;
    }

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {

            Intent intent = new Intent(getActivity(), DetailActivity.class);
//            intent.putExtra("id", )
            startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_person:
                    btnPerson.setChecked(true);
                    btnEnterprise.setChecked(false);
                    mRecyclerView1.setVisibility(View.VISIBLE);
                    mRecyclerView2.setVisibility(View.GONE);
                    break;
                case R.id.btn_enterprise:
                    btnPerson.setChecked(false);
                    btnEnterprise.setChecked(true);
                    mRecyclerView1.setVisibility(View.GONE);
                    mRecyclerView2.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_write:
                    Intent intent = new Intent(getActivity(), MakeEvaluationActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.btn_condition_set:
                    onClickedConditionSet();
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
        public ItemDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eval_main, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemDetailAdapter.ViewHolder viewHolder, final int position) {
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

    private void onClickedConditionSet() {
        int visibility = MainActivity.mainActivity.layoutCondition.getVisibility();
        if (visibility == View.GONE) {
            MainActivity.mainActivity.layoutCondition.setVisibility(View.VISIBLE);
            if (conditionEvalView == null) {
                conditionEvalView = new HangyeListView(getContext(), hyListener);
                MainActivity.mainActivity.layoutConditionBody.addView(conditionEvalView);
                conditionEvalView.setVisibility(View.VISIBLE);
            }
            if (conditionEvalView.getVisibility() == View.GONE) {
                conditionEvalView.setVisibility(View.VISIBLE);
            }
            CommonUtils.animationShowFromRight(conditionEvalView);
        } else {
            MainActivity.mainActivity.layoutCondition.setVisibility(View.GONE);
            if (conditionEvalView != null && conditionEvalView.getVisibility() == View.VISIBLE) {
                conditionEvalView.setVisibility(View.GONE);
            }
        }
    }

    HangyeListView.OnHangyeSelectListener hyListener = new HangyeListView.OnHangyeSelectListener() {
        @Override
        public void OnHangyeSelected(List<Integer> list) {
        }
    };

}

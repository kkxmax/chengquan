package com.beijing.chengxin.ui.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.ui.listener.OnItemClickListener;

import java.util.List;

public class MyErrorActivity extends ParentFragmentActivity {

    public final String TAG = MyErrorActivity.class.getName();

    ToggleButton btnComedity, btnHot;

    RecyclerView mRecyclerView1, mRecyclerView2;
    RecyclerView.Adapter mAdapter1, mAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.my_collect));

        btnComedity = (ToggleButton)findViewById(R.id.btn_comedity);
        btnHot = (ToggleButton)findViewById(R.id.btn_hot);
        btnComedity.setOnClickListener(mButtonClickListener);
        btnHot.setOnClickListener(mButtonClickListener);
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        mRecyclerView1 = (RecyclerView)findViewById(R.id.recyclerView1);
        mAdapter1 = new ItemDetailAdapter(this, null, listItemClickListener, 0);
        mRecyclerView1.setAdapter(mAdapter1);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView1.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, true));
        mRecyclerView1.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 8;
                int position = parent.getChildAdapterPosition(view);
                if (position % 2 == 0)
                    outRect.right = 8;
                if(position == 0 || position == 1)
                    outRect.top = 8;
            }
        });

        mRecyclerView2 = (RecyclerView)findViewById(R.id.recyclerView2);
        mAdapter2 = new ItemDetailAdapter(this, null, listItemClickListener, 1);
        mRecyclerView2.setAdapter(mAdapter2);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView2.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 8;
            }
        });
    }

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
//            Intent intent = new Intent(this, DetailActivity.class);
//            intent.putExtra("type", Constants.INDEX_ENTERPRISE);
//            startActivity(intent);
        }
    };

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_comedity:
                    mRecyclerView1.setVisibility(View.VISIBLE);
                    mRecyclerView2.setVisibility(View.GONE);
                    btnComedity.setChecked(true);
                    btnHot.setChecked(false);
                    break;
                case R.id.btn_hot:
                    mRecyclerView1.setVisibility(View.GONE);
                    mRecyclerView2.setVisibility(View.VISIBLE);
                    btnComedity.setChecked(false);
                    btnHot.setChecked(true);
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

    public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder> {

        //        public List<Message> mMessages;
        private Context mContext;
        private OnItemClickListener itemClickListener;
        private int mType;

        public ItemDetailAdapter(Context context, List items, OnItemClickListener listener, int type) {
            mContext = context;
            itemClickListener = listener;
            mType = type;
//            mMessages = messages;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout = -1;
            if (mType == 0)
                layout = R.layout.item_comedity;
            else
                layout = R.layout.item_hot;
            View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
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

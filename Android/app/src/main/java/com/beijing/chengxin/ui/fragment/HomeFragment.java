package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.ComedityDetailActivity;
import com.beijing.chengxin.ui.activity.EnterpriseDetailActivity;
import com.beijing.chengxin.ui.activity.ItemDetailActivity;
import com.beijing.chengxin.ui.activity.MakeComedityActivity;
import com.beijing.chengxin.ui.activity.MakeItemActivity;
import com.beijing.chengxin.ui.activity.PersonDetailActivity;
import com.beijing.chengxin.ui.activity.VideoPlayActivity;
import com.beijing.chengxin.ui.adapter.HomeListAdapter;
import com.beijing.chengxin.ui.config.Constants;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.AutoScrollViewPager;
import com.beijing.chengxin.ui.widget.PageIndicator;
import com.beijing.chengxin.ui.widget.UrlImagePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.ui.fragment.MainHomeFragment.INDEX_COMEDITY;
import static com.beijing.chengxin.ui.fragment.MainHomeFragment.INDEX_ENTERPRISE;
import static com.beijing.chengxin.ui.fragment.MainHomeFragment.INDEX_FAMILIAR;
import static com.beijing.chengxin.ui.fragment.MainHomeFragment.INDEX_ITEM;
import static com.beijing.chengxin.ui.fragment.MainHomeFragment.INDEX_SERVE;

public class HomeFragment extends Fragment {

	public final String TAG = HomeFragment.class.getName();
    private View rootView;

    AutoScrollViewPager recommendViewPager;
    PageIndicator pageIndicator;
    UrlImagePagerAdapter recommendImageAdapter;
    List<String> listRecommendImageUrl;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    ImageButton btnAdd;

    int mType;
    Point size;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setType(int type) {
        mType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_home, container, false);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);

        listRecommendImageUrl = new ArrayList<String>();
        listRecommendImageUrl.add(Constants.WEB_HOST_ADDRESS);
        listRecommendImageUrl.add(Constants.WEB_HOST_ADDRESS);
        listRecommendImageUrl.add(Constants.WEB_HOST_ADDRESS);
        recommendImageAdapter = new UrlImagePagerAdapter(listRecommendImageUrl).setInfiniteLoop(true);
        recommendImageAdapter.setOnItemClickListener(imageClickListener);
        recommendViewPager = (AutoScrollViewPager) rootView.findViewById(R.id.autoScrollViewPager);
        recommendViewPager.setAdapter(recommendImageAdapter);
        recommendViewPager.setInterval(5000);
        pageIndicator = (PageIndicator) rootView.findViewById(R.id.pageIndicator);
        pageIndicator.setViewPager(recommendViewPager);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mAdapter = new ItemDetailAdapter(getActivity(), null, listItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        btnAdd = (ImageButton)rootView.findViewById(R.id.btn_add);

        if (mType == INDEX_COMEDITY || mType == INDEX_ITEM || mType == INDEX_SERVE) {
            btnAdd.setVisibility(View.VISIBLE);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mType == INDEX_COMEDITY ) {
                        Intent intent = new Intent(getActivity() , MakeComedityActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else if (mType == INDEX_ITEM) {
                        Intent intent = new Intent(getActivity() , MakeItemActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else if (mType == INDEX_SERVE) {
                    }
                }
            });
        }

        if (mType != INDEX_COMEDITY) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = 1;
                }
            });
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, true));
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.top = 16;
                    int position = parent.getChildAdapterPosition(view);
                    if (position % 2 == 0)
                        outRect.right = 8;
                    else
                        outRect.left = 8;
                }
            });
        }

        return rootView;
    }

    UrlImagePagerAdapter.OnItemClickListener imageClickListener = new UrlImagePagerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            if (position >= 1) {
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                startActivity(intent);
            }
        }
    };

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
            Intent intent;

            int listType = mType;
            if (listType == INDEX_FAMILIAR) {
                if (position % 2 == 0)
                    intent = new Intent(getActivity(), EnterpriseDetailActivity.class);
                else
                    intent = new Intent(getActivity(), PersonDetailActivity.class);
                startActivity(intent);
            }
            else if (listType == INDEX_ENTERPRISE) {
                intent = new Intent(getActivity(), EnterpriseDetailActivity.class);
                startActivity(intent);
            }
            else if (listType == INDEX_ITEM || listType == INDEX_SERVE ) {
                intent = new Intent(getActivity(), ItemDetailActivity.class);
                startActivity(intent);
            }
            else if (listType == INDEX_COMEDITY) {
                intent = new Intent(getActivity(), ComedityDetailActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        recommendViewPager.stopAutoScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        recommendViewPager.startAutoScroll();
        mAdapter.notifyDataSetChanged();
    }

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
            int layout = -1;
            switch (mType) {
                case INDEX_FAMILIAR:
                    layout = R.layout.item_detail;
                    break;
                case INDEX_ENTERPRISE:
                    layout = R.layout.item_enterprise;
                    break;
                case INDEX_ITEM:
                case INDEX_SERVE:
                    layout = R.layout.item_item_serve;
                    break;
                case INDEX_COMEDITY:
                    layout = R.layout.item_comedity;
                    break;
            }
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

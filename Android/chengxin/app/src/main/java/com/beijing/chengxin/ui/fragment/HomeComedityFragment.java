package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.ComedityListModel;
import com.beijing.chengxin.network.model.ComedityModel;
import com.beijing.chengxin.network.model.XyleixingListModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.activity.MakeComedityActivity;
import com.beijing.chengxin.ui.listener.OnConditionClickListener;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ACCOUNT_TYPE_ENTERPRISE;
import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.PAGE_ITEM_COUNT;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class HomeComedityFragment extends Fragment {

	public final String TAG = HomeComedityFragment.class.getName();
    private View rootView;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    ImageButton btnAdd;

    SyncInfo info;
    AppConfig appConfig;

    List<ComedityModel> listComedity;

    public int mOrder;
    public String mCityName = "";
    public List<Integer> xyList;
    public String keyword = "";
    boolean isDataLoading = false;


    private static HomeComedityFragment instance;

    public static HomeComedityFragment getInstance() {
        if (instance == null)
            instance = new HomeComedityFragment();

        return instance;
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        info = new SyncInfo(getActivity());
        appConfig = new AppConfig(getActivity());

        if (listComedity == null)
            listComedity = new ArrayList<ComedityModel>();

        if (xyList == null)
            xyList = new ArrayList<Integer>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mAdapter = new ItemDetailAdapter(getActivity(), listItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        btnAdd = (ImageButton)rootView.findViewById(R.id.btn_add);
        if (SessionInstance.getInstance().getLoginData().getUser().getAkind() == ACCOUNT_TYPE_ENTERPRISE)
            btnAdd.setVisibility(View.VISIBLE);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                    Intent intent = new Intent(getActivity(), MakeComedityActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
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

        if (AppConfig.getInstance().pleixingList.size() == 0)
            new PleixingListAsync().execute("");

        return rootView;
    }

    private void loadData() {
        if (isDataLoading)
            return;
        String start = String.valueOf(listComedity.size());
        String length = String.valueOf(PAGE_ITEM_COUNT);
        String order = String.valueOf(mOrder + 1);
        String xyleixingId = "";
        for (int i = 0; i < xyList.size(); i ++) {
            if (!xyleixingId.equals(""))
                xyleixingId += ",";
            xyleixingId += String.valueOf(xyList.get(i));
        }

        new ComedityListAsync().execute(start, length, order, mCityName, xyleixingId, keyword);
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        if (info != null)
            reloadData();
    }

    public void reloadData() {
        listComedity.clear();
        loadData();
    }

    public OnConditionClickListener mConditionClickListener = new OnConditionClickListener() {
        @Override
        public void onClickReset() {
        }
        @Override
        public void onClickOk(String cityName, int kind, List<Integer>typeList) {
            mCityName = cityName;
            xyList = typeList;

            reloadData();
            MainActivity.mainActivity.hideConditionView(Constants.INDEX_COMEDITY);
        }
    };

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("type", Constants.INDEX_COMEDITY);
        intent.putExtra("id", listComedity.get(position).getId());
        startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (listComedity.size() == 0)
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
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_comedity, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            ComedityModel item = listComedity.get(position);

            Picasso.with(getActivity())
                    .load(Constants.FILE_ADDR + item.getImgPaths().get(0))
                    .placeholder(R.drawable.no_image)
                    .into(viewHolder.imgPhoto);
            viewHolder.txt_name.setText(item.getName());
            viewHolder.txt_price.setText(String.format("¥%.02f", item.getPrice()));

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
            return listComedity.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imgPhoto;
            private TextView txt_name;
            private TextView txt_price;

            public ViewHolder(View itemView) {
                super(itemView);
                imgPhoto = (ImageView) itemView.findViewById(R.id.imgPhoto);
                txt_name = (TextView) itemView.findViewById(R.id.txt_name);
                txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            }
        }
    }

    class ComedityListAsync extends AsyncTask<String, String, ComedityListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDataLoading = true;
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected ComedityListModel doInBackground(String... strs) {
            return info.syncComedityList(strs[0], strs[1], strs[2], strs[3], strs[4], strs[5]);
        }
        @Override
        protected void onPostExecute(ComedityListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    listComedity.addAll(result.getList());
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

    class PleixingListAsync extends AsyncTask<String, String, XyleixingListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected XyleixingListModel doInBackground(String... strs) {
            return info.syncPleixingList(strs[0]);
        }
        @Override
        protected void onPostExecute(XyleixingListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().pleixingList.clear();
                    AppConfig.getInstance().pleixingList.addAll(result.getList());
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}

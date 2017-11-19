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
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.FenleiListModel;
import com.beijing.chengxin.network.model.ItemListModel;
import com.beijing.chengxin.network.model.ItemModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.activity.MakeItemActivity;
import com.beijing.chengxin.ui.listener.OnConditionClickListener;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.PAGE_ITEM_COUNT;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class HomeItemFragment extends Fragment {

	public final String TAG = HomeItemFragment.class.getName();
    private View rootView;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    ImageButton btnAdd;

    SyncInfo info;
    AppConfig appConfig;

    List<ItemModel> listItem;

    public int mOrder;
    public String mCityName = "";
    public int aKind;
    public List<Integer> xyList;
    public String keyword = "";
    boolean isDataLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        info = new SyncInfo(getActivity());
        appConfig = new AppConfig(getActivity());

        if (listItem == null)
            listItem = new ArrayList<ItemModel>();

        if (xyList == null)
            xyList = new ArrayList<Integer>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        mAdapter = new ItemDetailAdapter(getActivity(), listItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
            btnAdd = (ImageButton)rootView.findViewById(R.id.btn_add);
            btnAdd.setVisibility(View.VISIBLE);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity() , MakeItemActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 1;
            }
        });

        if (AppConfig.getInstance().itemFenleiList.size() == 0)
            new FenleiListAsync().execute("");

        return rootView;
    }

    private void loadData() {
        if (isDataLoading)
            return;
        String start = String.valueOf(listItem.size());
        String length = String.valueOf(PAGE_ITEM_COUNT);
        String order = String.valueOf(mOrder + 1);
        String akindStr = String.valueOf(aKind);
        String xyleixingId = "";
        for (int i = 0; i < xyList.size(); i ++) {
            if (!xyleixingId.equals(""))
                xyleixingId += ",";
            xyleixingId += String.valueOf(xyList.get(i));
        }
        new ItemListAsync().execute(start, length, order, mCityName, akindStr, xyleixingId, keyword);
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        if (info != null)
            reloadData();
    }

    public void reloadData() {
        listItem.clear();
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
            MainActivity.mainActivity.hideConditionView(Constants.INDEX_ITEM);
        }
    };

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
            if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("type", Constants.INDEX_ITEM);
                AppConfig.getInstance().currentItem = listItem.get(position);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (listItem.size() == 0)
            loadData();
    }

    public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder> {
        private Context mContext;
        private OnItemClickListener itemClickListener;

        public ItemDetailAdapter(Context context, OnItemClickListener listener) {
            mContext = context;
            itemClickListener = listener;
//            mMessages = messages;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_serve, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            ItemModel item = listItem.get(position);

            Picasso.with(getActivity())
                    .load(Constants.FILE_ADDR + item.getLogo())
                    .placeholder(R.drawable.no_image)
                    .into(viewHolder.img_avatar);

            viewHolder.txt_name.setText(item.getName());
            viewHolder.txt_type.setText(item.getFenleiName());
            viewHolder.txt_descript.setText(item.getComment());

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
            return listItem.size();
        }

        @Override
        public int getItemViewType(int position) {
            return 0;//mMessages.get(position).getType();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView img_avatar;
            private TextView txt_name;
            private TextView txt_type;
            private TextView txt_descript;

            public ViewHolder(View itemView) {

                super(itemView);
                img_avatar = (ImageView) itemView.findViewById(R.id.img_avatar);
                txt_name = (TextView) itemView.findViewById(R.id.txt_name);
                txt_type = (TextView) itemView.findViewById(R.id.txt_type);
                txt_descript = (TextView) itemView.findViewById(R.id.txt_descript);
            }
        }
    }

    class ItemListAsync extends AsyncTask<String, String, ItemListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDataLoading = true;
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected ItemListModel doInBackground(String... strs) {
            return info.syncItemList(strs[0], strs[1], strs[2], strs[3], strs[4], strs[5], strs[6]);
        }
        @Override
        protected void onPostExecute(ItemListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    listItem.addAll(result.getList());
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

    class FenleiListAsync extends AsyncTask<String, String, FenleiListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected FenleiListModel doInBackground(String... strs) {
            return info.syncItemFenleiList();
        }
        @Override
        protected void onPostExecute(FenleiListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().itemFenleiList.clear();
                    AppConfig.getInstance().itemFenleiList.addAll(result.getList());
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

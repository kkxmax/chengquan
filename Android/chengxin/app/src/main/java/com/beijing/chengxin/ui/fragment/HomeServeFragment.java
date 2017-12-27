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
import com.beijing.chengxin.network.model.ServeListModel;
import com.beijing.chengxin.network.model.ServeModel;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.activity.MakeServeActivity;
import com.beijing.chengxin.ui.listener.OnConditionClickListener;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.PAGE_ITEM_COUNT;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class HomeServeFragment extends Fragment {

	public final String TAG = HomeServeFragment.class.getName();
    private View rootView;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    ImageButton btnAdd;

    SyncInfo info;
    AppConfig appConfig;

    List<ServeModel> listServe;

    public int mOrder;
    public String mCityName = "";
    public int aKind;
    public List<Integer> xyList;
    public String keyword = "";
    boolean isDataLoading = false;

    private static HomeServeFragment instance;

    public static HomeServeFragment getInstance() {
        if (instance == null)
            instance = new HomeServeFragment();

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        info = new SyncInfo(getActivity());
        appConfig = new AppConfig(getActivity());

        if (listServe == null)
            listServe = new ArrayList<ServeModel>();

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
                    Intent intent = new Intent(getActivity() , MakeServeActivity.class);
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

        if (AppConfig.getInstance().serveFenleiList.size() == 0)
            new FenleiListAsync().execute("");

        return rootView;
    }

    private void loadData() {
        if (isDataLoading)
            return;
        String start = String.valueOf(listServe.size());
        String length = String.valueOf(PAGE_ITEM_COUNT);
        String order = String.valueOf(mOrder + 1);
        String akindStr = String.valueOf(aKind);
        String xyleixingId = "";
        for (int i = 0; i < xyList.size(); i ++) {
            if (!xyleixingId.equals(""))
                xyleixingId += ",";
            xyleixingId += String.valueOf(xyList.get(i));
        }
        new ServeListAsync().execute(start, length, order, mCityName, akindStr, xyleixingId, keyword);
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        if (info != null)
            reloadData();
    }

    public void reloadData() {
        listServe.clear();
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
            MainActivity.mainActivity.hideConditionView(Constants.INDEX_SERVE);
        }
    };

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("type", Constants.INDEX_SERVE);
        AppConfig.getInstance().currentServe= listServe.get(position);
        startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (listServe.size() == 0)
            loadData();
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_serve, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            ServeModel item = listServe.get(position);

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
            return listServe.size();
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

    class ServeListAsync extends AsyncTask<String, String, ServeListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDataLoading = true;
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected ServeListModel doInBackground(String... strs) {
            return info.syncServeList(strs[0], strs[1], strs[2], strs[3], strs[4], strs[5], strs[6]);
        }
        @Override
        protected void onPostExecute(ServeListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    listServe.addAll(result.getList());
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
            return info.syncServeFenleiList();
        }
        @Override
        protected void onPostExecute(FenleiListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().serveFenleiList.clear();
                    AppConfig.getInstance().serveFenleiList.addAll(result.getList());
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

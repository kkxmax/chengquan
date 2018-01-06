package com.beijing.chengxin.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.ComedityListModel;
import com.beijing.chengxin.network.model.ComedityModel;
import com.beijing.chengxin.network.model.FenleiListModel;
import com.beijing.chengxin.network.model.ItemListModel;
import com.beijing.chengxin.network.model.ItemModel;
import com.beijing.chengxin.network.model.ServeListModel;
import com.beijing.chengxin.network.model.ServeModel;
import com.beijing.chengxin.network.model.XyleixingListModel;
import com.beijing.chengxin.ui.activity.MakeComedityActivity;
import com.beijing.chengxin.ui.activity.MakeItemActivity;
import com.beijing.chengxin.ui.activity.MakeServeActivity;
import com.beijing.chengxin.ui.activity.MyWriteActivity;
import com.beijing.chengxin.ui.listener.OnCallbackListener;
import com.beijing.chengxin.ui.widget.RefreshListView;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.beijing.chengxin.config.Constants.ACCOUNT_TYPE_ENTERPRISE;
import static com.beijing.chengxin.config.Constants.ERROR_DUPLICATE;
import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class MyWriteFragment extends Fragment {

    public static final int INDEX_GET_DATA_ALL = 0;
    public static final int INDEX_GET_DATA_COMEDITY = 1;
    public static final int INDEX_GET_DATA_ITEM = 2;
    public static final int INDEX_GET_DATA_SERVICE = 2;

    private Context mContext;
    private View rootView;

    EditText edtSearch;
    ToggleButton btnComedity, btnItem, btnServe;

    ViewPager pager;
    MyPagerAdapter pagerAdapter;
    ComedityListAdapter adapter1;
    ItemListAdapter adapter2;
    ServeListAdapter adapter3;
    RefreshListView listView1, listView2, listView3;
    View viewBlankPart;
    ImageButton btnWrite;

    private int mPageIndex = 0;

    public void setSelectedIndex(int selectedIndex) {
        this.mPageIndex = selectedIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_write, container, false);

        mContext = getContext();

        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        btnComedity = (ToggleButton)rootView.findViewById(R.id.btn_comedity);
        btnItem = (ToggleButton)rootView.findViewById(R.id.btn_item);
        btnServe = (ToggleButton)rootView.findViewById(R.id.btn_serve);
        edtSearch = (EditText) rootView.findViewById(R.id.edit_search);
        viewBlankPart = (View) rootView.findViewById(R.id.view_blank_part);
        btnWrite = (ImageButton) rootView.findViewById(R.id.btn_write);

        btnComedity.setOnClickListener(mButtonClickListener);
        btnItem.setOnClickListener(mButtonClickListener);
        btnServe.setOnClickListener(mButtonClickListener);
        btnWrite.setOnClickListener(mButtonClickListener);

        listView1 = new RefreshListView(mContext);
        listView1.showFooter(true);
        listView2 = new RefreshListView(mContext);
        listView2.showFooter(true);
        listView3 = new RefreshListView(mContext);
        listView3.showFooter(true);
        adapter1 = new ComedityListAdapter(mContext);
        adapter2 = new ItemListAdapter(mContext);
        adapter3 = new ServeListAdapter(mContext);
        listView1.setDividerHeight(10);
        listView2.setDividerHeight(10);
        listView3.setDividerHeight(10);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listView1.setLayoutParams(params);
        listView2.setLayoutParams(params);
        listView3.setLayoutParams(params);

        pagerAdapter = new MyPagerAdapter();
        pager = (ViewPager) rootView.findViewById(R.id.viewPager);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                selectTopTabButton(position);
                updatePageSelected(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        pager.setCurrentItem(mPageIndex);

        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    reloadData(INDEX_GET_DATA_ALL);
                    return true;
                }
                return false;
            }
        });

        listView1.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshHeader(View view) {
                reloadData(INDEX_GET_DATA_COMEDITY);
            }
            @Override
            public void onRefreshFooter(View view) {
                loadData(INDEX_GET_DATA_COMEDITY);
            }
        });
        listView2.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshHeader(View view) {
                reloadData(INDEX_GET_DATA_ITEM);
            }
            @Override
            public void onRefreshFooter(View view) {
                loadData(INDEX_GET_DATA_ITEM);
            }
        });
        listView3.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshHeader(View view) {
                reloadData(INDEX_GET_DATA_SERVICE);
            }
            @Override
            public void onRefreshFooter(View view) {
                loadData(INDEX_GET_DATA_SERVICE);
            }
        });

        reloadData(INDEX_GET_DATA_ALL);

        if (AppConfig.getInstance().pleixingList == null || AppConfig.getInstance().pleixingList.size() == 0)
            new PleixingListAsync().execute("");
        if (AppConfig.getInstance().itemFenleiList == null || AppConfig.getInstance().itemFenleiList.size() == 0)
            getFenleiListTask(1);
        if (AppConfig.getInstance().serveFenleiList == null || AppConfig.getInstance().serveFenleiList.size() == 0)
            getFenleiListTask(2);

        return rootView;
    }

    public void reloadData(int get_index) {
        if (get_index == INDEX_GET_DATA_ALL) {
            adapter1.mDatas.clear();
            adapter2.mDatas.clear();
            adapter3.mDatas.clear();
        }
        if (get_index == INDEX_GET_DATA_COMEDITY) {
            adapter1.mDatas.clear();
        }
        if (get_index == INDEX_GET_DATA_ITEM) {
            adapter2.mDatas.clear();
        }
        if (get_index == INDEX_GET_DATA_SERVICE) {
            adapter3.mDatas.clear();
        }
        getDataTask(get_index);
    }

    private void loadData(int get_index) {
        getDataTask(get_index);
    }

    private void updatePageSelected(int position) {
        if (position == 0) {
            if (adapter1.getData() == null || adapter1.getData().size() == 0) {
                viewBlankPart.setVisibility(View.VISIBLE);
            } else {
                viewBlankPart.setVisibility(View.GONE);
            }
            if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == Constants.TEST_STATUS_PASSED
                    && SessionInstance.getInstance().getLoginData().getUser().getAkind() == ACCOUNT_TYPE_ENTERPRISE) {
                btnWrite.setVisibility(View.VISIBLE);
            } else {
                btnWrite.setVisibility(View.GONE);
            }
        }
        if (position == 1) {
            if (adapter2.getData() == null || adapter2.getData().size() == 0) {
                viewBlankPart.setVisibility(View.VISIBLE);
            } else {
                viewBlankPart.setVisibility(View.GONE);
            }
            if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == Constants.TEST_STATUS_PASSED) {
                btnWrite.setVisibility(View.VISIBLE);
            } else {
                btnWrite.setVisibility(View.GONE);
            }
        }
        if (position == 2) {
            if (adapter3.getData() == null || adapter3.getData().size() == 0) {
                viewBlankPart.setVisibility(View.VISIBLE);
            } else {
                viewBlankPart.setVisibility(View.GONE);
            }
            if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == Constants.TEST_STATUS_PASSED) {
                btnWrite.setVisibility(View.VISIBLE);
            } else {
                btnWrite.setVisibility(View.GONE);
            }
        }
    }

    public void getDataTask(final int get_index) {
        final String search = edtSearch.getText().toString().trim();
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(getContext());
            }
            @Override
            protected Object doInBackground(Object... params) {
                if (get_index == INDEX_GET_DATA_COMEDITY) {
                    ComedityListModel progressDatas = new SyncInfo(mContext).syncMyProductList(search, adapter1.mDatas.size(), Constants.PAGE_ITEM_COUNT);
                    publishProgress(1, progressDatas);
                }
                if (get_index == INDEX_GET_DATA_ITEM) {
                    ItemListModel successDatas = new SyncInfo(mContext).syncMyItemList(search, adapter2.mDatas.size(), Constants.PAGE_ITEM_COUNT);
                    publishProgress(2, successDatas);
                }
                if (get_index == INDEX_GET_DATA_SERVICE) {
                    ServeListModel failDatas = new SyncInfo(getContext()).syncMyServeList(search, adapter3.mDatas.size(), Constants.PAGE_ITEM_COUNT);
                    publishProgress(3, failDatas);
                }
                if (get_index == INDEX_GET_DATA_ALL) {
                    ComedityListModel progressDatas = new SyncInfo(mContext).syncMyProductList(search, adapter1.mDatas.size(), Constants.PAGE_ITEM_COUNT);
                    publishProgress(1, progressDatas);

                    ItemListModel successDatas = new SyncInfo(mContext).syncMyItemList(search, adapter2.mDatas.size(), Constants.PAGE_ITEM_COUNT);
                    publishProgress(2, successDatas);

                    ServeListModel failDatas = new SyncInfo(getContext()).syncMyServeList(search, adapter3.mDatas.size(), Constants.PAGE_ITEM_COUNT);
                    publishProgress(3, failDatas);
                }

                return null;
            }
            @Override
            protected void onProgressUpdate(Object... values) {
                int index = (int) values[0];
                BaseModel result = (BaseModel) values[1];
                if (result.isValid()) {
                    if(result.getRetCode() == Constants.ERROR_OK) {
                        if (index == 1) {
                            adapter1.setData((ArrayList<ComedityModel>) ((ComedityListModel) result).getList());
                            adapter1.notifyDataSetChanged();
                            if (adapter1.mDatas.size() == 0) {
                                listView1.setVisibility(View.GONE);
                            } else {
                                listView1.setVisibility(View.VISIBLE);
                            }
                        }
                        if (index == 2) {
                            adapter2.setData((ArrayList<ItemModel>) ((ItemListModel) result).getList());
                            adapter2.notifyDataSetChanged();
                            if (adapter2.mDatas.size() == 0) {
                                listView2.setVisibility(View.GONE);
                            } else {
                                listView2.setVisibility(View.VISIBLE);
                            }
                        }
                        if (index == 3) {
                            adapter3.setData((ArrayList<ServeModel>) ((ServeListModel) result).getList());
                            adapter3.notifyDataSetChanged();
                            if (adapter3.mDatas.size() == 0) {
                                listView3.setVisibility(View.GONE);
                            } else {
                                listView3.setVisibility(View.VISIBLE);
                            }
                        }
                    } else if (result.getRetCode() == ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(getActivity());
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
                }
                if (btnComedity.isChecked()) {
                    btnComedity.callOnClick();
                    updatePageSelected(0);
                } else if (btnItem.isChecked()) {
                    btnItem.callOnClick();
                    updatePageSelected(1);
                } else if (btnServe.isChecked()) {
                    btnServe.callOnClick();
                    updatePageSelected(2);
                }
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Utils.disappearProgressDialog();
                listView1.onRefreshCompleteHeader();
                listView1.onRefreshCompleteFooter();
                listView2.onRefreshCompleteHeader();
                listView2.onRefreshCompleteFooter();
                listView3.onRefreshCompleteHeader();
                listView3.onRefreshCompleteFooter();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
                listView1.onRefreshCompleteHeader();
                listView1.onRefreshCompleteFooter();
                listView2.onRefreshCompleteHeader();
                listView2.onRefreshCompleteFooter();
                listView3.onRefreshCompleteHeader();
                listView3.onRefreshCompleteFooter();
            }
        }.execute();
    }

    public void actionUpDownTask(int index, int actionIndex, int id, final OnCallbackListener callbackListener) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(getContext());
            }
            @Override
            protected Object doInBackground(Object... params) {
                int index = (int) params[0];
                int actionIndex = (int) params[1];
                int id = (int) params[2];
                return new SyncInfo(mContext).syncUpDownMyWrite(index, actionIndex, id);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                BaseModel result = (BaseModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == Constants.ERROR_OK) {
                        if (callbackListener != null)
                            callbackListener.onCallback();
                    } else if (result.getRetCode() == ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(getActivity());
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
                }
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute(index, actionIndex, id);
    }

    public void actionDeleteTask(int index, int id, final OnCallbackListener callbackListener) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(getContext());
            }
            @Override
            protected Object doInBackground(Object... params) {
                int index = (int) params[0];
                int id = (int) params[1];
                return new SyncInfo(mContext).syncDeleteMyWrite(index, id);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                BaseModel result = (BaseModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == Constants.ERROR_OK) {
                        Toast.makeText(mContext, R.string.msg_success_delete, Toast.LENGTH_SHORT).show();
                        if (callbackListener != null)
                            callbackListener.onCallback();
                    } else if (result.getRetCode() == ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(getActivity());
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
                }
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute(index, id);
    }

    private class ComedityListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<ComedityModel> mDatas = new ArrayList<ComedityModel>();

        public ComedityListAdapter(Context context) {
            mContext = context;
        }

        public void setData(ArrayList<ComedityModel> datas) {
            mDatas.addAll(datas);
        }

        public ArrayList getData() {
            return mDatas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas == null ? 0 : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_comedity, parent, false);

                holder.imgLogo = (ImageView) convertView.findViewById(R.id.img_logo);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                holder.txtPrice = (TextView) convertView.findViewById(R.id.txt_price);
                holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
                holder.btnEdit = (Button) convertView.findViewById(R.id.btn_edit);
                holder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
                holder.btnAction = (Button) convertView.findViewById(R.id.btn_action);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            convertView.setId(position);

            final ComedityModel item = (ComedityModel) getItem(position);
            String imgLogoPath = null;
            if (item.getImgPaths() != null) {
                for (int i = 0; i < item.getImgPaths().size(); i++) {
                    if (item.getImgPaths().get(i) != null && item.getImgPaths().get(i).length() > 0) {
                        imgLogoPath = item.getImgPaths().get(i);
                        break;
                    }
                }
            }
            //Picasso.with(mContext).load(Constants.FILE_ADDR + item.getAccountLogo()).placeholder(R.drawable.no_image).into(holder.imgLogo);
            Picasso.with(mContext).load(Constants.FILE_ADDR + imgLogoPath).placeholder(R.drawable.no_image).into(holder.imgLogo);
            holder.txtTitle.setText(item.getName());
            holder.txtPrice.setText(String.format("¥%.02f", item.getPrice()));
            holder.txtStatus.setText(item.getStatusName());
            if (item.getStatus() == Constants.COMEDITY_STATUS_DONWLOADED) {
                holder.txtStatus.setText(R.string.already_down_comedity);
                holder.txtStatus.setTextColor(getResources().getColor(R.color.txt_gray));
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnAction.setText(R.string.raise_comedity);
                holder.btnAction.setTextColor(getResources().getColor(R.color.color_orange));
                holder.btnAction.setBackgroundResource(R.drawable.round_bg_no_border_orange);
            } else {
                holder.txtStatus.setText(R.string.already_raise_comedity);
                holder.txtStatus.setTextColor(getResources().getColor(R.color.color_orange));
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnDelete.setVisibility(View.GONE);
                holder.btnAction.setText(R.string.down_comedity);
                holder.btnAction.setTextColor(getResources().getColor(R.color.txt_gray));
                holder.btnAction.setBackgroundResource(R.drawable.round_bg_no_border_gray);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.hideKeyboardFrom(getActivity(), edtSearch);
                        MyComedityDetailFragment fragment = new MyComedityDetailFragment();
                        fragment.setData(MyWriteFragment.this, item);
                        ((MyWriteActivity) mContext).showFragment(fragment, true);
                }
            });
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MakeComedityActivity.class);
                        intent.putExtra("data", item);
                        intent.putExtra("from_where", "MyWriteFragment");
                        startActivityForResult(intent, Constants.REQEUST_CODE_MY_DEPLOY);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
            holder.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int actionIndex = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                    final int confirmStringId = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? R.string.msg_down_confirm_comedity : R.string.msg_up_confirm_comedity);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder
                            .setMessage(confirmStringId)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    actionUpDownTask(1, actionIndex, item.getId(), new OnCallbackListener() {
                                        @Override
                                        public void onCallback() {
                                            item.setStatus(item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                                            adapter1.notifyDataSetChanged();
                                        }
                                    });
                                    dialog.cancel();
                                }
                            })
                            .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder
                                .setMessage(R.string.msg_delete_confirm_commodity)
                                .setCancelable(false)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        actionDeleteTask(1, item.getId(), new OnCallbackListener() {
                                            @Override
                                            public void onCallback() {
                                                adapter1.mDatas.remove(item);
                                                adapter1.notifyDataSetChanged();
                                            }
                                        });
                                        dialog.cancel();
                                    }
                                })
                                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            int id;
            ImageView imgLogo;
            TextView txtTitle, txtPrice, txtStatus;
            Button btnEdit, btnDelete, btnAction;
        }
    }

    private class ItemListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<ItemModel> mDatas = new ArrayList<ItemModel>();

        public ItemListAdapter(Context context) {
            mContext = context;
        }

        public void setData(ArrayList<ItemModel> datas) {
            mDatas = datas;
        }

        public ArrayList getData() {
            return mDatas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas == null ? 0 : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_item, parent, false);

                holder.imgLogo = (ImageView) convertView.findViewById(R.id.img_logo);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                holder.txtFenlei = (TextView) convertView.findViewById(R.id.txt_fenlei);
                holder.txtComment = (TextView) convertView.findViewById(R.id.txt_comment);
                holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
                holder.btnEdit = (Button) convertView.findViewById(R.id.btn_edit);
                holder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
                holder.btnAction = (Button) convertView.findViewById(R.id.btn_action);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            convertView.setId(position);

            final ItemModel item = (ItemModel) getItem(position);
            Picasso.with(mContext).load(Constants.FILE_ADDR + item.getLogo()).placeholder(R.drawable.no_image).into(holder.imgLogo);
            holder.txtTitle.setText(item.getName());
            holder.txtFenlei.setText(item.getFenleiName());
            holder.txtComment.setText("简介：" + item.getComment());
            if (item.getStatus() == Constants.COMEDITY_STATUS_DONWLOADED) {
                holder.txtStatus.setText(R.string.already_down_comedity);
                holder.txtStatus.setTextColor(getResources().getColor(R.color.txt_gray));
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnAction.setText(R.string.raise_comedity);
                holder.btnAction.setTextColor(getResources().getColor(R.color.color_orange));
                holder.btnAction.setBackgroundResource(R.drawable.round_bg_no_border_orange);
            } else {
                holder.txtStatus.setText(R.string.already_raise_comedity);
                holder.txtStatus.setTextColor(getResources().getColor(R.color.color_orange));
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnDelete.setVisibility(View.GONE);
                holder.btnAction.setText(R.string.down_comedity);
                holder.btnAction.setTextColor(getResources().getColor(R.color.txt_gray));
                holder.btnAction.setBackgroundResource(R.drawable.round_bg_no_border_gray);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.hideKeyboardFrom(getActivity(), edtSearch);
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        MyItemDetailFragment fragment = new MyItemDetailFragment();
                        fragment.setData(MyWriteFragment.this, item);
                        ((MyWriteActivity) mContext).showFragment(fragment, true);
                    }
                }
            });
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        Intent intent = new Intent(getActivity(), MakeItemActivity.class);
                        intent.putExtra("data", item);
                        intent.putExtra("from_where", "MyWriteFragment");
                        startActivityForResult(intent, Constants.REQEUST_CODE_MY_DEPLOY);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });
            holder.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        final int actionIndex = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                        final int confirmStringId = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? R.string.msg_down_confirm_item : R.string.msg_up_confirm_item);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder
                                .setMessage(confirmStringId)
                                .setCancelable(false)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        actionUpDownTask(2, actionIndex, item.getId(), new OnCallbackListener() {
                                            @Override
                                            public void onCallback() {
                                                item.setStatus(item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                                                adapter2.notifyDataSetChanged();
                                            }
                                        });
                                        dialog.cancel();
                                    }
                                })
                                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder
                                .setMessage(R.string.msg_delete_confirm_item)
                                .setCancelable(false)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        actionDeleteTask(2, item.getId(), new OnCallbackListener() {
                                            @Override
                                            public void onCallback() {
                                                adapter2.mDatas.remove(item);
                                                adapter2.notifyDataSetChanged();
                                            }
                                        });
                                        dialog.cancel();
                                    }
                                })
                                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            });

            return convertView;
        }

        private class ViewHolder {
            int id;
            ImageView imgLogo;
            TextView txtTitle, txtFenlei, txtComment, txtStatus;
            Button btnEdit, btnDelete, btnAction;
        }
    }

    private class ServeListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<ServeModel> mDatas = new ArrayList<ServeModel>();

        public ServeListAdapter(Context context) {
            mContext = context;
        }

        public void setData(ArrayList<ServeModel> datas) {
            mDatas = datas;
        }

        public ArrayList getData() {
            return mDatas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas == null ? 0 : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_item, parent, false);

                holder.imgLogo = (ImageView) convertView.findViewById(R.id.img_logo);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                holder.txtFenlei = (TextView) convertView.findViewById(R.id.txt_fenlei);
                holder.txtComment = (TextView) convertView.findViewById(R.id.txt_comment);
                holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
                holder.btnEdit = (Button) convertView.findViewById(R.id.btn_edit);
                holder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
                holder.btnAction = (Button) convertView.findViewById(R.id.btn_action);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            convertView.setId(position);

            final ServeModel item = (ServeModel) getItem(position);
            Picasso.with(mContext).load(Constants.FILE_ADDR + item.getLogo()).placeholder(R.drawable.no_image).into(holder.imgLogo);
            holder.txtTitle.setText(item.getName());
            holder.txtFenlei.setText(item.getFenleiName());
            holder.txtComment.setText("简介：" + item.getComment());
            if (item.getStatus() == Constants.COMEDITY_STATUS_DONWLOADED) {
                holder.txtStatus.setText(R.string.already_down_comedity);
                holder.txtStatus.setTextColor(getResources().getColor(R.color.txt_gray));
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnAction.setText(R.string.raise_comedity);
                holder.btnAction.setTextColor(getResources().getColor(R.color.color_orange));
                holder.btnAction.setBackgroundResource(R.drawable.round_bg_no_border_orange);
            } else {
                holder.txtStatus.setText(R.string.already_raise_comedity);
                holder.txtStatus.setTextColor(getResources().getColor(R.color.color_orange));
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnDelete.setVisibility(View.GONE);
                holder.btnAction.setText(R.string.down_comedity);
                holder.btnAction.setTextColor(getResources().getColor(R.color.txt_gray));
                holder.btnAction.setBackgroundResource(R.drawable.round_bg_no_border_gray);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.hideKeyboardFrom(getActivity(), edtSearch);
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        MyServeDetailFragment fragment = new MyServeDetailFragment();
                        fragment.setData(MyWriteFragment.this, item);
                        ((MyWriteActivity) mContext).showFragment(fragment, true);
                    }
                }
            });
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        Intent intent = new Intent(getActivity(), MakeServeActivity.class);
                        intent.putExtra("data", item);
                        intent.putExtra("from_where", "MyWriteFragment");
                        startActivityForResult(intent, Constants.REQEUST_CODE_MY_DEPLOY);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });
            holder.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        final int actionIndex = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                        final int confirmStringId = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? R.string.msg_down_confirm_service : R.string.msg_up_confirm_service);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder
                                .setMessage(confirmStringId)
                                .setCancelable(false)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        actionUpDownTask(3, actionIndex, item.getId(), new OnCallbackListener() {
                                            @Override
                                            public void onCallback() {
                                                item.setStatus(item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                                                adapter3.notifyDataSetChanged();
                                            }
                                        });
                                        dialog.cancel();
                                    }
                                })
                                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder
                                .setMessage(R.string.msg_delete_confirm_service)
                                .setCancelable(false)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        actionDeleteTask(3, item.getId(), new OnCallbackListener() {
                                            @Override
                                            public void onCallback() {
                                                adapter3.mDatas.remove(item);
                                                adapter3.notifyDataSetChanged();
                                            }
                                        });
                                        dialog.cancel();
                                    }
                                })
                                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            });

            return convertView;
        }

        private class ViewHolder {
            int id;
            ImageView imgLogo;
            TextView txtTitle, txtFenlei, txtComment, txtStatus;
            Button btnEdit, btnDelete, btnAction;
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ListView itemView = listView1;
            switch (position) {
                case 0:
                    itemView = listView1;
                    break;
                case 1:
                    itemView = listView2;
                    break;
                case 2:
                    itemView = listView3;
                    break;
            }
            container.removeView(itemView);
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ListView itemView = listView1;
            switch (position) {
                case 0:
                    itemView = listView1;
                    break;
                case 1:
                    itemView = listView2;
                    break;
                case 2:
                    itemView = listView3;
                    break;
            }
            ((ViewPager) container).addView(itemView, 0);
            return itemView;
        }
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyWriteActivity parent = (MyWriteActivity)getActivity();
            switch (v.getId()) {
                case R.id.btn_comedity:
                    selectTopTabButton(0);
                    pager.setCurrentItem(0);
                    break;
                case R.id.btn_item:
                    selectTopTabButton(1);
                    pager.setCurrentItem(1);
                    break;
                case R.id.btn_serve:
                    selectTopTabButton(2);
                    pager.setCurrentItem(2);
                    break;
                case R.id.btn_write:
                    Intent intent = new Intent(getActivity(), MakeComedityActivity.class);;
                    if (pager.getCurrentItem() == 0) {
                        intent = new Intent(getActivity(), MakeComedityActivity.class);
                    }
                    if (pager.getCurrentItem() == 1) {
                        intent = new Intent(getActivity(), MakeItemActivity.class);
                    }
                    if (pager.getCurrentItem() == 2) {
                        intent = new Intent(getActivity(), MakeServeActivity.class);
                    }
                    intent.putExtra("from_where", "MyWriteFragment");
                    startActivityForResult(intent, Constants.REQEUST_CODE_MY_DEPLOY);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.btn_back:
                    parent.onBackActivity();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQEUST_CODE_MY_DEPLOY) {
            reloadData(INDEX_GET_DATA_ALL);
        }
        if (resultCode == Constants.RESULT_CODE_LOGIN_DUPLICATE) {
            ChengxinApplication.finishActivityFromDuplicate(getActivity());
        }
    }

    private void selectTopTabButton(int position) {
        btnComedity.setChecked(false);
        btnItem.setChecked(false);
        btnServe.setChecked(false);
        switch (position) {
            case 0:
                btnComedity.setChecked(true);
                break;
            case 1:
                btnItem.setChecked(true);
                break;
            case 2:
                btnServe.setChecked(true);
                break;
        }
        CommonUtils.hideKeyboardFrom(getActivity(), edtSearch);
    }

    class PleixingListAsync extends AsyncTask<String, String, XyleixingListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected XyleixingListModel doInBackground(String... strs) {
            return new SyncInfo(mContext).syncPleixingList(strs[0]);
        }
        @Override
        protected void onPostExecute(XyleixingListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().pleixingList.clear();
                    AppConfig.getInstance().pleixingList.addAll(result.getList());
                } else if (result.getRetCode() == ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
                } else {
                    Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mContext, getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private void getFenleiListTask(final int index) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected FenleiListModel doInBackground(Object... strs) {
                if (index == 1)
                    return new SyncInfo(mContext).syncItemFenleiList();
                else if (index == 2)
                    return new SyncInfo(mContext).syncServeFenleiList();

                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                BaseModel result = (BaseModel) o;
                if (result .isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        if (index == 1) {
                            AppConfig.getInstance().itemFenleiList.clear();
                            FenleiListModel tmp = (FenleiListModel) result;
                            AppConfig.getInstance().itemFenleiList.addAll(tmp.getList());
                        } else if (index == 2) {
                            AppConfig.getInstance().serveFenleiList.clear();
                            FenleiListModel tmp = (FenleiListModel) result;
                            AppConfig.getInstance().serveFenleiList.addAll(tmp.getList());
                        }
                    } else if (result.getRetCode() == ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(getActivity());
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
        }.execute();
    }

}

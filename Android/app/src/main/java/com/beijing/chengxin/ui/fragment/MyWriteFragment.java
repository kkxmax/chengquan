package com.beijing.chengxin.ui.fragment;

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

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.ComedityListModel;
import com.beijing.chengxin.network.model.ComedityModel;
import com.beijing.chengxin.network.model.ItemListModel;
import com.beijing.chengxin.network.model.ItemModel;
import com.beijing.chengxin.network.model.ServeListModel;
import com.beijing.chengxin.network.model.ServeModel;
import com.beijing.chengxin.ui.activity.MakeComedityActivity;
import com.beijing.chengxin.ui.activity.MakeItemActivity;
import com.beijing.chengxin.ui.activity.MakeServeActivity;
import com.beijing.chengxin.ui.activity.MyWriteActivity;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.beijing.chengxin.config.Constants.ACCOUNT_TYPE_ENTERPRISE;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class MyWriteFragment extends Fragment {

    private Context mContext;
    private View rootView;

    EditText edtSearch;
    ToggleButton btnComedity, btnItem, btnServe;

    ViewPager pager;
    MyPagerAdapter pagerAdapter;
    ComedityListAdapter adapter1;
    ItemListAdapter adapter2;
    ServeListAdapter adapter3;
    ListView listView1, listView2, listView3;
    View viewBlankPart;
    ImageButton btnWrite;

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

        listView1 = new ListView(mContext);
        listView2 = new ListView(mContext);
        listView3 = new ListView(mContext);
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

        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    getDataTask(edtSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        getDataTask("");

        return rootView;
    }

    private void updatePageSelected(int position) {
        if (position == 0) {
            if (adapter1.getData() == null || adapter1.getData().size() == 0) {
                viewBlankPart.setVisibility(View.VISIBLE);
            } else {
                viewBlankPart.setVisibility(View.GONE);
            }
            if (SessionInstance.getInstance().getLoginData().getUser().getAkind() == ACCOUNT_TYPE_ENTERPRISE) {
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
            btnWrite.setVisibility(View.VISIBLE);
        }
        if (position == 2) {
            if (adapter3.getData() == null || adapter3.getData().size() == 0) {
                viewBlankPart.setVisibility(View.VISIBLE);
            } else {
                viewBlankPart.setVisibility(View.GONE);
            }
            btnWrite.setVisibility(View.VISIBLE);
        }
    }

    private void getDataTask(final String search) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(getContext());
            }
            @Override
            protected Object doInBackground(Object... params) {
                ComedityListModel progressDatas = new SyncInfo(mContext).syncMyProductList(search);
                publishProgress(1, progressDatas);

                ItemListModel successDatas = new SyncInfo(mContext).syncMyItemList(search);
                publishProgress(2, successDatas);

                ServeListModel failDatas = new SyncInfo(getContext()).syncMyServeList(search);
                publishProgress(3, failDatas);

                return null;
            }
            @Override
            protected void onProgressUpdate(Object... values) {
                int index = (int) values[0];
                BaseModel result = (BaseModel) values[1];
                if (result.isValid()) {
                    if(result.getRetCode() == Constants.ERROR_OK) {
                        if (index == 1) {
                            if (((ComedityListModel) result).getList() == null || ((ComedityListModel) result).getList().size() == 0) {
                                listView1.setVisibility(View.GONE);
                            } else {
                                listView1.setVisibility(View.VISIBLE);
                            }
                            adapter1.setData((ArrayList<ComedityModel>) ((ComedityListModel) result).getList());
                            adapter1.notifyDataSetChanged();
                        }
                        if (index == 2) {
                            if (((ItemListModel) result).getList() == null || ((ItemListModel) result).getList().size() == 0) {
                                listView2.setVisibility(View.GONE);
                            } else {
                                listView2.setVisibility(View.VISIBLE);
                            }
                            adapter2.setData((ArrayList<ItemModel>) ((ItemListModel) result).getList());
                            adapter2.notifyDataSetChanged();
                        }
                        if (index == 3) {
                            if (((ServeListModel) result).getList() == null || ((ServeListModel) result).getList().size() == 0) {
                                listView3.setVisibility(View.GONE);
                            } else {
                                listView3.setVisibility(View.VISIBLE);
                            }
                            adapter3.setData((ArrayList<ServeModel>) ((ServeListModel) result).getList());
                            adapter3.notifyDataSetChanged();
                        }
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
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute();
    }

    private void actionUpDownTask(int index, int actionIndex, int id) {
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
                        getDataTask(edtSearch.getText().toString().trim());
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

    private void actionDeleteTask(int index, int id) {
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
                        getDataTask(edtSearch.getText().toString().trim());
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
        private ArrayList<ComedityModel> mDatas;

        public ComedityListAdapter(Context context) {
            mContext = context;
        }

        public void setData(ArrayList<ComedityModel> datas) {
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
            holder.txtPrice.setText("￥ " + item.getPrice());
            holder.txtStatus.setText(item.getStatusName());
            if (item.getStatus() == Constants.COMEDITY_STATUS_DONWLOADED) {
                holder.txtStatus.setTextColor(getResources().getColor(R.color.txt_gray));
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnAction.setText(R.string.raise_comedity);
                holder.btnAction.setTextColor(getResources().getColor(R.color.color_orange));
                holder.btnAction.setBackgroundResource(R.drawable.round_bg_no_border_orange);
            } else {
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
                    MyComedityDetailFragment fragment = new MyComedityDetailFragment();
                    ((MyWriteActivity) mContext).addFragment(fragment);
                }
            });
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        Intent intent = new Intent(getActivity(), MakeComedityActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });
            holder.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int actionIndex = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                    actionUpDownTask(1, actionIndex, item.getId());
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.ok);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(R.string.msg_delete_confirm)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    actionDeleteTask(1, item.getId());
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
        private ArrayList<ItemModel> mDatas;

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
            holder.txtComment.setText(item.getComment());
            if (item.getStatus() == Constants.COMEDITY_STATUS_DONWLOADED) {
                holder.txtStatus.setTextColor(getResources().getColor(R.color.txt_gray));
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnAction.setText(R.string.raise_comedity);
                holder.btnAction.setTextColor(getResources().getColor(R.color.color_orange));
                holder.btnAction.setBackgroundResource(R.drawable.round_bg_no_border_orange);
            } else {
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
                    MyItemDetailFragment fragment = new MyItemDetailFragment();
                    ((MyWriteActivity) mContext).addFragment(fragment);
                }
            });
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        Intent intent = new Intent(getActivity(), MakeItemActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });
            holder.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int actionIndex = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                    actionUpDownTask(2, actionIndex, item.getId());
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.ok);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(R.string.msg_delete_confirm)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    actionDeleteTask(2, item.getId());
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
            TextView txtTitle, txtFenlei, txtComment, txtStatus;
            Button btnEdit, btnDelete, btnAction;
        }
    }

    private class ServeListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<ServeModel> mDatas;

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
            holder.txtComment.setText(item.getComment());
            if (item.getStatus() == Constants.COMEDITY_STATUS_DONWLOADED) {
                holder.txtStatus.setTextColor(getResources().getColor(R.color.txt_gray));
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnAction.setText(R.string.raise_comedity);
                holder.btnAction.setTextColor(getResources().getColor(R.color.color_orange));
                holder.btnAction.setBackgroundResource(R.drawable.round_bg_no_border_orange);
            } else {
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
                    MyItemDetailFragment fragment = new MyItemDetailFragment();
                    ((MyWriteActivity) mContext).addFragment(fragment);
                }
            });
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        Intent intent = new Intent(getActivity(), MakeServeActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });
            holder.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int actionIndex = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                    actionUpDownTask(3, actionIndex, item.getId());
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info).setTitle(R.string.ok);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(R.string.msg_delete_confirm)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    actionDeleteTask(3, item.getId());
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
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
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
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    break;
                case R.id.btn_back:
                    parent.onBackActivity();
                    break;
            }
        }
    };

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
    }
}

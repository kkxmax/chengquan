package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class EvalDetailFragment extends Fragment {

	public final String TAG = EvalDetailFragment.class.getName();

    private View rootView;
    private EditText editMsg;
    ListView listView;
    ListAdapter listAdapter;
    EvalModel evalInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_eval_detail, container, false);

        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.eval_detail));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_send).setOnClickListener(mButtonClickListener);

        editMsg = (EditText)rootView.findViewById(R.id.edit_msg);

        listView = (ListView) rootView.findViewById(R.id.listView);
        listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);

        return rootView;
    }

    public void setEvalModel(EvalModel model) {
        evalInfo = model;
        if (listAdapter != null)
            listAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    ((BaseFragmentActivity)getActivity()).onBackPressed();
                    break;
                case R.id.btn_send:
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getActivity().getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), 0);
                    }
                    String msg = editMsg.getText().toString();
                    if (!msg.equals(""))
                        new OnReplyAsync().execute(msg);
                    break;
            }
        }
    };

    public class ListAdapter extends BaseAdapter {

        public final String TAG = ListAdapter.class.getName();
        private Context mContext;
        private View mRootView;

        @Override
        public int getCount() {
            if (evalInfo != null) {
                if (evalInfo.getReplys() != null)
                    return evalInfo.getReplys().size() + 1;
                else
                    return 1;
            }
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0)
                return evalInfo;
            return evalInfo.getReplys().get(position - 1);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (position == 0) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eval_detail, parent, false);

                ImageView imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                TextView txtName = (TextView) convertView.findViewById(R.id.txt_name);
                TextView txtEvalType = (TextView) convertView.findViewById(R.id.txt_eval_type);
                TextView txtEvalContent = (TextView) convertView.findViewById(R.id.txt_eval_content);
                HorizontalScrollView hscrollView = (HorizontalScrollView) convertView.findViewById(R.id.scroll_img);
                LinearLayout layout_images = (LinearLayout) convertView.findViewById(R.id.layout_images);
                TextView txtDate= (TextView) convertView.findViewById(R.id.txt_date);
                final TextView txtElectCnt = (TextView) convertView.findViewById(R.id.txt_elect_cnt);
                TextView txtEvaluate = (TextView) convertView.findViewById(R.id.txt_evaluate);
                TextView txtError = (TextView) convertView.findViewById(R.id.txt_error);

                Picasso.with(getContext())
                        .load(Constants.FILE_ADDR + evalInfo.getOwnerLogo())
                        .placeholder(R.drawable.no_image)
                        .into(imgAvatar);
                if (evalInfo.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON)
                    txtName.setText(evalInfo.getOwnerRealname());
                else
                    txtName.setText(evalInfo.getOwnerEnterName());

                txtEvalType.setText(evalInfo.getKindName());
                txtEvalContent.setText(evalInfo.getContent());
                txtDate.setText(evalInfo.getWriteTimeString());
                txtElectCnt.setText(String.format("(%d)", evalInfo.getElectCnt()));
                txtEvaluate.setText(String.format("(%d)", evalInfo.getReplys().size()));
                if (evalInfo.getIsElectedByMe() == 1)
                    txtElectCnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_sel), null, null, null);
                else
                    txtElectCnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_nor), null, null, null);

                txtElectCnt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(evalInfo.getIsElectedByMe() == 1) {
                            evalInfo.setIsElectedByMe(0);
                            evalInfo.setElectCnt(evalInfo.getElectCnt() - 1);
                            txtElectCnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_nor), null, null, null);
                            txtElectCnt.setText(String.format("(%d)", evalInfo.getElectCnt()));
                        }
                        else {
                            evalInfo.setIsElectedByMe(1);
                            evalInfo.setElectCnt(evalInfo.getElectCnt() + 1);
                            txtElectCnt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.zan_sel), null, null, null);
                            txtElectCnt.setText(String.format("(%d)", evalInfo.getElectCnt()));
                        }
                        new AsyncTask<String, String, BaseModel>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                Utils.displayProgressDialog(getContext());
                            }
                            @Override
                            protected BaseModel doInBackground(String... strs) {
                                return new SyncInfo(getContext()).syncElect(strs[0], strs[1]);
                            }
                            @Override
                            protected void onPostExecute(BaseModel result) {
                                super.onPostExecute(result);
                                if (result .isValid()) {
                                    if(result.getRetCode() == ERROR_OK) {
                                    } else {
                                        Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
                                }
                                Utils.disappearProgressDialog();
                            }
                            @Override
                            protected void onCancelled() {
                                super.onCancelled();
                                Utils.disappearProgressDialog();
                            }
                        }.execute(String.valueOf(evalInfo.getId()), String.valueOf(evalInfo.getIsElectedByMe()));
                    }
                });

                txtError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // correct error interface
                    }
                });

                if (evalInfo.getImgPaths().size() > 0) {
                    List<String> imgList = evalInfo.getImgPaths();
                    int listImgWidth = (int) (getResources().getDisplayMetrics().density * 120);;
                    int listImgHeight = (int) (getResources().getDisplayMetrics().density * 90);;
                    for (int i = 0; i < imgList.size(); i++) {
                        ImageView imgView = new ImageView(getContext());
                        imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(listImgWidth, listImgHeight);
                        lparams.setMargins(0, 0, 8, 0);
                        imgView.setLayoutParams(lparams);

                        Picasso.with(getContext())
                                .load(Constants.FILE_ADDR + imgList.get(i))
                                .placeholder(R.drawable.no_image)
                                .into(imgView);
                        layout_images.addView(imgView);
                    }
                } else {
                    hscrollView.setVisibility(View.GONE);
                }
            }
            else {
                EvalModel item = evalInfo.getReplys().get(position-1);
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eval_comment, parent, false);
                ImageView imgAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                TextView txtEvalContent = (TextView) convertView.findViewById(R.id.txt_eval_content);
                TextView txtDate= (TextView) convertView.findViewById(R.id.txt_date);

                Picasso.with(getContext())
                        .load(Constants.FILE_ADDR + evalInfo.getOwnerLogo())
                        .placeholder(R.drawable.no_image)
                        .into(imgAvatar);
                String name;
                if (item.getOwnerAkind() == Constants.ACCOUNT_TYPE_PERSON)
                    name = item.getOwnerRealname();
                else
                    name =item.getOwnerEnterName();

                txtEvalContent.setText(name + ":  " + item.getContent());
                txtDate.setText(item.getWriteTimeString());
            }

            return convertView;
        }

    }

    class OnReplyAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            SyncInfo info = new SyncInfo(getContext());
            return info.syncLeaveReply(String.valueOf(evalInfo.getId()), strs[0]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    EvalModel reply = new EvalModel();
                    UserModel me = SessionInstance.getInstance().getLoginData().getUser();
                    reply.setOwnerLogo(me.getLogo());
                    reply.setOwnerAkind(me.getAkind());
                    reply.setOwnerEnterName(me.getEnterName());
                    reply.setOwnerRealname(me.getRealname());
                    reply.setContent(editMsg.getText().toString());
                    Calendar c = new GregorianCalendar();
                    String time = String.format("%d-%02d-%02d %02d:%02d:%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE),c.get(Calendar.HOUR) ,c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
                    reply.setWriteTimeString(time);
                    editMsg.setText("");

                    List<EvalModel> replys = evalInfo.getReplys();
                    replys.add(0, reply);
                    evalInfo.setReplys(replys);
                    listAdapter.notifyDataSetChanged();
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

package com.beijing.chengxin.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beijing.chengxin.R;

import java.util.List;

public class EvalDetailActivity extends ParentFragmentActivity {

    public final String TAG = EvalDetailActivity.class.getName();

    ListView listView;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval_detail);

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.eval_detail));
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ListAdapter(this, null, 0);
        listView.setAdapter(listAdapter);
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

    public class ListAdapter extends BaseAdapter {

        public final String TAG = ListAdapter.class.getName();
        private Context mContext;
        private View mRootView;

        public ListAdapter(Context context, List list, int listType) {
//        listBoxs = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return 10;//listBoxs.size();
        }

        @Override
        public Object getItem(int position) {
            return position;// listBoxs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (position == 0)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eval_detail, parent, false);
            else
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eval_comment, parent, false);

            return convertView;
        }

    }
}

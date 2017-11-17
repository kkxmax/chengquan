package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchHistoryFragment extends Fragment {

	public final String TAG = SearchHistoryFragment.class.getName();

    private View rootView;

    private EditText editSearch;
    private ListView listView;
    private HistoryListAdapter mAdapter;
    int mainTabIndex;
    int subIndex;
    List<String> keyArray;

    public void setCurrentFragmentIndex(int mainTabIndex, int subIndex) {
        this.mainTabIndex = mainTabIndex;
        this.subIndex = subIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_history, container, false);
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_search_delete).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_search).setOnClickListener(mButtonClickListener);

        editSearch = (EditText)rootView.findViewById(R.id.edit_search);
        listView = (ListView)rootView.findViewById(R.id.listView);

        HashSet<String> hash = AppConfig.getInstance().getStringSetValue(Constants.SEARCH_HOME_KEYWORD_VALUE, null);
        keyArray = new ArrayList<String>();
        if (hash != null) {
            for (String  entry : hash) {
                keyArray.add(entry);
            }
        }

        mAdapter = new HistoryListAdapter(getContext());
        listView.setAdapter(mAdapter);

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
            switch (v.getId()) {
                case R.id.btn_search:
                    String searchStr = editSearch.getText().toString().trim();
                    if (searchStr.equals("")) {

                    } else {
                        if (mainTabIndex == Constants.SEARCH_IN_HOME) {
                            keyArray.add(String.format("%d%s", subIndex, searchStr));
                            if (keyArray.size() > 10)
                                keyArray.remove(0);
                            Set<String> set = new HashSet<String >();
                            for (int i = 0; i < keyArray.size(); i++)
                                set.add(keyArray.get(i));
                            AppConfig.getInstance().setStringSetValue(Constants.SEARCH_HOME_KEYWORD_VALUE, set);
                        }
                    }
                    parent.onBackActivity();
                    MainActivity.mainActivity.setKeyword(searchStr, subIndex);
                    break;
                case R.id.btn_search_delete:
                    if (mainTabIndex == Constants.SEARCH_IN_HOME) {
                        keyArray.clear();
                        AppConfig.getInstance().setStringSetValue(Constants.SEARCH_HOME_KEYWORD_VALUE, null);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case R.id.btn_back:
                    parent.goBack();
                    break;
            }
        }
    };

    public class HistoryListAdapter extends BaseAdapter {
        private Context mContext;

        public HistoryListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            if (keyArray != null)
                return keyArray.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return keyArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
            String str = keyArray.get(position);
            String title = "";
            final String  keyword;
            final int tabIndex;
            if (mainTabIndex == Constants.SEARCH_IN_HOME) {
                keyword = str.substring(1);
                tabIndex = Integer.valueOf(str.substring(0,1));
                switch (tabIndex) {
                    case Constants.SEARCH_HOME_FAMILIAR:
                        title = keyword + " " + getString(R.string.familiar);
                        break;
                    case Constants.SEARCH_HOME_ENTERPRISE:
                        title = keyword + " " + getString(R.string.enterprise);
                        break;
                    case Constants.SEARCH_HOME_COMEDITY:
                        title = keyword + " " + getString(R.string.comedity);
                        break;
                    case Constants.SEARCH_HOME_ITEM:
                        title = keyword + " " + getString(R.string.item);
                        break;
                    case Constants.SEARCH_HOME_SERVE:
                        title = keyword + " " + getString(R.string.serve);
                        break;
                    case Constants.SEARCH_HOME_CODE:
                        title = keyword + " " + getString(R.string.chengxin_number);
                        break;
                }
            } else {
                keyword = "";
                tabIndex = 0;
            }
            ToggleButton button = (ToggleButton)convertView.findViewById(R.id.txt_title);
            button.setTextOff(title);
            button.setTextOn(title);
            button.setChecked(false);

            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
                    parent.onBackActivity();
                    MainActivity.mainActivity.setKeyword(keyword, tabIndex);
                }
            });

            return convertView;
        }

    }
}

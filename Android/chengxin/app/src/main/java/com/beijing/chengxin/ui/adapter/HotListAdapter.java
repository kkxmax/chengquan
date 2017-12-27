package com.beijing.chengxin.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.MainHomeFragment;

import java.util.List;

public class HotListAdapter extends BaseAdapter {

    public final String TAG = HotListAdapter.class.getName();

//    private List<PlayingItemResponse> listBoxs;
    private Context mContext;
    private View mRootView;
    public interface OnListItemClickListener {
        public void onItemClick(int pos);
    }

    OnListItemClickListener listener;

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.listener = listener;
    }

    public HotListAdapter(Context context, List list, int listType) {
//        listBoxs = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return 10;//listBoxs.size();
    }

    @Override
    public Object getItem(int position) {
        return 1;// listBoxs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
//        final PlayingItemResponse data = (PlayingItemResponse) listBoxs.get(position);
        if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot, parent, false);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClick(position);
            }
        });

//        Picasso.with(parent.getContext())
//            .load(Constants.IMG_MODEL_URL + data.getUserid() + File.separator + data.getFirst())
//            .placeholder(R.drawable.no_image)
//            .into((ImageView) convertView.findViewById(R.id.imgPhoto));

        return convertView;
    }

}

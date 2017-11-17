package com.beijing.chengxin.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.MainHomeFragment;
import java.util.List;

public class HomeListAdapter extends BaseAdapter {

    public final String TAG = HomeListAdapter.class.getName();

//    private List<PlayingItemResponse> listBoxs;
    private Context mContext;
    private int listType;
    private View mRootView;
    private OnListItemClickListener itemClickListener;

    public interface OnListItemClickListener {
        void onListItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnListItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public int getListType() {return listType;}

    public HomeListAdapter(Context context, List list, int listType) {
//        listBoxs = list;
        mContext = context;
        this.listType = listType;
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
        if (convertView == null) {
            if (listType == MainHomeFragment.INDEX_FAMILIAR) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
                ImageView imgBlank = (ImageView)convertView.findViewById(R.id.img_blank);
                if (position % 2 == 0)
                    imgBlank.setImageResource(R.drawable.blank_enterprise);
                else
                    imgBlank.setImageResource(R.drawable.blank_person);
            }
            else if (listType == MainHomeFragment.INDEX_ENTERPRISE)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enterprise, parent, false);
            else if (listType == MainHomeFragment.INDEX_ITEM || listType == MainHomeFragment.INDEX_SERVE )
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_serve, parent, false);
            else if (listType == MainHomeFragment.INDEX_COMEDITY)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comedity, parent, false);
        }

        if (itemClickListener != null) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onListItemClick(position, view);
                }
            });
        }

//        Picasso.with(parent.getContext())
//            .load(Constants.IMG_MODEL_URL + data.getUserid() + File.separator + data.getFirst())
//            .placeholder(R.drawable.no_image)
//            .into((ImageView) convertView.findViewById(R.id.imgPhoto));

        return convertView;
    }

}

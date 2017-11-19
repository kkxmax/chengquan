package com.beijing.chengxin.ui.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.widget.recycling.RecyclingPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

//import com.bumptech.glide.Glide;


/**
 * UrlImagePagerAdapter
 *
 * @author ssk 
 */
public class UrlImagePagerAdapter extends RecyclingPagerAdapter {

    private List<String> imageUrlList;

    private boolean isInfiniteLoop;

    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public UrlImagePagerAdapter(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        if (imageUrlList.isEmpty())
            return 0;
        else
            return isInfiniteLoop ? Integer.MAX_VALUE : imageUrlList.size();
    }

    /**
     * get really item count
     *
     * @return count
     */
    public int getRealCount() {
        return imageUrlList.size();
    }

    /**
     * get really position
     *
     * @param position image position
     * @return position
     */
    private int getPosition(int position) {

        return isInfiniteLoop ? position % imageUrlList.size() : position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
        if (view == null) {
            view = new ImageView(container.getContext());
            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        Picasso.with(container.getContext())
                .load(String.valueOf(imageUrlList.get(getPosition(position))))
                .placeholder(R.drawable.no_image)
                .skipMemoryCache()
                .into((ImageView) view);
        if (itemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(getPosition(position), view);
                }
            });
        }
        return view;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public UrlImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

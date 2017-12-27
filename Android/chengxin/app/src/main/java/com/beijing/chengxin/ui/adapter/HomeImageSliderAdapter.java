package com.beijing.chengxin.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.CarouselImageModel;
import com.beijing.chengxin.ui.widget.recycling.RecyclingPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

//import com.bumptech.glide.Glide;


/**
 * UrlImagePagerAdapter
 *
 * @author ssk 
 */
public class HomeImageSliderAdapter extends RecyclingPagerAdapter {

    private List<CarouselImageModel> imageUrlList;

    private boolean isInfiniteLoop;

    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

        public HomeImageSliderAdapter(List<CarouselImageModel> imageUrlList) {
        this.imageUrlList = imageUrlList;
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        if (imageUrlList == null || imageUrlList.isEmpty())
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
        return imageUrlList == null ? 0 : imageUrlList.size();
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
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image_slider, container, false);
        }

        CarouselImageModel item = imageUrlList.get(getPosition(position));

        ImageView imgBg = (ImageView)view.findViewById(R.id.img_bg);
        ImageView imgPlay = (ImageView)view.findViewById(R.id.btn_play);

        Picasso.with(container.getContext())
                .load(Constants.FILE_ADDR +item.getImgUrl())
                .placeholder(R.drawable.no_image)
                .skipMemoryCache()
                .into(imgBg);
        if (item.getKind() == Constants.MEDIA_TYPE_VIDEO) {
            imgPlay.setVisibility(View.VISIBLE);
        } else {
            imgPlay.setVisibility(View.GONE);
        }

        if (itemClickListener != null) {
            imgPlay.setOnClickListener(new View.OnClickListener() {
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
    public HomeImageSliderAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

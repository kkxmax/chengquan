package com.beijing.chengxin.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beijing.chengxin.R;

public class SimpleSortSpinner extends TextView implements View.OnClickListener {

    private static int mSelection = 0;
    private OnItemSelectListener mListener;

    private PopupWnd mPopupWnd = null;
    private Context mContext;

    public SimpleSortSpinner(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SimpleSortSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public SimpleSortSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public int getSelIndex() {
        return mSelection;
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        mListener = listener;
    }

    private void init() {
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mPopupWnd != null) {
            mPopupWnd.dismiss();
            mPopupWnd = null;
        }
        switchPopup();
    }

    public void setSelection(int selection) {
        mSelection = selection;
        if (mPopupWnd != null)
            mPopupWnd.initSelect();
    }

    public int getSelection() {
        return mSelection;
    }

    private void switchPopup() {
//        mSelection = AppConfig.getInstance().getIntValue(Constants.SEARCH_ORDER, 0);
        if (mPopupWnd == null) {
            mPopupWnd = new PopupWnd(getContext());
            mPopupWnd.mListener = mSelectListener;
        }

        mPopupWnd.showAsDropDown(this);
    }

    private OnItemSelectListener mSelectListener = new OnItemSelectListener() {
        @Override
        public void onItemSelected(int index) {
            mSelection = index;
            mPopupWnd.dismiss();
            mPopupWnd = null;
            if (mListener != null) {
                mListener.onItemSelected(index);
            }
        }
    };

    private static class PopupWnd extends PopupWindow {

        public OnItemSelectListener mListener;
        private int mItemHeight;
        private View viewOut;
        private View viewInterst;
        private View viewTrust;
        private View viewLast;
        private TextView txtInterest;
        private ImageView imgInterest;
        private TextView txtTrust;
        private ImageView imgTrust;
        private TextView txtLast;
        private ImageView imgLast;

        public PopupWnd(Context context) {
            super(context);

            setAnimationStyle(0);

            Drawable drawable = context.getResources().getDrawable(R.color.color_black_trans);

            setOutsideTouchable(true);
            setBackgroundDrawable(drawable);

            Rect padding = new Rect();
            drawable.getPadding(padding);
            float dpi = context.getResources().getDisplayMetrics().density;
            mItemHeight = (int) (33.3f * dpi);

            setWidth(context.getResources().getDisplayMetrics().widthPixels);
//            setHeight(context.getResources().getDisplayMetrics().heightPixels);
            //int xxx = (int) (getResources().getDisplayMetrics().density * 40);

            LinearLayout sortView = new LinearLayout(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.view_sort, sortView);

            viewOut = sortView.findViewById(R.id.view_out) ;
            viewInterst = (View) sortView.findViewById(R.id.layout_interest);
            viewTrust = (View) sortView.findViewById(R.id.layout_trust);
            viewLast = (View) sortView.findViewById(R.id.layout_last);
            txtInterest = (TextView) sortView.findViewById(R.id.txt_interest);
            imgInterest = (ImageView) sortView.findViewById(R.id.img_interest);
            txtTrust = (TextView) sortView.findViewById(R.id.txt_trust);
            imgTrust = (ImageView) sortView.findViewById(R.id.img_trust);
            txtLast = (TextView) sortView.findViewById(R.id.txt_last);
            imgLast = (ImageView) sortView.findViewById(R.id.img_last);

            txtInterest.setTextColor((mSelection == 0) ? context.getResources().getColor(R.color.txt_gray) : context.getResources().getColor(R.color.txt_gray_light));
            imgInterest.setVisibility((mSelection == 0) ? View.VISIBLE : View.GONE);
            txtTrust.setTextColor((mSelection == 1) ? context.getResources().getColor(R.color.txt_gray) : context.getResources().getColor(R.color.txt_gray_light));
            imgTrust.setVisibility((mSelection == 1) ? View.VISIBLE : View.GONE);
            txtLast.setTextColor((mSelection == 2) ? context.getResources().getColor(R.color.txt_gray) : context.getResources().getColor(R.color.txt_gray_light));
            imgLast.setVisibility((mSelection == 2) ? View.VISIBLE : View.GONE);

            viewOut.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            viewInterst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelection != 0)
                        mListener.onItemSelected(0);
                }
            });
            viewTrust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelection != 1)
                        mListener.onItemSelected(1);
                }
            });
            viewLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelection != 2)
                        mListener.onItemSelected(2);
                }
            });

            setContentView(sortView);
            setFocusable(true);
            sortView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                        dismiss();
                    }
                    return false;
                }
            });
        }

        public void initSelect() {
            imgInterest.setVisibility((mSelection == 0) ? View.VISIBLE : View.GONE);
            imgTrust.setVisibility((mSelection == 1) ? View.VISIBLE : View.GONE);
            imgLast.setVisibility((mSelection == 2) ? View.VISIBLE : View.GONE);
        }
    }

    public static interface OnItemSelectListener {
        public void onItemSelected(int index);
    }

}

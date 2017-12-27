package com.beijing.chengxin.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.utils.CommonUtils;

public class RefreshListView extends ListView {

    private static final int STATE_HEADER_NORMAL = 0;
    private static final int STATE_HEADER_PULL = 1;
    private static final int STATE_HEADER_READY = 2;
    private static final int STATE_HEADER_REFRESH = 3;
    private static final int STATE_FOOTER_NORMAL = 10;
    private static final int STATE_FOOTER_REFRESH = 11;

    private static final int HEADER_HEIGHT_DP = 58;

    private int mHeaderState = STATE_HEADER_NORMAL;
    private int mFooterState = STATE_FOOTER_NORMAL;

    /** header view */
    private View mHeaderFrame = null;
    private View mHeaderView = null;
    private ProgressBar mProgress = null;

    /** footer view */
    private View mFooterFrame = null;
    private View mFooterView = null;

    private float mY = 0;
    private float mHistoricalY = 0;
    private int mInitialHeight = 0;
    private boolean mFlag = false;
    private int mHeaderHeight = 0;
    private OnRefreshListener mListener = null;

    private boolean mIsArrowUp = false;
    private boolean mIsShowFooter = false;

    private Animation mRotateAnimation;
    private FlingRunnable mRunnable;
    private int scaledTouchSlop;

    public RefreshListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mRunnable = new FlingRunnable();
        mRotateAnimation = new RotateAnimation(180.f, 0.f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(200);

        ViewConfiguration config = ViewConfiguration.get(context);
        scaledTouchSlop = config.getScaledTouchSlop();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeaderFrame = inflater.inflate(R.layout.refresh_list_header, this, false);
        mHeaderView = mHeaderFrame.findViewById(R.id.refresh_frame);
        mProgress = (ProgressBar) mHeaderFrame.findViewById(R.id.pull_to_refresh_progress);
        addHeaderView(mHeaderFrame);

        mHeaderHeight = (int) (HEADER_HEIGHT_DP * context.getResources().getDisplayMetrics().density);
        setHeaderHeight(0);

        mFooterFrame = inflater.inflate(R.layout.refresh_list_footer, this, false);
        mFooterView = mFooterFrame.findViewById(R.id.refresh_frame);
    }

    public void setOnRefreshListener(final OnRefreshListener listener) {
        mListener = listener;
    }

    public void onRefreshCompleteHeader() {
        changeHeaderState(STATE_HEADER_NORMAL);
        mRunnable.startFling(0, mHeaderHeight);
        invalidateViews();
    }

    public void onRefreshCompleteFooter() {
        changeFooterState(STATE_FOOTER_NORMAL);
//		mRunnable.startFling(0, mHeaderHeight);
        invalidateViews();
    }

    public void showFooter(boolean show) {
        if (show == mIsShowFooter) {
            return;
        }

        if (show) {
            addFooterView(mFooterFrame);
            mFooterView.setVisibility(GONE);

            setOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (visibleItemCount > 0 && totalItemCount > 0 && firstVisibleItem > 0 &&
                            firstVisibleItem + visibleItemCount == totalItemCount) {
                        changeFooterState(STATE_FOOTER_REFRESH);
                    }
                }
            });
        } else {
            removeFooterView(mFooterFrame);
            setOnScrollListener(null);
        }

        mIsShowFooter = show;
    }


    @Override
    public void setSelectionFromTop(int position, int y) {
        position ++;
        super.setSelectionFromTop(position, y);
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mHeaderState != STATE_HEADER_REFRESH) {
                    mY = mHistoricalY = ev.getY();
                    if (mHeaderFrame.getLayoutParams() != null) {
                        mInitialHeight = mHeaderFrame.getLayoutParams().height;
                    }
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                switch (mHeaderState) {
                    case STATE_HEADER_PULL:
                    case STATE_HEADER_NORMAL:
                        if (getChildAt(0).getTop() == 0) {
                            mRunnable.startFling(0, (int) (ev.getY() - mY) / 2 + mInitialHeight);
                        }
                        break;
                    case STATE_HEADER_READY:
                        changeHeaderState(STATE_HEADER_REFRESH);
                        mRunnable.startFling(mHeaderHeight, (int) (ev.getY() - mY) / 2 + mInitialHeight);
                        break;
                    case STATE_HEADER_REFRESH:
                        mRunnable.startFling(mHeaderHeight, (int) (ev.getY() - mY) / 2 + mInitialHeight);
                        break;
                    default:
                        break;
                }

                mFlag = false;
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        if ((ev.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            CommonUtils.hideKeyboardFrom(getContext(), this);
        }

        if (mHeaderState != STATE_HEADER_REFRESH &&
                ev.getAction() == MotionEvent.ACTION_MOVE &&
                getFirstVisiblePosition() == 0 &&
                !mRunnable.isRunning()) {

            float direction = ev.getY() - mHistoricalY;
            int height = (int) (ev.getY() - mY) / 2 + mInitialHeight;
            if (height < 0) {
                height = 0;
            }

            if (direction != 0) {
                float deltaY = Math.abs(mY - ev.getY());
                if (deltaY > scaledTouchSlop) {
                    if (direction > 0) { // Scrolling downward
                        // Refresh bar is extended if top pixel of the first item is visible
                        if (getChildAt(0) != null && getChildAt(0).getTop() == 0) {

                            // Extends refresh bar
                            setHeaderHeight(height);

                            // Stop list scroll to prevent the list from overscrolling
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            mFlag = false;
                        }
                    } else { // Scrolling upward
                        // Refresh bar is shortened if top pixel of the first item is visible
                        if (getChildAt(0) != null && getChildAt(0).getTop() == 0) {
                            setHeaderHeight(height);

                            // If scroll reaches top of the list, list scroll is enabled
                            if (getChildAt(1) != null && getChildAt(1).getTop() <= 1 && !mFlag) {
                                ev.setAction(MotionEvent.ACTION_DOWN);
                                mFlag = true;
                            }
                        }
                    }
                }
            }

            mHistoricalY = ev.getY();
        }

        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean performItemClick(final View view, final int position, final long id) {
        if (position == 0) {
            // This is the refresh header element
            return true;
        } else {
            return super.performItemClick(view, position - 1, id);
        }
    }

    private void setHeaderHeight(final int height) {
        if (height <= 1) {
            mHeaderView.setVisibility(GONE);
        } else {
            mHeaderView.setVisibility(VISIBLE);
        }

        // Extends refresh bar
        LayoutParams lp = (LayoutParams) mHeaderFrame.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        lp.height = height;
        mHeaderFrame.setLayoutParams(lp);

        // Refresh bar shows up from bottom to top
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHeaderView.getLayoutParams();
        if (params == null) {
            params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        params.topMargin = -mHeaderHeight + height;
        mHeaderView.setLayoutParams(params);

        // If scroll reaches the trigger line, start refreshing
        if (mHeaderState != STATE_HEADER_REFRESH) {
            if (height == 0) {
                changeHeaderState(STATE_HEADER_NORMAL);
            } else if (height > mHeaderHeight) {
                changeHeaderState(STATE_HEADER_READY);
            } else if (height < mHeaderHeight) {
                changeHeaderState(STATE_HEADER_PULL);
            } else {
            }
        }
    }

    /*private void rotateArrow() {
        Drawable drawable = mArrowView.getDrawable();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        canvas.rotate(180.0f, canvas.getWidth() / 2.0f, canvas.getHeight() / 2.0f);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        canvas.restore();
        mArrowView.setImageBitmap(bitmap);
        mIsArrowUp = !mIsArrowUp;
    }*/

    private void changeHeaderState(int state) {
        if (mHeaderState != state) {
            mHeaderState = state;

            switch (mHeaderState) {
                case STATE_HEADER_NORMAL:
                    mProgress.setVisibility(GONE);
                    break;
                case STATE_HEADER_PULL:
                    break;
                case STATE_HEADER_READY:
                    break;
                case STATE_HEADER_REFRESH:
                    mProgress.setVisibility(VISIBLE);
                    if (mListener != null) {
                        mListener.onRefreshHeader(this);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void changeFooterState(int state) {
        if (mFooterState != state) {
            mFooterState = state;

            switch(mFooterState) {
                case STATE_FOOTER_NORMAL:
                    mFooterView.setVisibility(GONE);
                    break;
                case STATE_FOOTER_REFRESH:
                    mFooterView.setVisibility(VISIBLE);
                    if (mListener != null) {
                        mListener.onRefreshFooter(this);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private class FlingRunnable implements Runnable {
        private int mValue;
        private int mLimit;
        private boolean mIsRunning;

        public FlingRunnable() {
            mValue = 0;
            mLimit = 0;
            mIsRunning = false;
        }

        public void startFling(int limit, int startValue) {
            mLimit = limit;
            mValue = startValue;
            stop();
            mIsRunning = true;
            post(this);
        }

        public void stop() {
            removeCallbacks(this);
            mIsRunning = false;
        }

        public boolean isRunning() {
            return mIsRunning;
        }

        @Override
        public void run() {
            if (mValue >= mLimit) {
                setHeaderHeight(mValue);
                int displacement = (mValue - mLimit) / 10;
                if (displacement == 0) {
                    mValue --;
                    post(this);
                } else {
                    mValue -= displacement;
                    post(this);
                }
            } else {
                mIsRunning = false;
            }
        }
    }


    public static interface OnRefreshListener {
        public void onRefreshHeader(View view);
        public void onRefreshFooter(View view);
    }

}

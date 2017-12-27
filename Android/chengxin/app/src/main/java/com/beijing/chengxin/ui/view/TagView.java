package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TagView extends ViewGroup {

	private int mItemBackground;
	private int mItemColor;

	private int mMaxLines;
	private int mContentHeight;
	private int mViewPortY;

	private int mFixedCount;
	private int mItemMaxLength;
	private OnTagItemSelectListener listener;

	private static final int MARGIN_ITEM = 8;

	public TagView(Context context) {
		super(context);
		init(context);
	}

	public TagView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init(context);
	}

	public TagView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	protected void init(Context context) {
		mItemBackground = 0;
		mItemColor = Color.BLACK;
		mMaxLines = 10;
		mContentHeight = 0;
		mViewPortY = 0;
		mItemMaxLength = 10;
		mFixedCount = 0;
		//mGestureDetector = new GestureDetector(context, mGestureListener);
		//setItemRemoveIcon(android.R.drawable.ic_delete);

		//setClickable(true);
	}

	public void setOnTagItemSelectListener(OnTagItemSelectListener listener) {
		this.listener = listener;
	}

	public void setItemBackground(int resId) {
		mItemBackground = resId;
	}

//	public void setItemRemoveIcon(int iconId) {
//		mItemRemoveIcon = getContext().getResources().getDrawable(iconId);
//		mItemRemoveIcon.setBounds(0, 0, mItemRemoveIcon.getIntrinsicWidth(), mItemRemoveIcon.getIntrinsicHeight());
//	}

	public void setItemColor(int color) {
		mItemColor = color;
	}

	public void setFixedView() {
		mFixedCount = getChildCount();
	}

	public void setItemMaxLength(int max) {
		mItemMaxLength = max;
	}

	public void addItem(String title, Object data) {
		addItem(title, data, mItemBackground, mItemColor);
	}

	public void addItem(String title, Object data, int bgResId, int color) {
		if (title.length() > mItemMaxLength) {
			title = title.substring(0, mItemMaxLength) + "...";
		}

		final TextView viewItem = new TextView(getContext());
		viewItem.setText(title);
		viewItem.setTextColor(color);
		viewItem.setSingleLine(true);
		viewItem.setTag(data);
		viewItem.setOnClickListener(mClickListener);
		if (bgResId != 0) {
			viewItem.setBackgroundResource(bgResId);
		}
		viewItem.setPadding(20, 12, 20, 12);
		//viewItem.setCompoundDrawablePadding(6);
		//viewItem.setCompoundDrawables(null, null, mItemRemoveIcon, null);

		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (mFixedCount == 0) {
			addView(viewItem, lp);
		} else {
			addView(viewItem, getChildCount() - mFixedCount, lp);
		}
	}

	public void removeItem(Object item) {
		View view = findViewWithTag(item);
		if (view != null) {
			removeView(view);
		}
	}

	public void clear() {
		final int count = getChildCount() - mFixedCount;
		for (int i = count - 1; i >= 0; i--) {
			removeViewAt(i);
		}
	}

	protected boolean hasFixed() {
		return (mFixedCount > 0);
	}

	protected void onItemRemoved(Object item) {
	}

	private View.OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Object item = view.getTag();
			if (listener != null) {
				listener.onTagSelected(item);
			} else {
				removeView(view);
				onItemRemoved(item);
			}
		}
	};

	SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			scrollByOffset((int) -distanceY);
			return false;
		}
	};

	private void scrollByOffset(int offset) {
		int height = getHeight();

		if (height >= mContentHeight) {
			return;
		}

		int oldY = mViewPortY;
		mViewPortY -= offset;

		if (mViewPortY < 0) {
			mViewPortY = 0;
		} else if (mViewPortY + height > mContentHeight) {
			mViewPortY = mContentHeight - height;
		}

		if (oldY == mViewPortY) {
			return;
		}

		offset = oldY - mViewPortY;
		int childCount = getChildCount();
		for (int i = childCount - 1; i >= 0; i--) {
			View child = getChildAt(i);
			child.offsetTopAndBottom(offset);
		}

		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mContentHeight = 0;

		final int widthSize = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		final int width = widthSize - getPaddingLeft() - getPaddingRight();
		int heightSize = 0;

		if (width > 0) {
			int lineHeight = 0;
			int lines = 0;
			int maxHeight = 0;

			int childX = 0;
			final int count = getChildCount();

			int childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

			if (count > 0) {
				for (int i = 0; i < count; i++) {
					final View child = getChildAt(i);

					if (child != null && child.getVisibility() != GONE) {
						measureChild(child, widthMeasureSpec, childHeightSpec);

						int childWidth = child.getMeasuredWidth();
						int childHeight = child.getMeasuredHeight();

						lineHeight = Math.max(lineHeight, childHeight);

						if (childX + childWidth > width) {
							childX = 0;
							mContentHeight += lineHeight + MARGIN_ITEM;

							if (++lines == mMaxLines && maxHeight == 0) {
								maxHeight = mContentHeight + getPaddingTop() + getPaddingBottom();
							}
							lineHeight = childHeight;
						}

						childX += childWidth + MARGIN_ITEM;
					}
				}

				mContentHeight += getPaddingTop() + getPaddingBottom() + lineHeight;
				heightSize = (maxHeight == 0) ? mContentHeight : maxHeight;
			}
		}

		heightSize = Math.max(heightSize, resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec));
		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		if (count == 0) {
			return;
		}

		int height = getHeight();
		if (height >= mContentHeight) {
			mViewPortY = 0;
		} else if (mContentHeight - mViewPortY < height) {
			mViewPortY = mContentHeight - height;
		}

		int childLeft = 0;
		int childTop = 0;
		final int selfLeft = getPaddingLeft();
		final int selfTop = getPaddingTop() - mViewPortY;
		final int width = getWidth() - selfLeft - getPaddingRight();
		int lineHeight = 0;

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			if (child != null && child.getVisibility() != GONE) {
				ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams)child.getLayoutParams();
				if (lp == null) {
					lp = (ViewGroup.LayoutParams) generateDefaultLayoutParams();
				}

				child.setLayoutParams(lp);

				int childWidth = child.getMeasuredWidth();
				int childHeight = child.getMeasuredHeight();
				lineHeight = Math.max(lineHeight, childHeight);

				if (childLeft + childWidth > width) {
					childLeft = 0;
					childTop += lineHeight + MARGIN_ITEM;
					lineHeight = childHeight;
				}

				child.layout(selfLeft + childLeft, selfTop + childTop, selfLeft + childLeft + childWidth, selfTop + childTop + childHeight);

				childLeft += childWidth + MARGIN_ITEM;
			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);

		//mGestureDetector.onTouchEvent(ev);

		return super.dispatchTouchEvent(ev);
	}

	public interface OnTagItemSelectListener {
		void onTagSelected(Object tag);
	}

}
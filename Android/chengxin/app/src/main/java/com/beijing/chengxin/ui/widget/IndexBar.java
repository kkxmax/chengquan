package com.beijing.chengxin.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.beijing.chengxin.R;

public class IndexBar extends View {

	private static final char[] INDEXS = new char[] {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	private ListView mListView;
	private SectionIndexer mIndexer;
	private TextView mTxtPopup;
	private Paint mPaint;//The Paint class holds the style and color information about how to draw geometries, text and bitmaps.


	public IndexBar(Context context) {
		super(context);
		init();
	}

	public IndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public IndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mPaint = new Paint();
		//mPaint.setColor(Color.GRAY);//Set the paint's color.
        mPaint.setColor(getResources().getColor(R.color.color_main_blue));//Set the paint's color.
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(20);
		mPaint.setTextAlign(Paint.Align.CENTER);
	}

	public void setWidgets(ListView listView, SectionIndexer indexer, TextView popup) {
		mListView = listView;
		mIndexer = indexer;
		mTxtPopup = popup;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		final Paint paint = mPaint;
		float centerOfW = getMeasuredWidth() / 2;
		float height = getMeasuredHeight() / INDEXS.length;//

		for (int i = 0; i < INDEXS.length; i++) {
			canvas.drawText(String.valueOf(INDEXS[i]), centerOfW, (i + 1) * height, paint);//Draw the text, with origin at (x,y), using the specified paint.
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		int action = event.getAction() & MotionEvent.ACTION_MASK;//event.getAction()Return the kind of action being performed
		int index;

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				//mPaint.setColor(Color.GRAY);
                mPaint.setColor(getResources().getColor(R.color.txt_blue_gray));
				invalidate();
			case MotionEvent.ACTION_MOVE:
				index  = (int) (event.getY() / (getMeasuredHeight() / INDEXS.length));
				index = index < 0 ? 0 : (index >= INDEXS.length) ? INDEXS.length - 1 : index;

				mTxtPopup.setVisibility(View.VISIBLE);
				mTxtPopup.setText(String.valueOf(INDEXS[index]));

				int position = mIndexer.getPositionForSection(INDEXS[index]);

				if (position >= 0) {
					mListView.setSelection(position);
				}
				break;
			default:
				//mPaint.setColor(Color.GRAY);
                mPaint.setColor(getResources().getColor(R.color.txt_blue_gray));
				mTxtPopup.setVisibility(View.INVISIBLE);
				invalidate();
				break;
		}

		return true;
	}

}

/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beijing.chengxin.R;

import handmark.pulltorefresh.library.ILoadingLayout;
import handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import handmark.pulltorefresh.library.internal.*;
import handmark.pulltorefresh.library.internal.Utils;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {

	static final String LOG_TAG = "PullToRefresh-LoadingLayout";

	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

	private FrameLayout mInnerLayout;

	protected final ImageView mHeaderImage;
	protected final ProgressBar mHeaderProgress;

	private boolean mUseIntrinsicAnimation;

	protected final Mode mMode;
	protected final Orientation mScrollDirection;

	private CharSequence mPullLabel;
	private CharSequence mRefreshingLabel;
	private CharSequence mReleaseLabel;

	public LoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
		super(context);
		mMode = mode;
		mScrollDirection = scrollDirection;

		switch (scrollDirection) {
			case HORIZONTAL:
				LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_horizontal, this);
				break;
			case VERTICAL:
			default:
				LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_vertical, this);
				break;
		}

		mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
		mHeaderProgress = (ProgressBar) mInnerLayout.findViewById(R.id.pull_to_refresh_progress);
		mHeaderImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);

		LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();

		switch (mode) {
			case PULL_FROM_END:
				lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;

				// Load in labels
				mPullLabel = context.getString(R.string.pull_down_to_refresh);
				mRefreshingLabel = context.getString(R.string.is_being_updated);
				mReleaseLabel = context.getString(R.string.release_to_update);
				break;

			case PULL_FROM_START:
			default:
				lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;

				// Load in labels
				mPullLabel = context.getString(R.string.pull_down_to_refresh);
				mRefreshingLabel = context.getString(R.string.is_being_updated);
				mReleaseLabel = context.getString(R.string.release_to_update);
				break;
		}

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
			Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
			if (null != background) {
				ViewCompat.setBackground(this, background);
			}
		}

		// Try and get defined drawable from Attrs
		Drawable imageDrawable = null;
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
			imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawable);
		}

		// Check Specific Drawable from Attrs, these overrite the generic
		// drawable attr above
		switch (mode) {
			case PULL_FROM_START:
			default:
				if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
				} else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)) {
					Utils.warnDeprecation("ptrDrawableTop", "ptrDrawableStart");
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
				}
				break;

			case PULL_FROM_END:
				if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
				} else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
					Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
				}
				break;
		}

		// If we don't have a user defined drawable, load the default
		if (null == imageDrawable) {
			imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
		}

		// Set Drawable, and save width/height
		setLoadingDrawable(imageDrawable);

		reset();
	}

	public final void setHeight(int height) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.height = height;
		requestLayout();
	}

	public final void setWidth(int width) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.width = width;
		requestLayout();
	}

	public final int getContentSize() {
		switch (mScrollDirection) {
			case HORIZONTAL:
				return mInnerLayout.getWidth();
			case VERTICAL:
			default:
				return mInnerLayout.getHeight();
		}
	}

	public final void hideAllViews() {
		if (View.VISIBLE == mHeaderProgress.getVisibility()) {
			mHeaderProgress.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderImage.getVisibility()) {
			mHeaderImage.setVisibility(View.INVISIBLE);
		}
	}

	public final void onPull(float scaleOfLayout) {
		if (!mUseIntrinsicAnimation) {
			onPullImpl(scaleOfLayout);
		}
	}

	public final void pullToRefresh() {
		// Now call the callback
		pullToRefreshImpl();
	}

	public final void refreshing() {
		if (mUseIntrinsicAnimation) {
			((AnimationDrawable) mHeaderImage.getDrawable()).start();
		} else {
			// Now call the callback
			refreshingImpl();
		}
	}

	public final void releaseToRefresh() {
		// Now call the callback
		releaseToRefreshImpl();
	}

	public final void reset() {
		mHeaderImage.setVisibility(View.VISIBLE);

		if (mUseIntrinsicAnimation) {
			((AnimationDrawable) mHeaderImage.getDrawable()).stop();
		} else {
			// Now call the callback
			resetImpl();
		}
	}

	public final void setLoadingDrawable(Drawable imageDrawable) {
		// Set Drawable
		mHeaderImage.setImageDrawable(imageDrawable);
		mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

		// Now call the callback
		onLoadingDrawableSet(imageDrawable);
	}

	public void setPullLabel(CharSequence pullLabel) {
		mPullLabel = pullLabel;
	}

	public void setRefreshingLabel(CharSequence refreshingLabel) {
		mRefreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(CharSequence releaseLabel) {
		mReleaseLabel = releaseLabel;
	}

	public final void showInvisibleViews() {
		if (View.INVISIBLE == mHeaderProgress.getVisibility()) {
			mHeaderProgress.setVisibility(View.VISIBLE);
		}
		if (View.INVISIBLE == mHeaderImage.getVisibility()) {
			mHeaderImage.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Callbacks for derivative Layouts
	 */

	protected abstract int getDefaultDrawableResId();

	protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

	protected abstract void onPullImpl(float scaleOfLayout);

	protected abstract void pullToRefreshImpl();

	protected abstract void refreshingImpl();

	protected abstract void releaseToRefreshImpl();

	protected abstract void resetImpl();

}
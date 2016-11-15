package com.hcz.core.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.ImageView;

/**
 * 可实现缩放的ImageView
 * 
 * @author cuihz
 *
 */
public class ScaleImageView extends ImageView {

	private ScaleGestureDetector mScaleDetector;
	private boolean mScaling;
	private boolean mMoving;
	private long mFirstclick;
	private float mStartX, mStartY;
	private float mImageLeft;
	private float mImageTop;
	private float mImageWidth;
	private float mImageHeight;
	private float mCurrentScale;

	public ScaleImageView(Context context) {
		// 调用自己的构造参数，不是是父类的，这样为了能够在创建时执行初始化的代码
		this(context, null);
	}

	public ScaleImageView(Context context, AttributeSet attrs) {
		// 调用自己的构造参数，不是是父类的，这样为了能够在创建时执行初始化的代码
		this(context, attrs, 0);
	}

	public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setScaleType(ScaleType.MATRIX);
		mScaleDetector = new ScaleGestureDetector(this.getContext(),
				new OnScaleGestureListener() {
					@Override
					public boolean onScale(ScaleGestureDetector detector) {
						scale(detector.getScaleFactor(), detector.getFocusX(),
								detector.getFocusY());
						return true;
					}

					@Override
					public boolean onScaleBegin(ScaleGestureDetector detector) {
						mScaling = true;
						return true;
					}

					@Override
					public void onScaleEnd(ScaleGestureDetector detector) {
					}
				});

		initImageState();
	}

	private void scale(float scale, float touchX, float touchY) {
		Matrix matrix = new Matrix();
		matrix.set(getImageMatrix());
		matrix.postScale(scale, scale, touchX, touchY);
		setImageMatrix(matrix);

		/**
		 * 根据不同的情况，限制图片的移动范围，共8种情况。
		 */
		getImageState(matrix);
		if (scale > 1) {
			// 第1、2种情况，当图片高度比view高度小的时候，放大时保证图片的顶部底部不超出view的范围
			if (mImageHeight <= getHeight()) {
				if (mImageTop < 0) {
					matrix.postTranslate(0, -mImageTop);
					setImageMatrix(matrix);
				}
				if (mImageTop + mImageHeight > getHeight()) {
					matrix.postTranslate(0, getHeight() - mImageTop
							- mImageHeight);
					setImageMatrix(matrix);
				}
			}
			// 第3、4种情况，当图片宽度比view宽度小的时候，放大时保证图片的顶部底部不超出view的范围
			if (mImageWidth <= getWidth()) {
				if (mImageLeft < 0) {
					matrix.postTranslate(-mImageLeft, 0);
					setImageMatrix(matrix);
				}
				if (mImageLeft + mImageWidth > getWidth()) {
					matrix.postTranslate(getWidth() - mImageLeft - mImageWidth,
							0);
					setImageMatrix(matrix);
				}
			}
		} else {
			// 第5、6种情况，当图片高度比view高度大的时候，缩小时保证图片的顶部底部一定要超出view的范围，防止上下出现空白
			if (mImageHeight >= getHeight()) {
				if (mImageTop > 0) {
					matrix.postTranslate(0, -mImageTop);
					setImageMatrix(matrix);
				}
				if (mImageTop + mImageHeight < getHeight()) {
					matrix.postTranslate(0, getHeight() - mImageTop
							- mImageHeight);
					setImageMatrix(matrix);
				}
			}
			// 第7、8种情况，当图片宽度比view宽度大的时候，缩小时保证图片的左右侧一定要超出view的范围，防止左右出现空白
			if (mImageWidth >= getWidth()) {
				if (mImageLeft > 0) {
					matrix.postTranslate(-mImageLeft, 0);
					setImageMatrix(matrix);
				}
				if (mImageLeft + mImageWidth < getWidth()) {
					matrix.postTranslate(getWidth() - mImageLeft - mImageWidth,
							0);
					setImageMatrix(matrix);
				}
			}
		}
	}

	private void move(float x, float y) {
		Matrix matrix = new Matrix();
		matrix.set(getImageMatrix());
		matrix.postTranslate(x, y);
		setImageMatrix(matrix);

		/**
		 * 根据不同的情况，限制图片的移动范围，共8种情况。
		 */
		getImageState(matrix);
		if (y > 0) {
			// 第1种情况，当图片高度比view高度大的时候，下拉时图片的顶部和view的顶部对齐时，则不再下移
			if (mImageHeight >= getHeight() && mImageTop > 0) {
				matrix.postTranslate(0, -mImageTop);
				setImageMatrix(matrix);
			}
			// 第2种情况，当图片高度比view高度小的时候，下拉时图片的底部和view的底部对齐时，则不再下移
			if (mImageHeight < getHeight()
					&& mImageTop + mImageHeight > getHeight()) {
				matrix.postTranslate(0, getHeight() - mImageTop - mImageHeight);
				setImageMatrix(matrix);
			}
		}
		if (y < 0) {
			// 第3种情况，当图片高度比view高度小的时候，上拉时图片的顶部和view的顶部对齐时，则不再上移
			if (mImageHeight <= getHeight() && mImageTop < 0) {
				matrix.postTranslate(0, -mImageTop);
				setImageMatrix(matrix);
			}
			// 第4种情况，当图片高度比view高度大的时候，上拉时图片的底部和view的底部对齐时，则不再上移
			if (mImageHeight > getHeight()
					&& mImageTop + mImageHeight < getHeight()) {
				matrix.postTranslate(0, getHeight() - mImageTop - mImageHeight);
				setImageMatrix(matrix);
			}
		}
		if (x > 0) {
			// 第5种情况，当图片宽度比view宽度大的时候，右拉时图片的左侧和view的左侧对齐时，则不再右移
			if (mImageWidth >= getWidth() && mImageLeft > 0) {
				matrix.postTranslate(-mImageLeft, 0);
				setImageMatrix(matrix);
			}
			// 第6种情况，当图片宽度比view宽度小的时候，右拉时图片的右侧和view的右侧对齐时，则不再右移
			if (mImageWidth < getWidth()
					&& mImageLeft + mImageWidth > getWidth()) {
				matrix.postTranslate(getWidth() - mImageLeft - mImageWidth, 0);
				setImageMatrix(matrix);
			}
		}
		if (x < 0) {
			// 第7种情况，当图片宽度比view宽小的时候，左拉时图片的左侧和view的左侧对齐时，则不再左移
			if (mImageWidth <= getWidth() && mImageLeft < 0) {
				matrix.postTranslate(-mImageLeft, 0);
				setImageMatrix(matrix);
			}
			// 第8种情况，当图片宽度比view宽度大的时候，左拉时图片的右侧和view的右侧对齐时，则不再左移
			if (mImageWidth > getWidth()
					&& mImageLeft + mImageWidth < getWidth()) {
				matrix.postTranslate(getWidth() - mImageLeft - mImageWidth, 0);
				setImageMatrix(matrix);
			}
		}
	}

	/**
	 * 通过values和rect可以得到imageview中图片在view中的位置和实际宽高 left values[2] top values[5]
	 * right values[2] + rect.width() * values[0] bottom values[5] +
	 * rect.height() * values[0] width rect.width() * values[0] height
	 * rect.height() * values[0]
	 */
	private void getImageState(Matrix matrix) {
		Rect rect = getDrawable().getBounds();
		float[] values = new float[9];
		matrix.getValues(values);
		mCurrentScale = values[0];
		mImageLeft = values[2];
		mImageTop = values[5];
		mImageWidth = rect.width() * values[0];
		mImageHeight = rect.height() * values[0];
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		mScaleDetector.onTouchEvent(event);
		super.dispatchTouchEvent(event);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d("ScaleImageView", "ACTION_DOWN");
			mStartX = event.getRawX();
			mStartY = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d("ScaleImageView", "ACTION_MOVE");
			if (mScaling) {
				return false;
			}
			float moveX = event.getRawX() - mStartX;
			float moveY = event.getRawY() - mStartY;
			if(Math.abs(moveX) > 20 || Math.abs(moveY) > 20){
				mMoving = true;
			}
			move(moveX, moveY);
			mStartX = event.getRawX();
			mStartY = event.getRawY();
			break;
		case MotionEvent.ACTION_UP:
			Log.d("ScaleImageView", "ACTION_UP");
			if (!mScaling && !mMoving) {
				long doubleClick = System.currentTimeMillis() - mFirstclick;
				if (doubleClick < 1000) {
					doubleClick(event);
				} else {
					mFirstclick = System.currentTimeMillis();
				}
			}
			mMoving = false;
			mScaling = false;
			break;
		}
		return super.onTouchEvent(event);
	}

	
	/**
	 * 双击事件。当图片宽高大于view的情况，缩小到初始情况；否则，将图片缩放到正常大小
	 * @param event
	 */
	private void doubleClick(MotionEvent event) {
		mFirstclick = 0;
		getImageState(getImageMatrix());
		if (mImageWidth > getWidth() || mImageHeight > getHeight()) {
			initImageState();
		} 
		else {
			scale(1 / mCurrentScale, event.getRawX(), event.getRawY());
		}
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		initImageState();
	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.d("ScaleImageView", "onSizeChanged," + getWidth());
		initImageState();
	}

	
	/**
	 * 初始化图片的状态
	 * 当图片真实大小比view大的情况，保证图片在view范围内
	 * 否则，保持图片大小，在view中居中。
	 */
	private void initImageState() {
		if (getScaleType().equals(ScaleType.MATRIX)) {
			Matrix matrix = new Matrix();
			Drawable d = getDrawable();
			if (getWidth() == 0 || getHeight() == 0) {
				return;
			}
			if (d.getIntrinsicWidth() > getWidth()
					|| d.getIntrinsicHeight() > getHeight()) {
				RectF dstR = new RectF(0, 0, getWidth(), getHeight());
				RectF srcR = new RectF(0, 0, d.getIntrinsicWidth(),
						d.getIntrinsicHeight());
				matrix.setRectToRect(srcR, dstR, ScaleToFit.CENTER);
				setImageMatrix(matrix);
			} else {
				matrix.set(getImageMatrix());
				matrix.reset();
				int x = (getWidth() - d.getIntrinsicWidth()) / 2 - getLeft();
				int y = (getHeight() - d.getIntrinsicHeight()) / 2 - getTop();
				matrix.postTranslate(x, y);
				setImageMatrix(matrix);
			}
		}
	}

}

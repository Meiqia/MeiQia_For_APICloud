package com.meiqia.meiqiasdk.widget;

import java.lang.reflect.Field;

import com.meiqia.meiqiasdk.util.MQResUtils;
import com.meiqia.meiqiasdk.util.MQUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com 创建时间:16/3/29 上午11:32 描述:
 */
public class MQImageView extends ImageView {
	private int mDefaultImageId;
	private int mCornerRadius = 0;
	private boolean mIsCircle = false;
	private boolean mIsSquare = false;
	private int mBorderWidth = 0;
	private int mBorderColor = Color.WHITE;
	private RectF mRect;

	private Paint mBorderPaint;
	private OnDrawableChangedCallback mOnDrawableChangedCallback;

	public MQImageView(Context context) {
		this(context, null);
	}

	public MQImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MQImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initCustomAttrs(context, attrs);

		initBorderPaint();

		setDefaultImage();

		mRect = new RectF();
	}

	private void initBorderPaint() {
		mBorderPaint = new Paint();
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);
	}

	private void initCustomAttrs(Context context, AttributeSet attrs) {
	}

	public void setCircle() {
		mIsCircle = true;
		postInvalidate();
	}

	public void setSquare() {
		mIsSquare = true;
		postInvalidate();
	}

	public void setCornerRadus(int value) {
		mCornerRadius = MQUtils.dip2px(getContext(), value);
		postInvalidate();
	}

	private void setDefaultImage() {
		if (mDefaultImageId != 0) {
			setImageResource(mDefaultImageId);
		}
	}

	@Override
	public void setImageResource(@DrawableRes int resId) {
		setImageDrawable(getResources().getDrawable(resId));
	}

	@Override
	public void setImageDrawable(@Nullable Drawable drawable) {
		if (drawable instanceof BitmapDrawable && mCornerRadius > 0) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			if (bitmap != null) {
				super.setImageDrawable(getRoundedDrawable(getContext(), bitmap, mCornerRadius));
			} else {
				super.setImageDrawable(drawable);
			}
		} else if (drawable instanceof BitmapDrawable && mIsCircle) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			if (bitmap != null) {
				super.setImageDrawable(getCircleDrawable(getContext(), bitmap));
			} else {
				super.setImageDrawable(drawable);
			}
		} else {
			super.setImageDrawable(drawable);
		}

		notifyDrawableChanged(drawable);
	}

	private void notifyDrawableChanged(Drawable drawable) {
        if (mOnDrawableChangedCallback != null) {
            mOnDrawableChangedCallback.onDrawableChanged(drawable);
        }
    }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mIsCircle || mIsSquare) {
			setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
			int childWidthSize = getMeasuredWidth();
			heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mBorderWidth > 0) {
			if (mIsCircle) {
				canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mBorderWidth / 2, mBorderPaint);
			} else {
				mRect.left = 0;
				mRect.top = 0;
				mRect.right = getWidth();
				mRect.bottom = getHeight();
				canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mBorderPaint);
			}
		}
	}

	public void setDrawableChangedCallback(OnDrawableChangedCallback onDrawableChangedCallback) {
		mOnDrawableChangedCallback = onDrawableChangedCallback;
	}

	public interface OnDrawableChangedCallback {
		void onDrawableChanged(Drawable drawable);
	}

	public static RoundedBitmapDrawable getCircleDrawable(Context context, Bitmap bitmap) {
		RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
		circleDrawable.setAntiAlias(true);
		circleDrawable.setCornerRadius(Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
		return circleDrawable;
	}

	public static RoundedBitmapDrawable getCircleDrawable(Context context, @DrawableRes int resId) {
		return getCircleDrawable(context, BitmapFactory.decodeResource(context.getResources(), resId));
	}

	public static RoundedBitmapDrawable getRoundedDrawable(Context context, Bitmap bitmap, float cornerRadius) {
		RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(),
				bitmap);
		roundedBitmapDrawable.setAntiAlias(true);
		roundedBitmapDrawable.setCornerRadius(cornerRadius);
		return roundedBitmapDrawable;
	}

	public static RoundedBitmapDrawable getRoundedDrawable(Context context, @DrawableRes int resId,
			float cornerRadius) {
		return getRoundedDrawable(context, BitmapFactory.decodeResource(context.getResources(), resId), cornerRadius);
	}

	/**
	 * 遍历R类得到styleable数组资源下的子资源，1.先找到R类下的styleable子类，2.遍历styleable类获得字段值
	 *
	 * @param context
	 * @param styleableName
	 * @param styleableFieldName
	 * @return
	 */
	public static int getStyleableFieldId(Context context, String styleableName, String styleableFieldName) {
		String className = context.getPackageName() + ".R";
		String type = "styleable";
		String name = styleableName + "_" + styleableFieldName;
		try {
			Class<?> cla = Class.forName(className);
			for (Class<?> childClass : cla.getClasses()) {
				String simpleName = childClass.getSimpleName();
				if (simpleName.equals(type)) {
					for (Field field : childClass.getFields()) {
						String fieldName = field.getName();
						if (fieldName.equals(name)) {
							return (int) field.get(null);
						}
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return 0;
	}
}

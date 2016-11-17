package com.hcz.core.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.hcz.R;


public class DashedView extends View{

	private int dashGap;
	private int dashWidth;
	private int dashColor;
	
	public DashedView(Context context) {
		super(context);
	}
	public DashedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttr(context, attrs);
	}
	public DashedView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttr(context, attrs);
	}
	@SuppressLint("NewApi")
	public DashedView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initAttr(context, attrs);
	}
	private void initAttr(Context context, AttributeSet attrs){
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DashedView);
		dashGap = array.getDimensionPixelSize(R.styleable.DashedView_dashGap, 1);
		dashWidth = array.getDimensionPixelSize(R.styleable.DashedView_dashWidth, 1);
		dashColor = array.getColor(R.styleable.DashedView_dashColor, Color.BLACK);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(dashColor);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(getHeight());                          //设置画笔大小为组件的高度
		PathEffect pe = new DashPathEffect(new float[]{dashWidth, dashGap}, 1);
		p.setPathEffect(pe);
		//canvas.drawLine(0, 0, getWidth(), getHeight(), p);     //如果直接画虚线，还是会显示实线，因为硬件加速的问题
		canvas.drawRect(0, 0, getWidth(), getHeight() * 3, p);   //这里画一个与组件同宽，是组件高度3倍的虚线框。这样在组件中就只能显示框的上边，就形成了一条虚线。
	}
}

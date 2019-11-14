package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by MXie2 on 2019/8/28.
 */

public class BatteryHorizontalView extends View {
    private Resources mResources;
    private Paint mBitPaint;
    private Bitmap fillerBitmap;
    private int mBitWidth;
    private int mBitHeight;
    private int percent = 0;

    public BatteryHorizontalView(Context context) {
        this(context, null);
    }

    public BatteryHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mResources = getResources();
        initPaint();
        initBitmap();

    }

    public BatteryHorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void initPaint() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        mBitPaint.setAntiAlias(true);
        mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
        this.invalidate();
    }

    private void initBitmap() {
        int resourcesId = 0;
        if (percent >= 51) {
            resourcesId = R.mipmap.battery_horizontal_green;
        } else if (percent >= 31) {
            resourcesId = R.mipmap.battery_horizontal_yellow;
        } else {
            resourcesId = R.mipmap.battery_horizontal_red;
        }
        fillerBitmap = ((BitmapDrawable) mResources.getDrawable(resourcesId)).getBitmap();
        mBitWidth = fillerBitmap.getWidth();
        mBitHeight = fillerBitmap.getHeight();


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initBitmap();
        double width = mBitWidth / 100.00;
        Path path = new Path();
        Path path2 = new Path();
        Paint linePaint = new Paint();
        float baseX1 = (float) (24 - 0.48 * percent);
        float baseX2 = (float) (percent * width);
        float addCoefficient=(float) (2-(percent*0.04));
        linePaint.setColor(Color.parseColor("#FFFFFF"));
        linePaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 3));
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setStyle(Paint.Style.FILL);
        if (percent>0&&percent<100) {
            canvas.drawLine((float) (percent * width) + baseX1 , (float) 3.2, baseX2 + addCoefficient, (float) (mBitHeight - 8.5), linePaint);
        }
        path.moveTo(-4, mBitHeight);
        path.lineTo(baseX1, 0);
        path.lineTo(baseX2, mBitHeight);
        path.close();
        path2.moveTo((float) (percent * width) + baseX1, 0);
        path2.lineTo(baseX1, 0);
        path2.lineTo(baseX2, mBitHeight);
        path2.close();
        path.addPath(path2);
//        canvas.drawPath(path,linePaint);
        if (percent < 100) {
            canvas.clipPath(path);
        }
        if (percent > 0) {
            canvas.drawBitmap(fillerBitmap, 0, 0, mBitPaint);
            Rect src = new Rect(0, 0, mBitWidth, mBitHeight);
            RectF dsc = new RectF(0, 0, mBitWidth, mBitHeight);
            canvas.drawBitmap(fillerBitmap, src, dsc, mBitPaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mBitWidth, mBitHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

}

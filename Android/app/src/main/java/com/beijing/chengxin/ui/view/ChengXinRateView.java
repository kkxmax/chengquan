package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.beijing.chengxin.R;

public class ChengXinRateView extends View {
    private float startAngle = 135.0f;
    private float endAngle = 315.0f;
    private float strokeWidth = 8;
    private int radius1 = 10;
    private int radius2 = 14;
    private int radius3 = 20;
    private long duration = 1000;

    private long current;
    private float radius;
    private boolean running;
    private int rateValue; // 0-100%

    private Path circle = new Path();
    private RectF oval = new RectF();
    private Paint paint = new Paint();

    public ChengXinRateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChengXinRateView(Context context) {
        super(context);
        init(context);
    }

    public void setRateValue(int rateValue) {
        this.rateValue = rateValue;
        duration = duration * rateValue / 100;
        current = duration;
        invalidate();
    }

    private void init(Context context) {
        rateValue = 0;
        paint.setTextSize(12);
        paint.setColor(getResources().getColor(R.color.color_white));
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public void start() {
        startWithTime(0);
    }

    public void startWithTime(long time) {
        if (duration > 0) {
            running = false;
            current = duration - time;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (updateCircle()) {
                        postDelayed(this, 10);
                    }
                }
            };
            this.post(runnable);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();
        int paddingLeft = (width > height) ? (width - height) / 2 : 0;
        int paddingTop = (width < height) ? (height - width) / 2 : 0;
        radius = (width > height) ? height / 2 : width / 2;
        radius = radius - radius3;

        Point center = new Point(width/2, height/2);

        paint.setColor(getResources().getColor(R.color.color_white_trans_3));
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        //General circle
        oval.set(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
        circle.addArc(oval, startAngle, 270);
        canvas.drawPath(circle, paint);

        // over circle
        circle.reset();
        float percent = ((float)rateValue / 100.0f) * ((float)(duration - current) / (float)duration);
        float degrees = 270.0f * percent;
        paint.setColor(Color.WHITE);
        circle.addArc(oval, startAngle, degrees);
        canvas.drawPath(circle, paint);

        // node circle
        paint.setStyle(Paint.Style.FILL);
        float cx = center.x + radius * (float) Math.sin((degrees - startAngle) * Math.PI / 180.0f);
        float cy = center.y - radius * (float) Math.cos((degrees - startAngle) * Math.PI / 180.0f);
        canvas.drawCircle(cx, cy, radius1, paint);
        paint.setColor(getResources().getColor(R.color.color_white_trans_5));
        canvas.drawCircle(cx, cy, radius2, paint);
        paint.setColor(getResources().getColor(R.color.color_white_trans_1));
        canvas.drawCircle(cx, cy, radius3, paint);

		/* Draw remain time */
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        int intPercent = (int) (percent  * 100.0f);
        String number = String.format("%02d", intPercent);
        canvas.drawText(number, center.x, center.y + 20, paint);

        paint.setTextSize(28);
        canvas.drawText("%", center.x + 54, center.y - 5, paint);
        canvas.drawText("诚信度", center.x, center.y + radius, paint);
    }

    private boolean updateCircle() {
        if (current <= 0) {
            running = false;
        } else {
            current -= 10;
            running = true;
        }

        invalidate();
        return running;
    }
}

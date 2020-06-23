package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

/**
 * Created by zlc
 * on 2020/5/28
 * Describe：
 */
public class FreeMergerMatrixView extends FrameLayout {

    protected static final int TOP_BOTTOM = 0x15;
    protected static final int LEFT_RIGHT = 0x16;
    protected static final int ROTATION = 0x17;
    protected static final int CENTER = 0x18;
    protected static final int ZOOM = 0x19;
    protected static final int DELETE = 0X20;

    protected static int ACTION_TYPE;

    private int autoMM = 16;
    protected int iconSize;

    private int minWidth;
    /**
     * 最小高度
     */
    private int minHeight;

    protected int padding;
    private Bitmap spinBitmap, deleteBitmap, zoomBitmap;
    private Context context;
    protected int adaptationHeight;
    protected Paint dashedPaint = AppUtils.getOrangeEffectBorderPaint();
    protected Paint drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected float downX = 0, downY = 0,downRawX = 0, downRawY = 0;
    private float tempDistance = 0;
    private View childView;
    private float childScale=1f;
    private float downSelfX, downSelfY;
    protected Point viewPCenter = new Point();

    public FreeMergerMatrixView(Context context) {
        this(context, null);
    }

    public FreeMergerMatrixView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreeMergerMatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    public FreeMergerMatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData(context);
    }

    protected void initData(Context context) {
        adaptationHeight = autoMM * 8;
        zoomBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_free_zoom);
        spinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_free_spin);
        deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_free_delete);

        this.context = context;
        this.iconSize = AppUtils.dip2px(context, 15);
        this.minWidth = this.minHeight = adaptationHeight + iconSize;
        this.padding = this.iconSize / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (childView==null){
            childView = getChildAt(0);
        }
        if (childView!=null){
            childView.setPivotX(0);
            childView.setPivotY(0);
            widthMeasureSpec= (int) (childView.getMeasuredWidth()*childView.getScaleX());
            heightMeasureSpec= (int) (childView.getMeasuredHeight()*childView.getScaleY());
        }
        if (widthMeasureSpec == 0 || heightMeasureSpec == 0) {
            widthMeasureSpec = getMinWidth();
            heightMeasureSpec = getMinHeight();
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("----","容器触摸");
        super.onTouchEvent(event);
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ACTION_TYPE = getDirection(event,(int) event.getX(), (int) event.getY());
                downX = event.getX();
                downY = event.getY();
                downSelfX = getX();
                downSelfY = getY();
                downRawX = event.getRawX();
                downRawY = event.getRawY();
                viewPCenter.y = (int) (getY() + getHeight() / 2);
                viewPCenter.x = (int) (getX() + getWidth() / 2);
                tempDistance = (float) Math.sqrt((Math.pow(downX, 2) + Math.pow(downY, 2)));
                if (childView!=null){
                    childScale=childView.getScaleX();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                actionFromTouchEvent(event);
                break;
        }

        return true;
    }

    private int getDirection(MotionEvent event,int x, int y) {

        if (x >= (getMeasuredWidth() - iconSize) && x <= getMeasuredWidth() && y >= (getMeasuredHeight() / 2 - iconSize / 2) && y <= (getMeasuredHeight() / 2 + iconSize / 2)) {
            Log.e("---", "左右宽度改变");
//            return LEFT_RIGHT;
        }
        if (x >= (getMeasuredWidth() / 2 - iconSize / 2) && x <= (getMeasuredWidth() / 2 + iconSize / 2) && y >= (getMeasuredHeight() - iconSize) && y <= getMeasuredHeight()) {
            Log.e("---", "上下高度改变");
//            return TOP_BOTTOM;
        }
        if (x >= (getMeasuredWidth() - iconSize) && x <= getMeasuredWidth() && y >= (getMeasuredHeight() - iconSize) && y <= getMeasuredHeight()) {
            Log.e("---", "宽高等比");
            return ZOOM;
        }
        if (x >= (getMeasuredWidth() - iconSize) && x <= getMeasuredWidth() && y >= 0 && y <= iconSize) {
            return ROTATION;
        }
        return CENTER;
    }

    private void actionFromTouchEvent(MotionEvent event) {
        final float xDistance = event.getRawX() - downRawX;
        final float yDistance = event.getRawY() - downRawY;
        if (Math.abs(xDistance) < 10 && Math.abs(yDistance) < 10) {
//            return;
        }
        switch (ACTION_TYPE) {
            case CENTER:
                float moveY = downSelfY + yDistance ;
                float moveX = downSelfX + xDistance ;

                setY(moveY);
                setX(moveX);
                break;
            case LEFT_RIGHT:
                break;
            case TOP_BOTTOM:
                break;
            case ROTATION:
//                if (rotationCenter==null||firstPoint==null){
//                    return;
//                }
//                if (secandPoint==null){
//                    secandPoint=new PointF();
//                }
//                secandPoint.x=event.getRawX();
//                secandPoint.y=event.getRawY();
//                rotationAngle = (oriRotation + angle(rotationCenter, firstPoint, secandPoint));
//                if (actionForChild) {
//                    dragRotationByChild(event, rotationAngle);
//                    invalidate();
//                    return;
//                }
//                setRotation(rotationAngle);
                break;
            case ZOOM:
                zoomViewCalculation(event, xDistance, yDistance);
                break;
            case DELETE://放到UP事件里面去处理

                break;
        }

    }

    private void zoomViewCalculation(MotionEvent event, float disX, float disY) {
        float moveX = event.getX(), moveY = event.getY();

        float dynamicVector = (float) (Math.pow(moveX, 2) + Math.pow(moveY, 2));
        float ableDis = (float) Math.sqrt(dynamicVector);

        float rate = ableDis / tempDistance;

        if (childView!=null){
            childView.setScaleX(childScale*rate);
            childView.setScaleY(childScale*rate);
            int width= (int) (childView.getWidth()*childView.getScaleY());
            int height = (int) (childView.getHeight()*childView.getScaleY());
            int x = viewPCenter.x - width / 2;
            int y = viewPCenter.y - height / 2;
            setX(x);
            setY(y);
            requestLayout();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawRect(padding, padding, getMeasuredWidth() - padding, getMeasuredHeight() - padding, dashedPaint);
        int right = getMeasuredWidth() - this.iconSize;
        int bottom = getMeasuredHeight() - this.iconSize;
        Rect local = new Rect(right, 0, getMeasuredWidth(), this.iconSize);
        Rect src = new Rect(0, 0, spinBitmap.getWidth(), spinBitmap.getHeight());
        canvas.drawBitmap(spinBitmap, src, local, drawPaint);

        Rect leftLocal = new Rect(0, 0, this.iconSize, this.iconSize);
        Rect leftSrc = new Rect(0, 0, deleteBitmap.getWidth(), deleteBitmap.getHeight());
        canvas.drawBitmap(deleteBitmap, leftSrc, leftLocal, drawPaint);


        Rect zoomLocal = new Rect(right, bottom, getMeasuredWidth(), getMeasuredHeight());
        Rect zoomSrc = new Rect(0, 0, zoomBitmap.getWidth(), zoomBitmap.getHeight());
        canvas.drawBitmap(zoomBitmap, zoomSrc, zoomLocal, drawPaint);
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }

        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("FreeMergerMatrixView can host only one direct child");
        }

        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("FreeMergerMatrixView can host only one direct child");
        }

        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("FreeMergerMatrixView can host only one direct child");
        }

        super.addView(child, index, params);
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }
    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}

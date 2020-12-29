package com.lt.zxmap.view.dragView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * create by 96212 on 2020/6/15.
 * Email 962123525@qq.com
 * desc 触摸绘制矩形 圆
 */
public class DragDrawView extends View {
    //声明paint对象
    private Paint mPaint = null;
    private int strokeWidth = 5;
    private int mRadius = 0;
    private Rect mRect;
    private Path mPath;
    private int currentX, currentY, memoryX, memoryY;
    private int mWidth, mHeight; //矩形宽 高
    private int startX, startY, endX, endY;
    private DrawType type;
    private Boolean isLine;
    private DragDrawResultListener dragDrawResultListener;
    private ArrayList<PointF> handPoints;

    public enum DrawType {
        RECTANGLE,
        CIRCLE,
        HANDPOINT
    }

    public DragDrawView(Context context) {
        this(context, null);
    }

    public DragDrawView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        //设置无锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAlpha(100);
        mPaint.setColor(Color.RED);
        mRect = new Rect(0, 0, 0, 0);
        mPath = new Path();
        handPoints = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clear();
                memoryX = (int) event.getX();
                memoryY = (int) event.getY();
                if (type == DrawType.RECTANGLE) {
                    mRect.left = memoryX;
                    mRect.top = memoryY;
                    mRect.right = mRect.left;
                    mRect.bottom = mRect.top;
                } else if (type == DrawType.HANDPOINT) {
                    mPath.reset();
                    handPoints.clear();
                    mPath.moveTo(memoryX, memoryY);
                    handPoints.add(new PointF(memoryX, memoryY));
                }
            case MotionEvent.ACTION_MOVE:
                currentX = (int) event.getX();
                currentY = (int) event.getY();
                if (type == DrawType.RECTANGLE) {
                    Rect old = new Rect(mRect.left, mRect.top, mRect.right + strokeWidth, mRect.bottom + strokeWidth);
                    mRect.right = currentX;
                    mRect.bottom = currentY;
                    old.union(currentX, currentY);
                } else if (type == DrawType.CIRCLE) {
                    int i = ((currentX - memoryX) * (currentX - memoryX) + (currentY - memoryY) * (currentY - memoryY));
                    mRadius = (int) Math.sqrt(i);
                } else if (type == DrawType.HANDPOINT) {
                    mPath.lineTo(currentX, currentY);
                    handPoints.add(new PointF(currentX, currentY));
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (type == DrawType.RECTANGLE) {
                    refreshLocation(mRect);
                    if (dragDrawResultListener != null) {
                        dragDrawResultListener.drawRectangleResult(mRect);
                    }
                } else if (type == DrawType.CIRCLE) {
                    if (dragDrawResultListener != null) {
                        dragDrawResultListener.drawCircleResult(mRadius, new PointF(memoryX, memoryY));
                    }
                } else if (type == DrawType.HANDPOINT) {
                    //手绘
                    if (dragDrawResultListener != null) {
                        if (!isLine) {
                            mPath.close();
                        }
                        invalidate();
                        dragDrawResultListener.drawHandPointResult(handPoints);
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (type == DrawType.RECTANGLE) {
            //画矩形
            canvas.drawRect(mRect, mPaint);
        } else if (type == DrawType.CIRCLE) {
            //画圆
            canvas.drawCircle(memoryX, memoryY, mRadius, mPaint);
        } else if (type == DrawType.HANDPOINT) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * 外部调用接口
     */
    public DragDrawView setRect(Rect rect) {
        mRect = rect;
        invalidate();
        return this;
    }

    /**
     * 设置绘制类型
     *
     * @param drawType
     * @return
     */
    public DragDrawView setType(DrawType drawType) {
        this.type = drawType;
        return this;
    }

    /**
     * 设置是否封闭
     *
     * @param isLine
     * @return
     */
    public DragDrawView setIsLine(boolean isLine) {
        this.isLine = isLine;
        return this;
    }

    /**
     * 绘制回调监听
     *
     * @return
     */
    public DragDrawView setDrawResultListener(DragDrawResultListener dragDrawResultListener) {
        this.dragDrawResultListener = dragDrawResultListener;
        return this;
    }

    /**
     * 清理
     */
    public void clear() {
        mPath.reset();
        handPoints.clear();
        mRadius = 0;
        mRect = new Rect();
        invalidate();
    }

    /**
     * 结束绘制
     */
    public void exit() {
        clear();
        if (this.getParent() != null) {
            ((ViewGroup) this.getParent()).removeView(this);
        }
    }


    /**
     * 更新矩形的坐标
     */
    private void refreshLocation(Rect rect) {
        this.startX = rect.left;
        this.startY = rect.top;
        this.endX = rect.right;
        this.endY = rect.bottom;
        mWidth = endX - startX;
        mHeight = endY - startY;
    }

}

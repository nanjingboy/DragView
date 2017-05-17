package me.tom.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class DragView extends View {

    protected int mXDelta;
    protected int mYDelta;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewGroup parent = (ViewGroup) getParent();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mXDelta = (int) event.getRawX() - layoutParams.leftMargin;
                mYDelta = (int) event.getRawY() - layoutParams.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                int[] margins = parseMargin(event, parent);
                layoutParams.leftMargin = margins[0];
                layoutParams.topMargin = margins[1];
                setLayoutParams(layoutParams);
                break;
        }
        bringToFront();
        parent.invalidate();
        return true;
    }

    protected int[] parseMargin(MotionEvent event, ViewGroup parent) {
        int leftMargin = (int) event.getRawX() - mXDelta;
        int maxHorizontalMargin = parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight() - getWidth();
        if (leftMargin <= 0) {
            leftMargin = 0;
        } else if (leftMargin >= maxHorizontalMargin) {
            leftMargin = maxHorizontalMargin;
        }

        int topMargin = (int) event.getRawY() - mYDelta;
        int maxVerticalMargin = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom() - getHeight();
        if (topMargin <= 0) {
            topMargin = 0;
        } else if (topMargin >= maxVerticalMargin) {
            topMargin = maxVerticalMargin;
        }

        return new int[] { leftMargin, topMargin };
    }
}

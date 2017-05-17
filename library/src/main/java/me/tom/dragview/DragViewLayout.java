package me.tom.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class DragViewLayout extends FrameLayout {

    public DragViewLayout(Context context) {
        this(context, null);
    }

    public DragViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}

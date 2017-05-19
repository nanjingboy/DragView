package me.tom.dragview.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.tom.dragview.TagDragView;

public class TagView extends TagDragView {

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int[] parseMargin(MotionEvent event, ViewGroup parent) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        int[] locations = new int[2];
        ImageView imageView = (ImageView) parent.getChildAt(0);
        imageView.getLocationOnScreen(locations);
        if (x >= locations[0] && x<= locations[0] + imageView.getWidth() && y >= locations[1] && y <= locations[1] + imageView.getHeight()) {
            return super.parseMargin(event, parent);
        }
        mArrowOrientation = ARROW_ORIENTATION_LEFT;
        return super.parseMargin(event, parent);
    }
}

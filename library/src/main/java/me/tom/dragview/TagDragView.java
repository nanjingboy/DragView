package me.tom.dragview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

public class TagDragView extends DragView {

    protected String mText;
    protected int mTextSize;
    protected int mTextColor;
    protected int mTextPaddingLeft;
    protected int mTextPaddingRight;

    protected int mCircleBackgroundColor;
    protected int mContentBackgroundColor;

    protected int mInnerCircleRadius;
    protected int mCircleRadius;
    protected int mTriangleWidth;

    protected Paint mCirclePaint;
    protected Paint mContentPaint;
    protected TextPaint mTextPaint;

    protected Path mPath;
    protected Rect mTextRect;


    public TagDragView(Context context) {
        this(context, null);
    }

    public TagDragView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagDragView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(0);

        mContentPaint = new Paint();
        mContentPaint.setAntiAlias(true);
        mContentPaint.setStyle(Paint.Style.FILL);
        mContentPaint.setStrokeWidth(0);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mPath = new Path();
        mTextRect = new Rect();

        Resources resources = getResources();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagDragView);
        mText = typedArray.getString(R.styleable.TagDragView_tagDragViewText);
        mTextSize = typedArray.getDimensionPixelSize(
                R.styleable.TagDragView_tagDragViewTextSize,
                resources.getDimensionPixelSize(R.dimen.tagDragViewTextSize)
        );
        mTextColor = typedArray.getColor(
                R.styleable.TagDragView_tagDragViewTextColor,
                ContextCompat.getColor(context, R.color.colorTagDragViewText)
        );
        mTextPaddingLeft = typedArray.getDimensionPixelSize(
                R.styleable.TagDragView_tagDragViewTextPaddingLeft,
                resources.getDimensionPixelSize(R.dimen.tagDragViewTextPaddingLeft)
        );
        mTextPaddingRight = typedArray.getDimensionPixelSize(
                R.styleable.TagDragView_tagDragViewTextPaddingRight,
                resources.getDimensionPixelSize(R.dimen.tagDragViewTextPaddingRight)
        );
        mCircleBackgroundColor = typedArray.getColor(
                R.styleable.TagDragView_circleBackground,
                ContextCompat.getColor(context, R.color.colorTagDragViewCircleBackground)
        );
        mContentBackgroundColor = typedArray.getColor(
                R.styleable.TagDragView_contentBackground,
                ContextCompat.getColor(context, R.color.colorTagDragViewContentBackgroundColor)
        );
        mCircleRadius = typedArray.getDimensionPixelSize(
                R.styleable.TagDragView_circleRadius,
                resources.getDimensionPixelSize(R.dimen.tagDragViewCircleRadius)
        );
        mInnerCircleRadius = typedArray.getDimensionPixelSize(
                R.styleable.TagDragView_innerCircleRadius,
                resources.getDimensionPixelSize(R.dimen.tagDragViewInnerCircleRadius)
        );
        mTriangleWidth = typedArray.getDimensionPixelSize(
                R.styleable.TagDragView_triangleWidth,
                resources.getDimensionPixelSize(R.dimen.tagDragViewTriangleWidth)
        );
        typedArray.recycle();
    }

    public void setCircleBackgroundColor(int color) {
        mCircleBackgroundColor = color;
        invalidate();
    }

    public void setContentBackgroundColor(int color) {
        mContentBackgroundColor = color;
        invalidate();
    }

    public void setInnerCircleRadius(int radius) {
        mInnerCircleRadius = radius;
        invalidate();
    }

    public void setCircleRadius(int radius) {
        mCircleRadius = radius;
        invalidate();
    }

    public void setTriangleWidth(int width) {
        mTriangleWidth = width;
        invalidate();
    }

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    public void setTextSize(int size) {
        mTextSize = size;
        invalidate();
    }

    public void setTextColor(int color) {
        mTextColor = color;
        invalidate();
    }

    public void setTextPaddingLeft(int paddingLeft) {
        mTextPaddingLeft = paddingLeft;
        invalidate();
    }

    public void setTextPaddingRight(int paddingRight) {
        mTextPaddingRight = paddingRight;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int halfHeight = height / 2;

        mCirclePaint.setColor(mCircleBackgroundColor);
        canvas.drawCircle(mCircleRadius, halfHeight, mCircleRadius, mCirclePaint);
        mContentPaint.setColor(mContentBackgroundColor);
        canvas.drawCircle(mCircleRadius, halfHeight, mInnerCircleRadius, mContentPaint);

        mPath.moveTo(mCircleRadius, halfHeight);
        mPath.lineTo(halfHeight + mTriangleWidth, 0);
        mPath.lineTo(halfHeight + mTriangleWidth, height);
        mPath.close();
        canvas.drawPath(mPath, mContentPaint);
        canvas.drawRect(halfHeight + mTriangleWidth, 0, width, height, mContentPaint);
        if (mText != null && mText.length() > 0) {
            mTextPaint.setColor(mTextColor);
            mTextPaint.setTextSize(mTextSize);
            int availableTextWidth = width - halfHeight - mTriangleWidth - mTextPaddingLeft - mTextPaddingRight;
            String text = TextUtils.ellipsize(mText, mTextPaint, availableTextWidth, TextUtils.TruncateAt.END).toString();
            mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);
            canvas.drawText(text, halfHeight + mTriangleWidth, (int) (mTextRect.height() * 1.5), mTextPaint);
        }
    }
}

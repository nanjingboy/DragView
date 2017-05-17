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

    public static final int ARROW_ORIENTATION_LEFT = 0;
    public static final int ARROW_ORIENTATION_RIGHT = 1;

    protected int mArrowOrientation;

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
        mArrowOrientation = typedArray.getInt(R.styleable.TagDragView_arrowOrientation, ARROW_ORIENTATION_LEFT);
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

    public void setArrowOrientation(int arrowOrientation) {
        mArrowOrientation = arrowOrientation;
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
        mCirclePaint.setColor(mCircleBackgroundColor);
        mContentPaint.setColor(mContentBackgroundColor);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        drawCircle(canvas);
        drawTriangle(canvas);
        drawText(canvas);
    }

    protected void drawCircle(Canvas canvas) {
        int offsetX;
        int offsetY = getHeight() / 2;
        if (mArrowOrientation == ARROW_ORIENTATION_LEFT) {
            offsetX = mCircleRadius;
        } else {
            offsetX = getWidth() - mCircleRadius;
        }
        canvas.drawCircle(offsetX, offsetY, mCircleRadius, mCirclePaint);
        canvas.drawCircle(offsetX, offsetY, mInnerCircleRadius, mContentPaint);
    }

    protected void drawTriangle(Canvas canvas) {
        int height = getHeight();
        int centerY = height / 2;
        mPath.reset();
        if (mArrowOrientation == ARROW_ORIENTATION_LEFT) {
            mPath.moveTo(mCircleRadius, centerY);
            mPath.lineTo(mCircleRadius + mTriangleWidth, 0);
            mPath.lineTo(mCircleRadius + mTriangleWidth, height);
            mPath.close();
            canvas.drawPath(mPath, mContentPaint);
        } else {
            int width = getWidth();
            mPath.moveTo(width - mCircleRadius, centerY);
            mPath.lineTo(width - mCircleRadius - mTriangleWidth, 0);
            mPath.lineTo(width - mCircleRadius - mTriangleWidth, height);
            mPath.close();
            canvas.drawPath(mPath, mContentPaint);
        }
    }

    protected void drawText(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        if (mArrowOrientation == ARROW_ORIENTATION_LEFT) {
            canvas.drawRect(mCircleRadius + mTriangleWidth, 0, width, height, mContentPaint);
        } else {
            canvas.drawRect(0, 0, width - mCircleRadius - mTriangleWidth, height, mContentPaint);
        }
        if (mText == null || mText.length() == 0) {
            return;
        }
        int availableTextWidth = width -  mCircleRadius - mTriangleWidth - mTextPaddingLeft - mTextPaddingRight;
        String text = TextUtils.ellipsize(mText, mTextPaint, availableTextWidth, TextUtils.TruncateAt.END).toString();
        mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);
        if (mArrowOrientation == ARROW_ORIENTATION_LEFT) {
            canvas.drawText(text, mCircleRadius + mTriangleWidth + mTextPaddingLeft, (int) (mTextRect.height() * 1.5), mTextPaint);
        } else {
            canvas.drawText(text, mTextPaddingLeft, (int) (mTextRect.height() * 1.5), mTextPaint);
        }
    }
}

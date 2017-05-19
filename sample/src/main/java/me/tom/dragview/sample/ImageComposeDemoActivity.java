package me.tom.dragview.sample;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions.RxPermissions;

import me.tom.dragview.DragViewLayout;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ImageComposeDemoActivity extends AppCompatActivity {

    protected ImageView mResultImageView;
    protected DragViewLayout mDragViewLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_compose_demo);
        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveComposedImageToFile();
            }
        });
        mResultImageView = (ImageView) findViewById(R.id.resultImageView);
        mDragViewLayout = (DragViewLayout) findViewById(R.id.dragViewLayout);
    }

    protected void saveComposedImageToFile() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .map(new Func1<Boolean, String>() {
                    @Override
                    public String  call(Boolean granted) {
                        if (granted) {
                            ImageView imageView = (ImageView) mDragViewLayout.getChildAt(0);
                            Rect imageViewRect = parseDragViewLayoutChildRect(imageView);
                            Bitmap sourceBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                            int sourceWidth = sourceBitmap.getWidth();
                            int sourceHeight = sourceBitmap.getHeight();
                            Matrix matrix = new Matrix();
                            matrix.postScale(
                                    ((float) imageView.getWidth()) / sourceWidth,
                                    ((float) imageView.getHeight()) / sourceHeight
                            );
                            Bitmap bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceWidth, sourceHeight, matrix, true);
                            Canvas canvas = new Canvas(bitmap);
                            int childCount = mDragViewLayout.getChildCount();
                            for (int index = 1; index < childCount; index++) {
                                View child = mDragViewLayout.getChildAt(index);
                                int[] offset = parseTagViewOffset(imageViewRect, child);
                                if (offset == null) {
                                    continue;
                                }
                                Bitmap childBitmap = child.getDrawingCache();
                                canvas.drawBitmap(childBitmap, offset[0], offset[1], null);
                            }
                            canvas.save(Canvas.CLIP_SAVE_FLAG);
                            canvas.restore();
                            matrix.reset();
                            matrix.postScale(
                                    ((float) sourceWidth) / imageView.getWidth(),
                                    ((float) sourceHeight) / imageView.getHeight()
                            );
                            Bitmap destBitmap = Bitmap.createBitmap(
                                    bitmap, 0, 0,
                                    bitmap.getWidth(), bitmap.getHeight(),
                                    matrix, true
                            );
                            String filePath = ImageUtils.saveToFile(ImageComposeDemoActivity.this, destBitmap);
                            bitmap.recycle();
                            destBitmap.recycle();
                            return filePath;
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String filePath) {
                        if (filePath != null) {
                            Glide.with(ImageComposeDemoActivity.this).load(filePath).into(mResultImageView);
                        }
                    }
                });
    }

    protected int[] parseTagViewOffset(Rect imageViewRect, View view) {
        Rect viewRect = parseDragViewLayoutChildRect(view);
        if (viewRect.left >= imageViewRect.left && viewRect.top >= imageViewRect.top &&
                viewRect.right <= imageViewRect.right && viewRect.bottom <= imageViewRect.bottom) {
            return new int[] { viewRect.left - imageViewRect.left, viewRect.top - imageViewRect.top };
        }
        return null;
    }

    protected Rect parseDragViewLayoutChildRect(View view) {
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        return new Rect(locations[0], locations[1], locations[0] + view.getWidth(), locations[1] + view.getHeight());
    }
}

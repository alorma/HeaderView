package com.alorma.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by bernat.borras on 16/12/15.
 */
public class HeaderImageView extends ImageView {

    // The image assigned from the outside
    private Drawable mask;
    private Bitmap output;

    private Rect rectRectangle;
    private Rect rectOval;
    private float arcHeight;
    private RectF rectFOval;
    private Paint defaultPaint;

    public HeaderImageView(Context context) {
        super(context);
        init(null);
    }

    public HeaderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HeaderImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (isInEditMode()) {
        }

        arcHeight = 24f;
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HeaderImageView, 0, 0);
            try {
                arcHeight = ta.getDimension(R.styleable.HeaderImageView_arcHeight, arcHeight);
            } finally {
                ta.recycle();
            }
        }

        rectRectangle = new Rect();
        rectOval = new Rect();

        defaultPaint = new Paint();
        defaultPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        defaultPaint.setStyle(Paint.Style.FILL);

        rectFOval = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(bitmap);
        if (getDrawable() != null) {
            super.onDraw(tempCanvas);
        }

        canvas.getClipBounds(rectOval);
        rectFOval.left = rectOval.left - arcHeight;
        rectFOval.top = rectOval.top;
        rectFOval.right = rectOval.right + arcHeight;
        rectFOval.bottom = rectOval.bottom - getPaddingBottom();

        canvas.getClipBounds(rectRectangle);
        rectRectangle.bottom = (int) (rectFOval.top + (rectFOval.height() / 2));

        defaultPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(bitmap, 0, 0, defaultPaint);

        canvas.drawRect(rectRectangle, defaultPaint);
        RectF clipRect = new RectF(rectFOval);
        clipRect.top = rectRectangle.bottom;
        canvas.clipRect(clipRect);
        canvas.drawOval(rectFOval, defaultPaint);


    }
}

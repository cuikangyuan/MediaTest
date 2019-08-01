package com.tiangou.mediatest.test1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tiangou.mediatest.R;

public class CustomView extends View {


    Paint paint = new Paint();
    Bitmap bitmap;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        bitmap = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.ic_launcher_round);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (bitmap != null) {

            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }
}

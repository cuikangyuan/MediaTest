package com.tiangou.mediatest.test1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tiangou.mediatest.R;

public class Main2Activity extends AppCompatActivity {


    private Button mButton;
    private ImageView mImageView;

    private SurfaceView surfaceView;

    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mButton = findViewById(R.id.button);

        mImageView = findViewById(R.id.image_view);


        surfaceView = findViewById(R.id.surface_view);


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                Log.d(TAG, "surfaceCreated: ");

                if (holder == null) {


                    return;

                }


                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);


                Bitmap bitmap = BitmapFactory.decodeResource(
                        Main2Activity.this.getResources(),
                        R.drawable.ic_launcher_round);


                Canvas canvas = holder.lockCanvas();

                canvas.drawBitmap(bitmap, 0, 0, paint);

                holder.unlockCanvasAndPost(canvas);


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                Log.d(TAG, "surfaceChanged: ");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {


                Log.d(TAG, "surfaceDestroyed: ");

            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d(TAG, "onClick: ");






            }
        });
    }
}

package com.tiangou.mediatest.test1;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.tiangou.mediatest.R;

import java.io.File;
import java.io.IOException;

public class CameraPreviewAct1 extends AppCompatActivity {

    private static final String TAG = "CameraPreviewAct1";
    

    SurfaceView surfaceView;

    SurfaceHolder surfaceHolder;

    Button button;


    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview_act1);


        button = findViewById(R.id.button);

        surfaceView = findViewById(R.id.surface_view);

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                Log.d(TAG, "surfaceCreated: ");


                surfaceHolder = holder;




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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (PermissionUtil.checkCameraPermission(CameraPreviewAct1.this, true)) {


                    try {



                        camera = Camera.open();
                        camera.setDisplayOrientation(90);

                        camera.setPreviewDisplay(surfaceHolder);

                        camera.startPreview();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }




            }
        });


        //PermissionUtil.checkCameraPermission(this, true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PermissionUtil.CHECK_CAMERA_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {


                        camera.setPreviewDisplay(surfaceHolder);

                        camera.startPreview();

                        camera = Camera.open();
                        camera.setDisplayOrientation(90);
                        camera.startPreview();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                }
                break;
        }
    }
}

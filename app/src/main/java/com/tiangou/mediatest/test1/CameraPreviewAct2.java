package com.tiangou.mediatest.test1;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.tiangou.mediatest.R;

import java.io.IOException;

public class CameraPreviewAct2 extends AppCompatActivity {


    private static final String TAG = "CameraPreviewAct2";

    TextureView textureView;


    SurfaceTexture surfaceTexture;

    Button button;


    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview_act2);


        button = findViewById(R.id.button);

        textureView = findViewById(R.id.texture_view);


        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {


                Log.d(TAG, "onSurfaceTextureAvailable: ");

                surfaceTexture = surface;
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {


                Log.d(TAG, "onSurfaceTextureSizeChanged: ");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

                Log.d(TAG, "onSurfaceTextureDestroyed: ");

                if (camera != null) {


                    camera.release();
                }

                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                Log.d(TAG, "onSurfaceTextureUpdated: ");

            }
        });
        

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (PermissionUtil.checkCameraPermission(CameraPreviewAct2.this, true)) {


                    try {



                        camera = Camera.open();
                        camera.setDisplayOrientation(90);

                        camera.setPreviewTexture(surfaceTexture);

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


                        camera = Camera.open();


                        camera.setPreviewTexture(surfaceTexture);



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

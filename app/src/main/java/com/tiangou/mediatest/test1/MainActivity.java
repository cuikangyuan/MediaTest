package com.tiangou.mediatest.test1;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tiangou.mediatest.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    private Button mButton;
    private ImageView mImageView;

    private String path;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button);

        mImageView = findViewById(R.id.image_view);



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean b = PermissionUtil.checkStoragePermission(MainActivity.this, true);


                if (b) {

                    path = Environment.getExternalStorageDirectory().getPath() + File.separator + "11.jpg";

                    Log.d(TAG, "onClick: path >>> " + path);


                    Bitmap bitmap = BitmapFactory.decodeResource(
                            MainActivity.this.getResources(),
                            R.drawable.ic_launcher_round);


                    mImageView.setImageBitmap(bitmap);

                } else {

                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PermissionUtil.CHECK_STORAGE_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    path = Environment.getExternalStorageDirectory().getPath() + File.separator + "11.jpg";

                    Log.d(TAG, "onRequestPermissionsResult: path >>> " + path);

                } else {

                }
                break;
        }
    }

}

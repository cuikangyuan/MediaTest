package com.tiangou.mediatest.test1;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tiangou.mediatest.R;

public class OpenGLTestAct1 extends AppCompatActivity {


    private GLSurfaceView mGLView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new MyGLSurfaceView(this);



        setContentView(mGLView);
    }
}

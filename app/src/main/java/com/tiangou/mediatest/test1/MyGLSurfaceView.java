package com.tiangou.mediatest.test1;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {

    private static final String TAG = "MyGLSurfaceView";


    private final MyGLSurfaceRenderer mRenderer;



    public MyGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        mRenderer = new MyGLSurfaceRenderer();

        setRenderer(mRenderer);
    }
}


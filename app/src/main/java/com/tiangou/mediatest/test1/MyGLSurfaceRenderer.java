package com.tiangou.mediatest.test1;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLSurfaceRenderer implements GLSurfaceView.Renderer {


    private static final String TAG = "MyGLSurfaceRenderer";
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //在View 的 OpenGL 环境被创建的时候调用

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);



    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        //视图的几何形状发生变化


        GLES20.glViewport(0, 0, width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        //每一次 View 的重绘都调用

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);


    }
}

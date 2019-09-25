package com.tiangou.mediatest.test1.opengl_coordinate

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tiangou.mediatest.R

class Main4Activity : AppCompatActivity() {

    
    lateinit var mSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSurfaceView = GLSurfaceView(this)


        mSurfaceView.setRenderer(MyGLRender())
        setContentView(mSurfaceView)

    }

    override fun onResume() {
        super.onResume()
        mSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mSurfaceView.onPause()
    }
}

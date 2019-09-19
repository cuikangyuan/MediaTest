package com.tiangou.mediatest.test1.opengl_draw_triangle

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tiangou.mediatest.R

class OpenGLDrawTriangleAct : AppCompatActivity() {


    lateinit var glSurfaceView: GLSurfaceView
    var isRenderSet = false
    lateinit var triangleShapeRender: TriangleShapeRender


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initContentView()

    }

    override fun onResume() {
        super.onResume()

        if (isRenderSet) {

            glSurfaceView.onResume()

        }
    }


    override fun onPause() {
        super.onPause()

        if (isRenderSet) {

            glSurfaceView.onPause()
        }
    }

    private fun initContentView() {

        //创建一个GLSurfaceView
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView.setEGLContextClientVersion(2)


        triangleShapeRender = TriangleShapeRender(this)

        //设置自己的Render Render内部进行图形的绘制
        glSurfaceView.setRenderer(triangleShapeRender)
        isRenderSet = true

        setContentView(glSurfaceView)

    }


}

package com.tiangou.mediatest.test1.opengl_coordinate

import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.opengl.GLU
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRender: GLSurfaceView.Renderer {


    lateinit var coordinate: Coordinate

    var mAngle = 45f

    init {

        coordinate = Coordinate()
    }

    override fun onDrawFrame(gl: GL10?) {


        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT or GLES31.GL_DEPTH_BITS)

        gl?.glLoadIdentity()
        gl?.glTranslatef(0.0f, 0.3f, -2.0f)
        mAngle += 1

        coordinate.draw(gl)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

        var heightToUse = height

        if (heightToUse == 0) {

            heightToUse = 1
        }

        val aspect = (width / heightToUse)

        gl?.glViewport(0, 0, width, heightToUse)

        gl?.glMatrixMode(GL10.GL_PROJECTION)
        gl?.glLoadIdentity()
        GLU.gluPerspective(gl, 45f, aspect.toFloat(), 0.1f, 100.0f)

        gl?.glMatrixMode(GL10.GL_MODELVIEW)
        gl?.glLoadIdentity()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        gl?.glClearColor(255.0f, 255.0f, 255.0f, 1.0f)
        gl?.glClearDepthf(1.0f)
        gl?.glEnable(GL10.GL_DEPTH_TEST)
        gl?.glDepthFunc(GL10.GL_LEQUAL)
        gl?.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
        gl?.glShadeModel(GL10.GL_SMOOTH)
        gl?.glDisable(GL10.GL_DITHER)

    }
}
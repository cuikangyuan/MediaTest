package com.tiangou.mediatest.test1.opengl_coordinate

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class Coordinate {

    lateinit var vertexsBuffer: FloatBuffer
    lateinit var colorsBuffer: FloatBuffer

    lateinit var indicesBuffer: ByteBuffer

    val vertexs = floatArrayOf(
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f)

    val colors = floatArrayOf(
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
            )

    val indices = byteArrayOf(0, 1, 2)

    init {

        val vbb = ByteBuffer.allocateDirect(vertexs.size * 4)
        vbb.order(ByteOrder.nativeOrder())
        vertexsBuffer = vbb.asFloatBuffer()
        vertexsBuffer.put(vertexs)
        vertexsBuffer.position(0)

        val cbb = ByteBuffer.allocateDirect(colors.size * 4)
        cbb.order(ByteOrder.nativeOrder())
        colorsBuffer = cbb.asFloatBuffer()
        colorsBuffer.put(colors)
        colorsBuffer.position(0)

    }

    fun draw(gl: GL10?) {


        gl?.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl?.glEnableClientState(GL10.GL_COLOR_ARRAY)

        gl?.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexsBuffer)
        gl?.glColorPointer(4, GL10.GL_FLOAT, 0, colorsBuffer)
        gl?.glLineWidth(9f)
        gl?.glDrawArrays(GL10.GL_LINES, 0, vertexs.size / 3)

        gl?.glDisableClientState(GL10.GL_VERTEX_ARRAY)
    }

   }
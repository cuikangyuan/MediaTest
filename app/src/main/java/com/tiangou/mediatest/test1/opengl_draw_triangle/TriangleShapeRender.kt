package com.tiangou.mediatest.test1.opengl_draw_triangle

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleShapeRender(var context: Context):GLSurfaceView.Renderer {


    val VERTEX_SHADER_FILE = "shape/triangle_vertex_shader.glsl"
    val FRAGMENT_SHADER_FILE = "shape/triangle_fragment_shader.glsl"

    //在数组中 一个顶点需要三个来描述其位置 需要三个偏移量
    val COORDS_PER_VERTEX = 3
    val COORDS_PER_COLOR = 0

    //在数组中 描述一个顶点 总共顶点的偏移量
    val TOTAL_COMPONENT_COUNT = COORDS_PER_VERTEX + COORDS_PER_COLOR
    //一个点需要的byte偏移量
    val STRIDE = TOTAL_COMPONENT_COUNT * Constant.BYTES_PER_FLOAT

    val A_POSITION = "aPosition"
    val U_COLOR = "uColor"

    val TRIANGLE_COORDS: FloatArray = floatArrayOf(
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f)   // bottom right)

    val TRIANGLE_COLOR = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

    val VERTEX_COUNT = TRIANGLE_COORDS.size / TOTAL_COMPONENT_COUNT

    lateinit var mVertexFloatBuffer: FloatBuffer

    var programId: Int = 0

    val mProjectionMatrix = FloatArray(16)

    var uMatrix: Int = 0

    companion object {


    }


    init {

        val capacity = TRIANGLE_COORDS.size * Constant.BYTES_PER_FLOAT

        mVertexFloatBuffer = ByteBuffer
                .allocateDirect(capacity)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TRIANGLE_COORDS)

        mVertexFloatBuffer.position(0)

    }

    override fun onDrawFrame(gl: GL10?) {

        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        //使用这个program
        GLES20.glUseProgram(programId)

        //根据我们定义的取出定义的位置
        val vPosition = GLES20.glGetAttribLocation(programId, A_POSITION)
        //开始启用我们的position
        GLES20.glEnableVertexAttribArray(vPosition)
        //将坐标数据放入
        GLES20.glVertexAttribPointer(
                vPosition, //上面得到的id
                COORDS_PER_VERTEX, //用几个偏移量来描述一个顶点
                GLES20.GL_FLOAT,
                false,
                STRIDE, //一个顶点需要几个字节的偏移量
                mVertexFloatBuffer
        )
        //取出颜色
        val uColor = GLES20.glGetUniformLocation(programId, U_COLOR)

        //开始绘制
        //设置绘制的三角形的颜色

        GLES20.glUniform4fv(
                uColor,
                1,
                TRIANGLE_COLOR,
                0
        )

        GLES20.glUniformMatrix4fv(
                uMatrix,
                1,
                false,
                mProjectionMatrix,
                0
        )

        //绘制三角形
        GLES20.glDrawArrays(
                GLES20.GL_TRIANGLES,
                0,
                VERTEX_COUNT
                )
        GLES20.glDisableVertexAttribArray(vPosition)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //在窗口改变的时候调用
        //GLES20.glViewport(0, 0, width, height)

        var aspectRatio: Float = 0f

        if (width > height) {

            aspectRatio = (width / height).toFloat()
            //横屏 需要设置的就是左右
            Matrix.orthoM(
                    mProjectionMatrix,
                    0,
                    -aspectRatio,
                    aspectRatio,
                    -1f,
                    1f,
                    -1f,
                    1f
                    )

        } else {

            //竖屏 需要设置的就是上下
            aspectRatio = (height / width).toFloat()

            Matrix.orthoM(
                    mProjectionMatrix,
                    0,
                    -1f,
                    1f,
                    -aspectRatio,
                    aspectRatio,
                    -1f,
                    1f

            )
        }

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        //简单的给窗口填充一种颜色
        //GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)


        //创建program 将得到的id绑定上 并链接program

        //先从Asset中得到着色器的代码
        val vertexShaderCode = GLESUtils.readAssetShaderCode(context, VERTEX_SHADER_FILE)
        val fragmentShaderCode = GLESUtils.readAssetShaderCode(context, FRAGMENT_SHADER_FILE)

        //得到之后进行编译 得到Id
        val vertexShaderObjectId = GLESUtils.compileShaderCode(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShaderObjectId = GLESUtils.compileShaderCode(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        //取得program
        programId = GLES20.glCreateProgram()

        //将shaderId 绑定到program中
        GLES20.glAttachShader(programId, vertexShaderObjectId)
        GLES20.glAttachShader(programId, fragmentShaderObjectId)

        uMatrix = GLES20.glGetUniformLocation(programId, "u_Matrix")


        //最后启动 Link Program
        GLES20.glLinkProgram(programId)
    }


}
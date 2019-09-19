package com.tiangou.mediatest.test1.opengl_draw_triangle

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLES20
import android.os.Build
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

object GLESUtils {

    //TODO
    private const val GLES_VERSION_2 = 0x20000

    fun isSupportEs2(context: Context): Boolean {

        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        if(activityManager != null) {

            val deviceConfigurationInfo = activityManager.deviceConfigurationInfo

            val reqGlEsVersion = deviceConfigurationInfo.reqGlEsVersion

            return reqGlEsVersion >= GLES_VERSION_2 || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                    && (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK build for x86")));


        } else {

            return false
        }

    }


    fun readAssetShaderCode(context: Context, shaderCodeName: String): String {

        val stringBuilder = StringBuilder()
        var open: InputStream? = null

        open = context.assets.open(shaderCodeName)

        var bufferReader = BufferedReader(InputStreamReader(open))

        var line: String? = null

        line = bufferReader.readLine()

        while (line != null) {

            stringBuilder.append(line)
            stringBuilder.append("\n")

            line = bufferReader.readLine()

        }

        return stringBuilder.toString()

    }


    //编译着色器代码 得到代表着色的Id 类似指针的感觉
    fun compileShaderCode(type: Int, shaderCode: String): Int {

        val shaderObjectId = GLES20.glCreateShader(type)
        //如果着色器id不为0 则表示可以用
        if(shaderObjectId != 0) {

            //上传代码
            GLES20.glShaderSource(shaderObjectId, shaderCode)
            //编译代码
            GLES20.glCompileShader(shaderObjectId)
            //查询编译状态
            val status = IntArray(1)
            GLES20.glGetShaderiv(
                    shaderObjectId,
                    GLES20.GL_COMPILE_STATUS,
                    status,
                    0)

            if (status[0] == 0) {
                //失败 需要释放资源 就是删除这个引用
                GLES20.glDeleteShader(shaderObjectId)
                return 0
            }

        }


        return shaderObjectId


    }
}
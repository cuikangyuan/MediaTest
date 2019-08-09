package com.tiangou.mediatest.test1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square {

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;



    static final int COORDS_PER_VERTEX = 3;

    static final float squareCoords[] = {

            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f    // top right
    };


    private short drawOrder[] = {0, 1, 2, 0, 2, 3};



    public Square() {


        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(
                squareCoords.length * 4
        );

        byteBuffer1.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer1.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);


        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(

                drawOrder.length * 2
        );

        byteBuffer2.order(ByteOrder.nativeOrder());
        drawListBuffer = byteBuffer2.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


    }


}

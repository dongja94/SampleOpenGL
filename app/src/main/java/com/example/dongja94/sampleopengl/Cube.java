package com.example.dongja94.sampleopengl;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by dongja94 on 2015-11-20.
 */
public class Cube {

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer drawListBuffer;

    float[] vertecis = {
            -0.5f, -0.5f, -0.5f,  // position
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f
    };

    float[] colors = {
            0.5f, 0.0f, 0.0f,  // color
            0.5f, 0.5f, 0.0f,// color
            0.5f, 0.0f, 0.5f,// color
            0.0f, 0.5f, 0.5f,// color
            0.0f, 0.5f, 0.0f,// color
            0.0f, 0.0f, 0.5f,// color
            0.0f, 0.0f, 0.0f,// color
            0.5f, 0.5f, 0.5f// color
    };

    short[] drawOrder = {0, 1, 3, 2, 7 , 1, 6, 0, 5, 3, 4, 7, 5, 6};

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec4 aColor;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  vColor = aColor;" +
                    "  gl_Position = uMVPMatrix * aPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final int mProgram;

    public Cube() {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertecis.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertecis);
        vertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        GLES20.glLinkProgram(mProgram);

    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);

        int indexPosition = GLES20.glGetAttribLocation(mProgram, "aPosition");
        GLES20.glEnableVertexAttribArray(indexPosition);
        GLES20.glVertexAttribPointer(indexPosition, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);

        int indexColor = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(indexColor);
        GLES20.glVertexAttribPointer(indexColor, 3, GLES20.GL_FLOAT, false, 3 * 4, colorBuffer);

        int locMVP = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(locMVP, 1, false, mvpMatrix, 0);

        GLES20.glFrontFace(GLES20.GL_CCW);
        GLES20.glCullFace(GLES20.GL_BACK);

//        drawListBuffer.position(0);
//        GLES20.glLineWidth(5);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, 14, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(indexPosition);
        GLES20.glDisableVertexAttribArray(indexColor);

    }
}

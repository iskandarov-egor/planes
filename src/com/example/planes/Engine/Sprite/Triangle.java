package com.example.planes.Engine.Sprite;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by egor on 09.07.15.
 */
public final class Triangle extends StaticSprite {
    private final float[] disp = {0,0,0,0};
    @Override
    public void draw(float x, float y, float angle, float[] transform) {
        //debug
        if(transform == null) throw new NullPointerException("matrix");
        if(mProgram <= 0) throw new RuntimeException("debug");


        if(angle != this.angle) rotate(angle);
        GLES20.glUseProgram(mProgram);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        int dispHandle = GLES20.glGetUniformLocation(mProgram, "disp");
        disp[0] = x;
        disp[1] = y;

        GLES20.glUniform4fv(dispHandle, 1, disp, 0);

        int matrHandle = GLES20.glGetUniformLocation(mProgram, "trans");
        GLES20.glUniformMatrix4fv(matrHandle, 1, false, transform, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
    private float angle = 0;

    private final float r = 0.1f;
    public void rotate(float angle) {
        this.angle = angle;

        triangleCoords[0] = r*(float)Math.cos(angle);
        triangleCoords[1] = r*(float)Math.sin(angle);
        triangleCoords[2] = r*(float)Math.cos(angle + 5*Math.PI/6);
        triangleCoords[3] = r*(float)Math.sin(angle + 5 * Math.PI / 6);
        triangleCoords[4] = r*(float)Math.cos(angle + 7*Math.PI/6);
        triangleCoords[5] = r*(float)Math.sin(angle + 7*Math.PI / 6);
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
    }

    @Override
    public float getRadius() {
        return r;
    }

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    private static int mProgram;

    public Triangle() {
        Log.d("hey", "Triangle called");
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
        rotate(0);
    }



    public static void onSurfaceCreated(){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader =loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    static final int COORDS_PER_VERTEX = 2;
    float triangleCoords[] = new float[6];

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private FloatBuffer vertexBuffer;

    private static final String vertexShaderCode =
            "uniform vec4 disp;" +
            "uniform mat4 trans;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position =trans*( disp + vPosition);" +
            "}";

    private static final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "int aaa;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +

            "}";

    public static int loadShader(int type, String shaderCode){
        Log.d("hey", "loadShader called ");
        int shader = GLES20.glCreateShader(type);
        if (shader == 0) {
            Log.d("hey", "shader 0");
            throw new RuntimeException("Error creating shader.");
        }
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        if (compileStatus[0] == GLES20.GL_FALSE)
        {
            GLES20.glDeleteShader(shader);
            shader = 0;
            throw new RuntimeException("Error creating shader.");
        }


        return shader;
    }
}

package com.example.planes.Engine.Sprite;

import android.opengl.GLES20;
import android.util.Log;
import com.example.planes.Engine.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by egor on 12.07.15.
 */
abstract public class OpenGLShape extends StaticSprite {
    protected final float[] disp = {0,0,0,0};
    protected float angle = 0;
    protected float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
    protected static int mProgram;
    protected final float r = 0.1f;
    protected static final int COORDS_PER_VERTEX = 2;
    protected final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    protected FloatBuffer vertexBuffer;
    protected final float[] coords;
    @Override
    public void draw(float x, float y, float angle, float[] transform) {
        //debug
        if(transform == null) throw new NullPointerException("matrix");
        if(mProgram <= 0) throw new RuntimeException("debug");
        if(angle != this.angle) rotate(angle);

        GLES20.glUseProgram(mProgram);

        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        int dispHandle = GLES20.glGetUniformLocation(mProgram, "disp");
        disp[0] = x;
        disp[1] = y;

        GLES20.glUniform4fv(dispHandle, 1, disp, 0);

        int matrHandle = GLES20.glGetUniformLocation(mProgram, "trans");
        GLES20.glUniformMatrix4fv(matrHandle, 1, false, transform, 0);

    }

    public void setColor(float r, float g, float b) {
        Utils.assertColor(r, g, b);
        color[0] = r;
        color[1] = g;
        color[2] = b;
    }

    public OpenGLShape(int numberOfVertexes) {
        coords = new float[numberOfVertexes];
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
        rotate(0);
    }
    public abstract void rotate(float angle);
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

    @Override
    public float getRadius() {
        return r;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("error", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

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
}

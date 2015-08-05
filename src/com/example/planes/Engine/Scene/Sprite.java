package com.example.planes.Engine.Scene;


import android.opengl.GLES20;
import android.util.Log;
import com.example.planes.Engine.MyGLRenderer;

/**
 * Created by egor on 10.07.15.
 */
public abstract class Sprite {

    protected int textureSurfaceVersion = -1;
    protected float w;
    protected float h;
    //Картинка кароч
    public Sprite() {

    }

    abstract void draw(float x, float y, float[] transform);
    abstract void onFrame(float graphicsFPS);

    public abstract float getRadius();
    public abstract void rebuild(float dx, float dy, float angle);

    public abstract void load();

    public float getH() {
        return h;
    }

    public float getW() {
        return w;
    }

    public static void onSurfaceCreated() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vs_Image);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fs_Image);

        tProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(tProgram, vertexShader);
        GLES20.glAttachShader(tProgram, fragmentShader);
        GLES20.glLinkProgram(tProgram);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        // Set our shader programm

    }
    protected static int tProgram;

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

    public static final String vs_Image =
            "uniform vec4 disp;" +
                    "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * (vPosition + disp);" +
                    "  v_texCoord = a_texCoord;" +
                    "}";
    public static final String fs_Image =
            "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
                    "}";

    public boolean isLoaded() {
        return textureSurfaceVersion == MyGLRenderer.surfaceVersion;
    }
}

package com.example.planes.Engine.Scene;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import com.example.planes.Engine.GLHelper;
import com.example.planes.Engine.TextureManager;
import com.example.planes.MyApplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by egor on 17.07.15.
 */
public class TextureSprite extends Rect {
    public static FloatBuffer uvBuffer;
    private static int tProgram;
    private int textureName;
    private boolean loaded = false;
    private int fileId;

    public TextureSprite(int fileId, float height) {
        super();
        if(fileId == 0) throw new IllegalArgumentException("0 id");
        this.fileId = fileId;
        h = height;
    }

    public void loadTexture() {
        this.textureName = TextureManager.getTexture(fileId);
        Drawable d = MyApplication.context.getResources().getDrawable(fileId);
        setWH(h * (float)d.getIntrinsicWidth()/d.getIntrinsicHeight(), h);
        loaded = true;
    }

    public static void loadImage() {
        // Create our UV coordinates.
        float[] uvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        // Generate Textures, if more needed, alter these numbers.


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

    @Override
    void draw(float x, float y, float angle, float[] m) {
        if(!loaded) {
            loadTexture();
        }
        if(angle != this.angle) rotate(angle);
        GLES20.glUseProgram(tProgram);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureName);
        // get handle to vertex shader's vPosition member
        int mPositionHandle =
                GLES20.glGetAttribLocation(tProgram, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride/**/, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(tProgram,
                "a_texCoord" );

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray ( mTexCoordLoc );

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(tProgram,
                "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (tProgram,
                "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 0);

        int dispHandle = GLES20.glGetUniformLocation(mProgram, "disp");
        disp[0] = x;
        disp[1] = y;

        GLES20.glUniform4fv(dispHandle, 1, disp, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }

    @Override
    public float getRadius() {
        return 0;
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

}

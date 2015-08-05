package com.example.planes.Engine.Scene;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.util.Log;
import com.example.planes.Engine.MyGLRenderer;
import com.example.planes.Engine.TextureManager;
import com.example.planes.MyApplication;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by egor on 01.07.15.
 */
public class StaticSprite extends Sprite {
    protected final float[] disp = {0,0,0,0};
    protected FloatBuffer uvBuffer;

    private int textureName;

    protected int fileId = 0;
    protected Bitmap bitmap = null;

    protected static final ShortBuffer drawListBuffer;
    protected static final short[] drawOrder = {0, 1, 2, 0, 2, 3}; // order to draw vertices
    protected static final int COORDS_PER_VERTEX = 2;
    protected static final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    protected FloatBuffer vertexBuffer;
    protected final float[] coords;

    static {
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


    }

    protected StaticSprite(float height, float width) {
        coords = new float[8];
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);

        // Create our UV coordinates.
        float[] uvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        // The texture buffer
        bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        h = height;
        w = width;

        rebuild(0, 0, 0);
    }

    protected static float getWidthBy(float h, int fileId) {
        Drawable d = MyApplication.getContext().getResources().getDrawable(fileId);
        return h * ((float)d.getIntrinsicWidth())/d.getIntrinsicHeight();
    }

    protected static float getWidthBy(float h, Bitmap bitmap) {
        return h * ((float)bitmap.getWidth()) / bitmap.getHeight();
    }

    public StaticSprite(Bitmap bmp, float height) {
        this(height, getWidthBy(height, bmp));
        if(bmp == null) throw new NullPointerException();
        bitmap = bmp;
    }

    public StaticSprite(int fileId, float height) {
        this(height, getWidthBy(height, fileId));
        if(fileId == 0) throw new IllegalArgumentException("0 id");
        this.fileId = fileId;
    }

    @Override
    public void load() {
        if(bitmap == null) {
            this.textureName = TextureManager.getTexture(fileId);
        } else {
            this.textureName = TextureManager.getTexture(bitmap);
        }

        textureSurfaceVersion = MyGLRenderer.surfaceVersion;
    }

    @Override
    void draw(float x, float y, float[] m) {


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

        int dispHandle = GLES20.glGetUniformLocation(tProgram, "disp");
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
        return (float) (Math.hypot(w, h)/2);
    }

    @Override
    public void rebuild(float dx, float dy, float angle) {
        coords[6] = w/2 + dx;
        coords[7] = h/2 + dy;
        coords[0] = -w/2 + dx;
        coords[1] = h/2 + dy;
        coords[2] = -w/2 + dx;
        coords[3] = -h/2 + dy;
        coords[4] = w/2 + dx;
        coords[5] = -h/2 + dy;

        for(int i = 0; i < 8; i+=2) {
            double x1 = coords[i];
            double y1 = coords[i + 1];

            coords[i] = (float)(x1 * Math.cos(angle) - y1 * Math.sin(angle));
            coords[i + 1] = (float)(x1 * Math.sin(angle) + y1 * Math.cos(angle));
        }
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
    }



    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("error", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    @Override
    public void onFrame(float graphicsFPS) {
        // ничего не делать
    }
}

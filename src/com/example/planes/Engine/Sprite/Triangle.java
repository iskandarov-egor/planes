package com.example.planes.Engine.Sprite;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by egor on 09.07.15.
 */
public final class Triangle extends OpenGLShape {

    @Override
    public void draw(float x, float y, float angle, float[] transform) {
        super.draw(x, y, angle, transform);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void rotate(float angle) {
        this.angle = angle;

        coords[0] = r*(float)Math.cos(angle);
        coords[1] = r*(float)Math.sin(angle);
        coords[2] = r*(float)Math.cos(angle + 5*Math.PI/6);
        coords[3] = r*(float)Math.sin(angle + 5 * Math.PI / 6);
        coords[4] = r*(float)Math.cos(angle + 7*Math.PI/6);
        coords[5] = r*(float)Math.sin(angle + 7*Math.PI / 6);
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
    }






    public Triangle() {
        super(6);
        Log.d("hey", "Triangle called");

        rotate(0);
    }

    private final int vertexCount = coords.length / COORDS_PER_VERTEX;





}

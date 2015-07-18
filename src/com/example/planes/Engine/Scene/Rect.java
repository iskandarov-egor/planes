package com.example.planes.Engine.Scene;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * Created by egor on 13.07.15.
 */
public class Rect extends OpenGLShape{
    private float w;
    protected float h;
    protected final ShortBuffer drawListBuffer;
    protected final short[] drawOrder = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    public Rect() {
        super(8);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


    }

    @Override
    void draw(float x, float y, float angle, float[] transform) {
        super.draw(x, y, angle, transform);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    @Override
    protected void rotate(float angle) {
        if(this.angle == angle) return;
        this.angle = angle;
        rebuild();
    }

    @Override
    protected void rebuild() {
        coords[6] = w/2;
        coords[7] = h/2;
        coords[0] = -w/2;
        coords[1] = h/2;
        coords[2] = -w/2;
        coords[3] = -h/2;
        coords[4] = w/2;
        coords[5] = -h/2;

        for(int i = 0; i < 8; i+=2) {
            double x1 = coords[i];
            double y1 = coords[i + 1];

            coords[i] = (float)(x1 * Math.cos(angle) - y1 * Math.sin(angle));
            coords[i + 1] = (float)(x1 * Math.sin(angle) + y1 * Math.cos(angle));
        }
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
    }

    public void setWH(float width, float height) {
        w = width;
        h = height;
        rebuild();
    }

}

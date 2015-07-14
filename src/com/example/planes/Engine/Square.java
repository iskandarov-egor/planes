package com.example.planes.Engine;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class Square extends OpenGLShape{

    private final ShortBuffer drawListBuffer;


    private final short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Square() {
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
    protected void rebuild(){
        coords[0] = r*(float)Math.cos(angle + Utils.PI / 4);
        coords[1] = r*(float)Math.sin(angle + Utils.PI / 4);
        coords[2] = r*(float)Math.cos(angle + 3*Utils.PI / 4);
        coords[3] = r*(float)Math.sin(angle + 3*Utils.PI / 4);
        coords[4] = r*(float)Math.cos(angle + 5*Utils.PI / 4);
        coords[5] = r*(float)Math.sin(angle + 5*Utils.PI / 4);
        coords[6] = r*(float)Math.cos(angle - Utils.PI / 4);
        coords[7] = r*(float)Math.sin(angle - Utils.PI / 4);
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
    }

    public void rotate(float angle) {
        this.angle = angle;

        rebuild();
    }

    @Override
    public void draw(float x, float y, float angle, float[] transform) {
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

}
package com.example.planes.Engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import com.example.planes.MyApplication;

/**
 * Created by egor on 18.07.15.
 */
public class GLHelper {
    public static final int createTexture(int fileId) {
        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[2];
        //int[] texturenames2 = new int[1];
        GLES20.glGenTextures(2, texturenames, 0);
        //GLES20.glGenTextures(1, texturenames2, 0);

        // Retrieve our image from resources.


        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(MyApplication.context.getResources(), fileId);


        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
        return texturenames[0];
    }

    public static final void useTexture(int name) {

    }
}

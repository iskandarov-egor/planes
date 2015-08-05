package com.example.planes.Engine.Body;

import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 04.08.15.
 */
public class Polygon extends Body{
    float[] x;
    float[] y;
    float[] d;

    public Polygon(float[] x, float[] y) {
        if(x.length != y.length || x.length <= 0) throw new IllegalArgumentException();

        int len = x.length;
        this.x = new float[len + 1];
        this.y = new float[len + 1];
        d = new float[len];
        for(int i = 0; i < len; i++) {
            this.x[i] = x[i];
            this.y[i] = y[i];
        }
        this.x[len] = x[0];
        this.y[len] = y[0];

        for(int i = 0; i < len; i++) {
            d[i] = (float) Math.hypot(this.x[i + 1] - this.x[i], this.y[i + 1] - this.y[i]);
        }
    }

    @Override
    public boolean intersects(Body body, float dx, float dy, float period) {

        return false;
    }

    @Override
    public boolean isPointInside(float dx, float dy) {
        return false;
    }

    @Override
    public void rebuild(float dx, float dy, float angle) {

    }

    public static boolean linesIntersect(float x[], float y[],float X[], float Y[], int i, int ii) {
//        if(x[i + 1] )
        float q = (y[i + 1]*x[i] - y[i]*x[i + 1]);
        float Q = (Y[ii + 1]*X[ii] - Y[ii]*X[ii + 1]);
        float dx = x[i + 1] - x[i];
        float DX = X[ii + 1] - X[ii];
        float dy = y[i + 1] - y[i];
        float DY = Y[ii + 1] - Y[ii];
        if(dx*DY - DX*dy != 0) {
            float o = (Q*dy - q*DY) / (dx*DY - DX*dy); // y пересечения
            return MathHelper.isBetween(o, y[i], y[i + 1]) && MathHelper.isBetween(o, Y[ii], Y[ii + 1]);
        } else {
            if(dy != 0 && DY != 0) return false; // ||
            if(dy == DY) return false; // ||
            // todo 1 is horiz
            float o = (q*DX - Q*dx) / (dy*DX - DY*dx); // x пересечения
            return MathHelper.isBetween(o, X[i], X[i + 1]) && MathHelper.isBetween(o, X[ii], X[ii + 1]);
        }
    }
}

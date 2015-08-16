package com.example.planes.Engine.Body;

import com.example.planes.Utils.MathHelper;
import com.example.planes.Utils.Vector;

/**
 * Created by egor on 04.08.15.
 */
public class SimplePolygon extends Body {
    float[] x;
    float[] y;
    float[] ax; // с учетом угла и смещения и масштаба
    float[] ay;
    float[] d;

    public SimplePolygon(float[] x, float[] y) {
        if(x.length != y.length || x.length <= 2) throw new IllegalArgumentException();

        int len = x.length;
        this.x = new float[len + 1];
        this.y = new float[len + 1];
        ax = new float[len + 1];
        ay = new float[len + 1];
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

        for(int i = 0; i < this.x.length; i++) {
            ax[i] = this.x[i];
            ay[i] = this.y[i];
        }
    }


    public boolean intersects(SimplePolygon body, float dx, float dy, float period) {
        if(body instanceof SimplePolygon) {
            SimplePolygon p = (SimplePolygon) body;
            int pleft = p.getLeftMost();
            int left = getLeftMost();
            int right = getRightMost();
            int pright = p.getRightMost();
            if(period != 0) {
                float disp = 0;
                while (ax[right] + disp > p.ax[pleft] + dx) disp -= period;
                while (ax[left] + disp < p.ax[pright] + dx) {
                    if(intersects1(p, dx - disp, dy, period).length == 2) return true;
                    disp += period;
                }
                return false;
            } else {
                return (intersects1(p, dx, dy, period).length == 2);
            }
        }
        return false;
    }

    private int getLeftMost() {
        float min = ax[0];
        int res = 0;
        for(int i = 1; i < this.ax.length - 1; i++) {
            if(ax[i] < min) {
                min = ax[i];
                res = i;
            }
        }
        return res;
    }

    private int getRightMost() {
        float max = ax[0];
        int res = 0;
        for(int i = 1; i < this.ax.length - 1; i++) {
            if(ax[i] > max) {
                max = ax[i];
                res = i;
            }
        }
        return res;
    }

    public float[] intersects1(Body body, float dx, float dy, float period) {
        if(body instanceof SimplePolygon) {
//            dx = -dx;
//            dy = -dy;
            SimplePolygon poly = (SimplePolygon) body;
            if(poly.isPointInside(ax[0] - dx, ay[0] - dy)) {
                return new float[2]; // true
            }
            if(isPointInside(poly.ax[0] + dx, poly.ay[0] + dy)) {
                return new float[2]; // true
            }
            // now at least 1 point is outside, look for intersections
            float[] X = poly.ax.clone();
            float[] Y = poly.ay.clone();
            if(!(x.length == y.length && X.length== Y.length)) throw new RuntimeException();

            for(int i = 0; i < X.length; i++) {
                X[i] += dx;
                Y[i] += dy;
            }

            for(int ii = 0; ii < X.length - 1; ii++) {
                for(int i = 0; i < x.length - 1; i++) {
                    float[] xy = linesIntersect(ax, ay, X, Y, i, ii);
                    if(xy.length != 0) return xy;
                }
            }
        }
        return new float[0];
        //throw new RuntimeException();
    }

    @Override
    public boolean isPointInside(float dx, float dy) {
        float x1 = ax[1] - ax[0];
        float y1 = ay[1] - ay[0];

        float k = x1*(dy - ay[0]) - (dx - ax[0])*y1;
        boolean side = k > 0;
        for(int i = 1; i < ax.length - 1; i++) {
            x1 = ax[i + 1] - ax[i];
            y1 = ay[i + 1] - ay[i];

            if(x1*(dy - ay[i]) - (dx - ax[i])*y1 > 0 != side) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void rebuild(float dx, float dy, float angle, float h) {
        Vector vec = new Vector();
        for(int i = 0; i < x.length - 1; i++) {
            vec.set(x[i] * h + dx, y[i] * h + dy);
            vec.rotate(angle);
            ax[i] = vec.x;
            ay[i] = vec.y;
        }
        ax[x.length - 1] = ax[0];
        ay[x.length - 1] = ay[0];
    }

    @Override
    protected float getLeft() {
        return ax[getLeftMost()];
    }

    @Override
    protected float getRight() {
        return ax[getRightMost()];
    }

    public static float[] linesIntersect(float x[], float y[],float X[], float Y[], int i, int ii) {
        float q = (y[i + 1]*x[i] - y[i]*x[i + 1]);
        float Q = (Y[ii + 1]*X[ii] - Y[ii]*X[ii + 1]);
        float dx = x[i + 1] - x[i];
        float DX = X[ii + 1] - X[ii];
        float dy = y[i + 1] - y[i];
        float DY = Y[ii + 1] - Y[ii];
        if(dx*DY - DX*dy != 0) {
            float o = (Q*dy - q*DY) / (dx*DY - DX*dy); // y пересечения

            if( MathHelper.isBetween(o, y[i], y[i + 1]) && MathHelper.isBetween(o, Y[ii], Y[ii + 1])){
                float a, b;
                if(dy != 0) {
                    b = q / dy;
                    a = dx / dy;
                } else {
                    b = Q / DY;
                    a = DX / DY;
                }
                float ox = a * o + b;
                if(MathHelper.isBetween(ox, x[i], x[i+1]) && MathHelper.isBetween(ox, X[ii], X[ii+1])){//todo rare need
                    float[] res = {ox, o};
                    return res;
                }
                return new float[0];
            }
            return new float[0];
        } else {
            return new float[0]; // yup
//            if(dy != 0 && DY != 0) return false; // ||
//            if(dy == DY) return false; // ||
//            float o = (q*DX - Q*dx) / (dy*DX - DY*dx); // x пересечения
//            return MathHelper.isBetween(o, X[i], X[i + 1]) && MathHelper.isBetween(o, X[ii], X[ii + 1]);
        }
    }
}

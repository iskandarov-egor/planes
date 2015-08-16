package com.example.planes.Engine.Body;

/**
 * Created by egor on 02.07.15.
 */
public abstract class Body {
    public boolean intersects(Body body, float dx, float dy, float period) {
        if(body instanceof ComplexPolygon) { // todo maybe some kind of runnable
            if(this instanceof ComplexPolygon) return intersects((ComplexPolygon)body, (ComplexPolygon)this, -dx, -dy,
                    period);
            if(this instanceof Circle) return intersects((ComplexPolygon)body, (Circle)this, -dx, -dy, period);
            if(this instanceof Point) return intersects((ComplexPolygon)body, (Point)this, -dx, -dy, period);
        } else if (body instanceof Circle) {
            if(this instanceof ComplexPolygon) return intersects((ComplexPolygon)this, (Circle)body, dx, dy, period);
            if(this instanceof Circle) return intersects((Circle)body, (Circle)this, dx, dy, period);
            if(this instanceof Point) return intersects((Point)this, (Circle)body, dx, dy, period);
        } else if (body instanceof Point) {
            if(this instanceof ComplexPolygon) return intersects((ComplexPolygon)this, (Point)body, dx, dy, period);
            if(this instanceof Circle) return intersects((Point)body, (Circle)this, -dx, -dy, period);
            if(this instanceof Point) return intersects((Point)this, (Point)body, dx, dy, period);
        } else if(body instanceof SimplePolygon && this instanceof SimplePolygon) {
            throw new RuntimeException();
            //return intersects((SimplePolygon )body, dx, dy, period);
        }
        throw new RuntimeException();
    }

    public abstract boolean isPointInside(float dx, float dy);


    public abstract void rebuild(float dx, float dy, float angle, float h);

    private static boolean intersects(ComplexPolygon poly, Point pt, float dx, float dy, float period) {

        return poly.isPointInside(dx, dy, period);
    }

    public boolean isPointInside(float dx, float dy, float period) {
        float l = getLeft();
        float r = getRight();
        while(dx > l) dx -= period;
        while (dx < r) {
            if(isPointInside(dx, dy)) {
                return true;
            }
            dx += period;
        }
        return false;
    }

    protected abstract float getLeft();

    protected abstract float getRight();

    private static boolean intersects(ComplexPolygon poly, Circle circle, float dx, float dy, float period) {
        throw new RuntimeException();
    }

    private static boolean intersects(ComplexPolygon p1, ComplexPolygon p2, float dx, float dy, float period) {
        return p1.intersects(p2, dx, dy, period);
    }

    private static boolean intersects(Point p1, Point p2, float dx, float dy, float period) {
        throw new RuntimeException();
    }

    private static boolean intersects(Point pt, Circle circle, float dx, float dy, float period) {
        return circle.isPointInside(dx, dy, period);
    }

    private static boolean intersects(Circle c1, Circle c2, float dx, float dy, float period) {
        return c1.intersects(c2, dx, dy, period);
    }
}

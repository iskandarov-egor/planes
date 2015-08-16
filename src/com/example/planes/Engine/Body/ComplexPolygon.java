package com.example.planes.Engine.Body;

import com.example.planes.Engine.Scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egor on 13.08.15.
 */
public class ComplexPolygon extends Body {
    List<SimplePolygon> polygons = new ArrayList<>();

    public ComplexPolygon(float[] x, float[] y) {
        addSimplePolygon(x, y);
    }

    public void addSimplePolygon(float[] x, float[] y) {
        polygons.add(new SimplePolygon(x, y));
    }

    protected boolean intersects(ComplexPolygon p2, float dx, float dy, float period) {
        for(SimplePolygon polygon : polygons) {
            for(SimplePolygon sp2 : p2.polygons) {
                if (polygon.intersects(sp2, dx, dy, period)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPointInside(float dx, float dy) {
        for(SimplePolygon polygon : polygons) {
            if(polygon.isPointInside(dx, dy)) return true;
        }
        return false;
    }

    @Override
    public void rebuild(float dx, float dy, float angle, float h) {
        for(SimplePolygon polygon : polygons) {
            polygon.rebuild(dx, dy, angle, h);
        }
    }

    @Override
    protected float getLeft() {
        float left = polygons.get(0).getLeft();
        for(SimplePolygon polygon : polygons) { // toopt
            float x = polygon.getLeft();
            if(x < left) left = x;
        }
        return left;
    }

    @Override
    protected float getRight() {
        float right = polygons.get(0).getRight();
        for(SimplePolygon polygon : polygons) { // toopt
            float x = polygon.getRight();
            if(x > right) right= x;
        }
        return right;
    }
}

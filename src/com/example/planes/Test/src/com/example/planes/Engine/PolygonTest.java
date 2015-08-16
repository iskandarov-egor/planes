package com.example.planes.Engine;

import com.example.planes.Config.Config;
import com.example.planes.Engine.Body.Point;
import com.example.planes.Engine.Body.ComplexPolygon;
import com.example.planes.Engine.Body.SimplePolygon;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneObject;

/**
 * Created by egor on 04.08.15.
 */
public class PolygonTest extends  junit.framework.TestCase{
    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void testLines() {
        float[] x = {0, 0, 266, 586, 0, 0};
        float[] y = {0, 0, 89, 459, 0, 0};
        float[] X = {477, 587, 0 ,0};
        float[] Y = {418, 98, 0 , 0};
        assertTrue(SimplePolygon.linesIntersect(x, y, X, Y, 2, 0).length == 2);

    }

    public void testLines2() {
        float[] x = {0, 0, 11, 11, 0, 0};
        float[] y = {0, 0, 22, 33, 0, 0};
        float[] X = {1, 111, 0 ,0};
        float[] Y = {24, 24, 0 , 0};
        assertTrue(SimplePolygon.linesIntersect(x, y, X, Y, 2, 0).length == 2);
    }

    public void testLines3() {
        float[] x = {11, 11, 0, 0};
        float[] y = {22, 33, 0, 0};
        float[] X = {0, 1, 111, 0 ,0};
        float[] Y = {0, 24, 24, 0 , 0};
        assertTrue(SimplePolygon.linesIntersect(x, y, X, Y, 0, 1).length == 2);
    }

    public void testSquares() {
        float[] x = {0, 11, 11, 0};
        float[] y = {0, 0, 11, 11};
        float[] X = {0, 11, 11, 0};
        float[] Y = {0, 0, 11, 11};
        SimplePolygon p1 = new SimplePolygon(x, y);
        SimplePolygon p2 = new SimplePolygon(X, Y);
        float o = p1.intersects1(p2, 5, 5, 0)[1];
        assertTrue(o == 5 || p1.isPointInside(X[0] + 5, Y[0] + 5) || p2.isPointInside(x[0] - 5, y[0] - 5));
    }

    public void testSTARZ() {
        float[] x = {320, 322, 388, 513, 506, 617, 422};
        float[] y = {105, 296, 440, 393, 252, 127, 220};
        float[] X = {465, 534, 444, 627, 580, 741, 795, 730, 708};
        float[] Y = {286, 303, 418, 332, 428, 383, 307, 243, 302};
        float[] correct1 = {507, 287};
        float[] correct2 = {509, 297};
        float[] correct3 = {511, 335};
        float[] correct4 = {513, 385};
        float[] correct5 = {506, 390};
        float[] correct6 = {478, 375};
        SimplePolygon p1 = new SimplePolygon(x, y);
        SimplePolygon p2 = new SimplePolygon(X, Y);
        float[] o = p1.intersects1(p2, 0, 0, 0);
        assertTrue(o.length == 2);

        assertTrue(
                pointNear(o, correct1) || pointNear(o, correct2) || pointNear(o, correct3) ||
                pointNear(o, correct4) || pointNear(o, correct5) || pointNear(o, correct6) ||
                p2.isPointInside(x[0], y[0]) || p1.isPointInside(X[0], Y[0])
        );

        //test symmetry
        o = p2.intersects1(p1, 0, 0, 0);
        assertTrue(o.length == 2);

        assertTrue(
                pointNear(o, correct1) || pointNear(o, correct2) || pointNear(o, correct3) ||
                pointNear(o, correct4) || pointNear(o, correct5) || pointNear(o, correct6) ||
                p1.isPointInside(X[0], Y[0]) || p1.isPointInside(X[0], Y[0])
        );
    }

    public void testRotate() {
        float[] x = {0, 11, 11, 0};
        float[] y = {12, 12, 23, 23};
        float[] X = {0, 11, 11, 0};
        float[] Y = {0, 0, 11, 11};
        SimplePolygon p1 = new SimplePolygon(x, y);
        SimplePolygon p2 = new SimplePolygon(X, Y);
        assertTrue(p1.intersects1(p2, 0, 0, 0).length == 0);
        p1.rebuild(0, 0, -0.5f, 1);
        assertTrue(p1.intersects1(p2, 0, 0, 0).length == 2);
    }

    public void test() {
        float[] x = {0, 0.5f, 1};
        float[] y = {0, 2, 0};
        float[] X = {0, 1, 1, 0};
        float[] Y = {0, 0, 1, 1};

        ComplexPolygon p1 = new ComplexPolygon(x, y);
        ComplexPolygon p2 = new ComplexPolygon(X, Y);
        Scene scene = new Scene(null);
        SceneObject o1 = new SceneObject(0, 0, scene, 1);
        o1.setBody(p1);

        SceneObject o2 = new SceneObject(0, 1.9f, scene, 1);
        o2.setBody(p2);
        SimplePolygon sp1 = new SimplePolygon(x, y);
        SimplePolygon sp2 = new SimplePolygon(X, Y);
        assertTrue(sp1.intersects(sp2, 0, 1.9f, 0));
        assertTrue(p1.intersects(p2, 0, 1.9f, 0));

        assertTrue(o1.intersects(o2, 0));
    }


    public void testPoint() {
        float[] x = {127, 170, 354};
        float[] y = {190, 330, 86};
        ComplexPolygon p = new ComplexPolygon(x, y);
        assertTrue(p.isPointInside(166, 282));
        assertTrue(p.isPointInside(218, 155));
        assertTrue(p.isPointInside(200, 200));
        assertFalse(p.isPointInside(0, 0));
        assertFalse(p.isPointInside(333, 333));
        assertFalse(p.isPointInside(238, 254));
    }

    public void testPeriod() {
        float[] x = {-5, 6, 6, -5};
        float[] y = {0, 0, 11, 11};
        float[] X = {7, 18, 18, 7};
        float[] Y = {0, 0, 11, 11};
        ComplexPolygon p1 = new ComplexPolygon(x, y);
        ComplexPolygon p2 = new ComplexPolygon(X, Y);
        assertFalse(p1.intersects(p2, 0, 5, 111));
        assertTrue(p1.intersects(p2, 0, 5, 18));
        assertFalse(p2.intersects(p1, 0, -5, 111));
        assertTrue(p2.intersects(p1, 0, -5, 18));
    }

    public void testUltimate() {
        float[] x = {-5, 5, 5, -5};
        float[] y = {-5, -5, 5, 5};
        float[] X = {-5, 5, 5, -5};
        float[] Y = {6, 6, 16, 16};
        ComplexPolygon p1 = new ComplexPolygon(x, y);
        ComplexPolygon p2 = new ComplexPolygon(X, Y);
        SimplePolygon sp1 = new SimplePolygon(x, y);
        SimplePolygon sp2 = new SimplePolygon(X, Y);
        assertFalse(sp1.intersects(sp2, 89, 0, 110));
        assertFalse(p1.intersects(p2, 89, 0, 110));

        assertFalse(p1.intersects(p2, 89, -11, 110));
        p1.rebuild(0, 0, 0.8f, 1);
        sp1.rebuild(0, 0, 0.8f, 1);
        assertFalse(sp1.intersects(sp2, 89, -11, 110));
        assertFalse(p1.intersects(p2, 89, -11, 110));

        assertFalse(p2.intersects(p1, -89, 11, 110));
        assertTrue(sp1.intersects(sp2, 89, -11, 100));
        assertTrue(p1.intersects(p2, 89, -11, 100));

        assertTrue(p2.intersects(p1, -89, 11, 100));
    }

    public void testPlanePoint() {
        ComplexPolygon plane = new ComplexPolygon(Config.planePolyX[0], Config.planePolyY[0]);
        for(int i = 1; i < Config.planePolyX.length; i++) {
            plane.addSimplePolygon(Config.planePolyX[i], Config.planePolyY[i]);
        }
        Point bullet = new Point();
        assertTrue(plane.isPointInside(0.2f, 0.1f));
        assertTrue(plane.intersects(bullet, 0.2f, 0.1f, 10));
        assertTrue(bullet.intersects(plane, -0.2f, -0.1f, 10));
        assertFalse(plane.intersects(bullet, 10f, 0.1f, 100));
        assertTrue(plane.intersects(bullet, 10f, 0.1f, 10.5f));
    }

    private boolean pointNear(float[] o, float[] correct) {
        return pointNear(o[0], o[1], correct[0], correct[1]);
    }

    private boolean pointNear(float x1, float y1, float x2, float y2) {
        return pointNear(x1, y1, x2, y2, 3);
    }

    private boolean pointNear(float x1, float y1, float x2, float y2, float eps) {
        float d = (float) Math.hypot(x2-x1, y2-y1);
        return (d < eps);
    }
}

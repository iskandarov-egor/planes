package com.example.planes.Engine;

import com.example.planes.Engine.Body.Polygon;

/**
 * Created by egor on 04.08.15.
 */
public class PolygonTest extends  junit.framework.TestCase{
    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void testLines() {
        float[] x = {266, 586, 0, 0};
        float[] y = {89, 459, 0, 0};
        float[] X = {477, 587, 0 ,0};
        float[] Y = {418, 98, 0 , 0};
        assertTrue(Polygon.linesIntersect(x, y, X, Y, 0, 0));

    }
}

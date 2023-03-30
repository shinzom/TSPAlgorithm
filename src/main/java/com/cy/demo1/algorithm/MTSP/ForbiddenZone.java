package com.cy.demo1.algorithm.MTSP;

public class ForbiddenZone {
    public int nodeNum;
    public double[] x;
    public double[] y;

    private boolean pointOnLineSegment(double x1, double y1, double x2, double y2, double px, double py) {
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);

        if (px < minX || px > maxX || py < minY || py > maxY) {
            return false;
        }

        double slope = (y2 - y1) / (x2 - x1);
        double intercept = y1 - slope * x1;

        return Math.abs(py - (slope * px + intercept)) < 1e-10;
    }

    public boolean isPointInside(double pointX, double pointY) {
        boolean inside = false;
        int j = nodeNum - 1;
        for (int i = 0; i < nodeNum; j = i++) {
            if (pointOnLineSegment(x[i], y[i], x[j], y[j], pointX, pointY)) {
                return false;
            }
            if (((y[i] > pointY) != (y[j] > pointY)) &&
                    (pointX < (x[j] - x[i]) * (pointY - y[i]) / (y[j] - y[i]) + x[i])) {
                inside = !inside;
            }
        }
        return inside;
    }
}

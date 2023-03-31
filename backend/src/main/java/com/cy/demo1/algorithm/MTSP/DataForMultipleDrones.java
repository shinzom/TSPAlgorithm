package com.cy.demo1.algorithm.MTSP;

public class DataForMultipleDrones {
    public int num;
    public double[] x;
    public double[] y;
    public ForbiddenZone[] forbiddenZones;
    // 判断两点之间的连线是否穿过禁区内部（射线法）
    public boolean isCrossingForbiddenZone(double x1, double y1, double x2, double y2) {
        int numberOfTestPoints = 1000;

        for (int i = 1; i < numberOfTestPoints; i++) {
            double testX = x1 + i * (x2 - x1) / (numberOfTestPoints + 1);
            double testY = y1 + i * (y2 - y1) / (numberOfTestPoints + 1);

            if (isInsideForbiddenZone(testX, testY)) {
                return true;
            }
        }

        return false;
    }
    private boolean isInsideForbiddenZone(double x, double y) {
        if (forbiddenZones != null)
        {
            for (ForbiddenZone zone : forbiddenZones) {
                if (zone.isPointInside(x, y))
                {
                    return true;
                }
            }
        }


        return false;
    }
}


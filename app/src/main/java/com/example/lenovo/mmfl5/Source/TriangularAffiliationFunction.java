package com.example.lenovo.mmfl5.Source;

public class TriangularAffiliationFunction implements AffiliationFunction {

    private double a;
    private double b;
    private double c;

    public TriangularAffiliationFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public TriangularAffiliationFunction() {

    }

    @Override
    public double calculateAffiliationFunction(double x) {
        if(x <= a || x >= c) {
            return 0;
        } else if(a <= x && x <=b) {
            return (x - a) / (b - a);
        } else if(b <= x && x <=c) {
            return (c - x) / (c - b);
        }
        return 0;
    }

    @Override
    public double getMinX() {
        return a - 1.5;
    }

    @Override
    public double getMaxX() {
        return c + 1.5;
    }

    @Override
    public boolean checkConstrains(double[] p) {
        return p[0] <= p[1] && p[1] <= p[2] && p[0] <= p[2];
    }
}

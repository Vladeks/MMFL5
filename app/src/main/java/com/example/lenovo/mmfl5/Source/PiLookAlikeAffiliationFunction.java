package com.example.lenovo.mmfl5.Source;

public class PiLookAlikeAffiliationFunction implements AffiliationFunction {

    private double a;
    private double b;
    private double c;
    private double d;

    private ZLookAlikeAffiliationFunction left;
    private SLookAlikeAffiliationFunction right;

    public PiLookAlikeAffiliationFunction(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        left = new ZLookAlikeAffiliationFunction(c, d);
        right = new SLookAlikeAffiliationFunction(a, b);
    }

    public PiLookAlikeAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        return left.calculateAffiliationFunction(x) * right.calculateAffiliationFunction(x);
    }

    @Override
    public double getMinX() {
        return a - 2;
    }

    @Override
    public double getMaxX() {
        return d + 2;
    }

    @Override
    public boolean checkConstrains(double[] p) {
        return p[0] <= p[1] && p[1] <= p[2] && p[2] <= p[3] && p[0] <= p[3] && p[0] <= p[2] && p[1] <= p[3];
    }
}

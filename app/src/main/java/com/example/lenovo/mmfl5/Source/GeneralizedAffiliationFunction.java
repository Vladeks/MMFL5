package com.example.lenovo.mmfl5.Source;

public class GeneralizedAffiliationFunction implements AffiliationFunction {

    private double a;
    private double b;
    private double c;

    public GeneralizedAffiliationFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public GeneralizedAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        return 1 / (1 + Math.pow( Math.abs( (x - c) / a ), 2 * b));
    }

    @Override
    public double getMinX() {
        return c - 3*a;
    }

    @Override
    public double getMaxX() {
        return c + 3*a;
    }

    @Override
    public boolean checkConstrains(double[] p) {
        return p[0] > 0;
    }
}

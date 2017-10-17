package com.example.lenovo.mmfl5.Source;

public class SigmoidAffiliationFunction implements AffiliationFunction {

    private double a;
    private double c;

    public SigmoidAffiliationFunction(double a, double c) {
        this.a = a;
        this.c = c;
    }

    public SigmoidAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        return 1 / (1 + Math.exp(-a * ( x - c )));
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
        return p[0] < p[1];
    }
}

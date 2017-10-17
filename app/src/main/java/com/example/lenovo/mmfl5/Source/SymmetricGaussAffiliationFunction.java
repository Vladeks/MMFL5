package com.example.lenovo.mmfl5.Source;

public class SymmetricGaussAffiliationFunction implements AffiliationFunction {

    private double b;
    private double c;

    public SymmetricGaussAffiliationFunction(double b, double c) {
        this.b = b;
        this.c = c;
    }

    public SymmetricGaussAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        return Math.exp(
                -( x * x - 2 * b * x + b * b ) / (2 * Math.pow(c, 2.0))
        );
    }

    @Override
    public double getMinX() {
        return b - 3*c;
    }

    @Override
    public double getMaxX() {
        return b + 3*c;
    }

    @Override
    public boolean checkConstrains(double[] p) {
        return p[0] > 0 && p[1] > 0 && p[2] > 0;
    }
}

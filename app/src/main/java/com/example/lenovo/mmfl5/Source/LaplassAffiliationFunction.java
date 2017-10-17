package com.example.lenovo.mmfl5.Source;

public class LaplassAffiliationFunction implements AffiliationFunction {

    private double a;
    private double b;

    public LaplassAffiliationFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public LaplassAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        return Math.exp(- (Math.abs(x - a) / b) );
    }

    @Override
    public double getMinX() {
        return a - 2.5*a*b;
    }

    @Override
    public double getMaxX() {
        return a + 2.5*a*b;
    }

    /**
     * @return b > 0
     */
    @Override
    public boolean checkConstrains(double[] p) {
        return p[1] > 0;
    }

}

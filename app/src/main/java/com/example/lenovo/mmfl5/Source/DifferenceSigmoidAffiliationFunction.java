package com.example.lenovo.mmfl5.Source;

public class DifferenceSigmoidAffiliationFunction implements AffiliationFunction {

    private double a1;
    private double a2;
    private double c1;
    private double c2;

    private SigmoidAffiliationFunction firstSigmoid;
    private SigmoidAffiliationFunction secondSigmoid;

    public DifferenceSigmoidAffiliationFunction(double a1, double c1, double a2, double c2) {
        this.a1 = a1;
        this.a2 = a2;
        this.c1 = c1;
        this.c2 = c2;

        firstSigmoid = new SigmoidAffiliationFunction(a1,c1);
        secondSigmoid = new SigmoidAffiliationFunction(a2, c2);
    }

    public DifferenceSigmoidAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        return firstSigmoid.calculateAffiliationFunction(x) - secondSigmoid.calculateAffiliationFunction(x);
    }

    @Override
    public double getMinX() {
        return c1 - 3*Math.abs(a1);
    }

    @Override
    public double getMaxX() {
        return c2 + 3*Math.abs(a2);
    }

    @Override
    public boolean checkConstrains(double[] p) {
        return true;
    }
}

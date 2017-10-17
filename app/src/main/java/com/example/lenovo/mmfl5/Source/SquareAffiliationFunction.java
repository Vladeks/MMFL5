package com.example.lenovo.mmfl5.Source;

public class SquareAffiliationFunction implements AffiliationFunction {

    private double a;
    private double b;

    public SquareAffiliationFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public SquareAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        if(squareRoot(x) < 1) {
            return 1 - squareRoot(x);
        }
        return 0;
    }

    private double squareRoot(double x) {
      return Math.pow( ((x - a) / b), 2.0);
    }

    @Override
    public double getMinX() {
        return a - b*3;
    }

    @Override
    public double getMaxX() {
        return a + b*3;
    }

    @Override
    public boolean checkConstrains(double[] p) {
        return p[1] !=0;
    }
}

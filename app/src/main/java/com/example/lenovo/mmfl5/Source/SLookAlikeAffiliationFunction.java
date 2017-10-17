package com.example.lenovo.mmfl5.Source;

public class SLookAlikeAffiliationFunction implements AffiliationFunction {

    private double a;
    private double b;

    public SLookAlikeAffiliationFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public SLookAlikeAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        if( x <= a) {
            return  0.0;
        } else if(a <= x && x <= b) {
            return 0.5 + 0.5 * ( Math.cos( ((x - b) / (b - a)) * Math.PI ));
        } else if(x >= b) {
            return 1.0;
        }
        return 0;
    }

    @Override
    public double getMinX() {
        return a - 2;
    }

    @Override
    public double getMaxX() {
        return b + 2;
    }

    @Override
    public boolean checkConstrains(double[] p) {
        return p[0] <= p[1];
    }
}

package com.example.lenovo.mmfl5.Source;

public class ZLookAlikeAffiliationFunction implements AffiliationFunction {

    private double a;
    private double b;

    public ZLookAlikeAffiliationFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public ZLookAlikeAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        if( x <= a) {
            return  1.0;
        } else if(a <= x && x <= b) {
            return 0.5 + 0.5 * ( Math.cos( ((x - a) / (b - a)) * Math.PI ));
        } else if(x >= b) {
            return 0;
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

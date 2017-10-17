package com.example.lenovo.mmfl5.Source;

public class TwoSideGaussAffiliationFunction implements AffiliationFunction {

    private double a;
    private double c1;
    private double c2;
    private double b;

    public TwoSideGaussAffiliationFunction(double a, double c1, double c2, double b) {
        this.a = a;
        this.c1 = c1;
        this.c2 = c2;
        this.b = b;
    }

    public TwoSideGaussAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        if(c1 < c2) {
            return leftSide(x);
        } else {
            return rightSide(x);
        }
    }

    @Override
    public double getMinX() {
        return c1 - 3*a;
    }

    @Override
    public double getMaxX() {
        return c2 + 3*b;
    }

    @Override
    public boolean checkConstrains(double[] p) {
        return p[0] <= p[1] && p[1] <= p[2] && p[2] <= p[3] && p[0] <= p[3] && p[0] <= p[2] && p[1] <= p[3];
    }

    private double leftSide(double x) {
        if(x < c1) {
            return exp(x, c1, a);
        } else if(c1 <= x && x <= c2) {
            return 1.0;
        } else if(x > c2) {
            return exp(x, c2, b);
        }
        return 0;
    }

    private double rightSide(double x) {
        if(x < c1) {
            return exp(x, c1, a);
        } else if(c1 <= x && x <= c2) {
            return exp(x, c1, a) * exp(x, c2, b);
        } else if(x > c2) {
            return exp(x, c2, b);
        }
        return 0;
    }

    private double exp(double x, double c, double a) {
        return Math.exp(- Math.pow((x - c), 2.0) / ( 2 * Math.pow(a, 2.0) ));
    }

    /**
     * @return a <= b && b <= c && c <= d
     */
    public static boolean checkConstrains(double a, double b, double c, double d) {
        return a <= b && b <= c && c <= d && a <= d && a <= c && b <= d;
    }
}

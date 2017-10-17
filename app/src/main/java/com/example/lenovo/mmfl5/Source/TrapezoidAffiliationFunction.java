package com.example.lenovo.mmfl5.Source;

public class TrapezoidAffiliationFunction implements AffiliationFunction {

    private double a;
    private double b;
    private double c;
    private double d;

    public TrapezoidAffiliationFunction(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public TrapezoidAffiliationFunction() {
    }

    @Override
    public double calculateAffiliationFunction(double x) {
        if(x <= a || x >= d) {
            return 0;
        } else if(a <= x && x <=b) {
            return (x - a) / (b - a);
        } else if(b <= x && x <=c) {
            return 1.0;
        } else if(c <= x && x <=d) {
            return (d - x) / (d - c);
        }
        return 0;
    }

    @Override
    public double getMinX() {
        return a - 1.5;
    }

    @Override
    public double getMaxX() {
        return d + 1.5;
    }

    /**
     * @return a <= b && b <= c && c <= d
     */
    @Override
    public boolean checkConstrains(double[] p) {
        return p[0] <= p[1] && p[1] <= p[2] && p[2] <= p[3] && p[0] <= p[3] && p[0] <= p[2] && p[1] <= p[3];
    }
}

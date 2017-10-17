package com.example.lenovo.mmfl5.Source;

public interface AffiliationFunction {

    double DELTA = 0.1;

    double calculateAffiliationFunction(double x);

    double getMinX();

    double getMaxX();

    boolean checkConstrains(double[] p);

}

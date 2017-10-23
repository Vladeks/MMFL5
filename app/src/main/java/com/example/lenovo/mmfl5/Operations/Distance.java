package com.example.lenovo.mmfl5.Operations;

import com.jjoe64.graphview.series.DataPoint;

public class Distance {

    private DataPoint[] functionA;
    private DataPoint[] functionB;

    public  DataPoint[] resultHeming;
    public  DataPoint[] resultEuclid;

    public  DataPoint[] entrophyPi;
    public  DataPoint[] entrophyH;
    public  DataPoint[] affilationArray;

    public Distance(DataPoint[] functionA, DataPoint[] functionB) {
        this.functionA = functionA;
        this.functionB = functionB;
    }

    public Distance(DataPoint[] functionA) {
        this.functionA = functionA;
    }

    public double calculateHemingDistance() {
        return hemingDistance(functionA, functionB);
    }

    public double hemingDistance(DataPoint[] funA, DataPoint[] funB) {
        double hemingDistance = 0;
        DataPoint[] result = new DataPoint[funA.length];
        for (int i = 0; i < funA.length; i++) {
            result[i] = new DataPoint(funA[i].getX() ,Math.abs(funA[i].getY() - funB[i].getY()));
            hemingDistance += result[i].getY();
        }
        resultHeming = result;
        return hemingDistance;
    }

    public double calculateEuclidDistance() {
        return euclidDistance(functionA, functionB);
    }

    public double euclidDistance(DataPoint[] funA, DataPoint[] funB) {
        double euclidDistance = 0;
        DataPoint[] result = new DataPoint[funA.length];
        for (int i = 0; i < funA.length; i++) {
            result[i] = new DataPoint(funA[i].getX() ,Math.pow(funA[i].getY() - funB[i].getY(), 2));
            euclidDistance += result[i].getY();
        }
        resultEuclid = result;
        return Math.sqrt(euclidDistance);
    }

    private double affilationFunction(double mx) {
        if (mx > 0.5) {
            return 1;
        } else {
            return 0;
        }
    }

    private DataPoint[] affilationArray() {
        DataPoint[] affilationArray = new DataPoint[functionA.length];
        for (int i = 0; i < affilationArray.length; i++) {
            affilationArray[i] = new DataPoint(
                    functionA[i].getX(),
                    affilationFunction(functionA[i].getY())
            );
        }
        return affilationArray;
    }

    public double calculateLineFuzzyIndex() {
        affilationArray  = affilationArray();
        return  2 / functionA.length * hemingDistance(functionA, affilationArray);
    }

    public double calculateSquareFuzzyIndex() {
        affilationArray  = affilationArray();
        return  2 / Math.sqrt(functionA.length) * euclidDistance(functionA, affilationArray);
    }

    public double calculateEntropy() {
        entrophyPi();
        entrophyH();

        return 1 / Math.log(functionA.length) * sum(entrophyPi);
    }

    private void entrophyPi() {
        entrophyPi = new DataPoint[functionA.length];
        double sum = sum(functionA);
        for (int i = 0; i < entrophyPi.length; i++) {
            entrophyPi[i] = new DataPoint(
                    functionA[i].getX(),
                    functionA[i].getY() / sum
            );
        }
    }

    private void entrophyH() {
        entrophyH = new DataPoint[functionA.length];
        for (int i = 0; i < entrophyH.length; i++) {
            entrophyH[i] = new DataPoint(
                    entrophyPi[i].getX(),
                    entrophyPi[i].getY() * Math.log(entrophyPi[i].getY())
            );
        }
    }

    private double sum(DataPoint[] array) {
        double sum = 0;
        for (DataPoint point: array) {
            sum += point.getY();
        }
        return sum;
    }
}

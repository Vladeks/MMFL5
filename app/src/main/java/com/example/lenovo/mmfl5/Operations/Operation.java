package com.example.lenovo.mmfl5.Operations;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.ArraySet;
import com.example.lenovo.mmfl5.Source.AffiliationFunction;
import com.jjoe64.graphview.series.DataPoint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public abstract class Operation {

    protected abstract double operation(double first, double second);

    public DataPoint[] calculateFunctionPoints(AffiliationFunction function, double min, double max) {
        int count = (int) Math.round((max - min) / function.DELTA);
        DataPoint[] data = new DataPoint[count];
        double x = function.getMinX();
        double y;
        for (int i = 0; i < count; i++) {
            y = function.calculateAffiliationFunction(x);
            data[i] = new DataPoint(x, y);
            x += function.DELTA;
        }
        return data;
    }

    public DataPoint[] calculateFunctionPoints(AffiliationFunction function, double min, double max, double stepSize) {
        int count = (int) Math.round((max - min) / stepSize);
        if(count < 1) {
            count = 1;
        }
        DataPoint[] data = new DataPoint[count];
        double x = function.getMinX();
        double y;
        for (int i = 0; i < count; i++) {
            y = function.calculateAffiliationFunction(x);
            data[i] = new DataPoint(x, y);
            x += stepSize;
        }
        return data;
    }

    public double calculateStepSize(AffiliationFunction functionA, AffiliationFunction functionB, int steps) {
        double distanceA = functionA.getMaxX() - functionA.getMinX();
        double distanceB = functionB.getMaxX() - functionB.getMinX();
        return Math.min(distanceA, distanceB) / steps;
    }

    public double calculateMin(AffiliationFunction functionA, AffiliationFunction functionB) {
        return Math.min(functionA.getMinX(), functionB.getMinX());
    }

    public double calculateMax(AffiliationFunction functionA, AffiliationFunction functionB) {
        return Math.max(functionA.getMaxX(), functionB.getMaxX());
    }

    public DataPoint[] execute(AffiliationFunction firstOperand, AffiliationFunction secondOperand) {
        double min = calculateMin(firstOperand, secondOperand);
        double max = calculateMax(firstOperand, secondOperand);
        return getMaxSingletonsOfArray(getAllPossibleVariants(
                calculateFunctionPoints(firstOperand, min, max),
                calculateFunctionPoints(secondOperand,  min, max)
        ));
    }

    public DataPoint[] execute(AffiliationFunction firstOperand, AffiliationFunction secondOperand, int steps) {
        double min = calculateMin(firstOperand, secondOperand);
        double max = calculateMax(firstOperand, secondOperand);
        double stepSize = calculateStepSize(firstOperand, secondOperand, steps);
        return getMaxSingletonsOfArray(getAllPossibleVariants(
                calculateFunctionPoints(firstOperand, min, max, stepSize),
                calculateFunctionPoints(secondOperand, min, max, stepSize)
        ));
    }

    private ArrayList<DataPoint> getAllPossibleVariants(DataPoint[] firstOperand, DataPoint[] secondOperand) {
        ArrayList<DataPoint> result = new ArrayList<>();

        for (DataPoint first: firstOperand) {
            for (DataPoint second : secondOperand) {
                result.add(new DataPoint(
                        operation(first.getX(), second.getX()),
                        Math.min(first.getY() , second.getY())
                ));
            }
            
        }
        System.out.println("first"+Arrays.toString(firstOperand));
        System.out.println("second"+Arrays.toString(secondOperand));
        System.out.println("result"+result.toString());
        return result;
    }

//    private DataPoint[] getMaxSingletonsOfArray(ArrayList<DataPoint> array) {
//        ArrayMap<Double, DataPoint> hashMap = new ArrayMap<>();
//
//        for (DataPoint point:array) {
//            if(hashMap.containsKey(point.getX())) {
//                if(hashMap.get(point.getX()).getY() < point.getY()) {
//                    hashMap.put(point.getX(), point);
//                } else{
//                    continue;
//                }
//            } else {
//                hashMap.put(point.getX(), point);
//            }
//        }
//        ArrayList<DataPoint> result = new ArrayList<>( hashMap.values());
//        Collections.sort(result, new Comparator<DataPoint>() {
//            @Override
//            public int compare(DataPoint dataPoint, DataPoint t1) {
//                return Double.compare(dataPoint.getX(), t1.getX());
//            }
//        });
////        Collections.sort(result, new Comparator<DataPoint>() {
////            @Override
////            public int compare(DataPoint dataPoint, DataPoint t1) {
////                return Double.compare(dataPoint.getY(), t1.getY());
////            }
////        });
//        System.out.println(result.size());
//        System.out.println(result.toString());
//        return  result.toArray(new DataPoint[hashMap.size()]);
//    }
//

    private DataPoint[] getMaxSingletonsOfArray(ArrayList<DataPoint> array) {
        ArrayMap<String, DataPoint> hashMap = new ArrayMap<>();

        for (DataPoint point:array) {
            if(hashMap.containsKey(keyOf(point.getX()))) {
                if(hashMap.get(keyOf(point.getX())).getY() < point.getY()) {
                    hashMap.put(keyOf(point.getX()), point);
                } else{
                    continue;
                }
            } else {
                hashMap.put(keyOf(point.getX()), point);
            }
        }
        ArrayList<DataPoint> result = new ArrayList<>( hashMap.values());
        Collections.sort(result, new Comparator<DataPoint>() {
            @Override
            public int compare(DataPoint dataPoint, DataPoint t1) {
                return Double.compare(dataPoint.getX(), t1.getX());
            }
        });
//        Collections.sort(result, new Comparator<DataPoint>() {
//            @Override
//            public int compare(DataPoint dataPoint, DataPoint t1) {
//                return Double.compare(dataPoint.getY(), t1.getY());
//            }
//        });
        System.out.println(result.size());
        System.out.println(result.toString());
        return  result.toArray(new DataPoint[hashMap.size()]);
    }

    private String keyOf(double x) {
        return Double.valueOf(x).toString().substring(0, 3);
    }

}

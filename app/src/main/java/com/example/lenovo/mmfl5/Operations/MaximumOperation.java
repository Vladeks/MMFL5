package com.example.lenovo.mmfl5.Operations;

public class MaximumOperation extends Operation {
    @Override
    protected double operation(double first, double second) {
        return Math.max(first, second);
    }
}

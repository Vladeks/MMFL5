package com.example.lenovo.mmfl5.Operations;

public class MinimumOperation extends Operation {
    @Override
    protected double operation(double first, double second) {
        return Math.min(first, second);
    }
}

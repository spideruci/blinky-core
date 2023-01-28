package hyr.tests;

import hyr.tests.Calculator2;

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public int hybrid(int a, int b) {
        Calculator2 c2 = new Calculator2();
        return c2.mul(a + b, a - b);
    }
}
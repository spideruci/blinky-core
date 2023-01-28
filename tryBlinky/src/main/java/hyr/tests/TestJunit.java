package hyr.tests;


import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestJunit {
    Calculator calculator = new Calculator();

    @Test
    public void testAddition() {
        System.out.println("in testAddition");
        assertEquals(8, calculator.add(5, 3));
    }

    @Test
    public void testSubtraction() {
        System.out.println("in testSubtraction");
        assertEquals(2, calculator.sub(5, 3));
    }
}
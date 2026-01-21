package asia.castis.evoucher.push;

import asia.castis.evoucher.push.model.Calculator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
/*Example of Test with custom TestRunner*/
@RunWith(TestRunner.class)
public class CalculatorTest {
    Calculator calculator = new Calculator();

    @Test
    public void testAddition() {
        System.out.println("in testAddition");
        assertEquals("addition", 8, calculator.add(5, 3));
    }
}
package backend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Stack;

public interface Plugin {

    BigDecimal exec(String operator, Stack<BigDecimal> operands);
    ArrayList<String> getOperators();
    int numOperands();

}

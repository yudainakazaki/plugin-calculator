package Code;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.text.DecimalFormat;

public class Main implements Plugin {

    //singleton
    private static Main basicPlugin = new Main();
    private Main(){}
    public static Main getInstance(){
        return basicPlugin;
    }

    private ArrayList<String> operators = new ArrayList<>(Arrays.asList("+","-","*","/"));

    public ArrayList<String> getOperators(){
        return operators;
    }

    public int numOperands(){
        return 2;
    }

    public BigDecimal exec(String operator, Stack<BigDecimal> operands){
        BigDecimal lhs = operands.pop();
        BigDecimal rhs = operands.pop();
        DecimalFormat decFormat = new DecimalFormat();
        decFormat.setMaximumFractionDigits(5);
        decFormat.setMinimumFractionDigits(0);
        switch (operator) {
            case "+":
                return lhs.add(rhs);
            case "-":
                return lhs.subtract(rhs);
            case "*":
                return lhs.multiply(rhs);
            case "/":
                if(rhs.doubleValue() == 0.0) {
                    return null;
                }else{
                    return new BigDecimal(decFormat.format(lhs.divide(rhs, 8, RoundingMode.HALF_EVEN)));
                }
            default:
                return null;
        }
    }

}
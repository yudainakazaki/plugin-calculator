package backend;

import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

public class Calculator {

    //singleton design
    private static final Calculator calculator = new Calculator();
    private Calculator(){}
    public static Calculator getInstance(){
        return calculator;
    }

    private final Map<String, PluginLoader> operators = new LinkedHashMap<>();
    private int symbolCount = 0;

    public void loadOperators(List<String> ops, PluginLoader plugin) {
        for (String op : ops) {
            if(op.endsWith(" (")){
                operators.put(op.substring(0, op.length()-2), plugin);
            }
            else{
                operators.put(op, plugin);
            }
        }
    }

    public int getSymbolCount(){return symbolCount;}
    public void incSymbolCount(){symbolCount++;}
    public Map<String, PluginLoader> getOperators(){return operators;}

    private boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(strNum).matches();
    }

    public String execution(String[] rpn){
        String res;

        Stack<BigDecimal> stack = new Stack<>();
        Stack<BigDecimal> operands = new Stack<>();

        for(String s:rpn){
            System.out.println("curstring: "+ s);
            if(isNumeric(s)){
                System.out.println("numeric:"+s);
                stack.push(new BigDecimal(s));
            }else{
                //Why returnOps?
                System.out.println(s);
                PluginLoader plugin = operators.get(s);
                if(plugin == null) {
                    System.out.println("plugin is null");
                    break;
                }

                int opCount = (int) plugin.invokeMethod("numOperands");
                //if(stack.size() < opCount) return stack.size()+"<"+opCount+"Not supported";
                for(int i = 0; i < opCount; i++){
                    operands.push(stack.pop());
                }
                BigDecimal subRes = (BigDecimal) plugin.invokeMethod("exec", s, operands);
                if(subRes == null) {
                    System.out.println("???????");
                    break;}
                stack.push(subRes);
            }
        }

        if(stack.size() == 1) res = stack.peek().toString();
        else res = "Not supported";

        return res;
    }
}

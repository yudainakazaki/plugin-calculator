package backend;
import java.util.Stack;

import static java.lang.Character.isDigit;

public class RPNMethod {
    public static boolean isNumber(String s) {
        String trimmedS;
        if(s.charAt(0) == '-') {
            trimmedS = s.substring(1);
        }
        else{
            trimmedS = s;
        }

        if(trimmedS.isEmpty()) return false;

        for(char c:trimmedS.toCharArray()){
            if(!isDigit(c) && c != '.') {
                System.out.println("isNumber: " + c);
                return false;}
        }
        return true;
    }

    private static int getPrecedence(String ch) {
        if (ch.length() > 2 && ch.endsWith(" (")) return 4;
        else if (ch.equals("+") || ch.equals("-")) return 2;
        else if (ch.equals("*") || ch.equals("/")) return 3;
        else return 1;
    }

    private static boolean hasLeftAssociativity(String s) {
        return s.equals("+") || s.equals("-") || s.equals("/") || s.equals("*");
    }

    public static String expToRPN(String expression) {

        Stack<String> stack = new Stack<>();
        StringBuilder output = new StringBuilder();

        for (String el: expression.split(" ")) {
            System.out.println("el: "+el+" output: "+output.toString());
            if (isNumber(el)) {
                System.out.println(el + "is num");
                output.append(el).append(" ");
            }
            else if (el.equals("("))
                stack.push(el);
            else if (el.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("("))
                    output.append(stack.pop()).append(" ");
                stack.pop();
            }
            else if(stack.isEmpty() || stack.peek().equals("("))
                stack.push(el);
            else if(getPrecedence(el) > getPrecedence(stack.peek()))
                stack.push(el);
            else{
                while(!stack.isEmpty() && (getPrecedence(el) < getPrecedence(stack.peek()) || (getPrecedence(el) == getPrecedence(stack.peek())) && hasLeftAssociativity(el)))
                    output.append(stack.pop()).append(" ");
                stack.push(el);
            }

        }

        //if(curStr.toString() != "") output += curStr + " ";
        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" ");
            System.out.println("final: " + output);
        }

        return output.toString();
    }
}

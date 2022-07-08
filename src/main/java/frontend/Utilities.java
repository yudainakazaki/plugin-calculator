package frontend;

public class Utilities {
    static boolean endWithDot(String curVal){
        return curVal.substring(curVal.length() - 1) == ".";
    }

    static String sliceLastChar(String curVal){
        return curVal.substring(0, curVal.length() - 1);
    }
}


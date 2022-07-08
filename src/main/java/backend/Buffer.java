package backend;

import java.math.BigDecimal;
import java.util.Stack;

public class Buffer {

    //singleton design
    private static final Buffer buffer = new Buffer();
    private Buffer(){}
    public static Buffer getInstance(){
        return buffer;
    }

    private Stack<BigDecimal> operands = new Stack<>();

    public void clearOperand(){operands.clear();}

    //Redo/Undo

    ResultUndoRedo resUndoRedo = new ResultUndoRedo();
    ExpUndoRedo expUndoRedo = new ExpUndoRedo();

    public void store(String exp, BigDecimal num) {
        expUndoRedo.undoStack.push(exp);
        resUndoRedo.undoStack.push(num);
    }

    public boolean canUndo() {
        return !expUndoRedo.undoStack.empty() && !resUndoRedo.undoStack.empty();
    }
    public boolean canRedo() {
        return !expUndoRedo.redoStack.empty() && !resUndoRedo.redoStack.empty();
    }
    public boolean canPeekAns() {return !resUndoRedo.undoStack.empty();}

    public String undo(){
        String exp = expUndoRedo.undoStack.pop();
        BigDecimal num = resUndoRedo.undoStack.pop();
        expUndoRedo.redoStack.push(exp);
        resUndoRedo.redoStack.push(num);
        return exp+"= "+num;
    }

    public String redo(){
        String exp = expUndoRedo.redoStack.pop();
        BigDecimal num = resUndoRedo.redoStack.pop();
        expUndoRedo.undoStack.push(exp);
        resUndoRedo.undoStack.push(num);
        return exp+"="+num;
    }
    public String peekAns(){
        return resUndoRedo.undoStack.peek().toString();
    }
    public void clearRedoUndo(){
        expUndoRedo.clear();
        resUndoRedo.clear();
    }
}
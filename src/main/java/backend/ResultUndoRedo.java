package backend;

import java.math.BigDecimal;
import java.util.Stack;

public class ResultUndoRedo implements UndoRedo {

    public Stack<BigDecimal> undoStack = new Stack<>();
    public Stack<BigDecimal> redoStack = new Stack<>();
    public boolean canUndo() {return !undoStack.empty();}
    public boolean canRedo() {return !redoStack.empty();}

    @Override
    public BigDecimal undo(){
        BigDecimal num = undoStack.pop();
        redoStack.push(num);
        return num;
    }

    @Override
    public BigDecimal redo(){
        BigDecimal num = redoStack.pop();
        undoStack.push(num);
        return num;
    }

    public void clear(){
        undoStack.clear();
        redoStack.clear();
    }

}


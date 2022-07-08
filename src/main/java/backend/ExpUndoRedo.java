package backend;

import java.util.Stack;

public class ExpUndoRedo implements UndoRedo{

    public Stack<String> undoStack = new Stack<>();
    public Stack<String> redoStack = new Stack<>();
    public boolean canUndo() {return !undoStack.empty();}
    public boolean canRedo() {return !redoStack.empty();}

    @Override
    public String undo(){
        String exp = undoStack.pop();
        redoStack.push(exp);
        return exp;
    }

    @Override
    public String redo(){
        String exp = redoStack.pop();
        undoStack.push(exp);
        return exp;
    }

    public void clear(){
        undoStack.clear();
        redoStack.clear();
    }

}

package backend;

import java.math.BigDecimal;

interface UndoRedo {
    boolean canUndo();
    boolean canRedo();
    Object undo();
    Object redo();
    void clear();
}

package frontend;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import backend.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import static java.lang.Character.isDigit;
import javafx.stage.FileChooser;

public class FXMLController implements Initializable {

    private final Calculator calculator = Calculator.getInstance();
    private final Buffer buffer = Buffer.getInstance();
    private boolean clearScreen = false;

    @FXML private GridPane mainGrid;
    //Label on GUI showing the val of curOperand
    @FXML private Label resultLabel;
    @FXML private Label messageLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void onNumberClicked(MouseEvent event) {
        clearScreenAfterResult();
        int value = Integer.parseInt(((Button)event.getSource()).getId().replace("btn",""));
        System.out.println(value);
        //if curOperand is 0, replace it with value. Otherwise, append value at the end of curOperand
        String curText = resultLabel.getText();
        if(!curText.endsWith("-") &&!curText.isEmpty() && isDigit(curText.charAt(curText.length() - 2)) && curText.endsWith(" ")){
            curText = curText.substring(0, curText.length() - 1);
        }
        resultLabel.setText(curText + value + " ");
    }

    private void clearScreenAfterResult(){
        if(clearScreen){
            resultLabel.setText("");
            clearScreen = false;
        }
    }

    private boolean isLegalFloatingPoint(String curText){
        return !curText.isEmpty() && !curText.equals("-") && isDigit(curText.charAt(curText.length()-2)) ;
    }

    @FXML
    private void onSymbolClicked(MouseEvent event) throws InterruptedException {
        clearScreenAfterResult();
        String symbol = ((Button)event.getSource()).getId().replace("btn", "");
        String curText = resultLabel.getText();
        switch (symbol) {
            case "Negative":
                resultLabel.setText(curText+"-");
                break;
            case "Dot":
                if(!isLegalFloatingPoint(curText)){
                    return;
                }
                if(!curText.isEmpty() && isDigit(curText.charAt(curText.length() - 2)) && curText.endsWith(" ")){
                    curText = curText.substring(0, curText.length() - 1);
                }
                resultLabel.setText(curText+".");
                break;
            case "OpenBracket":
                resultLabel.setText(curText+"( " );
                break;
            case "CloseBracket":
                resultLabel.setText(curText+") ");
                break;
            case "Equal":
                if(resultLabel.getText().isEmpty()){
                    return;
                }

                String exp = resultLabel.getText();
                if(exp.contains("=")){
                    exp = exp.substring(exp.indexOf('=') + 1);
                }
                String RPN = RPNMethod.expToRPN(exp);
                System.out.println(RPN);
                for(String s: RPN.split(" ")){
                    System.out.println("check: " + s);
                }
                String res = calculator.execution(RPN.split(" "));
                System.out.println(res + ":D");
                resultLabel.setText(res);
                buffer.store(exp, new BigDecimal(res));
                clearScreen = true;
                break;
            case "Undo":
                System.out.println("Undo");
                if(buffer.canUndo()){
                    String val = buffer.undo();
                    resultLabel.setText(val);
                }
                clearScreen = true;
                break;
            case "Redo":
                System.out.println("Redo");
                if(buffer.canRedo()){
                    String val = buffer.redo();
                    resultLabel.setText(val);
                }
                clearScreen = true;
                break;
            case "Clear":
                System.out.println("C");
                resultLabel.setText("");
                break;
            case "AC":
                System.out.println("AC");
                AC();
                break;
            case "Back":
                if(resultLabel.getText().length() > 0){
                    int cutOff = 1;
                    if(curText.endsWith(" ")){
                        cutOff = 2;
                    }
                    String newText = resultLabel.getText().substring(0, curText.length() - cutOff);
                    resultLabel.setText(newText);
                }
                break;
            case "Ans":
                if(buffer.canPeekAns()){
                    resultLabel.setText(resultLabel.getText() + buffer.peekAns() + " ");
                }
                break;
            case "Load": //test
                FileChooser fCh = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("jar file", "*.jar");
                fCh.getExtensionFilters().add(extFilter);
                File pluginPath = fCh.showOpenDialog(null);

                if(pluginPath == null){
                    System.out.println("Path is empty");
                    return;
                }

                PluginLoader plugin = new PluginLoader(pluginPath);
                ArrayList<String> newOperators = (ArrayList<String>) plugin.invokeMethod("getOperators");
                calculator.loadOperators(newOperators,plugin);

                int colCount = mainGrid.getColumnCount() - 1;
                for(var item:newOperators){
                    Button button = new Button(item);
                    button.getStyleClass().add("btn");
                    button.getStyleClass().add("btnOp");
                    button.setId("btn"+calculator.getSymbolCount());
                    int rowIndex = calculator.getSymbolCount() % 5 + 1;
                    if (rowIndex == 1){
                        colCount++;
                    }
                    button.setOnMouseClicked(onOperatorClicked);
                    button.setPrefWidth(180);
                    mainGrid.add(button, colCount, rowIndex);
                    calculator.incSymbolCount();
                    AC();
                }
                break;
            default:
                System.out.println(symbol);
        }
    }

    @FXML
    private final EventHandler<MouseEvent> onOperatorClicked = event -> {
        clearScreenAfterResult();
        String operatorName = ((Button)event.getSource()).getText();
        System.out.println(operatorName);
        String curText = resultLabel.getText();
        if(!curText.isEmpty() && isDigit(curText.charAt(curText.length()-1))){
            curText += " ";
        }
        resultLabel.setText(curText + operatorName + " ");
    };

    private void AC(){
        buffer.clearOperand();
        buffer.clearRedoUndo();
        resultLabel.setText("");
        messageLabel.setText("");
    }

}
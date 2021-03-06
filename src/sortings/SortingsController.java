package sortings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import static javafx.application.Platform.runLater;
import javafx.fxml.FXML;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author cjcal
 */
public class SortingsController implements Initializable {
    @FXML 
    private ComboBox AlgorithmsList;
    
    @FXML
    private HBox VisualSort;
    
    @FXML
    private Slider SizeSelector;
    
    @FXML 
    private TextField SizeField;  
    
    @FXML
    private Button SortButton;
    
    @FXML
    private Button ResetButton;
    
    @FXML
    private Button ExitButton;
    
    private int arraySize;
    private int[] arrayOfInts;
    private SortingStrategy sortMethod;
    SortingsModel model;

        
    public void alterSizeBySlider() {
         Double size = SizeSelector.getValue();
        arraySize = size.intValue();
        SizeField.setText(Integer.toString(arraySize));
        resetView();
    }    
    
    public void alterSizeByText() {
            
        if(SizeField.getText()!=""){
            
            int typedVal = Integer.parseInt(SizeField.getText());
            
            if(typedVal>100) {
                typedVal = 100;
                SizeField.setText("100");
            }            
            else if(typedVal<0) {
                typedVal = 0;
                SizeField.setText("0");

            }

            arraySize = typedVal;
            SizeSelector.setValue(arraySize);
            resetView();

        }
        
        else SizeSelector.setValue(0);
    }
    
    public void showArray() {
        
        while (VisualSort.getChildren().isEmpty()==false) VisualSort.getChildren().remove(0);
        
        for(int i = 0; i < arraySize; i++){
            Rectangle r = new Rectangle();
            r.setHeight(350*arrayOfInts[i]/arraySize+1);
            r.setWidth(((500-arraySize))/arraySize);
            r.setFill(Color.BLUEVIOLET);
            VisualSort.getChildren().add(r);
        }
    }
    
    public void setSortType() {
        String choice = AlgorithmsList.getValue().toString();
        
        if (choice == "Selection sort")     sortMethod = new SelectionSort();
        else if (choice == "Merge sort")    sortMethod = new MergeSort();
                
        SortButton.setDisable(false);
    }
    
    public void sort() {
    Runnable sort = new SortTask(); 
    Thread sortThread = new Thread (sort);
    sortThread.start();
    }
    
    
    
    public void close() {
        Stage stage = (Stage) VisualSort.getScene().getWindow();
        stage.close();
    }
    
    public class SortTask implements Runnable{
        
        @Override
        public void run() {
            sortMethod.sort(arrayOfInts);
        }
    }
    
    public class ShowTask implements Runnable{
                
        @Override
        public void run() {
            while (true){
                
                Platform.runLater(new Runnable(){
                    public void run(){
                        showArray();
                    }
                });
                
                try{
                    Thread.sleep (100);
                } catch (InterruptedException ex) {}
        
            }
        }
    }
    
    public void resetView(){
        model.reset(arraySize);
        arrayOfInts = model.getUnsortedList();
        if (ResetButton.isDisabled()==true) ResetButton.setDisable(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new SortingsModel();
        AlgorithmsList.getItems().addAll(
            "Selection sort",
            "Merge sort"
        );
        
        Runnable show = new ShowTask();
        Thread showThread = new Thread (show);
        showThread.start();
        
    }    
}

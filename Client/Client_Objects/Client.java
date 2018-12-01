package Client.Client_Objects;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Observable;
import java.util.Observer;

public class Client implements Observer{

    String[] fileName;
    ObservableList<String> observableList;

    public Client(ObservableList<String> list){
        observableList = list;
    }

    @Override
    public void update(Observable o, Object arg) {

        Platform.runLater( () -> {

                    this.setFileName((String[]) arg);

                    observableList.clear();

                    System.out.println("Update: ");

                    for (String each : fileName) {
                        System.out.println(each);
                        observableList.add(each);
                    }
                    System.out.println();
                }

        );
    }

    private void setFileName(String[] val){ this.fileName = val; }

    public String[] getFileName(){ return this.fileName; }

}

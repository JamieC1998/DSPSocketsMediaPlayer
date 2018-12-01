package Client;

import Client.Client_Objects.Client;
import Client.Client_Objects.ClientThread;
import Client.Folder.FileObservable;
import Client.Folder.FileWatcherInterface;
import Server.Folder.FileWatcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class HomeController{

    @FXML
    ListView<String> lvLocal;

    @FXML
    ListView<String> lvServer;

    @FXML
    Button play;

    @FXML 
    Button download;

    public static HomeController homeController = null;

    private String localDirectory = "src\\Client\\Client_Folder_1";

    ObservableList<String> observableListLocal;

    ObservableList<String> observableListServer;

    @FXML
    public void initialize(){

        homeController = this;

        FileObservable fileObservableLocal = new FileObservable(localDirectory);

        observableListLocal = FXCollections.observableArrayList();

        Client clientObservableLocal = new Client(observableListLocal);

        fileObservableLocal.addObserver(clientObservableLocal);

        new Thread(fileObservableLocal).start();

        observableListServer = FXCollections.observableArrayList();

        lvLocal.setItems(observableListLocal);

        lvServer.setItems(observableListServer);

        populateListView();

    }

    private void populateListView(){
        for(String each : ClientThread.serverContents)
            lvServer.getItems().add(each);
    }

    public ObservableList<String> gViewServer(){
        return observableListServer;
    }

    @FXML
    public void Download(ActionEvent e){
        ClientThread.fileToReturn = lvServer.getSelectionModel().getSelectedItem();
        ClientThread.switchVal = 2;

    }


}
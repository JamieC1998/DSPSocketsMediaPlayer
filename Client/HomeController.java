package Client;

import java.io.File;
import java.io.IOException;

import Client.Client_Objects.Client;
import Client.Client_Objects.ClientThread;
import Client.Folder.FileObservable;
import Client.View.MediaViewController;
import Server.Folder.FileWatcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

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
        String s = lvServer.getSelectionModel().getSelectedItem();
        Runnable r = () -> {
            try{ ClientThread.clientThread.downloadFile(s); }
            catch(IOException d) { d.printStackTrace(); }
            catch(ClassNotFoundException d) { d.printStackTrace(); }
        };

        new Thread(r).start();
    }

    @FXML
    public void PlayFile(ActionEvent e){
        FileWatcher fileWatcherLocal = new FileWatcher(localDirectory);

        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("View\\MediaView.fxml"));

        Parent root = null;

        try {
            root = (Parent) loader.load();

        } catch (IOException ex) {
            System.out.println("OOPS");
            ex.printStackTrace();
        }*/

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("View\\MediaView.fxml"));

        Parent root = null;

        try { root = loader.load(); }
        catch (IOException d) { d.printStackTrace(); }

        MediaViewController returnObject = loader.getController();

        String item = lvLocal.getSelectionModel().getSelectedItem();

        File file = (((FileWatcher) fileWatcherLocal).ReturnFileReq(item));

        System.out.println(file.getName());

        if(returnObject == null){
            System.out.println("NULL");
        }

        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        returnObject.setFile(file);


    }


}
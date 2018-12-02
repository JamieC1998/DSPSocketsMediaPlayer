package Client;

import Client.Folder.FileWatcher;
import Client.Folder.FileWatcherInterface;
import Client.Client_Objects.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class main extends Application{

    protected static ClientThread threadClient;

    protected static FileWatcher fileWatch;
    public static void main(String[] args){

        fileWatch = new FileWatcher("src\\Client\\Client_Folder_1");

        String serverName = "localhost";

        int port = 11001;

        threadClient = new ClientThread(port, serverName, fileWatch, args);

        Thread t = new Thread(threadClient);

        t.start();

        threadClient.t = t;

        launch(args);
        
    }

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Home.fxml"));

        Parent root = null;

        try { root = loader.load(); }
        catch (IOException e) { e.printStackTrace(); }

        primaryStage.setTitle("Media Player");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}


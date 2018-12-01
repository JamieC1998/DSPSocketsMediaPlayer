package Client.Client_Objects;

import Client.HomeController;
import Client.Folder.FileWatcher;
import Client.Folder.FileWatcherInterface;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;



public class ClientThread implements Runnable{

    private int port = 0;

    private String hostName = null;

    private static int clientCounter = 0;
    
    private int clientNumber = 0;

    private File[] listOfFiles = null;

    private FileWatcher fileWatch = null;

    private File file = null;

    private Socket client;

    private OutputStream os;

    private ObjectOutputStream out;

    private ObjectInputStream inputStream;

    private boolean checkVal = false;

    public static int switchVal;

    public static String[] serverContents;
    
    public static String fileToReturn = null;

    public ClientThread(int port, String hostName, FileWatcher fileWatch, String[] args){

        this.port = port;

        this.hostName = hostName;

        this.fileWatch = fileWatch;

        clientCounter++;

        clientNumber = clientCounter;

    }

    @Override
    public void run() {

        try {

            System.out.println("Starting Client " + clientNumber);

            System.out.println("Connecting to " + hostName + " on Port " + port);

            client = new Socket(hostName, port);

            System.out.println("Connected to server");

            os = client.getOutputStream();

            out = new ObjectOutputStream(os);

            inputStream = new ObjectInputStream(client.getInputStream());

            while (true) {

                checkServer(switchVal);
                switchVal = -1;
                
            }

            //out.close();

            //client.close();

        }
        catch (UnknownHostException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

    }

    private void checkServer(int switchVal) throws ClassNotFoundException, IOException{
        
        out.writeInt(switchVal);

        out.flush();

        boolean result = inputStream.readBoolean();

        if(result){
            switchVal = 3;
        }

        switch(switchVal){

        case 1:
            receiveFile();
            break;

        case 2:
            receiveOneFile(fileToReturn);
            break;
        case 3:
            for(String each : receiveListOfFileNames()) 
                System.out.println(each);
            break;

        default:
            //System.out.println("No Input");
            break;

        }

    }

    private void receiveFile() throws IOException, ClassNotFoundException{
        String fileName = (String) inputStream.readObject();
        
        byte[] content = (byte[]) inputStream.readObject();

        File directory = new File("C:\\Users\\Admin\\Documents\\College Documents\\Generic Eclipse Workspace\\SocketsExample\\src\\Client\\Client_Folder_" + clientNumber);
                
        if(!directory.exists())
            new File("C:\\Users\\Admin\\Documents\\College Documents\\Generic Eclipse Workspace\\SocketsExample\\src\\Client\\Client_Folder_" + clientNumber).mkdirs();

        File newFile = new File("C:\\Users\\Admin\\Documents\\College Documents\\Generic Eclipse Workspace\\SocketsExample\\src\\Client\\Client_Folder_" + clientNumber + "\\" + fileName);

        Files.write(newFile.toPath(), content);
    }

    private void receiveOneFile(String name) throws IOException, ClassNotFoundException{
        System.out.println("Sent name: " + name);
        out.writeObject(name);
        receiveFile();

    }

    private String[] receiveListOfFileNames() throws IOException, ClassNotFoundException{
        String[] fileNames = (String[]) inputStream.readObject();
        serverContents = fileNames;
        System.out.println("Case 3");

        if(HomeController.homeController != null){
            Platform.runLater(new Runnable(){

                @Override
                public void run(){
                    ObservableList<String> temp = HomeController.homeController.gViewServer();
                    temp.clear();

                    for(String each : fileNames){
                        temp.add(each);
                    }
                }
            });
        }
        return fileNames;
    }

}
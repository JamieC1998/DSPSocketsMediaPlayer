package Server;

import Server.Folder.FileObservable;
import Server.Folder.FileWatcher;
import Server.Folder.FileWatcherInterface;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


public class ServerMain {
	
    public static void main(String[] args){

        int port = 11001;

        Client[] threadPool = new Client[10];
        FileObservable fileObservables = new FileObservable("src\\Server\\FileFolder");
        Thread t = null;
        
        FileWatcherInterface fileWatch = new FileWatcher("C:\\Users\\Admin\\Documents\\College Documents\\Generic Eclipse Workspace\\SocketsExample\\src\\Server\\FileFolder");

        fileWatch.PrintListFiles();

        try {

            int threadNumber = 0;

        	System.out.println("Starting server");
        	
            ServerSocket servSocket = new ServerSocket(port);

            while(threadNumber < threadPool.length){
                if(threadPool[threadNumber] == null) {
                    threadPool[threadNumber] = new Client(servSocket.accept(), fileWatch);
                    new Thread(threadPool[threadNumber]).start();
                    fileObservables.addObserver(threadPool[threadNumber]);

                    if(t == null){
                        t = new Thread(fileObservables);
                        t.start();
                    }
                
                }
                else
                    threadNumber++;
            }
            servSocket.close();

        }

        catch (IOException e) { e.printStackTrace(); }


    }
}

class Client implements Runnable, Observer{

    private Socket socket = null;

    private FileWatcherInterface fileWatch = null;

    private File[] listOfFiles = null;

    private ObjectInputStream inputStream = null;

    private OutputStream os = null;

    private ObjectOutputStream out = null;

    private String[] listOfNames = null;

    private boolean fileChanged = false;

    public Client(Socket socket, FileWatcherInterface fileWatch){
       this.socket = socket;
       this.fileWatch = fileWatch;

    }

    @Override
    public void update(Observable o, Object obj){
        listOfNames = (String[]) obj;
        fileChanged = true;
        System.out.println("Directory Changed");

    }

    @Override
    public void run() {

        try {
            System.out.println("Connection accepted");
            
            inputStream = new ObjectInputStream(socket.getInputStream());

            int switchVal = 0;

            os = socket.getOutputStream();

            out = new ObjectOutputStream(os);
            
            while(true) {

                switchVal = inputStream.readInt();

                if(fileChanged == true){
                    out.writeBoolean(true);
                    switchVal = 3;
                    fileChanged = false;
                }
                else
                    out.writeBoolean(false);

                out.flush();

                
                switch(switchVal){
                    case 1:
                        sendAllFiles();
                        break;
                    case 2:
                        System.err.println("Sending One File");
                        sendOneFile();
                        break;

                    case 3:
                        sendListOfFileNames();
                        break;
                    
                    default:
                        //System.out.println("No input");
                        break;
                    }
                }

            }

            catch (IOException e) { e.printStackTrace(); }
            catch (ClassNotFoundException e) { e.printStackTrace(); }

    }

    private void sendAllFiles() throws IOException{
        File[] temp = fileWatch.ReturnListOfFiles();
        
        if(!Arrays.equals(temp, listOfFiles)){
            listOfFiles = temp;
            
            for(File each : listOfFiles){        
                sendFile(each);
            }
        }
    }

    private void sendFile(File each) throws IOException{
        System.out.println("Send File");
        out.writeObject(each.getName());
        
        out.flush();

        byte[] buffer = Files.readAllBytes(each.toPath());
                        
        out.writeObject(buffer);
        
        out.flush();
    }

    private void sendOneFile() throws IOException, ClassNotFoundException{
        System.out.println("Waiting for Ojbect String name");
        String fileName = (String) inputStream.readObject();
        System.out.println("Received String name");


        File fileToGet = fileWatch.ReturnFileReq(fileName);

        sendFile(fileToGet);


    }

    private void sendListOfFileNames() throws IOException{
        System.out.println("Sending list of file names");
        out.writeObject(fileWatch.ReturnFileNames());
    }

}
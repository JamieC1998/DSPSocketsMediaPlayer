package Client.Folder;


import Server.Folder.FileWatcherInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

public class FileWatcher implements FileWatcherInterface{

    private String directoryAddress = null;

    private File folder = null;

    public FileWatcher(String directoryName){

        directoryAddress = directoryName;

        folder = new File(directoryAddress);

        if(folder.isDirectory() == false){
            System.err.println("Address does not exist, creating new folder");
            folder.mkdir();

        }



    }

    public void PrintListFiles(){
        if(folder == null)
            return;

        File[] listOfFiles = folder.listFiles();

        for(File fileEntry : listOfFiles){
            System.out.println("File: \t" + fileEntry.getName());
            System.out.println("Last modified: \t" + new SimpleDateFormat().format(fileEntry.lastModified()));
        }
    }

    public String[] ReturnFileNames(){
        if(folder == null){
            System.err.println("Folder not found");
            return null;
        }

        String[] returnList = new String[folder.listFiles().length];

        for(int i = 0; i < returnList.length; i++){
            returnList[i] = folder.listFiles()[i].getName();
        }

        return returnList;
    }

    public File[] ReturnListOfFiles(){
        if(folder == null){
            System.err.println("Folder not found");
            return null;
        }

        return folder.listFiles();
    }

    public File ReturnFileReq(String name){
        for(File each: folder.listFiles()){
            if(each.getName().equals(name))
                return each;
        }

        return null;
    }

    public void AddFile(File src){
        File dest = new File(directoryAddress + "\\" + src.getName());

        try { Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING); }

        catch (IOException e) { e.printStackTrace(); }


    }

}

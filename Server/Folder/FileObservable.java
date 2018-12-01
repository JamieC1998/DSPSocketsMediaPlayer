package Server.Folder;

import java.util.Arrays;
import java.util.Observable;

public class FileObservable extends Observable implements Runnable{

    String[] fileName;

    String directory;

    public FileObservable(String name){
        this.directory = name;

    }

    @Override
    public void run() {

        FileWatcherInterface fW = new FileWatcher(directory);

        while(true) {
            try {

                String[] temp = fW.ReturnFileNames();

                if (!Arrays.equals(this.fileName, temp)) {
                    setChanged();
                    notifyObservers(temp);
                    fileName = temp;

                }

                Thread.sleep(1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

package Client.View;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.MalformedURLException;

public class MediaViewController{

    @FXML
    public Button play;

    @FXML
    public MediaView mediaView;

    public Media media;

    private File file;

    public void setFile(File file){

        this.file = file;

        Media media = null;

        try {
            media = new Media(file.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.play();

    }


    @FXML
    private void initialize(){

    }


    @FXML
    public void PlayHandler(){

        if(play.getText().equals("Play")){
            mediaView.getMediaPlayer().play();
            play.setText("Pause");
        }

        else{
            mediaView.getMediaPlayer().pause();
            play.setText("Play");
        }
    }
}

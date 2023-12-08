package brickGame;

import java.io.File;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Music class which is responsible for
 * playing the music in the game.
 */
public class Music {
    /**
     * AudioClip object to play the game's music
     */
    private AudioClip audioClip = null;

    /**
     * Boolean variable to indicate whether music
     * playing music or not.
     */
    private boolean isPlaying = false;

    /**
     * Plays the background music of the game.
     */
    public void playMusic(boolean play) {
        if (play) {
            if (audioClip == null) {
                String musicFile = "src/main/java/brickGame/music/Original Tetris theme (Tetris Soundtrack).mp3";
                File music = new File(musicFile);
                audioClip = new AudioClip(music.toURI().toString());
                audioClip.setCycleCount(AudioClip.INDEFINITE);
            }

            if (!isPlaying) {
                audioClip.play();
                isPlaying = true;
            }
        } else {
            stopMusic();
        }
    }

    /**
     * Stops the background music.
     */
    private void stopMusic() {
        if (audioClip != null) {
            audioClip.stop();
            isPlaying = false;
        }
    }

    /**
     * Returns a boolean to indicate
     * whether music is playing or not.
     *
     * @return true if music is playing false if not
     */
    private boolean getIsMusicPlaying(){
        return isPlaying;
    }


//    /**
//     * Creates a music button on the screen that
//     * allows the user to turn the music on/off.
//     */
//    public void musicButton(ToggleButton musicButton) {
//        musicButton.setOnAction(event -> {
//            if (musicButton.isSelected()) {
//                stopMusic();
//            } else {
//                playMusic();
//            }
//        });
//    }
}
package brickGame;

import java.io.File;
import javafx.scene.media.AudioClip;


/**
 * Music class which is responsible for
 * playing the music in the game. This is a new class.
 */
public class Music {
    /**
     * AudioClip object to play the game's music
     */
    private AudioClip audioClip = null;

    /**
     * Default constructor for the Music class.
     * This constructor is used to create an instance of the Music class.
     */
    public Music() {
    }

    /**
     * Boolean variable to indicate whether music
     * playing music or not.
     */
    private boolean isPlaying = false;

    /**
     * Plays the background music of the game.
     *
     * @param play      If true, starts playing the music; if false, stops the music.
     * @param musicType The type of music to be played. Use 1 for the original Tetris theme and any other number for the sound effect.
     * @see <a href="https://github.com/kooitt/CourseworkGame/tree/master/src/main/java/brickGame">brickGame package on GitHub</a>
     */
    public void playMusic(boolean play, int musicType) {
        String musicFile;
        if (play) {
            if (audioClip == null) {
                if (musicType == 1) {
                    musicFile = "src/main/java/brickGame/music/Original Tetris theme (Tetris Soundtrack).mp3";
                } else {
                    musicFile = "src/main/java/brickGame/music/tada-fanfare-a-6313.mp3";
                }
                File music = new File(musicFile);
                audioClip = new AudioClip(music.toURI().toString());

                if (musicType == 1) {
                    audioClip.setCycleCount(AudioClip.INDEFINITE);
                }
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
}
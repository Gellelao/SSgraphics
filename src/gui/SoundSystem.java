package src.gui;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Class used to play sounds. Currently only has a beep sound but it would be simple to add more.
 * This class also has an "error" system, where a number is increased the more times an error is
 * made, and the beep become longer at a point.
 * This is used to warn the user about attempting to spawn a tile when they cannot
 * 
 * @author Deacon
 *
 */
public class SoundSystem {
	private int errorNumber;
	
	public SoundSystem() {
		errorNumber = 0;
	}
	
	public void increaseError() {
		playBeep();
		errorNumber++;
	}
	
	public void resetError() {
		errorNumber = 0;
	}
	
	public void playBeep() {
		File sound = new File("beep.wav");
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			if(errorNumber == 0) clip.loop(0);
			else clip.loop(3);
		}catch(Exception e) { System.out.println(e);}
	}
	
	public int getError() {return errorNumber;}
}

package src.gui;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundSystem {
	private int errorNumber;
	
	public SoundSystem() {
		errorNumber = 0;
	}
	
	public void increaseError() {
		errorNumber++;
		playBeep();
	}
	
	public int getError() {return errorNumber;}
	
	public void resetError() {
		errorNumber = 0;
	}
	
	private void playBeep() {
		File sound = new File("beep.wav");
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			if(errorNumber == 1)
				clip.loop(1);
			else clip.loop(4);
		}catch(Exception e) {}
	}
}

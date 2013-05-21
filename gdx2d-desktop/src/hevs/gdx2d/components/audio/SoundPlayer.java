package hevs.gdx2d.components.audio;

import hevs.gdx2d.lib.utils.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * A class to play *short* samples within LibGDX. The music sample
 * must be short because it is completely loaded into memory.
 * 
 * @author Nils Chatton (chn)
 * @version 1.0
 */
public class SoundPlayer {
	protected Sound s;
	protected float volume = 1.0f;
		
	public SoundPlayer(String file){
		s = Gdx.audio.newSound(Gdx.files.internal(file));		
		Utils.callCheck("hevs.gdx2d.lib.Game2D", "create");
	}
	
	/**
	 * Changes volume of the song played
	 * @param v Should be between 0 and 1
	 */
	public void setVolume(float v){
		if(v>1.0f||v<0.0f){
			new UnsupportedOperationException("Volume must be set in a range between 0 and 1");
		}
		else{
			volume = v;
		}
	}
	
	/**
	 * Starts playing the song
	 */
	public void play(){
		s.play(volume);
	}
	
	/**
	 * Stops playing the song
	 */
	public void stop(){
		s.stop();		
	}
	
	/**
	 * Plays the song in loop
	 */
	public void loop(){
		s.loop();
	}
}

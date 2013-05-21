package hevs.gdx2d.components.audio;

import hevs.gdx2d.lib.utils.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * A class to stream music without loading it within LibGDX.
 * 
 * @author Pierre-Andre Mudry / mui
 * @version 1.0
 */
public class MusicPlayer {
	protected Music s;
	protected float volume = 1.0f;
		
	public MusicPlayer(String file){
		s = Gdx.audio.newMusic(Gdx.files.internal(file));		
		Utils.callCheck("hevs.gdx2d.lib.Game2D", "create");
	}
	
	public boolean isPlaying(){
		return s.isPlaying();
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
		s.play();
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
		s.play();
		s.setLooping(true);
	}
}

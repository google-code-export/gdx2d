package hevs.gdx2d.lib;

import hevs.gdx2d.lib.interfaces.GameInterface;
import hevs.gdx2d.lib.interfaces.KeyboardInterface;
import hevs.gdx2d.lib.interfaces.TouchInterface;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

/**
 * The base class that should be sub-classed by all
 * gdx2d applications. To get the functionalities you simply have
 * to overload the methods you need.
 * 
 * @author Pierre-André Mudry (mui)
 * @version 1.01
 */
public abstract class PortableApplication implements TouchInterface,
		KeyboardInterface, GameInterface {

	protected boolean onAndroid;
	private Game2D theGame;
	
	
	/**
	 * Changes the title of the window (Desktop only)
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		if (Gdx.app.getType() == ApplicationType.Android)
			Gdx.app.log("Warning", "Title can not be set on Android");

		Gdx.graphics.setTitle(title);
	}

	/**
	 * Rendering surface information
	 * 
	 * @return The height of the display surface (window)
	 */
	public int getWindowHeight() {
		return Gdx.graphics.getHeight();
	}

	/**
	 * Rendering surface information
	 * 
	 * @return The width of the display surface (window)
	 */	
	public int getWindowWidth() {
		return Gdx.graphics.getWidth();
	}

	@Override
	public void onGameLogicUpdate() {				
	}
	
	/**
	 * Invoked when the pointer is depressed (once)
	 */
	@Override
	public void onClick(int x, int y, int button) {
	}

	/**
	 * Invoked when the pointer (mouse or touch) is moved and down
	 */
	@Override
	public void onDrag(int x, int y) {
	}

	/**
	 * Invoked when the pointer (mouse or touch) is released
	 */
	@Override
	public void onRelease(int x, int y, int button) {
	}

	/**
	 * Invoked when a key is typed. Use Input.Keys.xy to read the keycode
	 */
	@Override
	public void onKeyDown(int keycode) {
		if(keycode == Input.Keys.MENU){
			Gdx.input.vibrate(300);			
		}
	}

	/**
	 * Invoked when a key is released. Use Input.Keys.xy to read the keycode
	 */
	@Override
	public void onKeyUp(int keycode) {
	}			
			
	/**
	 * Invoked for a zoom gesture on Android
	 * @param initialDistance
	 * @param distance
	 * @return
	 */
	public void onZoom(float initialDistance, float distance) {						
	}
	
	/**
	 * Invoked for a tap gesture on Android (see {@link GestureListener})
	 * @param x
	 * @param y
	 * @param count
	 * @param button
	 * @return
	 */
	public void onTap(float x, float y, int count, int button) {				
	}
		
	/**
 	 * Invoked for a ping gesture on Android (see {@link GestureListener})
	 * @param initialPointer1
	 * @param initialPointer2
	 * @param pointer1
	 * @param pointer2
	 * @return
	 */
	public void onPinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {				
	}
		
	/**
	 * Invoked for a pan gesture on Android (see {@link GestureListener})
	 * @param x
	 * @param y
	 * @param deltaX
	 * @param deltaY
	 * @return
	 */
	public void onPan(float x, float y, float deltaX, float deltaY) {	
		
	}
		
	/**
	 * Invoked for a long press on Android (see {@link GestureListener})
	 * @param x
	 * @param y
	 * @return
	 */
	public void onLongPress(float x, float y) {		
	}
	
	/**
	 * Invoked on a fling gesture on Android (see {@link GestureListener})
	 * @param velocityX
	 * @param velocityY
	 * @param button
	 * @return
	 */
	public void onFling(float velocityX, float velocityY, int button) {		
	}
	
	/**
	 * Invoked when the application is terminated (on Android and desktop)
	 */
	@Override
	public void onDispose() {
	}
	
	/**
	 * Invoked when the application is paused (on Android)
	 */
	@Override
	public void onPause() {						
	}
	
	/**
	 * Invoked when the application is restarted (on Android)
	 */
	@Override
	public void onResume() {			
	}

	/**
	 * Creates an application using GDX2D
	 * @param onAndroid true if running on Android
	 * @param width The width of the screen (if running desktop)
	 * @param height The height of the screen (if running desktop)
	 */
	public PortableApplication(boolean onAndroid, int width, int height) {
		this.onAndroid = onAndroid;
				
		if (!onAndroid)			
		{
			// TODO refactor this more nicely
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.resizable = false;
			config.useGL20 = true;
			config.height = height;
			config.width = width;
			config.fullscreen = false;
			config.title = "gdx2d application";
			config.vSyncEnabled = true; // Ignored under Linux						
			config.foregroundFPS = 60;
			config.backgroundFPS = 60;
			config.samples = 2; // Multi-sampling enables anti-alias for lines
			config.addIcon("data/icon16.png", FileType.Internal);
			config.addIcon("data/icon32.png", FileType.Internal);
			config.addIcon("data/icon64.png", FileType.Internal); // FIXME: icon not showing properly on Ubuntu 10.04			
			theGame = new Game2D(this);			
			LwjglApplication app = new LwjglApplication(theGame, config);
		}			
	}

	/**
	 * Creates an application using gdx2d
	 * @param onAndroid True if running on Android
	 */
	public PortableApplication(boolean onAndroid) {
		this(onAndroid, 500, 500);
	}
}
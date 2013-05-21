package hevs.gdx2d.lib;

import hevs.gdx2d.components.physics.utils.PhysicsWorld;
import hevs.gdx2d.lib.interfaces.AndroidResolver;

import java.security.InvalidParameterException;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

/**
 * A wrapper for the {@link ApplicationListener} class provided for the INF1
 * class.
 * 
 * @author Pierre-Andre Mudry (mui)
 * @author Nils Chatton
 * @date 2013
 * @version 1.1
 */
public class Game2D implements ApplicationListener {
	protected PortableApplication app;
	protected GdxGraphics g;

	public OrthographicCamera camera;
	protected ShapeRenderer shapeRenderer;
	protected int angle;
	protected SpriteBatch batch;
	
	// Use a second thread for game logic updates
	Timer t;
	protected static final int LOGIC_UPDATES_PER_SECOND = 1000;
	protected static int logicRefreshFps = LOGIC_UPDATES_PER_SECOND;
	
	// For triggered Android actions or intents
	AndroidResolver resolver;
	
	/**
	 * Changes the rate at which the updates are called for the logic part of
	 * the game. Default is 1000 updates per second.
	 * 
	 * @param fps
	 */
	public void setLogicRefreshRate(int fps) {
		try {
			if (fps <= 0)
				throw new InvalidParameterException(
						"Invalid rate for update, should be > 0");
			logicRefreshFps = fps;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Default constructor
	 * 
	 * @param app
	 */
	public Game2D(PortableApplication app) {
		this.app = app;		
	}
	
	// FIXME : this does not work yet
	/**
	 * Special contructor for Android, using interface
	 * for triggering actions from this side
	 * @param app
	 */
	public Game2D(PortableApplication app, AndroidResolver resolver) {
		this.app = app;		
		this.resolver = resolver;
	}
	
	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();

		// Log level for the application
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.log("[GDX2Dlib]", "version " + Version.version);
				
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		
		g = new GdxGraphics(shapeRenderer, batch, camera);

		//batch.setProjectionMatrix(camera.combined);
		//shapeRenderer.setProjectionMatrix(camera.combined);
		
		// Let's have multiple input processors
		InputMultiplexer multiplexer = new InputMultiplexer();					
		multiplexer.addProcessor(new GestureDetector(new GestureListener() {
			
			@Override
			public boolean zoom(float initialDistance, float distance) {		
				app.onZoom(initialDistance, distance);
				return false;
			}
			
			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				return false;
			}
			
			@Override
			public boolean tap(float x, float y, int count, int button) {
				app.onTap(x, y, count, button);
				return false;
			}
			
			@Override
			public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
				app.onPinch(initialPointer1, initialPointer2, pointer1, pointer2);
				return false;
			}
			
			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				app.onPan(x, y, deltaX, deltaY);
				return false;
			}
			
			@Override
			public boolean longPress(float x, float y) {
				app.onLongPress(x, y);				
				return false;
			}
			
			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				app.onFling(velocityX, velocityY, button);
				return false;
			}
		}));
		
		multiplexer.addProcessor(new InputProcessor() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				app.onRelease(screenX, screenY, button);
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				app.onDrag(screenX, Gdx.graphics.getHeight() - screenY);
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,	int button) {
				app.onClick(screenX, Gdx.graphics.getHeight() - screenY, button);
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {				
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				app.onKeyUp(keycode);
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean keyDown(int keycode) {
				// Trigger about box when pressing the menu button on Android
				if(keycode == Input.Keys.MENU){
				//	resolver.showAboutBox();
				}
				app.onKeyDown(keycode);
				return false;
			}
		});

		Gdx.input.setInputProcessor(multiplexer);
		
		// A fixed rate timer that schedules the application game updates
		t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				app.onGameLogicUpdate();
			}
		}, 0, 1000 / logicRefreshFps);
	
		// Initialize app
		app.onInit();
	}

	/**
	 * Mostly delegates rendering to the {@link #app} class
	 */
	@Override
	public void render() {
		
		app.onGraphicRender(g);
	}

	/**
	 * Called when the screen has been resized
	 */
	@Override
	public void resize(int width, int height) {		
	}

	/**
	 * Handles application life-cycle on Android and others
	 */
	@Override
	public void pause() {
		t.cancel();
		app.onPause();
	}

	/**
	 * Handles application life-cycle on Android and others
	 */
	@Override
	public void resume() {
		t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				app.onGameLogicUpdate();
			}
		}, 0, 1000 / logicRefreshFps);
		app.onResume();
	}

	/**
	 * Handles application life-cycle on Android and others
	 */
	@Override
	public void dispose() {		
		app.onDispose();
		batch.dispose();		
		shapeRenderer.dispose();
		PhysicsWorld.dispose();
		Gdx.app.debug("[GDX2DLib]", "Game2D disposing");
	}

}

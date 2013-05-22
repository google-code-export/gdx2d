package hevs.gdx2d.lib.utils;

import com.badlogic.gdx.Gdx;

/**
 * Simple logging facility using underlying libgdx.
 * @author Pierre-André Mudry (mui)
 * @version 1.0
 */
public class Logger {
	
	public static final int NONE = Gdx.app.LOG_NONE;
	public static final int ERROR = Gdx.app.LOG_ERROR;
	public static final int INFO = Gdx.app.LOG_INFO;
	public static final int DEBUG = Gdx.app.LOG_DEBUG;
	
	public static void log(String msg){
		Gdx.app.log("[GDX2DLib]", msg);
	}

	public static void setLogLevel(int logLevel){
		Gdx.app.setLogLevel(logLevel);
	}
}

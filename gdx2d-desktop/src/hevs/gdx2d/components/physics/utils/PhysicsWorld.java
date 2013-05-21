package hevs.gdx2d.components.physics.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Gets the single and only physics world, implements
 * the singleton design pattern.
 * 
 * @author Pierre-André Mudry (mui)
 * @version 1.01
 */
public class PhysicsWorld {

	private static World instance = null;

	// Exists only to defeat normal instantiation 
	private PhysicsWorld() {}

	public static World getInstance() {
		if (instance == null) {
			instance = new World(new Vector2(0, -10), true);
		}		
		return instance;		
	}
	
	/**
	 * To destroy it (required for JNI calls)
	 */
	public static void dispose(){
		if(instance != null){
			instance.dispose();
			instance = null;
		}
	}
	
}

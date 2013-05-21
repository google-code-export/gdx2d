package hevs.gdx2d.lib.utils;


/**
 * Provides a way to get a fixed framerate when calling the 
 * <code>sync</code> method in the game loop with the desired
 * frame-rate
 * 
 * Adapted from http://www.java-gaming.org/index.php?topic=22933.5
 * 
 * @author Pierre-Andre Mudry
 * @date March 2012 
 */
public class GraphicTimer {
	private long t1;	
	
	public GraphicTimer() {
		t1 = System.nanoTime();
	}

	/**
	 * Periodic call to this method in the main loop 
	 * insures a constant frame-rate. This is achieved in the
	 * method itself by waiting a variable amount of scale to get
	 * a constant frame-rate
	 * @param fps The desired frame-rate
	 */
	public void sync(int fps) {					
		long t2 = 1000000000L / fps + t1;
		long now = System.nanoTime();
		
		try {
			while (t2 > now) {
				Thread.sleep((t2 - now) / 10000000);								
				now = System.nanoTime();
				Thread.yield();
			}
		} catch (Exception e) {
		}

		t1 = now;
	}
}
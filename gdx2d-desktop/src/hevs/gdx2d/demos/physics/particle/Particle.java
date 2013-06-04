package hevs.gdx2d.demos.physics.particle;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.components.physics.PhysicsBox;
import hevs.gdx2d.lib.GdxGraphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;

/**
 * Demonstrates how to render particles
 * @author Pierre-André Mudry (mui)
 * @version 1.0 
 */
public class Particle extends PhysicsBox {
	protected static final BitmapImage img = new BitmapImage("data/texture.png");	
	protected int age = 0;
	protected final int maxAge;
			
	public Particle(Vector2 position, int radius, int maxAge) {
		super("particle", position, radius, radius, 0.12f, 1f, 1f);
		this.maxAge = maxAge;
		
		// Particles should not collide together
		Filter filter = new Filter();
		filter.groupIndex = -1;
		f.setFilterData(filter);			
	}

	public boolean shouldbeDestroyed(){
		return age > maxAge ? true : false;		
	}
	
	public void step() {
		age++;
	}

	public void render(GdxGraphics g) {
		final Color col = g.spriteBatch.getColor();
		g.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		
		// Make the particle disappear with time
		g.spriteBatch.setColor(.5f, 0.7f, 0.9f, 1.0f - age / (float) (maxAge+10));
		
		// Draw the particle
		g.spriteBatch.begin();
			g.spriteBatch.draw(img.getRegion(),(int) body.getPosition().x - img.getImage().getWidth()/2,(int) body.getPosition().y-img.getImage().getHeight()/2);
			g.spriteBatch.setColor(col);
		g.spriteBatch.end();
	}
}
package hevs.gdx2d.lib;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.components.graphics.Polygon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * A game graphics implementation for LibGDX based on the
 * interface of FunGraphics for INF1 class
 * 
 * FIXME: sprites sheets
 * TODO: multiple screens 
 * 
 * @author Nils Chatton (chn)
 * @author Pierre-Andre Mudry (mui)
 * @version 1.1
 */
public class GdxGraphics 
{

	/**
	 * For camera operations
	 */
	protected OrthographicCamera camera, fixedcamera;	
	
	protected ShapeRenderer shapeRenderer;
	protected Color currentColor = Color.WHITE;
	protected Color backGroundColor = Color.BLACK;
	public SpriteBatch spriteBatch;

	protected BitmapFont font;

	// For sprite-based circles
	protected Pixmap pixmap;
	protected Texture pixmaptex;
	Sprite sprite;					
	
	// For sprite-based logo
	final protected Texture logoBitmap;	
	
	public GdxGraphics(ShapeRenderer shapeRenderer2, SpriteBatch spriteBatch, OrthographicCamera camera) {
		this.shapeRenderer = shapeRenderer2;
		this.spriteBatch = spriteBatch;
		this.font = new BitmapFont();
		this.camera = camera;
		
		// A camera that never moves
		this.fixedcamera = new OrthographicCamera();
		fixedcamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			
		// Create a default sprite for filled circles
		pixmap = new Pixmap(128, 128, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fillCircle(64, 64, 64);
		pixmaptex = new Texture(pixmap);
		sprite = new Sprite(pixmaptex, 0, 0, 128, 128);
				
		// Preload the school logo
		logoBitmap = new Texture(Gdx.files.internal("data/logo_hes.png"));		
	}

	/**
	 * Draws frame per second (FPS) information
	 */
	public void drawFPS(){
		spriteBatch.setProjectionMatrix(fixedcamera.combined);
		setColor(Color.WHITE);
		drawString(5, 15, "FPS: " + Gdx.graphics.getFramesPerSecond());
		spriteBatch.setProjectionMatrix(camera.combined);
	}
	
	/**
	 * Draws school logo
	 */
	public void drawSchoolLogo(){
		spriteBatch.setProjectionMatrix(fixedcamera.combined);
		spriteBatch.begin();
			spriteBatch.draw(logoBitmap, getScreenWidth() - logoBitmap.getWidth(), 0);
		spriteBatch.end();		
		spriteBatch.setProjectionMatrix(camera.combined);
	}
	
	/**
	 * Draws the school logo in the upper right corner of the screen
	 */
	public void drawSchoolLogoUpperRight(){
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		spriteBatch.setProjectionMatrix(fixedcamera.combined);
		spriteBatch.begin();
			spriteBatch.draw(logoBitmap, width - logoBitmap.getWidth(), height - logoBitmap.getHeight());
		spriteBatch.end();
		spriteBatch.setProjectionMatrix(camera.combined);
	}
	
	/**
	 * Draws the school logo in the upper right corner of the screen
	 * @param moveWithCamera
	 * @deprecated use now {@link #drawSchoolLogoUpperRight()}
	 */
	@Deprecated
	public void drawSchoolLogoUpperRight(boolean moveWithCamera){
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		spriteBatch.setProjectionMatrix(fixedcamera.combined);
		spriteBatch.begin();
			spriteBatch.draw(logoBitmap, width - logoBitmap.getWidth(), height - logoBitmap.getHeight());
		spriteBatch.end();
		spriteBatch.setProjectionMatrix(camera.combined);
	}
	
	// FIXME : implement this
	public void setPenWidth(float width){
		new UnsupportedOperationException("Not implemented yet, sorry !");
	}	
		
	/**
	 * Gets the height of the screen
	 */
	public int getScreenHeight() {	
		return Gdx.graphics.getHeight();		
	}
	
	/**
	 * Gets the width of the rendering surface
	 */
	public int getScreenWidth() {	
		return Gdx.graphics.getWidth();
	}
	
	public void drawRectangle(int x, int y, int w, int h, int angle) {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(currentColor);
		shapeRenderer.identity();
		shapeRenderer.translate(x + w / 2, y + w / 2, 0);
		shapeRenderer.rotate(0, 0, 1, angle);
		shapeRenderer.rect(-w / 2, -w / 2, w, h);
		shapeRenderer.end();
	}

	public void clear() {
		Gdx.gl.glClearColor(0, 0, 0, 1);		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	public void clear(Color c) {
		Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	public void setColor(Color c) {
		currentColor = c;
	}

	public void setPixel(int x, int y) {
		shapeRenderer.begin(ShapeType.Point);
		shapeRenderer.identity();
		shapeRenderer.setColor(currentColor);
		shapeRenderer.point(x, y, 0);
		shapeRenderer.end();
	}

	public void setPixel(int x, int y, Color c) {
		shapeRenderer.begin(ShapeType.Point);
		shapeRenderer.identity();
		shapeRenderer.setColor(c);
		shapeRenderer.point(x, y, 0);
		shapeRenderer.end();
	}
		
	public void clearPixel(int x, int y) {
		shapeRenderer.begin(ShapeType.Point);
		shapeRenderer.identity();
		shapeRenderer.setColor(backGroundColor);
		shapeRenderer.point(x, y, 0);
		shapeRenderer.end();
	}

	public void drawLine(int p1x, int p1y, int p2x, int p2y) {
		shapeRenderer.begin(ShapeType.Line);		
		shapeRenderer.identity();
		shapeRenderer.setColor(currentColor);
		shapeRenderer.line(p1x, p1y, p2x, p2y);
		shapeRenderer.end();
	}

	public void drawLine(int p1x, int p1y, int p2x, int p2y, Color c) {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.identity();
		shapeRenderer.setColor(c);
		shapeRenderer.line(p1x, p1y, p2x, p2y);
		shapeRenderer.end();
	}

	public void drawFilledRectangle(int x, int y, int w, int h, int angle, Color c) {
		shapeRenderer.setColor(c);
		
		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.identity();			
			shapeRenderer.translate(x + w / 2, y + w / 2, 0);
			shapeRenderer.rotate(0, 0, 1, angle);
			shapeRenderer.rect(-w / 2, -w / 2, w, h);		
		shapeRenderer.end();
	}

	public void drawCircle(int centerX, int centerY, int radius) {
		shapeRenderer.begin(ShapeType.Line);		
			shapeRenderer.setColor(currentColor);
			shapeRenderer.identity();		
			shapeRenderer.circle(centerX, centerY, radius);
		shapeRenderer.end();		
	}

	public void drawFilledCircle(int centerX, int centerY, int radius, Color c) {		
		if(radius > 64)
		{
			shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(c);
				shapeRenderer.identity();
				shapeRenderer.circle(centerX, centerY, radius);
			shapeRenderer.end();				
		}
		else
		{		
			// Use a sprite-based approach to rendering circle			
			sprite.setScale(radius / 64.0f);
			sprite.setPosition(centerX-64, centerY-64);
			sprite.setColor(c);			
			spriteBatch.begin();						
				sprite.draw(spriteBatch);
			spriteBatch.end();			
		}
	}

	public void drawString(int posX, int posY, String str) {
		spriteBatch.begin();		
			font.drawMultiLine(spriteBatch, str, posX, posY);
		spriteBatch.end();
	}

	public void drawString(int posX, int posY, String str, Color color, int size) {
		// FIXME This is ugly (scaling does not work properly, should use another texture per size)
		spriteBatch.begin();
			font.setScale(size);		
			font.setColor(color);		
			font.drawMultiLine(spriteBatch, str, posX, posY);
			font.setScale(1);
		spriteBatch.end();
	}

	/**
	 * Draws an image in the background that will not move with the camera
	 * @param t 
	 * @param i
	 * @param j
	 */
	public void drawBackground(BitmapImage t, int i, int j){
		drawBackground(t.getImage(), i, j);
	}
	
	/**
	 * Draws a texture in background that will not move with the camera
	 * @param t
	 * @param i
	 * @param j
	 */
	public void drawBackground(Texture t, int i, int j){
		spriteBatch.setProjectionMatrix(fixedcamera.combined);
		
		spriteBatch.begin();		
			spriteBatch.disableBlending();
			spriteBatch.draw(t, i, j);
			spriteBatch.enableBlending();
		spriteBatch.end();
		
		spriteBatch.setProjectionMatrix(camera.combined);
	}
	
	/**
	 * Draws a {@link Texture} directly on screen
	 * @param t the texture to draw
	 * @param i x position of the texture
	 * @param j y position of the texture
	 * @param blending if blending should be enable or not. For performance reasons, blending is preferrably
	 * disabled for large texture if not required.
	 * @deprecated Use {@link #drawAlphaImage)
	 */
	@Deprecated
	public void drawTexture(Texture t, int i, int j, boolean blending) {
		spriteBatch.begin();		
		if(!blending)
			spriteBatch.disableBlending();
		// Required for scrollings
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.draw(t, i, j);		
		if(!blending)
			spriteBatch.enableBlending();		
		spriteBatch.end();
	}	

	/**
	 * Draws a picture at position ({@code posX, posY}).  
	 */
	public void drawPicture(float posX, float posY, BitmapImage bitmap) {						
		drawTransformedPicture(posX, posY, 0, 1, bitmap);		
	}
	
	/**
	 * Draws a picture at position ({@code posX, posY}). Center of rotation is located in the
	 * middle of the image. 
	 */
	public void drawTransformedPicture(float posX, float posY, float angle, float scale, BitmapImage bitmap) {
		drawTransformedPicture(posX, posY, bitmap.getImage().getWidth() / 2, bitmap.getImage().getHeight() / 2, angle, scale, bitmap);
	}
	
	/**
	 * Draws a picture at position ({@code posX, posY}) with a fixed width and height (not the one of the {@link BitmapImage} itself
	 */
	public void drawTransformedPicture(float posX, float posY, float angle, float width, float height, BitmapImage bitmap) {
		spriteBatch.begin();
			spriteBatch.draw(bitmap.getRegion(), (float)posX-width, (float)posY-height, width, height, width*2, height*2, 1.0f, 1.0f,(float) angle);
		spriteBatch.end();
	}
	
	/**
	 * Draws a picture at position ({@code posX, posY}) with a selectable center of rotation which is located
	 * at position ({@code centerX, centerY}).
	 * 
	 * @param posX
	 * @param posY
	 * @param centerX
	 * @param centerY
	 * @param angle
	 * @param scale
	 * @param bitmap
	 */
	public void drawTransformedPicture(float posX, float posY, float centerX, float centerY, float angle, float scale, BitmapImage bitmap) {
		spriteBatch.begin();			
			spriteBatch.draw(bitmap.getRegion(), posX-bitmap.getRegion().getRegionWidth()/2, posY-bitmap.getRegion().getRegionHeight()/2, centerX, centerY, (float) bitmap.getImage().getWidth(), (float) bitmap.getImage().getHeight(), (float) scale, (float) scale,(float) angle);
		spriteBatch.end();
	}

	public void drawPolygon(Polygon p) {
		shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(currentColor);
			shapeRenderer.identity();		
			shapeRenderer.polygon(p.getVertices());
		shapeRenderer.end();
	}

	public void drawFilledPolygon(Polygon polygon, Color c) {		
		float[] vertices =  polygon.getEarClippedVertices();	
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(c);
		shapeRenderer.identity();
		
		for (int i = 0; i < vertices.length; i+=6) {
			shapeRenderer.triangle(vertices[i], vertices[i+1], vertices[i+2], vertices[i+3], vertices[i+4], vertices[i+5]);
		}		
		
		shapeRenderer.end();
	}
	
	/****************************************************
	 * Camera stuff
	 ****************************************************/
	public OrthographicCamera getCamera(){
		return camera;
	}
	
	/**
	 * Move the camera to a fixed position
	 * @param x
	 * @param y
	 */
	public void moveCamera(float x, float y){
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(x, y);		
	}
	
	/**
	 * Puts the camera at its original location
	 */
	public void resetCamera(){
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());				
	}
	
	/**
	 * Displace the camera by a value
	 * @param dx
	 * @param dy
	 */
	public void scroll(float dx, float dy){
		camera.translate(dx, dy);
		camera.update();
	}
	
	/**
	 * Zooms in and out the camera, 1 is the default zoom-level
	 * @param factor
	 */
	public void zoom(float factor){
		camera.zoom = factor;
		camera.update();
	}
}

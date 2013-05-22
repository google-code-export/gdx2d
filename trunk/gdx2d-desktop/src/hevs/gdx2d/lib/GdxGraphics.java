package hevs.gdx2d.lib;

import hevs.gdx2d.components.bitmaps.BitmapImage;
import hevs.gdx2d.components.graphics.Polygon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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
 * @author Pierre-André Mudry (mui)
 * @author Nils Chatton (chn)
 * @version 1.12
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
	Sprite sprite;					
	
	// For sprite-based logo
	static final protected Texture logoTex = new Texture(Gdx.files.internal("data/logo_hes.png"));;	
	static final protected Texture circleTex = new Texture(Gdx.files.internal("data/circle.png"));;
	
	public GdxGraphics(ShapeRenderer shapeRenderer2, SpriteBatch spriteBatch, OrthographicCamera camera) {
		this.shapeRenderer = shapeRenderer2;
		this.spriteBatch = spriteBatch;
		this.font = new BitmapFont();
		this.camera = camera;
		
		// A camera that never moves
		this.fixedcamera = new OrthographicCamera();
		fixedcamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Enable alpha blending for shape rendere
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			
		// Create a default sprite for filled circles
		sprite = new Sprite(circleTex, 0, 0, 128, 128);
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
			spriteBatch.draw(logoTex, getScreenWidth() - logoTex.getWidth(), 0);
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
			spriteBatch.draw(logoTex, width - logoTex.getWidth(), height - logoTex.getHeight());
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
			spriteBatch.draw(logoTex, width - logoTex.getWidth(), height - logoTex.getHeight());
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
	
	public void drawRectangle(float x, float y, float w, float h, float angle) {
		shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(currentColor);
			shapeRenderer.identity();
			shapeRenderer.translate(x + w / 2, y + w / 2, 0);
			shapeRenderer.rotate(0, 0, 1, angle);
			shapeRenderer.rect(-w / 2, -w / 2, w, h);
		shapeRenderer.end();
	}

	/**
	 * Clears the screen black
	 */
	public void clear() {
		Gdx.gl.glClearColor(0, 0, 0, 1);		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	/**
	 * Clears the screen with a given color
	 * @param c
	 */
	public void clear(Color c) {
		Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	/**
	 * Sets the current color used for graphics drawing
	 * @param c
	 */
	public void setColor(Color c) {
		currentColor = c;
	}

	/**
	 * Sets the color of a pixel using the current color ({@link #currentColor}
	 * @param x
	 * @param y
	 */
	public void setPixel(float x, float y) {
		shapeRenderer.begin(ShapeType.Point);
		shapeRenderer.identity();
			shapeRenderer.setColor(currentColor);
			shapeRenderer.point(x, y, 0);
		shapeRenderer.end();
	}

	/**
	 * Sets the color of a pixel using a color
	 * @param x
	 * @param y
	 * @param c
	 */
	public void setPixel(float x, float y, Color c) {
		shapeRenderer.begin(ShapeType.Point);
		shapeRenderer.identity();
			shapeRenderer.setColor(c);
			shapeRenderer.point(x, y, 0);
		shapeRenderer.end();
	}
		
	public void clearPixel(float x, float y) {
		shapeRenderer.begin(ShapeType.Point);
			shapeRenderer.identity();
			shapeRenderer.setColor(backGroundColor);
			shapeRenderer.point(x, y, 0);
		shapeRenderer.end();
	}

	public void drawLine(float p1x, float p1y, float p2x, float p2y) {
		shapeRenderer.begin(ShapeType.Line);		
			shapeRenderer.identity();
			shapeRenderer.setColor(currentColor);
			shapeRenderer.line(p1x, p1y, p2x, p2y);
		shapeRenderer.end();
	}

	public void drawLine(float p1x, float p1y, float p2x, float p2y, Color c) {
		shapeRenderer.setColor(c);
		drawLine(p1x, p1y, p2x, p2y);		
	}

	public void drawFilledRectangle(float x, float y, float w, float h, float angle) {
		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.identity();			
			shapeRenderer.translate(x + w / 2, y + w / 2, 0);
			shapeRenderer.rotate(0, 0, 1, angle);
			shapeRenderer.rect(-w / 2, -w / 2, w, h);		
		shapeRenderer.end();
	}
	
	public void drawFilledRectangle(float x, float y, float w, float h, float angle, Color c) {
		shapeRenderer.setColor(c);		
		drawFilledRectangle(x, y, w, h, angle);
	}

	public void drawCircle(float centerX, float centerY, float radius, Color c) {
		shapeRenderer.setColor(c);
		drawCircle(centerX, centerY, radius);
	}
	
	public void drawCircle(float centerX, float centerY, float radius) {
		shapeRenderer.begin(ShapeType.Line);		
			shapeRenderer.identity();		
			shapeRenderer.circle(centerX, centerY, radius);
		shapeRenderer.end();		
	}

	public void drawFilledCircle(float centerX, float centerY, float radius, Color c) {		
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

	public void drawString(float posX, float posY, String str) {
		spriteBatch.begin();		
			font.drawMultiLine(spriteBatch, str, posX, posY);
		spriteBatch.end();
	}

	public void drawString(float posX, float posY, String str, Color color, float size) {
		// FIXME Scaling does not work properly, should use another texture per size
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
	public void drawBackground(BitmapImage t, float i, float j){
		drawBackground(t.getImage(), i, j);
	}
	
	/**
	 * Draws a texture in background that will not move with the camera
	 * @param t
	 * @param i
	 * @param j
	 */
	public void drawBackground(Texture t, float i, float j){
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
	 * Draws a picture at position ({@code posX, posY}) with a fixed width and height (not the one of the {@link BitmapImage} itself).
	 */
	public void drawTransformedPicture(float posX, float posY, float angle, float width, float height, BitmapImage bitmap) {
		spriteBatch.begin();
			spriteBatch.draw(bitmap.getRegion(), (float)posX-width, (float)posY-height, width, height, width*2, height*2, 1.0f, 1.0f,(float) angle);
		spriteBatch.end();
	}
	
	/**
	 * Draws a picture at position ({@code posX, posY}) with a selectable center of rotation which is located
	 * at position offset by ({@code centerX, centerY}).
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

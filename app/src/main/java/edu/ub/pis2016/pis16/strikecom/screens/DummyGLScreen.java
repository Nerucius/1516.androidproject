package edu.ub.pis2016.pis16.strikecom.screens;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;

/**
 * Dummy OpenGL screen.
 * <p/>
 * Order of calls:
 * - Created
 * - Resumed
 * - Resized
 * <p/>
 * Loop:
 * - Update
 * - Presented
 * <p/>
 * On back:
 * - Paused
 * - Disposed
 */
public class DummyGLScreen extends Screen {

	Texture atlas;
	TextureRegion strikeBaseMK2;
	SpriteBatch batch;

	public DummyGLScreen(Game game) {
		super(game);
		Log.i("SCREEN", "Created");
	}

	@Override
	public void resume() {
		Log.i("SCREEN", "Resumed");

		batch = new SpriteBatch(game.getGLGraphics(), 16);
		atlas = new Texture(game, "strike_base_mk2.png");
		strikeBaseMK2 = new TextureRegion(atlas, 0, 0, 32, 32);
	}

	@Override
	public void resize(int width, int height) {
		Log.i("SCREEN", "Resized: " + width + "x" + height);

		GL10 gl = game.getGLGraphics().getGL();
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1);

		gl.glViewport(0,0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = game.getGLGraphics().getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin(atlas);
		batch.drawSprite(0, 0, 32, 32, strikeBaseMK2);
		batch.end();

	}

	@Override
	public void pause() {
		Log.i("SCREEN", "Paused");

	}


	@Override
	public void dispose() {
		Log.i("SCREEN", "Disposed");

	}
}

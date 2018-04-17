package pl.lonski.kolourator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

class Figure extends Actor {

	private final Texture texture;

	Figure() {
		this.texture = new Texture(Gdx.files.internal("badlogic.jpg"));
		setBounds(0,0, texture.getWidth(), texture.getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY());
	}
}

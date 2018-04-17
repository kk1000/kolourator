package pl.lonski.kolourator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

class Figure extends Actor {

	private final TextureRegion texture;
	private final TextureRegion textureColor;
	private final BrushColor color;
	private boolean isColored;

	Figure(BrushColor color) {
		this.texture = new TextureRegion(new Texture(Gdx.files.internal("figure/ladybird.png")));
		this.textureColor = new TextureRegion(new Texture(Gdx.files.internal("figure/ladybird_color.png")));
		this.color = color;
		this.isColored = false;
		setBounds(0, 0, texture.getRegionWidth(), texture.getRegionHeight());
	}

	BrushColor getBrushColor() {
		return color;
	}

	void setColored(boolean colored) {
		isColored = colored;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color old = batch.getColor();
		batch.setColor(getColor());
		batch.draw(isColored ? textureColor : texture,
				getX(), getY(),
				getOriginX() + getWidth() / 2, getOriginY() + getHeight() / 2,
				getWidth(), getHeight(),
				getScaleX(), getScaleY(), getRotation());
		batch.setColor(old);
	}
}

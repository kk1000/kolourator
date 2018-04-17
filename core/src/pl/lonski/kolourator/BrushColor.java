package pl.lonski.kolourator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

enum BrushColor {
	RED("red.png"),
	GREEN("green.png"),
	BLUE("blue.png"),
	YELLOW("yellow.png");

	private final Texture texture;

	BrushColor(String textureFile) {
		this.texture = new Texture(Gdx.files.internal("brush/" + textureFile));
	}

	Texture getTexture() {
		return texture;
	}
}

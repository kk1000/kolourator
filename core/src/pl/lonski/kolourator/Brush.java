package pl.lonski.kolourator;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class Brush extends Actor {

	private float originalX;
	private float originalY;
	private final TextureRegion texture;
	private final BrushDropHandler dropHandler;
	private final BrushColor color;

	Brush(BrushColor color, final BrushDropHandler dropHandler) {
		this.color = color;
		this.dropHandler = dropHandler;
		this.texture = new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg")));
		setBounds(0, 0, texture.getRegionWidth(), texture.getRegionHeight());

		addListener(new DragListener() {
			public void dragStart(InputEvent event, float x, float y, int pointer) {
				if (originalX == 0) {
					originalX = getX();
				}
				if (originalY == 0) {
					originalY = getY();
				}
			}

			public void drag(InputEvent event, float x, float y, int pointer) {
				moveBy(x - getWidth() / 2, y - getHeight() / 2);
			}

			public void dragStop(InputEvent event, float x, float y, int pointer) {
				dropHandler.onDrop(x, y, Brush.this);
			}
		});
	}

	BrushColor getBrushColor() {
		return color;
	}

	void moveToOriginalPosition() {
		addAction(moveTo(originalX, originalY, 0.2f));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture,
				getX(), getY(),
				getOriginX() + getWidth() / 2, getOriginY() + getHeight() / 2,
				getWidth(), getHeight(),
				getScaleX(), getScaleY(), getRotation());
	}
}

package pl.lonski.kolourator;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static pl.lonski.kolourator.BrushColor.*;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

class GameStage extends Stage {

	private static final float ANIMATION_SPEED = 0.3f;
	private final float screenWidth;
	private final float screenHeight;
	private Figure figure;

	GameStage() {
		this.screenWidth = Gdx.graphics.getWidth();
		this.screenHeight = Gdx.graphics.getHeight();

		next();
	}

	private void next() {
		for (Actor actor : getActors()) {
			actor.addAction(Actions.removeActor());
		}

		figure = createFigure();
		addActor(figure);

		for (Brush brush : createBrushes(RED, GREEN, BLUE, YELLOW)) {
			addActor(brush);
		}
	}

	private Figure createFigure() {
		Figure figure = new Figure(RED);
		float x = (screenWidth - figure.getWidth()) / 2;
		figure.setPosition(x, -figure.getHeight());
		figure.addAction(Actions.moveTo(x, 50, ANIMATION_SPEED * 2));
		return figure;
	}

	private List<Brush> createBrushes(BrushColor... colors) {
		List<Brush> brushes = new ArrayList<>();
		for (int i = 0; i < colors.length; i++) {
			Brush brush = new Brush(colors[i], getBrushHandler());
			brush.setScale(0.01f);
			brush.setVisible(false);
			brush.addAction(sequence(delay(ANIMATION_SPEED * i), show(), scaleTo(1, 1, ANIMATION_SPEED)));
			brushes.add(brush);
		}
		final float spacing = screenWidth / 10;
		final float margin = (screenWidth
				- spacing * Math.max(0, brushes.size() - 1)
				- brushes.get(0).getWidth() * brushes.size()) / 2;

		for (int i = 0; i < brushes.size(); i++) {
			Brush brush = brushes.get(i);
			brush.setPosition(margin + i * (brush.getWidth() + spacing), screenHeight * 0.9f - brush.getHeight());
		}
		return brushes;
	}

	private BrushDropHandler getBrushHandler() {
		return new BrushDropHandler() {
			@Override
			public void onDrop(float x, float y, Brush brush) {
				if (isPainting(brush) && figure.getBrushColor() == brush.getBrushColor()) {
					next();
				} else {
					brush.moveToOriginalPosition();
				}
			}
		};
	}

	private boolean isPainting(Brush brush) {
		return overlaps(brush, figure);
	}

	private boolean overlaps(Actor a1, Actor a2) {
		Rectangle r1 = new Rectangle(a1.getX(), a1.getY(), a1.getWidth(), a1.getHeight());
		Rectangle r2 = new Rectangle(a2.getX(), a2.getY(), a2.getWidth(), a2.getHeight());
		return r1.overlaps(r2);
	}

}

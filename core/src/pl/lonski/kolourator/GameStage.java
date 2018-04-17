package pl.lonski.kolourator;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static pl.lonski.kolourator.GameStage.FigureAction.*;

import java.security.SecureRandom;
import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.SnapshotArray;

class GameStage extends Stage {

	private static final float ANIMATION_SPEED = 0.3f;
	private final float screenWidth;
	private final float screenHeight;
	private final List<Integer> brushIds;
	private final Random random;
	private Figure figure;
	private Kolourator game;

	GameStage(Kolourator game) {
		getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		this.screenWidth = getViewport().getScreenWidth();
		this.screenHeight = getViewport().getScreenHeight();
		this.game = game;
		this.random = new SecureRandom();
		this.brushIds = new ArrayList<>();
		for (Brush brush : game.getBrushes()) {
			brushIds.add(brush.getId());
		}
		next();
	}

	private void next() {
		for (Actor actor : new SnapshotArray<Actor>(getActors())) {
			actor.remove();
		}
		figure = createFigure();
		addActor(figure);

		for (Brush brush : createBrushes(figure.getBrushId())) {
			addActor(brush);
		}
	}

	private Figure createFigure() {
		int index = random.nextInt(game.getFigures().size());
		Figure figure = game.getFigures().get(index);
		float x = (screenWidth - figure.getWidth() + game.getBrushes().get(0).getWidth() * 2) / 2;
		float y = (screenHeight - figure.getHeight()) / 2;
		figure.setPosition(x, -figure.getHeight());
		figure.addAction(moveTo(x, y, ANIMATION_SPEED * 2));
		figure.setColored(false);
		return figure;
	}

	private Brush findBrush(int id) {
		for (Brush brush : game.getBrushes()) {
			if (brush.getId() == id) {
				return brush;
			}
		}
		throw new RuntimeException("Brush id " + id + " is missing!");
	}

	private List<Brush> pickBrushSet(int figureBrushId) {
		List<Brush> brushes = new ArrayList<>();
		List<Integer> ids = new ArrayList<>(brushIds);
		brushes.add(findBrush(figureBrushId));
		ids.remove(Integer.valueOf(figureBrushId));
		for (int i = 0; i < 2; i++) {
			int index = random.nextInt(ids.size());
			int id = ids.get(index);
			ids.remove(index);
			brushes.add(findBrush(id));
		}
		return brushes;
	}

	private List<Brush> createBrushes(final int figureBrushId) {
		List<Brush> brushes = pickBrushSet(figureBrushId);
		Collections.shuffle(brushes);
		final float spacing = screenHeight / 10;
		final float margin = (screenHeight
				- spacing * Math.max(0, brushes.size() - 1)
				- brushes.get(0).getHeight() * brushes.size()) / 2;
		for (int i = 0; i < brushes.size(); i++) {
			final Brush brush = brushes.get(i);
			brush.setScale(0.01f);
			brush.setVisible(false);
			brush.setPosition(brush.getWidth() / 2, margin + i * (brush.getHeight() + spacing));
			brush.setOriginalPosition(brush.getX(), brush.getY());
			brush.addAction(sequence(delay(ANIMATION_SPEED * i), show(), scaleTo(1, 1, ANIMATION_SPEED)));
			brush.addListener(new DragListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					game.getSpeaker().speak(brush.getSpokenName());
					return super.touchDown(event, x, y, pointer, button);
				}

				public void drag(InputEvent event, float x, float y, int pointer) {
					brush.moveBy(x - brush.getWidth() / 2, y - brush.getHeight() / 2);
				}

				public void dragStop(InputEvent event, float x, float y, int pointer) {
					if (isPainting(brush) && figureBrushId == brush.getId()) {
						brush.remove();
						figure.addAction(sequence(
								fadeOut(ANIMATION_SPEED),
								colorFigure(figure),
								fadeIn(ANIMATION_SPEED),
								speakFigure(game.getSpeaker(), figure),
								waitForSpeaker(game.getSpeaker()),
								moveTo(screenWidth, figure.getY(), ANIMATION_SPEED * 2),
								nextFigure(GameStage.this)
						));
					} else {
						brush.moveToOriginalPosition();
					}
				}
			});
		}
		return brushes;
	}

	private boolean isPainting(Brush brush) {
		return overlaps(brush, figure);
	}

	private boolean overlaps(Actor a1, Actor a2) {
		Rectangle r1 = new Rectangle(a1.getX(), a1.getY(), a1.getWidth(), a1.getHeight());
		Rectangle r2 = new Rectangle(a2.getX(), a2.getY(), a2.getWidth(), a2.getHeight());
		return r1.overlaps(r2);
	}

	static class FigureAction {
		static Action speakFigure(final Speaker speaker, final Figure figure) {
			return new Action() {
				@Override
				public boolean act(float delta) {
					speaker.speak(figure.getSpokenName());
					return true;
				}
			};
		}

		static Action colorFigure(final Figure figure) {
			return new Action() {
				@Override
				public boolean act(float delta) {
					figure.setColored(true);
					return true;
				}
			};
		}

		static Action waitForSpeaker(final Speaker speaker) {
			return new Action() {
				@Override
				public boolean act(float delta) {
					return !speaker.isSpeaking();
				}
			};
		}

		static Action nextFigure(final GameStage gameStage) {
			return new Action() {
				@Override
				public boolean act(float delta) {
					gameStage.next();
					return true;
				}
			};
		}
	}

}

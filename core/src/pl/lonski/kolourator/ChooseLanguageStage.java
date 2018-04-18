package pl.lonski.kolourator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

class ChooseLanguageStage extends Stage {

	private final Kolourator game;
	private final float screenWidth = Gdx.graphics.getWidth();
	private final float screenHeight = Gdx.graphics.getHeight();

	ChooseLanguageStage(Kolourator game) {
		this.game = game;

		FlagButton pl = createFlag("pl");
		pl.setX(screenWidth / 4 - pl.getWidth() / 2);
		addActor(pl);

		FlagButton en = createFlag("en");
		en.setX(screenWidth * 0.75f - en.getWidth() / 2);
		addActor(en);
	}

	private FlagButton createFlag(final String country) {
		FlagButton flag = new FlagButton(new Texture(Gdx.files.internal("flag/" + country + ".png")));
		flag.setPosition(screenWidth / 2 - flag.getWidth() / 2, screenHeight / 2 - flag.getHeight() / 2);
		flag.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.startGame("config/" + country + ".json");
				return true;
			}
		});
		return flag;
	}
}

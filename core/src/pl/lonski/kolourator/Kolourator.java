package pl.lonski.kolourator;

import java.util.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;

public class Kolourator extends ApplicationAdapter {

	private Stage stage;
	private Speaker.Provider speakerProvider;
	private Speaker speaker;
	private List<Brush> brushes;
	private List<Figure> figures;

	Kolourator(Speaker.Provider speakerProvider) {
		this.speakerProvider = speakerProvider;
	}

	List<Brush> getBrushes() {
		return brushes;
	}

	Speaker getSpeaker() {
		return speaker;
	}

	@Override
	public void create() {
		stage = new ChooseLanguageStage(this);
		Gdx.input.setInputProcessor(stage);
	}

	void startGame(String configFile) {
		Config config = new Json().fromJson(Config.class, Gdx.files.internal(configFile).readString());

		brushes = new ArrayList<>();
		for (Config.BrushDef def : config.brushes) {
			TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal(def.textureFile)));
			brushes.add(new Brush(def.id, def.spokenName, texture));
		}

		figures = new ArrayList<>();
		for (Config.FigureDef def : config.figures) {
			TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal(def.textureFile)));
			TextureRegion textureColor = new TextureRegion(new Texture(Gdx.files.internal(def.textureFileColor)));
			figures.add(new Figure(def.brushId, def.spokenName, texture, textureColor));
		}
		Collections.shuffle(figures);

		speaker = speakerProvider.get(new Locale(config.language));
		stage = new GameStage(this);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public List<Figure> getFigures() {
		return figures;
	}
}

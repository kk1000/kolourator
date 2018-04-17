package pl.lonski.kolourator;

import java.util.Locale;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Speaker.Provider speakerProvider = new Speaker.Provider() {
			@Override
			public Speaker get(Locale locale) {
				return new AndroidSpeaker(AndroidLauncher.this, locale);
			}
		};
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Kolourator(speakerProvider), config);
	}
}

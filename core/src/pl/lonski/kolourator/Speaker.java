package pl.lonski.kolourator;

import java.util.Locale;

public interface Speaker {
	void speak(String text);

	void speakQueued(String text);

	boolean isSpeaking();

	interface Provider {
		Speaker get(Locale locale);
	}
}

package com.alexrnl.subtitlecorrector.common;

import static com.alexrnl.subtitlecorrector.common.TranslationKeys.KEYS;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test suite for the {@link TranslationKeys} class.
 * @author Alex
 */
public class TranslationKeysTest {
	
	/**
	 * Test that the return values of the method is correct.
	 */
	@Test
	public void testTranslations () {
		assertEquals("subtitlecorrector.strategy", KEYS.strategy().toString());
		assertEquals("subtitlecorrector.strategy.letterreplacement", KEYS.strategy().letterReplacement().toString());
		assertEquals("subtitlecorrector.strategy.letterreplacement.description", KEYS.strategy().letterReplacement().description());
		assertEquals("subtitlecorrector.strategy.letterreplacement.originalletter", KEYS.strategy().letterReplacement().originalLetter());
		assertEquals("subtitlecorrector.strategy.letterreplacement.newletter", KEYS.strategy().letterReplacement().newLetter());
		assertEquals("subtitlecorrector.strategy.letterreplacement.onlymissingfromdictionary", KEYS.strategy().letterReplacement().onlyMissingFromDictionary());
		assertEquals("subtitlecorrector.strategy.letterreplacement.promptbeforecorrecting", KEYS.strategy().letterReplacement().promptBeforeCorrecting());
		assertEquals("subtitlecorrector.strategy.fixpunctuation", KEYS.strategy().fixPunctuation().toString());
		assertEquals("subtitlecorrector.strategy.fixpunctuation.description", KEYS.strategy().fixPunctuation().description());
		assertEquals("subtitlecorrector.strategy.fixpunctuation.locale", KEYS.strategy().fixPunctuation().locale());
		
		assertEquals("subtitlecorrector.subtitleprovider.noaccess", KEYS.subtitleProvider().noAccess());
		assertEquals("subtitlecorrector.subtitleprovider.foldervisiterror", KEYS.subtitleProvider().folderVisitError());
		assertEquals("subtitlecorrector.subtitleprovider.notfilenotdirectory", KEYS.subtitleProvider().notFileNotDirectory());
		assertEquals("subtitlecorrector.subtitleprovider.choosesubtitleformat", KEYS.subtitleProvider().chooseSubtitleFormat());
		assertEquals("subtitlecorrector.subtitleprovider.subtitlefilereaderror", KEYS.subtitleProvider().subtitleFileReadError());
		assertEquals("subtitlecorrector.subtitleprovider.nosubtitletocorrect", KEYS.subtitleProvider().noSubtitleToCorrect());
		
		assertEquals("subtitlecorrector.mainwindow.title", KEYS.mainWindow().title());
		assertEquals("subtitlecorrector.mainwindow.subtitleLabel", KEYS.mainWindow().subtitleLabel());
		assertEquals("subtitlecorrector.mainwindow.subtitleButton", KEYS.mainWindow().subtitleButton());
		assertEquals("subtitlecorrector.mainwindow.strategyLabel", KEYS.mainWindow().strategyLabel());
		assertEquals("subtitlecorrector.mainwindow.startCorrectingButton", KEYS.mainWindow().startCorrectingButton());
		
		assertEquals("subtitlecorrector.console.yes", KEYS.console().yes());
		assertEquals("subtitlecorrector.console.no", KEYS.console().no());
		assertEquals("subtitlecorrector.console.yesnoprompt", KEYS.console().yesNoPrompt());
		assertEquals("subtitlecorrector.console.userprompt", KEYS.console().userPrompt().toString());
		assertEquals("subtitlecorrector.console.userprompt.replace", KEYS.console().userPrompt().replace());
		assertEquals("subtitlecorrector.console.userprompt.context", KEYS.console().userPrompt().context());
		assertEquals("subtitlecorrector.console.userprompt.changereplacement", KEYS.console().userPrompt().changeReplacement());
		assertEquals("subtitlecorrector.console.userprompt.rememberchoice", KEYS.console().userPrompt().rememberChoice());
		assertEquals("subtitlecorrector.console.app", KEYS.console().app().toString());
		assertEquals("subtitlecorrector.console.app.strategyparametersinput", KEYS.console().app().strategyParametersInput());
		assertEquals("subtitlecorrector.console.app.strategyparametersinvalidvalue", KEYS.console().app().strategyParametersInvalidValue());
		assertEquals("subtitlecorrector.console.app.subtitlewriteerror", KEYS.console().app().subtitleWriteError());
	}
}

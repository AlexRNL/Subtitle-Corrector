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
		assertEquals("subtitlecorrector.strategy.checkspelling", KEYS.strategy().checkSpelling().toString());
		assertEquals("subtitlecorrector.strategy.checkspelling.description", KEYS.strategy().checkSpelling().description());
		
		assertEquals("subtitlecorrector.subtitleprovider.noaccess", KEYS.subtitleProvider().noAccess());
		assertEquals("subtitlecorrector.subtitleprovider.foldervisiterror", KEYS.subtitleProvider().folderVisitError());
		assertEquals("subtitlecorrector.subtitleprovider.notfilenotdirectory", KEYS.subtitleProvider().notFileNotDirectory());
		assertEquals("subtitlecorrector.subtitleprovider.choosesubtitleformat", KEYS.subtitleProvider().chooseSubtitleFormat());
		assertEquals("subtitlecorrector.subtitleprovider.subtitlefilereaderror", KEYS.subtitleProvider().subtitleFileReadError());
		assertEquals("subtitlecorrector.subtitleprovider.nosubtitletocorrect", KEYS.subtitleProvider().noSubtitleToCorrect());
		
		assertEquals("subtitlecorrector.gui.mainwindow.title", KEYS.gui().mainWindow().title());
		assertEquals("subtitlecorrector.gui.mainwindow.subtitleLabel", KEYS.gui().mainWindow().subtitleLabel());
		assertEquals("subtitlecorrector.gui.mainwindow.subtitleButton", KEYS.gui().mainWindow().subtitleButton());
		assertEquals("subtitlecorrector.gui.mainwindow.strategyLabel", KEYS.gui().mainWindow().strategyLabel());
		assertEquals("subtitlecorrector.gui.mainwindow.overwriteLabel", KEYS.gui().mainWindow().overwriteLabel());
		assertEquals("subtitlecorrector.gui.mainwindow.startCorrectingButton", KEYS.gui().mainWindow().startCorrectingButton());
		assertEquals("subtitlecorrector.gui.userprompt.replacewithcontext", KEYS.gui().userPrompt().replaceWithContext());
		assertEquals("subtitlecorrector.gui.userprompt.replace", KEYS.gui().userPrompt().replace());
		assertEquals("subtitlecorrector.gui.userprompt.rememberchoice", KEYS.gui().userPrompt().rememberChoice());
		
		assertEquals("subtitlecorrector.console.yes", KEYS.console().yes());
		assertEquals("subtitlecorrector.console.no", KEYS.console().no());
		assertEquals("subtitlecorrector.console.promptmark", KEYS.console().promptMark());
		assertEquals("subtitlecorrector.console.yesnoprompt", KEYS.console().yesNoPrompt());
		assertEquals("subtitlecorrector.console.userprompt", KEYS.console().userPrompt().toString());
		assertEquals("subtitlecorrector.console.userprompt.invalidchoice", KEYS.console().userPrompt().invalidChoice());
		assertEquals("subtitlecorrector.console.userprompt.replace", KEYS.console().userPrompt().replace());
		assertEquals("subtitlecorrector.console.userprompt.context", KEYS.console().userPrompt().context());
		assertEquals("subtitlecorrector.console.userprompt.changereplacement", KEYS.console().userPrompt().changeReplacement());
		assertEquals("subtitlecorrector.console.userprompt.rememberchoice", KEYS.console().userPrompt().rememberChoice());
		assertEquals("subtitlecorrector.console.app", KEYS.console().app().toString());
		assertEquals("subtitlecorrector.console.app.strategyparametersinput", KEYS.console().app().strategyParametersInput());
		assertEquals("subtitlecorrector.console.app.strategyparametersinvalidvalue", KEYS.console().app().strategyParametersInvalidValue());
		assertEquals("subtitlecorrector.console.app.subtitlewriteerror", KEYS.console().app().subtitleWriteError());
		
		assertEquals("subtitlecorrector.misc.fileExtension", KEYS.misc().fileExtension());
	}
}

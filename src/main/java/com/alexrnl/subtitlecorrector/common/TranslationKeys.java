package com.alexrnl.subtitlecorrector.common;

import static com.alexrnl.commons.translation.Translator.HIERARCHY_SEPARATOR;

/**
 * The translation keys structure.<br />
 * @author Alex
 */
public final class TranslationKeys {
	/** The root key of the translations */
	private static final String			ROOT_KEY	= "subtitlecorrector";
	/** Attribute to access the translation keys */
	public static final TranslationKeys	KEYS		= new TranslationKeys(ROOT_KEY);
	
	/** The key for the subtitle corrector */
	private final String				subtitleCorrectorKey;

	/**
	 * Constructor #1.<br />
	 * @param rootKey
	 *        the root key of the translation file.
	 */
	private TranslationKeys (final String rootKey) {
		super();
		subtitleCorrectorKey = rootKey;
	}
	
	/**
	 * The translations for the strategies
	 */
	public static final class Strategy {
		/** The root key for the strategies */
		private static final String	STRATEGY_KEY	= "strategy";
		
		/** The key for the strategies translations */
		private final String		strategyKey;
		
		/**
		 * Constructor #1.<br />
		 * @param parentKey
		 *        the key from the parent category.
		 */
		private Strategy (final String parentKey) {
			super();
			strategyKey = parentKey + HIERARCHY_SEPARATOR + STRATEGY_KEY;
		}
		
		@Override
		public String toString () {
			return strategyKey;
		}
		
		/**
		 * Class which factorise common translation keys declaration in strategies.
		 */
		public abstract static class AbstractStrategy {
			/** The key for the actual strategy translations */
			private final String	actualStrategyKey;
			
			/**
			 * Constructor #1.<br />
			 * @param parentKey
			 *        the key from the parent category.
			 * @param strategyKey
			 *        the key for the actual strategy.
			 */
			protected AbstractStrategy (final String parentKey, final String strategyKey) {
				super();
				actualStrategyKey = parentKey + HIERARCHY_SEPARATOR + strategyKey;
			}
			
			@Override
			public String toString () {
				return actualStrategyKey;
			}
			
			/**
			 * The translation for the description of the strategy.
			 * @return the description of the strategy.
			 */
			public String description () {
				return actualStrategyKey + HIERARCHY_SEPARATOR + "description";
			}
		}
		
		/**
		 * Translations for the letter replacement strategy.
		 */
		public static final class LetterReplacement extends AbstractStrategy {
			/** The root key for the letter replacement strategy */
			private static final String	LETTER_REPLACEMENT_KEY	= "letterreplacement";
			
			/**
			 * Constructor #1.<br />
			 * @param parentKey
			 *        the key from the parent category.
			 */
			private LetterReplacement (final String parentKey) {
				super(parentKey, LETTER_REPLACEMENT_KEY);
			}
			
			/**
			 * The translation for the original letter.
			 * @return the original letter.
			 */
			public String originalLetter () {
				return toString() + HIERARCHY_SEPARATOR + "originalletter";
			}
			
			/**
			 * The translation for the new letter.
			 * @return the new letter.
			 */
			public String newLetter () {
				return toString() + HIERARCHY_SEPARATOR + "newletter";
			}
			
			/**
			 * The translation for only missing from dictionary parameter.
			 * @return the only missing from dictionary parameter.
			 */
			public String onlyMissingFromDictionary () {
				return toString() + HIERARCHY_SEPARATOR + "onlymissingfromdictionary";
			}
			
			/**
			 * The translation for prompt before correcting parameter.
			 * @return the prompt before correcting parameter.
			 */
			public String promptBeforeCorrecting () {
				return toString() + HIERARCHY_SEPARATOR + "promptbeforecorrecting";
			}
		}
		
		/**
		 * The letter replacement translations.
		 * @return the letter replacement translations.
		 */
		public LetterReplacement letterReplacement () {
			return new LetterReplacement(strategyKey);
		}
		
		/**
		 * Translation for the fix punctuation strategy.
		 */
		public static final class FixPunctuation extends AbstractStrategy {
			/** The root key for the fix punctuation strategy */
			private static final String	FIX_PUNCTUATION_KEY	= "fixpunctuation";
			
			/**
			 * Constructor #1.<br />
			 * @param parentKey
			 *        the key from the parent category.
			 */
			private FixPunctuation (final String parentKey) {
				super(parentKey, FIX_PUNCTUATION_KEY);
			}
			
			/**
			 * The translation for the locale parameter.
			 * @return the translation for the locale parameter.
			 */
			public String locale () {
				return toString() + HIERARCHY_SEPARATOR + "locale";
			}
		}
		
		/**
		 * The fix punctuation translations.
		 * @return the fix punctuation translations.
		 */
		public FixPunctuation fixPunctuation () {
			return new FixPunctuation(strategyKey);
		}
		
		/**
		 * The translations for the check spelling strategy.
		 */
		public static final class CheckSpelling extends AbstractStrategy {
			/** The root key for the check spelling strategy */
			private static final String	CHECK_SPELLING_KEY	= "checkspelling";
			
			/**
			 * Constructor #1.<br />
			 * @param parentKey
			 *        the key from the parent category.
			 */
			private CheckSpelling (final String parentKey) {
				super(parentKey, CHECK_SPELLING_KEY);
			}
		}
		
		/**
		 * The check spelling translations.
		 * @return the check spelling translations.
		 */
		public CheckSpelling checkSpelling () {
			return new CheckSpelling(strategyKey);
		}
		
	}
	
	/**
	 * The strategy translations.
	 * @return the strategy translation.
	 */
	public Strategy strategy () {
		return new Strategy(subtitleCorrectorKey);
	}
	
	/**
	 * The translations for the subtitle provider.
	 */
	public static final class SubtitleProvider {
		/** The root key for the subtitle provider translations */
		private static final String	SUBTITLE_PROVIDER_KEY	= "subtitleprovider";
		
		/** The key for the subtitle provider translation */
		private final String		subtitleProviderKey;
		
		/**
		 * Constructor #1.<br />
		 * @param parentKey
		 *        the key from the parent category.
		 */
		private SubtitleProvider (final String parentKey) {
			super();
			subtitleProviderKey = parentKey + HIERARCHY_SEPARATOR + SUBTITLE_PROVIDER_KEY;
		}

		/**
		 * Return the translation for the lack of access to files.
		 * @return the translation for no access message.
		 */
		public String noAccess () {
			return subtitleProviderKey + HIERARCHY_SEPARATOR + "noaccess";
		}

		/**
		 * Return the translation for the folder visit error.
		 * @return the translation for the folder visit error.
		 */
		public String folderVisitError () {
			return subtitleProviderKey + HIERARCHY_SEPARATOR + "foldervisiterror";
		}

		/**
		 * Return the translation for the not file neither directory error.
		 * @return the translation for the not file/not directory error.
		 */
		public String notFileNotDirectory () {
			return subtitleProviderKey + HIERARCHY_SEPARATOR + "notfilenotdirectory";
		}
		
		/**
		 * Return the translation for the chooser for the subtitle format.
		 * @return the translation for the chooser.
		 */
		public String chooseSubtitleFormat () {
			return subtitleProviderKey + HIERARCHY_SEPARATOR + "choosesubtitleformat";
		}
		
		/**
		 * Return the translation for the subtitle file read error.
		 * @return the translation for the the subtitle file read error.
		 */
		public String subtitleFileReadError () {
			return subtitleProviderKey + HIERARCHY_SEPARATOR + "subtitlefilereaderror";
		}

		/**
		 * Return the translation for the no subtitle to correct message.
		 * @return the translation for the no subtitle to correct message.
		 */
		public String noSubtitleToCorrect () {
			return subtitleProviderKey + HIERARCHY_SEPARATOR + "nosubtitletocorrect";
		}
	}
	
	/**
	 * The subtitle provider translations.
	 * @return the translations for the subtitle provider.
	 */
	public SubtitleProvider subtitleProvider () {
		return new SubtitleProvider(subtitleCorrectorKey);
	}
	
	/**
	 * The translation for the graphic user interface
	 */
	public static final class Gui {
		/** The root key for the GUI */
		private static final String	GUI_KEY	= "gui";
		
		/** The key for the GUI translations */
		private final String		guiKey;
		
		/**
		 * Constructor #1.<br />
		 * @param parentKey
		 *        the key from the parent category.
		 */
		private Gui (final String parentKey) {
			super();
			this.guiKey = parentKey + HIERARCHY_SEPARATOR + GUI_KEY;
		}
		
		/**
		 * The translation for the main window.
		 */
		public static final class MainWindow {
			/** The root key for the main window translations */
			private static final String	MAIN_WINDOW_KEY	= "mainwindow";
			
			/** The key for the main window translation */
			private final String		mainWindowKey;
			
			/**
			 * Constructor #1.<br />
			 * @param parentKey
			 *        the key from the parent category.
			 */
			private MainWindow (final String parentKey) {
				super();
				mainWindowKey = parentKey + HIERARCHY_SEPARATOR + MAIN_WINDOW_KEY;
			}
			
			/**
			 * The translation for the title of the main window.
			 * @return the main window's title translation.
			 */
			public String title () {
				return mainWindowKey + HIERARCHY_SEPARATOR + "title";
			}
			
			/**
			 * The translation for the subtitle label.
			 * @return the subtitle label translation.
			 */
			public String subtitleLabel () {
				return mainWindowKey + HIERARCHY_SEPARATOR + "subtitleLabel";
			}
			
			/**
			 * The translation for the subtitle button.
			 * @return the subtitle button translation.
			 */
			public String subtitleButton () {
				return mainWindowKey + HIERARCHY_SEPARATOR + "subtitleButton";
			}
			
			/**
			 * The translation for the subtitle label.
			 * @return the subtitle label translation.
			 */
			public String strategyLabel () {
				return mainWindowKey + HIERARCHY_SEPARATOR + "strategyLabel";
			}
			
			/**
			 * The translation for the overwrite label.
			 * @return the overwrite label.
			 */
			public String overwriteLabel () {
				return mainWindowKey + HIERARCHY_SEPARATOR + "overwriteLabel";
			}
			
			/**
			 * The translation for the strategy parameter group.
			 * @return the title of the group for the strategy parameters buttons.
			 */
			public String strategyParameters () {
				return mainWindowKey + HIERARCHY_SEPARATOR + "strategyParameters";
			}
			
			/**
			 * The translation for the start correcting button.
			 * @return the start correcting button.
			 */
			public String startCorrectingButton () {
				return mainWindowKey + HIERARCHY_SEPARATOR + "startCorrectingButton";
			}
		}
		/**
		 * The main window translations.
		 * @return the main window translation.
		 */
		public MainWindow mainWindow () {
			return new MainWindow(guiKey);
		}
		
		/**
		 * The translation for the user prompt.
		 */
		public static final class UserPrompt {
			/** The root key for the user prompt */
			private static final String	USER_PROMPT_KEY	= "userprompt";
			
			/** The key for the user prompt translations */
			private final String		userPromptKey;
			
			/**
			 * Constructor #1.<br />
			 * @param parentKey
			 *        the key from the parent category.
			 */
			private UserPrompt (final String parentKey) {
				super();
				this.userPromptKey = parentKey + HIERARCHY_SEPARATOR + USER_PROMPT_KEY;
			}
			
			@Override
			public String toString () {
				return userPromptKey;
			}
			
			/**
			 * Return the translation for a replacement with the context.
			 * @return the translation for a replacement with the context.
			 */
			public String replaceWithContext () {
				return userPromptKey + HIERARCHY_SEPARATOR + "replacewithcontext";
			}
			
			/**
			 * Return the translation for a replacement with no context.
			 * @return the translation for a replacement with no context.
			 */
			public String replace () {
				return userPromptKey + HIERARCHY_SEPARATOR + "replace";
			}
			
			/**
			 * Return the translation for the remember choice message.
			 * @return the remember choice message.
			 */
			public String rememberChoice () {
				return userPromptKey + HIERARCHY_SEPARATOR + "rememberchoice";
			}
		}
		
		/**
		 * The user prompt translation.
		 * @return the GUI user prompt translation.
		 */
		public UserPrompt userPrompt () {
			return new UserPrompt(guiKey);
		}
	}
	
	/**
	 * The GUI translations
	 * @return the GUI translations.
	 */
	public Gui gui () {
		return new Gui(subtitleCorrectorKey);
	}
	
	/**
	 * The translation for the console.
	 */
	public static final class Console {
		/** The root key for the console */
		private static final String	CONSOLE_KEY	= "console";
		
		/** The key for the console translations */
		private final String		consoleKey;
		
		/**
		 * Constructor #1.<br />
		 * @param parentKey
		 *        the key from the parent category.
		 */
		private Console (final String parentKey) {
			super();
			this.consoleKey = parentKey + HIERARCHY_SEPARATOR + CONSOLE_KEY;
		}
		
		/**
		 * Return the abbreviation to use for "yes".
		 * @return the translation for yes.
		 */
		public String yes () {
			return consoleKey + HIERARCHY_SEPARATOR + "yes";
		}
		
		/**
		 * Return the abbreviation to use for "no".
		 * @return the translation for no.
		 */
		public String no () {
			return consoleKey + HIERARCHY_SEPARATOR + "no";
		}
		
		/**
		 * Return the prompt mark to use.
		 * @return the translation for the prompt mark.
		 */
		public String promptMark () {
			return consoleKey + HIERARCHY_SEPARATOR + "promptmark";
		}
		
		/**
		 * Return the yes/no prompt for the console.
		 * @return the translation for the yes/no prompt.
		 */
		public String yesNoPrompt () {
			return consoleKey + HIERARCHY_SEPARATOR + "yesnoprompt";
		}
		
		/**
		 * The translation for the user prompt.
		 */
		public static final class UserPrompt {
			/** The root key for the user prompt */
			private static final String	USER_PROMPT_KEY	= "userprompt";
			
			/** The key for the user prompt translations */
			private final String		userPromptKey;
			
			/**
			 * Constructor #1.<br />
			 * @param parentKey
			 *        the key from the parent category.
			 */
			private UserPrompt (final String parentKey) {
				super();
				this.userPromptKey = parentKey + HIERARCHY_SEPARATOR + USER_PROMPT_KEY;
			}
			
			@Override
			public String toString () {
				return userPromptKey;
			}
			
			/**
			 * Return the translation for the invalid choice message.
			 * @return the invalid choice message.
			 */
			public String invalidChoice () {
				return userPromptKey + HIERARCHY_SEPARATOR + "invalidchoice";
			}
			
			/**
			 * Return the translation for the replace message.
			 * @return the replace message.
			 */
			public String replace () {
				return userPromptKey + HIERARCHY_SEPARATOR + "replace";
			}
			
			/**
			 * Return the translation for the context message.
			 * @return the context message.
			 */
			public String context () {
				return userPromptKey + HIERARCHY_SEPARATOR + "context";
			}
			
			/**
			 * Return the translation for the change replacement message.
			 * @return the change replacement message.
			 */
			public String changeReplacement () {
				return userPromptKey + HIERARCHY_SEPARATOR + "changereplacement";
			}
			
			/**
			 * Return the translation for the remember choice message.
			 * @return the remember choice message.
			 */
			public String rememberChoice () {
				return userPromptKey + HIERARCHY_SEPARATOR + "rememberchoice";
			}
		}
		
		/**
		 * The user prompt translations.
		 * @return the user prompt translation.
		 */
		public UserPrompt userPrompt () {
			return new UserPrompt(consoleKey);
		}
		
		/**
		 * The translation for the console application.
		 */
		public static final class App {
			/** The root key for the user prompt */
			private static final String	APP_KEY	= "app";
			
			/** The key for the user prompt translations */
			private final String		appKey;
			
			/**
			 * Constructor #1.<br />
			 * @param parentKey
			 *        the key from the parent category.
			 */
			private App (final String parentKey) {
				super();
				this.appKey = parentKey + HIERARCHY_SEPARATOR + APP_KEY;
			}
			
			@Override
			public String toString () {
				return appKey;
			}
			
			/**
			 * Return the translation for the strategy parameters input message.
			 * @return the translation for the strategy parameters input message.
			 */
			public String strategyParametersInput () {
				return appKey + HIERARCHY_SEPARATOR + "strategyparametersinput";
			}
			
			/**
			 * Return the translation for the invalid parameter in strategy input.
			 * @return the translation for the invalid parameter in strategy input.
			 */
			public String strategyParametersInvalidValue () {
				return appKey + HIERARCHY_SEPARATOR + "strategyparametersinvalidvalue";
			}
			
			/**
			 * Return the translation for the subtitle write error.
			 * @return the translation for the subtitle write error.
			 */
			public String subtitleWriteError () {
				return appKey + HIERARCHY_SEPARATOR + "subtitlewriteerror";
			}
		}
		
		/**
		 * The console application translations.
		 * @return the console application translation.
		 */
		public App app () {
			return new App(consoleKey);
		}
	}
	
	/**
	 * The console translations.
	 * @return the console translation.
	 */
	public Console console () {
		return new Console(subtitleCorrectorKey);
	}
	
	/**
	 * Miscellaneous translations.
	 */
	public static final class Misc {
		/** The root key for the misc */
		private static final String	MISC_KEY	= "misc";
		
		/** The key for the console translations */
		private final String		miscKey;
		
		/**
		 * Constructor #1.<br />
		 * @param parentKey
		 *        the key from the parent category.
		 */
		private Misc (final String parentKey) {
			super();
			this.miscKey = parentKey + HIERARCHY_SEPARATOR + MISC_KEY;
		}
		
		/**
		 * The extension for corrected subtitles files.
		 * @return the extension for corrected subtitles files.
		 */
		public String fileExtension () {
			return miscKey + HIERARCHY_SEPARATOR + "fileExtension";
		}
		
	}
	
	/**
	 * The miscellaneous translations.
	 * @return the miscellaneous translations.
	 */
	public Misc misc () {
		return new Misc(subtitleCorrectorKey);
	}
}

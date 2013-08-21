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
	 * @author Alex
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
		 * Translations for the letter replacement strategy.
		 * @author Alex
		 */
		public static final class LetterReplacement {
			/** The root key for the letter replacement strategy */
			private static final String	LETTER_REPLACEMENT_KEY	= "letterreplacement";
			
			/** The key for the letter replacement strategy translations */
			private final String		letterReplacementKey;
			
			/**
			 * Constructor #1.<br />
			 * @param parentKey
			 *        the key from the parent category.
			 */
			private LetterReplacement (final String parentKey) {
				super();
				letterReplacementKey = parentKey + HIERARCHY_SEPARATOR + LETTER_REPLACEMENT_KEY;
			}
			
			@Override
			public String toString () {
				return letterReplacementKey;
			}
			
			/**
			 * The translation for the description of the strategy.
			 * @return the description of the strategy.
			 */
			public String description () {
				return letterReplacementKey + HIERARCHY_SEPARATOR + "description";
			}
			
			/**
			 * The translation for the original letter.
			 * @return the original letter.
			 */
			public String originalLetter () {
				return letterReplacementKey + HIERARCHY_SEPARATOR + "originalletter";
			}
			
			/**
			 * The translation for the new letter.
			 * @return the new letter.
			 */
			public String newLetter () {
				return letterReplacementKey + HIERARCHY_SEPARATOR + "newletter";
			}
			
			/**
			 * The translation for only missing from dictionary parameter.
			 * @return the only missing from dictionary parameter.
			 */
			public String onlyMissingFromDictionary () {
				return letterReplacementKey + HIERARCHY_SEPARATOR + "onlymissingfromdictionary";
			}
			
			/**
			 * The translation for prompt before correcting parameter.
			 * @return the prompt before correcting parameter.
			 */
			public String promptBeforeCorrecting () {
				return letterReplacementKey + HIERARCHY_SEPARATOR + "promptbeforecorrecting";
			}
		}
		
		/**
		 * The letter replacement translations.
		 * @return the letter replacement translations.
		 */
		public LetterReplacement letterReplacement () {
			return new LetterReplacement(strategyKey);
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
	 * The translation for the main window.
	 * @author Alex
	 */
	public static final class MainWindow {
		/** The root key for the main window translations */
		private static final String MAIN_WINDOW_KEY	= "mainwindow";
		
		/** The key for the main window translation */
		private final String mainWindowKey;
		
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
		
	}
	
	/**
	 * The main window translations.
	 * @return the main window translation.
	 */
	public MainWindow mainWindow () {
		return new MainWindow(subtitleCorrectorKey);
	}
}

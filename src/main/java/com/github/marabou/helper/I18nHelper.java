package com.github.marabou.helper;

import java.util.Locale;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class I18nHelper {

	static I18n i18n = I18nFactory.getI18n(I18nHelper.class, new Locale("De", "de"), org.xnap.commons.i18n.I18nFactory.FALLBACK);

	/**
	 * 
	 * Will ask i18n.tr for a translated version of the
	 * passed argument
	 * falls back to passed argument if no translation
	 * is found
	 * 
	 * @param str string to replace with translation
	 * @return returns translated string or given str
	 * if no translation is available 
	 */
	public static String _(String str) {
		return i18n.tr(str);
	}
}
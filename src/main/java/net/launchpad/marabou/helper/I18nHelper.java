/*
	Marabou Audio Tagger - A cross platform audio tagger using SWT
	
	Copyright (C) 2009  Jan-Hendrik Peters, Markus Herpich
	
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	      
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.launchpad.marabou.helper;

import java.util.Locale;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class I18nHelper {

	// TODO Release set the language in a property file, and read that one out here
	// String langCode =

	static I18n i18n = I18nFactory.getI18n(I18nHelper.class, new Locale("De",
			"de"), org.xnap.commons.i18n.I18nFactory.FALLBACK);

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
};
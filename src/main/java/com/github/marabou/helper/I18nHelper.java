/**
 * Marabou - Audio Tagger
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 * https://github.com/hennr/marabou
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.helper;

import java.util.Locale;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class I18nHelper {

    private static I18n i18n = I18nFactory.getI18n(I18nHelper.class, new Locale("De", "de"), org.xnap.commons.i18n.I18nFactory.FALLBACK);

    /**
     * Will ask i18n.tr for a translated version of the passed argument.
     * falls back to passed argument if no translation is found.
     */
    public static String i18n(String str) {
        return i18n.tr(str);
    }
}

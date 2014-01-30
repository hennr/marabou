package com.github.marabou.helper;

/**
 * allowed keys for marabou.properties
 */
public enum PropertiesAllowedKeys {
	
	// config's version
	version,
	// should we save the window size on exit?
	saveWindowSize,
	// the actual window size when user closes marabou
	windowSizeX,
	windowSizeY,
    // should we safe the last used path?
    safeLastPath,
    // the last path that the user opened
    lastPath,
    // size of the tag bar in relation to the table
    tagBarWeight,
    // size of the table in relation to the tag bar
    tableWeight;

}

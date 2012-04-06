package net.launchpad.marabou.helper;

/**
 * these are all allowed keys that can the stored or read in the marabou.properties file
 * 
 * @author Jan-Hendrik Peters
 *
 */
public enum PropertiesAllowedKeys {
	
	// marabou's version
	version,
	// the window size on close 
	saveWindowSize,
	// the actual window size when user closes marabou
	windowSizeX,
	windowSizeY,
	// the last path that the user opened
	lastPath,
	// should we safe the last used path?
	safeLastPath;

}

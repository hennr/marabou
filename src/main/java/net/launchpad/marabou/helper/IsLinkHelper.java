package net.launchpad.marabou.helper;

import java.io.File;
import java.io.IOException;

/**
 * Some utility class.
 * 
 * @author Jan-Hendrik Peters
 * 
 */
public class IsLinkHelper {

	/**
	 * Private constructor; since instantiation of a Utility Class makes no
	 * sense.
	 */
	private IsLinkHelper() {

	}

	/**
	 * 
	 * Tries to figure out if the given file object is a softlink link since
	 * java is not able to detect it itself This feature seems to be planed for
	 * java 7
	 * 
	 * *Note* This method will also return true if the file is non-existent!
	 * 
	 * 
	 * @param file
	 *            a file object to check
	 * @return true if file seems to be a softlink, false otherwise (also false
	 *         for hardlinks)
	 */
	public static boolean isLink(final File file) {
		try {
			if (!file.exists()) {
				return true;
			} else {
				String cnnpath = file.getCanonicalPath();
				String abspath = file.getAbsolutePath();
				return !abspath.equals(cnnpath);
			}
		} catch (IOException ex) {
			System.err.println(ex);
			return true;
		}
	}
}

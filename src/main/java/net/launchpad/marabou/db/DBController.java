package net.launchpad.marabou.db;

import java.io.File;
import java.io.IOException;

import net.launchpad.marabou.gui.TableShell;

import org.eclipse.swt.widgets.TabFolder;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * Abstract class defining database controller.
 * 
 * @author Markus Herpich
 * 
 */
public abstract class DBController {

	/**
	 * {@link TableShell} to put TableItems to.
	 */
	protected TableShell table;
	
	protected TabFolder tabFolder;
	
	private boolean tabFolderConnected = false;
	
	private boolean tableConnected = false;
	
	public void connectTabFolder(TabFolder tabFolder) {
		this.tabFolder = tabFolder;
		
	}
	
	public boolean isTabFolderConnected() {
		return tabFolderConnected;
	}
	
	public boolean isTableConnected() {
		return tableConnected;
	}
	
	

	/**
	 * Sets the {@link TableShell} to put TableItems to.
	 * 
	 * @param ts
	 *            {@link TableShell} to link with
	 */
	public final void connectTableShell(final TableShell ts) {
		this.table = ts;
		this.tableConnected = true;
	}

	/**
	 * Insert audio file into DB.
	 * 
	 * If you want the file to show up in the GUI you have to call addAllTableItems (or similar function) afterwards
	 * 
	 * @param audiofile
	 *            File object to be inserted
	 * @return affected rows
	 * @throws InvalidDataException if file is not a supported file type or if it's corrupt
	 * @throws IOException 
	 * @throws UnsupportedTagException 
	 */
	public abstract int insertFile(File audiofile) throws InvalidDataException, IOException, UnsupportedTagException;

	/**
	 * Set TableItem at given position.
	 * 
	 * @param filename
	 *            filename
	 * @return 
	 * @throws GUINotConnectedException 
	 */
	public abstract int addTableItemByFilename(String filename) throws GUINotConnectedException;

	/**
	 * Set all TableItems from DB.
	 * 
	 * @return 
	 * @throws GUINotConnectedException 
	 */
	public abstract int addAllTableItems() throws GUINotConnectedException;
	
	public abstract void saveFile(int idx);
	
	public abstract void updateTableItemById(int id) throws GUINotConnectedException;
	
	public abstract void deleteTableItemById(int id) throws GUINotConnectedException;

	public abstract void updateDBandTable() throws GUINotConnectedException;
	
	public abstract void updateItem(int id) throws GUINotConnectedException;

	/**
	 * gets selected files from the table and saves them
	 */
	public abstract boolean saveSelectedFiles() throws GUINotConnectedException;
}

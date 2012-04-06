/*
	Marabou Audio Tagger - A cross platform audio tagger using SWT
	
	Copyright (C) 2009-2010  Jan-Hendrik Peters, Markus Herpich
	
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

package net.launchpad.marabou.gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class FilesystemTab {

	/**
	 * 
	 * Reads in the filesystem and returns it as a tree object
	 * 
	 * @param tabFolder
	 *            The tabFoler which should hold the tree
	 * @return a tree item which holds the filesystem structure
	 * 
	 *         TODO create init2 and let the user choose which Tree style he
	 *         likes (init2 should implement a NortonCommander style)
	 */
	public static Tree init(TabFolder tabFolder) {

		// TODO if only one rootdir is available expand it
		final Tree tree = new Tree(tabFolder, SWT.BORDER);

		buildUp(tree);
		
		// add keylistener for the filetree
		tree.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				// update tree
				if (e.keyCode == SWT.F5) {
					rebuildTree(tree);
				}
			}
		});

		tree.addTreeListener(new TreeListener() {
			@Override
			public void treeExpanded(TreeEvent e) {
				final TreeItem root = (TreeItem) e.item;
				TreeItem[] treeItems = root.getItems();
				// check if we have permission to access the files
				for (TreeItem ti : treeItems) {
					if (ti.getData() != null) {
						return;
					} else {
						ti.dispose();
					}
					File file = (File) root.getData();
					File[] files = file.listFiles();
					
					if (files == null) {
						return;
					} else {
						// TODO option to ignore case in/sensitivity
						java.util.Arrays.sort(files);
						for (File f : files) {
							// TODO add "show/hide hidden files" to the options
							if (!f.getName().startsWith(".")) {
								TreeItem item = new TreeItem(root, SWT.NONE);
								item.setText(f.getName());
								item.setData(f);
								// new dir
								if (f.isDirectory()) {
									new TreeItem(item, SWT.None);
								}
							}
						}
					}
				}
			}

			@Override
			public void treeCollapsed(TreeEvent e) {
				// nothing to do
			}
		});
		return tree;
	}

	// helper methods
	
	private static void buildUp(final Tree tree) {
		// getting all root dirs
		File[] roots = File.listRoots();
		for (File rootFile : roots) {
			TreeItem root = new TreeItem(tree, SWT.None);
			root.setText(rootFile.toString());
			root.setData(rootFile);
			new TreeItem(root, SWT.NONE);
		}
	}
	// TODO keep current expanded entries
	/** 
	 * rebuilds the whole tree to make changes in the filesystem visible
	 * */
	protected static void rebuildTree(Tree tree) {
		tree.removeAll();
		buildUp(tree);
	}
}

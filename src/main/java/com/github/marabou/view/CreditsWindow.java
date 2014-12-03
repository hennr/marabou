package com.github.marabou.view;

import java.awt.Desktop;
import java.net.URI;

import com.github.marabou.helper.AvailableImage;
import com.github.marabou.helper.ImageLoader;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import static com.github.marabou.helper.I18nHelper._;

public class CreditsWindow {

	/**
	 * shows the devs, translators and libs that marabou uses
	 * all text comes from the translation files
	 */
	public static void showCredits() {
		final Display display = Display.getCurrent();

		final Shell shell = new Shell(display);
		shell.setText(_("Credits"));
		shell.setImage(new ImageLoader().getImage(AvailableImage.LOGO_SMALL));

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;

		shell.setLayout(gridLayout);

		// close window on ESC
		shell.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_ESCAPE) {
					shell.dispose();
				}
			}
		});

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);

		Composite devsComp = new Composite(tabFolder, SWT.NONE);

		Composite transComp = new Composite(tabFolder, SWT.NONE);

		Composite depsComp = new Composite(tabFolder, SWT.NONE);

		TabItem codersTab = new TabItem(tabFolder, SWT.None);
		codersTab.setText(_("Developers"));
		codersTab.setControl(devsComp);

		TabItem translatorsTab = new TabItem(tabFolder, SWT.None);
		translatorsTab.setText(_("Translators"));
		translatorsTab.setControl(transComp);

		TabItem dependenciesTab = new TabItem(tabFolder, SWT.None);
		dependenciesTab.setText(_("Dependencies"));
		dependenciesTab.setControl(depsComp);

		Link developers = new Link(devsComp, SWT.None);
		developers.setText(_("\n\n\n\n Jan-Hendrik Peters  <a>hennr@hennr.name</a>"));
		developers.pack();
		// listener to open mail client for the E-Mail-address clicked
		developers.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().mail(new URI("mailto", event.text, null));
                    } catch (Exception e) {
                        // no mail client found, well...
                    }
                }
			}});

		Link translators = new Link(transComp, SWT.None);
		translators.setText(_("\n\n\n English:\n Jan-Hendrik Peters  <a>hennr@hennr.name</a>"));
		translators.pack();

		Link depencies = new Link(depsComp, SWT.None);
		depencies.setText(_(
				"Marabou devs want to thank the following projects and persons:\n\n" +
				"SWT for the gui; " + "EPL; " + "<a>http://www.eclipse.org/swt/</a>\n" +
				"Michael Patricios for mp3agic as our backend libraby; " + "GPL 3; " + "<a>https://github.com/mpatric/mp3agic/</a>\n" +
				"junit for our unit tests; " + "CPL; " + "<a>http://junit.sourceforge.net/</a>\n" +
				"gettext-commons for internationalization; " + "Apache licence; " + "<a>http://code.google.com/p/gettext-commons/</a>\n" +
				"Tango Icon Library; " + "Public Domain; " + "<a>http://tango.freedesktop.org/Tango_Icon_Library</a>\n" +
				"HSQLDB; " + "BSD based licence; " + "<a>http://hsqldb.org/</a>\n" +
				"maven as a great build tool and dependency resolver; " + "Apache licence; " + "<a>http://maven.apache.org/</a>\n")+
				"Dries Bargheer for the logo; " + "CC-BY-SA" + "\n"
				);
		depencies.pack();

		Button close = new Button(shell, SWT.NONE);
		close.setText(_("&Close"));
		close.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
			shell.dispose();
			}
		});
		close.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		close.pack();
		shell.pack();
		shell.open();
		// close as well if display gets disposed
		while (!shell.isDisposed() && display.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}

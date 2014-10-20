package com.qualityeclipse.genealogy.view;

import java.io.*;

import org.eclipse.draw2d.*;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

import com.qualityeclipse.genealogy.model.GenealogyGraph;
import com.qualityeclipse.genealogy.model.io.*;
import com.qualityeclipse.genealogy.parts.GenealogyEditPartFactory;

/**
 * A example view displaying genealogy using the Draw2D and GEF frameworks, as opposed to
 * the {@link GenealogyView} which only uses the Draw2D framework.
 */
public class GenealogyViewGEF extends ViewPart
{
	private ScrollingGraphicalViewer viewer;
	private GenealogyGraph graph;

	/**
	 * Add a GEF viewer on which the diagram is rendered showing figures representing the
	 * various people and their relationships.
	 * 
	 * @param parent the composite to which the diagram is added
	 */
	private FigureCanvas createDiagram(Composite parent) {
		viewer = new ScrollingGraphicalViewer();
		viewer.createControl(parent);
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.getControl().setBackground(ColorConstants.white);
		viewer.setEditPartFactory(new GenealogyEditPartFactory());
		return (FigureCanvas) viewer.getControl();
	}

	/**
	 * Set the model being displayed
	 * @param newGraph the graph to be displayed
	 */
	private void setModel(GenealogyGraph newGraph) {
		graph = newGraph;
		viewer.setContents(graph);
	}

	//=============================================
	// View Part

	public void createPartControl(Composite parent) {
		createDiagram(parent);
		readAndClose(getClass().getResourceAsStream("genealogy.xml"));
	}

	public void setFocus() {
	}

	//=============================================
	// Standalone Shell
	
	/**
	 * The main application entry point
	 */
	public static void main(String[] args) {
		new GenealogyViewGEF().run();
	}

	/**
	 * Create, initialize, and run the shell.
	 * Call createDiagram to create the Draw2D diagram.
	 */
	private void run() {
		Shell shell = new Shell(new Display());
		shell.setSize(600, 500);
		shell.setText("Genealogy (GEF)");
		shell.setLayout(new GridLayout());
		
		FigureCanvas canvas = createDiagram(shell);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		createMenuBar(shell);
		
		// Show some default content
		readAndClose(getClass().getResourceAsStream("genealogy.xml"));
		
		Display display = shell.getDisplay();
		shell.open();
		while (!shell.isDisposed()) {
			while (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create a menu bar containing "File" and "Zoom" menus.
	 * 
	 * @param shell the shell
	 */
	private void createMenuBar(Shell shell) {

		// Create menu bar with "File" and "Zoom" menus
		final Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem fileMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuItem.setText("File");
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuItem.setMenu(fileMenu);
		
		// Create the File menu items
		createOpenFileMenuItem(fileMenu);
		createSaveFileMenuItem(fileMenu);
	}

	/**
	 * Create an "Open..." menu item
	 * @param menu the menu to contain the menu item
	 */
	private void createOpenFileMenuItem(Menu menu) {
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText("Open...");
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				openFile();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	/**
	 * Prompt the user for a file, and if one is selected,
	 * load the information from that file into the genealogy graph
	 */
	private void openFile() {
		Shell shell = Display.getDefault().getActiveShell();
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setText("Select a Genealogy Graph File");
		String path = dialog.open();
		if (path == null)
			return;
		try {
			readAndClose(new FileInputStream(path));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load information from the specified stream and update the genealogy graph with that
	 * information.
	 * 
	 * @param stream the stream (not <code>null</code>)
	 */
	private void readAndClose(InputStream stream) {
		GenealogyGraph newGraph = new GenealogyGraph();
		try {
			new GenealogyGraphReader(newGraph).read(stream);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		finally {
			try {
				stream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		setModel(newGraph);
	}

	/**
	 * Create a "Save..." menu item
	 * @param menu the menu to contain the menu item
	 */
	private void createSaveFileMenuItem(Menu menu) {
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText("Save...");
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				saveFile();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	/**
	 * Prompt the user for a file, and if one is selected,
	 * save the information from the genealogy graph into that file.
	 */
	private void saveFile() {
		Shell shell = Display.getDefault().getActiveShell();
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setText("Save Genealogy Graph");
		String path = dialog.open();
		if (path == null)
			return;
		File file = new File(path);
		if (file.exists()) {
			if (!MessageDialog.openQuestion(shell, "Overwrite?", "Overwrite the existing file?\n" + path))
				return;
		}
		PrintWriter writer;
		try {
			writer = new PrintWriter(file);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		try {
			new GenealogyGraphWriter(graph).write(writer);
		}
		finally {
			writer.close();
		}
	}
}

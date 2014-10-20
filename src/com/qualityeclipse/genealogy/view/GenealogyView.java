package com.qualityeclipse.genealogy.view;

import java.io.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;

import com.qualityeclipse.genealogy.model.GenealogyGraph;
import com.qualityeclipse.genealogy.model.io.GenealogyGraphReader;
import com.qualityeclipse.genealogy.model.io.GenealogyGraphWriter;

/**
 * A example view displaying genealogy using the Draw2D frameworks
 */
public class GenealogyView extends ViewPart
{
	private ScalableFreeformLayeredPane root;
	private FreeformLayer primary;
	private ConnectionLayer connections;
	private GenealogyGraph graph;
	private GenealogyGraphAdapter graphAdapter;

	/**
	 * Add a canvas on which the diagram is rendered showing figures representing the
	 * various people and their relationships.
	 * 
	 * @param parent the composite to which the diagram is added
	 */
	private FigureCanvas createDiagram(Composite parent) {

		// Create a layered pane along with primary and connection layers
		root = new ScalableFreeformLayeredPane();
		root.setFont(parent.getFont());

		primary = new FreeformLayer();
		primary.setLayoutManager(new FreeformLayout());
		root.add(primary, "Primary");

		connections = new ConnectionLayer();
		connections.setConnectionRouter(new ShortestPathConnectionRouter(primary));
		root.add(connections, "Connections");

		// Create the canvas and use it to show the root figure
		FigureCanvas canvas = new FigureCanvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setViewport(new FreeformViewport());
		canvas.setBackground(ColorConstants.white);
		canvas.setContents(root);
		return canvas;
	}

	/**
	 * Set the model being displayed
	 * @param newGraph the graph to be displayed
	 */
	private void setModel(GenealogyGraph newGraph) {
		if (graph != null) {
			graphAdapter.dispose();
			graphAdapter.graphCleared();
			graph = null;
		}
		if (newGraph != null) {
			graph = newGraph;
			graphAdapter = new GenealogyGraphAdapter(graph, primary, connections);
		}
	}

	//=============================================
	// View Part

	public void createPartControl(Composite parent) {
		createDiagram(parent);
		
		// Show some default content
		readAndClose(getClass().getResourceAsStream("genealogy.xml"));
	}

	public void setFocus() {
	}

	public void dispose() {
		setModel(null);
		super.dispose();
	}

	//=============================================
	// Standalone Shell
	
	/**
	 * The main application entry point
	 */
	public static void main(String[] args) {
		new GenealogyView().run();
	}

	/**
	 * Create, initialize, and run the shell.
	 * Call createDiagram to create the Draw2D diagram.
	 */
	private void run() {
		Shell shell = new Shell(new Display());
		shell.setSize(600, 500);
		shell.setText("Genealogy");
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
		MenuItem zoomMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		zoomMenuItem.setText("Zoom");
		Menu zoomMenu = new Menu(shell, SWT.DROP_DOWN);
		zoomMenuItem.setMenu(zoomMenu);
		
		// Create the File menu items
		createOpenFileMenuItem(fileMenu);
		createSaveFileMenuItem(fileMenu);
		
		// Create the "fixed" scale menu items
		createFixedZoomMenuItem(zoomMenu, "50%", 0.5);
		createFixedZoomMenuItem(zoomMenu, "100%", 1);
		createFixedZoomMenuItem(zoomMenu, "200%", 2);
		
		// Add "Scale to fit" menu item
		createScaleToFitMenuItem(zoomMenu);
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

	/**
	 * Create a new item in the Zoom menu with the specified scale setting
	 * @param menu the menu
	 * @param text the new item's text
	 * @param scale the scale setting
	 */
	private void createFixedZoomMenuItem(Menu menu, String text, final double scale) {
		MenuItem menuItem;
		menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText(text);
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				root.setScale(scale);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	/**
	 * Create a new item in the Zoom menu to scale the drawing
	 * such that the entire diagram is visible in the viewport
	 * @param menu the menu
	 */
	private void createScaleToFitMenuItem(Menu menu) {
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText("Scale to fit");
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				scaleToFit();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	/**
	 * Adjust the scale so that the entire diagram fills the viewport
	 */
	private void scaleToFit() {
		FreeformViewport viewport = (FreeformViewport) root.getParent();
		Rectangle viewArea = viewport.getClientArea();
		
		root.setScale(1);
		Rectangle extent = root.getFreeformExtent().union(0, 0);
		
		double wScale = ((double) viewArea.width) / extent.width;
		double hScale = ((double) viewArea.height) / extent.height;
		double newScale = Math.min(wScale, hScale);
		
		root.setScale(newScale);
	}
}

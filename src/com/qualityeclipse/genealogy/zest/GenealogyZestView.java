package com.qualityeclipse.genealogy.zest;

import java.io.*;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.*;
import org.eclipse.zest.layouts.algorithms.*;

import com.qualityeclipse.genealogy.model.*;
import com.qualityeclipse.genealogy.model.Person.Gender;
import com.qualityeclipse.genealogy.model.io.GenealogyGraphReader;

public class GenealogyZestView extends ViewPart
{
	private GraphViewer viewer;

	/**
	 * Instantiate a {@link GraphViewer} on which the diagram is rendered showing figures
	 * representing the various people and their relationships.
	 * 
	 * @param parent the composite to which the diagram is added
	 * @return the control displaying the diagram
	 */
	private Control createDiagram(Composite parent) {
		viewer = new GraphViewer(parent, SWT.NONE);
		viewer.setContentProvider(new GenealogyZestContentProvider1());
		Color blue = viewer.getGraphControl().LIGHT_BLUE;
		viewer.setLabelProvider(new GenealogyZestLabelProvider(blue));
		int style = LayoutStyles.NO_LAYOUT_NODE_RESIZING;
		viewer.setLayoutAlgorithm(new CompositeLayoutAlgorithm(style,
			new LayoutAlgorithm[]{
				new DirectedGraphLayoutAlgorithm(style),
				new HorizontalShift(style),
				new MarriageLayoutAlgorithm(style),
				new ShiftDiagramLayoutAlgorithm(style)
			}));
		new CustomFigureHighlightAdapter(viewer);
		return viewer.getControl();
	}

	/**
	 * Set the model being displayed
	 * 
	 * @param newGraph the graph to be displayed
	 */
	private void setModel(GenealogyGraph newGraph) {
		viewer.setInput(newGraph);
	}

	//=============================================
	// View Part

	public void createPartControl(Composite parent) {
		createDiagram(parent);

		// Show some default content
		readAndClose(getClass().getResourceAsStream("../view/genealogy.xml"));
	}

	public void setFocus() {
	}

	//=============================================
	// Standalone Shell

	/**
	 * The main application entry point
	 */
	public static void main(String[] args) {
		new GenealogyZestView().run();
	}

	/**
	 * Create, initialize, and run the shell. Call createDiagram to create the Zest based
	 * diagram.
	 */
	private void run() {
		Shell shell = new Shell(new Display());
		shell.setSize(600, 500);
		shell.setText("Genealogy (Zest)");
		shell.setLayout(new GridLayout());

		Control control = createDiagram(shell);
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		createMenuBar(shell);

		// Show some default content
		readAndClose(getClass().getResourceAsStream("../view/genealogy.xml"));

		Display display = shell.getDisplay();
		shell.open();
		while (!shell.isDisposed()) {
			while (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create and populate the menu bar
	 */
	private void createMenuBar(Shell shell) {

		// Create menu bar with "Filter" and "Layout" menus
		final Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem filterMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		filterMenuItem.setText("Filter");
		Menu filterMenu = new Menu(shell, SWT.DROP_DOWN);
		filterMenuItem.setMenu(filterMenu);
		MenuItem layoutMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		layoutMenuItem.setText("Layout");
		Menu layoutMenu = new Menu(shell, SWT.DROP_DOWN);
		layoutMenuItem.setMenu(layoutMenu);

		// Populate the "Filter" menu
		createFilterMenuItem(filterMenu, "Show Male Only", Gender.MALE);
		createFilterMenuItem(filterMenu, "Show Female Only", Gender.FEMALE);
		createFilterMenuItem(filterMenu, "Show Both", null);

		// Populate the "Layout" menu
		createLayoutMenuItem(layoutMenu, new DirectedGraphLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createCompositeLayoutMenuItem(layoutMenu,
			new DirectedGraphLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), new HorizontalShift(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createLayoutMenuItem(layoutMenu, new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createLayoutMenuItem(layoutMenu, new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createLayoutMenuItem(layoutMenu, new HorizontalLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createLayoutMenuItem(layoutMenu, new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createLayoutMenuItem(layoutMenu, new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createCompositeLayoutMenuItem(layoutMenu, new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),
			new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createLayoutMenuItem(layoutMenu, new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createLayoutMenuItem(layoutMenu, new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createCompositeLayoutMenuItem(layoutMenu, new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),
			new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		createLayoutMenuItem(layoutMenu, new VerticalLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
	}

	/**
	 * Create a new menu item for filtering the Zest diagram
	 * 
	 * @param menu the "Filter" menu item
	 * @param text the menu item text
	 * @param gender the gender to be displayed or <code>null</code> for both
	 */
	private void createFilterMenuItem(Menu menu, String text, final Gender gender) {
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText(text);
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				ViewerFilter[] filters;
				filters = gender != null ? new ViewerFilter[]{
					new GenealogyZestFilter(gender)
				} : new ViewerFilter[]{};
				viewer.setFilters(filters);
				viewer.applyLayout();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	/**
	 * Create a new menu item for changing the diagram's layout algorithm
	 * 
	 * @param menu the "Layout" menu
	 * @param layout the layout algorithm
	 */
	private void createLayoutMenuItem(Menu menu, final LayoutAlgorithm layout) {
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText(layout.getClass().getSimpleName());
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				viewer.setLayoutAlgorithm(layout, true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	/**
	 * Create a new menu item for changing the diagram's layout algorithm to be a
	 * composite of the two specified layout algorithms
	 * 
	 * @param menu the "Layout" menu
	 * @param layout1 the first layout algorithm
	 * @param layout2 the second layout algorithm
	 */
	private void createCompositeLayoutMenuItem(Menu menu, LayoutAlgorithm layout1, LayoutAlgorithm layout2) {
		final LayoutAlgorithm layout = new CompositeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING,
			new LayoutAlgorithm[]{
				layout1, layout2
			});
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText(layout1.getClass().getSimpleName() + " and " + layout2.getClass().getSimpleName());
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				viewer.setLayoutAlgorithm(layout, true);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
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
}

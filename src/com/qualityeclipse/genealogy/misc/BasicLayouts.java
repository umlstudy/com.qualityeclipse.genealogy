package com.qualityeclipse.genealogy.misc;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * A simple shell displaying some of the basic Draw2D layouts.
 * This is entirely separate from the {@link GenealogyView}, but included for completeness.
 */
public class BasicLayouts {

	private Figure root;
	private IFigure[] figures;

	/**
	 * Add a canvas on which the diagram is rendered.
	 * 
	 * @param parent the composite to which the diagram is added
	 */
	private Canvas createDiagram(Composite parent) {

		// Create a root figure and simple layout to contain all other figures
		root = new Figure();
		root.setFont(parent.getFont());
		
		// Create figures
		figures = new IFigure[4];
		figures[0] = createFigure("One");
		figures[1] = createFigure("Two");
		figures[2] = createFigure("Three");
		figures[3] = createFigure("Four");
		
		setToXYLayout();

		// Create the canvas and LightweightSystem
		// and use it to show the root figure in the shell
		Canvas canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(ColorConstants.white);
		LightweightSystem lws = new LightweightSystem(canvas);
		lws.setContents(root);
		return canvas;
	}

	private IFigure createFigure(String text) {
		final Label figure = new Label(text);
		figure.setBorder(new LineBorder(1));
		root.add(figure);
		return figure;
	}

	protected void setToBorderLayout() {
		BorderLayout layout = new BorderLayout();
		root.setLayoutManager(layout);
		layout.setConstraint(figures[0], BorderLayout.TOP);
		layout.setConstraint(figures[1], BorderLayout.LEFT);
		layout.setConstraint(figures[2], BorderLayout.BOTTOM);
		layout.setConstraint(figures[3], BorderLayout.CENTER);
		layout.setHorizontalSpacing(10);
		layout.setVerticalSpacing(5);
	}
	
	protected void setToDelegatingLayout() {
		DelegatingLayout layout = new DelegatingLayout();
		root.setLayoutManager(layout);
		layout.setConstraint(figures[0], new Locator() {
			public void relocate(IFigure target) {
				target.setBounds(new Rectangle(0, 0, 100, 25));
			}
		});
		layout.setConstraint(figures[1], new Locator() {
			public void relocate(IFigure target) {
				target.setBounds(new Rectangle(25, 25, 100, 50));
			}
		});
		layout.setConstraint(figures[2], new Locator() {
			public void relocate(IFigure target) {
				target.setBounds(new Rectangle(50, 75, 100, 50));
			}
		});
		layout.setConstraint(figures[3], new Locator() {
			public void relocate(IFigure target) {
				target.setBounds(new Rectangle(100, 125, 100, 75));
			}
		});
	}
	
	protected void setToFlowLayout_Horizontal() {
		root.setLayoutManager(new FlowLayout());
	}
	
	protected void setToFlowLayout_Vertical() {
		FlowLayout layout = new FlowLayout(false);
		root.setLayoutManager(layout);
		// Optional layout settings 
		layout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
		layout.setMajorSpacing(5);
		layout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
		layout.setMinorSpacing(20);
	}
	
	protected void setToGridLayout_1() {
		root.setLayoutManager(new GridLayout(2, true));
	}
	
	protected void setToGridLayout_2() {
		GridLayout layout = new GridLayout(2, false);
		root.setLayoutManager(layout);	
		layout.setConstraint(figures[0], new GridData(GridData.FILL_BOTH));
		layout.setConstraint(figures[1], new GridData(GridData.FILL_HORIZONTAL));
		layout.setConstraint(figures[2], new GridData(GridData.FILL_VERTICAL));
		layout.setConstraint(figures[3], new GridData());
	}
	
	protected void setToStackLayout() {
		root.setLayoutManager(new StackLayout());
	}
	
	protected void setToToolbarLayout() {
		ToolbarLayout layout = new ToolbarLayout();
		root.setLayoutManager(layout);
		layout.setSpacing(10);
	}
	
	protected void setToXYLayout() {
		XYLayout layout = new XYLayout();
		root.setLayoutManager(layout);
		layout.setConstraint(figures[0], new Rectangle(10, 10, 100, 60));
		layout.setConstraint(figures[1], new Rectangle(80, 100, 140, 60));
		layout.setConstraint(figures[2], new Rectangle(150, 20, 100, 60));
		layout.setConstraint(figures[3], new Rectangle(240, 120, 100, 60));
	}

	//===============================================
	// Show the diagram in a shell

	/**
	 * The main application entry point
	 */
	public static void main(String[] args) {
		BasicLayouts mgr = new BasicLayouts();
		Shell shell = mgr.initShell();
		Canvas canvas = mgr.createDiagram(shell);
		canvas.setLayoutData(new org.eclipse.swt.layout.GridData(
				org.eclipse.swt.layout.GridData.FILL_BOTH));
		mgr.runShell(shell);
	}

	/**
	 * Initialize the shell
	 */
	private Shell initShell() {
		Shell shell = new Shell(new Display());
		shell.setSize(380, 275);
		shell.setText("Basic Draw2D Layouts");
		shell.setLayout(new org.eclipse.swt.layout.GridLayout());
		
		// Create menu bar with "Layout" menu
		final Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem layoutMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		layoutMenuItem.setText("Layout");
		Menu layoutMenu = new Menu(shell, SWT.DROP_DOWN);
		layoutMenuItem.setMenu(layoutMenu);
		
		// Populate "Layout" menu
		Collection<Method> sorted = new TreeSet<Method>(new Comparator<Method>() {
			public int compare(Method m1, Method m2) {
				return m1.getName().compareTo(m2.getName());
			}
		});
		for (Method method : getClass().getDeclaredMethods())
			if (method.getName().startsWith("setTo"))
				sorted.add(method);
		for (final Method method : sorted) {
			MenuItem menuItem = new MenuItem(layoutMenu, SWT.NULL);
			menuItem.setText(method.getName().substring(5).replace('_', ' '));
			menuItem.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					try {
						method.invoke(BasicLayouts.this, new Object[] {});
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});
		}
		
		return shell;
	}

	/**
	 * Show the shell and process events
	 * 
	 * @param shell the shell (not <code>null</code>)
	 */
	private void runShell(Shell shell) {
		Display display = shell.getDisplay();
		shell.open();
		while (!shell.isDisposed()) {
			while (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}

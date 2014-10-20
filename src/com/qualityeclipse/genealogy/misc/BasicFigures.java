package com.qualityeclipse.genealogy.misc;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.qualityeclipse.genealogy.view.GenealogyView;

/**
 * A simple shell displaying some of the basic Draw2D figures.
 * This is entirely separate from the {@link GenealogyView}, but included for completeness.
 */
public class BasicFigures {

	/**
	 * Add a canvas on which the diagram is rendered.
	 * 
	 * @param parent the composite to which the diagram is added
	 */
	private static Canvas createDiagram(Composite parent) {

		// Create a root figure and simple layout to contain all other figures
		Figure root = new Figure();
		root.setFont(parent.getFont());
		root.setLayoutManager(new XYLayout());
		
		// ************ Shapes
		
		RectangleFigure rectangleFigure = new RectangleFigure();
		rectangleFigure.setBackgroundColor(ColorConstants.lightGray);
		rectangleFigure.setPreferredSize(60, 40);
		root.add(rectangleFigure, new Rectangle(new Point(10, 10),
				rectangleFigure.getPreferredSize()));

		RoundedRectangle roundedRectangle = new RoundedRectangle();
		roundedRectangle.setCornerDimensions(new Dimension(10, 10));
		roundedRectangle.setPreferredSize(60, 40);
		root.add(roundedRectangle, new Rectangle(new Point(80, 10),
				roundedRectangle.getPreferredSize()));

		Ellipse ellipse = new Ellipse();
		ellipse.setBackgroundColor(ColorConstants.lightGray);
		ellipse.setPreferredSize(60, 40);
		root.add(ellipse, new Rectangle(new Point(150, 10),
				ellipse.getPreferredSize()));
		
		Triangle triangle = new Triangle();
		root.add(triangle, new Rectangle(220, 10, 60, 40));
		
		Polygon polygon = new Polygon();
		polygon.addPoint(new Point(290, 10));
		polygon.addPoint(new Point(350, 10));
		polygon.addPoint(new Point(390, 50));
		polygon.addPoint(new Point(330, 50));
		polygon.setFill(true);
		polygon.setBackgroundColor(ColorConstants.lightGray);
		root.add(polygon, new Rectangle(polygon.getStart(), polygon.getPreferredSize()));
		
		Polyline line = new Polyline();
		line.addPoint(new Point(400, 10));
		line.addPoint(new Point(460, 10));
		line.addPoint(new Point(440, 50));
		line.addPoint(new Point(500, 50));
		root.add(line, new Rectangle(line.getStart(), line.getPreferredSize()));
		
		// ********************* Clickable

		ArrowButton arrowButton = new ArrowButton(PositionConstants.EAST);
		root.add(arrowButton, new Rectangle(10, 60, 20, 20));
		
		Button button = new Button("This is a Button");
		root.add(button, new Rectangle(40, 60, 100, 20));

		CheckBox checkbox = new CheckBox("This is a Checkbox");
		root.add(checkbox, new Rectangle(150, 60, 150, 20));
		
		// ********************** Others

		Label label = new Label("This is a label");
		label.setBorder(new LineBorder(1));
		root.add(label, new Rectangle(10, 90, 100, 20));

		final Image image = ImageUtilities.createRotatedImageOfString("Rotated Text",
				new Font(Display.getCurrent(), "Courier New", 12, SWT.NORMAL),
				ColorConstants.black, ColorConstants.white);
		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				image.dispose();
			}
		});
		ImageFigure imageFigure = new ImageFigure(image);
		root.add(imageFigure, new Rectangle(10, 120, 100, 200));

		label = new Label("The \"Rotated Text\" is displayed using\n   ImageFigure\nand\n   ImageUtilities.createRotatedImageOfString(...)");
		root.add(label, new Rectangle(110, 120, 280, 200));

		// Create the canvas and LightweightSystem
		// and use it to show the root figure in the shell
		Canvas canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(ColorConstants.white);
		LightweightSystem lws = new LightweightSystem(canvas);
		lws.setContents(root);
		return canvas;
	}

	//===============================================
	// Show the diagram in a shell

	/**
	 * The main application entry point
	 */
	public static void main(String[] args) {
		Shell shell = initShell();
		Canvas canvas = createDiagram(shell);
		canvas.setLayoutData(new org.eclipse.swt.layout.GridData(
				org.eclipse.swt.layout.GridData.FILL_BOTH));
		runShell(shell);
	}

	/**
	 * Initialize the shell
	 */
	private static Shell initShell() {
		Shell shell = new Shell(new Display());
		shell.setSize(600, 400);
		shell.setText("Basic Draw2D Figures");
		shell.setLayout(new org.eclipse.swt.layout.GridLayout());
		return shell;
	}

	/**
	 * Show the shell and process events
	 * 
	 * @param shell the shell (not <code>null</code>)
	 */
	private static void runShell(Shell shell) {
		Display display = shell.getDisplay();
		shell.open();
		while (!shell.isDisposed()) {
			while (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}

package com.qualityeclipse.genealogy.misc;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.qualityeclipse.genealogy.listener.FigureMover;
import com.qualityeclipse.genealogy.view.GenealogyView;

/**
 * A simple shell displaying some of the basic Draw2D decorations. This is
 * entirely separate from the {@link GenealogyView}, but included for
 * completeness.
 */
public class BasicDecorations {

	/**
	 * Add a canvas on which the diagram is rendered.
	 * 
	 * @param parent
	 *            the composite to which the diagram is added
	 */
	private Canvas createDiagram(Composite parent) {

		// Create a root figure and simple layout to contain all other figures
		IFigure root = new Figure();
		root.setFont(parent.getFont());
		root.setLayoutManager(new XYLayout());

		addSmallPolygonArrowheads(root);
		addSmallPolylineArrowhead(root);
		addSmallPolygonDiamond(root);
		addSmallPolylineDiamond(root);

		// Create the canvas and LightweightSystem
		// and use it to show the root figure in the shell
		Canvas canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(ColorConstants.white);
		LightweightSystem lws = new LightweightSystem(canvas);
		lws.setContents(root);
		return canvas;
	}

	private void addSmallPolygonArrowheads(IFigure root) {
		PolylineConnection conn = newFigureAndConnection(root,
				"Small Polygon Arrowheads", 10, 10);

		// Set the source decoration
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(PolygonDecoration.INVERTED_TRIANGLE_TIP);
		conn.setSourceDecoration(decoration);

		// Set the target decoration
		conn.setTargetDecoration(new PolygonDecoration());
	}

	private void addSmallPolylineArrowhead(IFigure root) {
		PolylineConnection conn = newFigureAndConnection(root,
				"Small Polyline Arrowheads", 10, 110);

		// Set the target decoration
		conn.setTargetDecoration(new PolylineDecoration());
	}

	private void addSmallPolygonDiamond(IFigure root) {
		PolylineConnection conn = newFigureAndConnection(root,
				"Polygon Diamond", 230, 10);

		// Set the target decoration
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setBackgroundColor(ColorConstants.blue);
		PointList points = new PointList();
		points.addPoint(0, 0);
		points.addPoint(-1, 1);
		points.addPoint(-2, 0);
		points.addPoint(-1, -1);
		points.addPoint(0, 0);
		decoration.setTemplate(points);
		conn.setTargetDecoration(decoration);
	}

	private void addSmallPolylineDiamond(IFigure root) {
		PolylineConnection conn = newFigureAndConnection(root,
				"Polyline Diamond", 230, 110);

		// Set the target decoration
		PolylineDecoration decoration = new PolylineDecoration();
		PointList points = new PointList();
		points.addPoint(0, 0);
		points.addPoint(-1, 1);
		points.addPoint(-2, 0);
		points.addPoint(-1, -1);
		points.addPoint(0, 0);
		decoration.setTemplate(points);
		conn.setTargetDecoration(decoration);
	}

	private PolylineConnection newFigureAndConnection(IFigure root,
			String text, int x, int y) {
		Ellipse ellipse = new Ellipse();
		ellipse.setBorder(new MarginBorder(10));
		ellipse.setLayoutManager(new GridLayout());
		ellipse.add(new Label(text));
		ellipse.setFont(root.getFont());
		root.add(ellipse, new Rectangle(new Point(x + 30, y + 20), ellipse
				.getPreferredSize()));
		new FigureMover(ellipse);

		PolylineConnection conn = new PolylineConnection();
		conn.setSourceAnchor(new XYAnchor(new Point(x, y)));
		conn.setTargetAnchor(new EllipseAnchor(ellipse));
		root.add(conn);
		return conn;
	}

	// ===============================================
	// Show the diagram in a shell

	/**
	 * The main application entry point
	 */
	public static void main(String[] args) {
		BasicDecorations mgr = new BasicDecorations();
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
		shell.setSize(480, 325);
		shell.setText("Basic Draw2D Decorations");
		shell.setLayout(new org.eclipse.swt.layout.GridLayout());
		return shell;
	}

	/**
	 * Show the shell and process events
	 * 
	 * @param shell
	 *            the shell (not <code>null</code>)
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

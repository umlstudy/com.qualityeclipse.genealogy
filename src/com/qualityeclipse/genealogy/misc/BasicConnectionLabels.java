package com.qualityeclipse.genealogy.misc;

import java.util.Arrays;

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
 * A simple shell displaying some of the basic Draw2D connection routers. This
 * is entirely separate from the {@link GenealogyView}, but included for
 * completeness.
 */
public class BasicConnectionLabels {

	private static final PointList ARROWHEAD = new PointList(new int[] { 0, 0,
			-2, 2, -2, 0, -2, -2, 0, 0 });

	/**
	 * Add a canvas on which the diagram is rendered.
	 * 
	 * @param parent
	 *            the composite to which the diagram is added
	 */
	private Canvas createDiagram(Composite parent) {

		// A root layer above the container layer
		// so that ShortestPathConnectionRouter can be used
		IFigure root = new Figure();
		root.setFont(parent.getFont());
		root.setLayoutManager(new StackLayout());

		// Create a container figure and simple layout to contain all other
		// figures
		IFigure container = new Figure();
		container.setLayoutManager(new XYLayout());
		root.add(container);

		showBendpointLocator(container);
		showConnectionEndpointLocator(container);
		showConnectionLocator(container);
		showMidpointLocator(container);

		// Create the canvas and LightweightSystem
		// and use it to show the container figure in the shell
		Canvas canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(ColorConstants.white);
		LightweightSystem lws = new LightweightSystem(canvas);
		lws.setContents(root);
		return canvas;
	}

	private void showBendpointLocator(IFigure container) {
		PolylineConnection connection = newFigureAndConnection(container,
				"BendpointLocator", 225, 10);
		BendpointConnectionRouter router = new BendpointConnectionRouter();

		// Bendpoint 1 - absolute
		AbsoluteBendpoint bp1 = new AbsoluteBendpoint(350, 10);

		// Bendpoint 2 - relative to 2 bendpoints
		RelativeBendpoint bp2 = new RelativeBendpoint(connection);
		bp2.setRelativeDimensions(new Dimension(-50, 20),
				new Dimension(10, -40));

		// Bendpoint 3 - relative to the ellipse
		RelativeBendpoint bp3 = new RelativeBendpoint(connection);
		bp3.setRelativeDimensions(new Dimension(0, 0), new Dimension(20, -45));
		bp3.setWeight(1);

		router.setConstraint(connection, Arrays.asList(new Bendpoint[] { bp1,
				bp2, bp3 }));
		connection.setConnectionRouter(router);

		connection.setLayoutManager(new DelegatingLayout());

		Label label;
		BendpointLocator locator;

		label = new Label("source");
		locator = new BendpointLocator(connection, 0);
		locator.setRelativePosition(PositionConstants.WEST);
		locator.setGap(5);
		connection.add(label, locator);

		label = new Label("1st");
		locator = new BendpointLocator(connection, 1);
		locator.setRelativePosition(PositionConstants.EAST);
		locator.setGap(5);
		connection.add(label, locator);

		label = new Label("2nd");
		locator = new BendpointLocator(connection, 2);
		locator.setRelativePosition(PositionConstants.WEST);
		locator.setGap(5);
		connection.add(label, locator);

		label = new Label("3rd");
		locator = new BendpointLocator(connection, 3);
		locator.setRelativePosition(PositionConstants.EAST);
		locator.setGap(5);
		connection.add(label, locator);

		label = new Label("target");
		locator = new BendpointLocator(connection, 4);
		locator.setRelativePosition(PositionConstants.NORTH_WEST);
		locator.setGap(5);
		connection.add(label, locator);
	}

	private void showConnectionEndpointLocator(IFigure container) {
		PolylineConnection connection = newFigureAndConnection(container,
				"ConnectionEndpointLocator", 25, 110);
		connection.setLayoutManager(new DelegatingLayout());

		Label label;
		ConnectionEndpointLocator locator;

		label = new Label("source");
		locator = new ConnectionEndpointLocator(connection, false);
		locator.setUDistance(2);
		locator.setVDistance(5);
		connection.add(label, locator);

		label = new Label("target");
		locator = new ConnectionEndpointLocator(connection, true);
		connection.add(label, locator);
	}

	private void showConnectionLocator(IFigure container) {
		PolylineConnection connection = newFigureAndConnection(container,
				"ConnectionLocator", 25, 10);
		connection.setLayoutManager(new DelegatingLayout());

		Label label;

		label = new Label("source");
		connection.add(label, new ConnectionLocator(connection,
				ConnectionLocator.SOURCE));

		label = new Label("middle");
		connection.add(label, new ConnectionLocator(connection,
				ConnectionLocator.MIDDLE));

		label = new Label("target");
		connection.add(label, new ConnectionLocator(connection,
				ConnectionLocator.TARGET));
	}

	private void showMidpointLocator(IFigure container) {
		PolylineConnection connection = newFigureAndConnection(container,
				"MidpointLocator", 425, 10);
		BendpointConnectionRouter router = new BendpointConnectionRouter();

		// Bendpoint 1 - absolute
		AbsoluteBendpoint bp1 = new AbsoluteBendpoint(550, 10);

		// Bendpoint 2 - relative to 2 bendpoints
		RelativeBendpoint bp2 = new RelativeBendpoint(connection);
		bp2.setRelativeDimensions(new Dimension(-50, 20),
				new Dimension(10, -40));

		// Bendpoint 3 - relative to the ellipse
		RelativeBendpoint bp3 = new RelativeBendpoint(connection);
		bp3.setRelativeDimensions(new Dimension(0, 0), new Dimension(20, -45));
		bp3.setWeight(1);

		router.setConstraint(connection, Arrays.asList(new Bendpoint[] { bp1,
				bp2, bp3 }));
		connection.setConnectionRouter(router);

		connection.setLayoutManager(new DelegatingLayout());
		for (int i = 0; i < 4; i++) {
			Label label = new Label("midpoint " + i);
			connection.add(label, new MidpointLocator(connection, i));
		}
	}

	private PolylineConnection newFigureAndConnection(IFigure container,
			String text, int x, int y) {
		Ellipse ellipse = newFigure(container, text, x, y);
		return newConnection(container, x, y, ellipse);
	}

	private Ellipse newFigure(IFigure container, String text, int x, int y) {
		Ellipse ellipse = new Ellipse();
		ellipse.setBorder(new MarginBorder(10));
		ellipse.setLayoutManager(new GridLayout());
		ellipse.add(new Label(text));
		ellipse.setFont(container.getFont());
		container.add(ellipse, new Rectangle(new Point(x + 30, y + 30), ellipse
				.getPreferredSize()));
		new FigureMover(ellipse);
		return ellipse;
	}

	private PolylineConnection newConnection(IFigure container, int x, int y,
			Ellipse ellipse) {
		XYAnchor sourceAnchor = new XYAnchor(new Point(x, y));
		EllipseAnchor targetAnchor = new EllipseAnchor(ellipse);
		return newConnection(container, sourceAnchor, targetAnchor);
	}

	private PolylineConnection newConnection(IFigure container,
			ConnectionAnchor sourceAnchor, ConnectionAnchor targetAnchor) {
		PolylineConnection conn = new PolylineConnection();
		conn.setSourceAnchor(sourceAnchor);
		conn.setTargetAnchor(targetAnchor);
		container.add(conn);

		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(ColorConstants.white);
		conn.setTargetDecoration(decoration);
		return conn;
	}

	// ===============================================
	// Show the diagram in a shell

	/**
	 * The main application entry point
	 */
	public static void main(String[] args) {
		BasicConnectionLabels mgr = new BasicConnectionLabels();
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
		shell.setSize(620, 400);
		shell.setText("Basic Draw2D Connection Labels");
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

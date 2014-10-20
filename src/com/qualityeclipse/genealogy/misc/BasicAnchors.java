package com.qualityeclipse.genealogy.misc;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.qualityeclipse.genealogy.anchors.CenterAnchor;
import com.qualityeclipse.genealogy.anchors.MarriageAnchor;
import com.qualityeclipse.genealogy.figures.MarriageFigure;
import com.qualityeclipse.genealogy.listener.FigureMover;
import com.qualityeclipse.genealogy.view.GenealogyView;

/**
 * A simple shell displaying some of the basic Draw2D anchors. This is entirely
 * separate from the {@link GenealogyView}, but included for completeness.
 */
public class BasicAnchors {

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

		// Add the common anchor examples
		addChopboxAnchor(root);
		addEllipseAnchor(root);
		addLabelAnchor(root);
		addXYAnchor(root);

		// Add the custom anchor examples
		addCenterAnchor(root);
		addMarriageAnchor(root);

		// Create the canvas and LightweightSystem
		// and use it to show the root figure in the shell
		Canvas canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(ColorConstants.white);
		LightweightSystem lws = new LightweightSystem(canvas);
		lws.setContents(root);
		return canvas;
	}

	private void addChopboxAnchor(IFigure root) {
		RectangleFigure rectangleFigure = new RectangleFigure();
		rectangleFigure.setLayoutManager(new GridLayout());
		rectangleFigure.add(new Label("ChopboxAnchor"));
		rectangleFigure.setFont(root.getFont());
		root.add(rectangleFigure, new Rectangle(new Point(40, 30),
				rectangleFigure.getPreferredSize()));
		new FigureMover(rectangleFigure);

		Connection conn = new PolylineConnection();
		conn.setSourceAnchor(new XYAnchor(new Point(10, 10)));
		conn.setTargetAnchor(new ChopboxAnchor(rectangleFigure));
		root.add(conn);
	}

	private void addEllipseAnchor(IFigure root) {
		Ellipse ellipse = new Ellipse();
		ellipse.setBorder(new MarginBorder(10));
		ellipse.setLayoutManager(new GridLayout());
		ellipse.add(new Label("EllipseAnchor"));
		ellipse.setFont(root.getFont());
		root.add(ellipse, new Rectangle(new Point(180, 25), ellipse
				.getPreferredSize()));
		new FigureMover(ellipse);

		Connection conn = new PolylineConnection();
		conn.setSourceAnchor(new XYAnchor(new Point(150, 10)));
		conn.setTargetAnchor(new EllipseAnchor(ellipse));
		root.add(conn);
	}

	private void addLabelAnchor(IFigure root) {
		Label label = new Label("LabelAnchor");
		label.setFont(root.getFont());
		root.add(label, new Rectangle(new Point(40, 130), label
				.getPreferredSize()));
		new FigureMover(label);

		Connection conn = new PolylineConnection();
		conn.setSourceAnchor(new XYAnchor(new Point(10, 110)));
		conn.setTargetAnchor(new LabelAnchor(label));
		root.add(conn);
	}

	private void addXYAnchor(IFigure root) {
		Label label = new Label("XYAnchor");
		label.setFont(root.getFont());
		root.add(label, new Rectangle(new Point(180, 130), label
				.getPreferredSize()));
		new FigureMover(label);

		Connection conn = new PolylineConnection();
		conn.setSourceAnchor(new XYAnchor(new Point(150, 110)));
		conn.setTargetAnchor(new XYAnchor(new Point(180, 130)));
		root.add(conn);
	}

	private void addCenterAnchor(IFigure root) {
		RectangleFigure rectangleFigure = new RectangleFigure();
		rectangleFigure.setLayoutManager(new GridLayout());
		rectangleFigure.add(new Label("CenterAnchor"));
		rectangleFigure.setFont(root.getFont());
		root.add(rectangleFigure, new Rectangle(new Point(40, 230),
				rectangleFigure.getPreferredSize()));
		new FigureMover(rectangleFigure);

		Connection conn = new PolylineConnection();
		conn.setSourceAnchor(new XYAnchor(new Point(10, 210)));
		conn.setTargetAnchor(new CenterAnchor(rectangleFigure));
		root.add(conn);
	}

	private void addMarriageAnchor(IFigure root) {
		IFigure marriage = new MarriageFigure(1942);
		marriage.setFont(root.getFont());
		root.add(marriage, new Rectangle(new Point(180, 230), marriage
				.getPreferredSize()));
		new FigureMover(marriage);

		Connection conn = new PolylineConnection();
		conn.setSourceAnchor(new XYAnchor(new Point(150, 210)));
		conn.setTargetAnchor(new MarriageAnchor(marriage));
		root.add(conn);
	}

	// ===============================================
	// Show the diagram in a shell

	/**
	 * The main application entry point
	 */
	public static void main(String[] args) {
		BasicAnchors mgr = new BasicAnchors();
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
		shell.setSize(380, 375);
		shell.setText("Basic Draw2D Anchors");
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

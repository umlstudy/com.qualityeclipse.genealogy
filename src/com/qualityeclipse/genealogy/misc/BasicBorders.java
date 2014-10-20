package com.qualityeclipse.genealogy.misc;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.qualityeclipse.genealogy.view.GenealogyView;

/**
 * A simple shell displaying some of the basic Draw2D borders.
 * This is entirely separate from the {@link GenealogyView}, but included for completeness.
 */
public class BasicBorders {

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
		
		// ************ Borders

		Label label;
		
		label = new Label("LineBorder");
		label.setBorder(new LineBorder(ColorConstants.blue, 3, Graphics.LINE_DASH));
		root.add(label, new Rectangle(10, 10, 100, 60));

		label = new Label("GroupBoxBorder");
		label.setBorder(new GroupBoxBorder("My Group"));
		root.add(label, new Rectangle(120, 10, 140, 60));

		label = new Label("FrameBorder");
		label.setBorder(new FrameBorder("My Title"));
		root.add(label, new Rectangle(270, 10, 100, 60));

		label = new Label("TitleBarBorder");
		label.setBorder(new TitleBarBorder("My Title"));
		root.add(label, new Rectangle(380, 10, 100, 60));

		label = new Label("SimpleEtchedBorder");
		label.setBorder(SimpleEtchedBorder.singleton);
		root.add(label, new Rectangle(10, 80, 150, 60));

		label = new Label("SimpleLoweredBorder");
		label.setBorder(new SimpleLoweredBorder(3));
		root.add(label, new Rectangle(170, 80, 150, 60));

		label = new Label("SimpleRaisedBorder");
		label.setBorder(new SimpleRaisedBorder(3));
		root.add(label, new Rectangle(330, 80, 150, 60));

		label = new Label("CompoundBorder\nwith GroupBoxBorder and MarginBorder");
		label.setBorder(new CompoundBorder(new GroupBoxBorder("My Group"), new MarginBorder(10)));
		root.add(label, new Rectangle(10, 150, 350, 120));

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
		shell.setText("Basic Draw2D Borders");
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

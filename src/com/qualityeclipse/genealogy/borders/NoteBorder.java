package com.qualityeclipse.genealogy.borders;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * A border with the left top corder "folded" over.
 */
public class NoteBorder extends AbstractBorder {
	public static final int FOLD = 10;
	
	private int lineWidth = 1;
	private Color lineColor = ColorConstants.black;

	public Insets getInsets(IFigure figure) {
		return new Insets(1, 2 + FOLD, 2, 2); // top, left, bottom, right
	}
	
	public void setLineWidth(int width) {
		lineWidth = width;
	}
	
	public void setLineColor(Color color) {
		lineColor = color;
	}

	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Rectangle r = figure.getBounds().getCopy();
		r.crop(insets);
		graphics.setForegroundColor(lineColor);
		graphics.setLineWidth(lineWidth);
		// solid long edges around border
		graphics.drawLine(r.x + FOLD, r.y, r.x + r.width - lineWidth, r.y);
		graphics.drawLine(r.x, r.y + FOLD, r.x, r.y + r.height - lineWidth);
		graphics.drawLine(r.x + r.width - lineWidth, r.y, r.x + r.width - lineWidth, r.y + r.height - lineWidth);
		graphics.drawLine(r.x, r.y + r.height - lineWidth, r.x + r.width - lineWidth, r.y + r.height - lineWidth);
		// solid short edges
		graphics.drawLine(r.x + FOLD, r.y, r.x + FOLD, r.y + FOLD);
		graphics.drawLine(r.x, r.y + FOLD, r.x + FOLD, r.y + FOLD);
		// gray small triangle
		graphics.setBackgroundColor(ColorConstants.lightGray);
		graphics.fillPolygon(new int[] { r.x, r.y + FOLD, r.x + FOLD, r.y,
				r.x + FOLD, r.y + FOLD });
		// dotted short diagonal line
		graphics.setLineStyle(SWT.LINE_DOT);
		graphics.drawLine(r.x, r.y + FOLD, r.x + FOLD, r.y);
	}
}

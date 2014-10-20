package com.qualityeclipse.genealogy.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.qualityeclipse.genealogy.borders.NoteBorder;

public class NoteFigure extends Label {

	public static final Image NOTE_IMAGE = new Image(Display.getCurrent(),
		MarriageFigure.class.getResourceAsStream("note.png"));

	public NoteFigure(String note) {
		super(note);
		setBorder(new NoteBorder());
	}
	
	protected void paintFigure(Graphics graphics) {
		graphics.setBackgroundColor(ColorConstants.white);
		Rectangle b = getBounds();
		final int fold = NoteBorder.FOLD;
		graphics.fillRectangle(b.x + fold, b.y, b.width - fold, fold);
		graphics.fillRectangle(b.x, b.y + fold, b.width, b.height - fold);
		super.paintFigure(graphics);
	}

	/**
	 * Adjust the receiver's appearance based upon whether the receiver is selected
	 * 
	 * @param selected <code>true</code> if the receiver is selected, else
	 *            <code>false</code>
	 */
	public void setSelected(boolean selected) {
		((NoteBorder) getBorder()).setLineColor(selected ? ColorConstants.blue : ColorConstants.black);
		((NoteBorder) getBorder()).setLineWidth(selected ? 2 : 1);
		erase();
	}
}

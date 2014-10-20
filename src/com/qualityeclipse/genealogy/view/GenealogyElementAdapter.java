package com.qualityeclipse.genealogy.view;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import com.qualityeclipse.genealogy.figures.*;
import com.qualityeclipse.genealogy.model.*;
import com.qualityeclipse.genealogy.model.listener.GenealogyElementListener;

/**
 * Provides common behavior for creating and managing a {@link PersonFigure},
 * {@link MarriageFigure}, or {@link NoteFigure} to represent an associated
 * {@link GenealogyElement} in a {@link GenealogyGraph}. As the {@link GenealogyElement}
 * is modified, this class and its subclasses update the appropriate figure to keep the
 * diagram in synch with the model.
 */
public abstract class GenealogyElementAdapter
	implements GenealogyElementListener, FigureListener
{
	private final GenealogyElement elem;
	private final IFigure figure;
	private GenealogyGraphAdapter graphAdapter;

	protected GenealogyElementAdapter(GenealogyElement elem, IFigure figure) {
		this.elem = elem;
		this.figure = figure;
		figure.setLocation(new Point(elem.getX(), elem.getY()));
		figure.setSize(elem.getWidth(), elem.getHeight());
		figure.addFigureListener(this);
	}

	public IFigure getFigure() {
		return figure;
	}

	public GenealogyGraphAdapter getGraphAdapter() {
		return graphAdapter;
	}

	public void setGraphAdapter(GenealogyGraphAdapter graphAdapter) {
		this.graphAdapter = graphAdapter;
	}

	public void locationChanged(int x, int y) {
		getFigure().setLocation(new Point(x, y));
	}

	public void sizeChanged(int width, int height) {
		getFigure().setSize(width, height);
	}

	public void figureMoved(IFigure source) {
		Rectangle r = source.getBounds();
		elem.setLocation(r.x, r.y);
		elem.setSize(r.width, r.height);
	}
	
	public abstract void dispose();
}

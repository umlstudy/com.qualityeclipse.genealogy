package com.qualityeclipse.genealogy.parts;

import java.util.Map;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.qualityeclipse.genealogy.model.*;
import com.qualityeclipse.genealogy.model.connection.GenealogyConnection;

/**
 * Shared behavior for several related {@link EditPart} subclasses such as
 * {@link PersonEditPart}.
 */
public abstract class GenealogyElementEditPart extends AbstractGraphicalEditPart
{
	/**
	 * Update the figure based upon the current model state
	 */
	protected void refreshVisuals() {
		GenealogyElement m = (GenealogyElement) getModel();
		Rectangle bounds = new Rectangle(m.getX(), m.getY(), m.getWidth(), m.getHeight());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
		super.refreshVisuals();
	}

	/**
	 * Find an existing connection between a person and marriage
	 * 
	 * @param p the person
	 * @param m the marriage
	 * @return the {@link ConnectionEditPart} or <code>null</code> if none
	 */
	protected ConnectionEditPart findConnection(Person p, Marriage m) {
		if (p == null || m == null)
			return null;
		Map<?, ?> registry = getViewer().getEditPartRegistry();
		Object conn = new GenealogyConnection(p, m);
		return (ConnectionEditPart) registry.get(conn);
	}

	/**
	 * Find an existing connection or create a new connection if one
	 * does not exist between the specified person and marriage.
	 * 
	 * @param p the person (not <code>null</code>)
	 * @param m the marriage (not <code>null</code>)
	 * @return a {@link ConnectionEditPart} (not <code>null</code>)
	 */
	protected ConnectionEditPart createOrFindConnection(Person p, Marriage m) {
		Object conn = new GenealogyConnection(p, m);
		return createOrFindConnection(conn);
	}

	// ==========================================================================
	// GenealogyElementListener
	
	/**
	 * Update the figure based upon the new model location
	 */
	public void locationChanged(int x, int y) {
		getFigure().setLocation(new Point(x, y));
	}

	/**
	 * Update the figure based upon the new model size
	 */
	public void sizeChanged(int width, int height) {
		getFigure().setSize(width, height);
	}
}
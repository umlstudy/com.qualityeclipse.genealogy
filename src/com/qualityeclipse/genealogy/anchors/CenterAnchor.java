package com.qualityeclipse.genealogy.anchors;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/**
 * A very simplistic anchor that attaches the connection end point to the center
 * of the owner’s bounding box
 */
public final class CenterAnchor extends AbstractConnectionAnchor {
	public CenterAnchor(IFigure owner) {
		super(owner);
	}

	public Point getLocation(Point reference) {
		return getOwner().getBounds().getCenter();
	}
}
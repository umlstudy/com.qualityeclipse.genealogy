package com.qualityeclipse.genealogy.zest;

import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.*;

/**
 * Repositions all nodes closer to the origin (0, 0) while preserving the relative
 * position of each node with one another.
 */
public class ShiftDiagramLayoutAlgorithm extends AbstractLayoutAlgorithm
{
	private double deltaX;
	private double delatY;

	public ShiftDiagramLayoutAlgorithm(int styles) {
		super(styles);
	}

	/**
	 * Calculate the left most node position and the top most node position, then use
	 * these to determine the overall horizontal and vertical offset.
	 */
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height)
	{
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		for (InternalNode entity : entitiesToLayout) {
			minX = Math.min(minX, entity.getCurrentX());
			minY = Math.min(minY, entity.getCurrentY());
		}
		deltaX = 10 - minX;
		delatY = 10 - minY;
	}

	/**
	 * Reposition each node based upon the horizontal and vertical offset calculated in
	 * {@link #preLayoutAlgorithm(InternalNode[], InternalRelationship[], double, double, double, double)}
	 * in a way that preserves relative positions of each node with all other nodes.
	 */
	protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth, double boundsHeight)
	{
		for (InternalNode entity : entitiesToLayout) {
			entity.setLocation(entity.getCurrentX() + deltaX, entity.getCurrentY() + delatY);
		}
	}

	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider)
	{
		// Ignored
	}

	public void setLayoutArea(double x, double y, double width, double height) {
		// Ignored
	}

	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
		return true;
	}

	protected int getTotalNumberOfLayoutSteps() {
		return 0;
	}

	protected int getCurrentLayoutStep() {
		return 0;
	}

}

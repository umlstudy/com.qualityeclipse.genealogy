package com.qualityeclipse.genealogy.zest;

import java.util.*;

import org.eclipse.zest.core.widgets.*;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.*;

import com.qualityeclipse.genealogy.model.Marriage;

/**
 * Reposition each node vertically so that its centerline is where its top edge was. This
 * has the effect of vertically aligning the nodes in each row along their centerlines.
 */
public class MarriageLayoutAlgorithm extends AbstractLayoutAlgorithm
{
	private Map<InternalNode, List<InternalNode>> sourcesMap;
	private Map<InternalNode, List<InternalNode>> targetsMap;

	public MarriageLayoutAlgorithm(int styles) {
		super(styles);
	}

	/**
	 * Build a mapping of InternalNode to GraphNode and back
	 */
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height)
	{
		sourcesMap = new HashMap<InternalNode, List<InternalNode>>();
		targetsMap = new HashMap<InternalNode, List<InternalNode>>();
		for (InternalRelationship relationship : relationshipsToConsider) {
			List<InternalNode> sources = sourcesMap.get(relationship.getDestination());
			if (sources == null) {
				sources = new ArrayList<InternalNode>();
				sourcesMap.put(relationship.getDestination(), sources);
			}
			sources.add(relationship.getSource());
			List<InternalNode> targets = targetsMap.get(relationship.getSource());
			if (targets == null) {
				targets = new ArrayList<InternalNode>();
				targetsMap.put(relationship.getSource(), targets);
			}
			targets.add(relationship.getDestination());
		}
	}

	protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth, double boundsHeight)
	{
		for (InternalNode entity : entitiesToLayout) {
			InternalNode e1 = (InternalNode) entity.getLayoutEntity();
			LayoutEntity e2 = e1.getLayoutEntity();
			GraphNode gn = (GraphNode) e2.getGraphData();
			Object data = gn.getData();
			if (!(data instanceof Marriage))
				continue;

			double x = getCenterX(entity);
			List<InternalNode> spouses = sourcesMap.get(entity);
			if (spouses.size() > 0) {
				x = getCenterX(spouses.get(0));
				if (spouses.size() > 1)
					x = (x + getCenterX(spouses.get(1))) / 2;
				x = x - entity.getWidthInLayout() / 2;
			}

			double y = entity.getCurrentY();
			List<InternalNode> offspring = targetsMap.get(entity);
			if (spouses.size() > 0 && offspring.size() > 0) {
				double y1 = getCenterY(spouses.get(0));
				double y2 = getCenterY(offspring.get(0));
				y = (y1 + y2) / 2 - entity.getHeightInLayout() / 2;
			}

			entity.setLocation(x, y);
		}
	}

	/**
	 * Answer the horizontal center of the specified node
	 */
	private double getCenterX(InternalNode entity) {
		return entity.getCurrentX() + entity.getWidthInLayout() / 2;
	}

	/**
	 * Answer the vertical center of the specified node
	 */
	private double getCenterY(InternalNode entity) {
		return entity.getCurrentY() + entity.getHeightInLayout() / 2;
	}

	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider)
	{
		// Ignored
	}

	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
		return true;
	}

	public void setLayoutArea(double x, double y, double width, double height) {
		// Ignored
	}

	protected int getTotalNumberOfLayoutSteps() {
		return 0;
	}

	protected int getCurrentLayoutStep() {
		return 0;
	}
}

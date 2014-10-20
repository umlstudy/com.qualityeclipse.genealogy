package com.qualityeclipse.genealogy.zest;

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.*;

/**
 * The {@link GraphViewer} does not properly highlight custom figures, so this class
 * listens to the underlying {@link Graph} control and adjusts the highlighting of any
 * figures whose highlighting has not been properly adjusted.
 */
class CustomFigureHighlightAdapter
	implements SelectionListener
{
	private final Graph graph;
	private final Collection<Highlight> highlighted;

	public CustomFigureHighlightAdapter(GraphViewer viewer) {
		graph = viewer.getGraphControl();
		graph.addSelectionListener(this);
		highlighted = new ArrayList<Highlight>();
	}

	//=============================================================
	// SelectionListener

	public void widgetSelected(SelectionEvent e) {
		Collection<?> selection = graph.getSelection();
		Iterator<Highlight> iter = highlighted.iterator();
		while (iter.hasNext()) {
			Highlight h = iter.next();
			if (h.unhighlight(selection))
				iter.remove();
		}
		if (e.item instanceof GraphNode) {
			Highlight h = new Highlight((GraphNode) e.item);
			if (h.highlight())
				highlighted.add(h);
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}

	//=============================================================
	// Inner class

	private final class Highlight
	{
		private final GraphNode node;
		private final IFigure figure;
		private final Color backgroundColor;

		public Highlight(GraphNode node) {
			this.node = node;
			figure = node.getNodeFigure();
			backgroundColor = figure.getBackgroundColor();
		}

		public boolean highlight() {
			Color highlightColor = node.getHighlightColor();
			if (backgroundColor == highlightColor)
				return false;
			figure.setBackgroundColor(highlightColor);
			return true;
		}

		public boolean unhighlight(Collection<?> selection) {
			if (selection.contains(node))
				return false;
			figure.setBackgroundColor(backgroundColor);
			return true;
		}
	}
}
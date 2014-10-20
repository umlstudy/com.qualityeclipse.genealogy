package com.qualityeclipse.genealogy.zest;

import java.util.*;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityRelationshipContentProvider;

import com.qualityeclipse.genealogy.model.*;

/**
 * Content provider for {@link GenealogyZestView}. The getElements(...) method returns the
 * elements in the diagram and the getRelationships(...) method returns the connections
 * between the specified elements. This content provider is best for models that have
 * elements representing both concrete objects and the connections between those objects.
 * <p>
 * Also see {@link GenealogyZestContentProvider1} and
 * {@link GenealogyZestContentProvider3}
 */
public class GenealogyZestContentProvider2
	implements IGraphEntityRelationshipContentProvider
{
	/**
	 * Adjust the receiver based upon new input
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/**
	 * Answer the people and marriages to be displayed
	 */
	public Object[] getElements(Object input) {
		ArrayList<GenealogyElement> results = new ArrayList<GenealogyElement>();
		if (input instanceof GenealogyGraph) {
			GenealogyGraph graph = (GenealogyGraph) input;
			results.addAll(graph.getPeople());
			results.addAll(graph.getMarriages());
		}
		return results.toArray();
	}

	/**
	 * Answer the connections or "relationships" , if any, between the specified objects
	 */
	public Object[] getRelationships(Object source, Object dest) {
		Collection<Object> results = new ArrayList<Object>();
		if (source instanceof Person) {
			Person p = (Person) source;
			if (p.getMarriage() == dest)
				results.add(new Object());
		}
		if (source instanceof Marriage) {
			Marriage m = (Marriage) source;
			if (m.getOffspring().contains(dest))
				results.add(new Object());
		}
		return results.toArray();
	}

	/**
	 * Cleanup the receiver for proper garbage collection by discarding any cached content
	 * and unhooking any listeners.
	 */
	public void dispose() {
	}
}

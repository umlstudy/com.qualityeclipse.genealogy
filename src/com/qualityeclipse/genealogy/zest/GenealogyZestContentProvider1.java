package com.qualityeclipse.genealogy.zest;

import java.util.ArrayList;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.*;

import com.qualityeclipse.genealogy.model.*;

/**
 * Content provider for {@link GenealogyZestView}. The getElements(...) method returns the
 * elements in the diagram and the getConnectedTo(...) method returns to which elements
 * the specified element is connected. This content provider is best for models that do
 * not have elements representing the connections between other objects.
 * <p>
 * Also see {@link GenealogyZestContentProvider2} and
 * {@link GenealogyZestContentProvider3}
 */
class GenealogyZestContentProvider1
	implements IGraphEntityContentProvider, INestedContentProvider
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
		ArrayList<Object> results = new ArrayList<Object>();
		if (input instanceof GenealogyGraph) {
			GenealogyGraph graph = (GenealogyGraph) input;
			results.addAll(graph.getPeople());
			results.addAll(graph.getMarriages());
		}
		return results.toArray();
	}

	/**
	 * Given a person or a marriage, answer to which other people or marriages that object
	 * is connected.
	 */
	public Object[] getConnectedTo(Object element) {
		ArrayList<Object> results = new ArrayList<Object>();
		if (element instanceof Person) {
			Person p = (Person) element;
			if (p.getMarriage() != null)
				results.add(p.getMarriage());
		}
		if (element instanceof Marriage) {
			Marriage m = (Marriage) element;
			results.addAll(m.getOffspring());
		}
		return results.toArray();
	}

	/**
	 * Answer <code>true</code> if the specified model element has children.
	 */
	public boolean hasChildren(Object element) {
		
		//
		// Uncomment this section to display notes as nested content
		//
		// if (element instanceof Person)
		//	return true;
		
		return false;
	}

	/**
	 * Answer the child model elements of the specified model element. This method is only called
	 * if the hasChildren method returns true for the specified element.
	 */
	public Object[] getChildren(Object element) {
		if (element instanceof Person)
			return ((Person) element).getNotes().toArray();
		return null;
	}

	/**
	 * Cleanup the receiver for proper garbage collection by discarding any cached content
	 * and unhooking any listeners.
	 */
	public void dispose() {
	}
}
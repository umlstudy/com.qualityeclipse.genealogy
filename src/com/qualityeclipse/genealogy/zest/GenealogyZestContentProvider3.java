package com.qualityeclipse.genealogy.zest;

import java.util.*;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.*;

import com.qualityeclipse.genealogy.model.*;

/**
 * Content provider for {@link GenealogyZestView}. Interestingly, the content provider’s
 * getElements(...) must return objects that model the connections or as the javadoc calls
 * it “relationships” rather than the objects being connected as one might expect. The
 * getSource(...) and getDestination(...) methods return the source and destination
 * objects respectively for the connections returned by the getElements(...) method. This
 * content provider is best for models that are relationship centric and have elements
 * representing the connections between concrete objects rather than the concrete objects
 * themselves.
 * <p>
 * Also see {@link GenealogyZestContentProvider1} and
 * {@link GenealogyZestContentProvider2}
 */
class GenealogyZestContentProvider3
	implements IGraphContentProvider
{
	private class Connection
	{
		public final GenealogyElement source, destination;

		public Connection(GenealogyElement s, GenealogyElement d) {
			source = s;
			destination = d;
		}
	}

	/**
	 * Adjust the receiver based upon new input
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/**
	 * Answer the objects modeling the connections or the "relationships"
	 */
	public Object[] getElements(Object input) {
		Collection<Connection> results = new ArrayList<Connection>();
		if (input instanceof GenealogyGraph) {
			GenealogyGraph graph = (GenealogyGraph) input;
			for (Person p : graph.getPeople()) {
				if (p.getMarriage() != null)
					results.add(new Connection(p, p.getMarriage()));
				if (p.getParentsMarriage() != null)
					results.add(new Connection(p.getParentsMarriage(), p));
			}
		}
		return results.toArray();
	}

	/**
	 * Answer the source model object for the specified relationship
	 */
	public Object getSource(Object connection) {
		return ((Connection) connection).source;
	}

	/**
	 * Answer the destination or target model object for the specified relationship
	 */
	public Object getDestination(Object connection) {
		return ((Connection) connection).destination;
	}

	/**
	 * Cleanup the receiver for proper garbage collection by discarding any cached content
	 * and unhooking any listeners.
	 */
	public void dispose() {
	}
}
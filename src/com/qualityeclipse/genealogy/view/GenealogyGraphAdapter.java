package com.qualityeclipse.genealogy.view;

import java.util.*;

import org.eclipse.draw2d.*;

import com.qualityeclipse.genealogy.listener.FigureMover;
import com.qualityeclipse.genealogy.model.*;
import com.qualityeclipse.genealogy.model.listener.GenealogyGraphListener;

/**
 * Listens to the {@link GenealogyGraph} model for changes and adapts the
 * {@link GenealogyView} diagram to match the model.
 */
public class GenealogyGraphAdapter
	implements GenealogyGraphListener
{
	private final GenealogyGraph graph;
	private final FreeformLayer primary;
	private final ConnectionLayer connections;
	private final Map<GenealogyElement, GenealogyElementAdapter> map = new HashMap<GenealogyElement, GenealogyElementAdapter>();

	public GenealogyGraphAdapter(GenealogyGraph graph, FreeformLayer primary, ConnectionLayer connections) {
		this.graph = graph;
		this.primary = primary;
		this.connections = connections;
		for (Person p : graph.getPeople())
			personAdded(p);
		for (Marriage m : graph.getMarriages())
			marriageAdded(m);
		List<Note> notes = graph.getNotes();
		int notesSize = notes.size();
		for (int i = 0; i < notesSize; i++)
			noteAdded(i, notes.get(i));
		this.graph.addGenealogyGraphListener(this);
	}
	
	public void graphCleared() {
		primary.removeAll();
		connections.removeAll();
		map.clear();
	}

	public void personAdded(Person p) {
		addPrimaryFigure(p, new PersonAdapter(p));
	}

	public void personRemoved(Person p) {
		removePrimaryFigure(p);
	}

	public void marriageAdded(Marriage m) {
		addPrimaryFigure(m, new MarriageAdapter(m));
	}

	public void marriageRemoved(Marriage m) {
		removePrimaryFigure(m);
	}

	public void noteAdded(int index, Note n) {
		addPrimaryFigure(n, new NoteAdapter(n));
	}

	public void noteRemoved(Note n) {
		removePrimaryFigure(n);
	}

	private void addPrimaryFigure(GenealogyElement elem, GenealogyElementAdapter adapter) {
		IFigure figure = adapter.getFigure();
		primary.add(figure, figure.getBounds());
		map.put(elem, adapter);
		adapter.setGraphAdapter(this);
		new FigureMover(adapter.getFigure());
	}

	private void removePrimaryFigure(GenealogyElement elem) {
		GenealogyElementAdapter adapter = map.remove(elem);
		primary.remove(adapter.getFigure());
		adapter.dispose();
	}

	public IFigure getPersonFigure(Person person) {
		return map.get(person).getFigure();
	}

	public ConnectionLayer getConnectionLayer() {
		return connections;
	}
	
	public void dispose() {
		for (GenealogyElementAdapter adapter : map.values())
			adapter.dispose();
		graph.removeGenealogyGraphListener(this);
	}
}

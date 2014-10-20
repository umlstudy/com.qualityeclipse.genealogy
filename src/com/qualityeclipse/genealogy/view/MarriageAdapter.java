package com.qualityeclipse.genealogy.view;

import java.util.*;

import org.eclipse.draw2d.*;

import com.qualityeclipse.genealogy.figures.*;
import com.qualityeclipse.genealogy.model.*;
import com.qualityeclipse.genealogy.model.listener.MarriageListener;

/**
 * Creates and manages a {@link MarriageFigure} to represent an associated {@link Marriage}
 * in a {@link GenealogyGraph}.
 */
public class MarriageAdapter extends GenealogyElementAdapter
	implements MarriageListener
{
	private final Marriage marriage;
	private Connection husbandConnection;
	private Connection wifeConnection;
	private final Map<Person, Connection> offspringConnections = new HashMap<Person, Connection>();

	public MarriageAdapter(Marriage marriage) {
		super(marriage, new MarriageFigure(marriage.getYearMarried()));
		this.marriage = marriage;
		marriage.addMarriageListener(this);
	}
	
	public void setGraphAdapter(GenealogyGraphAdapter graphAdapter) {
		super.setGraphAdapter(graphAdapter);
		// Need graphAdapter before connections can be added
		husbandChanged(marriage.getHusband(), null);
		wifeChanged(marriage.getWife(), null);
		for (Person p : marriage.getOffspring())
			offspringAdded(p);
	}
	
	public MarriageFigure getFigure() {
		return (MarriageFigure) super.getFigure();
	}

	public void yearMarriedChanged(int yearMarried) {
		getFigure().setYearMarried(yearMarried);
	}

	public void husbandChanged(Person husband, Person oldHusband) {
		husbandConnection = parentChanged(husband, husbandConnection);
	}

	public void wifeChanged(Person wife, Person oldWife) {
		wifeConnection = parentChanged(wife, wifeConnection);
	}
	
	private Connection parentChanged(Person p, Connection oldConnection) {
		if (oldConnection != null)
			oldConnection.getParent().remove(oldConnection);
		if (p == null)
			return null;
		IFigure pf = getGraphAdapter().getPersonFigure(p);
		PolylineConnection connection = getFigure().addParent(pf);
		getGraphAdapter().getConnectionLayer().add(connection);
		return connection;
	}

	public void offspringAdded(Person p) {
		IFigure personFigure = getGraphAdapter().getPersonFigure(p);
		PolylineConnection connection = getFigure().addChild(personFigure);
		offspringConnections.put(p, connection);
		getGraphAdapter().getConnectionLayer().add(connection);
	}

	public void offspringRemoved(Person p) {
		Connection connection = offspringConnections.remove(p);
		connection.getParent().remove(connection);
	}

	public void dispose() {
		marriage.removeMarriageListener(this);
	}
}

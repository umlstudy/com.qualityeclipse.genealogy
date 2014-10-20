package com.qualityeclipse.genealogy.parts;

import org.eclipse.gef.*;

import com.qualityeclipse.genealogy.model.*;
import com.qualityeclipse.genealogy.model.connection.*;

/**
 * A factory for constructing {@link EditPart}s for genealogy model objects.
 */
public class GenealogyEditPartFactory
	implements EditPartFactory
{
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof GenealogyGraph)
			return new GenealogyGraphEditPart((GenealogyGraph) model);
		if (model instanceof Person)
			return new PersonEditPart((Person) model);
		if (model instanceof Marriage)
			return new MarriageEditPart((Marriage) model);
		if (model instanceof GenealogyConnection)
			return new GenealogyConnectionEditPart((GenealogyConnection) model);
		if (model instanceof Note)
			return new NoteEditPart((Note) model);
		throw new IllegalStateException("No EditPart for " + model.getClass());
	}
}

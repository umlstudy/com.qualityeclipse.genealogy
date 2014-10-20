package com.qualityeclipse.genealogy.parts;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.*;
import org.eclipse.gef.requests.*;
import org.eclipse.swt.graphics.Image;

import com.qualityeclipse.genealogy.commands.*;
import com.qualityeclipse.genealogy.figures.PersonFigure;
import com.qualityeclipse.genealogy.model.*;
import com.qualityeclipse.genealogy.model.connection.GenealogyConnection;
import com.qualityeclipse.genealogy.model.listener.PersonListener;
import com.qualityeclipse.genealogy.policies.PersonGraphicalNodeEditPolicy;

/**
 * The {@link EditPart} for the {@link Person} model object. This EditPart is responsible
 * for creating a visual representation for the model object and for updating that visual
 * representation as the model changes.
 */
public class PersonEditPart extends GenealogyElementEditPart
	implements PersonListener, NodeEditPart
{
	public PersonEditPart(Person person) {
		setModel(person);
	}

	public Person getModel() {
		return (Person) super.getModel();
	}

	/**
	 * Create and return the figure representing this model object
	 */
	protected IFigure createFigure() {
		Person m = getModel();
		Image image = m.getGender() == Person.Gender.MALE ? PersonFigure.MALE : PersonFigure.FEMALE;
		return new PersonFigure(m.getName(), image, m.getBirthYear(), m.getDeathYear());
	}

	/**
	 * Answer the person figure associated with the receiver
	 */
	private PersonFigure getPersonFigure() {
		return (PersonFigure) getFigure();
	}

	/**
	 * Override the superclass implementation to return a special figure nested within
	 * {@link PersonFigure} that only contains notes.
	 */
	public IFigure getContentPane() {
		return getPersonFigure().getNotesContainer();
	}

	/**
	 * Answer the embedded notes to be displayed
	 */
	protected List<Note> getModelChildren() {
		return getModel().getNotes();
	}

	/**
	 * Answer a collection of connection model objects that originate with the receiver.
	 */
	public List<GenealogyConnection> getModelSourceConnections() {
		Person person = getModel();
		Marriage marriage = person.getMarriage();
		ArrayList<GenealogyConnection> marriageList = new ArrayList<GenealogyConnection>(1);
		if (marriage != null)
			marriageList.add(new GenealogyConnection(person, marriage));
		return marriageList;
	}

	/**
	 * Return an instance of {@link ChopboxAnchor} so that the connection originates along
	 * the {@link PersonFigure}'s bounding box. This is called once a connection has been
	 * established.
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	/**
	 * Return an instance of {@link ChopboxAnchor} so that the connection creation
	 * originates along the {@link PersonFigure}'s bounding box. 
	 * This is called when the user is interactively creating a connection.
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		if (request instanceof ReconnectRequest) {
			EditPart part = ((ReconnectRequest) request).getConnectionEditPart();
			if (!(part instanceof GenealogyConnectionEditPart))
				return null;
			GenealogyConnectionEditPart connPart = (GenealogyConnectionEditPart) part;
			CreateConnectionCommand connCmd = connPart.recreateCommand();
			if (!connCmd.isValidSource(getModel()))
				return null;
			return new ChopboxAnchor(getFigure());
		}
		return new ChopboxAnchor(getFigure());
	}

	/**
	 * Answer a collection of connection model objects that terminate at the receiver.
	 */
	protected List<GenealogyConnection> getModelTargetConnections() {
		ArrayList<GenealogyConnection> offspringList = new ArrayList<GenealogyConnection>();
		Person person = getModel();
		Marriage parentsMarriage = person.getParentsMarriage();
		if (parentsMarriage != null)
			offspringList.add(new GenealogyConnection(person, parentsMarriage));
		return offspringList;
	}

	/**
	 * Return an instance of {@link ChopboxAnchor} so that the connection terminates along
	 * the {@link PersonFigure}'s bounding box. This is called once a connection has been
	 * established.
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	/**
	 * If this request is creating a connection from a {@link MarriageEditPart} to the
	 * receiver, then return an instance of {@link ChopboxAnchor} so that the connection
	 * creation snaps to the figure and terminates along the {@link PersonFigure}'s
	 * bounding box. If the connection source is NOT from a {@link MarriageEditPart} then
	 * return <code>null</code> so that the connection does not appear to connect to the receiver. 
	 * This is called when the user is interactively creating a connection.
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		if (request instanceof CreateConnectionRequest) {
			Command cmd = ((CreateConnectionRequest) request).getStartCommand();
			if (!(cmd instanceof CreateConnectionCommand))
				return null;
			if (!((CreateConnectionCommand) cmd).isValidTarget(getModel()))
				return null;
			return new ChopboxAnchor(getFigure());
		}
		if (request instanceof ReconnectRequest) {
			EditPart part = ((ReconnectRequest) request).getConnectionEditPart();
			if (!(part instanceof GenealogyConnectionEditPart))
				return null;
			GenealogyConnectionEditPart connPart = (GenealogyConnectionEditPart) part;
			CreateConnectionCommand connCmd = connPart.recreateCommand();
			if (!connCmd.isValidTarget(getModel()))
				return null;
			return new ChopboxAnchor(getFigure());
		}
		return null;
	}

	/**
	 * Extend the superclass behavior to modify the associated figure's appearance to show
	 * that the element is selected.
	 */
	protected void fireSelectionChanged() {
		getPersonFigure().setSelected(getSelected() != 0);
		super.fireSelectionChanged();
	}

	/**
	 * Create and install {@link EditPolicy} instances used to define behavior
	 * associated with this EditPart's figure.
	 */
	protected void createEditPolicies() {
		// Resize feedback provided by container
		//NonResizableEditPolicy selectionPolicy = new NonResizableEditPolicy();
		//selectionPolicy.setDragAllowed(false);
		//installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionPolicy);

		// Handles creation and moving Notes contained by a Person
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new OrderedLayoutEditPolicy() {
			
			/**
			 * Return a new creation command 
			 * when a note is created in the person.
			 */
			protected Command getCreateCommand(CreateRequest request) {
				Object type = request.getNewObjectType();
				if (type == Note.class) {
					Note note = (Note) request.getNewObject();
					return new CreateNoteCommand(getModel(), note, null);
				}
				return null;
			}

			/**
			 * Return a new reparent command when the a note is dragged
			 * from one note container to another.
			 * Return null if the container has not changed.
			 */
			protected Command createAddCommand(EditPart child, EditPart after) {
				NoteContainer oldContainer = (NoteContainer) child.getParent().getModel();
				if (getModel() == oldContainer)
					return null;
				Note note = (Note) child.getModel();
				ReparentNoteCommand cmd = new ReparentNoteCommand(getModel(), note);
				if (after != null)
					cmd.setAfterNote((Note) after.getModel());
				cmd.setOldContainer(oldContainer);
				return cmd;
			}

			/**
			 * Return a new reorder command when a note is dragged
			 * within the same note container.
			 * Return null if the order is not changing.
			 */
			protected Command createMoveChildCommand(EditPart child, EditPart after) {
				if (child == after || getChildren().size() == 1)
					return null;
				int index = getChildren().indexOf(child);
				if (index == 0) {
					if (after == null)
						return null;
				}
				else {
					if (after == getChildren().get(index - 1))
						return null;
				}
				ReorderNoteCommand cmd = new ReorderNoteCommand(getModel(), (Note) child.getModel());
				if (after != null)
					cmd.setAfterNote((Note) after.getModel());
				return cmd;
			}

			/**
			 * Given a request, return the insertion position
			 * indicated by the y coordinate of the request.
			 * This is called both when the note is reparented and reordered.
			 */
			protected EditPart getInsertionReference(Request request) {
				int y = ((ChangeBoundsRequest) request).getLocation().y;
				List<?> notes = getChildren();
				NoteEditPart afterNote = null;
				for (Iterator<?> iter = notes.iterator(); iter.hasNext();) {
					NoteEditPart note = (NoteEditPart) iter.next();
					if (y < note.getFigure().getBounds().y)
						return afterNote;
					afterNote = note;
				}
				return afterNote;
			}
		});

		// Handles deleting the selected person
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
			protected Command createDeleteCommand(GroupRequest request) {
				GenealogyGraph graph = (GenealogyGraph) getParent().getModel();
				return new DeletePersonCommand(graph, getModel());
			}
		});
		
		// Handles connection creation and retargeting
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new PersonGraphicalNodeEditPolicy(getModel()));
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can add itself as a listener to the underlying model object
	 */
	public void addNotify() {
		super.addNotify();
		getModel().addPersonListener(this);
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can stop listening to events from the underlying model object
	 */
	public void removeNotify() {
		getModel().removePersonListener(this);
		super.removeNotify();
	}

	// ==================================================================
	// PersonListener

	public void nameChanged(String newName) {
		getPersonFigure().setName(newName);
	}

	public void birthYearChanged(int birthYear) {
		int deathYear = getModel().getDeathYear();
		getPersonFigure().setBirthAndDeathYear(birthYear, deathYear);
	}

	public void deathYearChanged(int deathYear) {
		int birthYear = getModel().getBirthYear();
		getPersonFigure().setBirthAndDeathYear(birthYear, deathYear);
	}

	/**
	 * Update the spouce connection from the receiver to the marriage.
	 */
	public void marriageChanged(Marriage marriage, Marriage oldMarriage) {
		ConnectionEditPart part = findConnection(getModel(), oldMarriage);
		if (part != null)
			removeSourceConnection(part);
		if (marriage != null) {
			part = createOrFindConnection(getModel(), marriage);
			addSourceConnection(part, 0);
		}
	}

	/**
	 * Update the offspring connection from the marriage to the receiver.
	 */
	public void parentsMarriageChanged(Marriage marriage, Marriage oldMarriage) {
		ConnectionEditPart part = findConnection(getModel(), oldMarriage);
		if (part != null)
			removeTargetConnection(part);
		if (marriage != null) {
			part = createOrFindConnection(getModel(), marriage);
			addTargetConnection(part, 0);
		}
	}
	
	/**
	 * Add a nested {@link NoteEditPart} to reflect the model change
	 */
	public void noteAdded(int index, Note n) {
		addChild(createChild(n), index);
	}

	/**
	 * Remove the nested {@link NoteEditPart} to reflect the model change
	 */
	public void noteRemoved(Note n) {
		Object part = getViewer().getEditPartRegistry().get(n);
		if (part instanceof EditPart)
			removeChild((EditPart) part);
	}
}

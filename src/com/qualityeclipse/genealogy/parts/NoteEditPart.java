package com.qualityeclipse.genealogy.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import com.qualityeclipse.genealogy.commands.DeleteNoteCommand;
import com.qualityeclipse.genealogy.commands.RenameNoteCommand;
import com.qualityeclipse.genealogy.figures.NoteFigure;
import com.qualityeclipse.genealogy.model.Note;
import com.qualityeclipse.genealogy.model.NoteContainer;
import com.qualityeclipse.genealogy.model.listener.NoteListener;

/**
 * The {@link EditPart} for the {@link Note} model object. This EditPart is responsible
 * for creating a visual representation for the model object and for updating that visual
 * representation as the model changes.
 */
public class NoteEditPart extends GenealogyElementEditPart 
	implements NoteListener
{
	public NoteEditPart(Note note) {
		setModel(note);
	}

	public Note getModel() {
		return (Note) super.getModel();
	}

	/**
	 * Create and return the figure representing this model object
	 */
	protected IFigure createFigure() {
		return new NoteFigure(getModel().getText());
	}

	/**
	 * Extend the superclass behavior to modify the associated figure's appearance to show
	 * that the element is selected.
	 */
	protected void fireSelectionChanged() {
		((NoteFigure) getFigure()).setSelected(getSelected() != 0);
		super.fireSelectionChanged();
	}

	/**
	 * Create and install {@link EditPolicy} instances used to define behavior
	 * associated with this EditPart's figure.
	 */
	protected void createEditPolicies() {
		// Resize feedback provided by container
		//NonResizableEditPolicy selectionPolicy = new NonResizableEditPolicy();
		//installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionPolicy);

		// Handles deleting the receiver
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
			protected Command createDeleteCommand(GroupRequest request) {
				NoteContainer container = (NoteContainer) getParent().getModel();
				return new DeleteNoteCommand(container, getModel());
			}
		});
		
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DirectEditPolicy() {
			
			@Override
			protected void showCurrentEditValue(DirectEditRequest request) {
			}
			
			@Override
			protected Command getDirectEditCommand(DirectEditRequest request) {
				String newValue = (String)request.getCellEditor().getValue();
				if ( !newValue.equals(getNoteFigure().getText()) ) {
					return new RenameNoteCommand(getModel(), newValue);
				}
				return null;
			}
		});
	}
	
	public void performRequest(Request req) {
		if ( req != null && req.getType().equals(RequestConstants.REQ_DIRECT_EDIT) ) {
			
			DirectEditManager dem = new DirectEditManager(this, TextCellEditor.class, null) {
				@Override
				protected void initCellEditor() {
					String text = ((NoteFigure) getFigure()).getText();
					getCellEditor().setValue(text);
				}
			};
			dem.setLocator(new CellEditorLocator() {
				@Override
				public void relocate(final CellEditor celleditor) {
					final IFigure figure = getFigure();
					final Text text = (Text) celleditor.getControl();
					final Rectangle rect = figure.getBounds();
//					figure.translateToAbsolute(rect);
//					final Point size = text.computeSize(SWT.DEFAULT,
//							SWT.DEFAULT, true);
//					final org.eclipse.swt.graphics.Rectangle trim = text
//							.computeTrim(0, 0, 0, 0);
//					rect.translate(trim.x, trim.y);
//					rect.width = Math.max(size.x, rect.width);
//					rect.width += trim.width;
//					rect.height += trim.height;
//					text.setBounds(rect.x, rect.y, rect.width, rect.height);
					text.setBounds(rect.x, rect.y, rect.width, rect.height);
				}
			});
			dem.show();
		}
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can add itself as a listener to the underlying model object
	 */
	public void addNotify() {
		super.addNotify();
		getModel().addNoteListener(this);
	}
	
	/**
	 * Override the superclass implementation so that the receiver
	 * can stop listening to events from the underlying model object
	 */
	public void removeNotify() {
		getModel().removeNoteListener(this);
		super.removeNotify();
	}

	// ==================================================================
	// NoteListener

	public void textChanged(String text) {
		((NoteFigure) getFigure()).setText(text);
	}
	
	private NoteFigure getNoteFigure() {
		return (NoteFigure) getFigure();
	}
}

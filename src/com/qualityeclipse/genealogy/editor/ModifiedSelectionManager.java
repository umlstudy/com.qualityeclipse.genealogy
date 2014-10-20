package com.qualityeclipse.genealogy.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SelectionManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class ModifiedSelectionManager extends SelectionManager {
	private final GraphicalViewer viewer;
	
	public ModifiedSelectionManager(GraphicalViewer viewer) {
		this.viewer = viewer;
	}
	
	public void setSelection(ISelection selection) {
		// build a collection of  originally selected parts
		// and a collection from which nested parts are removed
		
		List<?> oldSelection = ((IStructuredSelection)selection).toList();
		final List<Object> newSelection = new ArrayList<Object>(oldSelection.size());
		newSelection.addAll(oldSelection);
		
		Iterator<Object> iter = newSelection.iterator();
		while ( iter.hasNext() ) {
			if ( containsAncestor(newSelection, (EditPart)iter.next())) {
				iter.remove();
			}
		}
		
		super.setSelection(new StructuredSelection(newSelection));
		
	}
	
	/* 
	 * TODO !!!!!!
	 * (non-Javadoc)
	 * @see org.eclipse.gef.SelectionManager#appendSelection(org.eclipse.gef.EditPart)
	 */
	public void appendSelection(EditPart part ) {
		List<?> selection = ((IStructuredSelection)getSelection()).toList();
		
		if ( selection.size() == 1 && selection.get(0) == viewer.getContents() ) {
			super.appendSelection(part);
			return;
		}
		
		if ( containsAncestor(selection, part) ) {
			return;
		}
		
		Iterator<?> iter = new ArrayList<Object>(selection).iterator();
		while ( iter.hasNext() ) {
			EditPart each = (EditPart)iter.next();
			if ( isAncestor(part, each) ) {
				deselect(each);
			}
		}
		
		super.appendSelection(part);
	}
	
	
	private static boolean isAncestor(EditPart ancestor, EditPart part) {
		while ( part != null ) {
			part = part.getParent();
			if ( part == ancestor ) {
				return true;
			}
		}
		
		return false;
	}

	private static boolean containsAncestor(List<?> selection, EditPart part) {
		while ( part != null ) {
			part = part.getParent();
			if ( selection.contains(part)) {
				return true;
			}
		}
		
		return false;
	}
}

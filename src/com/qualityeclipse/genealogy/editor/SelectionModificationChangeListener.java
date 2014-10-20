package com.qualityeclipse.genealogy.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

public class SelectionModificationChangeListener implements ISelectionChangedListener {

	private final GraphicalViewer viewer;
	
	SelectionModificationChangeListener(GraphicalViewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// build a collection of  originally selected parts
		// and a collection from which nested parts are removed
		
		List<?> oldSelection = ((IStructuredSelection)event.getSelection()).toList();
		final List<Object> newSelection = new ArrayList<Object>(oldSelection.size());
		newSelection.addAll(oldSelection);
		
		// Cycle through all selected parts and remove nested parts
		// which have a parent or grandparent part that is selected
		
		EditPart root = viewer.getRootEditPart();
		Iterator<Object> iter = newSelection.iterator();
		while ( iter.hasNext() ) {
			EditPart part = (EditPart)iter.next();
			while ( part != root ) {
				part = part.getParent();
				if ( newSelection.contains(part)) {
					iter.remove();
					break;
				}
			}
		}
		
		// If the new selection is smaller than the original selection 
		// then modify the current selection
		
		if ( newSelection.size() < oldSelection.size() ) {
			viewer.getControl().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					viewer.setSelection(new StructuredSelection(newSelection));
				}
			});
		}

	}

}

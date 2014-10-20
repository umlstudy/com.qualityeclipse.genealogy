package com.qualityeclipse.genealogy.editor;

import java.io.*;
import java.util.EventObject;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.gef.*;
import org.eclipse.gef.dnd.*;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.palette.*;
import org.eclipse.gef.ui.parts.*;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.SWT;
import org.eclipse.ui.*;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.services.IServiceLocator;

import com.qualityeclipse.genealogy.model.GenealogyGraph;
import com.qualityeclipse.genealogy.model.io.*;
import com.qualityeclipse.genealogy.parts.*;

/**
 * An editor for loading, modifying, and saving genealogy graphs
 */
public class GenealogyGraphEditor extends GraphicalEditorWithFlyoutPalette
{
	private final GenealogyGraph genealogyGraph = new GenealogyGraph();
	private DirectEditAction directEditAction;

	public GenealogyGraphEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	/**
	 * Configure the viewer to display a genealogy graph
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		final GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new GenealogyEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		
		// Two different approaches for dynamically modifying the selection
		// to exclude nested figures when that figure's ancestor is selected
		
		// Option #1
		//viewer.addSelectionChangedListener(new SelectionModificationChangeListener(viewer));
		
		// Option #2
		viewer.setSelectionManager(new ModifiedSelectionManager(viewer));
		
		
		
		
		getEditorSite().getKeyBindingService().setScopes(new String[] {"com.qualityeclipse.genealogyContext",});
		directEditAction = new DirectEditAction(getEditorSite().getPart());
		directEditAction.setActionDefinitionId(IWorkbenchCommandConstants.FILE_RENAME);
		directEditAction.setEnabled(true);
		getEditorSite().getKeyBindingService().registerAction(directEditAction);
		
		GraphicalViewerKeyHandler graphicalViewerKeyHandler = new GraphicalViewerKeyHandler(viewer);
		graphicalViewerKeyHandler.put(KeyStroke.getPressed(SWT.F3, 0), directEditAction);
		viewer.setKeyHandler(graphicalViewerKeyHandler);
		getSelectionActions().add(directEditAction.getId());
		getActionRegistry().registerAction(directEditAction);
	}

	/**
	 * Initialize the viewer to display the specific genealogy graph being edited
	 */
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(genealogyGraph);
		getGraphicalViewer().addDropTargetListener(
			new TemplateTransferDropTargetListener(getGraphicalViewer()));
		
	}
	
	public void dispose() {
		getEditorSite().getKeyBindingService().unregisterAction(directEditAction);
		super.dispose();
	}

	/**
	 * Read the content from the specified file.
	 */
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		IFile file = ((IFileEditorInput) input).getFile();
		setPartName(file.getName());

		// For the purposes of this book, if the file is empty
		// then load some default content into the model
		
		try {
			InputStream stream = file.getContents();
			boolean isEmpty = stream.read() == -1;
			stream.close();
			if (isEmpty) {
				readAndClose(getClass().getResourceAsStream("../view/genealogy.xml"));
				return;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// Read the content from the stream into the model

		try {
			readAndClose(file.getContents());
		}
		catch (CoreException e) {
			handleException(e);
			return;
		}
	}

	/**
	 * Read the content from the stream into the model
	 * 
	 * @param stream the stream to be read and closed (not <code>null</code>)
	 */
	private void readAndClose(InputStream stream) {
		genealogyGraph.clear();
		try {
			new GenealogyGraphReader(genealogyGraph).read(stream);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				stream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Save the model being edited into the file associated with this editor,
	 * then update the editor state to indicate that the contents have been saved.
	 * 
	 * @param monitor the progress monitor
	 */
	public void doSave(IProgressMonitor monitor) {
		
		// Serialize the model
		
		StringWriter writer = new StringWriter(5000);
		new GenealogyGraphWriter(genealogyGraph).write(new PrintWriter(writer));
		ByteArrayInputStream stream = new ByteArrayInputStream(writer.toString().getBytes());

		// Store the serialized model in the file
		
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		try {
			if (file.exists())
				file.setContents(stream, false, true, monitor);
			else
				file.create(stream, false, monitor);
		}
		catch (CoreException e) { 
			handleException(e);
			return;
		}
		
		// Update the editor state to indicate that the contents 
		// have been saved and notify all listeners about the change in state
		
		getCommandStack().markSaveLocation();
		firePropertyChange(PROP_DIRTY);
	}

	/**
	 * Log the specified exception and notify the user that an problem has occurred
	 * 
	 * @param ex the exception that occurred (not <code>null</code>)
	 */
	private void handleException(Exception ex) {
		ex.printStackTrace();
		Status status = new Status(
			IStatus.ERROR, 
			"com.qualityeclipse.genealogy", 
			"An exception occurred while saving the file", 
			ex);
		ErrorDialog.openError(
			getSite().getShell(), "Exception", ex.getMessage(), status);
	}

	/**
	 * Override the superclass implementation to return <code>true</code>
	 * indicating that the doSaveAs() method may be called
	 * to save the editor's content in a different file.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Prompt the user for a new file in which to save the editor content. 
	 * If the user chooses a new file, then change the file associated with the editor 
	 * to the file selected by the user and call the doSave() method 
	 * to save the editor content in the new file.
	 */
	public void doSaveAs() {
		
		// Prompt the user for a new file
		
		SaveAsDialog dialog = new SaveAsDialog(getSite().getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();

		// If the user clicked cancel, then bail out
		
		IPath path = dialog.getResult();
		if (path == null)
			return;
		
		// Change the file associated with this editor and call doSave(...) 
		// to save the editor's content in the new file
		
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		super.setInput(new FileEditorInput(file));
		doSave(null);
		setPartName(file.getName());
		firePropertyChange(PROP_INPUT);
	}

	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}
	
	/**
	 * If possible, adapt the receiver to an object of the specified type.
	 */
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class type) {
		Object value = super.getAdapter(type);
		
		/* For informational purposes, 
		 * echo the calls to getAdapter(...) and the results returned
		 */
		System.out.println("getAdapter(" + type.getSimpleName() + ") -> " 
			+ (value != null ? value.getClass().getSimpleName() : "null"));
		
		if ( value == null ) {
			return null;
		}
		
		return value;
	}

	/**
	 * Create and answer the palette for use with the receiver
	 */
	protected PaletteRoot getPaletteRoot() {
		return GenealogyGraphEditorPaletteFactory.createPalette();
	}
	
	/**
	 * Override the superclass method to return a provider
	 * that adds a creation tool drag listener
	 */
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
	}
}

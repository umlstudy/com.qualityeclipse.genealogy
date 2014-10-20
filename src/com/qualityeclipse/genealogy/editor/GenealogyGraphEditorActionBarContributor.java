package com.qualityeclipse.genealogy.editor;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;

/**
 * Contributes actions for the current editor to the workbench.
 * This is covered in depth in the Eclipse Plug-ins book
 * and thus will not receive much attention in this GEF book.
 */
public class GenealogyGraphEditorActionBarContributor extends ActionBarContributor
{
	public GenealogyGraphEditorActionBarContributor() {
	}

	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new LabelRetargetAction(ActionFactory.SELECT_ALL.getId(), "Select All"));
//		addRetargetAction(new DirectEditAction(getPage().getActiveEditor());
		
//		addRetargetAction(new DirectEditAction(get));
	}
	
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
	}

	protected void declareGlobalActionKeys() {
	}
//	
//	public class RenameRetargetAction extends RetargetAction {
//
//		private DirectEditAction directEditAction;
//		
//		public RenameRetargetAction(DirectEditAction directEditAction) {
//			super(ActionFactory.RENAME.getId(), GEFMessages.RenameAction_Label);
//			setToolTipText(GEFMessages.RenameAction_Tooltip);
//			ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
//			setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
//			setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
//			this.directEditAction =directEditAction;
//		}
//	}

}

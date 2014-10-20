package com.qualityeclipse.genealogy.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;

public class RenameHandler implements IHandler {
	
	private ActionHandler actionHandler;
	
	public RenameHandler(IAction action) {
		actionHandler = new ActionHandler(action);
	}

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		actionHandler.addHandlerListener(handlerListener);
	}

	@Override
	public void dispose() {
		actionHandler.dispose();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return actionHandler.execute(event);
	}

	@Override
	public boolean isEnabled() {
		return actionHandler.isEnabled();
	}

	@Override
	public boolean isHandled() {
		return actionHandler.isHandled();
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		actionHandler.removeHandlerListener(handlerListener);
	}
}

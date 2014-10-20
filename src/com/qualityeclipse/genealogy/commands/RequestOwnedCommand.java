package com.qualityeclipse.genealogy.commands;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

public class RequestOwnedCommand<T extends Request> extends Command {

	private T request;

	public RequestOwnedCommand(T request) {
		setRequest(request);
	}
	
	public RequestOwnedCommand() {
		this(null);
	}

	public T getRequest() {
		return request;
	}

	public void setRequest(T request) {
		this.request = request;
	}
}

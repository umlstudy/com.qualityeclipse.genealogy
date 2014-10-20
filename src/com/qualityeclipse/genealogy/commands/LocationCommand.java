package com.qualityeclipse.genealogy.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public class LocationCommand extends Command {

	private Point location;

	public LocationCommand(Point location) {
		this.setLocation(location);
	}

	public LocationCommand() {
		this(null);
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

}

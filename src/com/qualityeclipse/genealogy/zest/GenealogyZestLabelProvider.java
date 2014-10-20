package com.qualityeclipse.genealogy.zest;

import org.eclipse.draw2d.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.zest.core.viewers.*;
import org.eclipse.zest.core.widgets.*;

import com.qualityeclipse.genealogy.anchors.MarriageAnchor;
import com.qualityeclipse.genealogy.figures.*;
import com.qualityeclipse.genealogy.model.*;

/**
 * A label provider for {@link GenealogyZestView}.
 */
class GenealogyZestLabelProvider extends LabelProvider
	implements IColorProvider, IFigureProvider, ISelfStyleProvider, IEntityStyleProvider,
	IEntityConnectionStyleProvider
{
	private final Color backgroundColor;

	public GenealogyZestLabelProvider(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	//==============================================================
	// ILabelProvider

	/**
	 * Answer the text to be displayed for the specified model element
	 */
	public String getText(Object element) {
		StringBuilder builder = new StringBuilder();

		if (element instanceof Person) {
			Person person = (Person) element;
			builder.append(person.getName());
			builder.append('\n');
			builder.append(person.getBirthYear() + " -");
			if (person.getDeathYear() != -1) {
				builder.append(' ');
				builder.append(person.getDeathYear());
			}
		}

		//
		// No longer used because this class implements IFigureProvider
		// and implements getFigure(...) which returns a custom figure
		// for Marriage model elements
		//
		// else if (element instanceof Marriage) {
		// 	Marriage marriage = (Marriage) element;
		// 	builder.append("Married\n");
		// 	builder.append(marriage.getYearMarried());
		// }

		//
		// Import org.eclipse.zest.core.viewers.EntityConnectionData
		// and uncomment this section to provide labels for connections
		//
		//	else if (element instanceof EntityConnectionData) {
		//		EntityConnectionData conn = (EntityConnectionData) element;
		//		if (conn.source instanceof Person)
		//			builder.append("Spouse");
		//		else
		//			builder.append("Offspring");
		//	}

		if (element instanceof Note) {
			Note n = (Note) element;
			builder.append("Note:\n");
			builder.append(n.getText());
		}

		return builder.toString();
	}

	/**
	 * If the element is a person, return the associated image
	 */
	public Image getImage(Object element) {
		if (element instanceof Person) {
			Person person = (Person) element;
			if (person.getGender() == Person.Gender.MALE)
				return PersonFigure.MALE;
			else
				return PersonFigure.FEMALE;
		}
		return null;
	}

	//==============================================================
	// IColorProvider

	/**
	 * Answer the foreground color used to display text for the specified model element
	 */
	public Color getForeground(Object element) {
		return ColorConstants.black;
	}

	/**
	 * Answer the background color used when drawing the node for the specified model
	 * element
	 */
	public Color getBackground(Object element) {
		return backgroundColor;
	}

	//==============================================================
	// IFigureProvider

	/**
	 * Answer a custom figure for the specified model element or <code>null</code> for a
	 * default Zest figure
	 */
	public IFigure getFigure(Object element) {
		if (element instanceof Marriage) {
			Marriage m = (Marriage) element;
			MarriageFigure f = new MarriageFigure(m.getYearMarried());
			f.setSize(f.getPreferredSize());
			return f;
		}
		return null;
	}

	//==============================================================
	// ISelfStyleProvider

	/**
	 * Customize the newly created connection
	 */
	public void selfStyleConnection(Object element, GraphConnection connection) {
		IFigure nodeFigure;
		Connection connectionFigure = connection.getConnectionFigure();
		nodeFigure = connection.getSource().getNodeFigure();
		if (nodeFigure instanceof MarriageFigure) {
			connectionFigure.setSourceAnchor(new MarriageAnchor(nodeFigure));
			connectionFigure.setToolTip(new Label("offspring"));
		}
		nodeFigure = connection.getDestination().getNodeFigure();
		if (nodeFigure instanceof MarriageFigure) {
			connectionFigure.setTargetAnchor(new MarriageAnchor(nodeFigure));
			connectionFigure.setToolTip(new Label("spouse"));
		}
	}

	/**
	 * Customize the newly created node
	 */
	public void selfStyleNode(Object element, GraphNode node) {
	}

	//==============================================================
	// IEntityStyleProvider

	/**
	 * Answer the background color used when the node is selected
	 */
	public Color getNodeHighlightColor(Object entity) {
		return ColorConstants.lightGreen;
	}

	/**
	 * Answer the node's border color
	 */
	public Color getBorderColor(Object entity) {
		return null;
	}

	/**
	 * Answer the border color used when a node is selected
	 */
	public Color getBorderHighlightColor(Object entity) {
		return null;
	}

	/**
	 * Answer the node's border width in pixels
	 */
	public int getBorderWidth(Object entity) {
		return 1;
	}

	/**
	 * Answer the node's background color. If the {@link IColorProvider} interface is
	 * implemented by the label provider, then the color returned by the
	 * {@link IColorProvider#getBackground(Object)} method supersedes the value returned
	 * by this method.
	 */
	public Color getBackgroundColour(Object entity) {
		return ColorConstants.darkGreen;
	}

	/**
	 * Answer the node's foreground color. If the {@link IColorProvider} interface is
	 * implemented by the label provider, then the color returned by the
	 * {@link IColorProvider#getForeground(Object)} method supersedes the value returned
	 * by this method.
	 */
	public Color getForegroundColour(Object entity) {
		return null;
	}

	/**
	 * Answer the figure to be displayed as a tooltip when the user hovers over the node
	 * or connection representing the specified model element. If the
	 * {@link #fisheyeNode(Object)} method returns true for this same model element, then
	 * this method will not be called for that model element and no tool tip figure will
	 * be displayed.
	 */
	public IFigure getTooltip(Object entity) {
		if (entity instanceof Person) {
			Person p = (Person) entity;
			StringBuilder builder = new StringBuilder();
			for (Note n : p.getNotes()) {
				if (builder.length() > 0)
					builder.append("\n\n");
				builder.append(n.getText());
			}
			return new NoteFigure(builder.toString());
		}
		return null;
	}

	/**
	 * Answer true if the node representing the specified model element should be
	 * displayed slightly larger (have the "fish eye" effect) when the user hovers over
	 * that node.
	 */
	public boolean fisheyeNode(Object entity) {
		return false;
	}

	//==============================================================
	// IEntityConnectionStyleProvider

	/**
	 * Return the style for the connection between the two specified model elements. Valid
	 * flags are those constants in {@link ZestStyles} that begin with "CONNECTION_".
	 */
	public int getConnectionStyle(Object src, Object dest) {
		if (src instanceof Person)
			return ZestStyles.CONNECTIONS_DIRECTED;
		return ZestStyles.CONNECTIONS_DIRECTED | ZestStyles.CONNECTIONS_DASH;
	}

	/**
	 * Return the color for the connection between the two specified model elements.
	 */
	public Color getColor(Object src, Object dest) {
		return ColorConstants.black;
	}

	/**
	 * Return the color for the connection between the two specified model elements when
	 * that connection is highlighted.
	 */
	public Color getHighlightColor(Object src, Object dest) {
		return ColorConstants.lightGreen;
	}

	/**
	 * Return the width of the connection in pixels or -1 for the default width.
	 */
	public int getLineWidth(Object src, Object dest) {
		return -1;
	}
}
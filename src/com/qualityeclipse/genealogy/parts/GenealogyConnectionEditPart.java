package com.qualityeclipse.genealogy.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.gef.requests.GroupRequest;

import com.qualityeclipse.genealogy.commands.CreateConnectionCommand;
import com.qualityeclipse.genealogy.commands.CreateOffspringConnectionCommand;
import com.qualityeclipse.genealogy.commands.CreateSpouseConnectionCommand;
import com.qualityeclipse.genealogy.commands.DeleteGenealogyConnectionCommand;
import com.qualityeclipse.genealogy.commands.RequestOwnedCommand;
import com.qualityeclipse.genealogy.model.connection.GenealogyConnection;

/**
 * The {@link EditPart} for the {@link GenealogyConnection} model object. This EditPart is
 * responsible for creating the connection between the person and marriage figures.
 */
public class GenealogyConnectionEditPart extends AbstractConnectionEditPart
{
	private static final PointList ARROWHEAD = new PointList(new int[]{
		0, 0, -2, 2, -2, 0, -2, -2, 0, 0
	});

	public GenealogyConnectionEditPart(GenealogyConnection marriageConnection) {
		setModel(marriageConnection);
	}

	public GenealogyConnection getModel() {
		return (GenealogyConnection) super.getModel();
	}

	/**
	 * Create a new connection between the person and marriage referenced by the
	 * reciever's model object. The arrow head fill color depends upon whether the person
	 * is a parent or an offspring in the referenced marriage.
	 */
	protected IFigure createFigure() {
		return createFigure(getModel().isOffspringConnection());
	}

	/**
	 * Create a new connection figure. The arrow head fill color depends upon whether the
	 * person is a parent or an offspring in the referenced marriage.
	 */
	public static Connection createFigure(boolean isOffspringConnection) {
		PolylineConnection connection = new PolylineConnection();
		connection.setConnectionRouter(new BendpointConnectionRouter());
		connection.setLineWidth(3);
		connection.setAntialias(1);

		// Add an arrowhead decoration
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(ARROWHEAD);
		decoration.setBackgroundColor(isOffspringConnection
			? ColorConstants.white
			: ColorConstants.darkGray);
		connection.setTargetDecoration(decoration);

		return connection;
	}
	
	public PolylineConnection getConnection() {
		return (PolylineConnection)getFigure();
	}
	
	public BendpointConnectionRouter getConnectionRouter() {
		return (BendpointConnectionRouter)getConnection().getConnectionRouter();
	}
	
	private void addBendpoint(BendpointRequest bendpointRequest) {
		BendpointConnectionRouter connectionRouter = getConnectionRouter();
		@SuppressWarnings("unchecked")
		List<Bendpoint> bendpoints = (List<Bendpoint>)connectionRouter.getConstraint(getConnection());
		if ( bendpoints == null ) {
			bendpoints = new ArrayList<Bendpoint>();
			connectionRouter.setConstraint(getConnection(), bendpoints);
		}
		
		AbsoluteBendpoint bp = new AbsoluteBendpoint(bendpointRequest.getLocation());
		bendpoints.add(bendpointRequest.getIndex(), bp);
//		connectionRouter.invalidate(getConnection());
	}

	/**
	 * Answer a new command to recreate the receiver
	 */
	public CreateConnectionCommand recreateCommand() {
		CreateConnectionCommand cmd;
		if (getModel().isOffspringConnection()) {
			cmd = new CreateOffspringConnectionCommand(getModel().marriage);
			cmd.setTarget(getModel().person);
		}
		else {
			cmd = new CreateSpouseConnectionCommand(getModel().person);
			cmd.setTarget(getModel().marriage);
		}
		return cmd;
	}

	/**
	 * Extend the superclass implementation to provide MarriageAnchor for MarriageFigures
	 * so that the connection terminates along the outside of the MarriageFigure rather
	 * than at the MarriageFigure's bounding box.
	 */
	protected ConnectionAnchor getSourceConnectionAnchor() {
		/*
		 * Rather than implementing the getSourceConnectionAnchor() in this class,
		 * modify MarriageEditPart to implement the NodeEditPart interface
		 */
		//if (getSource() instanceof MarriageEditPart) {
		//	MarriageEditPart editPart = (MarriageEditPart) getSource();
		//	return new MarriageAnchor(editPart.getFigure());
		//}
		return super.getSourceConnectionAnchor();
	}

	/**
	 * Extend the superclass implementation to provide MarriageAnchor for MarriageFigures
	 * so that the connection terminates along the outside of the MarriageFigure rather
	 * than at the MarriageFigure's bounding box.
	 */
	protected ConnectionAnchor getTargetConnectionAnchor() {
		/*
		 * Rather than implementing the getSourceConnectionAnchor() in this class,
		 * modify MarriageEditPart to implement the NodeEditPart interface
		 */
		//if (getTarget() instanceof MarriageEditPart) {
		//	MarriageEditPart editPart = (MarriageEditPart) getTarget();
		//	return new MarriageAnchor(editPart.getFigure());
		//}
		return super.getTargetConnectionAnchor();
	}

	/**
	 * Create and install {@link EditPolicy} instances used to define behavior
	 * associated with this EditPart's figure.
	 */
	protected void createEditPolicies() {
		ConnectionEndpointEditPolicy selectionPolicy = new ConnectionEndpointEditPolicy();
		
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, selectionPolicy);

		// Handles deleting the receiver
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnectionEditPolicy() {
			@Override
			protected Command getDeleteCommand(GroupRequest request) {
				return new DeleteGenealogyConnectionCommand(getModel());
			}
		});
		
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new BendpointEditPolicy() {
			@Override
			protected Command getMoveBendpointCommand(BendpointRequest request) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected Command getDeleteBendpointCommand(BendpointRequest request) {
				// TODO Auto-generated method stub
				return null;
			}
			
			final RequestOwnedCommand<BendpointRequest> addBendpointCommand = new RequestOwnedCommand<BendpointRequest>() {
				public boolean canExecute() {
					return true;
				}
				
				public void execute() {
					addBendpoint(getRequest());
				}
			};
			
			@Override
			protected Command getCreateBendpointCommand(BendpointRequest request) {
				System.out.println("AAAAAAAAAAAAAAA" + request.getLocation());
				addBendpointCommand.setRequest(request);
				return addBendpointCommand;
			}
		});
	}
}

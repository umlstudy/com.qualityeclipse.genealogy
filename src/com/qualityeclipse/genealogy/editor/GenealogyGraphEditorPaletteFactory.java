package com.qualityeclipse.genealogy.editor;

import java.util.*;

import org.eclipse.gef.palette.*;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.qualityeclipse.genealogy.figures.*;
import com.qualityeclipse.genealogy.model.*;

/**
 * A factory for constructing the {@link GenealogyGraphEditor}'s palette
 */
public class GenealogyGraphEditorPaletteFactory
{
	private static final ImageDescriptor FEMALE_IMAGE_DESCRIPTOR 
							= ImageDescriptor.createFromImage(PersonFigure.FEMALE);
	private static final ImageDescriptor MALE_IMAGE_DESCRIPTOR 
							= ImageDescriptor.createFromImage(PersonFigure.MALE);
	private static final ImageDescriptor MARRIAGE_IMAGE_DESCRIPTOR 
							= ImageDescriptor.createFromImage(MarriageFigure.MARRIAGE_IMAGE);
	private static final ImageDescriptor NOTE_IMAGE_DESCRIPTOR 
							= ImageDescriptor.createFromImage(NoteFigure.NOTE_IMAGE);
	private static final ImageDescriptor CONNECTION_IMAGE_DESCRIPTOR
							= ImageDescriptor.createFromImage(MarriageFigure.CONNECTION_IMAGE);
	
	/**
	 * Construct and return a new palette for a {@link GenealogyGraphEditor}
	 */
	public static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		palette.add(createElementsDrawer());
		return palette;
	}

	/**
	 * Create a toolbar containing the common tools that appear at the top of the palette
	 * and set the selection tool as the default tool.
	 */
	private static PaletteEntry createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");
	
		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);
	
		// Add a marquee tool to the group
		toolbar.add(new MarqueeToolEntry());
		
		return toolbar;
	}

	/**
	 * Create a drawer containing tools to add the various genealogy model elements
	 */
	private static PaletteEntry createElementsDrawer() {
		final int currentYear = new GregorianCalendar().get(Calendar.YEAR);
		
		PaletteDrawer componentsDrawer = new PaletteDrawer("Elements");
		
		// Add a factory and tool for creating women
		SimpleFactory factory = new SimpleFactory(Person.class) {
			public Object getNewObject() {
				Person person = new Person(Person.Gender.FEMALE);
				person.setName("Jane Smith");
				person.setBirthYear(currentYear);
				return person;
			}
		};
		CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
			"Woman",
			"Add a new female to the Genealogy Graph",
			factory, 
			FEMALE_IMAGE_DESCRIPTOR, 
			FEMALE_IMAGE_DESCRIPTOR);
		componentsDrawer.add(component);

		// Add a tool for creating men
		factory = new SimpleFactory(Person.class) {
			public Object getNewObject() {
				Person person = new Person(Person.Gender.MALE);
				person.setName("John Doe");
				person.setBirthYear(currentYear);
				return person;
			}
		};
		component = new CombinedTemplateCreationEntry(
			"Man",
			"Place a new male onto the Genealogy Graph",
			factory,
			MALE_IMAGE_DESCRIPTOR, 
			MALE_IMAGE_DESCRIPTOR);
		componentsDrawer.add(component);

		// Add a tool for creating marriages
		factory = new SimpleFactory(Marriage.class) {
			public Object getNewObject() {
				return new Marriage(currentYear);
			}
		};
		component = new CombinedTemplateCreationEntry(
			"Marriage",
			"Place a new marriage onto the Genealogy Graph",
			factory,
			MARRIAGE_IMAGE_DESCRIPTOR,
			MARRIAGE_IMAGE_DESCRIPTOR);
		componentsDrawer.add(component);

		// Add a tool for creating notes	
		factory = new SimpleFactory(Note.class) {
			public Object getNewObject() {
				return new Note("A note with default text");
			}
		};
		component = new CombinedTemplateCreationEntry(
			"Note",
			"Create a new Note",
			factory,
			NOTE_IMAGE_DESCRIPTOR,
			NOTE_IMAGE_DESCRIPTOR);
		componentsDrawer.add(component);
		
		// Add a tool for creating connections
		ToolEntry connection = new ConnectionCreationToolEntry(
			"Connection",
			"Create a connection", 
			null,
			CONNECTION_IMAGE_DESCRIPTOR,
			CONNECTION_IMAGE_DESCRIPTOR);
		componentsDrawer.add(connection);
		
		return componentsDrawer;
	}
}

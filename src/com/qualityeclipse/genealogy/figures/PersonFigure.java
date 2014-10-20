package com.qualityeclipse.genealogy.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;

/**
 * A custom figure for the GenealogyView displaying a person's information
 */
public class PersonFigure extends Figure {

	public static final Image MALE = new Image(Display.getCurrent(),
			PersonFigure.class.getResourceAsStream("male.png"));
	public static final Image FEMALE = new Image(Display.getCurrent(),
		PersonFigure.class.getResourceAsStream("female.png"));

	private final Label nameFigure;
	private final Label datesFigure;
	private final IFigure notesContainer;
	private final LineBorder lineBorder;

	public PersonFigure(String name, Image image, int birthYear, int deathYear) {
		final ToolbarLayout layout = new ToolbarLayout();
		layout.setSpacing(1);
		setLayoutManager(layout);
		setPreferredSize(100, 100);
		lineBorder = new LineBorder(1);
		setBorder(new CompoundBorder(lineBorder, new MarginBorder(2, 2, 2, 2)));

		// Display the image to the left of the name/date
		IFigure imageNameDates = new Figure();
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 1;
		imageNameDates.setLayoutManager(gridLayout);
		add(imageNameDates);
		imageNameDates.add(new ImageFigure(image));

		// Display the name and date to right of image
		IFigure nameDates = new Figure();
		nameDates.setLayoutManager(new ToolbarLayout());
		imageNameDates.add(nameDates, new GridData(GridData.FILL_HORIZONTAL));
		nameFigure = new Label(name);
		nameDates.add(nameFigure);

		// Display the year of birth and death
		datesFigure = new Label();
		nameDates.add(datesFigure);
		setBirthAndDeathYear(birthYear, deathYear);

		// Add a container for notes
		notesContainer = new Figure();
		final ToolbarLayout notesLayout = new ToolbarLayout();
		notesLayout.setSpacing(1);
		notesContainer.setLayoutManager(notesLayout);
		add(notesContainer);
	}

	public IFigure getNotesContainer() {
		return notesContainer;
	}

	public void paintFigure(Graphics graphics) {
		Rectangle r = getBounds();
		graphics.setBackgroundPattern(new Pattern(Display.getCurrent(), r.x,
				r.y, r.x + r.width, r.y + r.height, ColorConstants.white,
				ColorConstants.lightGray));
		graphics.fillRectangle(r);
	}

	public void setName(String newName) {
		nameFigure.setText(newName);
	}

	public void setBirthAndDeathYear(int birthYear, int deathYear) {
		String datesText = birthYear + " -";
		if (deathYear != -1)
			datesText += " " + deathYear;
		datesFigure.setText(datesText);
	}

	/**
	 * Adjust the receiver's appearance based upon whether the receiver is selected
	 * 
	 * @param selected <code>true</code> if the receiver is selected, else
	 *            <code>false</code>
	 */
	public void setSelected(boolean selected) {
		lineBorder.setColor(selected ? ColorConstants.blue : ColorConstants.black);
		lineBorder.setWidth(selected ? 2 : 1);
		erase();
	}
}

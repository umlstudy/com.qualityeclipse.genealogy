package com.qualityeclipse.genealogy.model.io;

import java.io.PrintWriter;
import java.util.*;

import com.qualityeclipse.genealogy.model.*;

/**
 * Stores information from a {@link GenealogyGraph} to an XML stream
 */
public class GenealogyGraphWriter
{
	private static final String INDENT = "  ";
	private static final String INDENT2 = INDENT + INDENT;
	
	private final GenealogyGraph graph;
	private PrintWriter writer;

	public GenealogyGraphWriter(GenealogyGraph graph) {
		this.graph = graph;
	}

	public void write(PrintWriter writer) {
		this.writer = writer;
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<genealogy>");
		Map<Person, Integer> personToId = writePeople();
		writeMarriages(personToId);
		writeNotes(graph, INDENT);
		writer.println("</genealogy>");
	}

	private Map<Person, Integer> writePeople() {
		HashMap<Person, Integer> personToId = new HashMap<Person, Integer>();
		int id = 0;
		Collection<Person> sorted = new TreeSet<Person>(new Comparator<Person>() {
			public int compare(Person p1, Person p2) {
				int delta = p1.getName().compareTo(p2.getName());
				if (delta == 0)
					delta = p1.hashCode() - p2.hashCode();
				return delta;
			}
		});
		sorted.addAll(graph.getPeople());
		for (Person p : sorted) {
			writer.print(INDENT);
			writer.print("<person");
			writeAttribute("id", id);
			writePresentationInfo(p);
			writeAttribute("name", p.getName());
			writeAttribute("gender", p.getGender().toString());
			writeAttribute("birthYear", p.getBirthYear());
			writeAttribute("deathYear", p.getDeathYear());
			if (p.getNotes().size() > 0) {
				writer.println(">");
				writeNotes(p, INDENT2);
				writer.print(INDENT);
				writer.println("</person>");
			}
			else
				writer.println("/>");
			personToId.put(p, id);
			id++;
		}
		return personToId;
	}

	private void writeMarriages(Map<Person, Integer> personToId) {
		Collection<Marriage> sorted = new TreeSet<Marriage>(new Comparator<Marriage>() {
			public int compare(Marriage m1, Marriage m2) {
				int delta = m1.getYearMarried() - m2.getYearMarried();
				if (delta == 0)
					delta = m1.hashCode() - m2.hashCode();
				return delta;
			}
		});
		sorted.addAll(graph.getMarriages());
		for (Marriage m : sorted) {
			writer.print(INDENT);
			writer.print("<marriage");
			writePresentationInfo(m);
			writeAttribute("yearMarried", m.getYearMarried());
			writeAttribute(personToId, "husbandId", m.getHusband());
			writeAttribute(personToId, "wifeId", m.getWife());
			if (m.getOffspring().size() > 0) {
				writer.println(">");
				writeOffspring(personToId, m);
				writer.print(INDENT);
				writer.println("</marriage>");
			}
			else
				writer.println("/>");
		}
	}

	private void writeOffspring(Map<Person, Integer> personToId, Marriage m) {
		for (Person p : m.getOffspring()) {
			writer.print(INDENT2);
			writer.print("<offspring");
			writeAttribute(personToId, "childId", p);
			writer.println("/>");
		}
	}

	private void writeAttribute(Map<Person, Integer> personToId, String key, Person value) {
		if (value != null)
			writeAttribute(key, personToId.get(value));
	}

	private void writeNotes(NoteContainer container, String indent) {
		for (Note n : container.getNotes()) {
			writer.print(indent);
			writer.print("<note");
			writePresentationInfo(n);
			writer.print(">");
			writeEscapedText(n.getText());
			writer.println("</note>");
		}
	}

	private void writePresentationInfo(GenealogyElement elem) {
		writeAttribute("x", elem.getX());
		writeAttribute("y", elem.getY());
		writeAttribute("width", elem.getWidth());
		writeAttribute("height", elem.getHeight());
	}

	private void writeAttribute(String key, int value) {
		writer.print(" ");
		writer.print(key);
		writer.print("=\"");
		writer.print(value);
		writer.print("\"");
	}

	private void writeAttribute(String key, String value) {
		writer.print(" ");
		writer.print(key);
		writer.print("=\"");
		writeEscapedText(value);
		writer.print("\"");
	}

	private void writeEscapedText(String text) {
		int length = text.length();
		for (int i = 0; i < length; i++) {
			char character = text.charAt(i);
			switch (character) {
				case '<' :
					writer.print("&lt;");
					break;
				case '>' :
					writer.print("&gt;");
					break;
				case '&' :
					writer.print("&amp;");
					break;
				case '\"' :
					writer.print("&quot;");
					break;
				default :
					writer.print(character);
					break;
			}
		}
	}
}

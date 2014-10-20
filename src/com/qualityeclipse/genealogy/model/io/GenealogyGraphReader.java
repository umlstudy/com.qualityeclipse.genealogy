package com.qualityeclipse.genealogy.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.qualityeclipse.genealogy.model.GenealogyElement;
import com.qualityeclipse.genealogy.model.GenealogyGraph;
import com.qualityeclipse.genealogy.model.Marriage;
import com.qualityeclipse.genealogy.model.Note;
import com.qualityeclipse.genealogy.model.Person;

/**
 * Loads information from an XML stream into a {@link GenealogyGraph}
 */
public class GenealogyGraphReader extends DefaultHandler
{
	private final GenealogyGraph graph;
	private Map<Integer, Person> idToPerson;
	private Person currentPerson;
	private Note currentNote;
	private Marriage currentMarriage;

	public GenealogyGraphReader(GenealogyGraph graph) {
		this.graph = graph;
	}

	public void read(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		idToPerson = new HashMap<Integer, Person>();
		parser.parse(stream, this);
		resolveRelationships();
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("person")) {
			Person p = new Person(Person.Gender.valueOf(attributes.getValue("gender")));
			idToPerson.put(readInt(attributes, "id"), p);
			p.setName(attributes.getValue("name"));
			p.setBirthYear(readInt(attributes, "birthYear"));
			p.setDeathYear(readInt(attributes, "deathYear"));
			readGenealogyElementAttributes(p, attributes);
			graph.addPerson(p);
			currentPerson = p;
		}
		else if (qName.equals("marriage")) {
			Marriage m = new Marriage(readInt(attributes, "yearMarried"));
			// ASSUME that all person objects have been created
			m.setHusband(idToPerson.get(readInt(attributes, "husbandId")));
			m.setWife(idToPerson.get(readInt(attributes, "wifeId")));
			readGenealogyElementAttributes(m, attributes);
			graph.addMarriage(m);
			currentMarriage = m;
		}
		else if (qName.equals("offspring")) {
			// ASSUME that all person objects have been created
			currentMarriage.addOffspring(idToPerson.get(readInt(attributes, "childId")));
		}
		else if (qName.equals("note")) {
			Note n = new Note(attributes.getValue("text"));
			readGenealogyElementAttributes(n, attributes);
			if (currentPerson != null)
				currentPerson.addNote(n);
			else
				graph.addNote(n);
			currentNote = n;
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (currentNote != null)
			currentNote.setText(new String(ch, start, length));
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("person"))
			currentPerson = null;
		else if (qName.equals("marriage"))
			currentMarriage = null;
		else if (qName.equals("note"))
			currentNote = null;
	}

	private void readGenealogyElementAttributes(GenealogyElement elem, Attributes attributes) {
		elem.setLocation(readInt(attributes, "x"), readInt(attributes, "y"));
		elem.setSize(readInt(attributes, "width"), readInt(attributes, "height"));
	}

	private int readInt(Attributes attributes, String key) {
		String value = attributes.getValue(key);
		if (value == null)
			return -1;
		return Integer.parseInt(value);
	}

	private void resolveRelationships() {
		// TODO Resolve parent - marriage - child relationships
	}
}

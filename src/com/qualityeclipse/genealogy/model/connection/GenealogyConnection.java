package com.qualityeclipse.genealogy.model.connection;

import java.util.HashMap;

import com.qualityeclipse.genealogy.model.*;

/**
 * A transient model object representing a connection between a {@link Person} and a
 * {@link Marriage}. This class is NOT a first class genealogy model element and NOT
 * persisted, but simply represents the connection information already encoded in the
 * genealogy model.
 */
public class GenealogyConnection
{
	public final Person person;
	public final Marriage marriage;

	public GenealogyConnection(Person person, Marriage marriage) {
		this.person = person;
		this.marriage = marriage;
	}

	/**
	 * Implement equals so that different instances this class referencing the same person
	 * and same marriage will be considered the equal.
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof GenealogyConnection))
			return false;
		GenealogyConnection conn = (GenealogyConnection) obj;
		return conn.person == person && conn.marriage == marriage;
	}

	/**
	 * Answer a hash code based upon the person and marriage so that different instances
	 * with the same person and same marriage will have the same hashCode and thus this
	 * class can be used as a key in a {@link HashMap}
	 */
	public int hashCode() {
		int hash = 0;
		if (person != null)
			hash += person.hashCode();
		if (marriage != null)
			hash += marriage.hashCode();
		return hash;
	}

	/**
	 * Determine if the person referenced by the receiver is an offspring of the marriage.
	 * 
	 * @return true if the person referenced by the receiver is an offspring of the
	 *         marriage, or false if the person is a parent in the marriage.
	 */
	public boolean isOffspringConnection() {
		return marriage != null && marriage.getOffspring().contains(person);
	}
}

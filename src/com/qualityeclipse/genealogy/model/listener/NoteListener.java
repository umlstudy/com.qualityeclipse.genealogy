package com.qualityeclipse.genealogy.model.listener;

import com.qualityeclipse.genealogy.model.Note;

/**
 * Used by {@link Note} to notify others when changes occur.
 */
public interface NoteListener
	extends GenealogyElementListener
{
	void textChanged(String text);
}

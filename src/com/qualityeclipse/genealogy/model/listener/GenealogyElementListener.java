package com.qualityeclipse.genealogy.model.listener;

import com.qualityeclipse.genealogy.model.GenealogyElement;

/**
 * Interface used by {@link GenealogyElement} to notify others when changes occur.
 */
public interface GenealogyElementListener
{
	void locationChanged(int x, int y);
	void sizeChanged(int width, int height);
}

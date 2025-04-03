package org.jmarkdownviewer.jmdviewer.service;

import java.io.File;
import java.io.IOException;

import javax.swing.text.JTextComponent;

public interface DocService {

	public String load(File file, JTextComponent textcomp);
	
	public String loadRaw(File file);
	
	public void exportHTML(File source, File target) throws IOException;
	
	public void exportText(File source, File target) throws IOException;
}

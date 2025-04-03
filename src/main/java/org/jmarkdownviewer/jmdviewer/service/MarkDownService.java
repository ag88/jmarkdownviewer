package org.jmarkdownviewer.jmdviewer.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;

import org.commonmark.node.Node;
import org.jmarkdownviewer.jmdviewer.App;
import org.jmarkdownviewer.jmdviewer.parser.MarkdownParser;

public class MarkDownService implements DocService {

	Node document;

	public MarkDownService() {
	}

	@Override
	public String load(File file, JTextComponent textcomp) {
		MarkdownParser parser = new MarkdownParser();
		String parent;
		try {
			file = file.getCanonicalFile();
			parent = file.getParentFile().getCanonicalPath();
		} catch (IOException e1) {
			parent = App.getInstance().getLastdir();
		}
		parser.parse(file);
		document = parser.getDocument();
		parser.updatefileimages(parent);
		String yaml = parser.getYAML();

		String html = parser.getHTML();
		if (html != null) {
			try {
				URL url = new URL("file", "", parent);
				// System.out.println(url.toString());
				HTMLDocument doc = (HTMLDocument) textcomp.getDocument();
				doc.setBase(url);
				textcomp.setDocument(doc);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (yaml != null && yaml != "") {
				StringBuilder sb = new StringBuilder(1024);
				int bi = html.indexOf("<body>");
				if (bi >= 0) {
					bi += "<body>".length();
					sb.append(html.substring(0, bi));
				} else
					bi = 0;
				sb.append("<pre>");
				sb.append(yaml);
				sb.append("</pre>");
				sb.append(html.substring(bi));
				html = sb.toString();
			}
		}
		return html;
	}
	
	@Override
	public String loadRaw(File file) {
		StringBuilder sb = new StringBuilder(1000);
		if(file == null) 
			return "";
	
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null ) {
				sb.append(line);
				sb.append('\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public void exportHTML(File source, File target) throws IOException {

		MarkdownParser parser = new MarkdownParser();
		if (source == null)
			return;

		parser.parse(source);
		String html = parser.getHTML();
		if (html != null && html != "") {
			BufferedWriter writer = new BufferedWriter(new FileWriter(target));
			writer.write("<html>\n<body>\n");
			writer.write(html);
			writer.write("</body>\n</html>\n");
			writer.flush();
			writer.close();
		}
	}

	@Override
	public void exportText(File source, File target) throws IOException {
		MarkdownParser parser = new MarkdownParser();
		if (source == null)
			return;

		parser.parse(source);
		String text = parser.getText();		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(target));
		writer.write(text);
		writer.flush();
		writer.close();
	}
}

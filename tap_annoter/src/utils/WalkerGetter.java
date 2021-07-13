/* class used to get a walker */

package utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.traversal.*;
import org.xml.sax.SAXException;

public class WalkerGetter {
	
	private File fileToGet;
	
	public WalkerGetter(File ourFile) {
		
		this.fileToGet = ourFile;
		
	}
	
	public TreeWalker getWalker() {
		
		Document ourDoc = null;
	    DocumentBuilderFactory factory = null;
	    factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		
		try {
			builder = factory.newDocumentBuilder();
			ourDoc = builder.parse(fileToGet);
			DocumentTraversal traversal = (DocumentTraversal) ourDoc;
		    TreeWalker walker = traversal.createTreeWalker(
		            ourDoc.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);
		    walker.getRoot();
		    return walker;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

		
	}
}

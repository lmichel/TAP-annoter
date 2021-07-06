package mapper;

import java.io.*;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.xml.sax.SAXException;

import utils.FileGetter;
import utils.TreeWalkerMover;
import utils.WalkerGetter;


public class DetectionFlagAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	private String ucd;
	private String semantic;
	private String description;
	private String frame;
	private String coordValue;
	private TreeWalkerMover walker;
	
	public DetectionFlagAppender(JSONObject json, File mango,TreeWalkerMover walker) {
		
			this.ourMeasure = json;
			this.mangoFile = mango;
			this.walker = walker;
	}
	
	public void AppendDetectionFlag(Document templateDoc) {
		
		GetParameters();
		
		//checking globals and putting them if needed
		if (!AreGlobalsSet()) {
			setGlobal(templateDoc);
		}
		
		WalkerGetter getter = new WalkerGetter(mangoFile);
		TreeWalkerMover mangoWalker = getter.getWalker();
		
		mangoWalker.goToTableMapping(); //We go to table mapping
		
		setParameters(mangoWalker);
		
		mangoWalker.goToTableMapping();//going back to table mapping to add the good node in global walker
		
		walker.goToCollectionParameters();
		
		walker.appendConfig(mangoWalker, templateDoc);
		
		System.out.println("Detection flag added");
	}

	public void GetParameters() {
		
		System.out.println("Getting parameters");
		this.ucd = (String) ourMeasure.get("ucd");
		System.out.println("ucd : " + ucd);
		this.semantic = (String) ourMeasure.get("semantic");
		System.out.println("semantic : " + semantic);
		this.description = (String) ourMeasure.get("description");
		System.out.println("description : " + description);
		JSONObject frames = (JSONObject) ourMeasure.get("frame");
		System.out.println("Got frame");
		this.frame = (String) frames.get("frame");
		System.out.println("frame : " + frame);
		JSONObject coordinate = (JSONObject) ourMeasure.get("coordinate");
		System.out.println("Got coordinate");
		this.coordValue = (String) coordinate.get("value");
		coordValue = coordValue.replace("@", "");
		System.out.println("value :" + coordValue);
		
		
	}
	
	public boolean AreGlobalsSet() {
		
		walker.goToRoot();
		Node ourGlobals = walker.firstChild();
		NodeList nodeList = ourGlobals.getChildNodes();
		int nodeNumber = nodeList.getLength();
		
		if (nodeNumber==0) return false;
		
		else {
			for (int i=0;i<nodeNumber;i++) {
				
				Node currentNode = nodeList.item(i);
				Element currentElement = (Element) currentNode;
				String ourFrame = currentElement.getAttribute("ID");
				
				if (ourFrame.equals("StatusFrame_"+frame)) return true;
			}
		}
		System.out.println("Globals are not set, we are setting them right now !");
		return false;
	}
	
	public void setGlobal(Document templateDoc) {
		
		FileGetter getter = new FileGetter("mango.frame."+frame+".xml");

		try {
			File frameFile = getter.GetFile();
			WalkerGetter gettingWalker = new WalkerGetter(frameFile);
			TreeWalker frameWalker = gettingWalker.getWalker();
			System.out.println(((Element) frameWalker.getRoot()).getTagName());
			walker.goToRoot();
			Node globals = walker.firstChild();
			Node importedNode = templateDoc.importNode(frameWalker.getRoot(), true);
			globals.appendChild(importedNode);

			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	    
	}
	
	public void setParameters(TreeWalkerMover mangoComponentWalker) {

		String currentdmrole = "notcoords";
		while (!(currentdmrole.equals("coords:Coordinate.coordSys"))) {
			//setting all parameters
			Node currentNode = mangoComponentWalker.getCurrentNode();
			
			Element currentElement = (Element) currentNode;
			currentdmrole = currentElement.getAttribute("dmrole");
				//to know in which instance we are
			switch(currentdmrole) {

				//updating values
				case("mango:Parameter.semantic"):
					currentElement.setAttribute("value", semantic);
					System.out.println("Setting semantic");
					break;
					
				case("mango:Parameter.ucd"):
					currentElement.setAttribute("value", ucd);
					System.out.println("Setting ucd");
					break;
		
				case("mango:Parameter.description"): 
					currentElement.setAttribute("value", description);
					System.out.println("Setting description");
					break;
			
				case("mango:stcextend.FlagCoord.coord"):
					currentElement.setAttribute("ref", coordValue);
					break;
					
				case("coords:Coordinate.coordSys"):
					currentElement.setAttribute("dmref", "StatusFrame_"+frame);
			}
			mangoComponentWalker.nextNode();
			
		}
		

	}

}


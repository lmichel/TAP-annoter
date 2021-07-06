package mapper;

import java.io.*;
import java.net.URISyntaxException;

import org.json.simple.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;

import utils.FileGetter;
import utils.TreeWalkerMover;
import utils.WalkerGetter;


/**
 * @author joann
 * Class used to append a Detection Flag in the mapping
 */
public class DetectionFlagAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	private String ucd;
	private String semantic;
	private String description;
	private String frame;
	private String coordValue;
	private TreeWalkerMover walker;
	
	/**
	 * @param json, the json config file
	 * @param mango, the mango mapping component
	 * @param walker, the walker, which we have to fill
	 */
	public DetectionFlagAppender(JSONObject json, File mango,TreeWalkerMover walker) {
		
			this.ourMeasure = json;
			this.mangoFile = mango;
			this.walker = walker;
	}
	
	/**
	 * @param templateDoc, the document which we have to fill (needed to import node)
	 * This is the core method of the class, it is used to append a measure of type
	 * DetectionFlag in the xml document
	 */
	public void AppendDetectionFlag(Document templateDoc) {
		
		//we have to get the parameters in the json first
		this.getParameters();
		
		//checking globals and setting them if needed
		if (!areGlobalsSet()) {
			setGlobal(templateDoc);
		}
		
		//getting the walker of mapping component to fill it
		WalkerGetter getter = new WalkerGetter(mangoFile);
		TreeWalkerMover mangoWalker = getter.getWalker();
		
		mangoWalker.goToTableMapping(); //We go to table mapping
		
		setParameters(mangoWalker); //filling the walker with parameters
		
		mangoWalker.goToTableMapping();//going back to table mapping to add the good node in global walker
		
		walker.goToCollectionParameters();//going to the right place in the walker we need to fill with mangoWalker
		
		walker.appendConfig(mangoWalker, templateDoc);//merging mangoWalker in the walker we need to fill
		
		System.out.println("Detection flag added");
	}

	/**
	 * Method used to get the several parameters that are in the json config file
	 */
	public void getParameters() {
		
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
	
	/**
	 * @return a boolean that represents if the globals are already set or not
	 */
	public boolean areGlobalsSet() {
		
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
	
	/**
	 * @param templateDoc the doc (needed for merging)
	 * 
	 * This method is setting globals in the walker we need to fill
	 */
	public void setGlobal(Document templateDoc) {
		
		FileGetter getter = new FileGetter("mango.frame."+frame+".xml"); //getting the frame file

		try {
			File frameFile = getter.GetFile();
			WalkerGetter gettingWalker = new WalkerGetter(frameFile);
			TreeWalker frameWalker = gettingWalker.getWalker();
			walker.goToRoot();
			Node globals = walker.firstChild();
			Node importedNode = templateDoc.importNode(frameWalker.getRoot(), true);
			globals.appendChild(importedNode);

			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param mangoComponentWalker the walker used for the mapping component
	 * This method is setting the parameters in the right attributes
	 */
	public void setParameters(TreeWalkerMover mangoComponentWalker) {

		String currentdmrole = "notcoords";
		
		while (!(currentdmrole.equals("coords:Coordinate.coordSys"))) {
			
			Node currentNode = mangoComponentWalker.getCurrentNode();
			
			Element currentElement = (Element) currentNode;
			//used to know in which instance we are
			currentdmrole = currentElement.getAttribute("dmrole");
				
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
			mangoComponentWalker.nextNode();//going to the next node if not one of the parameters
			
		}
		

	}

}


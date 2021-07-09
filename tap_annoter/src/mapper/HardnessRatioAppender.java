package mapper;

import java.io.*;
import java.net.URISyntaxException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.TreeWalker;

import utils.FileGetter;
import utils.WalkerGetter;

public class HardnessRatioAppender {

	private JSONObject ourMeasure;
	private File mangoComponentFile;
	private TreeWalker walker;
	private String frameName1;
	private String frameName2;
	private String frameName3;
	private String ucd;
	private String semantic;
	private String description;
	private String reductionStatus;
	private String coordValue;
	private String errorValue;
	private String errorUnit;
	
	public HardnessRatioAppender(JSONObject json,File mango,TreeWalker walker) {
		
		this.ourMeasure = json;
		this.mangoComponentFile = mango;
		this.walker = walker;
		
	}
	
	public void AppendHardnessRatio(Document templateDoc) {
		
		//we have to get the parameters in the json first
		this.getParameters();
		
		//checking globals and setting them if needed
		checkAndSetGlobals(templateDoc);
		
		//getting the walker of mapping component to fill it
		WalkerGetter getter = new WalkerGetter(this.mangoComponentFile);
		TreeWalker mangoWalker = getter.getWalker();
		
		this.goToTableMapping(mangoWalker); //We go to table mapping
		
		this.setParameters(mangoWalker); //filling the walker with parameters
		
		this.goToCollectionParameters();//going to the right place in the walker we need to fill with mangoWalker
		
		this.appendConfig(mangoWalker, templateDoc);//merging mangoWalker in the walker we need to fill
		
		System.out.println("LonLatSkyPosition added");
		
	}

	private void setGlobal(Document templateDoc,int frameNumber) {
		
		String frameName="";
		
		switch (frameNumber) {
		
		case 1:
			frameName = "mango.frame."+frameName1+".xml";
			break;
		
		case 2:
			frameName = "mango.frame."+frameName2+".xml";
			break;
			
		case 3:
			frameName = "mango.frame."+frameName3+".xml";
			break;
			
		}
		
		FileGetter getter = new FileGetter(frameName); //getting the frame file
		
		try {
			File frameFile = getter.GetFile();
			WalkerGetter gettingWalker = new WalkerGetter(frameFile);
			TreeWalker frameWalker = gettingWalker.getWalker();
			goToRoot(walker);
			Node globals = walker.firstChild();
			Node importedNode = templateDoc.importNode(frameWalker.getRoot(), true);
			globals.appendChild(importedNode);

			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private void checkAndSetGlobals(Document templateDoc) {
		
		goToRoot(walker);
		Node ourGlobals = walker.firstChild();
		NodeList nodeList = ourGlobals.getChildNodes();
		int nodeNumber = nodeList.getLength();
		boolean frame1Missing = true;
		boolean frame2Missing = true;
		boolean frame3Missing = true;
		
		for (int i=0;i<nodeNumber;i++) {
				
				Node currentNode = nodeList.item(i);
				Element currentElement = (Element) currentNode;
				String ourFrame = currentElement.getAttribute("ID");
				
				if (ourFrame.equals("PhotFrame_"+frameName1)) {
					frame1Missing = false;
				}
				if (ourFrame.equals("PhotFrame_"+frameName2)) {
					frame2Missing = false;
				}
				if (ourFrame.equals("PhotFrame_"+frameName3)) {
					frame3Missing = false;
				}
			}
		
		if (frame1Missing) {
			setGlobal(templateDoc,1);
		}
		
		if (frame2Missing) {
			setGlobal(templateDoc,2);
		}
		
		if (frame3Missing) {
			setGlobal(templateDoc,3);
		}
}

	private void getParameters() {
		
		System.out.println("Getting parameters");
		
		this.ucd = (String) ourMeasure.get("ucd");
		System.out.println("ucd : " + ucd);
		
		this.semantic = (String) ourMeasure.get("semantic");
		System.out.println("semantic : " + semantic);
		
		this.description = (String) ourMeasure.get("description");
		System.out.println("description : " + description);
		
		this.reductionStatus = (String) ourMeasure.get("reductionStatus");
		System.out.println("reductionStatus : " + reductionStatus);
		
		JSONObject frame = (JSONObject) ourMeasure.get("frame");
		JSONArray frameList = (JSONArray) frame.get("frame");
		
		this.frameName1 = (String) frameList.get(0);
		this.frameName2 = (String) frameList.get(1);
		this.frameName3 = (String) frameList.get(2);
		
		System.out.println("Got frames");
		
		JSONObject coord = (JSONObject) ourMeasure.get("coordinate");
		this.coordValue = (String) coord.get("value");
		
		JSONObject error = (JSONObject) ourMeasure.get("error");
		JSONObject random = (JSONObject) error.get("random");
		
		if (random==null) {
			this.errorValue="";
			this.errorUnit="";
		}
		
		else {
		this.errorValue = (String) random.get("value");
		errorValue = errorValue.replace("@","");
		
		this.errorUnit = (String) random.get("unit");
		}
		
		if (this.errorUnit==null) {
			this.errorUnit="";
		}
		
		if (this.errorValue==null) {
			this.errorValue="";
		}
	}

	public void setParameters(TreeWalker mangoComponentWalker) {
			
			
			String currentdmrole = "notivoa";
			
			while (!(currentdmrole.equals("ivoa:Quantity.unit"))) {
	
				Node currentNode = mangoComponentWalker.getCurrentNode();
				Element currentElement = (Element) currentNode;
				
				//to know in which instance we are
				currentdmrole = currentElement.getAttribute("dmrole");
	
				//we want to know where we are because systematic and random unit/values have same dmrole
				switch(currentdmrole) {
				
					//updating values
					case("mango:Parameter.semantic"):
						currentElement.setAttribute("value", this.semantic);
						System.out.println("Setting semantic");
						break;
						
					case("mango:Parameter.ucd"):
						currentElement.setAttribute("value", this.ucd);
						System.out.println("Setting ucd");
						break;
			
					case("mango:Parameter.description"): 
						currentElement.setAttribute("value", this.description);
						System.out.println("Setting description");
						break;
				
					case("mango:Parameter.reductionStatus"):
						currentElement.setAttribute("value",this.reductionStatus);
						System.out.println("Setting reductionStatus");
						break;
						
					case("mango:stcextend.HardnessRatioCoord.hardnessRatio"):
						currentElement.setAttribute("ref",this.coordValue);
						System.out.println("Coord value added");
						break;
						
					case("coords:Coordinate.coordSys"):
						currentElement.setAttribute("value","PhotFrame_"+this.frameName3);
						System.out.println("Coordsys added");
						break;
					
					case("ivoa:RealQuantity.value"):
						
						if (this.errorValue.equals("")) {
						currentElement.removeAttribute("ref");
						currentElement.setAttribute("value", "NotSet");
						}
					
						else {
							currentElement.setAttribute("ref",this.errorValue);
						}
						System.out.println("Unit ref added");
						break;
					
					case("ivoa:Quantity.unit"):
						
						currentElement.removeAttribute("ref");
					
						if (this.errorUnit.equals("")) {
							currentElement.setAttribute("value","NotSet");
						}
						
						else {
							currentElement.setAttribute("value",this.errorUnit);
						}
				}
				
				mangoComponentWalker.nextNode();//going to the next node if not one of the parameters
						
				}
				
			goToTableMapping(mangoComponentWalker);//going back to table mapping to add the good node in global walker
				
	}

	/*
	 * ----------------------------------------------------------------------------------------------
	 * These are generic methods that are to be put in a specific class later
	 * We tried this by extending TreeWalker but the method used to do this is a DocumentTraversal
	 * method, hence TreeWalker doesn't have a constructor that we can inherit and it causes troubles
	 * ----------------------------------------------------------------------------------------------
	 * */
	
	public void goToTableMapping(TreeWalker mangoWalker) {
		
		goToRoot(mangoWalker); //getting on root to parse the tree from the begginning
		
		//putting mangoWalker to the right place
		while (((Element) (mangoWalker.getCurrentNode())).getTagName()!="TABLE_MAPPING") {
			mangoWalker.nextNode();
			System.out.println(((Element) (mangoWalker.getCurrentNode())).getTagName());
		}
		
		mangoWalker.firstChild(); //we are at <INSTANCE dmrole="root"...>
		
	}
	
	public void goToCollectionParameters() {
		
		goToRoot(walker);
		
		while (this.walker.getCurrentNode()!=null) {


			String currentdmrole = ((Element)(this.walker.getCurrentNode())).getAttribute("dmrole");
				
			if (currentdmrole != null && currentdmrole.equals("mango:MangoObject.parameters")) {
				break; //we are in the right place
				}
			
			this.walker.nextNode();
			
			}
		}
	
	public void appendConfig(TreeWalker mangoWalker,Document templateDoc) {
		
		Node newImportedChild = templateDoc.importNode(mangoWalker.getCurrentNode(),true); //necessary, otherwise java doesn't know which node to modify
		Node nodeToModify = this.walker.getCurrentNode();
		nodeToModify.appendChild(newImportedChild);
		goToRoot(this.walker);

	}
	
	public void goToRoot(TreeWalker currentWalker) {
		
		currentWalker.setCurrentNode(currentWalker.getRoot());
	}
}

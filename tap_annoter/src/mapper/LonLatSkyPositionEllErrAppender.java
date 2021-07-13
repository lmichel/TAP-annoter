package mapper;

import java.io.File;
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

public class LonLatSkyPositionEllErrAppender {

	private JSONObject ourMeasure;
	private File mangoComponentFile;
	private TreeWalker walker;
	
	/*Components extracted from the json*/
	private String ucd;
	private String semantic;
	private String description;
	private String reductionStatus;
	private String frameName;
	//private String frameEquinox;
	private String frameEpoch;
	private String longitude;
	private String latitude;
	private String errMin;
	private String errMax;
	private String posAngle;
	private String angleUnit;
	private String errorUnit;
	
	/**
	 * @param json the json configuration file
	 * @param mango the mapping component file
	 * @param walker the walker we need to fill
	 */
	public LonLatSkyPositionEllErrAppender(JSONObject json, File mango,TreeWalker walker) {
		
		this.ourMeasure = json;
		this.mangoComponentFile = mango;
		this.walker = walker;
	}
	
	/**
	 * @param templateDoc needed to merge
	 * 
	 * This fonction is adding a LonLatSkyPositionEllErr to the walker in order to do the mapping
	 */
	public void AppendLonLatSkyPositionEllErr(Document templateDoc) {
		
		//we have to get the parameters in the json first
		this.getParameters();
		
		//checking globals and setting them if needed
		if (!(this.areGlobalsSet())) {
			setGlobal(templateDoc);
		}
		
		//getting the walker of mapping component to fill it
		WalkerGetter getter = new WalkerGetter(this.mangoComponentFile);
		TreeWalker mangoWalker = getter.getWalker();
		
		this.goToTableMapping(mangoWalker); //We go to table mapping
		
		this.setParameters(mangoWalker); //filling the walker with parameters
		
		this.goToCollectionParameters();//going to the right place in the walker we need to fill with mangoWalker
		
		this.appendConfig(mangoWalker, templateDoc);//merging mangoWalker in the walker we need to fill
		
		System.out.println("LonLatSkyPositionEllErr added");
		
	}

	/**
	 * This function is getting parameters from the json config file
	 */
	public void getParameters() {
		
		System.out.println("Getting parameters");
		
		this.ucd = (String) ourMeasure.get("ucd");
		System.out.println("ucd : " + ucd);
		
		this.semantic = (String) ourMeasure.get("semantic");
		System.out.println("semantic : " + semantic);
		
		this.description = (String) ourMeasure.get("description");
		System.out.println("description : " + description);
		
		this.reductionStatus = (String) ourMeasure.get("reductionStatus");
		System.out.println("reductionStatus : " + reductionStatus);
		
		JSONObject frame = (JSONObject) this.ourMeasure.get("frame");
		this.frameName = (String) frame.get("frame");
		//this.frameEquinox = (String) frame.get("equinox") (may be useful someday);
		this.frameEpoch = (String) frame.get("epoch");
		System.out.println("Got the details of the frame : "+frameName);
		
		JSONObject position = (JSONObject) this.ourMeasure.get("position");
		this.longitude = (String) position.get("longitude");
		this.latitude = (String) position.get("latitude");
		longitude = longitude.replace("@","");
		latitude = latitude.replace("@","");
		System.out.println("Got position details");
		
		JSONObject errors = (JSONObject) this.ourMeasure.get("errors");
		JSONObject randomError = (JSONObject) errors.get("random");
		JSONArray semiAxis = (JSONArray) randomError.get("semiAxis");
		this.errMin = (String) semiAxis.get(0);
		this.errMax = (String) semiAxis.get(1);
		errMin = errMin.replace("@",""); //these lines are used to remove the @ in front of the ref
		errMax = errMax.replace("@","");
		this.posAngle = (String) randomError.get("posAngle");
		posAngle = posAngle.replace("@","");
		this.angleUnit = (String) randomError.get("angleUnit");
		this.errorUnit = (String) randomError.get("errorUnit");
		System.out.println("Got all parameters from error");
	}
	
	/**
	 * @param mangoComponentWalker the mango component where we are replacing some attributes
	 * This function is setting the parameter in the walker before adding it to the whole walker
	 */
	public void setParameters(TreeWalker mangoComponentWalker) {
		
		boolean firstAxis = true; //this boolean allows us to know where we are in the component
		boolean inPosAngle = false;
		String currentdmrole = "notivoa";
		
		while (!(currentdmrole.equals("ivoa:Quantity.unit")&&inPosAngle)) {

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
					
				case("mango:stcextend.LonLatPoint.longitude"):
					currentElement.setAttribute("ref", this.longitude);
					System.out.println("Setting longitude ref");
					break;
					
				case("mango:stcextend.LonLatPoint.latitude"):
					currentElement.setAttribute("ref", this.latitude);
					System.out.println("Setting latitude ref");
					break;
				
				case("coords:Coordinate.coordSys"):
					currentElement.setAttribute("dmref", "SpaceFrame_"+this.frameName);
					System.out.println("Setting frame name");
					break;
				
				case("meas:Ellipse.posAngle"):
					inPosAngle = true;
					break;
					
			}
			//updating for errors can't be in the switch
			if (currentdmrole.equals("ivoa:RealQuantity.value") && firstAxis) {
				
				currentElement.setAttribute("ref",this.errMin);
				firstAxis = false;
				System.out.println("Setting first axis");
			}
			
			else if (currentdmrole.equals("ivoa:Quantity.unit")&& !inPosAngle) {
				
				currentElement.setAttribute("ref",this.errorUnit);
				System.out.println("Setting axis unit");
			}
			
			else if (currentdmrole.equals("ivoa:RealQuantity.value") && !firstAxis) {
				
				currentElement.setAttribute("ref",this.errMax);
				System.out.println("Setting second axis");
			}
			
			else if (currentdmrole.equals("ivoa:Quantity.unit") && inPosAngle) {
				
					currentElement.setAttribute("ref",this.angleUnit);
					System.out.println("Setting angle unit");
				}
			
			else if (currentdmrole.equals("ivoa:RealQuantity.value")&&inPosAngle) {
				
				currentElement.setAttribute("ref",this.posAngle);
				System.out.println("Setting PosAngleValue");
			}
			
			mangoComponentWalker.nextNode();
			
		}
		
		goToTableMapping(mangoComponentWalker); //going back to table mapping to have the whole encapsulation		

	}
	
	/**
	 * @param templateDoc needed for merging
	 * This function adds the global to our walker
	 */
	public void setGlobal(Document templateDoc) {
		
		FileGetter getter = new FileGetter("mango.frame."+frameName+".xml"); //getting the frame file

		try {
			File frameFile = getter.GetFile();
			WalkerGetter gettingWalker = new WalkerGetter(frameFile);
			TreeWalker frameWalker = gettingWalker.getWalker();
			
			//we want to check if the frame file has to be modified or not
			if (this.frameEpoch==null) {
				//if not, we just add it into globals
				goToRoot(walker);
				Node globals = walker.firstChild();
				Node importedNode = templateDoc.importNode(frameWalker.getRoot(), true);
				globals.appendChild(importedNode);
			}
			
			else {
				//modifying and then adding the frame into globals
				Element currentElement = (Element) frameWalker.getCurrentNode();
				String currentdmrole = currentElement.getAttribute("dmrole");
				
				while (!(currentdmrole.equals("coords:SpaceFrame.equinox"))&&currentElement!=null) {
					
					currentElement = (Element) frameWalker.getCurrentNode();
					currentdmrole = currentElement.getAttribute("dmrole");
					frameWalker.nextNode();
					
					}
				
				if (currentdmrole.equals("coords;SpaceFrame.equinox")) {
					
					currentElement.setAttribute("value", this.frameEpoch);
					
				}
				
				goToRoot(frameWalker);
				goToRoot(walker);
				Node globals = walker.firstChild();
				Node importedNode = templateDoc.importNode(frameWalker.getRoot(), true);
				globals.appendChild(importedNode);
			}
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return a boolean to know if the frame we are looking to add is already there
	 */
	private boolean areGlobalsSet() {

		goToRoot(walker);//going to root to know where we are
		Node ourGlobals = walker.firstChild();
		NodeList nodeList = ourGlobals.getChildNodes(); //this is the content of globals
		int nodeNumber = nodeList.getLength();
		
		if (nodeNumber==0) return false; //there are no globals
		
		else {
			//parsing the list to check if the global we want to add is already there or no
			for (int i=0;i<nodeNumber;i++) {
				
				Node currentNode = nodeList.item(i);
				Element currentElement = (Element) currentNode;
				String ourFrame = currentElement.getAttribute("ID");
				
				if (ourFrame.equals("SpaceFrame_"+this.frameName)) return true;
			}
		}
		System.out.println("Globals are not set, we are setting them right now !");
		return false;
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

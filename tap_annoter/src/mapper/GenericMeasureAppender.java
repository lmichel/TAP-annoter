package mapper;

import java.io.*;

import org.json.simple.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.TreeWalker;


import utils.WalkerGetter;


/**
 * @author joann
 *
 */
public class GenericMeasureAppender {
	
	private JSONObject ourMeasure;
	private File mangoComponentFile;
	private String ucd;
	private String semantic;
	private String description;
	private String reductionStatus;
	private String coordValue;
	private String coordUnit;
	private String errorValue;
	private String errorUnit;
	private TreeWalker walker;
	

	/**
	 * @param json config file
	 * @param mangoComponentFile the mapping component.xml
	 * @param walker the walker we have to fill
	 */
	public GenericMeasureAppender(JSONObject json, File mango, TreeWalker walker) {
		
		this.ourMeasure = json;
		this.mangoComponentFile = mango;
		this.walker = walker;
	}

	/**
	 * @param templateDoc needed to merge
	 * Class used to append a Generic Measure in the mapping
	 */
	public void AppendGenericMeasure(Document templateDoc) {
		
		this.getParameters(); //getting the parameters from the json file
		
		//walker for the mapping component
		WalkerGetter getter = new WalkerGetter(mangoComponentFile);
		TreeWalker mangoWalker = getter.getWalker();
		
		//going to the place where the <TABLE_MAPPING> is
		goToTableMapping(mangoWalker);
		
		//setting the parameters
		setParameters(mangoWalker);
		
		goToRoot(walker);
			
		//we are putting the walker in the place we want to fill our mapping component
		goToCollectionParameters();
		
		//filling our walker with the mangoWalker mapping component
		appendConfig(mangoWalker,templateDoc);
	}
	
	/**
	 * This method is used to get all the parameters needed to replace @@@@@ in mapping components
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
		JSONObject coordinate = (JSONObject) ourMeasure.get("coordinate");
		System.out.println("Got coordinates");
		this.coordValue = (String) coordinate.get("value");
		coordValue = coordValue.replace("@", "");
		System.out.println("value : " + coordValue);
		this.coordUnit = (String) coordinate.get("unit");
		System.out.println("unit : " + coordUnit);
		JSONObject error = (JSONObject) ourMeasure.get("errors");
		System.out.println("Got errors");
		JSONObject random = (JSONObject) error.get("random");
		System.out.println("Got random");
		this.errorValue = (String) random.get("value");
		errorValue = errorValue.replace("@", "");
		this.errorUnit = (String) random.get("unit");
		System.out.println("we got parameters");
		
	}

	/**
	 * @param mangoComponentWalker the walker of mango mapping component
	 * This method is used to replace the values of the different parameters in the mapping component
	 */
	public void setParameters(TreeWalker mangoComponentWalker) {
		
		boolean inError = false;
		String currentdmrole = "notivoa";
		
		while (!(currentdmrole.equals("ivoa:Quantity.unit"))) {

			Node currentNode = mangoComponentWalker.getCurrentNode();
			Element currentElement = (Element) currentNode;
			
			//to know in which instance we are
			currentdmrole = currentElement.getAttribute("dmrole");

			//updating values
			switch(currentdmrole) {
				case("meas:Measure.error"):
					inError = true;
					System.out.println("We have an error there");
					break;
			
				case("meas:GenericMeasure.coord"):
					inError = false;
					break;
			
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
			
				case("mango:Parameter.reductionStatus"):
					currentElement.setAttribute("value",reductionStatus);
					System.out.println("Setting reductionStatus");
					break;
			}
			
			if (currentdmrole.equals("ivoa:RealQuantity.value") && !(inError)) {
				currentElement.setAttribute("ref",coordValue);
				System.out.println("Setting coordValue");
			}
			else if (currentdmrole.equals("ivoa:RealQuantity.unit")&& !(inError)) {
				currentElement.setAttribute("ref",coordUnit);
				System.out.println("Setting coordUnit");
			}
			else if (currentdmrole.equals("ivoa:RealQuantity.value") && inError) {
				currentElement.setAttribute("ref",errorValue);
				System.out.println("Setting errorValue");
			}
			else if (currentdmrole.equals("ivoa:Quantity.unit")&& inError) {
					currentElement.setAttribute("ref",errorUnit);
					System.out.println("Setting errorUnit");

				}
			mangoComponentWalker.nextNode();
			}
		
		goToTableMapping(mangoComponentWalker); //going back to table mapping to have the whole encapsulation		
		

	}
	
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
		
		System.out.println("going to root !");
		
		while (currentWalker.getCurrentNode()!=currentWalker.getRoot()) {
			currentWalker.parentNode();
			System.out.println("Vroum");

		}
	}
}

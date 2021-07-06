package mapper;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.SAXException;

import utils.TreeWalkerMover;
import utils.WalkerGetter;


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
	private TreeWalkerMover walker;
	

	/**
	 * @param json
	 * @param mango
	 * @param walker
	 */
	public GenericMeasureAppender(JSONObject json, File mango, TreeWalkerMover walker) {
		
		this.ourMeasure = json;
		this.mangoComponentFile = mango;
		this.walker = walker;
	}

	public void AppendGenericMeasure(Document templateDoc) {
		
		this.getParameters();
		
		WalkerGetter getter = new WalkerGetter(mangoComponentFile);
		TreeWalkerMover mangoWalker = getter.getWalker();
		
		mangoWalker.goToTableMapping();
			
		setParameters(mangoWalker);
			
		mangoWalker.goToTableMapping();		
		
		walker.goToRoot();
			
		walker.goToCollectionParameters();
			
		walker.appendConfig(mangoWalker,templateDoc);
	}
	
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
		
	}

	public void setParameters(TreeWalker mangoComponentWalker) {
		
		boolean inError = false;
		String currentdmrole = "notivoa";
		while (!(currentdmrole.equals("ivoa:Quantity.unit"))) {
			//setting all parameters
			Node currentNode = mangoComponentWalker.getCurrentNode();
			
			Element currentElement = (Element) currentNode;
			currentdmrole = currentElement.getAttribute("dmrole");
				//to know in which instance we are
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
		

	}
	
}

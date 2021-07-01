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
import utils.WalkerGetter;


public class DetectionFlagAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	private String ucd;
	private String semantic;
	private String description;
	private String frame;
	private String coordValue;
	
	public DetectionFlagAppender(JSONObject json, File mango) {
		
			this.ourMeasure = json;
			this.mangoFile = mango;
	}
	
	public TreeWalker AppendDetectionFlag(BufferedWriter out, TreeWalker walker) {
		
		GetParameters();
		
		//checking globals and putting them if needed
		if (!AreGlobalsSet(walker)) {
			walker = setGlobal(walker);
		}
		
		WalkerGetter getter = new WalkerGetter(mangoFile);
		TreeWalker mangoWalker = getter.getWalker();
		mangoWalker.getRoot();
		
		while (!((((Element) mangoWalker.getCurrentNode()).getTagName()).equals("TABLE_MAPPING"))) {
			mangoWalker.nextNode(); //putting mangoWalker to the right place
		}
		
		mangoWalker.firstChild(); //we are at <INSTANCE dmrole="root" dmtype="mango:stcextend.Flag">
		
		while (mangoWalker.getCurrentNode()!=null) {
			Node currentNode = mangoWalker.nextNode();
			Element currentElement = (Element) currentNode;
			String currentdmrole = currentElement.getAttribute("dmrole");
			
			//updating values
			if (currentdmrole.equals("mango:Parameter.semantic")) currentElement.setAttribute("value", semantic);
			else if (currentdmrole.equals("mango:Parameter.ucd")) currentElement.setAttribute("value", ucd);
			else if (currentdmrole.equals("mango:Parameter.description")) currentElement.setAttribute("value", description);
			else if (currentdmrole.equals("mango:stcextend.FlagCoord.coord")) currentElement.setAttribute("ref",coordValue);
			else if (currentdmrole.equals("coords:Coordinate.coordSys")) currentElement.setAttribute("dmref","StatusFrame_"+frame);
		}
		
		mangoWalker.getRoot();
		
		while (!((((Element) mangoWalker.getCurrentNode()).getTagName()).equals("TABLE_MAPPING"))) {
			mangoWalker.nextNode(); //putting mangoWalker to the right place again
		}
		
		mangoWalker.firstChild(); //we are at <INSTANCE dmrole="root" dmtype="mango:stcextend.Flag">
		
		while (!(((Element)(walker.getCurrentNode())).getAttribute("dmrole")).equals("mango:MangoObject.parameters")) {
			walker.nextNode();
		}
		
		Node newChild = mangoWalker.getCurrentNode();
		Node nodeToModify = walker.getCurrentNode();
		nodeToModify.appendChild(newChild);
		walker.getRoot();
		return(walker);
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
	
	public boolean AreGlobalsSet(TreeWalker walker) {
		
		walker.getRoot();
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
		
		return false;
	}
	
	public TreeWalker setGlobal(TreeWalker walker) {
		
		FileGetter getter = new FileGetter("mango.frame."+frame+".xml");

		try {
			File frameFile = getter.GetFile();
			WalkerGetter gettingWalker = new WalkerGetter(frameFile);
			TreeWalker frameWalker = gettingWalker.getWalker();
		    walker.firstChild().appendChild(frameWalker.getRoot());
		    return(walker);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return null;
	    
	}
}


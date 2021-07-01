package mapper;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.*;
import org.w3c.dom.traversal.TreeWalker;

import utils.WalkerGetter;


public class GenericMeasureAppender {
	
	private JSONObject ourMeasure;
	private File mangoFile;
	private String ucd;
	private String semantic;
	private String description;
	private String reductionStatus;
	private String coordValue;
	private String coordUnit;
	private String errorValue;
	private String errorUnit;
	
	public GenericMeasureAppender(JSONObject json, File mango) {
		
		this.ourMeasure = json;
		this.mangoFile = mango;
	}

	public void AppendGenericMeasure(BufferedWriter out, TreeWalker walker) {
		
		GetParameters();
		WalkerGetter getter = new WalkerGetter(mangoFile);
		TreeWalker mangoWalker = getter.getWalker();
		mangoWalker.getRoot();
}
		
	
	public void GetParameters() {
		
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
	
}

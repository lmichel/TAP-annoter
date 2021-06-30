package mapper;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.*;


public class GenericMeasureAppender {
	
	private JSONObject ourMeasure;
	private File mangoFile;
	
	public GenericMeasureAppender(JSONObject json, File mango) {
		
		this.ourMeasure = json;
		this.mangoFile = mango;
	}

	public void AppendGenericMeasure(BufferedWriter out) {
		
		System.out.println("Getting parameters");
		String ucd = (String) ourMeasure.get("ucd");
		System.out.println("ucd : " + ucd);
		String semantic = (String) ourMeasure.get("semantic");
		System.out.println("semantic : " + semantic);
		String description = (String) ourMeasure.get("description");
		System.out.println("description : " + description);
		String reductionStatus = (String) ourMeasure.get("reductionStatus");
		System.out.println("reductionStatus : " + reductionStatus);
		JSONObject coordinate = (JSONObject) ourMeasure.get("coordinate");
		System.out.println("Got coordinates");
		System.out.println(coordinate.toString());
		String value = (String) coordinate.get("value");
		System.out.println("value : " + value);
		String unit = (String) coordinate.get("unit");
		System.out.println("unit : " + unit);
		
		
		try (BufferedReader content = new BufferedReader(new FileReader (mangoFile))) {
			
			   String line;
			   System.out.println("Writing");
			   while ((line = content.readLine()) != null) {
				   
				    Pattern pattern = Pattern.compile("@@@@@", Pattern.CASE_INSENSITIVE);
				    Matcher matcher = pattern.matcher(line);
				    boolean matchFound = matcher.find();
				    
				    if (matchFound) {
				    	StringBuilder myLine = new StringBuilder(line);
				    	myLine.replace(31, 39,"\"" + ucd + "\"");
				    	out.write(myLine.toString());
				    	out.newLine();
				    }
				   else {
					   out.write(line);
					   out.newLine();
				   }
			   }
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}

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
		String value = (String) coordinate.get("value");
		value = value.replace("@", "");
		System.out.println("value : " + value);
		String unit = (String) coordinate.get("unit");
		System.out.println("unit : " + unit);
		JSONObject error = (JSONObject) ourMeasure.get("errors");
		System.out.println("Got errors");
		JSONObject random = (JSONObject) error.get("random");
		System.out.println("Got random");
		String errorValue = (String) random.get("value");
		errorValue = errorValue.replace("@", "");
		String errorUnit = (String) random.get("unit");
		
		int count = 0;
		
		
		try (BufferedReader content = new BufferedReader(new FileReader (mangoFile))) {
			
			   String line;
			   System.out.println("Writing GenericMeasure");
			   while ((line = content.readLine()) != null) {
				   
				    Pattern pattern = Pattern.compile("@@@@@", Pattern.CASE_INSENSITIVE);
				    Matcher matcher = pattern.matcher(line);
				    boolean matchFound = matcher.find();
				    
				    if (matchFound) {
				    	int start = matcher.start();
				    	int end = matcher.end() + 1;
				    	StringBuilder myLine = new StringBuilder(line);
				    	
				    	if(count==6) myLine.replace(start,end,semantic);
				    	if (count==8) myLine.replace(start, end,ucd);
				    	if (count==10) myLine.replace(start,end, description);
				    	if (count==12) myLine.replace(start,end, reductionStatus);
				    	if (count==20) myLine.replace(start,end, value);
				    	if (count==22) myLine.replace(start,end,unit);
				    	if (count==30) myLine.replace(start, end, errorValue.equals("") ? "notSet" : errorValue);
				    	if (count==32) myLine.replace(start, end, errorUnit);
				    	
				    	out.write(myLine.toString());
				    	out.newLine();
				    }
				   else if (count != 0 && count != 1 && count != 40){
					   out.write(line);
					   out.newLine();
				   }
				   count +=1;
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

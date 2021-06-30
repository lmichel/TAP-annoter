package mapper;

import java.io.*;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.*;

import utils.FileGetter;

public class DetectionFlagAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	
	public DetectionFlagAppender(JSONObject json, File mango) {
		
			this.ourMeasure = json;
			this.mangoFile = mango;
	}
	
	public void AppendDetectionFlag(BufferedWriter out) {
		
		System.out.println("Getting parameters");
		String ucd = (String) ourMeasure.get("ucd");
		System.out.println("ucd : " + ucd);
		String semantic = (String) ourMeasure.get("semantic");
		System.out.println("semantic : " + semantic);
		String description = (String) ourMeasure.get("description");
		System.out.println("description : " + description);
		JSONObject frames = (JSONObject) ourMeasure.get("frame");
		System.out.println("Got frame");
		String frame = (String) frames.get("frame");
		System.out.println("frame : " + frame);
		JSONObject coordinate = (JSONObject) ourMeasure.get("coordinate");
		System.out.println("Got coordinate");
		String value = (String) coordinate.get("value");
		value = value.replace("@", "");
		System.out.println("value :" + value);
		
		int count = 0;
		
		try (BufferedReader content = new BufferedReader(new FileReader (mangoFile))) {
			
			   String line;

			   System.out.println("Writing");
			   
			   while ((line = content.readLine()) != null) {

				    Pattern pattern = Pattern.compile("@@@@@", Pattern.CASE_INSENSITIVE);
				    Matcher matcher = pattern.matcher(line);
				    boolean matchFound = matcher.find();
				    
				    if (matchFound) {
				    	int start = matcher.start();
				    	int end = matcher.end() + 1;
				    	StringBuilder myLine = new StringBuilder(line);
				    	
				    	if(count==7) myLine.replace(start,end,semantic);
				    	if (count==9) myLine.replace(start, end,ucd);
				    	if (count==11) myLine.replace(start,end, description);

				    	if (count==18) myLine.replace(start,end, value);
				    	if (count==20) myLine.replace(start,end,frame);
				    	
				    	out.write(myLine.toString());
				    	out.newLine();
				    }
				   else if (count != 0 && count != 1){
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

	public void AppendGlobalsDetectionFlag(BufferedWriter out) throws IOException, URISyntaxException {
		
		JSONObject frames = (JSONObject) ourMeasure.get("frame");
		System.out.println("Got frame for globals");
		String frame = (String) frames.get("frame");
		System.out.println("frame : " + frame);
		
		FileGetter getter = new FileGetter("mango.frame."+frame+".xml");
		File frameFile = getter.GetFile();
		
		try (BufferedReader content = new BufferedReader(new FileReader (frameFile))) {
			
			   String line;

			   System.out.println("Writing globals");
			   
			   while ((line = content.readLine()) != null) {
				   out.write(line);
				   out.newLine();
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


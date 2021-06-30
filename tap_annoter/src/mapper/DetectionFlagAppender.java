package mapper;

import java.io.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DetectionFlagAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	
	public DetectionFlagAppender(JSONObject json, File mango) {
		
			this.ourMeasure = json;
			this.mangoFile = mango;
	}
	
	public void AppendDetectionFlag(BufferedWriter out) {
		
		try (BufferedReader content = new BufferedReader(new FileReader (mangoFile))) {
			   String line;
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


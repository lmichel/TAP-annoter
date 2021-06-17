package tap_annoter;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import tap.TAPException;

public class ProductMapper {

	private String jsonPath;
	private BufferedWriter out;
	
	public ProductMapper(String nom_du_fichier,BufferedWriter buffer) {
		this.jsonPath = nom_du_fichier;
		this.out = buffer;
	}

	
	public void BuildAnnotations() throws IOException {
		
		JSONParser jsonP = new JSONParser();
		System.out.println("Salut");
		
		try {
			//On récupère le fichier json pour le parser
	         JSONObject jsonO = (JSONObject) jsonP.parse(new FileReader(jsonPath));
	         
	     
	         JSONArray parametersList = (JSONArray) jsonO.get("parameters");
	         
	         Iterator<?> paramIter = parametersList.iterator();
	         
	         //On regarde tout les paramètres
	         while (paramIter.hasNext()) {
	        	 
	        	 if (((JSONObject) paramIter.next()).get("measure").equals("LonLatSkyPositionEllErr")) {
	        		 out.write("LonLatSkyPositionEllErr found");
	        		 out.newLine();
	        	 }
	        	 
	        	 else if (((JSONObject) paramIter.next()).get("measure").equals("LonLatSkyPosition")) {
	        		 out.write("LonLatSkyPosition found");
	        		 out.newLine();
	        	 }
	        	 
	        	 else if (((JSONObject) paramIter.next()).get("measure").equals("Position")) {
	        		 out.write("Position found");
	        		 out.newLine();
	        	 }
	        	 
	        	 else if (((JSONObject) paramIter.next()).get("measure").equals("ProperMotion")) {
	        		 out.write("ProperMotion found");
	        		 out.newLine();
	        	 }
	        	 
	        	 else if (((JSONObject) paramIter.next()).get("measure").equals("status")) {
	        		 out.write("status found");
	        		 out.newLine();
	        	 }
	        	 
	        	 else if (((JSONObject) paramIter.next()).get("measure").equals("Photometry")) {
	        		 out.write("Photometry found");
	        		 out.newLine();
	        	 }
	        	 
	        	 else if (((JSONObject) paramIter.next()).get("measure").equals("GenericMeasure")) {
	        		 out.write("GenericMeasure found");
	        		 out.newLine();
	        	 }
	        	 
	        	 else if (((JSONObject) paramIter.next()).get("measure").equals("HardnessRatio")) {
	        		 out.write("HardnessRation found");
	        		 out.newLine();
	        	 }
	        	 
	        	 else if (((JSONObject) paramIter.next()).get("measure").equals("DetectionFlag")) {
	        		 out.write("DetectionFlag found");
	        		 out.newLine();
	        	 }
	        	 
	        	 else if (((JSONObject) paramIter.next()).get("measure").equals("MJD")) {
	        		 out.write("MJD found");
	        		 out.newLine();
	        	 }
	        	 
	        	 else paramIter.next();
	         }
	         
	      } catch (FileNotFoundException e) {
	    	  System.out.println("ou est le fichier ?");
	         e.printStackTrace();
	      } catch (IOException e) {
	    	  System.out.println("oh non, une IO exception !");
	         e.printStackTrace();
	      } catch (ParseException e) {
	    	  System.out.println("une parse exception !");
	         e.printStackTrace();
	      } finally { 
			out.write("</VODML>");
			out.newLine();
			}
		
	}
	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}
	
}

	